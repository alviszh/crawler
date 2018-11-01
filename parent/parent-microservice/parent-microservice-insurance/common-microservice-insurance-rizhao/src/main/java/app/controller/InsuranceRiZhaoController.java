package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import app.service.InsuranceRiZhaoService;

/**
 * @description: 日照社保
 * @author: sln 
 */
@RestController
@Configuration
@RequestMapping("/insurance/rizhao") 
public class InsuranceRiZhaoController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceRiZhaoController.class);
	@Autowired
	private InsuranceRiZhaoService insuranceRiZhaoService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@PostMapping(value="/login")  
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("登录的用户名和密码分别是：",insuranceRequestParameters.getUsername()+"和"+insuranceRequestParameters.getPassword());
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceRiZhaoService.login(insuranceRequestParameters);
		return taskInsurance;		
	}	
	@PostMapping(value="/getAllData")
	public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		insuranceRiZhaoService.getAllData(insuranceRequestParameters);
	}
}
