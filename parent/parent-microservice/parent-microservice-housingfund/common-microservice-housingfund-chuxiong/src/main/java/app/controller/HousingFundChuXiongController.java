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
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.service.HousingChuXiongCrawlerService;

@RestController
@Configuration
@RequestMapping("/housing/chuxiong") 
public class HousingFundChuXiongController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundChuXiongController.class);
	@Autowired
	private HousingChuXiongCrawlerService housingChuXiongCrawlerService;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing=housingChuXiongCrawlerService.getAllData(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}

}
