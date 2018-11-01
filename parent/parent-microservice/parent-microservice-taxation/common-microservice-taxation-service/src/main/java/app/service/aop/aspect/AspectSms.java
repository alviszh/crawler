package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.crawler.taxation.json.TaxationStatusCode;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

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
	private TaskTaxationRepository taskTaxationRepository;
	
	
	
	/**
	 * 发送短信验证码（第一次）
	 */
	@Before("execution(* app.service.aop.ISms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) { 
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start=="+taxationRequestParameters.toString());  
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_SMS_SEND_DOING.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_SMS_SEND_DOING.getPhasestatus());
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_SMS_SEND_DOING.getDescription());
		taskTaxation = taskTaxationRepository.save(taskTaxation);
		tracer.addTag("@Aspect beforeSendSms ", "==SEND MESSAGE=="+taskTaxation.toString());  
		
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.sendSms(..))", returning = "taskTaxation")
	public void afterSendSms(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterSendSms ", "==Start==" + taxationRequestParameters.toString());
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		tracer.addTag("@Aspect afterSendSms ", "==SEND MESSAGE=="+taskTaxation.toString());  
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
	}
	
	
	/**
	 * 效验短信验证码
	 */										  
	@Before("execution(* app.service.aop.ISms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start=="+taxationRequestParameters.toString());  
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		// 手机验证码验证状态更新
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_SMS_VALIDATE_DOING.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_SMS_VALIDATE_DOING.getPhasestatus());
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_SMS_VALIDATE_DOING.getDescription());
		taskTaxation = taskTaxationRepository.save(taskTaxation);
		
		tracer.addTag("@Aspect beforeVerifySms ", "==SEND MESSAGE=="+taskTaxation.toString());    
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
	}
	
	/**
	 * 效验短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.verifySms(..))", returning = "taskTaxation")
	public void afterVerifySms(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterVerifySms ", "==Start==" + taxationRequestParameters.toString());
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		tracer.addTag("@Aspect afterVerifySms ", "==SEND MESSAGE=="+taskTaxation.toString());    
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
