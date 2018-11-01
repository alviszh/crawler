package app.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.cmcc.domain.json.VerifyBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";
//	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
	private static WebDriver driver = null;
	
	public static void main(String[] args) {
		String url = "https://login.10086.cn/login.html";
		
		sendSms(url,"15210072522");
//		System.out.println("第一次登录短信   "+result);
//		
//		Scanner input = new Scanner(System.in);
//        String code = input.next();
//		
//		seleniumLogin(url,code);
	}
	
	private static void sendSms(String url, String mobileNo) {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu"); 
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(chromeOptions);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(url);
		
		//点击短信随机码登录选择项
		WebElement loginType = driver.findElement(By.id("sms_login_1"));
		loginType.click();
		
		//手机号输入框
		WebElement phone = driver.findElement(By.id("sms_name"));
		phone.sendKeys(mobileNo);
		//点击噶送短信按钮
		WebElement button = driver.findElement(By.id("getSMSPwd1"));
		button.click();
		
	}

	private static void seleniumLogin(String url, String code) {
//		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
//		System.out.println("launching IE browser");
//		System.setProperty("webdriver.ie.driver", driverPath );
//
//		driver = new InternetExplorerDriver();
//		driver = new InternetExplorerDriver(ieCapabilities);
//		 
//		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu"); 
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(chromeOptions);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(url);
		
		//手机号输入框
		WebElement phone = driver.findElement(By.id("p_name"));
		//密码输入框
		WebElement password = driver.findElement(By.id("p_pwd"));
		//点击登录按钮
		WebElement button = driver.findElement(By.id("submit_bt"));
		
		phone.sendKeys("13520800817");
		password.sendKeys("958575");
		button.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//短信输入框
		WebElement sms = driver.findElement(By.id("sms_pwd"));
		try{
			sms.sendKeys(code);
		}catch(Exception e){
			sms = driver.findElement(By.id("sms_pwd"));
			System.out.println("短信随机码   ====》"+code);
			System.out.println("短信输入框   ====》"+sms.getText());
			sms.sendKeys(code);
		}
		button.click();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("*************************************   点击登录按钮后");
		System.out.println(driver.getPageSource());
		System.out.println("当前url =====》"+driver.getCurrentUrl());
		
		WebDriverWait wait = new WebDriverWait(driver, 15); 
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("详单查询")));
		System.out.println(element.toString());
		
		element.click();
//		System.out.println("最后一次的源码： "+driver.getPageSource());
		
		
		WebElement meal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("meal-5")));
		System.out.println("点击通话记录查询按钮： "+meal.toString());
		
		meal.click();
		
		WebElement date = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("month2")));
		System.out.println("点击通话记录具体月份： "+date.getText());
		
		date.click();
		
		WebElement smsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("stc-send-sms")));
		System.out.println("点击第二次验证短信按钮： "+smsButton.getText());
		
		smsButton.click();
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
			Cookie cookieWebClient = new Cookie("shop.10086.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
//		sendSecondsms();
		
		
		
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
//		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
//			Cookie cookieWebClient = new Cookie("shop.10086.cn", cookie.getName(), cookie.getValue());
//			webClient.getCookieManager().addCookie(cookieWebClient);
//		}
//		
//		try{
//			String url1 = "http://shop.10086.cn/i/v1/cust/mergecust/"+"15911135456"+"?_="+System.currentTimeMillis();
//			WebRequest request = new WebRequest(new URL(url1), HttpMethod.GET); 
//			
//			request.setAdditionalHeader("Host", "shop.10086.cn");55
//			request.setAdditionalHeader("pragma", "no-cache");
//			request.setAdditionalHeader("expires", "0");
//			request.setAdditionalHeader("If-Modified-Since", "0");
//			request.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+System.currentTimeMillis());
//			
//			UnexpectedPage page = webClient.getPage(request);
//			
//			System.out.println("个人信息   ====》"+page.getWebResponse().getContentAsString());
//		}catch(Exception e){
//			
//		}
//		
	}

	private static void getHtml(String url) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			HtmlPage searchPage = (HtmlPage) getHtml(url,webClient);
			//手机号码
			HtmlTextInput phone = searchPage.getFirstByXPath("//input[@id='p_name']");
			//服务密码
			HtmlPasswordInput password = searchPage.getFirstByXPath("//input[@id='p_pwd']");
			//登录按钮
			HtmlButton button = searchPage.getFirstByXPath("//button[@id='submit_bt']");
			
			phone.setText("15210072522");
			password.setText("001314");
			
			HtmlPage page = button.click();
			System.out.println(page.asXml());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Page getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	
	public static String sendSMS( String mobileNum) {
		
		//原网站现已发现，发送请求返回结果为0才是发送成功。1和2为失败，其他返回数字还未知。无法判断1和2分别是哪种情况发送失败。
		
//		tracer.addTag("CmccLoginParser sendSMS", mobileNum);
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			URL smsAction = new URL("https://login.10086.cn/sendRandomCodeAction.action");
			WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("userName", mobileNum));
			requestSettings.getRequestParameters().add(new NameValuePair("type", "01"));
			requestSettings.getRequestParameters().add(new NameValuePair("channelID", "12034"));
			
			requestSettings.setAdditionalHeader("Host", "login.10086.cn");
			requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
			requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html");
			
			Page page = webClient.getPage(requestSettings); 
			String html = page.getWebResponse().getContentAsString();
//			tracer.addTag("sendSMS 登录短信发送", html);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void sendSecondsms(){
		
		String url = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/15210072522";
		try{
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
			requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
			
			requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+System.currentTimeMillis());
			requestSettings.setAdditionalHeader("Accept", "*/*");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			
			requestSettings.getRequestParameters().add(new NameValuePair("callback", "jQuery183008660428427514177_"+String.valueOf(System.currentTimeMillis())));
			requestSettings.getRequestParameters().add(new NameValuePair("_", String.valueOf(System.currentTimeMillis())));		
			
			Page page = webClient.getPage(requestSettings); 						
			String html = page.getWebResponse().getContentAsString();
			
			System.out.println("sendVerifySMS 二次验证短信发送 :"+html);
			String json = html.substring(html.indexOf("(")+1, html.length()-1);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
