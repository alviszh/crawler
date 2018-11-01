package app.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.htmlunit.TelecomXinJiangHtmlUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.xinjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.xinjiang")
public class TelecomXinJiangSmsService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomXinJiangHtmlUnit  telecomXinJiangHtmlUnit;
	@Autowired
	private TelecomXinJiangService telecomXinJiangService;
	@Autowired
	private TracerLog tracer;
	// 获取手机验证码
//	@Async
//	public void getPhoneCode(MessageLogin mssageLogin) {
//	    TaskMobile taskMobile=telecomCommonService.findtaskMobile(mssageLogin.getTask_id().trim());
//		tracer.addTag("getPhoneCode", taskMobile.getTaskid());
//		try {	
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
//			// 发送验证码状态更新
//			taskMobileRepository.save(taskMobile);
//			String smsCode = telecomXinJiangHtmlUnit.getphonecode(mssageLogin,taskMobile);
//			tracer.addTag("验证码发送返回字符串",smsCode);
//			if("1".equals(smsCode)){
//				tracer.addTag(mssageLogin.getTask_id(),"验证码发送成功");
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
//				// 发送验证码状态更新
//				taskMobileRepository.save(taskMobile);
//				telecomXinJiangService.getAccountInfo(mssageLogin, taskMobile);
//			}else{
//				tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
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
//	// 手机短信验证码验证
//	@Async
//	public void setPhoneCode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {		
//		tracer.addTag("setPhoneCode", mssageLogin.getTask_id());
//		// 手机验证码验证状态更新
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
//		taskMobileRepository.save(taskMobile);
//		WebParam  webParam = telecomXinJiangHtmlUnit.setphonecode(mssageLogin, taskMobile);	
//		String smsResult=webParam.getSmsResult();
//		tracer.addTag("setPhoneCode",smsResult);	
//		if (smsResult !=null && smsResult.contains("success")) {
//			JSONObject jsonInsurObjs = JSONObject.fromObject(webParam.getSmsResult());
//		    String success = jsonInsurObjs.getString("success");		
//		 	tracer.addTag("setPhoneCode success",success);	
//		     if("true".equals(success)){
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
//			  tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证失败！");		
//			  taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
//			  taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
//			  taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
//			  taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//			  taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
//			  // 手机验证码验证失败状态更新
//			 taskMobileRepository.save(taskMobile);
//		}
//	}
}
