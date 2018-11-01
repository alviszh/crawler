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
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaCreditCardHtml;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaCreditCardTransFlow;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaCreditCardUserInfo;
import com.microservice.dao.repository.crawler.bank.hxbchina.HxbChinaCreditCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.hxbchina.HxbChinaCreditCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.hxbchina.HxbChinaCreditCardUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HxbChinaParser;
import app.service.common.HxbChinaHelperService;

/**
 * @description:
 * @author: sln
 * @date: 2017年11月17日 上午10:14:21
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.hxbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.hxbchina" })
public class HxbChinaCreditCrawlerService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HxbChinaParser hxbChinaParser;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private HxbChinaCreditCardUserInfoRepository hxbChinaCreditCardUserInfoRepository;
	@Autowired
	private HxbChinaCreditCardTransFlowRepository hxbChinaCreditCardTransFlowRepository;
	@Autowired
	private HxbChinaCreditCardHtmlRepository hxbChinaCreditCardHtmlRepository;
	@Autowired
	private HxbChinaHelperService hxbChinaHelperService;

	public void getUserInfo(BankJsonBean bankJsonBean, TaskBank taskBank) throws Exception {
		WebClient webClient = hxbChinaHelperService.addcookie(taskBank);
		// 用如下链接获取参数
		String url = "https://sbank.hxb.com.cn/easybanking/PAccountMyAccount/ListPageGetCreditBalanceAction.do?actionType=link3";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Host", "sbank.hxb.com.cn");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Referer",
				"https://sbank.hxb.com.cn/easybanking/jsp/paccountmyaccount/paccountmyaccount-credit-iframe-page.jsp");
		webRequest.setAdditionalHeader("If-Modified-Since", "0");
		HtmlPage page = webClient.getPage(webRequest);
		if (null != page) {
			String html = page.asXml();
			Document doc = Jsoup.parse(html);
			// 获取请求链接的一部分
			String userUrl = doc.getElementsByClass("simpleb").get(0).getElementsByTag("tr").get(0)
					.getElementsByTag("td").get(2).getElementsByTag("a").attr("href");
			// 处理返回的链接
			userUrl = userUrl.substring(userUrl.indexOf("&"), userUrl.length());
			// 拼接完整请求链接
			url = "https://sbank.hxb.com.cn/easybanking/PAccountInfoDetailsQuery/FormPaconfirmationAction.do?actionType=confirm&OLN_top_activeNode=top11&OLN_top11side10_activeNode=top10side30&sideMenuBar=OLN_top11side10"
					+ userUrl;
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
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
					"https://sbank.hxb.com.cn/easybanking/jsp/paccountmyaccount/paccountmyaccount-credit-iframe-page.jsp");
			page = webClient.getPage(webRequest);
			if (null != page) {
				html = page.getWebResponse().getContentAsString();
				HxbChinaCreditCardHtml hxbChinaCreditCardHtml = new HxbChinaCreditCardHtml();
				hxbChinaCreditCardHtml.setHtml(html);
				hxbChinaCreditCardHtml.setPagenumber(1);
				hxbChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
				hxbChinaCreditCardHtml.setType("用户基本信息源码页");
				hxbChinaCreditCardHtml.setUrl(url);
				hxbChinaCreditCardHtmlRepository.save(hxbChinaCreditCardHtml);
				tracer.addTag("action.crawler.getUserInfo.html", "数据采集中，用户信息源码页已经入库");

				HxbChinaCreditCardUserInfo hxbChinaCreditCardUserInfo = hxbChinaParser.userCreditParser(html, taskBank);
				if (null != hxbChinaCreditCardUserInfo) {
					hxbChinaCreditCardUserInfoRepository.save(hxbChinaCreditCardUserInfo);
					taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
							BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
					// 爬取流水明细信息
					try {
						getTransflow(taskBank);
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
			}
		}

	}

	// 爬取账单信息
	private void getTransflow(TaskBank taskBank) throws Exception {
		WebClient webClient = hxbChinaHelperService.addcookie(taskBank);
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
			// 定义一个变量来判断最终获取数据情况，哪怕有一条数据，爬取状态也是200
			int successFlag = 0;
			String monthAgoDate = null;
			String qryYear = "";
			String qryMonth = "";
			for (int i = 0; i < 24; i++) {
				monthAgoDate = HxbChinaHelperService.getBeforeMonth(i);
				qryYear = monthAgoDate.substring(0, 4);
				qryMonth = monthAgoDate.substring(4, 6);
				// queryMode=1 代表查的是历史账单信息账单(已出账单)
				String requestBody = "randomTokenVerifyTag=" + randomTokenVerifyTag + "" + "&verifyCodeBackUriTag="
						+ verifyCodeBackUriTag + "" + "&queryDateFlag=&autoSdate=&autoEdate="
						+ "&cardNo=1%7C0&account=1" + "&busiType=0&sortFlag=0&beneAcctName=&minAmt=&maxAmt="
						+ "&queryStrDateYear=" + presentYear + "&queryStrDateMonth=" + presentMonth
						+ "&queryStrDateDay=01" + "&db_current_time_year=" + presentYear + "&db_current_time_month="
						+ presentMonth + "&db_current_time_day=" + presentDay + "" // 第几天
						+ "&queryEndDateYear=" + presentYear + "&queryEndDateMonth=" + presentMonth
						+ "&queryEndDateDay=" + presentDay + "" + "&db_current_time_year=" + presentYear
						+ "&db_current_time_month=" + presentMonth + "&db_current_time_day=" + presentDay + "" // 第几天
						+ "&queryMode=1&billYearMonYear=" + qryYear + "&billYearMonMonth=" + qryMonth + ""; // 只有此处是要查询的账单所属的年份和月份
				webRequest.setRequestBody(requestBody);
				Page hPage = webClient.getPage(webRequest);
				if (null != hPage) {
					html = hPage.getWebResponse().getContentAsString();
					doc = Jsoup.parse(html);
					if (html.contains("交易日期")) { // 要是有数据的话，会显示表头，此为表头中的一个字段，用此判断查询区间是否有数据可供采集
						successFlag++;
						HxbChinaCreditCardHtml hxbChinaCreditCardHtml = new HxbChinaCreditCardHtml();
						hxbChinaCreditCardHtml.setHtml(html);
						hxbChinaCreditCardHtml.setPagenumber(1);
						hxbChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
						hxbChinaCreditCardHtml.setType(monthAgoDate + "账单明细信息源码页");
						hxbChinaCreditCardHtml.setUrl(url);
						hxbChinaCreditCardHtmlRepository.save(hxbChinaCreditCardHtml);
						tracer.addTag("action.crawler.getTransflow.html" + i, monthAgoDate + "账单明细信息源码页已入库");
						List<HxbChinaCreditCardTransFlow> list = hxbChinaParser.transflowCreditParser(taskBank, doc,
								monthAgoDate);
						if (null != list && list.size() > 0) {
							hxbChinaCreditCardTransFlowRepository.saveAll(list);
							taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，" + monthAgoDate + "账单明细信息已采集完成",
									taskBank.getTaskid());
						}
					} else {
						// 即便真的没有数据，也存储html页面
						HxbChinaCreditCardHtml hxbChinaCreditCardHtml = new HxbChinaCreditCardHtml();
						hxbChinaCreditCardHtml.setHtml(html);
						hxbChinaCreditCardHtml.setPagenumber(1);
						hxbChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
						hxbChinaCreditCardHtml.setType(monthAgoDate + "账单明细信息源码页");
						hxbChinaCreditCardHtml.setUrl(url);
						hxbChinaCreditCardHtmlRepository.save(hxbChinaCreditCardHtml);
						tracer.addTag("action.crawler.getTransflow.html" + i, monthAgoDate + "账单明细信息源码页已入库");
						taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，" + monthAgoDate + "账单明细信息无数据可供采集",
								taskBank.getTaskid());
					}
				}
			}
			// 判断最终结果，更新task表
			if (successFlag > 0) {
				tracer.addTag("action.crawler.getTransflow.result", "账单明细信息全部采集完成");
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), taskBank.getTaskid());
				// 爬取完毕，爬取状态进行更新
				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			} else {
				tracer.addTag("action.crawler.getTransflow.result", "账单明细信息无数据可供采集");
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
				// 爬取完毕，爬取状态进行更新
				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			}
		}
	}
}
