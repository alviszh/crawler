package org.common.microservice.eureka.china.unicom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.util.StringUtils;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamUnicom;
import app.bean2.SucessResultBean;

public class liantongtest {

	private static Set<Cookie> cookies;
	private static Workbook wb;
	private static Sheet sheet;
	private static Row row;

	protected static Gson gs = new Gson();

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		InputStream is = new FileInputStream("C:/Users/Administrator/Desktop/tel.xls");
		wb = new HSSFWorkbook(is);
		System.out.println("list中的数据打印出来");
		sheet = wb.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("rowNum" + rowNum);
		System.out.println("colNum" + colNum);

		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum; i++) {
			row = sheet.getRow(i);

			System.out.println(i + ":" + "" + row.getCell(0) + ":::::::" + row.getCell(1));
			webClient.getCookieManager()
					.addCookie(new Cookie("iservice.10010.com", row.getCell(0) + "", row.getCell(1) + ""));
		}
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);

//		gethistory(webClient);
		 getCallThem(webClient);
//		getSmSCodeSecond(webClient);
//		setphonecodeSecond(webClient);
//		getCallThem(webClient);
	}

	public static WebClient getSmSCodeSecond(WebClient webClient) throws Exception {

		// String url =
		// "http://iservice.10010.com/e3/static/check/checklogin?_=" +
		// System.currentTimeMillis();
		// WebRequest webRequest = new WebRequest(new URL(url),
		// HttpMethod.POST);
		// // webRequest.setCharset(Charset.forName("UTF-8"));
		// Page searchPage = webClient.getPage(webRequest);

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		for (Cookie cookie : cookies) {
			System.out.println("二次短信验证：：：" + cookie.getName() + cookie.getValue());
		}

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
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

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

	public static WebClient getSmSCode(WebClient webClient) throws Exception {

		webClient.addRequestHeader("Host", "uac.10010.com");
		webClient.addRequestHeader("Pragma", "no-cache");

		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Referer", "https://uac.10010.com/portal/homeLogin");

		String url2 = "https://uac.10010.com/portal/Service/CheckNeedVerify?callback=jQuery172012727608617026065_"
				+ System.currentTimeMillis() + "&userName=" + "17600325986" + "&" + "pwdType=01&_="
				+ System.currentTimeMillis();
		Page page = getHtml2(url2, webClient);
		System.out.println("登录第一步url2 = " + url2 + ";" + page.getWebResponse().getContentAsString());

		String url_code = "https://uac.10010.com/portal/Service/SendCkMSG?callback=jQuery172003873314014258589_"
				+ System.currentTimeMillis() + "&req_time=" + System.currentTimeMillis() + "&mobile=" + "17600325986"
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

	public static Object setphonecode(WebClient webClient) throws Exception {
		webClient.addRequestHeader("Host", "uac.10010.com");
		webClient.addRequestHeader("Pragma", "no-cache");

		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Referer", "https://uac.10010.com/portal/homeLogin");

		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		String url = "https://uac.10010.com/portal/Service/MallLogin?" + "callback=jQuery172003873314014258589_"
				+ System.currentTimeMillis() + "&req_time=" + System.currentTimeMillis()
				+ "&redirectURL=http%3A%2F%2Fwww.10010.com" + "&userName=" + "17600325986".trim() + "&password="
				+ "921003" + "&pwdType=01&productType=01" + "&redirectType=01&rememberMe=1" + "&verifyCKCode="
				+ valicodeStr.trim() + "&_=" + System.currentTimeMillis();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setCharset(Charset.forName("UTF-8"));

		webRequest.setAdditionalHeader("Accept",
				"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "uac.10010.com");
		// webRequest.setAdditionalHeader("Origin",
		// "http://iservice.10010.com");
		webRequest.setAdditionalHeader("Referer", "https://uac.10010.com/portal/homeLoginNew");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		Page page = webClient.getPage(webRequest);

		webClient = page.getEnclosingWindow().getWebClient();

		System.out.println("短信验证码验证结果" + page.getWebResponse().getContentAsString());
		System.out.println("====================短信验证码验证url=========" + url);
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName() + "::::::" + cookie.getValue());
		}

		if (page.getWebResponse().getContentAsString().contains("resultCode:\"0000\"")) {

		} else {
			getLoginResult(page.getWebResponse().getContentAsString());
			return null;
		}

		// webClient.getOptions().setJavaScriptEnabled(true);
		//
		// url = "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html";
		// webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "iservice.10010.com");
		//
		// webRequest.setAdditionalHeader("Referer",
		// "http://www.10010.com/net5/011/");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36
		// (KHTML,like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		// Page searchPage = webClient.getPage(webRequest);
		//
		// System.out.println("=========call_dan-iframe========" +
		// searchPage.getWebResponse().getContentAsString());
		//
		// url = "http://www.10010.com/net5/";
		//
		// webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "www.10010.com");
		// // webRequest.setAdditionalHeader("Origin",
		// // "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("Referer",
		// "http://www.10010.com/net5/front/index_computer.html");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		//
		// Page searchPage = webClient.getPage(webRequest);
		//
		// System.out.println("====net5/011=====" +
		// searchPage.getWebResponse().getContentAsString());

		// url = "http://uac.10010.com/portal/hallLogin";
		// webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "uac.10010.com");
		// // webRequest.setAdditionalHeader("Origin",
		// // "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("Referer",
		// "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		// searchPage = webClient.getPage(webRequest);
		//
		// System.out.println("=========halllogin===1=====" +
		// searchPage.getWebResponse().getContentAsString());
		//
		// url = "http://www.10010.com/net5/011/";
		// webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "www.10010.com");
		// // webRequest.setAdditionalHeader("Origin",
		// // "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("Referer",
		// "http://www.10010.com/net5/front/index_computer.html");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		//
		// searchPage = webClient.getPage(webRequest);
		//
		//
		// System.out.println("====net5/011==
		// 2==="+searchPage.getWebResponse().getContentAsString());
		//
		// url =
		// "http://www.10010.com/mall/service/check/checklogin/?_=1526968676730";
		//
		// webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "uac.10010.com");
		// // webRequest.setAdditionalHeader("Origin",
		// // "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("Referer",
		// "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		//
		// Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		// for (Cookie cookie : cookies) {
		//
		// if(cookie.getName().indexOf("JUT")!=-1){
		// List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		// paramsList = new ArrayList<NameValuePair>();
		// paramsList.add(new NameValuePair("jutThird",
		// cookie.getValue().trim()));
		//
		// System.out.println("=jutThird=="+ cookie.getValue().trim());
		// webRequest.setRequestParameters(paramsList);
		// }
		// }
		//
		//
		// searchPage = webClient.getPage(webRequest);
		//
		// System.out.println("======================"+searchPage.getWebResponse().getContentAsString());

		// url = "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html";
		// webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "iservice.10010.com");
		//
		// webRequest.setAdditionalHeader("Referer",
		// "http://www.10010.com/net5/011/");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		// searchPage = webClient.getPage(webRequest);
		//
		// System.out.println("=========call_dan-iframe========" +
		// searchPage.getWebResponse().getContentAsString());
		//
		//
		// url = "http://uac.10010.com/portal/hallLogin";
		// webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		//
		// webRequest.setAdditionalHeader("Host", "uac.10010.com");
		// // webRequest.setAdditionalHeader("Origin",
		// // "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("Referer",
		// "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML,
		// like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		// searchPage = webClient.getPage(webRequest);
		//
		// System.out.println("=========halllogin========" +
		// searchPage.getWebResponse().getContentAsString());
		//
		// System.out.println(getCallThem(searchPage.getEnclosingWindow().getWebClient()));
		return null;

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

		System.out.println("jsoup :::::" + res4.body());
		return res4.body();

	}

	// 通话详单查询
	public static String getCallThem(WebClient webClient) throws Exception {

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {

			System.out.println("====" + cookie.getName() + ":" + cookie.getValue());
		}

		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://iservice.10010.com/e3/static/query/callDetail?_=1526964289307&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("pageNo", "1"));
		paramsList.add(new NameValuePair("pageSize", "20"));
		paramsList.add(new NameValuePair("beginDate", "20180901"));
		paramsList.add(new NameValuePair("endDate", "20180908"));

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

	public Page getHtml5(String url, WebClient webClient) {
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
		webRequest.setCharset(Charset.forName("UTF-8"));

		Page searchPage = webClient.getPage(webRequest);
		Set<Cookie> cookieSet = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookieSet) {

			System.out.println("==" + cookie.toString());
		}

		return searchPage;

	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webClient.getOptions().setJavaScriptEnabled(false);

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

	public static Page getHtml3(String url, WebClient webClient, int i) throws Exception, IOException {

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

}