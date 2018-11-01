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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingFundDaLiBaiZuService;


@RestController  
@Configuration
@RequestMapping("/housing/dalibaizu")
public class HousingFundDaLiBaiZuController extends HousingBasicController{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundDaLiBaiZuService housingFundDaLiBaiZuService;
	
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> housingFundCrawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.housingFund.crawler.start",messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getDescription());
		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getError_code());
		save(taskHousing);
		housingFundDaLiBaiZuService.crawler(messageLoginForHousing, taskHousing);
		result.setData(taskHousing);
		return result;
	}
	
}
