package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-jiangsu")
public interface TelecomJiangSuClient {
    /**
     * 江苏登录
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/jiangsu/login")
    public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin);

    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);
}
