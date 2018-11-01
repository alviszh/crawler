package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingTaianFutureService;

@RestController
@Configuration
@RequestMapping("/housing/taian")
public class HousingFundTaianController extends HousingBasicController {

	
	@Autowired
	private HousingTaianFutureService housingTaianFutureService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {		
		tracer.addTag("action.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.crawler.auth",messageLoginForHousing.getNum());	
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		try {
			housingTaianFutureService.login(messageLoginForHousing);
		} catch (Exception e) {
			tracer.addTag("HousingFundTaianController.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
		}		
		result.setData(taskHousing);
		return result;		
	}

}
