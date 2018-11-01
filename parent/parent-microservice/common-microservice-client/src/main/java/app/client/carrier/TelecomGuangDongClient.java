package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-guangdong")
public interface TelecomGuangDongClient {

    @PostMapping("/carrier/guangdong/gdlogin")
    public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin);

    /**
     * 数据采集接口
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/guangdong/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);

    /**
     * 发送手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/guangdong/getphonecode")
    public ResultData<TaskMobile> getphonecode(@RequestBody MessageLogin messageLogin);

    /**
     * 验证手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/guangdong/setphonecode")
    public ResultData<TaskMobile> setphonecode(@RequestBody MessageLogin messageLogin);
}
