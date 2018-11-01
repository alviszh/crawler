package app.service.aop.aspect;


import app.commontracerlog.TracerLog;
import app.service.CrawlerStatusStandaloneService;
import app.service.amqp.outbound.Source;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectAgent {

    @Autowired
    private TracerLog tracer;
    @Autowired
    private CrawlerStatusStandaloneService crawlerStatusStandaloneService;
    @Autowired
    private Source standaloneOutput;
    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;

    @Before("execution(* app.service.aop.IAgent.releaseInstance(..))")
    public void beforeReleaseInstance(JoinPoint joinPoint) {
        String instanceIpAddr = (String) joinPoint.getArgs()[0];
        System.out.println("=============释放资源  （释放这台电脑 by IP，关闭WebDriver）=====切点方法的前置方法========目前没有通用业务逻辑=======");
        tracer.addTag("@Aspect beforeReleaseInstance ", "==Start&END==" + instanceIpAddr.toString());
    }
    @AfterReturning(value = "execution(* app.service.aop.IAgent.releaseInstance(..))")
    public void afterReleaseInstance(JoinPoint joinPoint) {
        String instanceIpAddr = (String) joinPoint.getArgs()[0];
        System.out.println("=============释放资源  （释放这台电脑 by IP，关闭WebDriver）=====切点方法的后置方法============");
        tracer.addTag("@Aspect afterReleaseInstance ", "==Start&End==" + instanceIpAddr.toString());
    }

    @AfterThrowing(throwing="ex" , pointcut="execution(* app.service.aop.IAgent.postAgent(..))")
    public void postAgent(JoinPoint joinPoint,Throwable ex) {
        PbccrcJsonBean pbccrcJsonBean = (PbccrcJsonBean) joinPoint.getArgs()[0];
        tracer.addTag("@Aspect postAgent ", "中间层抛出异常：" + ex.getMessage());
        tracer.addTag("@Aspect postAgent ", "==Start==" + pbccrcJsonBean.toString());
        System.out.println("中间层抛出的异常：：："+ex+"-----对应的TaskId：：："+ pbccrcJsonBean.getMapping_id());

        TaskStandalone taskStandalone = crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_AGENT_ERROR.getPhase(),
                StandaloneEnum.STANDALONE_AGENT_ERROR.getPhasestatus(), StandaloneEnum.STANDALONE_AGENT_ERROR.getDescription(),
                StandaloneEnum.STANDALONE_AGENT_ERROR.getCode(), true, pbccrcJsonBean.getMapping_id());
        tracer.addTag("@Aspect postAgent ", "==End==" + taskStandalone.toString());

        standaloneOutput.output().send(MessageBuilder.withPayload(taskStandalone).build());
    }
}
