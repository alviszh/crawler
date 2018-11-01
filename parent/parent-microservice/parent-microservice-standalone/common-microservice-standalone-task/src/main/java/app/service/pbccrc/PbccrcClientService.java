package app.service.pbccrc;

import app.client.pbccrc.PbccrcDajinrongClient;
import app.commontracerlog.TracerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zmy on 2018/7/11.
 */

@Component
public class PbccrcClientService {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private PbccrcDajinrongClient pbccrcDajinrongClient;

    @Value("${dajinrong.message.url}")
    String jiemo_message_url;

    @Async
    public String sendMessageResultJiemo(String messageResult) {
        tracer.addTag("Async，推送异步状态",messageResult);
        System.out.println("推送异步状态:" + messageResult);
        String resultJiemo = null;
        try {
            resultJiemo = pbccrcDajinrongClient.sendMessageResultJiemo(messageResult);
        } catch (Exception e) {
            tracer.qryKeyValue("sendMessageResultJiemo.exception","状态发送失败");
            tracer.addTag("状态发送失败",e.toString());
        }
        tracer.qryKeyValue("大金融-借么-推送异步状态地址", jiemo_message_url+"/api/tianxi/callback.do");
        tracer.addTag("Async，推送异步状态-结果", resultJiemo);
        System.out.println("推送异步状态-结果=" + resultJiemo);
        return resultJiemo;
    }
}
