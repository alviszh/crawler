package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceChongqingCrawlerService;

@RestController
@Configuration
@RequestMapping("/insurance/chongqing") 
public class InsuranceChongqingController {
public static final Logger log = LoggerFactory.getLogger(InsuranceChongqingController.class);
	
	@Autowired
	private InsuranceChongqingCrawlerService insuranceChongqingCrawlerService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
	    tracer.addTag("InsuranceChongqingController.login",insuranceRequestParameters.getTaskId());		
	    TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());;
		try {
			taskInsurance=insuranceChongqingCrawlerService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {	
		tracer.addTag("InsuranceChongqingController.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());;
		taskInsurance = insuranceChongqingCrawlerService.getAllData(insuranceRequestParameters);
		return taskInsurance;
	}
}
