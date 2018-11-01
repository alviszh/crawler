package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.crawler.taxation.json.TaxationStatusCode;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectLogin {

	@Autowired
	private Source carrierOutput;

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskTaxationRepository taskTaxationRepository;

	private Gson gson = new Gson();

	/**
	 * 登陆前置通知
	 */
	@Before("execution(* app.service.aop.ILogin.login(..))")
	public void beforeLogin(JoinPoint joinPoint) {
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeLogin ", "==Start==" + taxationRequestParameters.toString());
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_DOING.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_DOING.getPhasestatus());
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_DOING.getDescription());
		taskTaxation.setTesthtml(gson.toJson(taxationRequestParameters));
		taskTaxation = taskTaxationRepository.save(taskTaxation);

		tracer.addTag("@Aspect beforeLogin ", "==END==" + taskTaxation.toString());
		// 发送开始登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
	}

	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ILogin.login(..))", returning = "taskTaxation")
	public void afterLogin(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterLogin ", "==Start==" + taxationRequestParameters.toString());
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		
		tracer.addTag("@Aspect afterLogin ", "==SEND MESSAGE==" + taskTaxation.toString());
		// TODO 需要有登录成功或者不成功的代码1.登录不成功的各种情况的匹配，2登录成功或者不成功的状态推送
		// 发送结束登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());

	}

}
