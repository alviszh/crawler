package app.service.aop.aspect;


import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
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
public class AspectMobileReport {

    @Autowired
    private Source carrierOutput;

    @Autowired
    private TracerLog tracer;

    @Autowired
    private TaskMobileRepository taskMobileRepository;

    /**
     * 生成运营商报告 前置通知
     */
    @Before("execution(* com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportRepository.proMobileReport(..))")
    public void beforeProMobileReport(JoinPoint joinPoint) {
        String taskid = (String)joinPoint.getArgs()[0];
        tracer.addTag("@Aspect beforeProMobileReport ", "==Start==taskid="+taskid);
        TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskid);
        taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_REPORT_DONING.getPhase());
        taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_REPORT_DONING.getPhasestatus());
        taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_REPORT_DONING.getDescription());
        taskMobile = taskMobileRepository.save(taskMobile);
        tracer.addTag("@Aspect beforeProMobileReport ", "==SEND MESSAGE=="+taskMobile.toString());

        // 生产报告通知
        carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
    }

    /**
     * 生成运营商报告 后置通知 returnVal,切点方法执行后的返回值
     */

    @AfterReturning(value = "execution(* com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportRepository.proMobileReport(..))", returning = "String")
    public void afterProMobileReport(JoinPoint joinPoint) {
        // JoinPoint joinPoint
        String taskid = (String)joinPoint.getArgs()[0];
        tracer.addTag("@Aspect afterProMobileReport ", "==Start==taskid=" +  taskid);
        TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskid);
        tracer.addTag("@Aspect afterProMobileReport ", "==SEND MESSAGE=="+taskMobile.toString());
        // 生产报告通知
        carrierOutput.output().send(MessageBuilder.withPayload(taskMobile).build());
    }
}
