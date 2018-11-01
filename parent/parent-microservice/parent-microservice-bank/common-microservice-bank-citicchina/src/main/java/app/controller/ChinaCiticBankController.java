package app.controller;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.parser.ChinaCiticBankParser;
import app.service.AgentService;
import app.service.ChinaCiticBankCommonService;
       
   
@RestController 
@Configuration
@RequestMapping("/bank/citicchina/debitcard")
public class ChinaCiticBankController {  
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired   
	private app.commontracerlog.TracerLog tracer;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private ChinaCiticBankCommonService chinaCiticBankCommonService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private ChinaCiticBankParser chinaCiticBankParser;
	
	  
	@Value("${spring.application.name}") 
	String appName;
     
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean)throws Exception {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
		  taskBank = agentService.postAgent(bankJsonBean, "/bank/citicchina/debitcard/login",300000L);
		} catch (Exception e) {
			tracerLog.qryKeyValue("RuntimeException", e.toString());
		}
		return taskBank;
	}

	// 登陆
	@PostMapping(path = "/login")
	public TaskBank CiticChinaLogin(@RequestBody BankJsonBean bankJsonBean) {
		tracer.addTag("action.crawler.bank.login", bankJsonBean.getTaskid());
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_DOING.getPhase(),
//				BankStatusCode.BANK_LOGIN_DOING.getPhasestatus(), BankStatusCode.BANK_LOGIN_DOING.getDescription(),
//				BankStatusCode.BANK_LOGIN_DOING.getError_code(), false, bankJsonBean.getTaskid());
		//准备登陆 
//		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		Gson gson = new Gson();
		taskBank.setTesthtml(gson.toJson(bankJsonBean));
		taskBank.setBankType(bankJsonBean.getBankType());
		System.out.println("cardTyep ==============================>"+bankJsonBean.getCardType());
		taskBank.setCardType(bankJsonBean.getCardType());
		taskBank.setLoginType(bankJsonBean.getLoginType());
		System.out.println("loginName ==============================>"+bankJsonBean.getLoginName());
		taskBank.setLoginName(bankJsonBean.getLoginName());
		taskBank.setCrawlerHost(bankJsonBean.getIp());
		taskBank.setCrawlerPort(bankJsonBean.getPort());
		taskBank = taskBankRepository.save(taskBank);
		try {
			chinaCiticBankCommonService.login(bankJsonBean);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.bank.login.exception", e.getMessage());
		}
		return taskBank;

	}

	
	@PostMapping(path = "/crawlerAgent")
	public TaskBank crawlerAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//		String uri = "http://" + taskBank.getCrawlerHost() + ":" + taskBank.getCrawlerPort() + "/bank/citicchina/debitcard/crawler";
//		System.out.println("uri->" + uri);
//		ResponseEntity<TaskBank> str = this.restTemplate.postForEntity(uri, bankJsonBean, TaskBank.class);
//		taskBank = str.getBody();
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		taskBank = agentService.postAgentCombo(bankJsonBean, "/bank/citicchina/debitcard/crawler");
		return taskBank;
	}
	
	// 爬取
	@PostMapping(path = "/crawler")
	public TaskBank debitcardCrawlerChina(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//		boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
		 taskBank = null;
//		if (isDoing) {
//			tracer.addTag("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
//		} else {
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
//					BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
//					BankStatusCode.BANK_CRAWLER_DOING.getDescription(), null, false, bankJsonBean.getTaskid());

		 taskBank = chinaCiticBankCommonService.getAllData(bankJsonBean);
//		}
		return taskBank;

	}
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = chinaCiticBankParser.quit(bankJsonBean);
		return taskBank;
	}
	
}
