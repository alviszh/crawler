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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangHtml;
import com.microservice.dao.entity.crawler.housing.yinchuan.HousingYinChuanHtml;
import com.microservice.dao.repository.crawler.housing.weifang.HousingWeiFangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yinchuan.HousingYinChuanHtmlRepository;

import app.common.WebParam;
import app.parser.HousingYinChuanParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ISms;
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yinchuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yinchuan")
public class HousingYinChuanService extends HousingBasicService implements ISms, ICrawler{

	@Autowired
	private HousingYinChuanHtmlRepository housingYinChuanHtmlRepository;
	@Autowired
	private HousingYinChuanParser housingYinChuanParser;
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
			WebParam webParam = housingYinChuanParser.login(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				HousingYinChuanHtml html = new HousingYinChuanHtml();
				html.setUrl(webParam.getUrl());
				html.setType("sendSms");
				html.setPageCount(1);
				html.setTaskid(taskHousing.getTaskid());
				html.setHtml(webParam.getHtml());
				housingYinChuanHtmlRepository.save(html);
				String[] strings = webParam.getHtml().split(",");
				if(webParam.getHtml().contains("success")){
					tracer.addTag("crawler.housingFund.login.sendSms.succcess", webParam.getHtml());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());
					taskHousing.setDescription("短信已成功发送到"+strings[3]+"请注意查收！");
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					save(taskHousing);
				}else{
					tracer.addTag("crawler.housingFund.login.sendSms.fail", webParam.getHtml());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(strings[5]);
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
			WebParam webParam = housingYinChuanParser.loginCombo(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				//登录失败
				if(null != webParam.getParam()){
					tracer.addTag("crawler.housingFund.loginCombo.fail", webParam.getParam());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(webParam.getParam());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}else if(webParam.getPage().getWebResponse().getContentAsString().contains("账号查询结果")){	//登陆成功
					tracer.addTag("crawler.housingFund.loginCombo.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					save(taskHousing);
					HousingYinChuanHtml html = new HousingYinChuanHtml();
					html.setUrl(webParam.getUrl());
					html.setType("logined");
					html.setPageCount(1);
					html.setTaskid(taskHousing.getTaskid());
					html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					housingYinChuanHtmlRepository.save(html);
				}else{
					tracer.addTag("crawler.housingFund.loginCombo.fail2", webParam.getParam());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription("登陆失败，请您重新登录");
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
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
		updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}


	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
