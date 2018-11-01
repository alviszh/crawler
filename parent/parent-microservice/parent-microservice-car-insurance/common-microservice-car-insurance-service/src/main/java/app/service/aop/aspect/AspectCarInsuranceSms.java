package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.crawler.car.insurance.bean.CarInsuranceStatusCode;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.repository.crawler.car.insurance.TaskCarInsuranceRepository;
import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
public class AspectCarInsuranceSms {
	
	@Autowired
	private Source carInsuranceOutput;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskCarInsuranceRepository taskCarInsuranceRepository;
	
	/**
	 * 发送短信验证码（第一次）
	 */
	@Before("execution(* app.service.aop.CarInsuranceSms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) { 
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start=="+carInsuranceRequestBean.toString());  
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_SEND_CODE_DONING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_SEND_CODE_DONING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_SEND_CODE_DONING.getDescription());
		taskCarInsurance = taskCarInsuranceRepository.save(taskCarInsurance);
		tracer.addTag("@Aspect beforeSendSms ", "==SEND MESSAGE=="+taskCarInsurance.toString());  
		
		// 发送短信验证码（第一次）通知
		carInsuranceOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.CarInsuranceSms.sendSms(..))", returning = "taskCarInsurance")
	public void afterSendSms(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterSendSms ", "==Start==" + carInsuranceRequestBean.toString());
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		tracer.addTag("@Aspect afterSendSms ", "==SEND MESSAGE=="+taskCarInsurance.toString());  
		// 发送短信验证码（第一次）通知
		carInsuranceOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
	}
	
	
	/**
	 * 效验短信验证码
	 */										  
	@Before("execution(* app.service.aop.CarInsuranceSms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start=="+carInsuranceRequestBean.toString());  
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		// 手机验证码验证状态更新
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_DONING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_DONING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_DONING.getDescription());
		taskCarInsurance = taskCarInsuranceRepository.save(taskCarInsurance);
		
		tracer.addTag("@Aspect beforeVerifySms ", "==SEND MESSAGE=="+taskCarInsurance.toString());    
		// 验证短信验证码（第一次）通知
		carInsuranceOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
	}
	
	/**
	 * 效验短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.CarInsuranceSms.verifySms(..))", returning = "taskCarInsurance")
	public void afterVerifySms(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterVerifySms ", "==Start==" + carInsuranceRequestBean.toString());
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		tracer.addTag("@Aspect afterVerifySms ", "==SEND MESSAGE=="+taskCarInsurance.toString());    
		// 验证短信验证码（第一次）通知
		carInsuranceOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
	}

}
