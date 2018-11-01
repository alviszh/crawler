package test;


import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

public class TestLogin2 {
	
	private static WebClient webClient = null;

	public static void main(String[] args) throws Exception {

		
			ChromeDriver driver = intiChrome();
			//driver.manage().window().maximize();
			String url = "https://wt.yibinhrss.gov.cn:7002/";
	//		driver.manage().window().maximize();
	
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			driver.get(url);
			
			webClient = WebCrawler.getInstance().getNewWebClient();
	
	
	
			driver.findElement(By.id("usename")).sendKeys("511525199207015389");
			Thread.sleep(1000);
			driver.findElement(By.id("password")).sendKeys("480094");
			Thread.sleep(1000);
			
			
			Actions action = new Actions(driver);
			WebElement source = driver.findElement(By.xpath("//span[@id='hnii_labelTip']"));
			
			action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            action.clickAndHold(source).moveByOffset((int)(Math.random()*200)+80, 0);
            Thread.sleep(2000);
            

            action.moveToElement(source).release();	   
			// 组织完这些一系列的步骤，然后开始真实执行操作
			Action actions = action.build();
			actions.perform();

			driver.findElement(By.id("submit")).click();
		
			// 获取cookies
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			Set<CookieJson> cookiesSet = new HashSet<CookieJson>();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				CookieJson cookiejson = new CookieJson();
				cookiejson.setDomain(cookie.getDomain());
				cookiejson.setKey(cookie.getName());
				cookiejson.setValue(cookie.getValue());
				cookiesSet.add(cookiejson);
			}

			String cookieJson = new Gson().toJson(cookiesSet);
			System.out.println(cookieJson);
	
			//https://www.sdwfhrss.gov.cn/rsjwz/self/cblb?time=2017
		    String loginUrl5 = "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls";
		
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.GET);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
			webRequest5.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls");
			webRequest5.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page5 = webClient.getPage(webRequest5);
			System.out.println(page5.asXml());
			
			
			
			System.out.println(getHtml("https://www.sdwfhrss.gov.cn/rsjwz/self/cblb?time=2016",webClient).asXml());
			
			String medUrl="https://www.sdwfhrss.gov.cn/rsjwz/self/zgyl?time=2017";
			HtmlPage medPage= getHtml(medUrl,webClient);
			System.out.println(medPage.asXml());
		
	}
	
	public static  ChromeDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", "D:/ChromeServer/chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
