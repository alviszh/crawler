package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.htmlunit.TelecomHubeiSmsUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomSmsHubeiService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHubeiSmsUnit  telecomHubeiSmsUnit;
	@Autowired
	private TracerLog tracer;
//	 //获取手机验证码
//    @Async
//	public void getPhoneCode(MessageLogin mssageLogin) {
//    	tracer.addTag("getPhoneCode", mssageLogin.getTask_id());		
//	    TaskMobile taskMobile=null;
//		try {
//		    taskMobile= telecomComService.findtaskMobile(mssageLogin.getTask_id().trim());	    
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
//			// 发送验证码状态更新
//			taskMobileRepository.save(taskMobile);
//		    int count=0;		    
//		    WebParam webParam2=telecomHubeiSmsUnit.getCityCode(mssageLogin, taskMobile, count);
//		    String areaCode="";
//		    if (null !=webParam2 && null !=webParam2.getAreaCode() && webParam2.isLogin.equals("true")) {
//		    	areaCode=webParam2.getAreaCode();
//			}else{
//				areaCode=taskMobile.getAreacode();
//				if ("027".equals(areaCode)) {
//					areaCode="0127";
//				}
//			}
//			tracer.addTag("getPhoneCode areaCode", areaCode);			
//			WebParam webParam = telecomHubeiSmsUnit.getphonecode(mssageLogin,taskMobile,areaCode);
//			System.out.println("电信获取验证码发送结果"+webParam.getSmsResult());
//			tracer.addTag("电信获取验证码发送结果",webParam.getSmsResult());
//		    if(webParam.getSmsResult().contains("随机验证码已经发送")){		    	
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
//			}else if(webParam.getSmsResult().contains("您获取随机码操作过于频繁")){		    	
//		    	tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败 获取随机码操作过于频繁");
//		    	taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//				taskMobile.setDescription("获取随机码操作过于频繁");
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//				taskMobileRepository.save(taskMobile);	
//			}else if(webParam.getSmsResult().contains("请选择正确的地市")){		    	
//		    	tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败 请选择正确的地市");
//		    	taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//				taskMobile.setDescription("手机号码对应的城市不正确");
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
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
//			e.printStackTrace();
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
//	// 手机短信验证码验证
//	@Async
//	public void setPhoneCode(MessageLogin mssageLogin) throws Exception {		
//		tracer.addTag("setPhoneCode", mssageLogin.getTask_id());
//		TaskMobile taskMobile= telecomComService.findtaskMobile(mssageLogin.getTask_id().trim());
//		// 手机验证码验证状态更新
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
//		taskMobileRepository.save(taskMobile);			
//		WebParam  webParam = telecomHubeiSmsUnit.setphonecode(mssageLogin, taskMobile);
//		tracer.addTag("setPhoneCode",webParam.getSmsResult());	
//		if (null !=webParam.getSmsResult()) {			
//		     if("1".equals(webParam.getSmsResult()) || "NO0".equals(webParam.getSmsResult())){
//		    	System.out.println("手机验证码验证成功！");
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
//	}

}
