package app.service;

import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.bean.WebParamHousing;
import app.crawler.domain.Cookies;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.quanzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.quanzhou")
public class HousingQuanZhouFutureService extends HousingBasicService implements ICrawlerLogin,ICrawler{
	
	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingQuanZhouService housingQuanZhouService;
//    Cookies cook = new Cookies();
	@Async
    @Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
    	WebParamHousing webParamHousing = null;
    	TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
//		HtmlPage htmlpage = null;
		try {
			webParamHousing =  loginAndGetService.loginByIDCard(messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆异常", e.getMessage());
			save(taskHousing);
			return taskHousing;
	    }
		
		if(webParamHousing!=null){
			tracer.addTag("登陆html", webParamHousing.getHtml());
			if (webParamHousing.getHtml().toString().contains("账户明细")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setCookies(webParamHousing.getPagnum());
				save(taskHousing);
				tracer.addTag("登陆成功", messageLoginForHousing.getTask_id());
//				taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
//				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
//				save(taskHousing);
//				updateTaskHousing(taskHousing.getTaskid());
				return taskHousing;
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				tracer.addTag("登陆失败", messageLoginForHousing.getTask_id());
				return taskHousing;
			}
		}else{
			tracer.addTag("登陆html", null);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			save(taskHousing);
			return taskHousing;
		}
		
	}
	
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		Set<Cookie> cookies = Cookies.transferJsonToSet(taskHousing.getCookies());
		WebClient webClient = Cookies.getWebClient(cookies);
		try {
			Future<String> future = housingQuanZhouService.getResult(messageLoginForHousing, taskHousing,
					webClient);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("爬取异常", e.getMessage());
			save(taskHousing);
		}
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
//	public Object code(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
//		Set<Cookie> cookies = Cookies.transferJsonToSet(taskHousing.getCookies());
//		WebClient webClient = Cookies.getWebClient(cookies);
//		WebParamHousing webParamHousing = null;
//		try {
//			webParamHousing =  loginAndGetService.loginByCard(webClient);
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//			tracer.addTag("parser.login.auth", e.getMessage());
//			save(taskHousing);
//			return null;
//	    }
//		if (webParamHousing.getHtml().toString().contains("获取短信验证码（")){
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
//			String cookies1 = Cookies.transcookieToJson(webParamHousing.getWebClient());
//			taskHousing.setCookies(cookies1);;
//			save(taskHousing);
//			return taskHousing;
//		}else{
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//			
//			save(taskHousing);
//			return null;
//		}
//		
//		
//		
//		
//	}
//	
//	
//	public Object crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
//		Set<Cookie> cookies = Cookies.transferJsonToSet(taskHousing.getCookies());
//		WebClient webClient = Cookies.getWebClient(cookies);
//		WebParamHousing webParamHousing = null;
//		try {
//			webParamHousing =  loginAndGetService.loginCard(webClient, messageLoginForHousing.getSms_code().trim(),cook.getHtmlpage());
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
//			tracer.addTag("parser.login.auth", e.getMessage());
//			save(taskHousing);
//			return null;
//	    }
//		if (webParamHousing.getHtml().toString().contains("账户明细")){
//			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
//			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
//			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
//			save(taskHousing);
//			Future<String> future = housingQuanZhouService.getResult(messageLoginForHousing, taskHousing, webParamHousing.getWebClient());
//			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
//			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
//			save(taskHousing);
//			updateTaskHousing(taskHousing.getTaskid());
//			return taskHousing;
//		}else{
//			taskHousing.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getDescription());
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_Five.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_Five.getMessage());
//			save(taskHousing);
//			return taskHousing;
//		}
//		
//		
//	}
	
}
