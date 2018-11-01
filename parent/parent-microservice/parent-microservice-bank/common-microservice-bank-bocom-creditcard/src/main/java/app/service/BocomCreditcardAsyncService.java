package app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBalance;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBillNow;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardHtml;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardTransFlow;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardUser;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardWechat;
import com.microservice.dao.repository.crawler.bank.bocom.creditcard.BocomCreditcardBalanceRepository;
import com.microservice.dao.repository.crawler.bank.bocom.creditcard.BocomCreditcardBillNowRepository;
import com.microservice.dao.repository.crawler.bank.bocom.creditcard.BocomCreditcardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.bocom.creditcard.BocomCreditcardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.bocom.creditcard.BocomCreditcardUserRepository;
import com.microservice.dao.repository.crawler.bank.bocom.creditcard.BocomCreditcardWechatRepository;

import app.bean.BalanceResult;
import app.bean.JsonRootBeanBocom;
import app.parser.BocomCreditcardParse;

/**   
*    
* 项目名称：common-microservice-bank-bocom-creditcard   
* 类名称：BocomCreditcardAsyncService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月24日 下午6:25:14   
* @version        
*/
@Component
public class BocomCreditcardAsyncService {

	@Autowired
	private BocomCreditcardLoginAndGetService bocomCreditcardLoginAndGetService;
	@Autowired
	private BocomCreditcardBalanceRepository bocomCreditcardBalanceRepository;
	@Autowired
	private BocomCreditcardTransFlowRepository bocomCreditcardTransFlowRepository;
	
	@Autowired
	private BocomCreditcardHtmlRepository bocomCreditcardHtmlRepository;
	@Autowired
	private BocomCreditcardUserRepository bocomCreditcardUserRepository;
	@Autowired
	private BocomCreditcardWechatRepository bocomCreditcardWechatRepository;
	@Autowired
	private BocomCreditcardBillNowRepository bocomCreditcardBillNowRepository;
	
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	
	
	public String crawlerBillByDate(WebClient webClient, String cardnum, BankJsonBean bankJsonBean,String billdateString) {
		try {
			String html = bocomCreditcardLoginAndGetService.getBill(webClient, cardnum,
					billdateString.replaceAll("-", "").trim());

			BalanceResult balanceResult = BocomCreditcardParse.balance_parse(html);
			if(balanceResult == null){
				return null;
			}

			BocomCreditcardBalance bocomCreditcardBalance = balanceResult.getBocomCreditcardBalance();
			bocomCreditcardBalance.setTaskid(bankJsonBean.getTaskid());
			bocomCreditcardBalanceRepository.save(bocomCreditcardBalance);

			List<BocomCreditcardTransFlow> list = balanceResult.getList();

			for (BocomCreditcardTransFlow transflow : list) {
				transflow.setTaskid(bankJsonBean.getTaskid());
				bocomCreditcardTransFlowRepository.save(transflow);
			}

			//Thread.sleep(10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void crawlerBill(WebClient webClient, String cardnum, BankJsonBean bankJsonBean) {
		// List<Future<String>> listfuture = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			String billdateString = LocalDate.now().withDayOfMonth(2).plusMonths(-i) + ""; // 2017-03-02
			System.out.println("============cardnum============" + cardnum);
			System.out.println("============billdateString============" + billdateString);
			crawlerBillByDate(webClient, cardnum, bankJsonBean, billdateString);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(),
				BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
		taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());

	}

	@Async
	public void crawlerUserInfo(WebClient webClient, BankJsonBean bankJsonBean) {

		try {
			String html = bocomCreditcardLoginAndGetService.getUserInfo(webClient);

			JsonRootBeanBocom jsonRootBeanBocom = BocomCreditcardParse.userinfo_parse(html);
			BocomCreditcardUser bocomCreditcardUser = jsonRootBeanBocom.getUser();
			bocomCreditcardUser.setTaskid(bankJsonBean.getTaskid());
			bocomCreditcardUserRepository.save(bocomCreditcardUser);

			List<BocomCreditcardWechat> wechat = jsonRootBeanBocom.getWechat();
			for (BocomCreditcardWechat result : wechat) {
				result.setTaskid(bankJsonBean.getTaskid());
				bocomCreditcardWechatRepository.save(result);
			}
			BocomCreditcardHtml bocomCreditcardHtml = new BocomCreditcardHtml();
			bocomCreditcardHtml.setHtml(html);
			bocomCreditcardHtml.setTaskid(bankJsonBean.getTaskid());
			bocomCreditcardHtmlRepository.save(bocomCreditcardHtml);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
				BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
		taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
	}

	@Async
	public void crawlerBillNow(WebClient webClient, BankJsonBean bankJsonBean, String cardNo) {

		try {
			String html = bocomCreditcardLoginAndGetService.getBillNow(webClient, cardNo);

			BocomCreditcardBillNow bocomCreditcardBillNow = BocomCreditcardParse.billnow_parse(html);
			bocomCreditcardBillNow.setTaskid(bankJsonBean.getTaskid());
			bocomCreditcardBillNow = bocomCreditcardLoginAndGetService.getBillNow2(webClient, cardNo,
					bocomCreditcardBillNow);
			bocomCreditcardBillNowRepository.save(bocomCreditcardBillNow);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public TaskBank quit(BankJsonBean bankJsonBean) {
		return bocomCreditcardLoginAndGetService.quit(bankJsonBean);
	}
	
}
