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

import app.commontracerlog.TracerLog;
import app.service.HousingXiNingCommonService;

@RestController
@Configuration
@RequestMapping("/housing/xining")
public class HousingFundXiNingController extends HousingBasicController {
	@Autowired
	private HousingXiNingCommonService housingXiNingCommonService;
	@Autowired
	private TracerLog tracer;
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {		
		tracer.addTag("parser.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLoginForHousing.getNum());	
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing=housingXiNingCommonService.login(messageLoginForHousing);
		if(taskHousing.getPhase().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus())!=-1){
			taskHousing=housingXiNingCommonService.getAllData(messageLoginForHousing);
		}		
		result.setData(taskHousing);
		return result;		
	}
	

}
