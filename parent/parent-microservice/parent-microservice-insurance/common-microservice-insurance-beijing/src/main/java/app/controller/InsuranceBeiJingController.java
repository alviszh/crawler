 package app.controller;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceBeijingService;
import app.service.aop.InsuranceLogin;
import app.service.aop.InsuranceSms;
    
@RestController
@Configuration      
@RequestMapping("/insurance") 
public class InsuranceBeiJingController {
  
	public static final Logger log = LoggerFactory.getLogger(InsuranceBeiJingController.class);
 
	@Autowired
	private InsuranceBeijingService insuranceBeijingService;
//	@Autowired
//	private AsyncBeijingGetAllDataService asyncBeijingGetAllDataService;
//	@Autowired
//	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceSms insuranceSms;
	@Autowired
	private InsuranceLogin insuranceLogin;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@PostMapping(value="/beijing/sendsms")
	public TaskInsurance sendSms(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceBeiJingController.sendSms", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
//			insuranceBeijingService.validate(insuranceRequestParameters,taskInsurance);
			taskInsurance = insuranceSms.sendSms(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
		
	}
	 
	@PostMapping(value="/beijing/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceBeiJingController login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		TaskInsurance taskInsurance = insuranceBeijingService.changeStatus(insuranceRequestParameters);
		try {
//			insuranceBeijingService.login(insuranceRequestParameters);
			taskInsurance = insuranceLogin.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}
	
	@PostMapping(value="/beijing/getdata")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceBeiJingController crawler", insuranceRequestParameters.getTaskId());
//		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		if(isCrawler){
			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
//		}else{
//			System.out.println("controller");
//			taskInsurance = insuranceBeijingService.updateTaskInsurance(insuranceRequestParameters);		
//			asyncBeijingGetAllDataService.getAllData(insuranceRequestParameters);	
			taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
//		}
		return taskInsurance;
	}
	
	@GetMapping(value="/hystrix")
	public String hystrix(){
		
		return insuranceBeijingService.hystrix();
		
	}
	
	
	
	
}
