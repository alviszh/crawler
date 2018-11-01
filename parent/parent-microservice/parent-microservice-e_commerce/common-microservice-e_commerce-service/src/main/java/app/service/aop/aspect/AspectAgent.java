package app.service.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import app.commontracerlog.TracerLog;
import app.service.ampq.outbound.EcommerceOutput;

@Aspect
@Component
@EnableBinding(EcommerceOutput.class)
public class AspectAgent {
	@Autowired
	private TracerLog tracer;

	@Before("execution(* app.service.aop.IAgent.releaseInstance(..))")
	public void beforeReleaseInstance(JoinPoint joinPoint) {
		String instanceIpAddr = (String) joinPoint.getArgs()[0];
		System.out.println("=============释放资源  （释放这台电脑 by IP，关闭WebDriver）=====切点方法的前置方法========目前没有通用业务逻辑=======");
		tracer.addTag("@Aspect beforeLogin ", "==Start&END==" + instanceIpAddr.toString());
	}
	
	@AfterReturning(value = "execution(* app.service.aop.IAgent.releaseInstance(..))")
	public void afterReleaseInstance(JoinPoint joinPoint) {
		String instanceIpAddr = (String) joinPoint.getArgs()[0];
		System.out.println("=============释放资源  （释放这台电脑 by IP，关闭WebDriver）=====切点方法的后置方法============");
		tracer.addTag("@Aspect afterLogin ", "==Start&End==" + instanceIpAddr.toString());
	}
	
	@Before("execution(* app.service.aop.IAgent.postAgentCombo(..))")
	public void beforePostAgentCombo(JoinPoint joinPoint) {
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作=====切点方法的前置方法========目前没有通用业务逻辑=======");
		tracer.addTag("@Aspect beforeLogin ", "==Start&END==" + e_CommerceJsonBean.toString());
	}
	
	@AfterReturning(value = "execution(* app.service.aop.IAgent.postAgentCombo(..))")
	public void afterPostAgentCombo(JoinPoint joinPoint) {
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean) joinPoint.getArgs()[0];
		System.out.println("=============POST代理请求的封装   用于登录后的请求（发短信、短信验证、爬取等等），该方法不会获取闲置实例，而是继续使用登录的实例完成后续操作=====切点方法的后置方法============");
		tracer.addTag("@Aspect afterLogin ", "==Start&End==" + e_CommerceJsonBean.toString());
	}
	
	
	/*@AfterThrowing(throwing="ex" , pointcut="execution(* app.service.aop.IAgent.postAgent(..))")
	public void postAgent(JoinPoint joinPoint,Throwable ex) {
		E_CommerceJsonBean e_CommerceJsonBean = (E_CommerceJsonBean) joinPoint.getArgs()[0];
		tracer.addTag("@Aspect postAgent ", "==Start==" + e_CommerceJsonBean.toString());
		System.out.println("中间层抛出的异常：：："+ex+"-----对应的TaskId：：："+e_CommerceJsonBean.getTaskid());
		E_CommerceTask e_CommerceTask = eCommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhase(),
				ex.getMessage(), E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getDescription(),
				E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getError_code(), true, e_CommerceJsonBean.getTaskid());
		tracer.addTag("@Aspect postAgent ", "==End==" + e_CommerceTask.toString());
		
		ecommerceOutput.e_commerce_output().send(MessageBuilder.withPayload(e_CommerceTask).build());
		
	}*/
	
	
}

