package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CmbcChinaCreditCrawlerAllService;
import app.service.CmbcChinaLoginService;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月14日 下午2:46:35 
 */
@RestController
@Configuration
@RequestMapping("/bank/cmbcchina/creditcard") 
public class CmbcChinaCreditController extends AgentService{
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private CmbcChinaLoginService loginService;
	@Autowired
	private CmbcChinaCreditCrawlerAllService creditCrawlerAllService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private AgentService agentService;
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		tracer.addTag("crawler.bank.login", bankJsonBean.getTaskid());   
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			agentService.postAgent(bankJsonBean, "/bank/cmbcchina/creditcard/login",300000L); 
		} catch (Exception e) {
			tracer.addTag("CmbcChinaCreditController.creditcard.loginAgent.exception", e.getMessage()); 
		}
		return taskBank;
	}
	
	
	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){
		tracer.addTag("action.crawler.bank.login", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try{
			loginService.login(bankJsonBean);			
		}catch(Exception e){
			tracer.addTag("action.crawler.bank.login.exception", e.getMessage());
		}
		return taskBank;
	}
	
	
	@PostMapping(path = "/crawlerAgent")
	public TaskBank crawlerAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		taskBank = agentService.postAgentCombo(bankJsonBean, "/bank/cmbcchina/creditcard/crawler");
		return taskBank;
	}
	

	@PostMapping(path = "/crawler")
	public TaskBank debitcardCrawlerChina(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank = creditCrawlerAllService.getAllData(bankJsonBean);
		return taskBank;
	}
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = loginService.quit(bankJsonBean);
		return taskBank;
	}
}
