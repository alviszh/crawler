package test;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class YibinTestChrome {

	public static void main(String[] args) throws Exception {

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// String
			// cookies="[{\"domain\":\"www.cdzfgjj.gov.cn\",\"key\":\"loginType\",\"value\":\"4\"},{\"domain\":\"www.cdzfgjj.gov.cn\",\"key\":\"JSESSIONID1\",\"value\":\"F7cpHjMeP2sfZN7-ijNAo1284WlWHyu6vcP1xar5uyOXppdSKEAn!499450747\"},{\"domain\":\"www.cdzfgjj.gov.cn\",\"key\":\"userType\",\"value\":\"gr\"}]";
			String username = "511525199207015389";
			String password = "480094";
			String cookie = login(username, password);
	
	}


	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	public static ChromeDriver intiChrome() throws Exception {
		String driverPath = "D:\\ChromeDriver\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static String login(String username ,String password) throws Exception {

		ChromeDriver driver = intiChrome();

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "https://wt.yibinhrss.gov.cn:7002/";
		driver.get(baseUrl);
System.out.println(driver.getPageSource());
		driver.findElement(By.id("usename")).sendKeys(username);
		Thread.sleep(2000);
		driver.findElement(By.id("password")).sendKeys(password);
		Thread.sleep(2000);

		Actions action = new Actions(driver);
	    WebElement source = driver.findElement(By.xpath("//span[@id='hnii_label']"));
		
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		
		// 释放
		action.moveToElement(source).release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();

		driver.findElement(By.id("submit")).click();
		Thread.sleep(1000);
		
		String html = driver.getPageSource();
		String currentUrl = driver.getCurrentUrl();
		System.out.println(html);
		System.out.println(currentUrl);
		
		if (currentUrl!=baseUrl) {
			System.out.println("登录成功！");
			
		}else{
			
			System.out.println("登录失败！");
		}
		
		Thread.sleep(3000);
		driver.findElement(By.id("submit")).click();
		
		
		
		System.out.println(driver.getCurrentUrl());
		System.out.println(driver.getPageSource());
		WebDriverWait wait=new WebDriverWait(driver, 10);
	
		Alert alert = null;
		try {
			alert = wait.until(new Function<WebDriver, Alert>() {  
	            public Alert apply(WebDriver driver) {  
	                return driver.switchTo().alert(); 
	            }
	        });
		} catch (Exception e) {
			System.out.println("没有alert提示框");
		}
		
		if(null != alert){
			String alertText = alert.getText();
			System.out.println("弹框提示=="+alertText);
			alert.accept();
		}
		

		
			// 获取cookies
//			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
//			Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
//			for (org.openqa.selenium.Cookie cookie : cookies) {
//				CookieJson cookiejson = new CookieJson();
//				cookiejson.setDomain(cookie.getDomain());
//				cookiejson.setKey(cookie.getName());
//				cookiejson.setValue(cookie.getValue());
//				cookiesSet.add(cookiejson);
//			}
//			cookieJson = new Gson().toJson(cookiesSet);
//			System.out.println("cookieJson---------------------" + cookieJson);

		driver.quit();
		return null;

	}


}
