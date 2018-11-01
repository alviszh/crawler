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
import app.service.InsuranceHengShuiService;

/**
 * @description: 衡水社保(之前用selenium和HTMLunit相结合的登录方式，登录和爬取公用一个接口，由于登录的cookie取出来后会失效)
 * 				
 * 				 目前的登录方式去除了selenium，故还用两个接口
 * @author: sln 
 * @date: 2017年12月15日 下午4:25:23 
 */
@RestController
@Configuration
@RequestMapping("/insurance/hengshui") 
public class InsuranceHengShuiController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceHengShuiController.class);
	@Autowired
	private InsuranceHengShuiService insuranceHengShuiService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	 /**
	  * 登录接口
	  * @param insuranceRequestParameters
	  * @return
	  */
	@PostMapping(value="/login")  
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("登录的用户名和密码分别是：",insuranceRequestParameters.getUsername()+"和"+insuranceRequestParameters.getPassword());	
		TaskInsurance taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceHengShuiService.login(insuranceRequestParameters);
		return taskInsurance;		
	}	
	@PostMapping(value="/getAllData")
	public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		insuranceHengShuiService.getAllData(insuranceRequestParameters);
	}
	
}
