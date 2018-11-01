package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.controller.HousingBasicController;
import app.service.HousingBeiJingCentFutureService;

@RestController
@Configuration
@RequestMapping("/housing/beijing/center") 
public class HousingFundBeiJingController extends HousingBasicController {

	@Autowired
	private HousingBeiJingCentFutureService housingBeiJingCentFutureService;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {

		housingBeiJingCentFutureService.login(messageLoginForHousing);		
		return null;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {

		housingBeiJingCentFutureService.getAllData(messageLoginForHousing);	
		
		return null;
	}

}
