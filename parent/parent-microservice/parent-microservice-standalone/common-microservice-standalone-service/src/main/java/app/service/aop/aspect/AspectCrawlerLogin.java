package app.service.aop.aspect;

import app.commontracerlog.TracerLog;
import app.service.CrawlerStatusStandaloneService;
import app.service.amqp.outbound.Source;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Aspect
@Component
@EnableBinding(Source.class)
public class AspectCrawlerLogin {

    @Autowired
    private TracerLog tracer;
    @Autowired
    private Source standaloneOutput;
    @Autowired
    private CrawlerStatusStandaloneService statusStandaloneService;
    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;

    private Gson gson = new Gson();

    /**
     * 登陆前置通知
     */
    @Before("execution(* app.service.aop.ICrawlerLogin.login(..))")
    public void beforeLogin(JoinPoint joinPoint) {
        PbccrcJsonBean pbccrcJsonBean = (PbccrcJsonBean) joinPoint.getArgs()[0];
        tracer.addTag("@Aspect beforeLogin ", "==Start==" + pbccrcJsonBean.toString());
        TaskStandalone taskStandalone = statusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_LOGIN_DONING.getPhase(),
                StandaloneEnum.STANDALONE_LOGIN_DONING.getPhasestatus(), StandaloneEnum.STANDALONE_LOGIN_DONING.getDescription(),
                StandaloneEnum.STANDALONE_LOGIN_DONING.getCode(), false, pbccrcJsonBean.getMapping_id());
        tracer.addTag("@Aspect beforeLogin ", "==END==" + taskStandalone.toString());
        // 发送开始登陆通知
        standaloneOutput.output().send(MessageBuilder.withPayload(taskStandalone).build());
    }

    /**
     * 登录后置通知 returnVal,切点方法执行后的返回值
     */
    @AfterReturning(value = "execution(* app.service.aop.ICrawlerLogin.login(..))", returning = "reportResult")
    public void afterLogin(JoinPoint joinPoint,String reportResult) {
        PbccrcJsonBean pbccrcJsonBean = (PbccrcJsonBean) joinPoint.getArgs()[0];
        tracer.addTag("@Aspect afterLogin ", "== login ==" + pbccrcJsonBean.toString());
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());

        // 发送结束登陆通知
        standaloneOutput.output().send(MessageBuilder.withPayload(taskStandalone).build());
    }
}
