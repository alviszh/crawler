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

import app.service.HousingFundsuzhouService;

@RestController
@Configuration
@RequestMapping("/housing/suzhou2")
public class HousingFundsuzhouController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundsuzhouController.class);
	@Autowired
	private HousingFundsuzhouService housingFundsuzhouService;

	// 登录
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundsuzhouService.login(messageLoginForHousing);
		return taskHousing;
	}

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundsuzhouService.getAllData(messageLoginForHousing);
		return taskHousing;

	}
}
