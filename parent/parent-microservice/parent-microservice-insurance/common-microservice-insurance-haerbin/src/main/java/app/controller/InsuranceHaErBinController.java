package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceHaerbinCommonService;


@RestController
@RequestMapping("/insurance/haerbin")
public class InsuranceHaErBinController {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHaerbinCommonService insuranceHaerbinCommonService;
	@Autowired
	private TracerLog tracer;

	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("InsuranceHaerbinController login", insuranceRequestParameters.getTaskId());
		insuranceHaerbinCommonService.login(insuranceRequestParameters);
		return taskInsurance;
	}

}
