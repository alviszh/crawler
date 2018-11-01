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
import app.service.AsyncZiboGetAllDataService;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance")
public class InsuranceZiboController {
	
	@Autowired
	private AsyncZiboGetAllDataService asyncZiboGetAllDataService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer; 
	
	@PostMapping(value="/zibo")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeLoginStatusDoing(taskInsurance);
		tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
		
		try {
			asyncZiboGetAllDataService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	
	
	@PostMapping(value="/zibo/getData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
		
		TaskInsurance taskInsurance = asyncZiboGetAllDataService.updateTaskInsurance(insuranceRequestParameters);		
		asyncZiboGetAllDataService.getAllData(insuranceRequestParameters);	
	
		return taskInsurance;
	}
	
}
