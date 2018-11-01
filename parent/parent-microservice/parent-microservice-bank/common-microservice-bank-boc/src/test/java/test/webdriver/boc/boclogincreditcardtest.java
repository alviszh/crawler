package test.webdriver.boc;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.CookieJson;
import com.module.htmlunit.WebCrawler;

import app.bean.JsonRootBean;
import app.bean.creditcard.TransFlowResult;
import app.crawler.htmlparse.BocCreditParse;

public class boclogincreditcardtest {

	static String driverPath = "C:\\chromedriver.exe";

	static Boolean headless = false;
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

	public static String loginChromeCreditcard() throws Exception {
		WebDriver driver = intiChrome();
		// WebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.202.218:32768//wd/hub/"),
		// DesiredCapabilities.chrome());
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://ebsnew.boc.cn/boc15/login.html";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445"));
			}
		});

		String htmlsource = driver.getPageSource();
		System.out.println("htmlsource--11--" + htmlsource);
		driver.findElement(By.id("txt_username_79443")).sendKeys("5248651108859522");
		ele1.click();

		// WebDriverWait wait2 = new WebDriverWait(driver, 5);
		// wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("input_div_password_79445_1")));
		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele2 = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_txt_50531_740884"));
			}
		});

		String htmlsource2 = driver.getPageSource();
		ele2.sendKeys("123456");
		System.out.println("htmlsource--22--" + htmlsource2);

		String code = JOptionPane.showInputDialog("请输入验证码：");

		driver.findElement(By.id("txt_captcha_740885")).sendKeys(code);

		driver.findElement(By.id("btn_49_740887")).click();

		// Thread.sleep(10000L);//
		// 等待10秒，根据当前URL是否还是登录页面，判断登录是否成功，等待5秒也是等待登录成功后的页面信息加载

		wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		ele2 = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("div_account_information_740844"));
			}
		});

		String currentPageURL = driver.getCurrentUrl();
		System.out.println("currentPageURL--" + currentPageURL);
		if (currentPageURL.indexOf("https://ebsnew.boc.cn/boc15/welcome_ele.html") != -1) {
			System.out.println("==========登录成功=========");
		} else {
			String htmlsource3 = driver.getPageSource();
			System.out.println(htmlsource3);
			Document doc = Jsoup.parse(htmlsource3);
			String error_texg = doc.select("span#msgContent").text();
			System.out.println("=============error_texg=============" + error_texg);
		}

		System.out.println(driver.getPageSource());

		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("ebsnew.boc.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		// driver.quit();

		String url = "https://ebsnew.boc.cn/BII/PsnGetUserProfile.do?_locale=zh_CN";
		String countid = getCountid(url, webClient);

		driver.findElement(By.id("div_billedtrans_740846")).click();
		Thread.sleep(2000);
		cookiesDriver = driver.manage().getCookies();
		webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("ebsnew.boc.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);

		}

		Page page = getCreditInfo(url, webClient, countid);
		JsonRootBean<TransFlowResult> jsonroot = BocCreditParse.transflow_parse(page.getWebResponse().getContentAsString());

		System.out.println(jsonroot.getResponse().get(0).getResult());
		/*page = getTranList(url, webClient, countid, jsonroot.getResponse().get(0).getResult().getCrcdCustomerInfo().getCreditcardId());
		BocCreditParse.translist_parse(page.getWebResponse().getContentAsString());*/

		/*
		 * page = getUserInfo(url, webClient);
		 * BocParse2.userinfo_parse(page.getWebResponse().getContentAsString());
		 */

		/*
		 * System.out.println("htmlsource--33--" + htmlsource3);
		 * //BocParse.userinfo_parse(htmlsource3);
		 * 
		 * System.out.println("===================================");
		 * driver.findElement(By.id("div_transaction_details_740993")).click();
		 * String htmlsource4 = driver.getPageSource();
		 * System.out.println("htmlsource--444--" + htmlsource4); // driver.get(
		 * "https://ebsnew.boc.cn/BII/PsnGetUserProfile.do?_locale=zh_CN");
		 * Thread.sleep(5000L);// 等待5秒也是等待登录成功后的页面信息加载
		 * driver.findElement(By.id("date_start_date_740972")).clear();
		 * driver.findElement(By.id("date_start_date_740972")).sendKeys(
		 * "2017/04/30");
		 * driver.findElement(By.id("div_transaction_details_740993")).click();
		 * driver.findElement(By.id("btn_49_740974")).click();
		 * Thread.sleep(2000L);// 等待5秒也是等待登录成功后的页面信息加载 String htmlsource5 =
		 * driver.getPageSource();
		 * 
		 * System.out.println("========htmlsource5========"+htmlsource5);
		 */
		// islast(htmlsource5);

		return null;

	}

	/**
	 * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
	 * 
	 * @param soap
	 * @param rgex
	 * @return
	 */
	public static String getSubUtilSimple(String soap, String rgex) {
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public static boolean islast(String html) {
		Document doc = Jsoup.parse(html);
		Element ele = doc.select("div#pager").select("ul > li").get(1);
		Integer nowpage = Integer.parseInt(getSubUtilSimple(ele.text(), "第(.*?)页").trim());
		Integer allpage = Integer.parseInt(getSubUtilSimple(ele.text(), "共(.*?)页").trim());
		System.out.println("第nowpage" + nowpage + "页" + "   " + "共allpage" + allpage + "页");

		if (nowpage < allpage) {
			return true;
		} else {
			return false;
		}
	}

	public static Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}

		return set;

	}

	public static void main(String[] args) throws Exception {
		loginChromeCreditcard();
	}

	public static String getCountid(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},"
						+ "\"request\":[{\"id\":17,\"method\":\"PsnAccBocnetCreateConversation\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		String counid = getSubUtilSimple(searchPage.getWebResponse().getContentAsString(), "result(.*?)error")
				.replaceAll("\"", "").replaceAll(",", "").replaceAll(":", "").trim();
		System.out.println("counid======" + counid);
		return counid;
	}

	public static Page getCreditInfo(String url, WebClient webClient, String countid) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("X-id", "23");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20171013030320567&locale=zh&login=card&segment=1");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":19,\"method\":\"PsnAccBocnetQryCrcdStatement\",\"conversationId\":\""
						+ countid.trim()
						+ "\",\"params\":{\"accountSeq\":\"36202596\",\"statementMonth\":\"2017/11\"}}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		System.out.println("cookie = " + webClient.getCookieManager().getCookies());
		System.out.println("countid = " + countid.trim());
		return searchPage;
	}

	public static Page getTranList(String url, WebClient webClient, String countid,String creditcardId) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("X-id", "23");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20171013030320567&locale=zh&login=card&segment=1");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":22,\"method\":\"PsnAccBocnetQryCrcdStatementDetail\","
						+ "\"conversationId\":\"" + countid.trim()
						+ "\",\"params\":{\"creditcardId\":\""
						+ creditcardId.trim()
						+ "\",\"statementMonth\":\"2017/11/06\",\"accountType\":\"104\",\"primary\":\"\",\"_refresh\":\"true\",\"lineNum\":\"1000\",\"pageNo\":\"1\"}}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		System.out.println("cookie = " + webClient.getCookieManager().getCookies());
		System.out.println("countid = " + countid.trim());
		return searchPage;
	}

	public static Page getUserInfo(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":6,\"method\":\"PsnAccBocnetQryLoginInfo\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		return searchPage;
	}

	public static Page getAccount(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":8,\"method\":\"PsnAccBocnetQueryGeneralInfo\",\"conversationId\":null,\"params\":{\"accountSeq\":\"36202596\",\"currency\":\"\"}}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		return searchPage;
	}

}
