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
public class AspectSms {
	@Autowired
	private Source carrierOutput;
	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskBankRepository taskBankRepository;

	/**
	 * 发送短信验证码（第一次）
	 */
	@Before("execution(* app.service.aop.ISms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) {
		System.out.println("=============发送验证码切点方法的前置方法===============");
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank.setPhase(BankStatusCode.BANK_SEND_CODE_DONING.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_SEND_CODE_DONING.getPhasestatus());
		taskBank.setError_code(BankStatusCode.BANK_SEND_CODE_DONING.getError_code());
		taskBank.setDescription(BankStatusCode.BANK_SEND_CODE_DONING.getDescription());
		taskBank = taskBankRepository.save(taskBank);
		tracer.addTag("@Aspect beforeSendSms ", "==END==" + taskBank.toString());

		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.sendSms(..))", returning = "taskBank")
	public void afterSendSms(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============发送验证码切点方法的后置方法=======目前没有通用业务逻辑========");
		tracer.addTag("@Aspect afterSendSms ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("@Aspect afterSendSms ", "==END==" + taskBank.toString());
		// 发送短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 验证短信验证码
	 */
	@Before("execution(* app.service.aop.ISms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		System.out.println("=============验证验证码切点方法的前置方法===============");
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank.setPhase(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus());
		taskBank.setError_code(BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code());
		taskBank.setDescription(BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription());
		taskBank = taskBankRepository.save(taskBank);
		tracer.addTag("@Aspect beforeVerifySms ", "==END==" + taskBank.toString());

		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

	/**
	 * 验证短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.verifySms(..))", returning = "taskBank")
	public void afterVerifySms(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============验证验证码切点方法的后置方法=======目前没有通用业务逻辑========");
		tracer.addTag("@Aspect afterVerifySms ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("@Aspect afterVerifySms ", "==END==" + taskBank.toString());
		// 验证短信验证码（第一次）通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

}
