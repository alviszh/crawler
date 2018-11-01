package app.controller;


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

import app.service.HousingBayanzhuoerFutureService;

@RestController
@Configuration
@RequestMapping("/housing/bayanzhuoer")
public class HousingFundBayanzhuoerController extends HousingBasicController {

	
	@Autowired
	private HousingBayanzhuoerFutureService housingBayanzhuoerFutureService;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		tracer.addTag("action.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.crawler.auth",messageLoginForHousing.getNum());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing=housingBayanzhuoerFutureService.login(messageLoginForHousing);
		if(taskHousing.getPhase().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus())!=-1){
			taskHousing=housingBayanzhuoerFutureService.getAllData(messageLoginForHousing);
		}		
		result.setData(taskHousing);
		return result;
		
		
	}

}
