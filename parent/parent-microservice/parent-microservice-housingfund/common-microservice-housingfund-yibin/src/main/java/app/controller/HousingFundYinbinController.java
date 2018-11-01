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

import app.service.HousingYibinService;

@RestController
@Configuration
@RequestMapping("/housing/yibin") 
public class HousingFundYinbinController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundYinbinController.class);
	@Autowired
	private HousingYibinService  housingYibinService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		taskHousing.setCity(messageLoginForHousing.getCity());
		taskHousing.setPaymentStatus(201);
		save(taskHousing);
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing=housingYibinService.getAllData(messageLoginForHousing);		
		result.setData(taskHousing);
		return result;
	}

}
