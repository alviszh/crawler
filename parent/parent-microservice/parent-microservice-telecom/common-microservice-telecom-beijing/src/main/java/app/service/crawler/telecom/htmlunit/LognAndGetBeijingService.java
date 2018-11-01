package app.service.crawler.telecom.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.service.common.LoginAndGetCommon;

@Component
public class LognAndGetBeijingService {
	
	@Autowired
	private LognAndGetBeijingUnitService lognAndGetBeijingUnitService;
	
	@Autowired
	private TracerLog tracerLog;

	// 获取北京用户基本信息
	public  String getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		webClient = lognAndGetBeijingUnitService.ready(webClient, 0);

		webClient = lognAndGetBeijingUnitService.readyForCallThrem(webClient, 0, messageLogin);

		String url = "http://bj.189.cn/iframe/custservice/modifyUserInfo.action?indexPage=INDEX3";

		Page page = getHtmlForCookie(url, webClient);

		return page.getWebResponse().getContentAsString();
	}

	// 获取北京用户往月话费账单
	public  String getphoneBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {
	
		String url = "http://bj.189.cn/iframe/feequery/queryBillInfoAll.action?"
				+ "accNum="+messageLogin.getName()
				+"&billCycle="
				+"&requestFlag=synchronization";
		try {

			Page page = getHtmlForCookie(url, webClient);
			return page.getWebResponse().getContentAsString();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}

	// 获取北京用户缴费信息
	public  String getpayResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile, String stardate,
			String enddate) {


		String url = "http://bj.189.cn/iframe/local/queryPaymentRecord.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNum", messageLogin.getName().trim()));
		paramsList.add(new NameValuePair("requestFlag", "synchronization"));
		paramsList.add(new NameValuePair("paymentHistoryQueryIn.startDate", stardate));
		paramsList.add(new NameValuePair("paymentHistoryQueryIn.endDate", enddate));
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			return page.getWebResponse().getContentAsString();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	// 获取北京用户通话详单 带分页信息
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public  String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate, String enddate, String month, String pagnum) {

		System.out.println(stardate + ":" + enddate + ":" + month + ":" + pagnum);

		String url = "http://bj.189.cn/iframe/feequery/billDetailQuery.action?requestFlag=synchronization"
				+ "&billDetailType=1" + "&qryMonth=" + month.trim() + "&startTime=" + stardate.trim() + "&accNum="
				+ messageLogin.getName().trim() + "&endTime=" + enddate.trim() + "&billPage=" + pagnum.trim();

		tracerLog.output("请求的url::::::"+month, url);

		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtmlForCookie(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			if (html.indexOf("系统正忙，请稍后再试") != -1) {
//				if (i == 0) {
//					i++;
//					html = getCallThrem(webClient, messageLogin, taskMobile, stardate, enddate, month, pagnum, i);
//				}
				throw new ErrorException("系统正忙，请稍后再试");

			}
			return html;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}

	// 获取北京用户短信详单 带分页信息
	public  String getSMSThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate, String enddate, String month, String pagnum) {

		System.out.println(stardate + ":" + enddate + ":" + month + ":" + pagnum);

		String url = "http://bj.189.cn/iframe/feequery/billDetailQuery.action?requestFlag=synchronization"
				+ "&billDetailType=2" + "&qryMonth=" + month + "&startTime=" + stardate.trim() + "&accNum="
				+ messageLogin.getName().trim() + "&endTime=" + enddate.trim() + "&billPage=" + pagnum.trim();

		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtmlForCookie(url, webClient);

			String html = page.getWebResponse().getContentAsString();

			return html;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}

	// 获取北京用户积分信息
	public  String getintegraResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

		String url = "http://bj.189.cn/iframe/custquery/qryPointHistory.action?indexPage=INDEX3&fastcode=01410646&cityCode=bj";
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtmlForCookie(url, webClient);
			return (page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	// 获取北京用户话费余额
	public  String getChargesResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

	
		String url = "http://bj.189.cn/iframe/feequery/qryRealtimeFee.action?requestFlag=synchronization&p1QueryFlag=2&accNum="
				+ messageLogin.getName() + "&time=" + System.currentTimeMillis() / 1000;
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtmlForCookie(url, webClient);
			return (page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public  Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {

		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public  HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			// webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public  Page getHtmlForCookie(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "bj.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public  WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}

	public  WebClient ready(WebClient webClient) {
		String url = "http://bj.189.cn/iframe/feequery/billQueryIndex.action?fastcode=01390637";

		try {
			getHtml(url, webClient);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return webClient;
	}

}
