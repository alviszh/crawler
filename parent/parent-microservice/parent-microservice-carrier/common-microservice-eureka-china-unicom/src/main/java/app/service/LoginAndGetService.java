package app.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.WebParamUnicom;
import app.bean2.SucessResultBean;
import app.commontracerlog.TracerLog;

@Component
public class LoginAndGetService {

	@Autowired
	private TracerLog tracerLog;
	
	@Autowired
	private AgentService agentService;
	

	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	private WebDriver driver = null;

	public WebDriver intiChrome() throws Exception {
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

	public WebParamUnicom<WebClient> login(MessageLogin messageLogin) {
		WebParamUnicom<WebClient> webParamUnicom = new WebParamUnicom<>();
		tracerLog.addTag("登录底层", messageLogin.toString());
		try {
			driver = intiChrome();
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			String url = "https://uac.10010.com/portal/mallLogin.jsp?redirectURL=http://www.10010.com/net5/";

			driver.get(url);
			Set<org.openqa.selenium.Cookie> cookies_driver = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies_driver) {
				webClient.getCookieManager()
						.addCookie(new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue()));
			}
			try {
				driver.close();
			} catch (Exception e) {
				driver.close();
			}

			tracerLog.addTag("unicomcrawler login", "==================准备工作完成=====================");
			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING);
			webParamUnicom.setWebClient(webClient);

