package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

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
	private TaskBankRepository taskBankRepository;

	/**
	 * 发送短信验证码（第二次）
	 */
	@Before("execution(* app.service.aop.ISmsTwice.sendSmsTwice(..))")
	public void beforeSendSmsTwice(JoinPoint joinPoint) {
		System.out.println("=============第二次发送验证码切点方法的前置方法===============");
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSmsTwice ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank.setPhase(BankStatusCode.BANK_SEND_CODE_DONING2.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_SEND_CODE_DONING2.getPhasestatus());
		taskBank.setError_code(BankStatusCode.BANK_SEND_CODE_DONING2.getError_code());
		taskBank.setDescription(BankStatusCode.BANK_SEND_CODE_DONING2.getDescription());
		taskBank = taskBankRepository.save(taskBank);
		tracer.addTag("@Aspect beforeSendSmsTwice ", "==END==" + taskBank.toString());

		// 发送短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISmsTwice.sendSmsTwice(..))", returning = "taskBank")
	public void afterSendSmsTwice(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============第二次发送验证码切点方法的后置方法=======目前没有通用业务逻辑========");
		tracer.addTag("@Aspect afterSendSmsTwice ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("@Aspect afterSendSmsTwice ", "==END==" + taskBank.toString());
		// 发送短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 效验短信验证码（第二次）
	 */
	@Before("execution(* app.service.aop.ISmsTwice.verifySmsTwice(..))")
	public void beforeVerifySmsTwice(JoinPoint joinPoint) {
		System.out.println("=============第二次验证验证码切点方法的前置方法===============");
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySmsTwice ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank.setPhase(BankStatusCode.BANK_VALIDATE_CODE_DONING2.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_DONING2.getPhasestatus());
		taskBank.setError_code(BankStatusCode.BANK_VALIDATE_CODE_DONING2.getError_code());
		taskBank.setDescription(BankStatusCode.BANK_VALIDATE_CODE_DONING2.getDescription());
		taskBank = taskBankRepository.save(taskBank);
		tracer.addTag("@Aspect beforeVerifySmsTwice ", "==END==" + taskBank.toString());

		// 效验短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 效验短信验证码（第二次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISmsTwice.verifySmsTwice(..))", returning = "taskBank")
	public void afterVerifySmsTwice(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============第二次验证验证码切点方法的后置方法=======目前没有通用业务逻辑========");
		tracer.addTag("@Aspect afterVerifySmsTwice ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("@Aspect afterVerifySmsTwice ", "==END==" + taskBank.toString());
		// 效验短信验证码（第二次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

}
