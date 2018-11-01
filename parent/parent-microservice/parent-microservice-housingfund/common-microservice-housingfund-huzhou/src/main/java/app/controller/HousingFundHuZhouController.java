package app.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingFundHuZhouService;
/**
 * 湖州公积金网站在登陆的时候有进度条，用程序登录的时候，偶尔会报read time out超时异常，故决定提示：系统繁忙，请稍后再试
 * 
 * 爬取用户基本信息需要登录时候所用的个人账号，故开发该网站的时候将登录和爬取写在一起
 * @author sln
 *
 */
@RestController
@Configuration
@RequestMapping("/housing/huzhou") 
public class HousingFundHuZhouController extends HousingBasicController {
	public static final Logger log = LoggerFactory.getLogger(HousingFundHuZhouController.class);
	@Autowired
	private HousingFundHuZhouService housingFundHuZhouService;
	@Autowired
	private TracerLog tracer;
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.login.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.login.auth", "登录号码是：====>"+messageLoginForHousing.getNum()+"密码是：====>"+messageLoginForHousing.getPassword());
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		housingFundHuZhouService.login(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		housingFundHuZhouService.getAllData(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}		
	
}
