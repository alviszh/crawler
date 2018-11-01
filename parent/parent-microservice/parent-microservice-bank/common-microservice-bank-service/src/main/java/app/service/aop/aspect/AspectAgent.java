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

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.TaskBankStatusService;
import app.service.amqp.outbound.Source;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectAgent {
	@Autowired
	private Source carrierOutput;
	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskBankStatusService taskBankStatusService;
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
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作=====切点方法的前置方法========目前没有通用业务逻辑=======");
		tracer.addTag("@Aspect beforeLogin ", "==Start&END==" + bankJsonBean.toString());
	}
	@AfterReturning(value = "execution(* app.service.aop.IAgent.postAgentCombo(..))")
	public void afterPostAgentCombo(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作=====切点方法的后置方法============");
		tracer.addTag("@Aspect afterLogin ", "==Start&End==" + bankJsonBean.toString());
	}
	
	
	
	
	
	
	@AfterThrowing(throwing="ex" , pointcut="execution(* app.service.aop.IAgent.postAgent(..))")
	public void postAgent(JoinPoint joinPoint,Throwable ex) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect postAgent ", "==Start==" + bankJsonBean.toString());
		System.out.println("中间层抛出的异常：：："+ex+"-----对应的TaskId：：："+bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
				ex.getMessage(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
		BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
		tracer.addTag("@Aspect postAgent ", "==End==" + taskBank.toString());
		
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
		
	}
	
	
}

