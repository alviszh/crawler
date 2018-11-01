package app.service.common.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.TaskStatusCode;
import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import app.commontracerlog.TracerLog;
import app.service.common.amqp.outbound.Source;

@Aspect
@Component
public class AspectCrawler {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskHousingRepository taskHousingRepository;

	@Autowired
	private Source output;
	/**
	 * 爬取总接口
	 * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskHousing状态
	 * @throws Throwable 
	 */
	@Around("execution(* app.service.common.aop.ICrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { 
		MessageLoginForHousing messageLoginForHousing = (MessageLoginForHousing)proceedingJoinPoint.getArgs()[0];
		tracer.output("@Aspect beforeGetAllData ", "==Start=="+messageLoginForHousing.toString());  
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		if(taskHousing!=null&&"CRAWLER".equals(taskHousing.getPhase()) && "DOING".equals(taskHousing.getPhase_status())){
			tracer.output("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");    
		}else{
			tracer.output("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");    
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getDescription());
			taskHousingRepository.save(taskHousing);
			output.output().send(MessageBuilder.withPayload(taskHousing).build());
			proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
		} 
		// 发送开始爬取通知
		tracer.output("@Aspect beforeGetAllData ", "==END=="+taskHousing.toString());   
	}

	/**
	 * 发送短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.common.aop.ICrawler.getAllData(..))",argNames = "taskHousing", returning = "taskHousing")
	public void afterGetAllData(JoinPoint joinPoint) { 
		MessageLoginForHousing messageLoginForHousing = (MessageLoginForHousing)joinPoint.getArgs()[0]; 
		tracer.output("@Aspect afterGetAllData ", "==Start=="+messageLoginForHousing.toString());   
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		tracer.output("@Aspect afterGetAllData ", "==END=="+taskHousing.toString());    
	}

	/**
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.common.aop.ICrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable { 
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start=="+taskId);  
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(taskId);
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END=="+taskHousing.toString());    
	}

	/**
	 * 发送短信验证码 后置通知 returnVal,切点方法执行后的返回值
	 * 
	 */

	@AfterReturning(value = "execution(* app.service.common.aop.ICrawler.getAllDataDone(..))",argNames = "taskHousing", returning = "taskHousing")
	public void afterGetAllDataDone(TaskHousing taskHousing) {
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start&END=="+taskHousing.toString()); 
		// 发送结束爬取通知
		output.output().send(MessageBuilder.withPayload(taskHousing).build());
	}


}
