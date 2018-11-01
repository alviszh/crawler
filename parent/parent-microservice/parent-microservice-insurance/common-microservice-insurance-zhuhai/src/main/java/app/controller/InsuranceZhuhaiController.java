package app.controller;

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
import app.service.InsuranceService;
import app.service.InsuranceZhuhaiHandle;
import app.service.InsuranceZhuhaiService;

@RestController
@Configuration
@RequestMapping("/insurance/zhuhai") 
public class InsuranceZhuhaiController {
	
//	@Autowired
//	private TaskInsuranceRepository taskInsuranceRepository;
//	@Autowired
//	private InsuranceService insuranceService;
//	@Autowired
//	private InsuranceZhuhaiService insuranceZhuhaiService;
	@Autowired
	private InsuranceZhuhaiHandle insuranceZhuhaiHandle;
	@Autowired
	private TracerLog tracer; 
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("parser.login",insuranceRequestParameters.getTaskId());
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//		insuranceZhuhaiService.login(insuranceRequestParameters,taskInsurance);	
		return insuranceZhuhaiHandle.processor(insuranceRequestParameters);
		
	}

}
