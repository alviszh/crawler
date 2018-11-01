package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-hainan")
public interface TelecomHainanClient {

    /**
     * 海南第二次登录
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/loginhainan")
    public ResultData<TaskMobile> loginTwo(@RequestBody MessageLogin messageLogin);

    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);

    /**
     * 发送短信验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/getphonecode")
    public ResultData<TaskMobile> getphonecode(@RequestBody MessageLogin messageLogin);

    /**
     * 验证短信验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/setphonecode")
    public ResultData<TaskMobile> setphonecode(@RequestBody MessageLogin messageLogin);
}
