package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import app.commontracerlog.TracerLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@RestController
@Configuration
@RequestMapping("/housing/cangzhou") 
public class HousingfundCangZhouController extends HousingBasicController{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ICrawlerLogin crawlerLogin;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.cangzhou.login", messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing = crawlerLogin.login(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public String crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.cangzhou.login", messageLoginForHousing.getTask_id());
		
		crawlerLogin.getAllData(messageLoginForHousing);
		
		return "SUCCESS";
	}
	
}
