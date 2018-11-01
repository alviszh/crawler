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

import app.service.HousingFundShenZhenService;
@RestController
@Configuration
@RequestMapping("/housing/shenzhen") 
public class HousingFundShenZhenController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundShenZhenController.class);
	@Autowired
	private HousingFundShenZhenService housingFundTianJinService;
	//爬取信息
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingFundTianJinService.getAllData(messageLoginForHousing);
		return taskHousing;

	}
}