			return webParamUnicom;
		} catch (Exception e) {
			e.printStackTrace();
			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE);
			return webParamUnicom;
		}
	}

	// 通话详单查询
	public String getCallThem(WebClient webClient, String firstday, String lastDay) {

	
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "http://iservice.10010.com/e3/static/query/callDetail?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("pageNo", "1"));
		paramsList.add(new NameValuePair("pageSize", "5000"));
		paramsList.add(new NameValuePair("beginDate", firstday));
		paramsList.add(new NameValuePair("endDate", lastDay));

	
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return null;
		
	}

	// 短信详单查询
	public String getNoteThem(WebClient webClient, String firstday, String lastDay) {

		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/sms?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/calls/call_sms-iframe.html&menuid=000100030002";
		List<NameValuePair> paramsList = new ArrayList<>();
		paramsList = new ArrayList<>();
		paramsList.add(new NameValuePair("pageNo", "1"));
		paramsList.add(new NameValuePair("pageSize", "5000"));
		paramsList.add(new NameValuePair("begindate", firstday));
		paramsList.add(new NameValuePair("enddate", lastDay));
		try {
			Page textPage = gethtmlPost(webClient, paramsList, url);
			tracerLog.addTag("unicomcrawler:url=" + url, "getNoteThem");
			tracerLog.addTag("unicomcrawler:time=" + firstday + "-" + lastDay,
					textPage.getWebResponse().getContentAsString());
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			if (textPage != null) {
				return textPage.getWebResponse().getContentAsString();
			}
		} catch (Exception e) {
			tracerLog.addTag("unicomcrawler crawler", e.getMessage());

		}

		return null;
	}

	// 获取历史详单
	public String getHistoryThem(Map<String, String> cookiesmap, String nowdate) {
		tracerLog.addTag("获取历史详单", nowdate);
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/queryHistoryBill?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
		Connection con4 = Jsoup.connect(url).method(Method.POST).timeout(30000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		con4.data("querytype", "0001");
		con4.data("querycode", "0001");
		con4.data("billdate", nowdate);
		con4.data("flag", "2");
		con4.cookies(cookiesmap);
		try {
			Response res4 = con4.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			return res4.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler", e.getMessage());

			return null;
		}
	}

	// 获取积分
	public String getIntegraThem(Map<String, String> cookiesmap, String nowdate) {
		tracerLog.addTag("获取积分详单", nowdate);
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/queryScoreResult?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/basic/score_iframe.html?menuCode=000100050001&menuid=000100050001";
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(30000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
		con.data("querycode", "0001");
		con.data("pageNo", "1");
		con.data("pageSize", "5000");
		con.data("billdate", nowdate);
		con.cookies(cookiesmap);

		try {
			Response res = con.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			return res.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler", e.getMessage());
			return null;
		}
	}

	// 获取积分2
	public String getIntegraThem2Production(Map<String, String> cookiesmap) {
		tracerLog.addTag("获取积分详单2", "");
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/scoreRecordFourgResult?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/basic/scoreQuery-iframe.html?menuCode=000100050001&menuid=000200040004";
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(30000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
		con.cookies(cookiesmap);

		try {
			Response res = con.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			return res.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler:url" + url, e.getMessage());

			return null;
		}
	}

	// 获取积分2
	public String getIntegraThem2All(Map<String, String> cookiesmap) {
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/scoreFourgResult?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/basic/scoreQuery-iframe.html?menuCode=000100050001&menuid=000200040003";
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(50000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
		con.cookies(cookiesmap);

		try {
			Response res = con.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			return res.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler:url" + url, e.getMessage());

			return null;
		}
	}

	// 缴费记录详单查询
	public String getPayMsgStatusThem(Map<String, String> cookiesmap, String firstday, String lastDay) {
		tracerLog.addTag("获取缴费记录详单", "firstday:" + firstday + ";" + "lastDay:" + lastDay);
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/paymentRecord?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/calls/paid_record-iframe.html?menuCode=000100010003&menuid=000100010003";
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(30000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
		con.data("pageNo", "1");
		con.data("pageSize", "5000");
		con.data("beginDate", firstday);
		con.data("endDate", lastDay);
		con.cookies(cookiesmap);
		try {
			Response res = con.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			return res.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler:url" + url, e.getMessage());
			tracerLog.addTag("unicomcrawler:time=" + firstday + "-" + lastDay, e.getMessage());
			System.out.println("====缴费html=======null ");
			return null;
		}

	}

	// 用户信息
	public String getUserinfoThem(Map<String, String> cookiesmap) {
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/contractPeriodQuery" + "?_="
				+ System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/basic/personal_xx_iframe.html?menuCode=000800010001";
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(30000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
		con.cookies(cookiesmap);
		try {
			Response res = con.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			
			con.execute();
			return res.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler:url" + url, e.getMessage());
			try {
				con.execute();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return null;
		}

	}

	// 账户余额
	public String getBalanceThem(Map<String, String> cookiesmap) {
		tracerLog.addTag("=账户余额", "");
		long s3 = System.currentTimeMillis();

		String url = "http://iservice.10010.com/e3/static/query/userinfoquery?_=" + System.currentTimeMillis();
		Connection con = Jsoup.connect(url).method(Method.POST).timeout(30000).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // 发送参数
		con.cookies(cookiesmap);
		try {
			Response res = con.execute();
			long s4 = System.currentTimeMillis();
			tracerLog.addTag("unicomcrawler:url=" + url, "耗时：" + (s4 - s3) + "ms");
			con.execute();

			return res.body();
		} catch (IOException e) {
			tracerLog.addTag("unicomcrawler crawler", e.getMessage());
			try {
				con.execute();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return null;
		}
	}

	public HtmlPage getHtml(String url, WebClient webClient) {

		try {

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracerLog.addTag("getHtml", e.getMessage());
			// TODO Auto-generated catch block
			return null;
		}

	}

	public Page getHtml2(String url, WebClient webClient) {
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

	public WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}

	public Page getHtml3(String url, WebClient webClient, int i) throws Exception, IOException {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setCharset(Charset.forName("UTF-8"));

		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public WebParamUnicom<WebClient> getPhonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		WebParamUnicom<WebClient> webParamUnicom = new WebParamUnicom<>();
		try {

			webClient.addRequestHeader("Host", "uac.10010.com");
			webClient.addRequestHeader("Pragma", "no-cache");

			webClient.addRequestHeader("Connection", "keep-alive");
			webClient.addRequestHeader("Referer", "https://uac.10010.com/portal/homeLogin");

			String url2 = "https://uac.10010.com/portal/Service/CheckNeedVerify?callback=jQuery172012727608617026065_"
					+ System.currentTimeMillis() + "&userName=" + messageLogin.getName().trim() + "&" + "pwdType=01&_="
					+ System.currentTimeMillis();
			Page page = getHtml2(url2, webClient);

			String url_code = "https://uac.10010.com/portal/Service/SendCkMSG?callback=jQuery172003873314014258589_"
					+ System.currentTimeMillis() + "&req_time=" + System.currentTimeMillis() + "&mobile="
					+ messageLogin.getName().trim() + "&_=" + System.currentTimeMillis();
			page = getHtml2(url_code, webClient);

			if (page.getWebResponse().getContentAsString().contains("resultCode:\"0000\"")) {
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());
				webParamUnicom.setWebClient(webClient);
			} else {
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());
				webParamUnicom.setWebClient(webClient);
				SucessResultBean sucessResult = getLoginResult(page.getWebResponse().getContentAsString());
				webParamUnicom.setErrormessage(sucessResult.getMsg().replaceAll(",needvode", ""));
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());
				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT);
			}

			return webParamUnicom;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			webParamUnicom.setErrormessage(e.getMessage());
			return webParamUnicom;
		}

	}

	// 验证手机验证码 (htmlunit方法验证 已弃用）
	public WebParamUnicom<WebClient> setphonecode(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		WebParamUnicom<WebClient> webParamUnicom = new WebParamUnicom<>();
		webClient.addRequestHeader("Host", "uac.10010.com");
		webClient.addRequestHeader("Pragma", "no-cache");

		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Referer", "https://uac.10010.com/portal/homeLogin");

		String url = "https://uac.10010.com/portal/Service/MallLogin?" + "callback=jQuery172003873314014258589_"
				+ System.currentTimeMillis() + "&req_time=" + System.currentTimeMillis()
				+ "&redirectURL=http%3A%2F%2Fwww.10010.com" + "&userName=" + messageLogin.getName().trim()
				+ "&password=" + messageLogin.getPassword().trim() + "&pwdType=01&productType=01"
				+ "&redirectType=01&rememberMe=1" + "&verifyCKCode=" + messageLogin.getSms_code().trim() + "&_="
				+ System.currentTimeMillis();
		Page page = getHtml2(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("====================短信验证码验证url=========" + url);

		if (page.getWebResponse().getContentAsString().contains("resultCode:\"0000\"")) {
			url = "http://iservice.10010.com/e3/static/common/mall_info?callback=jsonp1500458749489";
			webClient.getOptions().setRedirectEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.addRequestHeader("Host", "iservice.10010.com");
			webClient.addRequestHeader("Pragma", "no-cache");
			webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
			getHtml3(url, webClient, 0);
			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1);
			webParamUnicom.setWebClient(webClient);
			webParamUnicom.setHtml(page.getWebResponse().getContentAsString());

		} else {
			SucessResultBean sucessResult = getLoginResult(page.getWebResponse().getContentAsString());
			webParamUnicom.setErrormessage(sucessResult.getMsg().replaceAll(",needvode", ""));
			webParamUnicom.setHtml(page.getWebResponse().getContentAsString());
			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT);
		}

		return webParamUnicom;
	}

	/**   
	  *    
	  * 项目名称：common-microservice-eureka-china-unicom  
	  * 所属包名：app.service
	  * 类描述：   联通第一次验证短信验证码
	  * 创建人：hyx 
	  * 创建时间：2018年9月11日 
	  * @version 1  
	  * 返回值    WebParamUnicom<WebClient>
	  */
	public WebParamUnicom<WebClient> verifySms(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebParamUnicom<WebClient> webParamUnicom = new WebParamUnicom<>();

		driver = intiChrome();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		String url = "https://uac.10010.com/portal/homeLoginNew";

		driver.get(url);

		for (Cookie sookie : cookies) {
			driver.manage().addCookie(new org.openqa.selenium.Cookie(sookie.getName(), sookie.getValue()));
		}

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("userName"));
			}
		});

		loginname.sendKeys(messageLogin.getName().trim());

		driver.findElement(By.id("userPwd")).click();
		driver.findElement(By.id("userPwd")).sendKeys(messageLogin.getPassword().trim());

		Thread.sleep(1000);
		WebElement userCK = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("userCK"));
			}
		});
		if ((userCK.isDisplayed())) {
			userCK.sendKeys(messageLogin.getSms_code().trim());
		}

		driver.findElement(By.id("login1")).click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		try {
			wait.until(new Function<WebDriver, WebElement>() {

				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.className("user_phone"));

				}
			});

			driver.get("http://iservice.10010.com/e4/skip.html?menuCode=000100020001");

			Thread.sleep(2000);

			driver.get("http://iservice.10010.com/e3/navhtml3/WT3/WT_MENU_3_001/011/112.html");
			Thread.sleep(2000);

			Set<org.openqa.selenium.Cookie> cookies_driver = driver.manage().getCookies();

			String JSESSIONID_String = null;
			for (org.openqa.selenium.Cookie cookie : cookies_driver) {

				if (cookie.getName().indexOf("JSESSIONID") != -1) {
					JSESSIONID_String = cookie.getValue();
				}
				tracerLog.output("webClient2 cookie",cookie.getName() + ":::::::::::::" + cookie.getValue());
				// webClient2.getCookieManager()
				// .addCookie(new Cookie(cookie.getName(), cookie.getName(),
				// cookie.getValue()));
			}

			driver.get("http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
			Thread.sleep(5000);
			try{

				 wait.until(new Function<WebDriver, WebElement>() {

					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("huoqu_buttons"));
					}
				});
				 
				 
			}catch(Exception e){
				e.printStackTrace();
				
				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR);
