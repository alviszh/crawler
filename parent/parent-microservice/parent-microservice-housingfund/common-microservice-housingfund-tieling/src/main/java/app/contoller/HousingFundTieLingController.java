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
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.controller.HousingBasicController;
import app.service.HousingTieLingFutureService;

@RestController
@Configuration
@RequestMapping("/housing/tieling")
public class HousingFundTieLingController extends HousingBasicController{
	public static final Logger log = LoggerFactory.getLogger(HousingFundTieLingController.class);
	@Autowired
	public HousingTieLingFutureService housingTieLingFutureService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public MessageLoginForHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.cangzhou.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		taskHousing = housingTieLingFutureService.login(messageLoginForHousing);
		
		return new MessageLoginForHousing();
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		
		housingTieLingFutureService.crawler(messageLoginForHousing, taskHousing);
		
		result.setData(taskHousing);
		return result;
		
		
	}
}
