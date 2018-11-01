package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.InsuranceDaLianService;
import app.service.InsuranceService;


@RestController
@RequestMapping("/insurance/dalian") 
public class InsuranceDaLianController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceDaLianController.class);
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceDaLianService insuranceDaLianService;
	@Autowired
	private AgentService agentService;
//	@PostMapping(path = "/loginAgent")
//	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws  Exception { 
//		System.out.println("loginAgent登陆");
//		tracer.addTag("crawler.bank.login", insuranceRequestParameters.getTaskId());   
//		TaskInsurance taskBank = null;
//		try{
//			taskBank =  agentService.postAgent(insuranceRequestParameters, "/insurance/dalian/login",3 * 60 * 1000L); 
//			
//		}catch(RuntimeException e){
//			tracer.addTag("loginAgent", "runtimeexception");
//			taskBank.setPhase(InsuranceStatusCode.SYSTEM_QUIT.getPhase());
//			taskBank.setPhase_status(InsuranceStatusCode.SYSTEM_QUIT.getPhasestatus());
//			taskBank.setDescription("系统繁忙，请稍后再试");
//		}
//		return taskBank;
//	}
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracerLog.qryKeyValue("InsuranceHangzhouontroller login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		try {
			taskInsurance = insuranceDaLianService.login(insuranceRequestParameters);
		} catch (Exception e) {
			tracerLog.addTag("登陆异常", e.toString());
		}
		//taskInsurance.setFinished(true);
		return taskInsurance;
	}
	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracerLog.qryKeyValue("InsuranceWeiFangController.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceDaLianService.getAllData(insuranceRequestParameters);
		} catch (Exception e) {
			tracerLog.addTag("爬取异常", e.toString());
		}
		return taskInsurance;
		
	}
	
//	@PostMapping(path = "/quit")
//	public TaskBank quit(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
//		TaskBank taskBank = insuranceDaLianService.quit(bankJsonBean);
//		return taskBank;
//	}
}
