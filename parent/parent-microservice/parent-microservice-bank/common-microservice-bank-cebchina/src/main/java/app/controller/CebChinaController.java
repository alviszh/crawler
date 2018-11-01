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
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CebChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/cebchina/debitcard") 
public class CebChinaController {
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CebChinaService cebChinaService;
	@Value("${spring.application.name}") 
	String appName;
	public final static long INTERVAL_TIME =  5 * 60 * 1000;
	/***
	 * 
	 * @param bankJsonBean
	 * @throws Exception
	 */
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		tracer.output("crawler.bank.login", bankJsonBean.getTaskid());   
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		try{
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/cebchina/debitcard/login",
					INTERVAL_TIME); 
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			tracer.output("CebChinaController.loginAgent.exception", e.getMessage());
			System.out.println("CebChinaController.loginAgent.exception=" + e.getMessage());
			return taskBank;
		}
		return taskBank;
	}

	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){

		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		tracer.output("crawler.bank.login", bankJsonBean.getTaskid());
		try {
			cebChinaService.loginCombo(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskBank;
	}

	@PostMapping(path = "/sendSmsCodeAgent")
	public TaskBank sendSmsCodeAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracer.output("crawler.bank.sendcode.agent", bankJsonBean.toString());  
		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(), 
				null, false, bankJsonBean.getTaskid());

		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());

		// 发送短信
		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cebchina/debitcard/setSmsCode");  
		return taskBank;
	} 
	//验证短信验证码
	@PostMapping(path = "/setSmsCode")
	public TaskBank setSMSCode(@RequestBody BankJsonBean bankJsonBean){
		tracer.output("crawler.bank.setSMSCode.taskid", bankJsonBean.getTaskid());

		boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim());
		if(isDoing){
			tracer.output("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
		}else{
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(),
					BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code(), false, bankJsonBean.getTaskid());
			taskBank.setBankType(bankJsonBean.getBankType());
			taskBank.setLoginType(bankJsonBean.getLoginType());
			taskBank.setCardType(bankJsonBean.getCardType());
			taskBankRepository.save(taskBank);
			try {
				cebChinaService.setSMSCode(bankJsonBean);
			} catch (Exception e) {
				tracer.output("短信验证失败", e.toString());
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
	                    BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
	                    BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(),
	                    BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				tracer.output("cebChinaService.setSMSCode.exception", e.getMessage());
	            System.out.println("cebChinaService.setSMSCode.exception=" + e.getMessage());
	            return taskBank;
			}
		}
		return taskBank;
	}	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = cebChinaService.quit(bankJsonBean);
		return taskBank;
	}
}
