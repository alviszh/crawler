package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.bocom"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.bocom"})
public class BocomHandle {
	
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private BocomService bocomService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	
	@Async
	public TaskBank processor(BankJsonBean bankJsonBean){
		tracerLog.output("crawler.bank.processor", bankJsonBean.getTaskid());
		
		bocomService.login(bankJsonBean);
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if(taskBank.getPhase().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase()) && 
				taskBank.getPhase_status().equals(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus())){
			bocomService.getAllData(bankJsonBean);
		}else{
			tracerLog.output("crawler.bank.processor.taskbank", taskBank.toString());
		}

		return taskBank;
		
	}
	
	@Async
	public TaskBank sendSMS(BankJsonBean bankJsonBean){
		tracerLog.output("crawler.bank.processor.sendSMS", bankJsonBean.getTaskid());
		
		bocomService.sendSms(bankJsonBean);
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if(taskBank.getPhase().equals(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase()) && 
			taskBank.getPhase_status().equals(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus())){
			bocomService.getAllData(bankJsonBean);
		}else{
			tracerLog.output("crawler.bank.processor.sendSMS.taskbank", taskBank.toString());
		}
		return taskBank;
		
	}
	
	
	
	
}
