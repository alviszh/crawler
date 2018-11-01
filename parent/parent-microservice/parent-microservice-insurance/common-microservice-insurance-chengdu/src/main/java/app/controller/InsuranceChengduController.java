package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.AsyncChengduGetAllDataService;
import app.service.InsuranceChengduService;


@RestController
@Configuration
@RequestMapping("/insurance/chengdu") 
public class InsuranceChengduController {

	@Autowired
	private InsuranceChengduService insuranceChengduService;
	@Autowired
	private AsyncChengduGetAllDataService asyncChengduGetAllDataService;
	
	@Autowired
	private TracerLog tracer;
	 
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("parser.login.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.auth", insuranceRequestParameters.getUsername());

		insuranceChengduService.login(insuranceRequestParameters);

		return null;
		
	}
	
	/**
	 * 获取数据
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/getAllData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
		
		asyncChengduGetAllDataService.getAllData(insuranceRequestParameters);
		
		return null;
		
	}
	
	@GetMapping(value="/hystrix")
	public String hystrix(){
		
		return insuranceChengduService.hystrix();
		
	}
	
	
}
