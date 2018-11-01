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
import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.crawler.car.insurance.bean.CarInsuranceStatusCode;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.repository.crawler.car.insurance.TaskCarInsuranceRepository;
import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
public class AspectCarInsuranceCrawler {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private Source carrierOutput;	
	@Autowired
	private TaskCarInsuranceRepository taskCarInsuranceRepository;
	
	/**
	 * 爬取总接口
	 * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskInsurance状态
	 * @throws Throwable 
	 */
	@Around("execution(* app.service.aop.CarInsuranceCrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { 
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean)proceedingJoinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllData ", "==Start=="+carInsuranceRequestBean.toString());  
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		if(taskCarInsurance!=null&&"CRAWLER".equals(taskCarInsurance.getPhase()) && "DOING".equals(taskCarInsurance.getPhase_status())){
			tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");    
		}else{
			tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");    
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_DOING.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_DOING.getPhasestatus());
			taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_DOING.getDescription());
			taskCarInsuranceRepository.save(taskCarInsurance);
			// 发送开始爬取通知
			carrierOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
			proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
		} 
		tracer.addTag("@Aspect beforeGetAllData ", "==END=="+carInsuranceRequestBean.toString());  

	}
	
	
	/**
	 * 完成爬取后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.CarInsuranceCrawler.getAllData(..))", returning = "taskCarInsurance")
	public void afterGetAllData(JoinPoint joinPoint) { 
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllData ", "==Start=="+carInsuranceRequestBean.toString());   
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid()); 
		tracer.addTag("@Aspect afterGetAllData ", "  开始爬取方法完成（注意因为爬取都是异步，此处只是开始爬取方法完成，爬取并没有完成）==END=="+taskCarInsurance.toString());    
	}
	
	
	/**
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.aop.CarInsuranceCrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable { 
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start=="+taskId);  
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END=="+taskCarInsurance.toString());    
	}
	
	/**
	 * 完成爬取）后置通知 returnVal,切点方法执行后的返回值
	 * 
	 */

	@AfterReturning(value = "execution(* app.service.aop.CarInsuranceCrawler.getAllDataDone(..))", returning = "taskCarInsurance")
	public void afterGetAllDataDone(JoinPoint joinPoint) {
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start=="+taskId);  
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect afterGetAllDataDone ", "==SEND MESSAGE=="+taskCarInsurance.toString());   
		// 发送结束爬取通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
	}
	

}
