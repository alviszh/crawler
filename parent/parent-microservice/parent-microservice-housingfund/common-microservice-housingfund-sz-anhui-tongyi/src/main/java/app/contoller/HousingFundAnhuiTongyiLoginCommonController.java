package app.contoller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.controller.HousingBasicController;
import app.service.common.HousingAnhuiTongyiService;

@RestController
@Configuration
@RequestMapping("/housing/sz/anhui/")
public class HousingFundAnhuiTongyiLoginCommonController extends HousingBasicController {

	@Autowired
	public HousingAnhuiTongyiService housingAnhuiTongyiService;

	@Autowired
	public TracerLog tracerLog;

	// 登录
	@RequestMapping(value = "tongyi/login", method = RequestMethod.POST)
	public TaskHousing login(@RequestBody MessageLoginForHousing messageLoginForHousing) {

		tracerLog.output("taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		
		housingAnhuiTongyiService.login(messageLoginForHousing);

		return taskHousing;
		
	}

}
