package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.HousingFundMuDanJiangCommonService;

@RestController
@Configuration  
@RequestMapping("/housing/mudanjiang")
public class HousingFundMuDanJiangController extends HousingBasicController{

	@Autowired
	private HousingFundMuDanJiangCommonService housingFundMuDanJiangCommonService;
	@Autowired
	private TracerLog tracer;
	
	//登陆
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> housingFundLogin(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.housingFund.login.start",messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();

//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
//		save(taskHousing);
		
		housingFundMuDanJiangCommonService.login(messageLoginForHousing);
		
		result.setData(taskHousing);
		return result;
	}
	
	//爬取
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> housingFundCrawler(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.housingFund.crawler.start",messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
//
//		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
//		taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhasestatus());
//		taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getDescription());
//		taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getError_code());
//		save(taskHousing);
		
		housingFundMuDanJiangCommonService.getAllData(messageLoginForHousing);
		
		result.setData(taskHousing);
		return result;
	}
}
