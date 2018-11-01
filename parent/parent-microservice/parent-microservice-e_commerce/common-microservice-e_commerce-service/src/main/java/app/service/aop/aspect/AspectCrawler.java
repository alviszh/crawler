package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog;

@Aspect
@Component
public class AspectCrawler {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	
	
	/**
	 * 爬取总接口
	 * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskMobile状态
	 * @throws Throwable 
	 */
	@Around("execution(* app.service.aop.ICrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { 
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean)proceedingJoinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllData ", "==Start=="+e_CommerceJsonBean.toString());  
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
		if(commerceTask!=null&&"CRAWLER".equals(commerceTask.getPhase()) && "DOING".equals(commerceTask.getPhase_status())){
			tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");    
		}else{
			tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");    
			commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getPhase());
			commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getPhasestatus());
			commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getDescription());
			e_CommerceTaskRepository.save(commerceTask);
			proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
		} 
		tracer.addTag("@Aspect beforeGetAllData ", "==END=="+e_CommerceJsonBean.toString());   
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllData(..))", returning = "commerceTask")
	public void afterGetAllData(JoinPoint joinPoint) { 
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllData ", "==Start=="+e_CommerceJsonBean.toString());   
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid()); 
		tracer.addTag("@Aspect afterGetAllData ", "电商  开始爬取方法完成==END=="+commerceTask.toString());    
	}
	
	/**
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.aop.ICrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable { 
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start=="+taskId);  
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END=="+commerceTask.toString());    
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 * 
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllDataDone(..))", returning = "commerceTask")
	public void afterGetAllDataDone(E_CommerceTask commerceTask) {
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start&END=="+commerceTask.toString());   
	}
	

}
