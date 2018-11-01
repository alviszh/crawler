package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.housingfund.json.TaskHousingfund;

/**
 * @author sln
 * @date 2018年8月23日上午11:02:20
 * @Description: 定时任务——公积金爬取
 */
//@FeignClient("HOUSINGFUND-H5")    //微服务的名字
@FeignClient(name = "proxy", url = "${proxy}")
public interface HousingFundClient { 	
	@RequestMapping(value = "/h5/fund/login",method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	TaskHousingfund login(@RequestParam("task_id") String task_id,
			@RequestParam("num") String num,
            @RequestParam("password") String password,
            @RequestParam("city") String city,
            @RequestParam("username") String username,
            @RequestParam("telephone") String telephone,
            @RequestParam("hosingFundNumber") String hosingFundNumber,
            @RequestParam("countNumber") String countNumber,
            @RequestParam("logintype") String logintype) ;
	
	@PostMapping(path = "/h5/fund/crawler",headers={"content-type=application/x-www-form-urlencoded"})
	TaskHousingfund crawler(@RequestParam("task_id") String task_id,
			@RequestParam("num") String num,
            @RequestParam("password") String password,
            @RequestParam("city") String city,
            @RequestParam("username") String username,
            @RequestParam("telephone") String telephone,
            @RequestParam("hosingFundNumber") String hosingFundNumber,
            @RequestParam("countNumber") String countNumber,
            @RequestParam("logintype") String logintype) ;

	
	/////////////////////////////////////////////////////////////////////////////////////
	
	/*//如下为最开始用的接口和参数，可以正确调用的（后为了适应所有的网站，改成如上方式）
	@RequestMapping(value = "/h5/fund/login",method = RequestMethod.POST,headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	TaskHousingfund login(@RequestParam("task_id") String task_id,
			@RequestParam("num") String num,
            @RequestParam("password") String password,
            @RequestParam("city") String city,
            @RequestParam("username") String username,
            @RequestParam("logintype") String logintype) ;
	
	//目前任务爬取数据只需要个taskid
	@PostMapping(path = "/h5/fund/crawler",headers={"content-type=application/x-www-form-urlencoded"})
	TaskHousingfund crawler(@RequestParam("task_id") String task_id,
			@RequestParam("city") String city) ;*/
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	
	//不能用如下方式，因为h5的controller中是根据前端页面输入框的映射关系将相关登陆信息转成MessageLoginForHousing的，没有用@RequestBody，所以，下列方式不能将实体中的
	//登陆信息传到h5的参数实体中，所以，爬取也需要更改
//	@PostMapping(path = "/h5/fund/login")
//	public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);
	
//	@PostMapping(path = "/h5/fund/crawler",headers="application/x-www-form-urlencoded")
//	public TaskHousingfund crawler(MessageLoginForHousing messageLoginForHousing);

}
