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
public class AspectSmsTwice {
	
	@Autowired
	private Source carrierOutput;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	
	
	/**
	 * 发送短信验证码（第二次）
	 */
	@Before("execution(* app.service.aop.ISmsTwice.sendSmsTwice(..))")
	public void beforeSendSmsTwice(JoinPoint joinPoint) { 
		MessageLogin messageLogin = (MessageLogin)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeSendSmsTwice ", "==Start=="+messageLogin.toString());  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_DOING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_DOING.getPhasestatus());
		taskMobile.setError_code(null);
		taskMobile.setError_message(null);		
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_DOING.getDescription());
		taskMobile = taskMobileRepository.save(taskMobile);
		tracer.addTag("@Aspect beforeSendSmsTwice ", "==SEND MESSAGE=="+taskMobile.toString());   
		// 发送短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISmsTwice.sendSmsTwice(..))", returning = "taskMobile")
	public void afterSendSmsTwice(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		MessageLogin messageLogin = (MessageLogin) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterSendSmsTwice ", "==Start==" + messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("@Aspect afterSendSmsTwice ", "==SEND MESSAGE=="+taskMobile.toString());    
		// 发送短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	
	
	/**
	 * 效验短信验证码（第二次）
	 */
	@Before("execution(* app.service.aop.ISmsTwice.verifySmsTwice(..))")
	public void beforeVerifySmsTwice(JoinPoint joinPoint) { 
		MessageLogin messageLogin = (MessageLogin)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySmsTwice ", "==Start=="+messageLogin.toString());  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id()); 
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getDescription());
		taskMobileRepository.save(taskMobile);
		tracer.addTag("@Aspect beforeVerifySmsTwice ",  "==SEND MESSAGE=="+taskMobile.toString());  
		// 效验短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	
	/**
	 * 效验短信验证码（第二次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISmsTwice.verifySmsTwice(..))", returning = "taskMobile")
	public void afterVerifySmsTwice(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		MessageLogin messageLogin = (MessageLogin) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterVerifySmsTwice ", "==Start==" + messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("@Aspect afterVerifySmsTwice ", "==SEND MESSAGE=="+taskMobile.toString());   
		// 效验短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}

}
