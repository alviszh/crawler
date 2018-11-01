package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("crawler-telecom-guangxi")
public interface TelecomGuangxiClient {

    @PostMapping("/carrier/guangxi/login")
    public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin);

    @PostMapping("/carrier/crawler")
    public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin);

    /*第一次获取短信验证码*/
    @PostMapping("/carrier/getphonecodeFirst")
    public ResultData<TaskMobile> getphonecodeFirst(@RequestBody MessageLogin messageLogin);

    /*第二次获取短信验证码*/
    @PostMapping("/carrier/getphonecodeTwo")
    public ResultData<TaskMobile> getphonecodeTwo(@RequestBody MessageLogin messageLogin);

    /*第一次验证短信验证码*/
    @PostMapping("/carrier/setphonecodeFirst")
    public ResultData<TaskMobile> setphonecodeFirst(@RequestBody MessageLogin messageLogin);

    /*第二次验证短信验证码*/
    @PostMapping("/carrier/setphonecodeTwo")
    public ResultData<TaskMobile> setphonecodeTwo(@RequestBody MessageLogin messageLogin);
}
