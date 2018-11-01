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
import app.service.BocomHandle;
import app.service.BocomService;
import app.service.TaskBankStatusService;


@RestController
@Configuration
@RequestMapping("/bank/bocom/debitcard") 
public class BocomController {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private BocomHandle handle;
	@Autowired
	private BocomService bocomService;
	@Autowired
	private AgentService agentService;
	@Value("${spring.application.name}") 
	String appName;
	
	
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception { 
		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());   
		TaskBank taskBank = null;
		try{
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/bocom/debitcard/login");
			
		}catch(RuntimeException e){
			tracerLog.output("loginAgent", "runtimeexception");
			tracerLog.output("runtimeexception", e.getMessage());
//			taskBank.setPhase(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase());
//			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus());
//			taskBank.setDescription("系统繁忙，请稍后再试");
//			taskBank.setFinished(true);
//			taskBank = bocomService.save(taskBank);
		}
	    return taskBank;
	    
	}
	
	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.login", bankJsonBean.getTaskid());
		
		TaskBank taskBank = handle.processor(bankJsonBean);			

		return taskBank;
	}
	
	@PostMapping(path = "/sendsms")
	public TaskBank sendSms(@RequestBody BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.sendsms", bankJsonBean.getTaskid());
//		//改为短信发送中状态
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_DONING.getPhase(), 
//				BankStatusCode.BANK_SEND_CODE_DONING.getPhasestatus(), BankStatusCode.BANK_SEND_CODE_DONING.getDescription(), 
//				200, false, bankJsonBean.getTaskid());
//		
		TaskBank taskBank = handle.sendSMS(bankJsonBean);
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
//			
//			bocomService.crawler(taskBank);
//					
//		}
//		return taskBank;
//		
//	}
}
