package app.controller;

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
import app.service.InsuranceService;
import app.service.InsuranceZhangZhouCommonService;
import app.service.aop.InsuranceLogin;

@RestController  
@Configuration
@RequestMapping("/insurance/zhangzhou")
public class InsuranceZhangZhouController {

	@Autowired
	private InsuranceZhangZhouCommonService insuranceZhangZhouCommonService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceLogin insuranceLogin;
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceZhangZhouController login", insuranceRequestParameters.getTaskId());
		//通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//	    taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//		try {
//			insuranceZhangZhouCommonService.login(insuranceRequestParameters,taskInsurance);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		taskInsurance = insuranceLogin.login(insuranceRequestParameters);
		return taskInsurance;
	}
	
	
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.addTag("InsuranceZhangZhouController crawler", insuranceRequestParameters.getTaskId());
//		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
//		}else{
//			taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//			insuranceZhangZhouCommonService.getAllData(insuranceRequestParameters,taskInsurance);	
//		}
		taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
		return taskInsurance;
		
	}
}
