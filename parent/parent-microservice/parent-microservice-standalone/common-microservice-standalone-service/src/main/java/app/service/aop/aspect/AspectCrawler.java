package app.service.aop.aspect;


import app.commontracerlog.TracerLog;
import app.service.CrawlerStatusStandaloneService;
import app.service.amqp.outbound.Source;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectCrawler {

    @Autowired
    private TracerLog tracer;
    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;
    @Autowired
    private CrawlerStatusStandaloneService statusStandaloneService;
    @Autowired
    private Source standaloneOutput;

    /**
     * 爬取总接口
     * 使用@Around增强处理是为了通过proceedingJoinPoint.proceed()来拦截getAllData
     * 如果爬取正在进行中则不会调用proceed ，爬取方法getAllData将不会被执行
     * 如果不是爬取中，则调用proceed方法，使getAllData执行，通知更改taskMobile状态
     * @throws Throwable
     */
    @Before("execution(* app.service.aop.ICrawler.getAllData(..))")
    public void beforeGetAllData(JoinPoint joinPoint) {
        PbccrcJsonBean pbccrcJsonBean = (PbccrcJsonBean) joinPoint.getArgs()[0];
    /*@Around("execution(* app.service.aop.ICrawler.getAllData(..))")
    public void beforeGetAllData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        PbccrcJsonBean pbccrcJsonBean = (PbccrcJsonBean) proceedingJoinPoint.getArgs()[0];*/
        tracer.addTag("@Aspect beforeGetAllData ", "==Star=="+ pbccrcJsonBean.toString());
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());//mappingId==taskId
        if(taskStandalone!=null&&"CRAWLER".equals(taskStandalone.getPhase()) && "DOING".equals(taskStandalone.getPhase_status())){
            tracer.addTag("@Aspect--beforeGetAllData ", "==正在进行上次未完成的爬取任务==");
        }else {
            tracer.addTag("@Aspect--beforeGetAllData ", "==开始执行爬取任务==");
            taskStandalone = statusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_CRAWLER_DONING.getPhase(),
                    StandaloneEnum.STANDALONE_CRAWLER_DONING.getPhasestatus(), StandaloneEnum.STANDALONE_CRAWLER_DONING.getDescription(),
                    StandaloneEnum.STANDALONE_CRAWLER_DONING.getCode(), false, pbccrcJsonBean.getMapping_id());
            // 发送开始爬取通知
            standaloneOutput.output().send(MessageBuilder.withPayload(taskStandalone).build());
//            proceedingJoinPoint.proceed();//***重要！***调用了proceed方法，爬取方法getAllData 才会被执行******
        }
        tracer.addTag("@Aspect beforeGetAllData ", "==END=="+ pbccrcJsonBean.toString());

    }

    /**
     * 爬取征信报告后置通知 returnVal,切点方法执行后的返回值
     */
    @AfterReturning(value = "execution(* app.service.aop.ICrawler.getAllData(..))", returning = "reportResult")
    public void afterGetAllData(JoinPoint joinPoint,String reportResult) {
        PbccrcJsonBean pbccrcJsonBean = (PbccrcJsonBean) joinPoint.getArgs()[0];
        tracer.addTag("@Aspect afterGetAllData pbccrcJsonBean", "==Start"+ pbccrcJsonBean);
        tracer.addTag("@Aspect afterGetAllData", reportResult);
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
        // 发送开始爬取通知
        standaloneOutput.output().send(MessageBuilder.withPayload(taskStandalone).build());
        tracer.addTag("@Aspect afterGetAllData ==End", taskStandalone.toString());
    }


}
