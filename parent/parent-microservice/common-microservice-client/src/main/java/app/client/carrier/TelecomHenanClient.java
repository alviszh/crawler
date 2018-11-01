package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-henan")
public interface TelecomHenanClient {
    /**
     * 爬取总接口
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/henan/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);

    /**
     * 发送手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/henan/sendSms")
    public ResultData<TaskMobile> telecomgetcode(@RequestBody MessageLogin messageLogin);

    /**
     * 验证手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/henan/setSmscode")
    public ResultData<TaskMobile> telecomsetcode(@RequestBody MessageLogin messageLogin);
}
