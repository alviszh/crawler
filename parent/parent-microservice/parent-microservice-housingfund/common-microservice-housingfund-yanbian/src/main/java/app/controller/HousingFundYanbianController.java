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

import app.service.HousingYanbianFutureService;

@RestController
@Configuration
@RequestMapping("/housing/yanbian") 
public class HousingFundYanbianController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundYanbianController.class);
	@Autowired
	private HousingYanbianFutureService housingYanbianFutureService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing.setCity(messageLoginForHousing.getCity());
		save(taskHousing);		
		taskHousing=housingYanbianFutureService.crawler(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}

}
