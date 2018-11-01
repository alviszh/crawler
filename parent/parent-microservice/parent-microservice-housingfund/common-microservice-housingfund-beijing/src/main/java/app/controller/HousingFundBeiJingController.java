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

import app.controller.HousingBasicController;
import app.service.HousingBeiJingCrawlerService;

@RestController
@Configuration
@RequestMapping("/housing/beijing") 
public class HousingFundBeiJingController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundBeiJingController.class);
	@Autowired
	private HousingBeiJingCrawlerService housingBeiJingCrawlerService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {

		housingBeiJingCrawlerService.crawler(messageLoginForHousing);
		
		return null;
	}

}
