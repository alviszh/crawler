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

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CcbChinaCreditcardHandle;
import app.service.CcbChinaCreditcardService;
import app.service.CcbChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/ccbchina/creditcard") 
public class CcbChinaCreditcardController {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private CcbChinaCreditcardService ccbChinaCreditcardService;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CcbChinaService ccbChinaService;
	@Autowired
	private CcbChinaCreditcardHandle handle;
	
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception { 
		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());  
		TaskBank taskBank = ccbChinaService.findTaskBank(bankJsonBean);
		try{
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/ccbchina/creditcard/login");
			
		}catch(RuntimeException e){
			tracerLog.output("loginAgent", "runtimeexception");
			tracerLog.output("runtimeexception", e.getMessage());
//			taskBank.setPhase(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase());
//			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus());
//			taskBank.setDescription("系统繁忙，请稍后再试");
//			taskBank.setFinished(true);
//			taskBank = ccbChinaService.saveTask(taskBank);
		}
		return taskBank;	    
	}
	

	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.login.creditcard", bankJsonBean.getTaskid());
		
//		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
		
//		try{
//			ccbChinaCreditcardService.login(bankJsonBean);
//		}catch(Exception e){
//			tracerLog.output("crawler.bank.login.creditcard.exception", e.getMessage());
//			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
//					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
//					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
//					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//		}
		TaskBank taskBank = handle.processor(bankJsonBean);
		
		return taskBank;
		
	}

}
