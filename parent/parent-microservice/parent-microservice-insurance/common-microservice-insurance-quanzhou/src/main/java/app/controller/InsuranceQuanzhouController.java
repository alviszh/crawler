package app.controller;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 泉州社保
 */
@RestController
@Configuration
@RequestMapping("/insurance/quanzhou")
public class InsuranceQuanzhouController {



	@Autowired
	private TracerLog tracer;


	@Autowired
	private InsuranceLogin insuranceLogin;
	
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){

		tracer.addTag("InsuranceQuanzhouController.login",insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());

		TaskInsurance taskInsurance = insuranceLogin.login(insuranceRequestParameters);
		return taskInsurance;

	}


	@PostMapping(value="/getAllData")
	public TaskInsurance getAllData(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("InsuranceChengduController.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
		return taskInsurance;

	}

}
