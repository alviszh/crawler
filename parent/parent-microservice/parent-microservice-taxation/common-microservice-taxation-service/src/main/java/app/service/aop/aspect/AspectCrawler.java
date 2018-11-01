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

import com.crawler.taxation.json.MessageLogin;
import com.crawler.taxation.json.TaxationRequestParameters;
import com.crawler.taxation.json.TaxationStatusCode;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

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
	private TaskTaxationRepository taskTaxationRepository;
	
	
	/**
	 * 爬取总接口
	 * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskTaxation状态
	 * @throws Throwable 
	 */
	@Around("execution(* app.service.aop.ICrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { 
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters)proceedingJoinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllData ", "==Start=="+taxationRequestParameters.toString());  
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		if(taskTaxation!=null&&"CRAWLER".equals(taskTaxation.getPhase()) && "DOING".equals(taskTaxation.getPhase_status())){
			tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");    
		}else{
			tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");    
			taskTaxation.setPhase(TaxationStatusCode.TAXATION_CRAWLER_DOING.getPhase());
			taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_CRAWLER_DOING.getPhasestatus());
			taskTaxation.setDescription(TaxationStatusCode.TAXATION_CRAWLER_DOING.getDescription());
			taskTaxationRepository.save(taskTaxation);
			// 发送开始爬取通知
			carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
			proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
		} 
		tracer.addTag("@Aspect beforeGetAllData ", "==END=="+taxationRequestParameters.toString());   
		
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllData(..))", returning = "taskTaxation")
	public void afterGetAllData(JoinPoint joinPoint) { 
		TaxationRequestParameters taxationRequestParameters = (TaxationRequestParameters)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllData ", "==Start=="+taxationRequestParameters.toString());   
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId()); 
		tracer.addTag("@Aspect afterGetAllData ", "个税  开始爬取方法完成（注意因为爬取都是异步，此处只是开始爬取方法完成，爬取并没有完成）==END=="+taskTaxation.toString());    
	}
	
	/**
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.aop.ICrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable { 
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start=="+taskId);  
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END=="+taskTaxation.toString());    
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 * 
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllDataDone(..))", returning = "taskTaxation")
	public void afterGetAllDataDone(JoinPoint joinPoint) {
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start=="+taskId);  
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect afterGetAllDataDone ", "==SEND MESSAGE=="+taskTaxation.toString());   
		// 发送结束爬取通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskTaxation).build());
	}
	

}
