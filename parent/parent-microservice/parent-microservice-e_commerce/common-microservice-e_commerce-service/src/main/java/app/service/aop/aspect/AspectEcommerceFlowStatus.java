package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_commerceFlowStatus;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceFlowStatusRepository;

import app.commontracerlog.TracerLog;

@Aspect
@Component
public class AspectEcommerceFlowStatus {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private E_CommerceFlowStatusRepository ecommerceStateRecordRepository;
	
	
	/**
	 * 前置通知 
	 */
	@Before("execution(* com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository.save(..))")
	public void beforeSave(JoinPoint joinPoint) { 
		tracer.addTag("@Aspect beforeSave ", "==Start&END==");   
	}

	/**
	 * 后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository.save(..))", returning = "commerceTask")
	public void afterSave(E_CommerceTask commerceTask) {
		tracer.addTag("@Aspect afterSave ", "==Start=="+commerceTask.toString());   
		if(null != commerceTask){
			E_commerceFlowStatus ecommerceFlowStatus = new E_commerceFlowStatus(commerceTask.getTaskid(), commerceTask.getDescription());
			ecommerceStateRecordRepository.save(ecommerceFlowStatus);
		}
		tracer.addTag("@Aspect afterSave ", "==END=="+commerceTask.toString());  
	}
	

}
