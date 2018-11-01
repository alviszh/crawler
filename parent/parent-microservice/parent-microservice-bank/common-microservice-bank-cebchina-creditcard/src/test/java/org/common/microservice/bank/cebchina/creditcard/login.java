package org.common.microservice.bank.cebchina.creditcard;

import java.awt.Robot;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class login {
		static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
		private static final String LEN_MIN = "0";
		private static final String TIME_ADD = "0";
		private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		
		try {
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			System.setProperty("webdriver.ie.driver", driverPath);
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

			String url = "https://xyk.cebbank.com/mall/login?target=/mycard/home/home.htm";

			driver.get(url);
			Thread.sleep(2000L);
			driver.findElement(By.id("userName")).sendKeys("");//输入帐号 6226580034768784
			String path = WebDriverUnit.saveImg(driver, By.className("grid-mock"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
			driver.findElement(By.id("yzmcode")).sendKeys(code);//验证码
			Thread.sleep(2000L);
			
			WebElement findElement = driver.findElement(By.xpath("//button[@class='btn btn-small js-login-sendCode']"));//发送短信			
			findElement.click();
			driver.findElement(By.xpath("//button[@class='btn btn-success btn-small js-btn-cancel']")).click();
		//	btn btn-success btn-small js-btn-cancel
			try { 
			String text = driver.switchTo().alert().getText();
			System.out.println(text);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			//String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			Thread.sleep(5000L);
			driver.findElement(By.id("verification-code")).sendKeys("789777");//输入短信验证码
			//登录
			driver.findElement(By.xpath("//input[@class='btn login-style-bt ']"));
			String html = driver.getPageSource();
			
			
			Thread.sleep(2000L);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
}
