package app.service;

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
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomHubeiSmsUnit;
import app.service.aop.ICrawler;
import app.service.aop.ISms;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomHubeiCrawlerService implements ISms,ICrawler{

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHubeiSmsUnit telecomHubeiSmsUnit;
	@Autowired
	private TelecomGetAllDataHubeiService  telecomGetAllDataHubeiService;
	@Autowired 
	private TracerLog tracer;
	
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
	   TaskMobile taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id());
	   tracer.addTag("sendSms", taskMobile.getTaskid());
		try {  
		    int count=0;		    
		    WebParam webParam2=telecomHubeiSmsUnit.getCityCode(messageLogin, taskMobile, count);
		    String areaCode="";
		    if (null !=webParam2 && null !=webParam2.getAreaCode() && webParam2.isLogin.equals("true")) {
		    	areaCode=webParam2.getAreaCode();
			}else{
				areaCode=taskMobile.getAreacode();
				if ("027".equals(areaCode)) {
					areaCode="0127";
				}
			}
			tracer.addTag("getPhoneCode areaCode", areaCode);			
			WebParam webParam = telecomHubeiSmsUnit.getphonecode(messageLogin,taskMobile,areaCode);
			tracer.addTag("电信获取验证码发送结果",webParam.getSmsResult());
		    if(webParam.getSmsResult().contains("随机验证码已经发送")){		    	
		    	tracer.addTag(messageLogin.getTask_id(),"验证码发送成功");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskMobile.setCookies(cookies);
				// 发送验证码成功状态更新
				taskMobileRepository.save(taskMobile);		
			}else if(webParam.getSmsResult().contains("您获取随机码操作过于频繁")){		    	
		    	tracer.addTag(messageLogin.getTask_id(),"验证码发送失败 获取随机码操作过于频繁");
		    	taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("获取随机码操作过于频繁");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				taskMobileRepository.save(taskMobile);	
			}else if(webParam.getSmsResult().contains("请选择正确的地市")){		    	
		    	tracer.addTag(messageLogin.getTask_id(),"验证码发送失败 请选择正确的地市");
		    	taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("手机号码对应的城市不正确");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				taskMobileRepository.save(taskMobile);	
			}else{
			   	tracer.addTag(messageLogin.getTask_id(),"验证码发送失败");
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
			tracer.addTag(messageLogin.getTask_id(),"验证码发送失败");
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
		}	
		return taskMobile;
	}

	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		tracer.addTag("setPhoneCode", messageLogin.getTask_id());
		TaskMobile taskMobile= taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			WebParam webParam = telecomHubeiSmsUnit.setphonecode(messageLogin, taskMobile);
			tracer.addTag("setPhoneCode",webParam.getSmsResult());	
			if (null !=webParam.getSmsResult()) {			
			     if("1".equals(webParam.getSmsResult()) || "NO0".equals(webParam.getSmsResult())){
			    	System.out.println("手机验证码验证成功！");
					tracer.addTag(messageLogin.getTask_id(), "手机验证码验证成功！");		
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
	                taskMobile.setCookies(cookies);           
					// 手机验证码验证成功状态更新
					taskMobileRepository.save(taskMobile);				
				}else{
				    tracer.addTag(messageLogin.getTask_id(), "手机验证码验证失败！");		
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
					// 手机验证码验证失败状态更新
					taskMobileRepository.save(taskMobile);
				}
			}else{
				    tracer.addTag(messageLogin.getTask_id(), "手机验证码验证失败！");		
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
					// 手机验证码验证失败状态更新
					taskMobileRepository.save(taskMobile);
			}	   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return taskMobile;
	}
	
	
	
	
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		tracer.addTag("@Async getAllData", messageLogin.toString());
		tracer.addTag("taskid",messageLogin.getTask_id());	
		try{		
			tracer.addTag("TelecomGuizhouAspectService getAllData",messageLogin.getTask_id());			
			telecomGetAllDataHubeiService.getAllData(messageLogin);
		}catch(Exception e){
			tracer.addTag("TelecomGuizhouAspectService getAllData",e.getMessage());
			updateStatus(messageLogin);
		}		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());	
		tracer.addTag("TelecomGuizhouAspectService getAllData",taskMobile.toString());
		return taskMobile;
	
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void changeStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobileRepository.save(taskMobile);
	}

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if(null == taskMobile){
			return true;
		}
		if("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())){
			return true;
		}
		return false;
	}


	public void updateStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
		taskMobileRepository.save(taskMobile);
	} 
	
}
