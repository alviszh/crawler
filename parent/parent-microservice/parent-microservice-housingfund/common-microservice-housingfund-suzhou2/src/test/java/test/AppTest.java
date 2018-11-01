package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

public class AppTest {
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\IEDriverServer_Win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "http://60.171.196.71:8004/";
		driver.get(baseUrl);
		// 用户名
		driver.findElement(By.id("username")).sendKeys("342201198505160231");
		// 密码
		Actions action = new Actions(driver);
		WebElement psddly = driver.findElement(By.xpath("//*[@id=\"password\"]"));
		action.moveToElement(psddly).click().sendKeys("521000mm").perform();

		WebElement source = driver.findElement(By.xpath("//*[@id=\"drag\"]/div[3]"));
		action.moveToElement(source).clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		// 释放
		action.moveToElement(source).release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();
		// 登录按钮
		WebElement button = driver
				.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[2]/div[2]/ul/li/ul/li[6]/button"));
		action.moveToElement(button).click().perform();
		Thread.sleep(5000);
		String html = driver.getPageSource();
		System.out.println(html);
	}
}