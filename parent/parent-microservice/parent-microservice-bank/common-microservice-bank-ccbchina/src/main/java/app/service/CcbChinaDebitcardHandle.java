package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.ccbchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.ccbchina"})
public class CcbChinaDebitcardHandle {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private CcbChinaService ccbChinaService;
	
	@Async
	public TaskBank processor(BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.processor", bankJsonBean.getTaskid());  
		
		ccbChinaService.login(bankJsonBean);
		
		TaskBank taskBank = ccbChinaService.findTaskBank(bankJsonBean);
		if(null != taskBank){
			//不需要短信
			if(taskBank.getPhase().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase())
				&& taskBank.getPhase_status().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus())){			
				ccbChinaService.getAllData(bankJsonBean);
			}
			//需要短信验证码
			else if(taskBank.getPhase().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase())
				&& taskBank.getPhase_status().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus())){
				tracerLog.output("crawler.bank.login.processor.taskBank", taskBank.toString());
			}
			//登录出错
			else{
				tracerLog.output("crawler.bank.login.processor.taskBank", taskBank.toString());
			}
		}
		return taskBank;
		
	}
	
	@Async
	public TaskBank sendSms(BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.sendSms", bankJsonBean.getTaskid());  
		
		ccbChinaService.sendSms(bankJsonBean);
		TaskBank taskBank = ccbChinaService.findTaskBank(bankJsonBean);
		//发送及验证短信成功
		if(taskBank.getPhase().equals("LOGIN") && 
				taskBank.getPhase_status().equals("SMS_SUCCESS")){
			ccbChinaService.getAllData(bankJsonBean);
		}else{
			tracerLog.output("crawler.bank.sendsms.processor.taskBank", taskBank.toString());
		}
		return taskBank;
		
	}
	
	
	

}