//				webParamUnicom.setHtml(searchPage.getWebResponse().getContentAsString());
				quit(messageLogin);
				return webParamUnicom;
			}

			cookies_driver = driver.manage().getCookies();
			WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();

			for (org.openqa.selenium.Cookie cookie : cookies_driver) {

				webClient2.getCookieManager()
						.addCookie(new Cookie("iservice.10010.com", cookie.getName(), cookie.getValue()));
			}

			webClient2.getCookieManager().addCookie(new Cookie("iservice.10010.com", "JSESSIONID", JSESSIONID_String));

			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_READY_CODE_SECOND_DONING);
			webParamUnicom.setWebClient(webClient2);
//			webParamUnicom.setHtml(searchPage.getWebResponse().getContentAsString());
			quit(messageLogin);
			return webParamUnicom;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}
			try {
				
				Document doc = Jsoup.parse(driver.getPageSource());
//				mt35mf32
				Element ele = doc.select("span.mt10mf32,span.mt35mf32").first();
				String errormessage = ele.text();
//				WebElement errormessage = driver.findElement(By.className("mt10mf32"));
				if (!(errormessage == null || errormessage.isEmpty())) {
					
					
					if(errormessage.indexOf("短信验证码不正确")!=-1){
						webParamUnicom.setHtml(driver.getPageSource());
						webParamUnicom.setErrormessage(errormessage);
						webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1);
						quit(messageLogin);
						return webParamUnicom;
					}
					

					if(errormessage.indexOf("请输入验证码")!=-1){
						webParamUnicom.setHtml(driver.getPageSource());
						webParamUnicom.setErrormessage("未发送短信验证码，请点击发送短信验证码");
						webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1);
						quit(messageLogin);
						return webParamUnicom;
					}
					
					webParamUnicom.setHtml(driver.getPageSource());
					webParamUnicom.setErrormessage(errormessage);
					webParamUnicom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT);
					quit(messageLogin);
					return webParamUnicom;
				} else {
					webParamUnicom.setHtml(driver.getPageSource());
					webParamUnicom.setErrormessage("联通网络超时！");
					webParamUnicom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT);
					quit(messageLogin);
					return webParamUnicom;
				}
			} catch (Exception e1) {
				webParamUnicom.setHtml(driver.getPageSource());
				webParamUnicom.setErrormessage("联通网络超时！");
				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT);
				quit(messageLogin);
				return webParamUnicom;
			}

		}
	}

	/**   
	  *    
	  * 项目名称：common-microservice-eureka-china-unicom  
	  * 所属包名：app.service
	  * 类描述：   联通第二次发送短信验证码
	  * 创建人：hyx 
	  * 创建时间：2018年9月11日 
	  * @version 1  
	  * 返回值    WebParamUnicom<WebClient>
	  */
	public WebParamUnicom<WebClient> sendSmsTwice(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		WebParamUnicom<WebClient> webParamUnicom = new WebParamUnicom<>();
		String url = "http://iservice.10010.com/e3/static/query/sendRandomCode?_=" + System.currentTimeMillis()
				+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("menuId", "000100030001"));

		try {
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
			Page page = webClient.getPage(webRequest);

			tracerLog.addTag("获取第二次短信验证码" , page.getWebResponse().getContentAsString());

			if (page.getWebResponse().getContentAsString().contains("{\"issuccess\":true,\"sendcode\":true}")) {

				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS);
				webParamUnicom.setWebClient(webClient);
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());

			} else {
				// SucessResultBean sucessResult =
				// getLoginResult(page.getWebResponse().getContentAsString());
				webParamUnicom.setErrormessage(page.getWebResponse().getContentAsString());
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());
				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			webParamUnicom.setErrormessage("网络连接超时");
			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1);
		}

		return webParamUnicom;
	}

	/**   
	  *    
	  * 项目名称：common-microservice-eureka-china-unicom  
	  * 所属包名：app.service
	  * 类描述：   联通验证通话详单短信验证码
	  * 创建人：hyx 
	  * 创建时间：2018年9月11日 
	  * @version 1  
	  * 返回值    WebParamUnicom<WebClient>
	  */
	public WebParamUnicom<WebClient> verifySmsTwice(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebParamUnicom<WebClient> webParamUnicom = new WebParamUnicom<>();

		try {
			WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

			String url = "http://iservice.10010.com/e3/static/query/verificationSubmit?_=" + System.currentTimeMillis()
					+ "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("menuId", "000100030001"));
			paramsList.add(new NameValuePair("inputcode", messageLogin.getSms_code().trim()));

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
			Page page = webClient.getPage(webRequest);

			tracerLog.addTag("验证第二次短信验证码" , page.getWebResponse().getContentAsString());

			if (page.getWebResponse().getContentAsString().contains("{\"flag\":\"00\"}")) {

				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_QUERY_SUCCESS);
				webParamUnicom.setWebClient(webClient);
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());

			} else if (page.getWebResponse().getContentAsString().contains("{\"flag\":\"02\"}")) {
				webParamUnicom.setErrormessage("短信验证码错误或已过期，请重新发送");

				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_QUERY_ERROR);
				webParamUnicom.setWebClient(webClient);
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());

			} else {
				// SucessResultBean sucessResult =
				// getLoginResult(page.getWebResponse().getContentAsString());
				webParamUnicom.setErrormessage("短信验证码错误或已过期，请重新发送");
				webParamUnicom.setHtml(page.getWebResponse().getContentAsString());
				webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_QUERY_ERROR);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			e.printStackTrace();

			webParamUnicom.setErrormessage("网络连接超时");
			webParamUnicom.setStatusCodeEnum(StatusCodeEnum.TASKMOBILE_QUERY_ERROR);
		}

		return webParamUnicom;

	}

	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
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

	public static SucessResultBean getLoginResult(String html) {
		String value = html.split("\\(\\{")[1];

		String[] values = value.split("\"\\,");

		SucessResultBean sucessResultBean = new SucessResultBean();
		for (int i = 0; i < values.length; i++) {
			if (values[i].split(":")[0].contains("resultCode")) {
				sucessResultBean.setResultCode(values[i].split(":")[1].replaceAll("\"", "").replaceAll("\\'", "") + "");
			} else if (values[i].split(":")[0].contains("msg")) {
				sucessResultBean.setMsg(values[i].split(":")[1].replaceAll("\"", "").replaceAll("\\'", "")
						.replaceAll("。<a href=https", ""));
			}
		}

		return sucessResultBean;
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

	public void quit(MessageLogin messageLogin) {
		driver.close();
		agentService.releaseInstance(messageLogin.getIp(), driver);
	}

}
