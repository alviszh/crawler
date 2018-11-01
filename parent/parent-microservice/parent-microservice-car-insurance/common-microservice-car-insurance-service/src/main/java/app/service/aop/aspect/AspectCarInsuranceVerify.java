package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
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
@EnableBinding(Source.class)
public class AspectCarInsuranceVerify {
	
	@Autowired
	private Source carInsuranceOutput;

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskCarInsuranceRepository taskCarInsuranceRepository;

	
	/**
	 * 登陆前置通知
	 */
	@Before("execution(* app.service.aop.CarInsuranceVerify.verify(..))")
	public void beforeVerify(JoinPoint joinPoint) {
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeVerify ", "==Start==" + carInsuranceRequestBean.toString());
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_DOING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_DOING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_DOING.getDescription());
//		taskCarInsurance.setRequestJson(gson.toJson(carInsuranceRequestBean));   再task的check接口里已赋值
		taskCarInsurance = taskCarInsuranceRepository.save(taskCarInsurance);

		tracer.addTag("@Aspect beforeVerify ", "==END==" + taskCarInsurance.toString());
		// 发送开始登陆通知
		carInsuranceOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());
	}
	
	
	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.CarInsuranceVerify.verify(..))", returning = "taskCarInsurance")
	public void afterLogin(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		CarInsuranceRequestBean carInsuranceRequestBean = (CarInsuranceRequestBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterVerify ", "==Start==" + carInsuranceRequestBean.toString());
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(carInsuranceRequestBean.getTaskid());
		
		tracer.addTag("@Aspect afterVerify ", "==SEND MESSAGE==" + taskCarInsurance.toString());
		// TODO 需要有登录成功或者不成功的代码1.登录不成功的各种情况的匹配，2登录成功或者不成功的状态推送
		// 发送结束登陆通知
		carInsuranceOutput.output().send(MessageBuilder.withPayload(taskCarInsurance).build());

	}

}
