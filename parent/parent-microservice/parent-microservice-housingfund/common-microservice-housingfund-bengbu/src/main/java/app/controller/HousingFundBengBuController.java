package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.HousingFundBengBuCommonService;

@RestController
@Configuration
@RequestMapping("/housing/bengbu")
public class HousingFundBengBuController{

	@Autowired   
	private HousingFundBengBuCommonService housingFundBengBuCommonService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	   
	    //登陆  
		@RequestMapping(value = "/login", method = RequestMethod.POST)
		public TaskHousing housingFundLogin(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
			TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
			tracer.addTag("parser.housingFund.login.start",messageLoginForHousing.getTask_id());
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
//			save(taskHousing);
			taskHousing = housingFundBengBuCommonService.login(messageLoginForHousing);
			return taskHousing;
		}
		
		
		//爬取
		@RequestMapping(value = "/crawler", method = RequestMethod.POST)
		public TaskHousing housingFundCrawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
			TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
			tracer.addTag("parser.housingFund.crawler.start",messageLoginForHousing.getTask_id());
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getDescription());
//			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getError_code());
//			save(taskHousing);
			taskHousing = housingFundBengBuCommonService.getAllData(messageLoginForHousing);
			return taskHousing;
		}
}
