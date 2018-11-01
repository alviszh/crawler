package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;
import app.service.amqp.outbound.Source;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectInsuanceAgent {
	@Autowired
	private Source carrierOutput;
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceService insuranceService;

	@Before("execution(* app.service.aop.IAgent.releaseInstance(..))")
	public void beforeReleaseInstance(JoinPoint joinPoint) {
		String instanceIpAddr = (String) joinPoint.getArgs()[0];
		System.out.println("=============释放资源  （释放这台电脑 by IP，关闭WebDriver）=====切点方法的前置方法========目前没有通用业务逻辑=======");
		tracer.addTag("@Aspect beforeLogin ", "==Start&END==" + instanceIpAddr.toString());
	}

	@AfterReturning(value = "execution(* app.service.aop.IAgent.releaseInstance(..))")
	public void afterReleaseInstance(JoinPoint joinPoint) {
		String instanceIpAddr = (String) joinPoint.getArgs()[0];
		System.out.println("=============释放资源  （释放这台电脑 by IP，关闭WebDriver）=====切点方法的后置方法============");
		tracer.addTag("@Aspect afterLogin ", "==Start&End==" + instanceIpAddr.toString());
	}

	@Before("execution(* app.service.aop.IAgent.postAgentCombo(..))")
	public void beforePostAgentCombo(JoinPoint joinPoint) {
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters) joinPoint.getArgs()[0];
		System.out.println(
				"=============POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作=====切点方法的前置方法========目前没有通用业务逻辑=======");
		tracer.addTag("@Aspect beforeLogin ", "==Start&END==" + insuranceRequestParameters.toString());
	}

	@AfterReturning(value = "execution(* app.service.aop.IAgent.postAgentCombo(..))")
	public void afterPostAgentCombo(JoinPoint joinPoint) {
		InsuranceRequestParameters insuranceRequestParameter = (InsuranceRequestParameters) joinPoint.getArgs()[0];
		System.out.println(
				"=============POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作=====切点方法的后置方法============");
		tracer.addTag("@Aspect afterLogin ", "==Start&End==" + insuranceRequestParameter.toString());
	}

	@AfterThrowing(throwing = "ex", pointcut = "execution(* app.service.aop.IAgent.postAgent(..))")
	public void postAgent(JoinPoint joinPoint, Throwable ex) {
		InsuranceRequestParameters insuranceRequestParameter = (InsuranceRequestParameters) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect postAgent ", "==Start==" + insuranceRequestParameter.toString());
		System.out.println("中间层抛出的异常：：：" + ex + "-----对应的TaskId：：：" + insuranceRequestParameter.getTaskId());
		TaskInsurance taskInsurance = insuranceService.changeStatusException(
				InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase(),
				InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus(),
				InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription(),
				InsuranceStatusCode.INSURANCE_AGENT_ERROR.getError_code(), true, insuranceRequestParameter.getTaskId());
		tracer.addTag("@Aspect postAgent ", "==End==" + taskInsurance.toString());

		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());

	}

}
