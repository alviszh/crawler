package app.service.common.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.annotation.EnableBinding;
import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import app.commontracerlog.TracerLog;
import app.service.common.amqp.outbound.Source;
import org.springframework.messaging.support.MessageBuilder;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectCrawlerLogin { 
	
	@Autowired
    private Source source;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	
	private Gson gson = new Gson();
	
	/**
	 * 登陆前置通知 
	 */
	@Before("execution(* app.service.common.aop.ICrawlerLogin.login(..))")
	public void beforeLogin(JoinPoint joinPoint) { 
		MessageLoginForHousing messageLoginForHousing = (MessageLoginForHousing)joinPoint.getArgs()[0]; 
		tracer.addTag("@Aspect beforeLogin ", "==Start=="+messageLoginForHousing.toString());   
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		taskHousing.setPassword(messageLoginForHousing.getPassword());
		taskHousing = taskHousingRepository.save(taskHousing);
		tracer.addTag("@Aspect beforeLogin ", "==END=="+taskHousing.toString());  
		
		//发送开始登陆通知
		source.output().send(MessageBuilder.withPayload(taskHousing).build());
	}

	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.common.aop.ICrawlerLogin.login(..))",argNames = "taskHousing", returning = "taskHousing")
	public void afterLogin(TaskHousing taskHousing) {
		tracer.addTag("@Aspect afterLogin ", "==Start&END=="+taskHousing.toString());   
		//TODO 需要有登录成功或者不成功的代码1.登录不成功的各种情况的匹配，2登录成功或者不成功的状态推送
		// 发送结束登陆通知
		source.output().send(MessageBuilder.withPayload(taskHousing).build());
	}
	

}
