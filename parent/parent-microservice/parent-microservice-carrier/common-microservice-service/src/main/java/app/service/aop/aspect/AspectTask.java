package app.service.aop.aspect;


import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;
import com.crawler.mobile.json.MobileJsonBean;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectTask {

    @Autowired
    private Source carrierOutput;

    @Autowired
    private TracerLog tracer;

    @Autowired
    private TaskMobileRepository taskMobileRepository;

    /**
     * 前置通知
     */
	@Before("execution(* app.service.aop.ITask.createTask(..))")
	public void beforeCreateTask(JoinPoint joinPoint) {
        MobileJsonBean mobileJsonBean = (MobileJsonBean)joinPoint.getArgs()[0];
        tracer.addTag("@Aspect beforeCreateTask ", "==Start&END==" + mobileJsonBean);
	}

    /**
     * 后置通知 returnVal,切点方法执行后的返回值
     */

    @AfterReturning(value = "execution(* app.service.aop.ITask.createTask(..))", returning = "taskMobile")
    public void afterCreateTask(TaskMobile taskMobile) {
        tracer.addTag("@Aspect afterCreateTask ", "==Start==" + taskMobile.toString());
        if (null != taskMobile) {
            //通知
            carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
        }
        tracer.addTag("@Aspect afterCreateTask ", "==END==" + taskMobile.toString());
    }
}
