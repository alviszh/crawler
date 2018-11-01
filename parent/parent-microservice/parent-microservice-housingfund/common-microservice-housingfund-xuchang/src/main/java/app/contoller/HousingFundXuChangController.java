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
import app.service.HousingXuChangFutureService;

@RestController
@Configuration
@RequestMapping("/housing/xuchang")
public class HousingFundXuChangController  extends HousingBasicController{
	public static final Logger log = LoggerFactory.getLogger(HousingFundXuChangController.class);
	
	@Autowired
	public HousingXuChangFutureService housingXuChangFutureService;
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		
		housingXuChangFutureService.crawler(messageLoginForHousing, taskHousing);
		
		result.setData(taskHousing);
		return result;
		
		
	}
}
