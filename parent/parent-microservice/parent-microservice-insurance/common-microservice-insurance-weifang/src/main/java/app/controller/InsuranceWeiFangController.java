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
import app.service.InsuranceWeiFangCrawleService;

@RestController
@Configuration
@RequestMapping("/insurance/weifang") 
public class InsuranceWeiFangController {
public static final Logger log = LoggerFactory.getLogger(InsuranceWeiFangController.class);
	

	@Autowired
	private InsuranceWeiFangCrawleService insuranceWeiFangCrawleService;
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
	    tracer.addTag("InsuranceWeiFangController.login",insuranceRequestParameters.getTaskId());		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance=insuranceWeiFangCrawleService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters)  {
		tracer.addTag("InsuranceWeiFangController.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance=insuranceWeiFangCrawleService.getAllData(insuranceRequestParameters);
		return taskInsurance;
	}
}
