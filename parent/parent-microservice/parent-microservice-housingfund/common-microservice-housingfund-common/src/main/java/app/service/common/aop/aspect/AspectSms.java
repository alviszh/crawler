package app.service.common.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.TaskStatusCode;
import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.common.amqp.outbound.Source;

@Aspect
@Component
public class AspectSms {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	
	@Autowired
	private Source output;
	
	/**
	 * 发送短信验证码
	 */
	@Before("execution(* app.service.common.aop.ISms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) { 
		MessageLoginForHousing messageLoginForHousing = (MessageLoginForHousing)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start=="+messageLoginForHousing.toString());  
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_DONING.getDescription());
		taskHousing = taskHousingRepository.save(taskHousing);
		tracer.addTag("@Aspect beforeSendSms ", "==END=="+taskHousing.toString()); 
		// 发送短信验证码通知
		output.output().send(MessageBuilder.withPayload(taskHousing).build());
	}
	
	/**
	 * 发送短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.common.aop.ISms.sendSms(..))",argNames = "taskHousing", returning = "taskHousing")
	public void afterSendSms(TaskHousing taskHousing) {
		tracer.addTag("@Aspect afterSendSms ", "==Start&END=="+taskHousing.toString()); 
		// 发送短信验证码通知
		output.output().send(MessageBuilder.withPayload(taskHousing).build());
	}
	
	
	/**
	 * 效验短信验证码
	 */										  
	@Before("execution(* app.service.common.aop.ISms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		MessageLoginForHousing messageLoginForHousing = (MessageLoginForHousing)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start=="+messageLoginForHousing.toString());  
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		// 手机验证码验证状态更新
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getDescription());
		taskHousing = taskHousingRepository.save(taskHousing);
		// 验证短信验证码通知
		output.output().send(MessageBuilder.withPayload(taskHousing).build());
	}
	
	/**
	 * 效验短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.common.aop.ISms.verifySms(..))",argNames = "taskHousing", returning = "taskHousing")
	public void afterVerifySms(TaskHousing taskHousing) {
		tracer.addTag("@Aspect beforeVerifySms ", "==Start&END=="+taskHousing.toString());
		// 验证短信验证码通知
		output.output().send(MessageBuilder.withPayload(taskHousing).build());
	}

}
