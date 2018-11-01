package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.AsyncHangzhouGetAllDataService;
import app.service.InsuranceHangzhouService;
import app.service.InsuranceService;

@RestController
@RequestMapping("/insurance/hangzhou") 
public class InsuranceHangzhouController {
public static final Logger log = LoggerFactory.getLogger(InsuranceHangzhouController.class);
@Autowired
private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHangzhouService insuranceHangzhouService;
	@Autowired
	private AsyncHangzhouGetAllDataService asyncHangzhouGetAllDataService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("InsuranceHangzhouontroller login", insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			insuranceHangzhouService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
		}

		return taskInsurance;
		
	}
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.qryKeyValue("InsuranceHangzhouontroller.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceHangzhouService.getAllData(insuranceRequestParameters);
//			insuranceService.changeCrawlerStatusSuccess(taskInsurance);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("爬取异常", e.toString());
//			insuranceService.changeLoginStatusException(taskInsurance);
		}
		return taskInsurance;
		
	}
	
//	@PostMapping(value="/getdata")
//	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
//
//		tracer.qryKeyValue("InsuranceHangzhouController crawler", insuranceRequestParameters.getTaskId());
//		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
//		TaskInsurance taskInsurance = null;
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
//		}else{
//			taskInsurance = insuranceHangzhouService.updateTaskInsurance(insuranceRequestParameters);		
//			asyncHangzhouGetAllDataService.getAllData(insuranceRequestParameters);	
//		}
//       
//		
//			
//		
//        
//		return taskInsurance;
//		
//	}
	
	@GetMapping(value="/hystrix")
	public String hystrix(){
		
		return insuranceHangzhouService.hystrix();
		
	}

}
