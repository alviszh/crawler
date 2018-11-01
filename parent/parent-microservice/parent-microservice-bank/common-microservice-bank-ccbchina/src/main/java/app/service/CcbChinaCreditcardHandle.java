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
public class CcbChinaCreditcardHandle {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private CcbChinaCreditcardService ccbChinaCreditcardService;
	@Autowired
	private CcbChinaService ccbChinaService;
	
	@Async
	public TaskBank processor(BankJsonBean bankJsonBean){
		
		tracerLog.output("crawler.bank.login.creditcard.processor", bankJsonBean.getTaskid());
		ccbChinaCreditcardService.login(bankJsonBean);
		TaskBank taskBank = ccbChinaService.findTaskBank(bankJsonBean);
		if(taskBank.getPhase().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase())
				&& taskBank.getPhase_status().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus())){			
			ccbChinaCreditcardService.getAllData(bankJsonBean);
		}else{
			tracerLog.output("crawler.bank.login.creditcard.processor.taskBank", taskBank.toString());
		}
		
		return taskBank;
		
	}

}
