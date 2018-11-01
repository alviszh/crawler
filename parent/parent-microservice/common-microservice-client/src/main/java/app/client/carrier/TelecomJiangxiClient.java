package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-jiangxi")
public interface TelecomJiangxiClient {

    /**
     * 获取登录的短信验证码
     * 第二次登录（需要发送验证码）
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/jiangxilogin")
    public ResultData<TaskMobile> getphonecodelogin(@RequestBody MessageLogin messageLogin);

    /**
     * 验证短信验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/setphonecodelogin")
    public ResultData<TaskMobile> setphonecodelogin(@RequestBody MessageLogin messageLogin);

    /**
     * 爬取总接口
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);

    /**
     * 发送手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/getphonecode")
    public ResultData<TaskMobile> getphonecode(@RequestBody MessageLogin messageLogin);

    /**
     * 验证手机验证码
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/setphonecode")
    public ResultData<TaskMobile> setphonecode(@RequestBody MessageLogin messageLogin);
}
