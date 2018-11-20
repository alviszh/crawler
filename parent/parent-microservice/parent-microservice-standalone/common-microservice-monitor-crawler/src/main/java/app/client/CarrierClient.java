package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;


/**
 * @author sln
 * @Description: 定时任务——运营商电信
 * (部分网站除了登陆这个步骤，其他步骤，如爬取、短信的发送和验证，也需要登陆信息
 * 或者经过加密或者其他操作后的登陆信息作为参数，故在调用其他接口的时候，将登陆信息也作参数传入)
 */
@FeignClient(name = "h5-proxy",configuration = ClientConfig.class, url = "${h5-proxy}")
//@FeignClient(name = "MOBILE-H5")
public interface CarrierClient { 
	//登陆
	@RequestMapping(value = "/h5/carrier/telecom/login",method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	ResultData<TaskMobile> login(@RequestParam("task_id") String taskid,
            @RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
            @RequestParam("province") String province);
	//爬取——可以跳过验证码的网站
	@RequestMapping(path = "/h5/carrier/telecom/getAllData",method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public @ResponseBody
    ResultData<TaskMobile> crawler(@RequestParam("task_id") String taskid,
			@RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
			@RequestParam("province") String province) ;
	
//	//爬取(部分省份在爬取数据的时候，需要短信验证码作为参数，例如：四川、湖北、广西、湖南、广东、江西、浙江)
	@RequestMapping(path = "/h5/carrier/telecom/getAllData",method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
	public @ResponseBody
	ResultData<TaskMobile> crawlerNeedSmsCode(@RequestParam("task_id") String taskid,
			@RequestParam("username") String username,
			@RequestParam("idNum") String idNum,
			@RequestParam("name") String phonenum,
			@RequestParam("password") String password,
			@RequestParam("user_id") Integer user_id,
			@RequestParam("province") String province,
			@RequestParam("sms_code") String sms_code) ;
	//===============================================================================
	//发送验证码(一次)
    @RequestMapping(value = "/h5/carrier/telecom/sendCode", method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public TaskMobile sendCode(@RequestParam("task_id") String taskid,
    		@RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
            @RequestParam("province") String province);
    //验证验证码(一次)	
    @RequestMapping(value = "/h5/carrier/telecom/verifiCode", method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public TaskMobile verifiCode(@RequestParam("task_id") String taskid,
    		@RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
    		@RequestParam("sms_code") String sms_code,
            @RequestParam("province") String province);

    
    //===============================================================================
	//发送验证码(二次)
    @RequestMapping(value = "/h5/carrier/telecom/sendCodeTwo", method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public TaskMobile sendCodeTwo(@RequestParam("task_id") String taskid,
    		@RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
            @RequestParam("province") String province);
   //验证验证码(二次)	
    @RequestMapping(value = "/h5/carrier/telecom/verifiCodeTwo", method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public TaskMobile verifiCodeTwo(@RequestParam("task_id") String taskid,
    		@RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
    		@RequestParam("sms_code") String sms_code,
            @RequestParam("province") String province);
  
    
    //===============================================================================
    //海南电信二次登录
    @RequestMapping(value = "/h5/carrier/telecom/loginTwo", method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public @ResponseBody ResultData<TaskMobile> loginTwo(@RequestParam("task_id") String taskid,
            @RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
            @RequestParam("province") String province);
            
    
    //浙江电信发送短信验证码之前的页面加载
    @RequestMapping(value = "/h5/carrier/telecom/intermediate", method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"})
    public @ResponseBody TaskMobile intermediate(@RequestParam("task_id") String taskid,
            @RequestParam("username") String username,
            @RequestParam("idNum") String idNum,
            @RequestParam("name") String phonenum,
            @RequestParam("password") String password,
            @RequestParam("user_id") Integer user_id,
            @RequestParam("province") String province);
	//======================================================================
}
