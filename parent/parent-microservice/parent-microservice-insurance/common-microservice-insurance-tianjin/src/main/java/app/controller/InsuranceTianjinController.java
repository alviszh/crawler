package app.controller;

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
import app.service.AsyncTianjinGetAllDataService;
import app.service.InsuranceService;
import app.service.InsuranceTianjinService;
import app.service.aop.InsuranceLogin;

@RestController
@Configuration
@RequestMapping("/insurance/tianjin") 
public class InsuranceTianjinController {

	@Autowired
	private InsuranceTianjinService insuranceTianjinService;
	@Autowired
	private AsyncTianjinGetAllDataService asyncTianjinGetAllDataService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceLogin insuranceLogin;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	 
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceTianjinController login", insuranceRequestParameters.getTaskId());
		
//		TaskInsurance taskInsurance = insuranceTianjinService.changeStatus(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
//			asyncTianjinGetAllDataService.login(insuranceRequestParameters);
			insuranceLogin.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}
	
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.addTag("InsuranceTianJinController crawler", insuranceRequestParameters.getTaskId());
//		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
//		TaskInsurance taskInsurance=null;
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
//		}else{
//		    taskInsurance = asyncTianjinGetAllDataService.updateTaskInsurance(insuranceRequestParameters);
//			asyncTianjinGetAllDataService.getAllData(insuranceRequestParameters);	
//		}
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceLogin.getAllData(insuranceRequestParameters);
		return taskInsurance;
		
	}
	
//	/**
//	 * 熔断器
//	 * @return
//	 */
//	@GetMapping(value="/hystrix")
//	public String hystrix(){
//		
//		return insuranceTianjinService.hystrix();
//		
//	}
}
