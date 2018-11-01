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
import app.service.HousingFundEErDuoSiService;
/**
 * 鄂尔多斯公积金控制数据爬取的并不是登录成功之后的cookie,而是链接中括号之间的那个部分，必须从登陆到爬取
 * 保持一致才可以，故将登录和爬取写在同一个接口中
 * @author sln
 *
 */
@RestController
@Configuration
@RequestMapping("/housing/eerduosi") 
public class HousingFundEErDuoSiController extends HousingBasicController {
	public static final Logger log = LoggerFactory.getLogger(HousingFundEErDuoSiController.class);
	@Autowired
	private HousingFundEErDuoSiService housingFundEErDuoSiService;
	@Autowired
	private TracerLog tracer;
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.login.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.login.auth", "登录号码是：====>"+messageLoginForHousing.getNum()+"密码是：====>"+messageLoginForHousing.getPassword());
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		housingFundEErDuoSiService.login(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		housingFundEErDuoSiService.getAllData(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}		
	
}
