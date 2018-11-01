package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.TaskStatusCode;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog; 

@Aspect
@Component
public class AspectSms {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	
	
	
	/**
	 * 发送短信验证码（第一次）
	 */
	@Before("execution(* app.service.aop.ISms.sendSms(..))")
	public void beforeSendSms(JoinPoint joinPoint) { 
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeSendSms ", "==Start=="+e_CommerceJsonBean.toString());  
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
		commerceTask.setPhase(TaskStatusCode.SEND_CODE_DONING.getPhase());
		commerceTask.setPhase_status(TaskStatusCode.SEND_CODE_DONING.getPhasestatus());
		commerceTask.setDescription(TaskStatusCode.SEND_CODE_DONING.getDescription());
		commerceTask = e_CommerceTaskRepository.save(commerceTask);
		tracer.addTag("@Aspect beforeSendSms ", "==END=="+commerceTask.toString());  
	}
	
	/**
	 * 发送短信验证码（第一次）后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.sendSms(..))", returning = "commerceTask")
	public void afterSendSms(E_CommerceTask commerceTask) {
		tracer.addTag("@Aspect afterSendSms ", "==Start&END=="+commerceTask.toString());    
	}
	
	
	/**
	 * 效验短信验证码
	 */										  
	@Before("execution(* app.service.aop.ISms.verifySms(..))")
	public void beforeVerifySms(JoinPoint joinPoint) {
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean)joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerifySms ", "==Start=="+e_CommerceJsonBean.toString());  
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
		// 手机验证码验证状态更新
		commerceTask.setPhase(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase());
		commerceTask.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus());
		commerceTask.setDescription(BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription());
		commerceTask = e_CommerceTaskRepository.save(commerceTask);
	}
	
	/**
	 * 效验短信验证码后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ISms.verifySms(..))", returning = "commerceTask")
	public void afterVerifySms(E_CommerceTask commerceTask) {
		tracer.addTag("@Aspect beforeVerifySms ", "==Start&END=="+commerceTask.toString());     
	}

}
