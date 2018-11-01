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

import app.service.HousingFundMaAnShanService;
import app.service.common.HousingBasicService;

@RestController
@Configuration
@RequestMapping("/housing/maanshan") 
public class HousingFundMaAnShanController extends HousingBasicService {
	public static final Logger log = LoggerFactory.getLogger(HousingFundMaAnShanController.class);
	@Autowired
	private HousingFundMaAnShanService housingFundMaAnShanService;
	//用安徽省直登陆
	/*@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.login.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.login.auth", "登录号码是：====>"+messageLoginForHousing.getNum()+"密码是：====>"+messageLoginForHousing.getPassword());
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		housingFundMaAnShanService.login(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}*/
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		housingFundMaAnShanService.getAllData(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}	
	
}
