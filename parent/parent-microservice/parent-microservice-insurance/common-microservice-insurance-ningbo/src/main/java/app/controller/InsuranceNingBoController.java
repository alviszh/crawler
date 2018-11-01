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
import app.service.aop.InsuranceLogin;

@RestController
@Configuration
@RequestMapping("/insurance/ningbo")
public class InsuranceNingBoController {

	@Autowired
	private TracerLog tracer;
//	@Autowired
//	private InsuranceNingBoService insuranceNingBoService;
//	@Autowired
//	private AsyncNingboGetAllDataService asyncNingboGetAllDataService;
	@Autowired
	private InsuranceLogin insuranceLogin;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
//	@PostMapping(value = "/ningbo")
//	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception {
//
//		tracer.addTag("parser.login", "taskid");
//		TaskInsurance taskInsurance = insuranceNingBoService.changeStatus(insuranceRequestParameters);
//		insuranceNingBoService.login(insuranceRequestParameters);
//		return taskInsurance;
//
//	}

	/*@PostMapping(value = "/ningbo/getData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {

		tracer.addTag("InsuranceNingBoController", "crawler");
		TaskInsurance taskInsurance = insuranceNingBoService.updateTaskInsurance(insuranceRequestParameters);

		try {
			// insuranceNingBoService.getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskInsurance;

	}*/

	@PostMapping(value = "/login")
	public TaskInsurance loginlogin(@RequestBody InsuranceRequestParameters insuranceRequestParameters)
	{
//		TaskInsurance taskInsurance = insuranceNingBoService.changeStatus(insuranceRequestParameters);
//		insuranceNingBoService.loginlogin(insuranceRequestParameters);
		tracer.addTag("Insurance ningbo login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceLogin.login(insuranceRequestParameters);
		return taskInsurance;
	}
	
	@PostMapping(value = "/crawler")
	public TaskInsurance loginCrawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters)
	{
//		TaskInsurance taskInsurance = insuranceNingBoService.changeStatus(insuranceRequestParameters);
//		asyncNingboGetAllDataService.loginCrawler(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceLogin.getAllData(insuranceRequestParameters);
		return taskInsurance;
	}
	
}
