package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceJiangMenFutureService;
import app.service.InsuranceService;

@RestController
@RequestMapping("/insurance/jiangmen") 
public class InsuranceJiangMenController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceJiangMenController.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceJiangMenFutureService insuranceJiangMenFutureService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("InsuranceJiangMenController login", insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		try {
			insuranceJiangMenFutureService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
		}

		return taskInsurance;
		
	}
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.qryKeyValue("InsuranceJiangMenController.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceJiangMenFutureService.getAllData(insuranceRequestParameters);
//			insuranceService.changeCrawlerStatusSuccess(taskInsurance);
		} catch (Exception e) {
			tracer.addTag("爬取异常", e.toString());
		}
		return taskInsurance;
		
	}
	
}
