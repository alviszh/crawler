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
import app.service.InsuranceZhenJiangService;

/**
 * @description: 镇江社保
 * 经过登录调研，发现镇江社保的网站将图片验证码和用户信息分开校验，在图片验证码验证通过的前提下，再验证用户信息
 * 后经过测试类的测试，返现直接用验证用户信息的链接，即使图片验证码写错，或者是直接不写， 也能登录成功，返回用户
 * 登陆后的部分信息，其中包含完整的身份证号（用户信息的一部分），故决定直接将登录和爬取写在同一个接口中，将登录成功
 * 获取的身份证号作为参数传递。
 * @author: sln 
 */
@RestController
@Configuration
@RequestMapping("/insurance/zhenjiang") 
public class InsuranceZhenJiangController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceZhenJiangController.class);
	@Autowired
	private InsuranceZhenJiangService insuranceZhenJiangService;
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
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceZhenJiangService.login(insuranceRequestParameters);
		return taskInsurance;		
	}
	@PostMapping(value="/getAllData")
	public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		insuranceZhenJiangService.getAllData(insuranceRequestParameters);
	}
}
