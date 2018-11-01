package app.service.pbccrc;

import app.commontracerlog.TracerLog;
import com.crawler.pbccrc.json.MessageResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  推送前置规则、推送状态
 */

@Component
public class PbccrcSendService{
    @Autowired
    private TracerLog tracer;
    @Autowired
    private PbccrcPrecedingRuleService pbccrcPrecedingRuleService;
    @Autowired
    private PbccrcClientService pbccrcClientService;


    /**
     * 推送前置规则
     * @param taskStandalone
     * @param reportDataResultStr
     */
    public void sendPrecedingRule(TaskStandalone taskStandalone, String reportDataResultStr){
        System.out.println("======retryPrecedingRule start============");
        try {
            String sendCreditReport = pbccrcPrecedingRuleService.retryPrecedingRule(taskStandalone,
                    reportDataResultStr);
            System.out.println("推送结果：" + sendCreditReport);
        } catch (RuntimeException e) {
            tracer.qryKeyValue("前置规则推送失败-重试三次", e.getMessage());
        }
        System.out.println("======retryPrecedingRule end============");

    }

    /**
     * 推送状态
     * @param taskStandalone
     */
    public void sendMessageResult(TaskStandalone taskStandalone){
        tracer.qryKeyValue("mappingId", taskStandalone.getTaskid());
        if (taskStandalone.getOwner().equals("dajinrong")) {
            tracer.qryKeyValue("推送状态", "借么");
            Gson gson = new GsonBuilder().create();
            MessageResult messageResult = new MessageResult(taskStandalone.getKey(),
                    taskStandalone.getCode()+"", taskStandalone.getDescription());
            tracer.addTag("发送状态",gson.toJson(messageResult));
            System.out.println("发送状态:" + taskStandalone.getDescription());
            pbccrcClientService.sendMessageResultJiemo(gson.toJson(messageResult));
        }
    }
}
