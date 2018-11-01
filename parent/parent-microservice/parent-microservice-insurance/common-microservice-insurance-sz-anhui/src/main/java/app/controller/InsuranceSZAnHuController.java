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

import app.commontracerlog.TracerLog;
import app.service.InsuranceSZAnHuService;

@RestController
@Configuration
@RequestMapping("/insurance/szanhui") 
public class InsuranceSZAnHuController {
public static final Logger log = LoggerFactory.getLogger(InsuranceSZAnHuController.class);
	
	@Autowired
	private InsuranceSZAnHuService insuranceSZAnHuService;
	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceSZAnHuController.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceSZAnHuService.login(insuranceRequestParameters);
		return taskInsurance;
	}	
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
		tracer.addTag("InsuranceSZAnHuController.crawler", insuranceRequestParameters.getTaskId());	
		TaskInsurance taskInsurance = insuranceSZAnHuService.getAllData(insuranceRequestParameters);
		return taskInsurance;
	}
}
