package org.common.microservice.eureka.china.unicom;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Set;
import java.util.UUID;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.util.StringUtils;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.crawler.unicom.htmlunit.LoginAndGet;

/**
 * @author zhenZ
 * @createtime 2017年6月8日 上午10:02:20 北京移动通话记录爬取（测试类）
 */
public class liantongtest {

	private static Set<Cookie> cookies;

	public static void main(String[] args) throws Exception {
		postlogin();
	}

	public static Object postlogin() throws Exception {

		String url = "https://uac.10010.com/portal/Service/MallLogin?callback=jQuery17207362528050565731_1500340724362&req_time=1500340731372&redirectURL=http%3A%2F%2Fwww.10010.com"
				+ "&userName=18618135874" + "&password=096572"
				+ "&pwdType=01&productType=01&redirectType=01&rememberMe=1&_=1500340731375";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); //

		Page javaScriptPage = getHtml2(url, webClient);
		System.out.println("=========================================");
		System.out.println(javaScriptPage.getWebResponse().getContentAsString());
		System.out.println("=========================================");

		if (javaScriptPage.getWebResponse().getContentAsString().indexOf("系统繁忙，请稍后再试") != -1) {
			System.out.println("=============系统繁忙，请稍后再试==================");
			System.out.println("==================登录失败=====================");
			return null;
		}
		if (javaScriptPage.getWebResponse().getContentAsString().indexOf("验证码错误") != -1) {
			System.out.println("=============验证码错误=================");
			System.out.println("==================登录失败=====================");
			return null;
		}

		if (javaScriptPage.getWebResponse().getContentAsString().indexOf("请输入正确的服务密码") != -1
				|| javaScriptPage.getWebResponse().getContentAsString().indexOf("用户名或密码不正确") != -1) {
			System.out.println("==================登录失败=====================");
			return null;
		}

		// url =
		// "http://iservice.10010.com/e3/static/common/mall_info?callback=jsonp1500458749489";

		// http://iservice.10010.com/e3/static/query/paymentRecord?_=1500286077314&accessURL=http://iservice.10010.com/e4/query/calls/paid_record-iframe.html?menuCode=000100010003&menuid=000100010003
		/*url = "http://iservice.10010.com/e3/static/common/mall_info?callback=jsonp1500862832608";

		Page page = getHtml3(url, webClient);

		Set<Cookie> cookieSet = webClient.getCookieManager().getCookies();

		String cookieString = CommonUnit.transcookieToJson(webClient);

		WebClient webClientw = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookieSet2 = CommonUnit.transferJsonToSet(cookieString);

		Iterator<Cookie> i = cookieSet2.iterator();
		while (i.hasNext()) {
			webClientw.getCookieManager().addCookie(i.next());
		}*/
		/*
		 * url =
		 * "http://iservice.10010.com/e3/static/check/checklogin?_=1500531203267";
		 * Page page3 = gethtmlPost(webClient, null, url);
		 * System.out.println(page3.toString());
		 */

		/*
		 * url =
		 * "http://iservice.10010.com/e3/static/query/checkmapExtraParam?_=1500531210262";
		 * List<NameValuePair> paramsList = new ArrayList<>();
		 * paramsList.add(new NameValuePair("menuId", "000100030002")); TextPage
		 * textPage = (TextPage) gethtmlPost(webClient, paramsList, url);
		 */

		/*
		 * url =
		 * "http://iservice.10010.com/e3/static/query/sms?_=1500528910947&accessURL=http://iservice.10010.com/e4/query/calls/call_sms-iframe.html&menuid=000100030001";
		 * 
		 * List<NameValuePair> paramsList = new ArrayList<>(); paramsList = new
		 * ArrayList<>(); paramsList.add(new NameValuePair("pageNo", "1"));
		 * paramsList.add(new NameValuePair("pageSize", "100"));
		 * paramsList.add(new NameValuePair("begindate", "20170701"));
		 * paramsList.add(new NameValuePair("enddate", "20170720")); TextPage
		 * textPage = (TextPage) gethtmlPost(webClientw, paramsList, url);
		 * System.out.println(textPage.getContent());
		 */

	/*	cookieSet = webClient.getCookieManager().getCookies();

		Map<String, String> cookiesmap = new HashMap<>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}*/
		/*LocalDate nowdate = LocalDate.now();

		String month = nowdate.plusMonths(-0).getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}
		url = "http://iservice.10010.com/e3/static/query/queryHistoryBill?_=1500004693172&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
		Connection con4 = Jsoup.connect(url).method(Method.POST).timeout(50000000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		con4.data("querytype", "0001");
		con4.data("querycode", "0001");
		con4.data("billdate", nowdate.plusMonths(-0).getYear() + "" + month);
		con4.data("flag", "2");
		con4.cookies(cookiesmap);

		Response res4 = con4.execute();

		System.out.println(res4.body());*/

		/*
		 * Set<Cookie> cookieSet = webClient.getCookieManager().getCookies();
		 * 
		 * Map<String, String> cookiesmap = new HashMap<>(); for (Cookie cookie
		 * : cookieSet) { cookiesmap.put(cookie.getName(), cookie.getValue()); }
		 */

		// 测试用通话详单

