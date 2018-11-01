package test.housingfund.chengdu;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.crawler.mobile.json.CookieJson;
import com.google.gson.Gson;

public class ChengduTest {

	 static String driverPath64 = "D:\\IEDriverServer_x64\\IEDriverServer.exe";
	static String driverPath32 = "D:\\WD\\s\\IEDriverServer_Win32\\IEDriverServer.exe";

	public static void main(String[] args) throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath32);

		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://www.cdzfgjj.gov.cn:9802/cdnt/login.jsp#per";
		driver.get(baseUrl);


		WebElement adrOption = driver.findElement(By.cssSelector("#aType option[value='4']"));
		adrOption.click();

		driver.findElement(By.id("j_username")).sendKeys("510525198805233022");
		driver.findElement(By.id("j_password")).sendKeys("233022");

		Actions action = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//div[@class='Slider ui-draggable']"));
		// 点击并按住
		Actions clickAndHold = action.clickAndHold(source);
		// 移动至
		Actions moveByOffset = clickAndHold.moveByOffset(313, 0);
		// 释放
		moveByOffset.release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();

		driver.findElement(By.id("btn-login")).click();

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

}
