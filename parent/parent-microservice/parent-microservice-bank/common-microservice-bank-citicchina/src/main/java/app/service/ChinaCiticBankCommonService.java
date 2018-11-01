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

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.citicchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.citicchina"})
public class ChinaCiticBankCommonService implements ICrawlerLogin{

	@Value("${filesavepath}") 
    String fileSavePath;
	@Autowired
	private ChinaCiticBankService chinaCiticBankService;
	@Autowired
	private TaskBankRepository taskBankRepository;

	//登陆储蓄卡
	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		chinaCiticBankService.login(bankJsonBean,taskBank);
		return taskBank;
	}
	
	//爬取储蓄卡
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		chinaCiticBankService.UserInfocrawler(taskBank,bankJsonBean);
		chinaCiticBankService.debitCardRegularCrawler(taskBank,bankJsonBean);
		chinaCiticBankService.Accountcrawler(taskBank,bankJsonBean);
		return taskBank;
	}
	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}



		
	

}
