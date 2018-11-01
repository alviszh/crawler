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
import app.service.BocAysncService;
import app.service.BocService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/boc/debitcard")
public class BocController {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private BocAysncService bocAysncService;

	@Autowired
	private TaskBankRepository taskBankRepository;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private BocService bocService;
	

	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {

		tracerLog.output("中国银行集群的调用...", bankJsonBean.getTaskid());
		tracerLog.output("taskid", bankJsonBean.getTaskid());

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
			taskBank = agentService.postAgent(bankJsonBean, "/bank/boc/debitcard/login", 300000L);
		} catch (Exception e) {
			e.printStackTrace();
			taskBank.setError_message(e.getMessage());
			taskBank.setTesthtml(bankJsonBean.toString());
			taskBank = taskBankRepository.save(taskBank);
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(), BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			tracerLog.output("BeijingBankDebitCardController.loginAgent.exception", e.getMessage());
			return taskBank;
		}

		return taskBank;
	}

	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean) throws Exception {

		tracerLog.output("taskid", bankJsonBean.getTaskid());
		tracerLog.output("bankJsonBean",bankJsonBean.toString());
		bocService.crawler(bankJsonBean);
		
		return null;
	}

	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean) {
		TaskBank taskBank = bocAysncService.quit(bankJsonBean);
		return taskBank;
	}

}
