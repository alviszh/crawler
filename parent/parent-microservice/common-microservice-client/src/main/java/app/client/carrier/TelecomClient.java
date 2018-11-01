package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电信登录接口
 * Created by zmy on 2018/2/9.
 */
@FeignClient("crawler-telecom-login")
public interface TelecomClient {


    @PostMapping("/carrier/login")
    public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin);

}
