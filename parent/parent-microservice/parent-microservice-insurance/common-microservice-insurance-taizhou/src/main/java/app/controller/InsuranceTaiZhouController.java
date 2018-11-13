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
import app.service.InsuranceTaiZhouCommonService;

@RestController  
@Configuration
@RequestMapping("/insurance/taizhou")
public class InsuranceTaiZhouController {

	@Autowired
	private InsuranceTaiZhouCommonService insuranceTaiZhouCommonService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceTianjinController login", insuranceRequestParameters.getTaskId());
		//通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceTaiZhouCommonService.login(insuranceRequestParameters);
		return taskInsurance;
	}
	
	
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("InsuranceTianJinController crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceTaiZhouCommonService.getAllData(insuranceRequestParameters);	
		return taskInsurance;
		
	}
}
