package app.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.controller.HousingBasicController;
import app.service.HousingEnShiFutureService;;

@RestController
@Configuration
@RequestMapping("/housing/enshi")
public class HousingFundEnShiController extends HousingBasicController{
	public static final Logger log = LoggerFactory.getLogger(HousingFundEnShiController.class);
	
	@Autowired
	public HousingEnShiFutureService housingEnShiFutureService;
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		
		taskHousing = housingEnShiFutureService.login(messageLoginForHousing);
		
		result.setData(taskHousing);
		return result;
		
		
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		
		taskHousing = housingEnShiFutureService.getAllData(messageLoginForHousing);
		
		result.setData(taskHousing);
		return result;
		
		
	}
}
