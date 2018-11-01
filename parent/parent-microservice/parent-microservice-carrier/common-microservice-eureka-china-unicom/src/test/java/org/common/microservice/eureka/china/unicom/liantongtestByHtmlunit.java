package org.common.microservice.eureka.china.unicom;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.unicom.UnicomNoteResult;
import com.module.htmlunit.WebCrawler;

import app.bean.UnicomRoot;
import app.bean.UnicomUserResultRoot;
import app.bean2.SucessResultBean;

public class liantongtestByHtmlunit {


	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static WebClient getSmSCode(WebClient webClient) throws Exception {

		webClient.addRequestHeader("Host", "uac.10010.com");
		webClient.addRequestHeader("Pragma", "no-cache");

		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Referer", "https://uac.10010.com/portal/homeLogin");

		String url2 = "https://uac.10010.com/portal/Service/CheckNeedVerify?callback=jQuery172012727608617026065_"
				+ System.currentTimeMillis() + "&userName=" + "18600914623" + "&" + "pwdType=01&_="
				+ System.currentTimeMillis();
		Page page = getHtml2(url2, webClient);
		System.out.println("登录第一步url2 = " + url2 + ";" + page.getWebResponse().getContentAsString());

		String url_code = "https://uac.10010.com/portal/Service/SendCkMSG?callback=jQuery172003873314014258589_"
				+ System.currentTimeMillis() + "&req_time=" + System.currentTimeMillis() + "&mobile=" + "18600914623"
				+ "&_=" + System.currentTimeMillis();
		page = getHtml2(url_code, webClient);
		System.out.println(page.getWebResponse().getContentAsString());
		if (page.getWebResponse().getContentAsString().contains("resultCode:\"0000\"")) {

		} else {
			System.out.println("===================");
			getLoginResult(page.getWebResponse().getContentAsString());
			System.out.println("===================");
		}
		return webClient;
	}

	public static WebClient getSmSCodeSecond(WebClient webClient) throws Exception {

		String url = "http://iservice.10010.com/e3/static/query/sendRandomCode?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("menuId", "000100030001"));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "iservice.10010.com");
		webRequest.setAdditionalHeader("Origin", "http://iservice.10010.com");
		webRequest.setAdditionalHeader("Referer", "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page searchPage = webClient.getPage(webRequest);

		System.out.println("获取第二次短信验证码：：：：：：" + searchPage.getWebResponse().getContentAsString());
		return webClient;
	}

	public static WebClient setphonecodeSecond(WebClient webClient) throws Exception {

		String valicodeStr = JOptionPane.showInputDialog("请输入第二次验证码：");

		// Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		String url = "http://iservice.10010.com/e3/static/query/verificationSubmit?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("menuId", "000100030001"));
		paramsList.add(new NameValuePair("inputcode", valicodeStr.trim()));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "iservice.10010.com");
		webRequest.setAdditionalHeader("Origin", "http://iservice.10010.com");
		webRequest.setAdditionalHeader("Referer", "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page searchPage = webClient.getPage(webRequest);

		System.out.println("验证第二次短信验证码：：：：：：" + searchPage.getWebResponse().getContentAsString());

		getCallThem(webClient);

		return webClient;

	}

	public static WebClient setphonecode(WebClient webClient) throws Exception {

		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		String url = "https://uac.10010.com/portal/homeLoginNew";
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(true);

		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='userName']");

		username.setText("18600914623");

		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='userPwd']");

		passwordInput.click();

		passwordInput.setText("211318");

