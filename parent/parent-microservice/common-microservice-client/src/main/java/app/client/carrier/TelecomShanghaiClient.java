package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-shanghai")
public interface TelecomShanghaiClient {

    @PostMapping("/carrier/sendsms")
    public ResultData<TaskMobile> sendSms(@RequestBody MessageLogin messageLogin);

    @PostMapping("/carrier/validate")
    public ResultData<TaskMobile> validate(@RequestBody MessageLogin messageLogin);

    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);
}
