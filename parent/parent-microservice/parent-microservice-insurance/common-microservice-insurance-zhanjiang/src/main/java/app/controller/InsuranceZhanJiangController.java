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
import app.service.CommonService;
import app.service.InsuranceService;
import app.service.InsuranceZhanJiangService;

@RestController
@Configuration
@RequestMapping("/insurance/zhanjiang")
public class InsuranceZhanJiangController {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceZhanJiangService insuranceZhanJiangService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private TracerLog tracer; 
	
	@PostMapping(path = "/loginAgent")
	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws IllegalAccessException, Exception {
		tracer.addTag("crawler.insurance.login", insuranceRequestParameters.getTaskId());   
		tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = new TaskInsurance();
		try {
			taskInsurance =  agentService.postAgent(insuranceRequestParameters, "/insurance/zhanjiang/login"); 
		} catch (RuntimeException e) {
			tracer.addTag("InsuranceZhanJiangController.loginAgent.exception", e.getMessage());
		}
		return taskInsurance;
	}
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("crawler.controller.login.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeLoginStatusDoing(taskInsurance);
		tracer.addTag("crawler.controller.login.taskInsurance", taskInsurance.toString());
		try {
			taskInsurance = commonService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	
	@PostMapping(path = "/quit")
	public TaskInsurance quit(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance = insuranceZhanJiangService.quitDriver(insuranceRequestParameters);
		return taskInsurance;
	}
}
