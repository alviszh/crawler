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
import app.service.HousingTaiyuanFutureService;

@RestController
@Configuration
@RequestMapping("/housing/taiyuan")
public class HousingFundTaiyuanController extends HousingBasicController {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private HousingTaiyuanFutureService housingTaiyuanFutureService;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		tracer.qryKeyValue("parser.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLoginForHousing.getNum());
		
		try {
			housingTaiyuanFutureService.login(messageLoginForHousing);
		} catch (Exception e) {
			tracer.addTag("HousingFundTaiyuanController.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		tracer.qryKeyValue("parser.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLoginForHousing.getNum());
		
		housingTaiyuanFutureService.getAllData(messageLoginForHousing);

		return null;
		
		
	}

}
