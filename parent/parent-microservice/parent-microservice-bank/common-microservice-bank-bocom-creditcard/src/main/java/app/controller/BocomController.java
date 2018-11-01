package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.BocomCreditService;
import app.service.TaskBankStatusService;


@RestController
@Configuration
@RequestMapping("/bank/bocom/creditcard") 
public class BocomController {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private BocomCreditService bocomCreditService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TaskBankRepository taskBankRepository;
	
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {

		tracerLog.output("中国银行集群的调用...", bankJsonBean.getTaskid());

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim());

		taskBank.setPhase(BankStatusCode.BANK_LOGIN_DOING.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_DOING.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_DOING.getDescription());
		taskBank.setError_code(BankStatusCode.BANK_LOGIN_DOING.getError_code());
		taskBank.setBankType(bankJsonBean.getBankType());
		taskBank.setCardType(bankJsonBean.getCardType());
		taskBank.setLoginType(bankJsonBean.getLoginType());
		taskBank.setTesthtml(bankJsonBean.toString());
		taskBank = taskBankRepository.save(taskBank);
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/bocom/creditcard/login", 300000L);
//			taskBank.setCrawlerHost(crawlerHost);
		} catch (Exception e) {
			e.printStackTrace();
			taskBank.setError_message(e.getMessage());
			taskBank.setTesthtml(bankJsonBean.toString());
			taskBank = taskBankRepository.save(taskBank);
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			return taskBank;
		}

		return taskBank;
	}
	
	@PostMapping(path = "/login")
	public BankJsonBean login(@RequestBody BankJsonBean bankJsonBean){
		
		tracerLog.output("taskid", bankJsonBean.getTaskid());
		try {
			bocomCreditService.login(bankJsonBean);
		} catch (Exception e) {
			bocomCreditService.quit(bankJsonBean);
			tracerLog.output("crawler.bank.login.cardnum.exception", e.getMessage());
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid());
		}
	
		return bankJsonBean;
	}
	
	//验证短信验证码
	@PostMapping(path = "/setSmsCode")
	public void setSMSCode(@RequestBody BankJsonBean bankJsonBean){
		tracerLog.output("crawler.bank.setSMSCode.taskid", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim());

		taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(),
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code(), false, bankJsonBean.getTaskid());
		
		taskBankRepository.save(taskBank);
		
	}	
	
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = bocomCreditService.quit(bankJsonBean);
		return taskBank;
	}
	
}
