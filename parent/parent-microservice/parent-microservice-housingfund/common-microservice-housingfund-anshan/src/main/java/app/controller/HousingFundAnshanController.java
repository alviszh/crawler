package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingFundAnshanService;
    
  

@RestController  
@Configuration
@RequestMapping("/housing/anshan")
public class HousingFundAnshanController extends HousingBasicController{
	
	@Autowired  
	private HousingFundAnshanService housingFundAnshanService;
	@Autowired
	private TracerLog tracer;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing housingFundLogin(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());		tracer.addTag("action.housingFund.anshan.login.start",messageLoginForHousing.getTask_id());
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
//		save(taskHousing);
		
		taskHousing = housingFundAnshanService.login(messageLoginForHousing);
		return taskHousing;
		
	}
		
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing TaskHousinghousingFundCrawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.anshan.housingFund.crawler.start",messageLoginForHousing.getTask_id());
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getDescription());
//		taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getError_code());
		 taskHousing = housingFundAnshanService.getAllData(messageLoginForHousing);
		return taskHousing;
	
	
	}
	
}
