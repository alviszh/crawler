package Test;

import java.awt.Robot;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class Testlogin2 {

    static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	
//	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	public static void main(String[] args) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		 
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://i.bank.ecitic.com/perbank6/signIn.do";
		driver.get(baseUrl);
		driver.manage().window().maximize();
		String pageSource = driver.getPageSource();
		System.out.println(pageSource);
		WebElement username = driver.findElement(By.id("logonid_temp"));
		Thread.sleep(1000);
		
		username.click();
		Thread.sleep(1000);
//		username.clear();
//		Thread.sleep(1000);232
		
		username.sendKeys("6217680704617328");
		VirtualKeyBoard.KeyPressEx("6217680704617328", 500);
		
		Thread.sleep(1000);
		InputTab();
		
		String password = "yl405232";
		VirtualKeyBoard.KeyPressEx(password,500);

		driver.findElement(By.id("logonButton")).click();
		Thread.sleep(1000);
		String currentUrl = driver.getCurrentUrl();
		System.out.println(currentUrl);
		if (baseUrl.contains(currentUrl)) {
			System.out.println("登录失败！");
		} else {
			
		}
	}
	public static void InputTab() throws IllegalAccessException, NativeException, Exception { 
		Thread.sleep(1000L); 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
		VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab")); 
		} 
		} 
	
	public static void InputEnter() throws IllegalAccessException, NativeException, Exception { 
		Thread.sleep(1000L); 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
		VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter")); 
		} 
		} 

}
