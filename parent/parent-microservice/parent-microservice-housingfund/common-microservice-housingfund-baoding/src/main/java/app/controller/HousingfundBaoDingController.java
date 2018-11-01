package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeLogin;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingfundBaoDingService;

@RestController
@Configuration
@RequestMapping("/housing/baoding") 
public class HousingfundBaoDingController extends HousingBasicController{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundBaoDingService housingfundBaoDingService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.cangzhou.login", messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		Gson gson = new Gson();
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		taskHousing.setLogintype(StatusCodeLogin.IDNUM);
		taskHousing.setLoginMessageJson(gson.toJson(messageLoginForHousing));
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		save(taskHousing);
		housingfundBaoDingService.login(messageLoginForHousing,taskHousing);
		return null;
		
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST )
	public String crawler(@RequestBody MessageLoginForHousing messageLogin){
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		System.out.println("------crawler----------");
		try {
			housingfundBaoDingService.getcrawler(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.getuserinfo.auth", e.toString());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getDescription());
			taskHousing.setFinished(true);
			save(taskHousing);
		}
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		result.setData(taskHousing);
		return null;
	}
}
