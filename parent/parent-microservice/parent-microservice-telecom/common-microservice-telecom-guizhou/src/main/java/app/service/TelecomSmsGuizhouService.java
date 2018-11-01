package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.htmlunit.TelecomGuizhouSmsUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guizhou")
public class TelecomSmsGuizhouService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private CrawlerStatusMobileService  crawlerStatusMobileService;
	@Autowired
	private TelecomGuizhouSmsUnit  telecomGuizhouSmsUnit;
	// 获取手机验证码
//    @Async
//	public void getPhoneCode(MessageLogin mssageLogin) {
//	    TaskMobile taskMobile=null;
//		try {
//		    taskMobile= telecomComService.findtaskMobile(mssageLogin.getTask_id().trim());
//			tracer.addTag("getPhoneCode", taskMobile.getTaskid());
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
//			// 发送验证码状态更新
//			taskMobileRepository.save(taskMobile);
//			WebParam webParam = telecomGuizhouSmsUnit.getphonecode(mssageLogin,taskMobile);
//			String smsResult=webParam.getSmsResult();
//			System.out.println("电信获取验证码发送结果"+smsResult);
//			tracer.addTag("电信获取验证码发送结果",smsResult);
//		    if("1".equals(smsResult)){		    	
//		    	tracer.addTag(mssageLogin.getTask_id(),"验证码发送成功");
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
//				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
//				taskMobile.setCookies(cookies);
//				// 发送验证码成功状态更新
//				taskMobileRepository.save(taskMobile);		 
//			}else{
//			   	tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//				// 发送验证码状态更新
//				taskMobileRepository.save(taskMobile);
//			}
//		} catch (Exception e) {
//			tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
//			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//			// 发送验证码状态更新
//			taskMobileRepository.save(taskMobile);
//		}
//	}
	// 手机短信验证码验证
//	@Async
//	public void setPhoneCode(MessageLogin mssageLogin) throws Exception {		
//		tracer.addTag("setPhoneCode", mssageLogin.getTask_id());
//		TaskMobile taskMobile= telecomComService.findtaskMobile(mssageLogin.getTask_id().trim());
//		// 手机验证码验证状态更新
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
//		taskMobileRepository.save(taskMobile);	
//		
//		WebParam  webParam = telecomGuizhouSmsUnit.setphonecode(mssageLogin, taskMobile);
//		System.out.println("电信获取验证码验证结果"+webParam.getSmsResult());
//		tracer.addTag("setPhoneCode",webParam.getSmsResult());	
//		if (null !=webParam.getSmsResult()  && webParam.getSmsResult().contains("OperationStatus")) {			
//			 JSONObject jsonInsurObjs = JSONObject.fromObject(webParam.getSmsResult());
//		     String operationStatus = jsonInsurObjs.getString("OperationStatus");			
//		     if("0".equals(operationStatus)){
//				tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证成功！");		
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
//				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
//                taskMobile.setCookies(cookies);           
//				// 手机验证码验证成功状态更新
//				taskMobileRepository.save(taskMobile);				
//			}else{
//			    tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证失败！");		
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
//				// 手机验证码验证失败状态更新
//				taskMobileRepository.save(taskMobile);
//			}
//		}else{
//			    tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证失败！");		
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
//				// 手机验证码验证失败状态更新
//				taskMobileRepository.save(taskMobile);
//		}
//	   
//	}

}
