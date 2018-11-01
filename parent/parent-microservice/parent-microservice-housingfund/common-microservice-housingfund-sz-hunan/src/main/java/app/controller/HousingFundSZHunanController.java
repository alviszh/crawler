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

import app.service.HousingSZHunanFutureService;

@RestController
@Configuration
@RequestMapping("/housing/szhunan")
public class HousingFundSZHunanController extends HousingBasicController {

	
	@Autowired
	private HousingSZHunanFutureService housingSZHunanFutureService;
	
	
	public ResultData<TaskHousing> login(MessageLoginForHousing messageLoginForHousing) {
		
		tracer.qryKeyValue("parser.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.qryKeyValue("parser.crawler.auth",messageLoginForHousing.getNum());
		
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();

		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		save(taskHousing);
		
		try {
			housingSZHunanFutureService.login(messageLoginForHousing, taskHousing,1);
		} catch (Exception e) {
			tracer.addTag("HousingFundSZHunanController.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
		}
		
		result.setData(taskHousing);
		return result;
		
		
	}
	
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		
		ResultData<TaskHousing> result = login(messageLoginForHousing);

		return result;
		
		
	}

}
