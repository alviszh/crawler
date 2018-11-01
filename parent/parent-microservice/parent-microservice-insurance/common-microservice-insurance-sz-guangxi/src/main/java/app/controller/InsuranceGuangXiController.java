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
import app.service.InsuranceGuangXiService;

@RestController
@Configuration
@RequestMapping("/insurance/guangxi")
public class InsuranceGuangXiController {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuangXiService insuranceGuangXiService;
	@Autowired
	private TracerLog tracer; 
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("crawler.controller.login.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceGuangXiService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return taskInsurance;
	}
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("crawler.controller.crawler.taskid",insuranceRequestParameters.getTaskId());	
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance=insuranceGuangXiService.getAllData(insuranceRequestParameters);	
		return taskInsurance;
		
	}
}
