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
import app.service.InsuranceNanjingService;


/**
 * @description: 南京社保(登录错误的信息是弹出框,可用getAlert方法抓取到，同样的，也可以在页面上体现，二者均可)
 * @author: sln 
 * @date: 2017年9月26日 下午6:15:59 
 */
@RestController
@Configuration
@RequestMapping("/insurance/nanjing") 
public class InsuranceNanjingController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceNanjingController.class);
	@Autowired
	private InsuranceNanjingService insuranceNanjingService;
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
		taskInsurance = insuranceNanjingService.login(insuranceRequestParameters);
		return taskInsurance;		
	}
	@PostMapping(value="/getAllData")
	public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		insuranceNanjingService.getAllData(insuranceRequestParameters);
	}
}
