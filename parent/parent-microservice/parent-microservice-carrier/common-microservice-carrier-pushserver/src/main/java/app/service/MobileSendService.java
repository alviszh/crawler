package app.service;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import com.crawler.NoticeInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;

/**
 *  推送前置规则、推送状态
 */

@Component
public class MobileSendService{
    @Autowired
    private TracerLog tracer;
    @Autowired
    private MobilePrecedingRuleService mobilePrecedingRuleService;

    /**
     * 推送前置规则
     * @param taskStandalone
     * @param reportDataResultStr
     */
    public void sendPrecedingRule(TaskMobile taskMobile){
    	tracer.qryKeyValue("retryPrecedingRule-start",taskMobile.getTaskid());
        try {
            String sendCreditReport = mobilePrecedingRuleService.retryPrecedingRule(taskMobile);
            System.out.println("推送结果：" + sendCreditReport);
        } catch (RuntimeException e) {
            tracer.addTag("前置规则推送失败-重试三次", ExUtils.getEDetail(e));
        }
        tracer.qryKeyValue("retryPrecedingRule-end",taskMobile.getTaskid());

    }

    /**
     * 推送通知
     * @param taskStandalone
     */
    public void sendMessageResult(TaskMobile taskMobile){
        tracer.qryKeyValue("taskid", taskMobile.getTaskid());
        if (taskMobile.getOwner().equals("dajinrong")) {
            tracer.qryKeyValue("推送状态", "借么");
            Gson gson = new GsonBuilder().create();
            String timeStamp = String.valueOf(System.currentTimeMillis());
            NoticeInfo messageResult = new NoticeInfo(taskMobile.getKey(), taskMobile.getBasicUser().getId()+"",
                    taskMobile.getTaskid(),taskMobile.getPhase() , taskMobile.getDescription(), timeStamp);
            tracer.addTag("发送状态",gson.toJson(messageResult));
            System.out.println("发送状态:" + taskMobile.getDescription());
//            pbccrcClientService.sendMessageResultJiemo(gson.toJson(messageResult));
        }
    }
}
