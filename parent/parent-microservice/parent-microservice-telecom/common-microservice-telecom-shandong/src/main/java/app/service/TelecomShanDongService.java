package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomShanDongParser;
import app.service.aop.ISms;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shandong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shandong")
public class TelecomShanDongService implements ISms{

	@Autowired
	private ShanDongGetDataService shanDongGetDataService;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TelecomShanDongParser telecomShanDongParser;
	@Autowired
	private TracerLog tracer;
	
	//发送短信验证码
	@Override
	@Async
	public TaskMobile sendSms(MessageLogin messageLogin){
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.sendSms", taskMobile.getTaskid());
		WebParam webParam = telecomShanDongParser.sendSms(taskMobile);
		if(null != webParam && null != webParam.getHtmlPage() && webParam.getHtmlPage().asXml().contains("短信随机密码已发送到您的手机")){
			String imgcode = webParam.getHtml();
			String cookies = CommonUnit.transcookieToJson(webParam.getHtmlPage().getWebClient());
			taskMobile.setCookies(cookies);
			taskMobile.setNexturl(imgcode);//将发送短信验证码的图形验证码入库，以备在验证短信验证码时使用
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
			telecomCommonService.save(taskMobile);
			tracer.addTag("parser.telecom.crawler.sendSms.status.success", "短信验证码发送成功");
		}else{
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			telecomCommonService.save(taskMobile);
			tracer.addTag("parser.telecom.crawler.sendSms.status.error", "短信验证码发送失败");
		}
		return taskMobile;
	}
	
	//验证短信验证码
	@Override
	@Async
	public TaskMobile verifySms(MessageLogin messageLogin){
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.setSmscode", taskMobile.getTaskid());
		try {
			WebParam webParam = telecomShanDongParser.setSmscode(taskMobile,messageLogin);
			if(null != webParam.getPage()){
				String string = webParam.getPage().getWebResponse().getContentAsString();
				if(string.contains("0")){			//验证成功
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setCookies(cookies);
					tracer.addTag("parser.telecom.crawler.setSmscode.succsess", "验证成功");
				}else{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					if(string.contains("3")){
						taskMobile.setDescription("短信随机码超时，请重新操作");
						tracer.addTag("parser.telecom.crawler.setSmscode.error", "短信随机码超时，请重新操作");
					}else if(string.contains("4")){
						taskMobile.setDescription("短信随机码和手机号码不匹配");
						tracer.addTag("parser.telecom.crawler.setSmscode.error", "短信随机码和手机号码不匹配");
					}else if(string.contains("5")){
						taskMobile.setDescription("短信随机码校验未通过");
						tracer.addTag("parser.telecom.crawler.setSmscode.error", "短信随机码校验未通过");
					}else if(string.contains("6")){
//					taskMobile.setFinished(true);
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
						taskMobile.setDescription("您的用户信息校验未通过，请确认后重新输入机主信息");
						tracer.addTag("parser.telecom.crawler.setSmscode.error", "您的用户信息校验未通过，请确认后重新输入机主信息");
					}else{
						taskMobile.setDescription("验证未通过");
						tracer.addTag("parser.telecom.crawler.setSmscode.error", "身份认证未通过");
					}
				}
			}else{
				taskMobile.setFinished(true);
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
				taskMobile.setDescription("网络异常，身份认证未通过");
				tracer.addTag("parser.telecom.crawler.setSmscode.error", "网络异常，身份认证未通过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.setSmscode.Exception", e.getMessage());
			taskMobile.setFinished(true);
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
			taskMobile.setDescription("网络异常，身份认证未通过");
		}
		telecomCommonService.save(taskMobile);
		return taskMobile;
	}
	
	
	public TaskMobile getAllData(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		
		taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		taskMobile.setFamilyMsgStatus(201);
		taskMobile.setIntegralMsgStatus(201);
		telecomCommonService.save(taskMobile);
		
		taskMobile = shanDongGetDataService.getAllData(messageLogin);
		
		return taskMobile;
	}

}
