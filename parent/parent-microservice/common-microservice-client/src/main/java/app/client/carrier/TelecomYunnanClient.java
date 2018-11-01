package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-yunnan")
public interface TelecomYunnanClient {

    /**
     * 爬取总接口
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin);

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
