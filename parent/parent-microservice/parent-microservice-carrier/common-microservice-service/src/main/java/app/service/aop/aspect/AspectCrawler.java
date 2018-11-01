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

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
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
	private TaskMobileRepository taskMobileRepository;
	
	
	/**
	 * 爬取总接口
	 * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskMobile状态
	 * @throws Throwable 
	 */
	@Around("execution(* app.service.aop.ICrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { 
		MessageLogin messageLogin = (MessageLogin)proceedingJoinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllData ", "==Start=="+messageLogin.toString());  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if(taskMobile!=null&&"CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())){
			tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");    
		}else{
			tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");    
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
			taskMobileRepository.save(taskMobile);
			// 发送开始爬取通知
			carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
			proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
		} 
		tracer.addTag("@Aspect beforeGetAllData ", "==END=="+messageLogin.toString());   
		
	}
	
	/**
	 * 完成爬取后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllData(..))", returning = "taskMobile")
	public void afterGetAllData(JoinPoint joinPoint) { 
		MessageLogin messageLogin = (MessageLogin)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllData ", "==Start=="+messageLogin.toString());   
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id()); 
		tracer.addTag("@Aspect afterGetAllData ", "运营商  开始爬取方法完成（注意因为爬取都是异步，此处只是开始爬取方法完成，爬取并没有完成）==END=="+taskMobile.toString());    
	}
	
	/**
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.aop.ICrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable { 
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start=="+taskId);  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END=="+taskMobile.toString());    
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 * 
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllDataDone(..))", returning = "taskMobile")
	public void afterGetAllDataDone(JoinPoint joinPoint) {
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start=="+taskId);  
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect afterGetAllDataDone ", "==SEND MESSAGE=="+taskMobile.toString());   
		// 发送结束爬取通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}
	

}
