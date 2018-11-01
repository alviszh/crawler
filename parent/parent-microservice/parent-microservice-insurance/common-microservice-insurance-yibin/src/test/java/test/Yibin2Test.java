package test;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Yibin2Test {

	static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	private static final String OCR_FILE_PATH = "/home/img";

	public static void main(String[] args) throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://wt.yibinhrss.gov.cn:7002/";
		driver.get(baseUrl);

		driver.findElement(By.id("usename")).sendKeys("511525199207015389");
		// 密码
	
		driver.findElement(By.id("password")).sendKeys("480094");


		Actions action = new Actions(driver);
		

		WebElement source = driver.findElement(By.xpath("//span[@id='hnii_label']"));
		
		action.clickAndHold(source).moveByOffset(258, 0);
		Thread.sleep(2000);
//		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 250, 0);
//		Thread.sleep(2000);
//		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 250, 0);
//		Thread.sleep(2000);
//		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 250, 0);
//		Thread.sleep(2000);
//		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 250, 0);
//		Thread.sleep(2000);
//		
		// 释放
		action.moveToElement(source).release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();

		driver.findElement(By.id("submit")).click();

		Thread.sleep(10000);
		
		String html = driver.getPageSource();
		String currentUrl = driver.getCurrentUrl();
		System.out.println(html);
		if (currentUrl!=baseUrl) {
			System.out.println("登录成功！");
			System.out.println(html);
		}else{
			
			System.out.println("登录失败！");
			Thread.sleep(2000);
			//验证账号
			try {
				String text = driver.findElement(By.id("sfzhError")).getText();
				System.out.println("1111111111111"+text);
				if("错误代码：Q6010004，你尚未注册成为电子公积金用户，请先注册。".equals(text)){
					System.out.println("账号输入不正确！");
				}
			} catch (Exception e) {
				System.out.println("账号输入正确！");
			}
			//验证密码
			try {
				String text = driver.findElement(By.id("cxmmError")).getText();
				System.out.println("2222222222222"+text);
				if("错误代码：Q6010005，您录入的查询密码有误，请核对后重新录入。".equals(text)||"错误代码：Q6010006，您输错查询密码次数已达2次，再次输错您的登录将受到限制。".equals(text)){
					System.out.println("密码输入不正确！");
				}
			} catch (Exception e) {
				System.out.println("密码输入正确！");
			}
			//验证验证码
			try {
				String text = driver.findElement(By.id("yzmError")).getText();
				System.out.println("3333333333333333"+text);
				if(null!=text){
					System.out.println("图片验证码输入不正确！");
				}
			} catch (Exception e) {
				System.out.println("图片验证码输入正确！");
			}
			
		}
	}



}
