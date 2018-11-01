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

import app.commontracerlog.TracerLog;
import app.service.HousingChengduFutureService;

@RestController
@Configuration
@RequestMapping("/housing/chengdu") 
public class HousingFundChengduController extends HousingBasicController {
	
	@Autowired
	private TracerLog tracer;

	
	@Autowired
	private HousingChengduFutureService housingChengduFutureService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
			
		tracer.qryKeyValue("parser.login.taskid",messageLoginForHousing.getTask_id());
		housingChengduFutureService.login(messageLoginForHousing);
		return null;
		
		
	}
	
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		
		tracer.qryKeyValue("parser.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLoginForHousing.getNum());
		
		housingChengduFutureService.getAllData(messageLoginForHousing);
		
		return null;
		
		
	}

}
