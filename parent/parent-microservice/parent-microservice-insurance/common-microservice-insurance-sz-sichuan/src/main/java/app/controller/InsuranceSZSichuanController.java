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
import app.service.AsyncSZSichuanInsuranceService;
import app.service.InsuranceSZSichuanHandle;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance")
public class InsuranceSZSichuanController {
	
//	@Autowired
//	private TaskInsuranceRepository taskInsuranceRepository;
//	@Autowired
//	private InsuranceService insuranceService;
//	@Autowired
//	private AsyncSZSichuanInsuranceService asyncSZSichuanInsuranceService;
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceSZSichuanHandle handle;
	
	@PostMapping(value="/szsichuan")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("parser.login",insuranceRequestParameters.getTaskId());
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//		tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
		
//		asyncSZSichuanInsuranceService.crawler(insuranceRequestParameters,taskInsurance);
		return handle.processor(insuranceRequestParameters);
		
	}

}
