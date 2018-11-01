package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangHtml;
import com.microservice.dao.repository.crawler.housing.weifang.HousingWeiFangHtmlRepository;

import app.common.WebParam;
import app.parser.HousingWeiFangParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ISms;
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.weifang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.weifang")
public class HousingWeiFangService extends HousingBasicService implements ISms, ICrawler{

	@Autowired
	private HousingWeiFangHtmlRepository housingWeiFangHtmlRepository;
	@Autowired
	private HousingWeiFangParser housingWeiFangParser;
	@Autowired
	private GetDataService getDataService;
	
	@Override
	@Async
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("crawler.housingFund.login.start", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		try {
			WebParam webParam = housingWeiFangParser.login(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				HousingWeiFangHtml html = new HousingWeiFangHtml();
				html.setUrl(webParam.getUrl());
				html.setType("sendSms");
				html.setPageCount(1);
				html.setTaskid(taskHousing.getTaskid());
				html.setHtml(webParam.getHtml());
				housingWeiFangHtmlRepository.save(html);
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(webParam.getHtml()); // 创建JsonObject对象
				String info = object.get("info").getAsString();
				if(webParam.getHtml().contains("true")){
					tracer.addTag("crawler.housingFund.login.sendSms.succcess", info);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());
					taskHousing.setDescription(info);
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					save(taskHousing);
				}else{
					tracer.addTag("crawler.housingFund.login.sendSms.fail", info);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(info);
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing); 
				}
			}else{
				tracer.addTag("crawler.housingFund.login.TimeOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("网络异常，请核对信息及其网络");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("crawler.housingFund.login.TimeOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("网络异常，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			e.printStackTrace();
		}
		return taskHousing;
	}


	@Override
	@Async
	public TaskHousing verifySms(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("crawler.housingFund.loginCombo.start", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingWeiFangParser.loginCombo(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				//登录失败
				if(webParam.getHtml().contains("http://www.wfgjj.gov.cn/personal/personLogin_new.jspx")){
					String loginedHtml = webParam.getPage().getWebResponse().getContentAsString();
					int i = loginedHtml.indexOf("alert(");
					int j = loginedHtml.indexOf(")", i);
					String info = loginedHtml.substring(i+7, j-1);
					tracer.addTag("crawler.housingFund.loginCombo.fail", info);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(info);
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}else if(webParam.getPage().getWebResponse().getContentAsString().contains("职工姓名")){	//登陆成功
					tracer.addTag("crawler.housingFund.loginCombo.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					save(taskHousing);
					HousingWeiFangHtml html = new HousingWeiFangHtml();
					html.setUrl(webParam.getUrl());
					html.setType("logined");
					html.setPageCount(1);
					html.setTaskid(taskHousing.getTaskid());
					html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					housingWeiFangHtmlRepository.save(html);
				}
			}else {
				tracer.addTag("crawler.housingFund.loginCombo.TimeOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("网络异常，请核对信息及其网络");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("crawler.housingFund.loginCombo.TimeOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("网络异常，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			e.printStackTrace();
		}
		return taskHousing;
	}


	@Override
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		getDataService.getUserInfo(messageLoginForHousing, taskHousing);
		getDataService.getTrans(messageLoginForHousing, taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}


	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
