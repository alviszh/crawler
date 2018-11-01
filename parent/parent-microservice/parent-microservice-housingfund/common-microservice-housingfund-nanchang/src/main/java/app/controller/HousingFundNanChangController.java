package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingNanChangService;

@RestController
@Configuration
@RequestMapping("/housing/nanchang") 
public class HousingFundNanChangController extends HousingBasicController {
	
	@Autowired
	private HousingNanChangService housingNanChangService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		if(null != taskHousing){
			
			taskHousing = housingNanChangService.login(messageLoginForHousing);
		}
		result.setData(taskHousing);
		return result;
	}

	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		taskHousing = housingNanChangService.getAllData(messageLoginForHousing);
		return taskHousing;
	}
}
