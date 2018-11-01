package app.contoller;

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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.controller.HousingBasicController;
import app.service.HousingHangZhouFutureService;

@RestController
@Configuration
@RequestMapping("/housing/hangzhou")
public class HousingFundHangZhouController extends HousingBasicController{
	public static final Logger log = LoggerFactory.getLogger(HousingFundHangZhouController.class);
	@Autowired
	private HousingHangZhouFutureService housingHangZhouFutureService;

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();

		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getError_code());
		save(taskHousing);
		
		housingHangZhouFutureService.crawler(messageLoginForHousing, taskHousing);
		
		result.setData(taskHousing);
		return result;
		
		
	}

}
