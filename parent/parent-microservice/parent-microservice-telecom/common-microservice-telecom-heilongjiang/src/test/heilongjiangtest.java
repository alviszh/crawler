
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;


/**   
*    
* 项目名称：common-microservice-telecom-heilongjiang   
* 类名称：heilongjiangtest   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年10月23日 上午11:42:30   
* @version        
*/
public class heilongjiangtest {

	// static String driverPath = "d:\\ChromeServer\\chromedriver.exe";

	static String driverPath = "c:\\chromedriver.exe";

	static Boolean headless = false;

	protected static Gson gs = new Gson();

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
		loginname.sendKeys("13351102145");
		driver.findElement(By.id("txtShowPwd")).click();
		// driver.findElement(By.id("txtPassword")).sendKeys("795372");
		driver.findElement(By.id("txtPassword")).sendKeys("216832");
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

		
			driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000846");
			
			Thread.sleep(5000);
			
			driver.get("http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");
			
			Thread.sleep(2000);
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();

		Map<String, String> cookiemap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			cookiemap.put(cookie.getName(), cookie.getValue());
			System.out.println("driver cookie:::::::::::"+
					cookieWebClient.getDomain() + ":" + cookieWebClient.getName() + ":" + cookieWebClient.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		WebParamTelecom<?>  webParamTelecom = getphonecode(webClient);
		
		System.out.println("发送短信验证码"+webParamTelecom.toString());
		
		String sms_code = JOptionPane.showInputDialog("请输入短信验证码：");
		
		MessageLogin messageLogin = new MessageLogin();
		
		messageLogin.setSms_code(sms_code.trim());
		
		setphonecode(messageLogin, webClient);
		getCallThemHtml(webClient);
		return null;
		//
	}

	public static WebParamTelecom<?> getphonecode(WebClient webClient) {

		WebParamTelecom<?>  webParamTelecom = new WebParamTelecom<>();
		try {

//			String errorurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000846";
//			HtmlPage htmlpage_error = getHtml(errorurl, webClient);
//
//			if (htmlpage_error.getWebResponse().getContentAsString().indexOf("对不起，系统忙，请稍后再试") != -1) {
//				webParamTelecom.setHtml(htmlpage_error.getWebResponse().getContentAsString());
//				webParamTelecom.setErrormessage("电信黑龙江网站出现问题，请稍后重试");
//				return webParamTelecom;
//			}
//			webClient = htmlpage_error.getWebClient();
//			//
//			String url = "http://hl.189.cn/service/cqd/detailQueryCondition.do";
//			HtmlPage htmlpage = getHtml(url, webClient);
			String url = "http://hl.189.cn/service/userCheck.do?method=sendMsg";

			List<NameValuePair> paramsList = new ArrayList<>();
			paramsList.add(new NameValuePair("method", "sendMsg"));

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");

			webRequest.setAdditionalHeader("Host", "hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Origin", "http://hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Referer",
					"http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");// 设置请求报文头里的refer字段

			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");// 设置请求报文头里的refer字段

			webClient.getOptions().setJavaScriptEnabled(false);
			
			Page page = webClient.getPage(webRequest);
			System.out.println(page.getWebResponse().getContentAsString());
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);

			System.out.println(page.getWebResponse().getContentAsString());
			System.out.println("==========================");
			if (webParamTelecom.getHtml().indexOf("对不起，系统忙，请稍后再试") != -1) {
				webParamTelecom.setErrormessage("电信黑龙江网站出现问题，请稍后重试");
			}
			return webParamTelecom;
		} catch (Exception e) {
			
			return webParamTelecom;
		}

	}

	public static WebParamTelecom<?>  setphonecode(MessageLogin messageLogin,WebClient webClient) {
		WebParamTelecom<?>  webParamTelecom = new WebParamTelecom<>();

		String url = "http://hl.189.cn/service/zzfw.do";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("method", "checkDX"));
		paramsList.add(new NameValuePair("yzm", messageLogin.getSms_code().trim()));

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");

			webRequest.setAdditionalHeader("Host", "hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Origin", "http://hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Referer",
					"http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");// 设置请求报文头里的refer字段

			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");// 设置请求报文头里的refer字段

			Page page = webClient.getPage(webRequest);
			System.out.println(page.getWebResponse().getContentAsString());
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			return webParamTelecom;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			webParamTelecom.setHtml(e.getMessage());
			webParamTelecom.setErrormessage(e.getMessage());
			return webParamTelecom;
		}

	}
	
	public static String getCallThemHtml(WebClient webClient) {
		String url = "http://hl.189.cn/service/cqd/queryDetailList.do?isMobile=0&seledType=9&queryType=&pageSize=10&pageNo=1&flag=&pflag=&accountNum=13351102145%3A2000004&callType=3&selectType=1&detailType=9&selectedDate=20188&method=queryCQDMain";

		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			System.out.println("==========================================");
			System.out.println(page.getWebResponse().getContentAsString());
			System.out.println("==========================================");

			Set<Cookie> cookies = webClient.getCookieManager().getCookies();

			for (Cookie cookie : cookies) {
				System.out.println("cookie数据为:::::::" + cookie.getName() + "==============" + cookie.getValue());
			}
			
			return page.getWebResponse().getContentAsString();
		} catch (Exception e) {

			return null;
		}

	}

	public static HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			return null;
		}

	}
	
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}


	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
