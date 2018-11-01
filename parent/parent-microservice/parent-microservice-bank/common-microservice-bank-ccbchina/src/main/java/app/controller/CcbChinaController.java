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
import com.crawler.mobile.json.StatusCodeLogin;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CcbChinaDebitcardHandle;
import app.service.CcbChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/ccbchina/debitcard") 
public class CcbChinaController {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private CcbChinaService ccbChinaService;
//	@Autowired
//	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CcbChinaDebitcardHandle handle;
	
	
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception { 
		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());  
		TaskBank taskBank = ccbChinaService.findTaskBank(bankJsonBean);
		try{
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/ccbchina/debitcard/login");
			
		}catch(RuntimeException e){
			tracerLog.output("loginAgent", "runtimeexception");
			tracerLog.output("runtimeexception", e.getMessage());
			
		}
		return taskBank;	    
	}
	
	
	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());
		
//		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
//		
//		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.CARD_NUM)){
//			try{
//				ccbChinaService.loginByCardNum(bankJsonBean);
//			}catch(Exception e){
//				tracerLog.output("crawler.bank.login.cardnum.exception", e.getMessage());
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
//						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
//						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
//						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//			}
//					
//		}
//		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.ACCOUNT_NUM)){
//			try{
//				ccbChinaService.loginByAccountNum(bankJsonBean);			
//			}catch(Exception e){
//				tracerLog.output("crawler.bank.login.accountnum.exception", e.getMessage());
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
//						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
//						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
//						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//			}		
//		}
		TaskBank taskBank = handle.processor(bankJsonBean);
		return taskBank;
		
	}
	
//	@PostMapping(path = "/crawler")
//	public TaskBank crawler(@RequestBody BankJsonBean bankJsonBean){
//		
//		tracerLog.output("crawler.bank.crawler", bankJsonBean.getTaskid());
//		
//		boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
//		TaskBank taskBank = null;
//		if(isDoing){
//			tracerLog.output("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
//		}else{
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
//					BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
//					BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
//					null, false, bankJsonBean.getTaskid());
//			if(bankJsonBean.getLoginType().equals(StatusCodeLogin.CARD_NUM)){
//				
//				Integer	page = ccbChinaService.getCrawlerOnePage(taskBank,bankJsonBean);
//				if(null != page && page>2){
//					for(int i = 2; i < page; i++){
//						try {
//							ccbChinaService.crawlerByCard(taskBank,bankJsonBean,i);
//						} catch (Exception e) {
//							tracerLog.output("crawler.bank.ccbchina.crawlerbycard.error", e.getMessage());
//							taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//									BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//									BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
//									null, true, bankJsonBean.getTaskid());
//						}						
//					}
//				}else{
//					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
//							null, true, bankJsonBean.getTaskid());
//				}
//				
//			}
//			if(bankJsonBean.getLoginType().equals(StatusCodeLogin.ACCOUNT_NUM)){
//				ccbChinaService.crawler(taskBank,bankJsonBean);				
//			}
//		}
//		return taskBank;
//		
//	}
	
	@PostMapping(path = "/sendsms")
	public TaskBank sendSms(@RequestBody BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.sendsms", bankJsonBean.getTaskid());
		//改为短信发送中状态
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_DONING.getPhase(), 
//				BankStatusCode.BANK_SEND_CODE_DONING.getPhasestatus(), "验证用户信息中，请稍后。。。", 
//				200, false, bankJsonBean.getTaskid());
		
		TaskBank taskBank = handle.sendSms(bankJsonBean);
		return taskBank;
		
	}

}
