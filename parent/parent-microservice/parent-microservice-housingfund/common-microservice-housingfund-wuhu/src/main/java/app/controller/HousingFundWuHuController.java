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

import app.service.HousingFundWuHuService;

@RestController
@Configuration
@RequestMapping("/housing/wuhu") 
public class HousingFundWuHuController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundWuHuController.class);
	@Autowired
	private HousingFundWuHuService housingFundWuHuService;
	//登录
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {		
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing	taskHousing=housingFundWuHuService.getAllData(messageLoginForHousing);		
		result.setData(taskHousing);		
		return result;
	}
}
