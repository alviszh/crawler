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
import app.service.InsuranceShangQiuService;

/**
 * @description: 商丘社保(调研的网站只有养老保险的些许保险信息)
 * @author: sln 
 */
@RestController
@Configuration
@RequestMapping("/insurance/shangqiu") 
public class InsuranceShangQiuController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceShangQiuController.class);
	@Autowired
	private InsuranceShangQiuService insuranceShangQiuService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@PostMapping(value="/login")  
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("登录的用户名和密码分别是：",insuranceRequestParameters.getUsername()+"和"+insuranceRequestParameters.getPassword());
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceShangQiuService.login(insuranceRequestParameters);
		return taskInsurance;		
	}	
	@PostMapping(value="/getAllData")
	public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		insuranceShangQiuService.getAllData(insuranceRequestParameters);
	}
}
