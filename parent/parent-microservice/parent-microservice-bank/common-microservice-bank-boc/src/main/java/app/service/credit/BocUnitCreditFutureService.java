package app.service.credit;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdAccountInfoList;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdCustomerInfo;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdScoreInfoList;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdTransList;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardUserBillActList;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardUserInfoResult;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaCebitCardCrcdAccountInfoListRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaCebitCardCrcdCustomerInfoRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaCebitCardCrcdScoreInfoListRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaCebitCardCrcdTransListRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaCebitCardUserBillActListRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaCebitCardUserInfoResultRepository;

import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.bean.Error;
import app.bean.JsonRootBean;
import app.bean.Response;
import app.bean.creditcard.TransFlowResult;
import app.bean.creditcard.TransListResult;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.BocCreditParse;
import app.service.TaskBankStatusService;
import java.util.concurrent.Future;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
@EnableAsync
public class BocUnitCreditFutureService extends AbstractChaoJiYingHandler {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private BocchinaCebitCardUserInfoResultRepository bocchinaCebitCardUserInfoResultRepository;

	@Autowired
	private BocchinaCebitCardUserBillActListRepository bocchinaCebitCardUserBillActListRepository;

	@Autowired
	private BocchinaCebitCardCrcdCustomerInfoRepository bocchinaCebitCardCrcdCustomerInfoRepository;

	@Autowired
	private BocchinaCebitCardCrcdAccountInfoListRepository bocchinaCebitCardCrcdAccountInfoListRepository;

	@Autowired
	private BocchinaCebitCardCrcdScoreInfoListRepository bocchinaCebitCardCrcdScoreInfoListRepository;

	@Autowired
	private BocchinaCebitCardCrcdTransListRepository bocchinaCebitCardCrcdTransListRepository;

	@Autowired
	private BocServiceCreditLoginAndGet bocServiceCreditLoginAndGet;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private TaskBankRepository taskBankRepository;

	private String url = "https://ebsnew.boc.cn/BII/PsnGetUserProfile.do?_locale=zh_CN";

