package testTaobao;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Sleeper;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

public class TestLogon {
	
	
	static String driverPath = "D:\\ChromeDriver\\chromedriver.exe";
	
	static Boolean headless = false;

	public static void main(String[] args) throws Exception {
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		driver.manage().window().fullscreen();
		String baseUrl = "https://auth.alipay.com/login/index.htm";
		driver.get(baseUrl);  
		Thread.sleep(1100); 
		
		WebElement pwdLogin = driver.findElement(By.xpath("//*[@id='J-loginMethod-tabs']/li[2]"));
		pwdLogin.click();
		Thread.sleep(1100); 
		WebElement username = driver.findElement(By.id("J-input-user"));
		WebElement password = driver.findElement(By.id("password_rsainput"));
		WebElement btnsubmit = driver.findElement(By.id("J-login-btn"));
//		username.clear();
//		Thread.sleep(800); 
		username.sendKeys("该宝贝缺货");
		Thread.sleep(1100); 
//		password.clear();
//		Thread.sleep(1600);
		password.sendKeys("sun1992");
		Thread.sleep(1100); 
		btnsubmit.click();
		Thread.sleep(1100); 
		
		System.out.println("url==="+driver.getCurrentUrl());
		System.out.println("html==="+driver.getPageSource());
		
		
		Thread.sleep(11000);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		Thread.sleep(1500);
		WebElement sub = driver.findElement(By.id("J-submit"));
		WebElement riskackcode = driver.findElement(By.id("riskackcode"));
		riskackcode.clear();
		riskackcode.sendKeys(input);
		Thread.sleep(1500);
		sub.click();
		Thread.sleep(1500);
		System.out.println("url2==="+driver.getCurrentUrl());
		System.out.println("html2==="+driver.getPageSource());
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("shanghu.alipay.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		String accountUrl = "https://shanghu.alipay.com/user/myAccount/index.htm";
		WebRequest request2 = new WebRequest(new URL(accountUrl), HttpMethod.GET);
		Page accountPage = webClient.getPage(request2);
		System.out.println("accountPage=="+accountPage.getWebResponse().getContentAsString());
		
//		driver.get("https://zht.alipay.com/asset/newIndex.htm");
		driver.get("https://zht.alipay.com/asset/bankList.htm");
		Thread.sleep(1500);
		Set<org.openqa.selenium.Cookie> cookiesDriver2 = driver.manage().getCookies();
		
		webClient = WebCrawler.getInstance().getNewWebClient();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver2) {
			Cookie cookieWebClient = new Cookie("zht.alipay.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		String banklistUrl = "https://zht.alipay.com/asset/bankList.htm";
//		String banklistUrl = "https://zht.alipay.com/asset/bindQuery.json?_input_charset=utf-8&providerType=BANK";
		WebRequest request = new WebRequest(new URL(banklistUrl), HttpMethod.GET);
		Page bankListPage = webClient.getPage(request);
		System.out.println("bankCardPage=="+bankListPage.getWebResponse().getContentAsString());
		
		
	}
	
	
	
	
	
	
	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
//		 if(headless){
//		 chromeOptions.addArguments("headless");// headless mode
//		 }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
//		chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

}
