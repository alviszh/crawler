package app.client.pbccrc;

import app.client.StandaloneClientConfig;
import com.crawler.callback.json.pbccrc.JiemoPrecedingRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by zmy on 2018/6/19.
 */

/**
 * 推送前置规则
 * 借么（大金融）
 */
//@FeignClient(name = "pbccrc", url = "http://localhost:3060")
@FeignClient(name = "pbccrc", url = "${dajinrong.precedingrule.url}",configuration = StandaloneClientConfig.class)
public interface PbccrcResultDajinrongClient {

    //推送前置规则
    /*@PostMapping("/pbccrc/sendMessageResultTest")
    String sendPrecedingRuleJiemo(@RequestBody Result precedingRule);*/
    @PostMapping("/jieme/crawler/zhengxin/data.do")
    String sendPrecedingRuleJiemo(@RequestBody JiemoPrecedingRule jiemoPrecedingRule);
}
