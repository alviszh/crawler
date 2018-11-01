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
import app.service.IcbcChinaService;
import app.service.MidService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/icbcchina/creditcard") 
public class IcbcChinaCreditController {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private IcbcChinaService icbcChinaService;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private MidService midService;
	@Autowired
	private AgentService agentService;
	
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception {
		tracer.addTag("crawler.bank.credit.login", bankJsonBean.getTaskid());   
		TaskBank taskBank = new TaskBank();
		try {
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/icbcchina/creditcard/login", 300000L); 
		} catch (RuntimeException e) {
			tracer.qryKeyValue("taskid", bankJsonBean.getTaskid());
			tracer.addTag("icbcBankController.creditcard.loginAgent.exception", e.getMessage());
		}
		return taskBank;
	}
	
	@PostMapping(path = "/login")
	public TaskBank loginIcbcChina(@RequestBody BankJsonBean bankJsonBean){
		tracer.addTag("crawler.bank.credit.login", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
		try{
			midService.login(bankJsonBean);			
		}catch(Exception e){
			tracer.addTag("crawler.bank.credit.login.exception", e.getMessage());
		}
		return taskBank;
		
	}
	
	/*@PostMapping(path = "/crawler")
	public TaskBank crawler(@RequestBody BankJsonBean bankJsonBean){
		
		tracer.addTag("crawler.bank.crawler", bankJsonBean.getTaskid());
		
		boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim());
		if(isDoing){
			tracer.addTag("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
		}else{
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
					BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
					BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
					null, false, bankJsonBean.getTaskid());
			try{
				icbcChinaService.getData(bankJsonBean);			
			}catch(Exception e){
				tracer.addTag("crawler.bank.crawler.exception", e.getMessage());
			}
		}
		return taskBank;
	}*/
	
	@PostMapping(path = "/setSMSCodeAgent")
	public TaskBank setSMSCodeAgent(@RequestBody BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception {
		tracer.addTag("crawler.bank.credit.setSMSCode", bankJsonBean.getTaskid());   
		TaskBank taskBank =  taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim()); 
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		try {
			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/icbcchina/creditcard/setSMSCode");
		} catch (RuntimeException e) {
			tracer.qryKeyValue("taskid", bankJsonBean.getTaskid());
			tracer.addTag("icbcBankController.creditcard.setSMSCodeAgent.exception", e.getMessage());
		}
		return taskBank;
	}
	
	//验证短信验证码
	@PostMapping(path = "/setSMSCode")
	public TaskBank setSMSCode(@RequestBody BankJsonBean bankJsonBean){
		tracer.addTag("crawler.bank.credit.setSMSCode.taskid", bankJsonBean.getTaskid());
		
		boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim());
		if(isDoing){
			tracer.addTag("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
		}else{
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(),BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(),
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(),BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code(), false, bankJsonBean.getTaskid());
			icbcChinaService.verifySms(bankJsonBean);
		}
		return taskBank;
	}
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = icbcChinaService.quitDriver(bankJsonBean);
		return taskBank;
	}
}