		HtmlPasswordInput smsInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='userCK']");

		smsInput.setText(valicodeStr.trim());

		System.out.println("username::::::::" + username);
		System.out.println("passwordInput::::::::" + passwordInput);

		System.out.println("smsInput::::::::" + smsInput);

		HtmlElement button = (HtmlElement) html.getFirstByXPath("//input[@id='login1']");

		HtmlPage htmlpage = button.click();

		Thread.sleep(2000);

		System.out.println(htmlpage.asXml());

		HtmlPage callpage = getHtml("http://iservice.10010.com/e4/query/bill/call_dan-iframe.html", webClient);
		
		Set<Cookie> cookies = callpage.getWebClient().getCookieManager().getCookies();

		for (Cookie cookie : cookies) {
			
			System.out.println("=======cookie======="+cookie.getName()+";;;;;"+cookie.getValue());
		}

		return callpage.getWebClient();
	}

	// 通话详单查询
	public static String getCallThem(WebClient webClient) throws Exception {

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {

			System.out.println("====" + cookie.getName() + ":" + cookie.getValue());
		}

		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://iservice.10010.com/e3/static/query/callDetail?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("pageNo", "1"));
		paramsList.add(new NameValuePair("pageSize", "20"));
		paramsList.add(new NameValuePair("beginDate", "20180901"));
		paramsList.add(new NameValuePair("endDate", "20180910"));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "iservice.10010.com");
		webRequest.setAdditionalHeader("Origin", "http://iservice.10010.com");
		webRequest.setAdditionalHeader("Referer", "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());

		return searchPage.getWebResponse().getContentAsString();

	}

	// 通话详单查询
	public static String getCallThem2(WebClient webClient) throws Exception {
		String cookiejson = CommonUnit.transcookieToJson(webClient);

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(cookiejson);
		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}

		String url = "http://iservice.10010.com/e3/static/query/callDetail?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
		Connection con4 = Jsoup.connect(url).method(Method.POST).timeout(50000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		con4.data("pageNo", "1");
		con4.data("pageSize", "20");
		con4.data("beginDate", "20180501");
		con4.data("endDate", "20180521");
		con4.cookies(cookiesmap);
		Response res4 = con4.execute();

		return res4.body();

	}

	public static String gethistory(WebClient webClient) throws Exception {

		String url = "http://iservice.10010.com/e3/static/query/queryHistoryBill?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuid=000100020001";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("querytype", "0001"));
		paramsList.add(new NameValuePair("querycode", "0001"));
		paramsList.add(new NameValuePair("billdate", "201808"));
		paramsList.add(new NameValuePair("flag", "1"));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "iservice.10010.com");
		webRequest.setAdditionalHeader("Origin", "http://iservice.10010.com");
		webRequest.setAdditionalHeader("Referer",
				"http://iservice.10010.com/e4/query/basic/history_list.html?menuId=000100020001");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page searchPage = webClient.getPage(webRequest);

		System.out.println("getCallThem:::::" + searchPage.getWebResponse().getContentAsString());
		return searchPage.getWebResponse().getContentAsString();

	}

	public static String getChinese(String paramValue) {
		String regex = "([\u4e00-\u9fa5]+)";
		String str = "";
		Matcher matcher = Pattern.compile(regex).matcher(paramValue);
		while (matcher.find()) {
			str += matcher.group(0);
		}
		return str;
	}

	public static void getLoginResult(String html) {
		String value = html.split("\\(\\{")[1];

		String[] values = value.split("\"\\,");

		SucessResultBean sucessResultBean = new SucessResultBean();
		for (int i = 0; i < values.length; i++) {
			if (values[i].split(":")[0].contains("resultCode")) {
				sucessResultBean.setResultCode(values[i].split(":")[1].replaceAll("\"", "").replaceAll("\\'", "") + "");
			} else if (values[i].split(":")[0].contains("msg")) {
				sucessResultBean.setMsg(getChinese(values[i].split(":")[1].replaceAll("\"", "").replaceAll("\\'", "")
						.replaceAll("。<a href=https", "")));
			}
		}

		System.out.println(sucessResultBean.toString());
	}

	public static Page getHtml2(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setCharset(Charset.forName("UTF-8"));

		Page searchPage = webClient.getPage(webRequest);
		Set<Cookie> cookieSet = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookieSet) {

			System.out.println("==" + cookie.toString());
		}

		return searchPage;

	}

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		webClient = getSmSCode(webClient);
		webClient = setphonecode(webClient);
		
		webClient = getSmSCodeSecond(webClient);
		
		 setphonecodeSecond(webClient);
	}
}
