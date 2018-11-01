package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.aop.InsuranceLogin;

@RestController
@Configuration
@RequestMapping("/insurance/szheilongjiang")
public class InsuranceHeiLongJiangController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private AgentService agentService;
	@Autowired
	private InsuranceLogin insuranceLogin;
	
	@Value("${spring.application.name}") 
	String appName;
	
	@PostMapping(path = "/loginAgent")
	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance=null;
		try {
			taskInsurance =  agentService.postAgent(insuranceRequestParameters, "/insurance/szheilongjiang/login",300000L); 
		} catch (Exception e) {
			tracer.qryKeyValue("RuntimeException", e.toString());
		}
		return taskInsurance;
	}
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceTianjinController login", insuranceRequestParameters.getTaskId());
		//通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//	    taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//		try {
//			insuranceHeiLongJiangCommonService.login(insuranceRequestParameters);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("login.exception.e", e.getMessage());
//		}
		taskInsurance = insuranceLogin.login(insuranceRequestParameters);
		return taskInsurance;
	}
	
	
	@PostMapping(path = "/crawlerAgent")
	public TaskInsurance crawlerAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance=null;
		try {
			taskInsurance =  agentService.postAgentCombo(insuranceRequestParameters, "/insurance/szheilongjiang/crawler"); 
		} catch (Exception e) {
			tracer.addTag("crawlerAgent.exception.e", e.getMessage());
		}
		return taskInsurance;
	}
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.addTag("InsuranceTianJinController crawler", insuranceRequestParameters.getTaskId());
//		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
//		}else{
//			taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
//			insuranceHeiLongJiangCommonService.getAllData(insuranceRequestParameters);	
//		}
		taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
		return taskInsurance;
		
	}
}
