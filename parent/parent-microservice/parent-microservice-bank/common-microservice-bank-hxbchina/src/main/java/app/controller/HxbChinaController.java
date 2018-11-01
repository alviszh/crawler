package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import app.service.HxbChinaCrawlerAllService;
import app.service.HxbChinaLoginService;

@RestController
@Configuration
@RequestMapping("/bank/hxbchina/debitcard")
public class HxbChinaController extends AgentService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HxbChinaCrawlerAllService crawlerAllService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private HxbChinaLoginService loginService;
	@Value("${spring.application.name}")
	String appName;
	@Autowired
	private AgentService agentService;

	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracer.addTag("crawler.bank.login", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			agentService.postAgent(bankJsonBean, "/bank/hxbchina/debitcard/login", 300000L);
		} catch (Exception e) {
			tracer.addTag("HxbChinaController.debitcard.loginAgent.exception", e.getMessage());
		}
		return taskBank;
	}

	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean) {
		// changeStatusLoginDoing方法中就会存储登录ip和端口号
		tracer.addTag("action.crawler.bank.login", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			// 用户名登录或者是证件号登录(service中有判断)
			loginService.login(bankJsonBean);
		} catch (Exception e) {
			tracer.addTag("action.crawler.bank.login.exception", e.getMessage());
		}
		return taskBank;
	}

	@PostMapping(path = "/crawlerAgent")
	public TaskBank crawlerAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		taskBank = agentService.postAgentCombo(bankJsonBean, "/bank/hxbchina/debitcard/crawler");
		return taskBank;
	}

	@PostMapping(path = "/crawler")
	public TaskBank debitcardCrawlerChina(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank = crawlerAllService.getAllData(bankJsonBean);
		return taskBank;
	}

	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = loginService.quit(bankJsonBean);
		return taskBank;
	}
}
