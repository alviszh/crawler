package Test;

import java.awt.Robot;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class TestCreditCard {
	 static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
		
//		private static final String LEN_MIN = "0";
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
			String baseUrl = "https://creditcard.ecitic.com/citiccard/ucweb/entry.do";
			driver.get(baseUrl);
			driver.manage().window().maximize();
			String pageSource = driver.getPageSource();
			System.out.println(pageSource);
			WebElement username = driver.findElement(By.id("phone"));
			
			Thread.sleep(1000);
			
			username.click();
			Thread.sleep(1000);
			username.clear();
			Thread.sleep(1000);
			username.sendKeys("13520800817");
			Thread.sleep(1000);
			InputTab();
			
			String password = "12qwaszx";
			VirtualKeyBoard.KeyPressEx(password,500);

			driver.findElement(By.className("submit")).click();
			Thread.sleep(1000);
			String pageSource2 = driver.getPageSource();
			String html = pageSource2;
			
			
			if(html.contains("短信验证码"))
			{
				Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
						.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				WebElement dtsecretcode = wait2.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("dtsecretcode"));
					}
				});
				dtsecretcode.click();
				dtsecretcode.clear();
				
//				while(true)
//				{
//					if()
//					{
//						
//					}
//					else
//					{
//						
//					}
//				}
				
				System.out.println("登录成功！");
				System.out.println(driver.getPageSource());
				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
				WebClient webClient = WebCrawler.getInstance().getWebClient();
				for(org.openqa.selenium.Cookie cookie : cookies){
					Cookie cookieWebClient = new Cookie("creditcard.ecitic.com", cookie.getName(), cookie.getValue());
					webClient.getCookieManager().addCookie(cookieWebClient);}
			}
			else
			{
				System.out.println("登录失败！");
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
