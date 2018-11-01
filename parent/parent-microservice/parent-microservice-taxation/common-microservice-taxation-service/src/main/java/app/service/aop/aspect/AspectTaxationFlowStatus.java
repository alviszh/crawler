package app.service.aop.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.entity.crawler.taxation.basic.TaxationFlowStatus;
import com.microservice.dao.repository.crawler.taxation.basic.TaxationFlowStatusRepository;

import app.commontracerlog.TracerLog;

@Aspect
@Component
public class AspectTaxationFlowStatus {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaxationFlowStatusRepository taxationFlowStatusRepository;
	

	/**
	 * 后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository.save(..))", returning = "taskTaxation")
	public void afterSave(TaskTaxation taskTaxation) {
		tracer.addTag("@Aspect afterSave ", "==Start==" + taskTaxation.toString());
		if (null != taskTaxation) {
			TaxationFlowStatus taxationFlowStatus = new TaxationFlowStatus(taskTaxation.getTaskid(), taskTaxation.getDescription(),
					taskTaxation.getPhase(), taskTaxation.getPhase_status());
			taxationFlowStatusRepository.save(taxationFlowStatus);
		}
		tracer.addTag("@Aspect afterSave ", "==END==" + taskTaxation.toString());
	}
	

}
