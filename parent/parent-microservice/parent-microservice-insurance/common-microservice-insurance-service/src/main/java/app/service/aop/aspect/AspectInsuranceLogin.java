package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectInsuranceLogin {

	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private Source carrierOutput;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	private Gson gson = new Gson();
	
	/**
	 * 登陆前置通知 
	 */
	@Before("execution(* app.service.aop.InsuranceLogin.login(..))")
	public void beforeLogin(JoinPoint joinPoint) { 
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeLogin ", "==Start=="+insuranceRequestParameters.toString());   
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId()); 
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhasestatus());
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getDescription());
//		taskInsuranceRepository.save(taskInsurance);
		taskInsurance.setTesthtml(gson.toJson(insuranceRequestParameters));
		taskInsurance.setCity(insuranceRequestParameters.getCity());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		tracer.addTag("@Aspect beforeLogin ", "==END=="+taskInsurance.toString());   
//		System.out.println("-----1---11-----1-1----11--1-1-1-111---1---1-1-----1--1----1-----------1-1----1-");
		// 发送开始登陆通知
	    carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
	}

	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.InsuranceLogin.login(..))", returning = "taskInsurance")
	public void afterLogin(JoinPoint joinPoint) {
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0];
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("@Aspect afterLogin ", "==Start&END=="+taskInsurance.toString());   
		// TODO 需要有登录成功或者不成功的代码1.登录不成功的各种情况的匹配，2登录成功或者不成功的状态推送
		// 发送结束登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
//		System.out.println("222222222222222222222222222222222222222222222222222222222222222222");
	}
}
