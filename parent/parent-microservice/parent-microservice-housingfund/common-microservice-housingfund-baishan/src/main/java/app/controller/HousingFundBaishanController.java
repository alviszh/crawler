package app.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingBaishanFutureService;

@RestController
@Configuration
@RequestMapping("/housing/baishan") 
public class HousingFundBaishanController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundBaishanController.class);
	@Autowired
	private HousingBaishanFutureService housingBaishanFutureService;
	@Autowired
	private TracerLog tracer;
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.crawler.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.crawler.auth",messageLoginForHousing.getNum());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing=housingBaishanFutureService.login(messageLoginForHousing);
		if(taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus())!=-1){
			taskHousing=housingBaishanFutureService.getAllData(messageLoginForHousing);
		}	
		result.setData(taskHousing);
		return result;
	}

}
