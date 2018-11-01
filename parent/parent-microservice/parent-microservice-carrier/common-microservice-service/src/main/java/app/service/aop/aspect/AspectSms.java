package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source; 

@Aspect
@Component
public class AspectSms {
	
	@Autowired
	private Source carrierOutput;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	
	
	/**
	 * 发送短信验证码（第一次）
	 */
	@Before("execution(* app.service.aop.ISms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) { 
		MessageLogin messageLogin = (MessageLogin)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start=="+messageLogin.toString());  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		taskMobile = taskMobileRepository.save(taskMobile);
		tracer.addTag("@Aspect beforeSendSms ", "==SEND MESSAGE=="+taskMobile.toString());  
		
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.sendSms(..))", returning = "taskMobile")
	public void afterSendSms(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		MessageLogin messageLogin = (MessageLogin) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterSendSms ", "==Start==" + messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("@Aspect afterSendSms ", "==SEND MESSAGE=="+taskMobile.toString());  
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	
	
	/**
	 * 效验短信验证码
	 */										  
	@Before("execution(* app.service.aop.ISms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		MessageLogin messageLogin = (MessageLogin)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start=="+messageLogin.toString());  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		// 手机验证码验证状态更新
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		taskMobile = taskMobileRepository.save(taskMobile);
		
		tracer.addTag("@Aspect beforeVerifySms ", "==SEND MESSAGE=="+taskMobile.toString());    
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	
	/**
	 * 效验短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.verifySms(..))", returning = "taskMobile")
	public void afterVerifySms(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		MessageLogin messageLogin = (MessageLogin) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterVerifySms ", "==Start==" + messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("@Aspect afterVerifySms ", "==SEND MESSAGE=="+taskMobile.toString());    
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
