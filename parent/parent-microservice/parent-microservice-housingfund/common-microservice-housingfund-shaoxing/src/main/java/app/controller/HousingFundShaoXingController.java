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

import app.service.HousingFundShaoXingService;

@RestController
@Configuration
@RequestMapping("/housing/shaoxing")
public class HousingFundShaoXingController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundShaoXingController.class);
	@Autowired
	private HousingFundShaoXingService housingFundShaoXingService;

	// 登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundShaoXingService.login(messageLoginForHousing);
		return taskHousing;
	}

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundShaoXingService.getAllData(messageLoginForHousing);
		return taskHousing;

	}

}
