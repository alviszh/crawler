package app.service.credit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardUserInfoResult;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.bean.Response;
import app.bean.auuount.AccountJsonRootBean;
import app.bean.auuount.AccountSeqResult;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.BocCreditParse;
import app.service.AgentService;
import app.service.TaskBankStatusService;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
@EnableAsync
public class BocUnitCreditCrawlerService extends AbstractChaoJiYingHandler {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private BocUnitCreditFutureService bocUnitCreditFutureService;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private BocServiceCreditLoginAndGet bocServiceCreditLoginAndGet;
	
	@Autowired
	private TaskBankRepository taskBankRepository;

	private String url = "https://ebsnew.boc.cn/BII/PsnGetUserProfile.do?_locale=zh_CN";
	
	@Autowired
	private AgentService agentService;
	
	public Object crawlerForCredit(BankJsonBean bankJsonBean, WebDriver driver) {
		
		
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

		taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
				BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
				BankStatusCode.BANK_CRAWLER_DOING.getDescription(), null, false, bankJsonBean.getTaskid());
		
		
		try {
			String html_accountseq = bocServiceCreditLoginAndGet.getAccountSeq(url, driver);
			
			AccountJsonRootBean accountseq_result = BocCreditParse.accountSeq_parse(html_accountseq);
			AccountSeqResult result = accountseq_result.getResponse().get(0).getResult();

			
			List<Response<BocchinaCebitCardUserInfoResult>> accountUserInfo = bocUnitCreditFutureService.getAccountUserInfo(bankJsonBean, driver, result.getAccountSeq());
			String billdate = null;
			try{
				billdate = accountUserInfo.get(0).getResult().getBillDate();

			}catch(Exception e){
				e.printStackTrace();
				taskBankStatusService.changeTaskBankFinishByStatusError(bankJsonBean.getTaskid().trim());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

				return null;
			}
			driver.findElement(By.id("div_billedtrans_740846")).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
				Cookie cookieWebClient = new Cookie("ebsnew.boc.cn", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);

			}

			String countid =null;

			try {
				countid = bocServiceCreditLoginAndGet.getCountid(url, webClient);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(countid == null){
				taskBankStatusService.changeTaskBankFinishByStatusError(bankJsonBean.getTaskid().trim());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

				return null;
			}

			List<Future<String>> list_future = new ArrayList<Future<String>>();
			for (int i = 0; i < 6; i++) {
				try {
					Future<String> future = bocUnitCreditFutureService.crawlerByDate(bankJsonBean, webClient, countid, result.getAccountSeq(), result.getAccountType(), i,billdate);
					list_future.add(future);
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			boolean istrue = true;
			int falsenum = 0;
			int listnum = list_future.size();
			while (istrue) {
				if (list_future.size() <= 0) {
					istrue = false;
				}
				for (Future<String> future : list_future) {

					if (future.isDone()) { // 判断是否执行完毕

						list_future.remove(future);

						try {
							if (future.get().indexOf("false") != -1) {
								falsenum++;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							falsenum++;
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							falsenum++;
						}

						break;
					}
				}

			}

			if (falsenum < listnum) {
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
			} else {
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
			}
			return null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("loginChromeForCreditexception", e.getMessage());
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//			driver.close();
			return null;
		}
		

	}
	
	

}
