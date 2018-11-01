package app.service.aop.aspect;

import app.client.monitor.MailClient;
import app.commontracerlog.TracerLog;
import app.service.amqp.outbound.Source;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 推送前置规则的切面
 */
@Aspect
@Component
public class AspectPrecedingRule{

    @Autowired
    private TracerLog tracer;
    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;
    @Autowired
    private Source standaloneOutput;
    @Autowired
    private MailClient mailClient;


    /**
     * 回调接口-汇诚
     * @param joinPoint
     * @param ex
     */

//    @AfterThrowing(throwing="ex" , pointcut="execution(* app.client.pbccrc.PbccrcResultClient.sendCreditReport(..))")
    @AfterThrowing(throwing="ex" , pointcut="execution(* app.service.aop.IPrecedingRule.retryPrecedingRule(..))")
    public void sendPrecedingRule(JoinPoint joinPoint,Throwable ex) {
        tracer.addTag("进入 @Aspect sendPrecedingRule 回调接口异常",  "******");
        TaskStandalone taskStandalone = (TaskStandalone) joinPoint.getArgs()[0];
        tracer.addTag("@Aspect sendPrecedingRule 回调接口异常 start&end",  taskStandalone.toString());
        System.out.println("回调接口抛出的异常：：" + ex + "-----对应的TaskId：：：" + taskStandalone.getTaskid());

        tracer.addTag("@Aspect sendPrecedingRule  发送邮件 Start",  taskStandalone.toString());
        PbccrcJsonBean pbccrcJsonBean = new PbccrcJsonBean();
        pbccrcJsonBean.setOwner(taskStandalone.getOwner());
        pbccrcJsonBean.setMapping_id(taskStandalone.getTaskid());
        pbccrcJsonBean.setKey(taskStandalone.getKey());
        mailClient.pbccrcmail(pbccrcJsonBean, ex+"");
        tracer.addTag("@Aspect sendPrecedingRule  发送邮件 End", pbccrcJsonBean.toString());

//        standaloneOutput.output().send(MessageBuilder.withPayload(taskStandalone).build());
    }
}
