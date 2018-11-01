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
import app.service.aop.InsuranceLogin;

@RestController    
@Configuration     
@RequestMapping("/insurance/bengbu")  
public class InsuranceBengBuController {
        
//	@Autowired
//	private InsuranceBengBuCommonService insuranceBengBuCommonService;    
	@Autowired
	private TracerLog tracer;     
//	@Autowired     
//	private InsuranceService insuranceService;  
	@Autowired  
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceLogin insuranceLogin;
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		  
		tracer.addTag("InsuranceBengBuController login", insuranceRequestParameters.getTaskId());
		//通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//	    taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		try {
//			insuranceBengBuCommonService.login(insuranceRequestParameters,taskInsurance);
//			insuranceBengBuCommonService.login(insuranceRequestParameters);
			 taskInsurance = insuranceLogin.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}
	
	
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.addTag("InsuranceBengBuController crawler", insuranceRequestParameters.getTaskId());
//		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
//		}else{
//			taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//			insuranceBengBuCommonService.getAllData(insuranceRequestParameters,taskInsurance);	
//			taskInsurance = insuranceBengBuCommonService.getAllData(insuranceRequestParameters);
			 taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
//		}
		return taskInsurance;
		
	}
}