	Gson gs = new Gson();

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 void
	 * @return 
	 * @return
	 */
	public List<Response<BocchinaCebitCardUserInfoResult>> getAccountUserInfo(BankJsonBean bankJsonBean, WebDriver driver,String acccountSeq) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("ebsnew.boc.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		try {
			//String acccountSeq = bocServiceCreditLoginAndGet.getAccountSeq(url, webClient);
			String html = bocServiceCreditLoginAndGet.getAccountUserInfo(url, webClient,acccountSeq);
			if (html.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1) {
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), "您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务",
						null, false, bankJsonBean.getTaskid());
				taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
						BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());
				return null;
			}

			JsonRootBean<BocchinaCebitCardUserInfoResult> userinfo_root = BocCreditParse.account_parse(html);
			List<Response<BocchinaCebitCardUserInfoResult>> userinfo_list = userinfo_root.getResponse();

			for (Response<BocchinaCebitCardUserInfoResult> result_res : userinfo_list) {
				Error erro = result_res.getError();
				BocchinaCebitCardUserInfoResult result = result_res.getResult();
				if (result != null) {
					result.setTaskid(bankJsonBean.getTaskid());
					bocchinaCebitCardUserInfoResultRepository.save(result);
					List<BocchinaCebitCardUserBillActList> actList = result.getActList();
					for (BocchinaCebitCardUserBillActList billresult : actList) {
						billresult.setTaskid(bankJsonBean.getTaskid());
						bocchinaCebitCardUserBillActListRepository.save(billresult);
					}
					tracerLog.output("crawler.bank.crawler.userinfo", "信用卡用户基本信息存储成功");
				} else {
					tracerLog.output("crawler.bank.crawler.userinfo", "信用卡用户基本信息:" + erro.getMessage());
				}
			}
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
					BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());

			return userinfo_list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
					BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
		}
		return null;

	}

	public Future<String> crawlerByDate(BankJsonBean bankJsonBean, WebClient webClient, String countid, String acccountSeq,String accountType,int i,String billdate){

		try{
			LocalDate today = LocalDate.now();
			LocalDate infodate = today.plusMonths(-i);
			String month = infodate.getMonthValue()+"";
			
			if(month.length()<2){
				month = "0"+month;
			}
			String creditInfo_date = infodate.getYear() + "/" + month;
			
			int id_countid = (16+i*8);
			countid = bocServiceCreditLoginAndGet.getConversationId(url, webClient, id_countid);
					
			
			
			String page = bocServiceCreditLoginAndGet.getCreditInfo(url, webClient, countid,acccountSeq ,creditInfo_date);
			if (page.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1) {
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),
						"您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务", bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
				return new AsyncResult<String>("false");
			}
			
			if(page.indexOf("对不起，您本月账单尚未生成，请于账单日后再试。如有疑问，请致电信用卡客服热线") != -1) {

				tracerLog.output("获取信用卡账单信息"+infodate+"", "对不起，您本月账单尚未生成，请于账单日后再试。如有疑问，请致电信用卡客服热线");
				return new AsyncResult<String>("false");
			}
			if(page.indexOf("已出账单月份不符合要求格式") != -1) {

				tracerLog.output("获取信用卡账单信息"+infodate+"", "已出账单月份不符合要求格式");
				return new AsyncResult<String>("false");
			}
			
			if(page.indexOf("已出账单查询失败") != -1) {

				tracerLog.output("获取信用卡账单信息"+infodate+"", "已出账单查询失败");
				return new AsyncResult<String>("false");
			}
			

			JsonRootBean<TransFlowResult> jsonroot = BocCreditParse.transflow_parse(page);
			String cardno = null;
			String creditcardId = null;
			List<Response<TransFlowResult>> response = jsonroot.getResponse();
			for (Response<TransFlowResult> responseresult : response) {
				TransFlowResult transFlowResult = responseresult.getResult();

				BocchinaCebitCardCrcdCustomerInfo crcdCustomerInfo = transFlowResult.getCrcdCustomerInfo();
				crcdCustomerInfo.setTaskid(bankJsonBean.getTaskid());
				crcdCustomerInfo.setCrcdBillInfoListString(gs.toJson(transFlowResult.getCrcdBillInfoList()));
				bocchinaCebitCardCrcdCustomerInfoRepository.save(crcdCustomerInfo);
				cardno = crcdCustomerInfo.getCardNo();
				creditcardId = crcdCustomerInfo.getCreditcardId();
				List<BocchinaCebitCardCrcdAccountInfoList> crcdAccountInfoList = transFlowResult.getCrcdAccountInfoList();
				for (BocchinaCebitCardCrcdAccountInfoList crcdAccountInfo : crcdAccountInfoList) {
					crcdAccountInfo.setTaskid(bankJsonBean.getTaskid());
					crcdAccountInfo.setCardNo(crcdCustomerInfo.getCardNo());
					bocchinaCebitCardCrcdAccountInfoListRepository.save(crcdAccountInfo);
				}

				List<BocchinaCebitCardCrcdScoreInfoList> crcdScoreInfoList = transFlowResult.getCrcdScoreInfoList();

				for (BocchinaCebitCardCrcdScoreInfoList bocchinaCebitCardCrcdScoreInfo : crcdScoreInfoList) {
					bocchinaCebitCardCrcdScoreInfo.setTaskid(bankJsonBean.getTaskid());
					bocchinaCebitCardCrcdScoreInfo.setCardNo(crcdCustomerInfo.getCardNo());
					bocchinaCebitCardCrcdScoreInfoListRepository.save(bocchinaCebitCardCrcdScoreInfo);
				}

			}
			String id = (20+i*8)+"";
			String tranList_date  = infodate.getYear() + "/" + month+ "/"+billdate;
			page = bocServiceCreditLoginAndGet.getTranList(url, webClient, countid,accountType,creditcardId, tranList_date,id);
			if (page.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1||page.indexOf("您的回话已失效") != -1) {
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),
						"您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务", bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());
				return new AsyncResult<String>("false");
			}
			
			if(page.indexOf("对不起，您本月账单尚未生成，请于账单日后再试。如有疑问，请致电信用卡客服热线") != -1) {

				tracerLog.output("获取流水信息"+tranList_date+"", "对不起，您本月账单尚未生成，请于账单日后再试。如有疑问，请致电信用卡客服热线");
				return new AsyncResult<String>("false");
			}
			if(page.indexOf("已出账单月份不符合要求格式") != -1) {

				tracerLog.output("获取流水信息"+tranList_date+"", "已出账单月份不符合要求格式");
				return new AsyncResult<String>("false");
			}
			
			if(page.indexOf("已出账单查询失败") != -1) {

				tracerLog.output("获取流水信息"+tranList_date+"", "已出账单查询失败");
				return new AsyncResult<String>("false");
			}
			JsonRootBean<TransListResult> result = BocCreditParse.translist_parse(page);
			List<Response<TransListResult>> responseTransList = result.getResponse();
			for (Response<TransListResult> responseTrans : responseTransList) {
				TransListResult transListResult = responseTrans.getResult();
				List<BocchinaCebitCardCrcdTransList> transList = transListResult.getTransList();
				for (BocchinaCebitCardCrcdTransList bocchinaCebitCardCrcdTransList : transList) {
					bocchinaCebitCardCrcdTransList.setTaskid(bankJsonBean.getTaskid());
					bocchinaCebitCardCrcdTransList.setCardNo(cardno);
					bocchinaCebitCardCrcdTransListRepository.save(bocchinaCebitCardCrcdTransList);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			//return null;
			return new AsyncResult<String>("false");
		}
		
		//return null;
		return new AsyncResult<String>("sucess");
	}

	public static void main(String[] args) {
		LocalDate today = LocalDate.now();

		for (int i = 0; i < 4; i++) {
			LocalDate enddate = today.plusMonths(-(i * 6));
			LocalDate startdate = today.plusMonths(-((i + 1) * 6));

			String enddate_string = (enddate + "").replaceAll("-", "/");
			String startdate_string = (startdate + "").replaceAll("-", "/");
			System.out.println("====" + i + "========" + enddate.getYear() + "/" + enddate.getMonthValue());
			System.out.println("=========" + enddate_string);
			System.out.println("=========" + startdate_string);
		}
	}
}
