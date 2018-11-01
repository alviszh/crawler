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
import app.service.HousingFundHandanService;

@RestController
@Configuration
@RequestMapping("/housing/handan") 
public class HousingFundHandanController extends HousingBasicController {
	
	@Autowired
	private TracerLog tracer;	
	@Autowired
	private HousingFundHandanService housingFundHandanService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing){
		
		tracer.addTag("HousingFundHandanController.crawler", messageLoginForHousing.getTask_id());
		
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//		taskHousing.setLoginMessageJson(gs.toJson(messageLoginForHousing));
//		taskHousing = taskHousingRepository.save(taskHousing);
//		
		housingFundHandanService.crawler(taskHousing,messageLoginForHousing);
		
		return taskHousing;
		
	}

}
