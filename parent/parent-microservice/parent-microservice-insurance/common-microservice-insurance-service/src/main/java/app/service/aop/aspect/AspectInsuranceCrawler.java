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

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
public class AspectInsuranceCrawler {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private Source carrierOutput;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	
	/**
	 * 爬取总接口
	 * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
	 * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
	 * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskInsurance状态
	 * @throws Throwable 
	 */
	@Around("execution(* app.service.aop.InsuranceCrawler.getAllData(..))")
	public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { 
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)proceedingJoinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeGetAllData ", "==Start=="+insuranceRequestParameters.toString());  
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if(taskInsurance!=null&&"CRAWLER".equals(taskInsurance.getPhase()) && "DOING".equals(taskInsurance.getPhase_status())){
			tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");    
		}else{
			tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");    
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getDescription());
			taskInsuranceRepository.save(taskInsurance);
			// 发送开始爬取通知
			carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
			proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
		} 
		tracer.addTag("@Aspect beforeGetAllData ", "==END=="+insuranceRequestParameters.toString());  

	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.InsuranceCrawler.getAllData(..))", returning = "taskInsurance")
	public void afterGetAllData(JoinPoint joinPoint) { 
		InsuranceRequestParameters insuranceRequestParameters = (InsuranceRequestParameters)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect afterGetAllData ", "==Start=="+insuranceRequestParameters.toString());   
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId()); 
//		System.out.println("4444444444444444444444444444444444444444444444"+taskInsurance.toString());

		tracer.addTag("@Aspect afterGetAllData ", "==Start=="+taskInsurance.toString());   

	}
	
	/**
	 * @Before
	 * 爬取完成接口
	 */
	@Before("execution(* app.service.aop.InsuranceCrawler.getAllDataDone(..))")
	public void beforeGetAllDataDone(JoinPoint joinPoint) throws Throwable { 
		String taskId = (String)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==Start=="+taskId);  
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskId); 
		tracer.addTag("@Aspect beforeGetAllDataDone ", "==END=="+taskInsurance.toString()); 
//		System.out.println("55555555555555555555555555555555555555555555555555555555");
	}
	
	/**
	 * @AfterReturning
	 * 爬取完成接口
	 * 
	 */

	@AfterReturning(value = "execution(* app.service.aop.InsuranceCrawler.getAllDataDone(..))", returning = "taskInsurance")
	public void afterGetAllDataDone(TaskInsurance taskInsurance) {
		tracer.addTag("@Aspect afterGetAllDataDone ", "==Start&END=="+taskInsurance.toString());   
//		System.out.println("6666666666666666666666666666666666666666666666666666666666666");
		// 发送结束爬取通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskInsurance).build());
	}
}
