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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingZhuHaiService;
//import app.service.HousingHaiNanFutureService;

@RestController
@Configuration
@RequestMapping("/housing/zhuhai") 
public class HousingFundZhuHaiController extends HousingBasicController {

	@Autowired
	private HousingZhuHaiService housingZhuHaiService;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		if(null != taskHousing){
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getError_code());
			save(taskHousing);
			
			taskHousing = housingZhuHaiService.login(messageLoginForHousing);
		}
		result.setData(taskHousing);
		return result;
	}
	
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		if(null != taskHousing){
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getError_code());
			save(taskHousing);
			
			taskHousing = housingZhuHaiService.getAllData(messageLoginForHousing);
		}
		result.setData(taskHousing);
		return result;
	}

}
