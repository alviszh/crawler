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
import app.service.InsuranceShiJiaZhuangService;
/**
 * 
 * @Description: 石家庄主页面显示四险，但实际上只有三险可以查到具体的缴费信息，其中，养老保险是以年度单位进行查询，且不包含当年
 * @author sln
 * @date 2017年8月5日
 */
@RestController
@Configuration
@RequestMapping("/insurance/shijiazhuang") 
public class InsuranceShiJiaZhuangController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceShiJiaZhuangController.class);
	@Autowired
	private InsuranceShiJiaZhuangService insuranceShiJiaZhuangService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	 /**
	  * 登录接口
	  * @param insuranceRequestParameters
	  * @return
	  */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("登录的用户名和密码分别是：",insuranceRequestParameters.getUsername()+"和"+insuranceRequestParameters.getPassword());
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance=insuranceShiJiaZhuangService.login(insuranceRequestParameters);
		return taskInsurance;		
	}	
	@PostMapping(value="/getAllData")
	public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		insuranceShiJiaZhuangService.getAllData(insuranceRequestParameters);
	}
}
