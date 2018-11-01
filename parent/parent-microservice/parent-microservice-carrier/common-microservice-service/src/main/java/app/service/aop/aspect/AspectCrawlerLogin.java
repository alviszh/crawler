package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;

@Aspect
@Component
@EnableBinding(Source.class)
public class AspectCrawlerLogin {

	@Autowired
	private Source carrierOutput;

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	private Gson gson = new Gson();

	/**
	 * 登陆前置通知
	 */
	@Before("execution(* app.service.aop.ICrawlerLogin.login(..))")
	public void beforeLogin(JoinPoint joinPoint) {
		MessageLogin messageLogin = (MessageLogin) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect beforeLogin ", "==Start==" + messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		taskMobile.setTesthtml(gson.toJson(messageLogin));
		taskMobile = taskMobileRepository.save(taskMobile);

		tracer.addTag("@Aspect beforeLogin ", "==END==" + taskMobile.toString());
		// 发送开始登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
	}

	/**
	 * 登录后置通知 returnVal,切点方法执行后的返回值
	 */

	@AfterReturning(value = "execution(* app.service.aop.ICrawlerLogin.login(..))", returning = "taskMobile")
	public void afterLogin(JoinPoint joinPoint) {
		// JoinPoint joinPoint
		MessageLogin messageLogin = (MessageLogin) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect afterLogin ", "==Start==" + messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		tracer.addTag("@Aspect afterLogin ", "==SEND MESSAGE==" + taskMobile.toString());
		// TODO 需要有登录成功或者不成功的代码1.登录不成功的各种情况的匹配，2登录成功或者不成功的状态推送
		// 发送结束登陆通知
		carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());

	}

}
