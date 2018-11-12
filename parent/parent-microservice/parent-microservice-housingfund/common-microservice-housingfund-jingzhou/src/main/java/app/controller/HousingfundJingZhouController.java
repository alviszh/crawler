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
import com.crawler.mobile.json.StatusCodeLogin;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingfundJingZhouService;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;


@RestController
@Configuration
@RequestMapping("/housing/jingzhou") 
public class HousingfundJingZhouController extends HousingBasicController{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundJingZhouService housingfundJingZhouService;
	@Autowired
	private HousingBasicService housingBasicService;
	@Autowired
	private ICrawlerLogin iCrawlerLogin;
	@Autowired
	private ICrawler iCrawler;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.JingZhou.login", messageLoginForHousing.getTask_id());
	/*	ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		Gson gson = new Gson();
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		taskHousing.setLogintype(StatusCodeLogin.IDNUM);
		taskHousing.setLoginMessageJson(gson.toJson(messageLoginForHousing));
		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		save(taskHousing);
		housingfundJingZhouService.login(messageLoginForHousing,taskHousing);*/
		
		TaskHousing taskHousing = housingfundJingZhouService.login(messageLoginForHousing);
		return taskHousing;
		
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST )
	public String crawler(@RequestBody MessageLoginForHousing messageLogin){
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		/*TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		System.out.println("------crawler----------");
		try {
			//流水
			housingfundJingZhouService.getcrawler(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.getuserinfo.auth", e.toString());
			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
			taskHousing.setFinished(true);
			save(taskHousing);
		}
		
		try {
			//个人
			housingfundJingZhouService.getuserinfo(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.getuserinfo.auth", e.toString());
			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
			taskHousing.setFinished(true);
			save(taskHousing);
		}
		housingBasicService.updateTaskHousing(messageLogin.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		result.setData(taskHousing);*/
		
		housingfundJingZhouService.getAllData(messageLogin);
		return "SUCCESS";
	}
}
