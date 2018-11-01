package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.paeser.InsuranceSZHuBeiParser;
import app.service.AgentService;
import app.service.InsuranceService;

@RestController
@RequestMapping("/insurance/szhubei")
public class InsuranceSZHuBeiController {
	public static final Logger log = LoggerFactory.getLogger(InsuranceSZHuBeiController.class);
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZHuBeiParser insuranceSZHuBeiParser;
	@Autowired
	private AgentService agentService;
	
	
	// 登录中间层
	@PostMapping(path = "/loginAgent")
	public TaskInsurance loginAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws  Exception { 
		System.out.println("loginAgent登陆");
		tracer.qryKeyValue("集群的调用...", insuranceRequestParameters.getTaskId());
//		TaskBank taskBank = null;
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try{
			taskInsurance =  agentService.postAgent(insuranceRequestParameters, "/insurance/szhubei/login"); 
			
		}catch(RuntimeException e){
			
            System.out.println("loginAgent.exception="+ e.getMessage());
            return taskInsurance;
		}

//		TaskBank taskBank =  agentService.postAgent(bankJsonBean, "/bank/cibchina/debitcard/login"); 
		return taskInsurance;
	}	
	
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.qryKeyValue("InsuranceSZHuBeiController login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		try {
			taskInsurance = insuranceSZHuBeiParser.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
		}
	
		return taskInsurance;
	}
	
	
	
	
	// 爬取和解析中间层
		@PostMapping(path = "/getAllDataAgent")
		public TaskInsurance getAllDataAgent(@RequestBody InsuranceRequestParameters insuranceRequestParameters)throws  Exception {
			tracer.qryKeyValue("兴业银行（储蓄卡）爬取和解析集群的调用...", insuranceRequestParameters.getTaskId());
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			
			insuranceRequestParameters.setIp(taskInsurance.getCrawlerHost());
			insuranceRequestParameters.setPort(taskInsurance.getCrawlerPort());
			insuranceRequestParameters.setWebdriverHandle(taskInsurance.getWebdriverHandle());
			try{
				taskInsurance =  agentService.postAgentCombo(insuranceRequestParameters, "/insurance/szhubei/crawler");  
				
			}catch(RuntimeException e){
				tracer.addTag("crawlerAgent", "runtimeexception");
				
			}
//			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
			return taskInsurance;
		}	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		tracer.qryKeyValue("InsuranceSZHuBeiController.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceSZHuBeiParser.getAllData(insuranceRequestParameters);
		} catch (Exception e) {
			tracer.addTag("爬取异常", e.toString());
		}
		return taskInsurance;
		
	}
}
