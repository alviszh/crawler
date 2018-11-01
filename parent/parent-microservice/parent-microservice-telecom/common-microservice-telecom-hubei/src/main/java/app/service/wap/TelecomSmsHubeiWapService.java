package app.service.wap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WapParam;
import app.htmlunit.wap.TelecomHubeiWapSmsUnit;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomSmsHubeiWapService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHubeiWapSmsUnit  telecomHubeiWapSmsUnit;
	@Autowired
	private TelecomGetAllDataHubeiWapService  telecomGetAllDataHubeiWapService;
	@Autowired
	private TracerLog tracer;
	 //获取手机验证码 
	public TaskMobile getPhoneCode(MessageLogin mssageLogin) {
    	tracer.addTag("TelecomSmsHubeiWapService.getPhoneCode", mssageLogin.getTask_id());		
	    TaskMobile taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		try {
			// 发送验证码状态更新				
			WapParam webParam = telecomHubeiWapSmsUnit.getphonecode(mssageLogin,taskMobile);
			System.out.println("TelecomSmsHubeiWapService.getPhoneCode.smsResult"+webParam.getSmsResult());
			tracer.addTag("电信获取验证码发送结果",webParam.getSmsResult());					
		    if(webParam.isState()){		    	
		    	tracer.addTag(mssageLogin.getTask_id(),"验证码发送成功");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskMobile.setCookies(cookies);
				// 发送验证码成功状态更新
				taskMobileRepository.save(taskMobile);		
			}else{
			   	tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomSmsHubeiWapService.getPhoneCode.Exception",e.toString());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
		}
		taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		return taskMobile;
	}

	public TaskMobile setPhoneCode(MessageLogin mssageLogin) {		
		tracer.addTag("setPhoneCode", mssageLogin.getTask_id());
		TaskMobile taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());		
		try {
			WapParam webParam = telecomHubeiWapSmsUnit.setphonecode(mssageLogin, taskMobile);
			tracer.addTag("setPhoneCode",webParam.getSmsResult());	
			tracer.addTag("电信获取验证码发送结果",webParam.getSmsResult());			
		     if(webParam.isState()){
		    	System.out.println("手机验证码验证成功！");
				tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证成功！");		
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());		
				System.out.println("setPhoneCode cookies====="+cookies);
                taskMobile.setCookies(cookies);           
				// 手机验证码验证成功状态更新
				taskMobileRepository.save(taskMobile);				
			}else{
			    tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证失败！");		
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
				// 手机验证码验证失败状态更新
				taskMobileRepository.save(taskMobile);
			}
		   
		} catch (Exception e) {
			tracer.addTag("TelecomSmsHubeiWapService.setPhoneCode.Exception",e.toString());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
			// 手机验证码验证失败状态更新
			taskMobileRepository.save(taskMobile);
			e.printStackTrace();
		}
		taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		return taskMobile;
	}

	public TaskMobile getPhoneCodeTwo(MessageLogin mssageLogin) {
    	tracer.addTag("getPhoneCode", mssageLogin.getTask_id());		
	    TaskMobile taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		try {
			// 发送验证码状态更新		
			int count=0;
			WapParam webParam = telecomHubeiWapSmsUnit.getphonecodeTwo(mssageLogin,taskMobile,count);
			System.out.println("TelecomSmsHubeiWapService.getPhoneCode.smsResult"+webParam.getSmsResult());
			tracer.addTag("电信获取验证码发送结果",webParam.getSmsResult());		
		    if(webParam.isState()){		    	
		    	tracer.addTag(mssageLogin.getTask_id(),"验证码发送成功");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getError_code());
				taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getDescription());
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskMobile.setCookies(cookies);
				// 发送验证码成功状态更新
				taskMobileRepository.save(taskMobile);			
			}else{
			   	tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getError_code());
				taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getDescription());
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
			}
		} catch (Exception e) {
			tracer.addTag("TelecomSmsHubeiWapService.getPhoneCodeTwo.Exception",e.toString());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getError_code());
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getDescription());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
			e.printStackTrace();			
		}
		taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		return taskMobile;
	}

	public TaskMobile setPhoneCodeTwo(MessageLogin mssageLogin)  {		
		tracer.addTag("setPhoneCode", mssageLogin.getTask_id());
		TaskMobile taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());	
		try {
			int count=0;
			WapParam webParam = telecomHubeiWapSmsUnit.setphonecodeTwo(mssageLogin, taskMobile,count);
			tracer.addTag("setPhoneCodeTwo",webParam.getSmsResult());	
			tracer.addTag("电信获取验证码发送结果",webParam.getSmsResult());					
			if (null !=webParam.getSmsResult()) {
				String flag="";
				String result="";
				if (webParam.getSmsResult().contains("flag") && webParam.getSmsResult().contains("result")) {
					JSONObject jsonInsurObjs = JSONObject.fromObject(webParam.getSmsResult());
					 flag=jsonInsurObjs.getString("flag");
					 result=jsonInsurObjs.getString("result");	
				}			
			     if("1".equals(flag) && "0".equals(result)){
			    	System.out.println("手机验证码第二次验证成功！");
					tracer.addTag(mssageLogin.getTask_id(), "手机验证码setPhoneCodeTwo验证成功！");		
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
					taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
	                taskMobile.setCookies(cookies);           
	            	tracer.addTag("手机验证码第二次验证成功通过 cookies为",cookies);		
					// 手机验证码验证成功状态更新
					taskMobileRepository.save(taskMobile);	
					taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
					taskMobile.setFamilyMsgStatus(201);
					taskMobile.setAccountMsgStatus(201);
					taskMobile.setSmsRecordStatus(201);
					taskMobileRepository.save(taskMobile);	
					telecomGetAllDataHubeiWapService.getAllData(mssageLogin);
				}else{
				    tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证失败！");		
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
					// 手机验证码验证失败状态更新
					taskMobileRepository.save(taskMobile);
				}
			}else{
				    tracer.addTag(mssageLogin.getTask_id(), "手机验证码验证失败！");		
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getError_code());
					taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getDescription());			
					// 手机验证码验证失败状态更新
					taskMobileRepository.save(taskMobile);
			}	   
		} catch (Exception e) {
			tracer.addTag("TelecomSmsHubeiWapService.setPhoneCodeTwo.Exception",e.toString());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getError_code());
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getDescription());	
			// 手机验证码验证失败状态更新
			taskMobileRepository.save(taskMobile);
			e.printStackTrace();
		}
		taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		return taskMobile;
	}
}
