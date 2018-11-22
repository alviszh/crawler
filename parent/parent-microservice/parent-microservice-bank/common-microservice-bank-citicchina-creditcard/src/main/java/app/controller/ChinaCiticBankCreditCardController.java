package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.ChinaCiticBankCommonService;
import app.service.TaskBankStatusService;
      
@RestController  
@Configuration
@RequestMapping("/bank/citicchina/creditcard")
public class ChinaCiticBankCreditCardController {
	@Autowired   
	private TracerLog tracer;
	@Autowired    
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private ChinaCiticBankCommonService chinaCiticBankCommonService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	
		 //登陆htmlunit
		@PostMapping(path = "/loginHtmlunit")
		public TaskBank CiticChinaLoginCreditHtmlunit(@RequestBody BankJsonBean bankJsonBean){
			TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			tracer.addTag("action.crawler.bank.login.htmlunit", bankJsonBean.getTaskid());
			//准备登陆 
//			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
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
			try{
				chinaCiticBankCommonService.login(bankJsonBean);			
			}catch(Exception e){
				tracer.addTag("login.exception", "登陆异常");
				tracer.addTag("action.crawler.bank.login.exception", e.getMessage());
			}
			return taskBank;
			
		}
		
		
		
		//爬取
		@PostMapping(path = "/crawler")
		public TaskBank creditcardCrawlerChina(@RequestBody BankJsonBean bankJsonBean){
			
			tracer.addTag("action.crawler.bank.crawler", bankJsonBean.getTaskid());
			boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
			TaskBank taskBank = null;
			if(isDoing){
				tracer.addTag("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
			}else{
//				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
//						null, false, bankJsonBean.getTaskid());
				chinaCiticBankCommonService.getAllData(bankJsonBean);
			}
			return taskBank;
			
		}
		

		
		
		//保存验证码
		@PostMapping(path = "/saveCode")
		public TaskBank creditcardSaveCode(@RequestBody BankJsonBean bankJsonBean){
			
			tracer.addTag("action.crawler.bank.saveCode", bankJsonBean.getTaskid());
			TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//			TaskBank taskBank = null;
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(), 
//						BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(), 
//						BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(), 
//						null, false, bankJsonBean.getTaskid());
				
			chinaCiticBankCommonService.verifySms(bankJsonBean);
			return taskBank;
		}

		
}
