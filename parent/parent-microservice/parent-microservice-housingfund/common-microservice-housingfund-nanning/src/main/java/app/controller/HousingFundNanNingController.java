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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingFundNanNingService;

@RestController
@Configuration
@RequestMapping("/housing/nanning") 
public class HousingFundNanNingController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundNanNingController.class);
	@Autowired
	private HousingFundNanNingService housingFundnanningService;

	// 登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundnanningService.login(messageLoginForHousing);
		return taskHousing;
	}

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundnanningService.getAllData(messageLoginForHousing);
		return taskHousing;

	}
	
	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public TaskHousing sendMsg(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		//
		housingFundnanningService.sendSms(messageLoginForHousing);
		return taskHousing;
		
	}
}
