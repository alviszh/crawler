package test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.crawler.mobile.json.CookieJson;
import com.google.gson.Gson;

public class YibinTest {

	 static String driverPath32 = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	public static void main(String[] args) throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath32);

		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://wt.yibinhrss.gov.cn:7002/";
		driver.get(baseUrl);


	

		driver.findElement(By.id("usename")).sendKeys("511525199207015389");
		driver.findElement(By.id("password")).sendKeys("480094");

		//driver.findElement(By.id("hnii_labelTip")).click();
		
		Actions action = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//span[@id='hnii_label']"));
		// 点击并按住
		Actions clickAndHold = action.clickAndHold(source);
		// 移动至
		Actions moveByOffset = clickAndHold.moveByOffset(306, 0);
		// 释放
		moveByOffset.release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();

		driver.findElement(By.id("submit")).click();
		
		System.out.println(driver.getPageSource());

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
}
