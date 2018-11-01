package app.client.pbccrc;
import app.client.StandaloneClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 推送状态
 * 借么（大金融）
 * Created by zmy on 2018/7/11.
 */
//@FeignClient(name = "pbccrc", url = "http://localhost:3060")
@FeignClient(name = "pbccrc", url = "${dajinrong.message.url}",configuration = StandaloneClientConfig.class)
public interface PbccrcDajinrongClient {

    //推送状态
//    @PostMapping("/pbccrc/sendMessageResultTest")
    @PostMapping("/api/tianxi/callback.do")
    String sendMessageResultJiemo(@RequestBody String messageResult);
}
