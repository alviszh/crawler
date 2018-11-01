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
import app.service.InsuranceHuzhouCrawlerService;

@RestController
@Configuration
@RequestMapping("/insurance/huzhou") 
public class InsuranceHuzhouController {
public static final Logger log = LoggerFactory.getLogger(InsuranceHuzhouController.class);
	@Autowired
	private InsuranceHuzhouCrawlerService  insuranceHuzhouCrawlerService;
	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;	
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
	    tracer.addTag("InsuranceHuzhouController.login",insuranceRequestParameters.getTaskId());	
	    TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance=insuranceHuzhouCrawlerService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{		
		tracer.addTag("InsuranceHuzhouController.crawler", insuranceRequestParameters.getTaskId());	
		TaskInsurance taskInsurance= insuranceHuzhouCrawlerService.getAllData(insuranceRequestParameters);	  
		return taskInsurance;		
	}
}
