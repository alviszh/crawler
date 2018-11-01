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
import app.service.HfbChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/hfbchina/debitcard")
public class HfbChinaController extends AgentService {

	@Autowired
	HfbChinaService cmbChinaService;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TaskBankRepository taskBankRepository;

	@Autowired
	private TracerLog tracerLog;
	
	public final static long INTERVAL_TIME =  5 * 60 * 1000;

	// 登录的代理接口，对登录请求转发到限制的实例上
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.login", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/hfbchina/debitcard/login",INTERVAL_TIME);
        } catch (Exception e) {
            tracerLog.addTag("CzbChinaController.loginAgent.exception", e.getMessage());
        }
		return taskBank;
	}

	// 登录接口
	@PostMapping(path = "/login")
	public TaskBank logincmbchina(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.login", bankJsonBean.toString());
//		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		TaskBank taskBank = cmbChinaService.login(bankJsonBean);
		return taskBank;
	}

	// 数据采集代理接口，将采集请求按照taskid 转发和登录步骤的那台机器上运行（为了复用 webdriver）
	@PostMapping(path = "/crawlerAgent")
	public TaskBank crawlerAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.crawler.agent", bankJsonBean.getTaskid());
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
//				BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), BankStatusCode.BANK_CRAWLER_DOING.getDescription(),
//				null, false, bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());

		taskBank = agentService.postAgentCombo(bankJsonBean, "/bank/hfbchina/debitcard/crawler");
		return taskBank;

	}

	// 数据采集接口
	@PostMapping(path = "/crawler")
	public TaskBank crawler(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.crawler", bankJsonBean.getTaskid());
		// 无需短信，直接开始爬取（异步）
		TaskBank taskBank = cmbChinaService.getAllData(bankJsonBean); 
		
		return taskBank;
	}

	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = cmbChinaService.quit(bankJsonBean);
		return taskBank;
	}

}
