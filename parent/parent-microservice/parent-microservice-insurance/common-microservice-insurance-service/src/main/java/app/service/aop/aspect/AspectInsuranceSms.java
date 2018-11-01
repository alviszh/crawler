package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
public class AspectInsuranceSms {

	@Autowired
	private Source carrierOutput;
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	
	
	/**
	 * 发送短信验证码（第一次）
	 */
	@Before("execution(* app.service.aop.InsuranceSms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) { 
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start=="+insuranceRequestParameters.toString());  
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_SMS_SEND_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_SMS_SEND_DOING.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		tracer.addTag("@Aspect beforeSendSms ", "==END=="+taskInsurance.toString());  
//		System.out.println("11111sendsendsendsendsendsendsendsendsendsendsendsendsendsendsendsend");
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.InsuranceSms.sendSms(..))", returning = "taskInsurance")
	public void afterSendSms(JoinPoint joinPoint) {
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0];
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("@Aspect afterSendSms ", "==Start&END=="+taskInsurance.toString());  
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
//		System.out.println("222222sendsendsendsendsendsendsendsendsendsendsendsendsendsendsendsend");
	}
	
	
	/**
	 * 效验短信验证码
	 */										  
	@Before("execution(* app.service.aop.InsuranceSms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		tracer.addTag("@Aspect beforeVerifySms ", "==Start&END=="+joinPoint.getArgs()[0].toString()); 
		
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start=="+insuranceRequestParameters.toString());  
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_SMS_VALIDATE_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_SMS_VALIDATE_DOING.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		tracer.addTag("@Aspect beforeVerifySms ", "==END=="+taskInsurance.toString()); 
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
	}
	
	/**
	 * 效验短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.InsuranceSms.verifySms(..))", returning = "taskInsurance")
	public void afterVerifySms(JoinPoint joinPoint) {
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0];
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("@Aspect beforeVerifySms ", "==Start&END=="+taskInsurance.toString()); 
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
	}
}
