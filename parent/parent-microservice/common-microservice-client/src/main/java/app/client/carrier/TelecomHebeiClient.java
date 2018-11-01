package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-hebei")
public interface TelecomHebeiClient {

    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin);

    /**
     * 发送短信验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/sendsms")
    public ResultData<TaskMobile> sendSMS(@RequestBody MessageLogin messageLogin);
}
