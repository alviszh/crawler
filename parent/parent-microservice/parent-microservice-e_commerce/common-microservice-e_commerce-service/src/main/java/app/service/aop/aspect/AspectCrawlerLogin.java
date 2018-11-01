package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog;
import app.service.ampq.outbound.EcommerceOutput;

@Aspect
@Component
@EnableBinding(EcommerceOutput.class)
public class AspectCrawlerLogin { 
	
	@Autowired
    private EcommerceOutput ecommerceOutput;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	
	private Gson gson = new Gson();
	
	/**
	 * 登陆前置通知 
	 */
	@Before("execution(* app.service.aop.ICrawlerLogin.login(..))")
	public void beforeLogin(JoinPoint joinPoint) { 
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeLogin ", "==Start=="+e_CommerceJsonBean.toString());   
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid()); 
		commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhase());
		commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhasestatus());
		commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getDescription());
		commerceTask.setTesthtml(gson.toJson(e_CommerceJsonBean));
		commerceTask = e_CommerceTaskRepository.save(commerceTask);
		tracer.addTag("@Aspect beforeLogin ", "==END=="+commerceTask.toString());   
		//发送开始登陆通知
		ecommerceOutput.e_commerce_output().send(MessageBuilder.withPayload(commerceTask).build());
	}

	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawlerLogin.login(..))", returning = "commerceTask")
	public void afterLogin(E_CommerceTask commerceTask) {
		tracer.addTag("@Aspect afterLogin ", "==Start&END=="+commerceTask.toString());   
		//TODO 需要有登录成功或者不成功的代码1.登录不成功的各种情况的匹配，2登录成功或者不成功的状态推送
		
	}
	
	
	@Before("execution(* app.service.aop.ICrawlerLogin.getQRcode(..))")
	public void beforeGetQRcode(JoinPoint joinPoint) { 
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeGetQRcode ", "==Start=="+e_CommerceJsonBean.toString());   
		E_CommerceTask commerceTask = e_CommerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid()); 
		commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_DOING.getPhase());
		commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_DOING.getPhasestatus());
		commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_GET_QRCODE_DOING.getDescription());
		commerceTask.setTesthtml(gson.toJson(e_CommerceJsonBean));
		commerceTask.setCrawlerHost(e_CommerceJsonBean.getIp());
		commerceTask.setCrawlerPort(e_CommerceJsonBean.getPort());
		commerceTask = e_CommerceTaskRepository.save(commerceTask);
		tracer.addTag("@Aspect beforeGetQRcode ", "==END=="+commerceTask.toString());   
		//发送通知
		ecommerceOutput.e_commerce_output().send(MessageBuilder.withPayload(commerceTask).build());
	}
	
	@AfterReturning(value = "execution(* app.service.aop.ICrawlerLogin.checkQRcode(..))", returning = "commerceTask")
	public void afterCheckQRcode(E_CommerceTask commerceTask) {
		tracer.addTag("@Aspect afterCheckQRcode ", "==Start&END=="+commerceTask.toString());   
		//发送通知
		ecommerceOutput.e_commerce_output().send(MessageBuilder.withPayload(commerceTask).build());
		
	}

}
