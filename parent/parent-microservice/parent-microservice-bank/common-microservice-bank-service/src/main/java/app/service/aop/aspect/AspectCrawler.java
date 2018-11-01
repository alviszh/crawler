package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
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
public class AspectCrawler {
	@Autowired
	private Source carrierOutput;
	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskBankRepository taskBankRepository;

	/**
	 * 爬取总接口 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskBank状态
	 * 
	 * @throws Throwable
	 */
	@Around("execution(* app.service.aop.ICrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("=============爬取切点方法的前置方法===============");
		BankJsonBean bankJsonBean = (BankJsonBean) proceedingJoinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllData ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if (taskBank != null && "CRAWLER".equals(taskBank.getPhase()) && "DOING".equals(taskBank.getPhase_status())) {
			tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");
		} else {
			tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");
			taskBank.setPhase(BankStatusCode.BANK_CRAWLER_DOING.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus());
			taskBank.setDescription(BankStatusCode.BANK_CRAWLER_DOING.getDescription());
			taskBankRepository.save(taskBank);
			// 发送开始爬取通知
			carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
			// ***重要！***调用了proceed方法，爬取方法getAllData才会被执行******
			proceedingJoinPoint.proceed();
		}
		tracer.addTag("@Aspect beforeGetAllData ", "==END==" + bankJsonBean.toString());

	}

	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */
	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllData(..))", returning = "taskBank")
	public void afterGetAllData(JoinPoint joinPoint) {
		BankJsonBean bankJsonBean = (BankJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterGetAllData ", "==Start==" + bankJsonBean.toString());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracer.addTag("@Aspect afterGetAllData ",
				"网银  开始爬取方法完成（注意因为爬取都是异步，此处只是开始爬取方法完成，爬取并没有完成）==END==" + taskBank.toString());
	}

	/**
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.aop.ICrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable {
		System.out.println("=============爬取完成切点方法的前置方法===============");
		String taskId = (String) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start==" + taskId);
		TaskBank taskBank = taskBankRepository.findByTaskid(taskId);
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END==" + taskBank.toString());
	}

	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 * 
	 */
	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllDataDone(..))", returning = "taskBank")
	public void afterGetAllDataDone(JoinPoint joinPoint) {
		String taskId = (String)joinPoint.getArgs()[0];
		System.out.println("=============爬取完成切点方法的后置方法===============");
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start&END==" + taskId);
		TaskBank taskBank = taskBankRepository.findByTaskid(taskId);
		// 发送结束爬取通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskBank).build());
	}

}
