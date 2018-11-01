package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.InsuranceHuiZhouCommonService;
import app.service.InsuranceHuiZhouService;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance/huizhou")
public class InsuranceHuiZhouController {
	@Autowired
	private InsuranceHuiZhouCommonService insuranceHuiZhouCommonService;
	@Autowired
	private InsuranceHuiZhouService insuranceHuiZhouService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@PostMapping(path = "/loginAgent")
	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("crawler.insurance.login", insuranceRequestParameters.getTaskId());   
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance =  agentService.postAgent(insuranceRequestParameters, "/insurance/huizhou/crawler"); 
		} catch (RuntimeException e) {
			tracer.qryKeyValue("RuntimeException", e.toString());
		}
		return taskInsurance;
	}
	
	
	@PostMapping(value="/login")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("login.controller.login.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceHuiZhouCommonService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return taskInsurance;
	}
	
	@PostMapping(path = "/quit")
	public TaskInsurance quit(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance = insuranceHuiZhouService.quitDriver(insuranceRequestParameters);
		return taskInsurance;
	}
}
