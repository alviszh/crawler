package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import com.crawler.mobile.json.UnicomChangePasswordResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 中国联通
 */
@FeignClient("crawler-unicom")
public interface UnicomLoginClient {
    /**
     * 登录
     * @param messageLogin
     * @return
     */
    @PostMapping(value = "/carrier/login")
    public TaskMobile unicomlong(@RequestBody MessageLogin messageLogin);

    /**
     * 数据采集
     * @param messageLogin
     * @return
     */
    @PostMapping(value = "/carrier/crawler")
    public TaskMobile unicom(@RequestBody MessageLogin messageLogin);

    //获取短信验证
    @PostMapping(value = "/carrier/getphonecode")
    public ResultData<TaskMobile> getphonecode(@RequestBody MessageLogin messageLogin);

    //验证短信验证码
    @PostMapping(value = "/carrier/setphonecodeAgent")
    public TaskMobile setphonecode(@RequestBody MessageLogin messageLogin);

    //验证第二次短信验证码
    @PostMapping(value = "/carrier/verifySmsTwice")
    public ResultData<TaskMobile> verifySmsTwice(@RequestBody MessageLogin messageLogin);

    //发送第二次短信验证码
    @PostMapping(value = "/carrier/sendSmsTwice")
    public ResultData<TaskMobile> sendSmsTwice(@RequestBody MessageLogin messageLogin);

    /**
     * 图片验证码登录
     * @param result
     * @return
     */
    @PostMapping(value = "/carrier/passwordlogin")
    public TaskMobile unicompasswordlogin(@RequestBody UnicomChangePasswordResult result);

    /**
     * 发送短信随机码
     * @param result
     * @return
     */
    @PostMapping(value = "/carrier/passwordgetCode")
    public TaskMobile unicompasswordgetCode(@RequestBody UnicomChangePasswordResult result);

    /**
     * 验证短信随机码
     * @param result
     * @return
     */
    @PostMapping(value = "/carrier/passwordsetCode")
    public TaskMobile unicompasswordsetCode(@RequestBody UnicomChangePasswordResult result);

    /**
     * 密码变更
     * @param result
     * @return
     */
    @PostMapping(value = "/carrier/passwordchange")
    public TaskMobile unicompasswordchange(@RequestBody UnicomChangePasswordResult result);
}
