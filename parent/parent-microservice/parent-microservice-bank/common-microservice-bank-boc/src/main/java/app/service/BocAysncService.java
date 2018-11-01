package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.service.credit.BocUnitCreditCrawlerService;
import app.service.deposit.BocUnitDepositCrawlerService;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
public class BocAysncService extends AbstractChaoJiYingHandler {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private BocServiceLoginAndGet bocServiceLoginAndGet;
	
	@Autowired
	private BocUnitCreditCrawlerService bocUnitCreditCrawlerService;
	
	@Autowired
	private BocUnitDepositCrawlerService bocUnitDepositCrawlerService;
	

	@Autowired
	private AgentService agentService;

	private WebDriver driver = null;

	public void login(BankJsonBean bankJsonBean) {
		try {
			driver = bocServiceLoginAndGet.loginChrome(bankJsonBean, 0);
			
			if(driver !=null){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid());
		}

	}

	public void loginChromeByUserName(BankJsonBean bankJsonBean) {
		try {
			driver = bocServiceLoginAndGet.loginChromeByUserName(bankJsonBean, 0);

			if(driver !=null){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
		}

	}

	public void getAllData(BankJsonBean bankJsonBean) {
		if (driver != null) {
			tracerLog.output("crawler.bank.crawler", bankJsonBean.getTaskid());

			boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
			if (isDoing) {
				tracerLog.output("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
			} else {
				if (bankJsonBean.getCardType().indexOf("CREDIT_CARD") != -1) {
					bocUnitCreditCrawlerService.crawlerForCredit(bankJsonBean, driver);
					// return
					// driver.findElement(By.id("input_txt_50531_740884"));
				} else {

					bocUnitDepositCrawlerService.crawler(bankJsonBean);
				}

			}
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
		}
	}
	
	public TaskBank quit(BankJsonBean bankJsonBean) {
		return bocServiceLoginAndGet.quit(bankJsonBean);
	}
}
