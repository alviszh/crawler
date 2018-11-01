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
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingMianYangCrawlerService;

@RestController
@Configuration
@RequestMapping("/housing/mianyang") 
public class HousingFundMianYangController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundMianYangController.class);
	@Autowired
	private HousingMianYangCrawlerService housingMianYangCrawlerService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing=housingMianYangCrawlerService.login(messageLoginForHousing);
		if(taskHousing.getPhase().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus())!=-1){
			taskHousing=housingMianYangCrawlerService.getAllData(messageLoginForHousing);
		}		
		result.setData(taskHousing);
		return result;
	}

}
