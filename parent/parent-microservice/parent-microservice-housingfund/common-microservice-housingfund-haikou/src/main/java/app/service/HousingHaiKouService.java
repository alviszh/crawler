package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.haikou.HousingHaiKouHtml;
import com.microservice.dao.repository.crawler.housing.haikou.HousingHaiKouHtmlRepository;

import app.common.WebParam;
import app.parser.HousingHaiKouParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ISms;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.haikou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.haikou")
public class HousingHaiKouService extends HousingBasicService implements ISms, ICrawler{

	@Autowired
	private HousingHaiKouHtmlRepository housingHaiKouHtmlRepository;
	@Autowired
	private HousingHaiKouParser housingHaiKouParser;
	@Autowired
	private GetDataService getDataService;

	@Override
	@Async
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("housing.haikou.service.sendSMS.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingHaiKouParser.sendSMS(messageLoginForHousing);
			HousingHaiKouHtml html = new HousingHaiKouHtml();
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("sendSMS");
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			housingHaiKouHtmlRepository.save(html);
			if(null != webParam.getHtml()){	//短信发送不成功
				tracer.addTag("housing.haikou.service.sendSMS.fail", webParam.getHtml());
		        taskHousing.setDescription(webParam.getHtml());
		    	taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
		    	taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
		    	save(taskHousing);
			}else{
				tracer.addTag("housing.haikou.service.sendSMS.success", "短信发送成功");
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getDescription());
		    	taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
		    	taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());

		        String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
		        taskHousing.setCookies(cookies);
		        save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("housing.haikou.service.sendSMS.Exception", e.toString());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getDescription());
	    	taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
	    	taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
	    	save(taskHousing);
		}
		return taskHousing;
	}

	@Override
	@Async
	public TaskHousing verifySms(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("housing.haikou.service.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingHaiKouParser.login(messageLoginForHousing, taskHousing);
			HousingHaiKouHtml html = new HousingHaiKouHtml();
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("login");
			html.setHtml(webParam.getHtmlPage().asXml());
			housingHaiKouHtmlRepository.save(html);
			if(null != webParam.getHtml()){		//登陆失败
				tracer.addTag("housing.haikou.service.login.fail", webParam.getHtml());
		        taskHousing.setDescription(webParam.getHtml());
		    	taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
		    	taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
		    	save(taskHousing);
			}else{
				tracer.addTag("housing.haikou.service.login.success", "登录成功");
				taskHousing = changeLoginStatusSuccess(taskHousing, webParam.getWebClient());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("housing.haikou.service.login.Exception", e.toString());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
	    	taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
	    	taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
	    	save(taskHousing);
		}
		return taskHousing;
	}

	@Override
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("housing.haikou.service.getData.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
    	
    	getDataService.getUserInfo(messageLoginForHousing, taskHousing);
    	getDataService.getPayInfo(messageLoginForHousing, taskHousing);
		
    	taskHousing = findTaskHousing(taskHousing.getTaskid());
    	return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}