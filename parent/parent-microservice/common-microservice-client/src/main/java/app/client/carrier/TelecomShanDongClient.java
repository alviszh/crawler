package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-shandong")
public interface TelecomShanDongClient {

    /**
     * 发送手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/shandong/sendSms")
    public ResultData<TaskMobile> getphonecode(@RequestBody MessageLogin messageLogin);

    /**
     * 验证手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/shandong/setSmscode")
    public ResultData<TaskMobile> setphonecode(@RequestBody MessageLogin messageLogin);

    /**
     * 数据采集
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/shandong/crawler")
    public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin);
}
