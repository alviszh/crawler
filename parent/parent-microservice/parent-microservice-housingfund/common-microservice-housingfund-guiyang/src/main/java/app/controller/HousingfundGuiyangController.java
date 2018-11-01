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
import app.service.HousingfundGuiyangService;

@RestController
@Configuration
@RequestMapping("/housing/guiyang") 
public class HousingfundGuiyangController extends HousingBasicController{
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundGuiyangService housingfundGuiyangService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		tracer.addTag("parser.guiyang.login", messageLoginForHousing.getTask_id());
		
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
//		Gson gson = new Gson();
//		
//		taskHousing.setPassword(messageLoginForHousing.getPassword());
//		taskHousing.setLogintype(StatusCodeLogin.IDNUM);
//		taskHousing.setLoginMessageJson(gson.toJson(messageLoginForHousing));
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		save(taskHousing);
//		
		housingfundGuiyangService.login(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}
	
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		tracer.addTag("parser.guiyang.crawler", messageLoginForHousing.getTask_id());
		
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		
		housingfundGuiyangService.getAllData(messageLoginForHousing);
		
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
//		save(taskHousing);
		
		//用户信息
//		try {
//			housingfundGuiyangService.getUserInfo(messageLoginForHousing,taskHousing);
//		} catch (Exception e) {
//			tracer.addTag("parser.guiyang.crawler.getuserinfo.error", e.getMessage());
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getDescription());
//			save(taskHousing);
//			housingfundGuiyangService.updateTaskHousing(taskHousing.getTaskid());
//		}
//		
//		//缴费信息
//		try{
//			housingfundGuiyangService.getPay(messageLoginForHousing,taskHousing);			
//		}catch(Exception e){
//			tracer.addTag("parser.guiyang.crawler.getpay.error", e.getMessage());
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getDescription());
//			save(taskHousing);
//			housingfundGuiyangService.updateTaskHousing(taskHousing.getTaskid());
//		}
		
		
		result.setData(taskHousing);
		return result;	
		
	}

}