	//return null;
/*
	    url = "http://iservice.10010.com/e3/static/query/paymentRecord?_=1500286077314&accessURL=http://iservice.10010.com/e4/query/calls/paid_record-iframe.html?menuCode=000100010003&menuid=000100010003";
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(500000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
																																	// con.data("pageNo",																																	// con.data("pageSize",
																																	// "20");
		con.data("beginDate", "20170701");
		con.data("endDate", "20170727");
		con.cookies(cookiesmap);
		Response res = con.execute();
		System.out.println(res.body());
		System.out.println(cookiesmap);
		return null;*/
		
	   /* url = "http://iservice.10010.com/e3/static/query/queryHistoryBill?_=1500004693172&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
		Connection con4 = Jsoup.connect(url).method(Method.POST).timeout(50000000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		con4.data("querytype", "0001");
		con4.data("querycode", "0001");
		con4.data("billdate", "201705");
		con4.data("flag", "2");
		con4.cookies(cookiesmap);
		Response res = con4.execute();
		System.out.println(res.body());
		System.out.println(cookiesmap);

		*/
		return null;
	}

	/*
	 * public static void buttonlogin() throws Exception { WebClient webClient =
	 * WebCrawler.getInstance().getWebClient(); String url =
	 * "https://uac.10010.com/portal/homeLogin"; HtmlPage html = getHtml(url,
	 * webClient); HtmlTextInput username = (HtmlTextInput)
	 * html.getFirstByXPath("//input[@id='userName']"); HtmlPasswordInput
	 * password = (HtmlPasswordInput)
	 * html.getFirstByXPath("//input[@id='userPwd']"); HtmlElement button =
	 * (HtmlElement) html.getFirstByXPath("//input[@id='login1']");
	 * username.setText("18618135874"); password.setText("096571"); HtmlPage
	 * loggedPage = button.click(); System.out.println(loggedPage.asXml());
	 * Set<Cookie> cookieSet = webClient.getCookieManager().getCookies();
	 * Map<String, String> cookiesmap = new HashMap<>(); for (Cookie cookie :
	 * cookieSet) { cookiesmap.put(cookie.getName(), cookie.getValue()); }
	 * 
	 * url =
	 * "http://iservice.10010.com/e3/static/query/sms?_=1500289213208&accessURL=http://iservice.10010.com/e4/query/calls/call_sms-iframe.html&menuid=000100030002";
	 * Connection con =
	 * Jsoup.connect(url).method(Method.POST).timeout(500000).userAgent(
	 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"
	 * ); // 发送参数 con.data("pageNo", "1"); con.data("pageSize", "20");
	 * con.data("beginDate", "20170501"); con.data("endDate", "20170531");
	 * con.cookies(cookiesmap); Response res = con.execute();
	 * System.out.println(res.body()); System.out.println(cookiesmap);
	 * 
	 * 
	 * url =
	 * "http://iservice.10010.com/e3/static/query/paymentRecord?_=1500286077314&accessURL=http://iservice.10010.com/e4/query/calls/paid_record-iframe.html?menuCode=000100010003&menuid=000100010003";
	 * Connection con =
	 * Jsoup.connect(url).method(Method.POST).timeout(500000).userAgent(
	 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"
	 * ); // 发送参数 con.data("pageNo", "1"); con.data("pageSize", "20");
	 * con.data("beginDate", "20170601"); con.data("endDate", "20170630");
	 * con.cookies(cookiesmap); Response res = con.execute();
	 * System.out.println(res.body()); System.out.println(cookiesmap);
	 * 
	 * // System.out.println(cookieString);
	 * 
	 * 
	 * 
	 * url =
	 * "http://iservice.10010.com/e3/static/query/verificationSubmit?_=1496972568536"
	 * +
	 * "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
	 * Connection con2 =
	 * Jsoup.connect(url).method(Method.POST).timeout(500000).userAgent(
	 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31"
	 * ); con2.data("inputcode", valicodeStr); con2.data("menuId",
	 * "000100030001"); con2.cookies(cookiesmap); Response res2 =
	 * con2.execute(); System.out.println(res2.body()); url =
	 * "http://iservice.10010.com/e3/static/query/callDetail?_=1496973914842" +
	 * "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
	 * 
	 * Connection con3 =
	 * Jsoup.connect(url).method(Method.POST).timeout(500000).userAgent(
	 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31"
	 * ); con3.data("pageNo", "1"); con3.data("pageSize", "20");
	 * con3.data("beginDate", "20170501"); con3.data("endDate", "20170531");
	 * con3.cookies(cookiesmap); Response res3 = con3.execute();
	 * System.out.println("==================="+res3.body());
	 * 
	 * Connection con4 =
	 * Jsoup.connect(url).method(Method.POST).timeout(500000).userAgent(
	 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31"
	 * ); con4.data("pageNo", "1"); con4.data("pageSize", "20");
	 * con4.data("beginDate", "20170401"); con4.data("endDate", "20170430");
	 * con4.cookies(cookiesmap); Response res4 = con4.execute();
	 * 
	 * System.out.println("==================="+res4.body());
	 * 
	 * }
	 */
	
	public  Page getHtml5(String url, WebClient webClient) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Page getHtml3(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.setJavaScriptTimeout(5000000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static Page getHtml2(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		//webClient.getOptions().setJavaScriptEnabled(false);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			System.out.println(url);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}

			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private static WebClient addCookies(WebClient webClient, Set<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			if (StringUtils.isEmpty(cookie.getName())) {
				System.out.println("详情：" + cookie.getName() + ":" + cookie.getValue());
				continue;
			}
			webClient.getCookieManager().addCookie(cookie);
		}
		return webClient;
	}

}