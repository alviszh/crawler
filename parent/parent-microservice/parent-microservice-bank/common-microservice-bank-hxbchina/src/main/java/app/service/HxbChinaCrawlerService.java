package app.service;

import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaDebitCardHtml;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.hxbchina.HxbChinaDebitCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.hxbchina.HxbChinaDebitCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.hxbchina.HxbChinaDebitCardUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HxbChinaParser;
import app.service.common.HxbChinaHelperService;

/**
 * @description:
 * 
 * @author: sln
 * @date: 2017年11月06日
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.hxbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.hxbchina" })
public class HxbChinaCrawlerService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HxbChinaParser hxbChinaParser;
	@Autowired
	private HxbChinaDebitCardHtmlRepository hxbChinaDebitCardHtmlRepository;
	@Autowired
	private HxbChinaDebitCardUserInfoRepository hxbChinaDebitCardUserInfoRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private HxbChinaDebitCardTransFlowRepository hxbChinaDebitCardTransFlowRepository;
	@Autowired
	private HxbChinaHelperService hxbChinaHelperService;

	/**
	 * 爬取用户信息
	 * 
	 * @param bankJsonBean
	 * @param taskBank
	 * @param webClient
	 * @throws Exception
	 */
	public void getUserInfo(BankJsonBean bankJsonBean, TaskBank taskBank) throws Exception {
		WebClient webClient = hxbChinaHelperService.addcookie(taskBank);
		webClient.getOptions().setJavaScriptEnabled(false);
		// 用如下链接获取参数
		String url = "https://sbank.hxb.com.cn/easybanking/PAccountInfoDetailsQuery/PAccountInfoDetailsQueryAction.do?actionType=entry&OLN_top_activeNode=top11&OLN_top11side10_activeNode=top10side30&sideMenuBar=OLN_top11side10";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Host", "sbank.hxb.com.cn");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Referer",
				"https://sbank.hxb.com.cn/easybanking/PAccountMyAccount/PAccountMyAccountAction.do?actionType=entry&OLN_top_activeNode=top11&OLN_top11side10_activeNode=top10side10&sideMenuBar=OLN_top11side10");
		HtmlPage page = webClient.getPage(webRequest);
		if (null != page) {
			String html = page.asXml();
			Document doc = Jsoup.parse(html);
			String randomTokenVerifyTag = doc.getElementById("randomTokenVerifyTag").val();
			String verifyCodeBackUriTag = doc.getElementById("verifyCodeBackUriTag").val();
			String accountDetailQueryDisplayVBListRowSelection = doc
					.getElementById("accountDetailQueryDisplayVBList_row").getElementsByTag("tbody").get(0)
					.getElementsByClass("odd").get(0).getElementsByTag("td").get(0).getElementsByTag("input")
					.attr("value");
			webClient = page.getWebClient();
			// 2018年6月28日爬取不到数据了， 经过调研官网，发现爬取用户信息的连接变，如下注释的为之前的
			// url="https://sbank.hxb.com.cn/easybanking/PAccountInfoDetailsQuery/SFormPaconfirmationAction.do?actionType=confirm";
			url = "https://sbank.hxb.com.cn/easybanking/PAccountInfoDetailsQuery/PAccountInfoDetailsQueryAction.do?actionType=confirm&selected=0";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody = "randomTokenVerifyTag=" + randomTokenVerifyTag + "&verifyCodeBackUriTag="
					+ verifyCodeBackUriTag + "&accountDetailQueryDisplayVBListRowSelection="
					+ accountDetailQueryDisplayVBListRowSelection + "";
			webRequest.setRequestBody(requestBody);
			webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Host", "sbank.hxb.com.cn");
			webRequest.setAdditionalHeader("Connection", "Keep-Alive");
			webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			webRequest.setAdditionalHeader("Referer",
					"https://sbank.hxb.com.cn/easybanking/PAccountInfoDetailsQuery/PAccountInfoDetailsQueryAction.do?actionType=entry&OLN_top_activeNode=top11&OLN_top11side10_activeNode=top10side30&sideMenuBar=OLN_top11side10");
			page = webClient.getPage(webRequest);
			if (null != page) {
				html = page.getWebResponse().getContentAsString();
				HxbChinaDebitCardHtml hxbChinaDebitCardHtml = new HxbChinaDebitCardHtml();
				hxbChinaDebitCardHtml.setHtml(html);
				hxbChinaDebitCardHtml.setPagenumber(1);
				hxbChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
				hxbChinaDebitCardHtml.setType("用户基本信息源码页");
				hxbChinaDebitCardHtml.setUrl(url);
				hxbChinaDebitCardHtmlRepository.save(hxbChinaDebitCardHtml);
				tracer.addTag("action.crawler.getUserInfo.html", "数据采集中，用户信息源码页已经入库");
				if (html.contains("客户姓名")) {
					HxbChinaDebitCardUserInfo hxbChinaDebitCardUserInfo = hxbChinaParser.userParser(html, taskBank);
					if (null != hxbChinaDebitCardUserInfo) {
						hxbChinaDebitCardUserInfoRepository.save(hxbChinaDebitCardUserInfo);
						taskBankStatusService.updateTaskBankUserinfo(
								BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
								BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
						// 爬取流水明细信息
						try {
							getTransflow(taskBank, hxbChinaDebitCardUserInfo);
						} catch (Exception e) {
							tracer.addTag("action.crawler.bank.getTransflow.exception", e.getMessage());
							taskBankStatusService.updateTaskBankTransflow(
									BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(),
									BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
							taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
						}
					} else {
						taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR.getError_code(),
								BankStatusCode.BANK_USERINFO_ERROR.getDescription(), taskBank.getTaskid());
					}
				} else { // 系统繁忙，请稍候再试(页面中有时候会包含，所以采集不到数据)
					taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR.getError_code(),
							BankStatusCode.BANK_USERINFO_ERROR.getDescription(), taskBank.getTaskid());
				}
			}
		}
	}

	public void getTransflow(TaskBank taskBank, HxbChinaDebitCardUserInfo hxbChinaDebitCardUserInfo) throws Exception {
		// 流水信息所属卡号
		String cardno = hxbChinaDebitCardUserInfo.getBelongCard().trim();
		WebClient webClient = hxbChinaHelperService.addcookie(taskBank);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "https://sbank.hxb.com.cn/easybanking/PAccountQueryAccountDetails/PAccountQueryAccountDetailsAction.do?actionType=entry&OLN_top_activeNode=top11&OLN_top11side10_activeNode=top10side40&sideMenuBar=OLN_top11side10";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		if (null != page) {
			String html = page.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			String randomTokenVerifyTag = doc.getElementById("randomTokenVerifyTag").val();
			String verifyCodeBackUriTag = doc.getElementById("verifyCodeBackUriTag").val();
			webClient = page.getWebClient();
			url = "https://sbank.hxb.com.cn/easybanking/PAccountQueryAccountDetails/FormParedirectAction.do?actionType=redirect";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Referer",
					"https://sbank.hxb.com.cn/easybanking/PAccountQueryAccountDetails/PAccountQueryAccountDetailsAction.do?actionType=entry&OLN_top_activeNode=top11&OLN_top11side10_activeNode=top10side40&sideMenuBar=OLN_top11side10");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequest.setAdditionalHeader("Host", "sbank.hxb.com.cn");
			webRequest.setAdditionalHeader("Connection", "Keep-Alive");
			webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			String presentDate = HxbChinaHelperService.getPresentDate();
			String presentYear = presentDate.substring(0, 4);
			String presentMonth = presentDate.substring(4, 6);
			String presentDay = presentDate.substring(6, 8);
			if (presentDay.startsWith("0")) {
				presentDay = presentDay.substring(1, 2);
			}
			String monthAgoDate = null;
			String monthLaterDate = null;

			// 定义一个变量来判断最终获取数据情况，哪怕有一条数据，爬取状态也是200
			int successFlag = 0;

			for (int i = 1; i <= 4; i++) { // 只能获取近两年的数据，且查询跨度最大6个月
				monthAgoDate = HxbChinaHelperService.getBeforeMonth(6 * i); // 获取前6个月
				monthLaterDate = HxbChinaHelperService.getBeforeMonth(6 * (i - 1));
				String startYear = monthAgoDate.substring(0, 4);
				String startMonth = monthAgoDate.substring(4, 6);
				String startDay = monthAgoDate.substring(6, 8);

				String endYear = monthLaterDate.substring(0, 4);
				String endMonth = monthLaterDate.substring(4, 6);
				String endDay = monthLaterDate.substring(6, 8);
				// queryMode=1 代表查的是历史流水信息账单
				String requestBody = "randomTokenVerifyTag=" + randomTokenVerifyTag + "" + "&verifyCodeBackUriTag="
						+ verifyCodeBackUriTag + "" + "&queryDateFlag=&autoSdate=&autoEdate="
						+ "&cardNo=1%7C0&account=1" + "&busiType=0&sortFlag=0&beneAcctName=&minAmt=&maxAmt="
						+ "&queryStrDateYear=" + startYear + "&queryStrDateMonth=" + startMonth + "&queryStrDateDay="
						+ startDay + "" + "&db_current_time_year=" + presentYear + "&db_current_time_month="
						+ presentMonth + "&db_current_time_day=" + presentDay + "" // 第几天
						+ "&queryEndDateYear=" + endYear + "&queryEndDateMonth=" + endMonth + "&queryEndDateDay="
						+ endDay + "" + "&db_current_time_year=" + presentYear + "&db_current_time_month="
						+ presentMonth + "&db_current_time_day=" + presentDay + "" // 第几天
						+ "&queryMode=1&billYearMonYear=" + presentYear + "&billYearMonMonth=" + presentMonth + "";
				webRequest.setRequestBody(requestBody);
				Page hPage = webClient.getPage(webRequest);
				if (null != hPage) {
					html = hPage.getWebResponse().getContentAsString();
					doc = Jsoup.parse(html);
					if (html.contains("对方开户行")) { // 要是有数据的话，会显示表头，此为表头中的一个字段，用此判断查询区间是否有数据可供采集
						successFlag++;
						HxbChinaDebitCardHtml hxbChinaDebitCardHtml = new HxbChinaDebitCardHtml();
						hxbChinaDebitCardHtml.setHtml(html);
						hxbChinaDebitCardHtml.setPagenumber(1);
						hxbChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
						hxbChinaDebitCardHtml.setType(monthAgoDate + "至" + monthLaterDate + "流水明细信息源码页");
						hxbChinaDebitCardHtml.setUrl(url);
						hxbChinaDebitCardHtmlRepository.save(hxbChinaDebitCardHtml);
						tracer.addTag("action.crawler.getTransflow.html" + i,
								monthAgoDate + "至" + monthLaterDate + "流水明细信息源码页已入库");
						List<HxbChinaDebitCardTransFlow> list = hxbChinaParser.transflowParser(taskBank, doc,
								monthAgoDate, monthLaterDate, cardno);
						if (null != list && list.size() > 0) {
							hxbChinaDebitCardTransFlowRepository.saveAll(list);
							taskBankStatusService.updateTaskBankTransflow(200,
									"数据采集中，" + monthAgoDate + "至" + monthLaterDate + "流水明细信息已采集完成",
									taskBank.getTaskid());
						}
					} else {
						// 即便真的没有数据，也存储html页面
						HxbChinaDebitCardHtml hxbChinaDebitCardHtml = new HxbChinaDebitCardHtml();
						hxbChinaDebitCardHtml.setHtml(html);
						hxbChinaDebitCardHtml.setPagenumber(1);
						hxbChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
						hxbChinaDebitCardHtml.setType(monthAgoDate + "至" + monthLaterDate + "流水明细信息源码页");
						hxbChinaDebitCardHtml.setUrl(url);
						hxbChinaDebitCardHtmlRepository.save(hxbChinaDebitCardHtml);
						tracer.addTag("action.crawler.getTransflow.html" + i,
								monthAgoDate + "至" + monthLaterDate + "流水明细信息源码页已入库");
						taskBankStatusService.updateTaskBankTransflow(201,
								"数据采集中，" + monthAgoDate + "至" + monthLaterDate + "流水明细信息无数据可供采集", taskBank.getTaskid());
					}
				}
			}
			// 判断最终结果，更新task表
			if (successFlag > 0) {
				tracer.addTag("action.crawler.getTransflow.result", "流水明细信息全部采集完成");
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), taskBank.getTaskid());
				// 爬取完毕，爬取状态进行更新
				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			} else {
				tracer.addTag("action.crawler.getTransflow.result", "流水明细信息无数据可供采集");
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
				// 爬取完毕，爬取状态进行更新
				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			}
		}
	}
}
