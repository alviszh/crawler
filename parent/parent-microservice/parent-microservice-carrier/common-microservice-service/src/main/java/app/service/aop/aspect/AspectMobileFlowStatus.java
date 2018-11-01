package app.service.aop.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.MobileFlowStatus;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.MobileFlowStatusRepository;

import app.commontracerlog.TracerLog;

@Aspect
@Component
public class AspectMobileFlowStatus {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private MobileFlowStatusRepository mobileStateRecordRepository;
	
	/**
	 * 前置通知 
	 */
//	@Before("execution(* com.microservice.dao.repository.crawler.mobile.TaskMobileRepository.save(..))")
//	public void beforeSave(JoinPoint joinPoint) { 
//		tracer.addTag("@Aspect beforeSave ", "==Start&END==");   
//	}

	/**
	 * 后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* com.microservice.dao.repository.crawler.mobile.TaskMobileRepository.save(..))", returning = "taskMobile")
	public void afterSave(TaskMobile taskMobile) {
		tracer.addTag("@Aspect afterSave ", "==Start==" + taskMobile.toString());
		if (null != taskMobile) {
			MobileFlowStatus mobileStateRecord = new MobileFlowStatus(taskMobile.getTaskid(), taskMobile.getDescription(),
					taskMobile.getPhase(), taskMobile.getPhase_status());
			mobileStateRecordRepository.save(mobileStateRecord);
		}
		tracer.addTag("@Aspect afterSave ", "==END==" + taskMobile.toString());
	}
	

}
