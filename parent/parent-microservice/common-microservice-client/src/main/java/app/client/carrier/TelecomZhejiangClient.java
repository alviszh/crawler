package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("crawler-telecom-zhejiang")
public interface TelecomZhejiangClient {

    /**
     * 登录后到发送短信直接所需的步骤
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/intermediate")
    public ResultData<TaskMobile> intermediate(@RequestBody MessageLogin messageLogin);

    /**
     * 发送短信验证码
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/carrier/sendsms", method = RequestMethod.POST)
    public ResultData<TaskMobile> sendSms(@RequestBody MessageLogin messageLogin);

    /**
     * 验证短信验证码
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/carrier/validate", method = RequestMethod.POST)
    public ResultData<TaskMobile> validate(@RequestBody MessageLogin messageLogin);

    /**
     * 爬取总接口
     * @param messageLogin
     * @return
     */
    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);
}
