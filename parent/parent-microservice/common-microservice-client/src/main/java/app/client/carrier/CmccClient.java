package app.client.carrier;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("crawler-cmcc")
public interface CmccClient {

    /**
     * 发送登录短信随机码
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/carrier/cmccsendSMS", method = RequestMethod.POST)
    public TaskMobile sendSMS(@RequestBody MessageLogin messageLogin);

    /**
     * 登录中国移动
     * @param messageLogin
     * @return
     */
//    @PostMapping(value="/carrier/cmcclogin")
    @RequestMapping(value = "/carrier/cmcclogin", method = RequestMethod.POST)
    public TaskMobile login(@RequestBody MessageLogin messageLogin);

    /**
     * 发送第二次认证短信随机码
     * @param messageLogin
     * @return
     */
//    @PostMapping(value="/carrier/cmccsendVerifySMS")
    @RequestMapping(value = "/carrier/cmccsendVerifySMS", method = RequestMethod.POST)
    public TaskMobile sendVerifySMS(@RequestBody MessageLogin messageLogin);

    /**
     * 第二次验证
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/carrier/cmccsecondAttestation", method = RequestMethod.POST)
    public TaskMobile secondAttestation(@RequestBody MessageLogin messageLogin);

    /**
     * 获取通话详单
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/carrier/cmccGetCallRecord", method = RequestMethod.POST)
    public String getCallRecord(@RequestBody MessageLogin messageLogin);

    /**
     * 爬取总接口
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/carrier/cmccGetAllData", method = RequestMethod.POST)
    public String getAllData(@RequestBody MessageLogin messageLogin);
}
