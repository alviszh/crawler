package com.test;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import app.bean.TelecomHaiNanUserIdBean;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseHaiNan;
import app.unit.TeleComCommonUnit;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：jdlogin 类描述： 创建人：hyx
 * 创建时间：2017年12月8日 下午6:04:36
 * 
 * @version
 */
public class TelecomloginHaiNan {

	// static String driverPath = "d:\\ChromeServer\\chromedriver.exe";

	static String driverPath = "c:\\chromedriver.exe";

	static Boolean headless = false;

	protected static Gson gs = new Gson();

	private static WebClient webClient;

	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static String loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://login.189.cn/web/login";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("txtAccount"));
			}
		});

		loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("txtAccount"));
			}
		});
		// loginname.sendKeys("18995154123");
		loginname.sendKeys("17763819087");
		driver.findElement(By.id("txtShowPwd")).click();
		// driver.findElement(By.id("txtPassword")).sendKeys("795372");
		driver.findElement(By.id("txtPassword")).sendKeys("211314");
		String code = JOptionPane.showInputDialog("请输入验证码：");
		driver.findElement(By.id("txtCaptcha")).sendKeys(code.trim());

		driver.findElement(By.id("loginbtn")).click();
		Thread.sleep(5000);
		System.out.println("sucess");
		System.out.println("============" + driver.getCurrentUrl());
		System.out.println("====================clieck===================");
		/* driver.findElement(By.id("hqyzm")).click(); */
		System.out.println("====================clieck===================");
		// Thread.sleep(130000);

		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02091577");
		wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("bodyIframe"));
			}
		});
		System.out.println("======================");

		driver.get("http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");

		Thread.sleep(3000);

		driver.findElement(By.id("a_sendRandom")).click();

		Thread.sleep(3000);

		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (NoAlertPresentException e) {
			Thread.sleep(1000);
			e.printStackTrace();
		}

		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			System.out.println(cookieWebClient.getName() + ":" + cookieWebClient.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		TelecomHaiNanUserIdBean telecomHaiNanUserIdBean = TelecomParseHaiNan.readyforUserId(driver.getPageSource());
		// getPhonecode(webClient);

		String smsCode = JOptionPane.showInputDialog("请输入短信验证码：");

		verifySms(smsCode, telecomHaiNanUserIdBean);
		// getYzm(webClient);
		return null;
		//
	}

	public static void getPhonecode(WebClient webClient) throws Exception {

		String url = "http://www.hi.189.cn/BUFFALO/buffalo/CommonAjaxService";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		webRequest.setRequestBody("<buffalo-call>" + "<method>getSmsCode</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PHONENUM</string>" + "<string>" + "17763819087"
				+ "</string>" + "<string>PRODUCTID</string>" + "<string>50</string>" + "<string>RTYPE</string>"
				+ "<string>QD</string>" + "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		System.out.println(page.getWebResponse().getContentAsString());

	}

	public static void verifySms(String smsCode, TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) throws Exception {
		// String html_UserId = null;

		// try {
		// html_UserId = readyGetUserId(webClient);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// throw new ErrorException("获取关键字错误");
		// }

		LocalDate today = LocalDate.now();

		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-0);

		String month = enddate.getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}

		String stardate = today.getYear() + month;
		 setphonecodeRead(telecomHaiNanUserIdBean, webClient);
		setphonecode(smsCode, telecomHaiNanUserIdBean, stardate);
		// 手机验证码验证成功状态更新

	}

	public static WebParamTelecom<?> setphonecode(String smsCode, TelecomHaiNanUserIdBean telecomHaiNanUserIdBean,
			String stardate) throws Exception {
		WebParamTelecom<?> webParamTelecom = new WebParamTelecom<>();
		// System.out.println(telecomHaiNanUserIdBean.toString());

		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";
		// http://hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer",
				"http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "hi.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		String canshuString = "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type>"
				+ "<string>PRODNUM</string>" + "<string>" + telecomHaiNanUserIdBean.getCanshu().trim() + "</string>"
				+ "<string>CITYCODE</string><string>0898</string>" + "<string>QRYDATE</string>"
				+ "<string>201810</string>" + "<string>TYPE</string>" + "<string>8</string>"
				+ "<string>PRODUCTID</string>" + "<string>50</string>" + "<string>CODE</string>" + "<string>"
				+ smsCode.trim() + "</string>" + "<string>USERID</string>" + "<string>"
				+ telecomHaiNanUserIdBean.getUserid().trim() + "</string></map></buffalo-call>";
		
		
		webRequest.setRequestBody(canshuString);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("telecomHaiNanUserIdBean=====" + telecomHaiNanUserIdBean.toString());
		System.out.println("canshuString============="+canshuString);

		if (page.getWebResponse().getContentAsString().indexOf("对不起，短信验证码不正确，请重新输入") != -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setErrormessage("对不起，短信验证码不正确，请重新输入");
			return webParamTelecom;

		}
		if (page.getWebResponse().getContentAsString().indexOf("对不起，你的短信验证码已失效，请重新获取") != -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setErrormessage("对不起，你的短信验证码已失效，请重新获取");
			return webParamTelecom;

		}
		if (page.getWebResponse().getContentAsString().indexOf("null") != -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			return webParamTelecom;
		}
		if (page.getWebResponse().getContentAsString().indexOf("呼叫类型") == -1
				&& page.getWebResponse().getContentAsString().indexOf("null") == -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setErrormessage("对不起，你的短信验证码验证失败");
			return webParamTelecom;
		}
		webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
		return webParamTelecom;
	}

	public static void setphonecodeRead(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, WebClient webCliente)
			throws Exception {
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";
		// http://hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer",
				"http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "hi.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		String canshuString = "<buffalo-call><method>checkDetailBill</method><map><type>java.util.HashMap</type>"
				+ "<string>PRODNUM</string><string>" + telecomHaiNanUserIdBean.getCanshu().trim()
				+ "</string><string>CITYCODE</string><string>0898</string>"
				+ "<string>OBJECTTYPE</string><string>50</string></map></buffalo-call>";
		webRequest.setRequestBody(canshuString);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		System.out.println("==========setphonecodeRead========" + page.getWebResponse().getContentAsString());

	}

	public static String readyGetUserId(WebClient webClient) throws Exception {
		// String url = "http://www.189.cn/login/index.do";
		//
		// LoginAndGetCommon.getHtml(url, webClient);
		// url =
		// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02091577";
		// getHtml(url, webClient);
		String url = "http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
		Page page = getHtml(url, webClient);

		// url =
		// "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
		// // webClient.getOptions().setJavaScriptEnabled(false);
		// WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// Page page = TeleComCommonUnit.gethtmlWebRequest(webClient,
		// webRequest);
		return page.getWebResponse().getContentAsString();

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "www.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/html/login/right.html");
		webRequest.setAdditionalHeader("Origin", "http://www.czgjj.com");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	// 得到验证码方法
	public static void getYzm2(WebClient webClient) {

		try {
			String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000776";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			webClient = wdzl.getWebClient();
			int statusCode = wdzl.getWebResponse().getStatusCode();
			if (statusCode == 200) {

				String packurl7 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend7 = "<buffalo-call><method>getCustOfBillByCustomerCode</method><string>2951121277270000__giveup</string></buffalo-call>";
				WebRequest webRequestpack7 = new WebRequest(new URL(packurl7), HttpMethod.POST);
				webRequestpack7.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack7.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack7.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack7.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack7.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack7.setRequestBody(requestPayloadSend7);
				webClient.getPage(webRequestpack7);

				String packurl8 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend8 = "<buffalo-call><method>getFeeNumByHT</method><string>10732369</string><string>201708</string></buffalo-call>";
				WebRequest webRequestpack8 = new WebRequest(new URL(packurl8), HttpMethod.POST);
				webRequestpack8.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack8.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack8.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack8.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack8.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack8.setRequestBody(requestPayloadSend8);
				webClient.getPage(webRequestpack8);

				String packurl9 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend9 = "<buffalo-call><method>getSelectedFeeProdNum</method><string>undefined</string><string>"
						+ "18995154123" + "</string><string>2</string></buffalo-call>";
				WebRequest webRequestpack9 = new WebRequest(new URL(packurl9), HttpMethod.POST);
				webRequestpack9.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack9.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack9.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack9.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack9.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack9.setRequestBody(requestPayloadSend9);
				Page page2 = webClient.getPage(webRequestpack9);
				System.out.println("发送验证码接口" + page2.getWebResponse().getContentAsString());
				// 发送验证码接口
				String sendurl = "http://nx.189.cn/bfapp/buffalo/CtSubmitService";
				String requestPayloadSend = "<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
				WebRequest webRequestSend = new WebRequest(new URL(sendurl), HttpMethod.POST);
				webRequestSend.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestSend.setAdditionalHeader("Host", "nx.189.cn");
				webRequestSend.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestSend.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestSend.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestSend.setRequestBody(requestPayloadSend);
				Page pageSend = webClient.getPage(webRequestSend);

				String send = pageSend.getWebResponse().getContentAsString();
				System.out.println("发送验证码接口" + send);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getYzm(WebClient webClient) {
		/*
		 * for (Cookie cookie : cookies) {
		 * webClient.getCookieManager().addCookie(cookie); }
		 */
		try {
			/*
			 * String wdzlurl =
			 * "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000501";
			 * WebRequest webRequestwdzl = new WebRequest(new URL(wdzlurl),
			 * HttpMethod.GET); HtmlPage wdzl =
			 * webClient.getPage(webRequestwdzl);
			 * System.out.println(wdzl.asXml()); webClient =
			 * wdzl.getWebClient(); String url =
			 * "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx";
			 * WebRequest webRequestGet = new WebRequest(new URL(url),
			 * HttpMethod.GET);
			 * webRequestGet.setAdditionalHeader("Content-Type",
			 * "text/plain;charset=UTF-8");
			 * webRequestGet.setAdditionalHeader("Host", "nx.189.cn");
			 * webRequestGet.setAdditionalHeader("Referer",
			 * "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000779"
			 * ); webRequestGet.setAdditionalHeader("User-Agent",
			 * "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"
			 * ); Page fastcode = webClient.getPage(webRequestGet);
			 * System.out.println("=========================================");
			 * System.out.println(fastcode.getWebResponse().getContentAsString()
			 * );
			 * System.out.println("=========================================");
			 * 
			 * String url2 =
			 * "http://nx.189.cn/html/wt/service/bill/printXd.html"; Page
			 * fastcode2 = webClient.getPage(webRequestGet);
			 * 
			 * String wdzlurl2 =
			 * "http://www.189.cn/dqmh/my189/checkMy189Session.do"; WebRequest
			 * webRequestwdzl2 = new WebRequest(new URL(wdzlurl),
			 * HttpMethod.POST); List<NameValuePair> paramsList = new
			 * ArrayList<NameValuePair>(); paramsList.add(new
			 * NameValuePair("fastcode", "20000776"));
			 * webRequestwdzl2.setAdditionalHeader("Content-Type",
			 * "text/plain;charset=UTF-8");
			 * webRequestwdzl2.setAdditionalHeader("Host", "www.189.cn");
			 * webRequestwdzl2.setAdditionalHeader("Origin",
			 * "http://www.189.cn");
			 * webRequestwdzl2.setAdditionalHeader("Referer",
			 * "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000779"
			 * ); webRequestwdzl2.setAdditionalHeader("User-Agent",
			 * "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"
			 * ); webRequestwdzl2.setRequestParameters(paramsList);
			 * 
			 * Page wdzl2 = webClient.getPage(webRequestwdzl2);
			 * System.out.println("=========================================");
			 * System.out.println(wdzl.getWebResponse().getContentAsString());
			 * System.out.println("=========================================");
			 * 
			 * // webClient = wdzl.getWebClient();
			 * 
			 * String packurl7 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
			 * String requestPayloadSend7 =
			 * "<buffalo-call><method>getSelectedFeeProdNum</method>" +
			 * "<string>0951</string><string>18995154123</string><string>2</string></buffalo-call>";
			 * WebRequest webRequestpack7 = new WebRequest(new URL(packurl7),
			 * HttpMethod.POST);
			 * 
			 * webRequestpack7.setAdditionalHeader("Content-Type",
			 * "text/plain;charset=UTF-8");
			 * webRequestpack7.setAdditionalHeader("Accept-Encoding",
			 * "gzip, deflate");
			 * webRequestpack7.setAdditionalHeader("Accept-Language",
			 * "zh-CN,zh;q=0.9"); webRequestpack7.setAdditionalHeader("Host",
			 * "nx.189.cn"); webRequestpack7.setAdditionalHeader("Origin",
			 * "http://nx.189.cn");
			 * webRequestpack7.setAdditionalHeader("Referer",
			 * "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
			 * webRequestpack7.setAdditionalHeader("User-Agent",
			 * "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"
			 * ); webRequestpack7.setAdditionalHeader("X-Buffalo-Version",
			 * "2.0");
			 * 
			 * webRequestpack7.setRequestBody(requestPayloadSend7);
			 * webClient.getPage(webRequestpack7); Page page1 =
			 * webClient.getPage(webRequestpack7);
			 * 
			 * String packurl9 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
			 * String requestPayloadSend9 =
			 * "<buffalo-call><method>checkIsBillSMSShow</method></buffalo-call>";
			 * WebRequest webRequestpack9 = new WebRequest(new URL(packurl9),
			 * HttpMethod.POST);
			 * webRequestpack9.setAdditionalHeader("Content-Type",
			 * "text/plain;charset=UTF-8");
			 * webRequestpack9.setAdditionalHeader("Host", "nx.189.cn");
			 * webRequestpack9.setAdditionalHeader("Origin",
			 * "http://nx.189.cn");
			 * webRequestpack9.setAdditionalHeader("Referer",
			 * "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
			 * webRequestpack9.setAdditionalHeader("User-Agent",
			 * "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"
			 * ); webRequestpack9.setRequestBody(requestPayloadSend9); Page
			 * page2 = webClient.getPage(webRequestpack9);
			 */
			// 发送验证码接口
			/*
			 * String sendurl =
			 * "http://nx.189.cn/bfapp/buffalo/CtSubmitService"; String
			 * requestPayloadSend =
			 * "<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
			 * WebRequest webRequestSend = new WebRequest(new URL(sendurl),
			 * HttpMethod.POST);
			 * webRequestSend.setAdditionalHeader("Content-Type",
			 * "text/plain;charset=UTF-8");
			 * webRequestSend.setAdditionalHeader("Host", "nx.189.cn");
			 * webRequestSend.setAdditionalHeader("Origin", "http://nx.189.cn");
			 * webRequestSend.setAdditionalHeader("Referer",
			 * "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
			 * webRequestSend.setAdditionalHeader("User-Agent",
			 * "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"
			 * ); webRequestSend.setRequestBody(requestPayloadSend); Page
			 * pageSend = webClient.getPage(webRequestSend);
			 */

			// String send = pageSend.getWebResponse().getContentAsString();
			/*
			 * System.out.println("page1=" +
			 * page1.getWebResponse().getContentAsString());
			 * System.out.println("page2=" +
			 * page2.getWebResponse().getContentAsString());
			 */
			String packurl7 = "http://nx.189.cn/bfapp/buffalo/CtSubmitService";
			String requestPayloadSend7 = "<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
			WebRequest webRequestpack7 = new WebRequest(new URL(packurl7), HttpMethod.POST);

			webRequestpack7.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequestpack7.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack7.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequestpack7.setAdditionalHeader("Host", "nx.189.cn");
			webRequestpack7.setAdditionalHeader("Origin", "http://nx.189.cn");
			webRequestpack7.setAdditionalHeader("Referer",
					"http://nx.189.cn/jt/bill/xd/?fastcode=10000501&cityCode=nx");
			webRequestpack7.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack7.setAdditionalHeader("X-Buffalo-Version", "2.0");

			webRequestpack7.setRequestBody(requestPayloadSend7);
			webClient.getPage(webRequestpack7);
			Page page1 = webClient.getPage(webRequestpack7);
			String send = page1.getWebResponse().getContentAsString();
			System.out.println("发送验证码接口" + send);

			Set<Cookie> cookies = webClient.getCookieManager().getCookies();
			for (Cookie cookie : cookies) {
				System.out.println(cookie.getName() + ":" + cookie.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// https://order.jd.com/center/list.action
	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
