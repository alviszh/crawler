package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectCrawlerLogin {
	@Autowired
	private Source carrierOutput;
	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskBankRepository taskBankRepository;

	private Gson gson = new Gson();

	/**
	 * 登陆前置通知
	 */
	@Before("execution(* app.service.aop.ICrawlerLogin.login(..))")
	public void beforeLogin(JoinPoint joinPoint) {
		System.out.println("=============登录切点方法的前置方法===============");
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeLogin ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if (null == taskBank) {
			throw new RuntimeException("Entity bean TaskBank is null ! taskid>>" + bankJsonBean.getTaskid() + "<<");
		}
		taskBank.setPhase(BankStatusCode.BANK_LOGIN_DOING.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_DOING.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_DOING.getDescription());
		taskBank.setTesthtml(gson.toJson(bankJsonBean));
		taskBank.setBankType(bankJsonBean.getBankType());
		System.out.println("bankType ==============================>" + bankJsonBean.getBankType());
		taskBank.setCardType(bankJsonBean.getCardType());
		System.out.println("cardType ==============================>" + bankJsonBean.getCardType());
		taskBank.setLoginType(bankJsonBean.getLoginType());
		System.out.println("loginName ==============================>" + bankJsonBean.getLoginName());
		taskBank.setLoginName(bankJsonBean.getLoginName());
		taskBank.setCrawlerHost(bankJsonBean.getIp());
		taskBank.setCrawlerPort(bankJsonBean.getPort());
		taskBank = taskBankRepository.save(taskBank);
		tracer.addTag("@Aspect beforeLogin ", "==END==" + taskBank.toString());
		// 发送开始登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */
	@AfterReturning(value = "execution(* app.service.aop.ICrawlerLogin.login(..))", returning = "taskBank")
	public void afterLogin(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============登录切点方法的后置方法========目前没有通用业务逻辑=======");
		tracer.addTag("@Aspect afterLogin ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("@Aspect afterLogin ", "==END==" + taskBank.toString());
		// 发送结束登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}
}
