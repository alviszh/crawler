package app.controller;

import java.util.Set;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;
import com.gargoylesoftware.htmlunit.util.Cookie;
import app.service.HousingQuanZhouFutureService;

@RestController
@Configuration
@RequestMapping("/housing/quanzhou") 
public class HousingFundQuanzhouController extends HousingBasicController {
	public static final Logger log = LoggerFactory.getLogger(HousingFundQuanzhouController.class);
	
	@Autowired
	private HousingQuanZhouFutureService housingQuanZhouFutureService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();

		taskHousing = housingQuanZhouFutureService.login(messageLoginForHousing);
		
		result.setData(taskHousing);
		return result;
	}
	
//	@RequestMapping(value = "/code", method = RequestMethod.POST)
//	public ResultData<TaskHousing> code(@RequestBody MessageLoginForHousing messageLoginForHousing) {
//		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
//
//		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
//
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		//taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//		save(taskHousing);
//		
//		housingQuanZhouFutureService.code(messageLoginForHousing, taskHousing);
//		
//		result.setData(taskHousing);
//		return result;
//	}
//	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		
		taskHousing = housingQuanZhouFutureService.getAllData(messageLoginForHousing);
		
		result.setData(taskHousing);
		return result;
	}
}
