package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingfundSiPingService;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;
import app.commontracerlog.TracerLog;

@RestController
@Configuration
@RequestMapping("/housing/siping") 
public class HousingfundSiPingController extends HousingBasicController{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundSiPingService housingfundSiPingService;
	@Autowired
	private HousingBasicService housingBasicService;
	@Autowired
	private ICrawlerLogin crawlerLogin;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.siping.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = crawlerLogin.login(messageLoginForHousing);
		return taskHousing;
		
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST )
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLogin){
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		TaskHousing taskHousing = crawlerLogin.getAllData(messageLogin);
//		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
//		System.out.println("------crawler----------");
//		try {
//			//流水
//			housingfundSiPingService.getcrawler(messageLogin,taskHousing);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("parser.getuserinfo.auth", e.toString());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskHousing.setFinished(true);
//			save(taskHousing);
//		}
//		
//		try {
//			//个人
//			housingfundSiPingService.getuserinfo(messageLogin,taskHousing);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("parser.getuserinfo.auth", e.toString());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskHousing.setFinished(true);
//			save(taskHousing);
//		}
//		housingBasicService.updateTaskHousing(messageLogin.getTask_id());
//		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
//		result.setData(taskHousing);
		return taskHousing;
	}
}
