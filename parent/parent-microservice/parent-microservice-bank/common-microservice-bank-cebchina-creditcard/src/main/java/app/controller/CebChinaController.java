package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.mobile.json.StatusCodeLogin;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CebChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/cebchina/creditcard") 
public class CebChinaController {

	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private CebChinaService cebChinaService;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private AgentService agentService;
	
	public final static long INTERVAL_TIME =  3 * 60 * 1000;

	/***
	 * 
	 * @param bankJsonBean
	 * @throws Exception
	 */
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());   
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
		try{
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/cebchina/creditcard/login",
					INTERVAL_TIME); 
		} catch (RuntimeException e) {
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
                    BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
            tracerLog.output("CebChinaController.loginAgent.exception", e.getMessage());
            System.out.println("CebChinaController.loginAgent.exception=" + e.getMessage());
            return taskBank;
		}
		return taskBank;
	}



	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){

		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if(null == taskBank){
			throw new RuntimeException("Entity bean TaskBank is null ! taskid>>"+bankJsonBean.getTaskid()+"<<");
		}
		taskBank.setPhase(BankStatusCode.BANK_SEND_CODE_DONING.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_SEND_CODE_DONING.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_SEND_CODE_DONING.getDescription());
		taskBank.setError_code(BankStatusCode.BANK_SEND_CODE_DONING.getError_code());
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
		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.CARD_NUM)){
			try{
				cebChinaService.loginByCardNum(bankJsonBean);	
			}catch(Exception e){
				tracerLog.output("crawler.bank.login.cardnum.exception", bankJsonBean.getTaskid());
				taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			}

		}else if(bankJsonBean.getLoginType().equals(StatusCodeLogin.IDNUM)){
			try{
				cebChinaService.loginByCardNum(bankJsonBean);
			}catch(Exception e){
				tracerLog.output("crawler.bank.login.cardnum.exception", e.getMessage());
				taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			}	
		}else if(bankJsonBean.getLoginType().equals(StatusCodeLogin.ACCOUNT_NUM)){
			try{
				cebChinaService.loginByCardNum(bankJsonBean);
			}catch(Exception e){
				tracerLog.output("crawler.bank.login.cardnum.exception", e.getMessage());
				taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			}	
		}
		else{
			taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getDescription(), 
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(),false,bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	@PostMapping(path = "/sendSmsCodeAgent")
	public TaskBank sendSmsCodeAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.output("crawler.bank.sendcode.agent", bankJsonBean.toString());  
		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(), 
				null, false, bankJsonBean.getTaskid());
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cebchina/creditcard/setSMSCode");  
		return taskBank;
	}


	@PostMapping(path = "/setSMSCode")
	public TaskBank setsmscode(@RequestBody BankJsonBean bankJsonBean){

		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());

		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code(),false,bankJsonBean.getTaskid());


		cebChinaService.setsmscode(bankJsonBean);

		return taskBank;

	}

	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = cebChinaService.quit(bankJsonBean);
		return taskBank;
	}
}
