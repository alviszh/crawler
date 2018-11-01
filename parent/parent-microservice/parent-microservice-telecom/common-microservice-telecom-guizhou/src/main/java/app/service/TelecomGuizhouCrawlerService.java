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
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomGuizhouSmsUnit;
import app.service.aop.ICrawler;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guizhou")
public class TelecomGuizhouCrawlerService implements ISms,ICrawler{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private TelecomGuizhouSmsUnit  telecomGuizhouSmsUnit;
	@Autowired
	private TelecomGuizhouService  telecomGuizhouService;
	/**
	 * @Description: 发送登录短信随机码
	 * @param messageLogin
	 * @return String
	 */
	public TaskMobile sendSms(MessageLogin mssageLogin) {
	    TaskMobile taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
		tracer.addTag("sendSms", taskMobile.getTaskid());
		try {		
			WebParam webParam = telecomGuizhouSmsUnit.getphonecode(mssageLogin,taskMobile);
			String smsResult=webParam.getSmsResult();
			tracer.addTag("电信获取验证码发送结果",smsResult);
		    if("1".equals(smsResult)){		    	
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
				return taskMobile;
			}else{
			   	tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
				return taskMobile;
			}
		} catch (Exception e) {
			tracer.addTag("sendSms.Exception",e.getMessage());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
			return taskMobile;
		}

	}
	
	public void updateStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
		taskMobileRepository.save(taskMobile);
	} 

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		tracer.addTag("@Async getAllData", messageLogin.toString());
		tracer.addTag("taskid",messageLogin.getTask_id());	
		tracer.output("telecomGuizhouCrawlerService.getAllData", messageLogin.getTask_id());
	    TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		try{		
			tracer.addTag("telecomGuizhouCrawlerService getAllData",messageLogin.getTask_id());				
			telecomGuizhouService.getAccountInfo(messageLogin, taskMobile);//获取账户信息
			telecomGuizhouService.getPaymonths(messageLogin, taskMobile);//月账单信息
			telecomGuizhouService.getVoiceRecord(messageLogin, taskMobile);//详细通话信息
			telecomGuizhouService.getSmsRecord(messageLogin, taskMobile);//短信详单信息
			telecomGuizhouService.getRechargeRecord(messageLogin, taskMobile);//充值缴费信息
			//telecomGuizhouService.getPoint(messageLogin, taskMobile);//积分信息
		}catch(Exception e){
			tracer.addTag("telecomGuizhouCrawlerService getAllData",e.getMessage());
			updateStatus(messageLogin);
		}		
		taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		return taskMobile;
	}


	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {		
		tracer.addTag("setPhoneCode", messageLogin.getTask_id());
		TaskMobile taskMobile= taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			WebParam webParam = telecomGuizhouSmsUnit.setphonecode(messageLogin, taskMobile);
			System.out.println("电信获取验证码验证结果" + webParam.getSmsResult());
			tracer.addTag("setPhoneCode 电信获取验证码验证结果为:", webParam.getSmsResult());
			if (null != webParam.getSmsResult() && webParam.getSmsResult().contains("OperationStatus")) {
				JSONObject jsonInsurObjs = JSONObject.fromObject(webParam.getSmsResult());
				String operationStatus = jsonInsurObjs.getString("OperationStatus");
				if ("0".equals(operationStatus)) {
					tracer.addTag(messageLogin.getTask_id(), "手机验证码验证成功！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setCookies(cookies);
					// 手机验证码验证成功状态更新
					taskMobileRepository.save(taskMobile);
					return taskMobile;
				} else {
					tracer.addTag(messageLogin.getTask_id(), "手机验证码验证失败！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
					// 手机验证码验证失败状态更新
					taskMobileRepository.save(taskMobile);
					return taskMobile;
				}
			} else {
				tracer.addTag(messageLogin.getTask_id(), "手机验证码验证失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				taskMobileRepository.save(taskMobile);
				return taskMobile;
			}
		} catch (Exception e) {		
			tracer.addTag("verifySms.Exception", e.getMessage());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			taskMobileRepository.save(taskMobile);
			return taskMobile;
		}
		

	}
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
