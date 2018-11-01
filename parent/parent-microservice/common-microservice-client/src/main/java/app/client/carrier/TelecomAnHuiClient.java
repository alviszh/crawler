package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-anhui")
public interface TelecomAnHuiClient {

    @PostMapping("/carrier/anhui/login")
    public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin);

    /**
     * 爬取接口（短信验证通过后爬取）
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawlerTwo(@RequestBody MessageLogin messageLogin);

    /**
     * 发送验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/getphonecode")
    public ResultData<TaskMobile> getphonecode(@RequestBody MessageLogin messageLogin);

    @PostMapping("/carrier/setphonecode")
    public ResultData<TaskMobile> setphonecode(@RequestBody MessageLogin messageLogin);
}
