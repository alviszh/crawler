package app.amqp;


import app.service.amqp.inbound.Sink;
import app.service.pbccrc.PbccrcSendService;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.microservice.dao.entity.crawler.pbccrc.PlainPbccrcJson;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.PlainPbccrcJsonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import app.commontracerlog.TracerLog;

@EnableBinding(Sink.class)
public class StandaloneEventListener {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private PbccrcSendService pbccrcSendService;
    @Autowired
    private PlainPbccrcJsonRepository plainPbccrcJsonRepository;

    @StreamListener(Sink.INPUT)
    public void messageSink(TaskStandalone taskStandalone) {
        tracer.addTag("Task Received start taskStandalone=", taskStandalone.toString());

        //人行征信
        if (taskStandalone.getServiceName() != null && !"".equals(taskStandalone.getServiceName())) {
            if (taskStandalone.getServiceName().contains("pbccrc")) {
                //推送状态
                tracer.qryKeyValue("listener.sendMessageResult.start", "开始状态的推送");
                pbccrcSendService.sendMessageResult(taskStandalone);
                tracer.qryKeyValue("listener.sendMessageResult.end", "结束状态的推送");

                //数据采集完成 推送前置规则
                if (taskStandalone.getFinished() && taskStandalone.getPhase().equals(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getPhase())
                        && taskStandalone.getPhase_status().equals(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getPhasestatus())) {
                    System.out.println(" pbccrc ==========数据采集完成 推送前置规则");
                    //推送前置规则
                    PlainPbccrcJson plainPbccrcJson = plainPbccrcJsonRepository.findByMappingId(taskStandalone.getTaskid());
                    tracer.addTag("StandaloneEventListener messageSink sendPrecedingRule start", "开始前置规则的推送");
                    pbccrcSendService.sendPrecedingRule(taskStandalone, plainPbccrcJson.getJson_v1());
                    tracer.addTag("StandaloneEventListener messageSink sendPrecedingRule end", "结束前置规则的推送");
                }
            }
        }

        tracer.addTag("Task Received end taskStandalone=", taskStandalone.toString());
    }
}
