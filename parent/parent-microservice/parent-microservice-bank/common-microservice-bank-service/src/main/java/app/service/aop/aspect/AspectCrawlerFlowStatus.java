package app.service.aop.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.basic.BankFlowStatus;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.BankFlowStatusRepository;

import app.commontracerlog.TracerLog;

@Aspect
@Component
public class AspectCrawlerFlowStatus { 
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private BankFlowStatusRepository bankFlowStatusRepository;
	
	/**
	 * 银行业务中记录状态时的前置操作---将对应的状态存入对应的表中
	 */
	@AfterReturning(value = "execution(* com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository.save(..))", returning = "taskBank")
	public void afterSave(TaskBank taskBank) { 
		System.out.println("Save的后置方法");
		tracer.addTag("@Aspect afterSave ", "==START=="+taskBank.toString());   
		String taskid = taskBank.getTaskid();
		String description = taskBank.getDescription();	
		System.out.println("Save的后置方法-----taskid："+taskid);
		System.out.println("Save的后置方法-----description："+description);
		BankFlowStatus bankFlowStatus = new BankFlowStatus();
		bankFlowStatus.setTaskid(taskid);
		bankFlowStatus.setDescription(description);
		bankFlowStatusRepository.save(bankFlowStatus);
		tracer.addTag("@Aspect afterSave ", "==END=="+taskBank.toString());   
	}
}
