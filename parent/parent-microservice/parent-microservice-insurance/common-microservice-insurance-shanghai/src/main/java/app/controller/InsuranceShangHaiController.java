package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;
import app.service.InsuranceShanghaiService;


@RestController
@Configuration
@RequestMapping("/insurance/shanghai") 
public class InsuranceShangHaiController {

	
	@Autowired
	private InsuranceShanghaiService insuranceShanghaiService;
	
	@Autowired
	private TracerLog tracer;
	 
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {

		tracer.qryKeyValue("parser.login.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.auth", insuranceRequestParameters.getUsername());
		insuranceShanghaiService.login(insuranceRequestParameters);
		return null;

	}
	
	/**
	 * 获取信息
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/getData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
		insuranceShanghaiService.getAllData(insuranceRequestParameters);
		return null;
		
	}
	
	
	
}
