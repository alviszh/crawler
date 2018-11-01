package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.citicchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.citicchina"})
public class ChinaCiticBankCommonService implements ICrawlerLogin,ISms{

	@Value("${filesavepath}") 
    String fileSavePath;
	@Autowired
	private ChinaCiticBankService chinaCiticBankService;
	@Autowired
	private TaskBankRepository taskBankRepository;

	//登陆信用卡
	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		chinaCiticBankService.loginCreditCardHtmlunit(bankJsonBean,taskBank,fileSavePath);
		return taskBank;
	}
	
	//爬取信用卡
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		chinaCiticBankService.creditCardUserInfoCrawlerHtmlunit(taskBank,bankJsonBean);
		chinaCiticBankService.creditCardAccountCrawlerHtmlunit(taskBank,bankJsonBean);
		
		chinaCiticBankService.creditCardBillCrawlerHtmlunit(taskBank,bankJsonBean);
		return taskBank;
	}


	//保存验证码
	@Async
	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		chinaCiticBankService.creditcardSaveCodeHtmlunit(taskBank,bankJsonBean);
		return taskBank;
	}
	


	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}


	




		
	

}
