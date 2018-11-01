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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiamusi.HousingJiaMuSiHtml;

import app.common.WebParam;
import app.parser.HousingJiaMuSiParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jiamusi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jiamusi")
public class HousingJiaMuSiService extends HousingBasicService implements ICrawler, ICrawlerLogin{

	@Autowired
	private HousingJiaMuSiParser housingJiaMuSiParser;
	@Autowired
	private GetDataService getDataService;

	@Override
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("crawler.housingFund.login.start", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		try {
			WebParam webParam = housingJiaMuSiParser.login(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtmlPage()){
				DomElement element = webParam.getHtmlPage().getElementById("error");
				if(null != element){				//登陆失败
					tracer.addTag("crawler.housingFund.login.ERROR", element.asText());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(element.asText().trim());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(104);
					save(taskHousing); 
				}else if(webParam.getHtmlPage().asXml().contains("网上大厅")){			//登录成功
					tracer.addTag("crawler.housingFund.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
					
					HousingJiaMuSiHtml html = new HousingJiaMuSiHtml();
					html.setUrl(webParam.getUrl());
					html.setPageCount(1);
					html.setTaskid(taskHousing.getTaskid());
					html.setType("loginedPage");
					html.setHtml(webParam.getHtmlPage().asXml());
				}else {
					tracer.addTag("crawler.housingFund.login.ERROR2", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(104);
					save(taskHousing);
				}
			}else{
				tracer.addTag("crawler.housingFund.login.TimeOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("crawler.housingFund.login.TimeOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setPaymentStatus(104);
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