package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.ServiceABCChinaAop;
import app.service.TaskBankStatusService;
import app.service.aop.ICrawler;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@RestController
@Configuration
@RequestMapping("/bank/abcchina")
public class ControllerABCChinaCreditAop {

	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private ServiceABCChinaAop serviceABCChinaAop;

	@Value("${spring.application.name}")
	String appName;
	@Autowired
	private AgentService agentService;

	// 登录中间层
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		TaskBank taskBank;
		tracerLog.qryKeyValue("农业银行（储蓄卡）集群的调用...", bankJsonBean.getTaskid());
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/abcchina/login", 180000L);
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	// 登录业务
	@PostMapping(path = "/login")
	public TaskBank debitcardLoginCcbChina(@RequestBody BankJsonBean bankJsonBean) {

		tracerLog.qryKeyValue("农业银行（储蓄卡）业务登录的调用...", bankJsonBean.getTaskid());

		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		try {
			serviceABCChinaAop.login(bankJsonBean);
		} catch (Exception e) {
			tracerLog.addTag("农业银行（储蓄卡）业务登录的调用异常", e.getMessage());
		}
		return taskBank;
	}

	// 发送验证码中间层
	@PostMapping(path = "/sendSmsAgent")
	public TaskBank sendSmsAgent(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {
		tracerLog.qryKeyValue("农业银行（储蓄卡）发送验证码集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank;
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/abcchina/sendSms");
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	// 发送验证码
	@PostMapping(path = "/sendSms")
	public TaskBank sendSms(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("农业银行（储蓄卡）发送验证码的业务调用...", bankJsonBean.getTaskid());
		try {
			serviceABCChinaAop.sendSms(bankJsonBean);
		} catch (Exception e) {
			tracerLog.addTag("农业银行（储蓄卡）发送验证码的业务调用异常...", e.getMessage());
		}
		return new TaskBank();
	}

	// 验证验证码中间层
	@PostMapping(path = "/verifySmsAgent")
	public TaskBank verifySmsAgent(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {
		tracerLog.qryKeyValue("农业银行（储蓄卡）验证验证码集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank;
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/abcchina/verifySms");
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	// 验证验证码
	@PostMapping(path = "/verifySms")
	public TaskBank verifySms(@RequestBody BankJsonBean bankJsonBean) {

		tracerLog.qryKeyValue("农业银行（储蓄卡）验证验证码的业务调用...", bankJsonBean.getTaskid());

		try {
			serviceABCChinaAop.verifySms(bankJsonBean);
		} catch (Exception e) {
			tracerLog.addTag("农业银行（储蓄卡）验证验证码的业务调用异常...", e.getMessage());
		}
		return new TaskBank();
	}

	// 爬取和解析中间层
	@PostMapping(path = "/getAllDataAgent")
	public TaskBank getAllDataAgent(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {
		tracerLog.qryKeyValue("农业银行（储蓄卡）爬取和解析集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank;
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/abcchina/getAllData");
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
		tracerLog.qryKeyValue("农业银行（储蓄卡）爬取和解析的业务调用...", bankJsonBean.getTaskid());
		try {
			serviceABCChinaAop.getAllData(bankJsonBean);
		} catch (Exception e) {
			tracerLog.addTag("农业银行（储蓄卡）爬取和解析的业务调用异常...", e.getMessage());
		}
		return new TaskBank();
	}

	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = serviceABCChinaAop.quit(bankJsonBean);
		return taskBank;
	}
}
