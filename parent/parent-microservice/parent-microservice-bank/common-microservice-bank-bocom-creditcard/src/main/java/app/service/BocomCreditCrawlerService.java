package app.service;

import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.parser.BocomCreditcardParse;
import app.unit.CommonUnitForBocomCreditcard;
import app.service.TaskBankStatusService;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;


/**
 * 
 * 项目名称：common-microservice-bank-bocom-creditcard 类名称：BocomCreditcardService
 * 类描述： 创建人：hyx 创建时间：2017年11月24日 上午10:20:06
 * 
 * @version
 */
@Component
public class BocomCreditCrawlerService extends AbstractChaoJiYingHandler implements ISms, ICrawlerLogin{

	@Autowired
	private BocomCreditcardLoginAndGetService bocomCreditcardLoginAndGetService;
	
	@Autowired
	private BocomCreditcardAsyncService bocomCreditcardAsyncService;
	
	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TaskBankRepository taskBankRepository;
	
	private WebDriver driver = null;

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

		if(CommonUnitForBocomCreditcard.isMobileNO(bankJsonBean.getLoginName())){
			driver = bocomCreditcardLoginAndGetService.loginForPhone(bankJsonBean);
		}else if(CommonUnitForBocomCreditcard.isEmailNO(bankJsonBean.getLoginName())){
			driver = bocomCreditcardLoginAndGetService.loginForPhone(bankJsonBean);
		}else{
			driver = bocomCreditcardLoginAndGetService.loginForCard(bankJsonBean);
		}

		
		if (driver == null) {
			agentService.releaseInstance(bankJsonBean.getIp(), driver);

			return taskBank;
		}

//		crawlerForNoCode(driver, bankJsonBean);
//		agentService.releaseInstance(bankJsonBean.getIp(), driver);
		taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		return taskBank;
	}

	
	/**   
	  *    
	  * 项目名称：common-microservice-bank-bocom-creditcard  
	  * 所属包名：app.service
	  * 类描述：   未启用
	  * 创建人：hyx 
	  * 创建时间：2018年6月21日 
	  * @version 1  
	  * 返回值    TaskBank
	  */
//	public TaskBank setCode(BankJsonBean bankJsonBean) throws Exception {
//		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//		taskBank.setPhase(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase());
//		taskBank.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus());
//		taskBank.setDescription(BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription());
//		taskBank.setError_code(BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code());
//
//		taskBank = taskBankRepository.save(taskBank);
//		try {
//			WebDriver driver = bocomCreditcardLoginAndGetService.setSMSCode(bankJsonBean);
//
//			if (taskBank != null ) {
//				
//				crawlerForNoCode(driver, bankJsonBean);
//				
//				return taskBank;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			taskBank.setPhase(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase());
//			taskBank.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus());
//			taskBank.setDescription(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription());
//			taskBank.setError_message(e.getMessage());
//			taskBank.setError_code(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code());
//			taskBank = taskBankRepository.save(taskBank);
//
//			return taskBank;
//		}
//		return taskBank;
//
//	}

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		
		String cardnum = BocomCreditcardParse.getCardnum(driver.getPageSource());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("creditcardapp.bankcomm.com", cookie.getName(), cookie.getValue());

			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		try {
			String cardNo = bocomCreditcardLoginAndGetService.getCardNo(webClient);

			bocomCreditcardAsyncService.crawlerBillNow(webClient, bankJsonBean, cardNo);

			bocomCreditcardAsyncService.crawlerBill(webClient, cardnum, bankJsonBean);

			String url_urerinfo = "https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html";
			driver.get(url_urerinfo);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			cookiesDriver = driver.manage().getCookies();

			webClient = WebCrawler.getInstance().getNewWebClient();
			for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
				Cookie cookieWebClient = new Cookie("creditcardapp.bankcomm.com", cookie.getName(), cookie.getValue());

				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			// driver.quit();
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
			bocomCreditcardAsyncService.crawlerUserInfo(webClient, bankJsonBean);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), e1.getMessage(),
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
		}
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


	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
