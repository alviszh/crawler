package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.ServicePABChinaCreditAop;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/pabchina/credit")
public class ControllerPABChinaCreditAop {
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private ServicePABChinaCreditAop servicePABChinaCreditAop;

	@Autowired
	private AgentService agentService;
	@Value("${spring.application.name}")
	String appName;

	/**
	 * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
	 * @param bankJsonBean
	 */
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("平安银行（信用卡）集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank;
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/pabchina/credit/login", 180000L);
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
		}

		return taskBank;
	}

	@PostMapping(path = "/login")
	public TaskBank debitcardLoginCcbChina(@RequestBody BankJsonBean bankJsonBean) {

		tracerLog.qryKeyValue("平安银行（信用卡）业务登录的调用...", bankJsonBean.getTaskid());

		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		try {
			servicePABChinaCreditAop.login(bankJsonBean);
		} catch (Exception e) {
			tracerLog.addTag("平安银行（信用卡）业务登录的调用异常...", e.getMessage());
		}
		return taskBank;
	}

	// 爬取和解析中间层
	@PostMapping(path = "/getAllDataAgent")
	public TaskBank getAllDataAgent(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("平安银行（信用卡）爬取和解析集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank;
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/pabchina/getAllData");
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	// 爬取和解析
	@PostMapping(path = "/getAllData")
	public TaskBank getAllData(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("平安银行（信用卡）爬取和解析的业务调用...", bankJsonBean.getTaskid());
		try {
			servicePABChinaCreditAop.getAllData(bankJsonBean);
		} catch (Exception e) {
			tracerLog.addTag("平安银行（信用卡）爬取和解析的业务调用异常...", e.getMessage());
		}
		return new TaskBank();
	}

	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = servicePABChinaCreditAop.quit(bankJsonBean);
		return taskBank;
	}
}
