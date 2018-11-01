package app.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestCardNoLogin {
	
	private static String iedriverPath = "D:\\IEDriverServer.exe";
	
	private static String chromedriverPath = "D:\\chromedriver.exe";
	

	public static void main(String[] args)  throws Exception {
		String loginUrl = "http://accounts.ccb.com/tran/WCCMainB1L1?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainB1L1&TXCODE=E3CX00";
		
		login(loginUrl); 
	}
	
	
	private static void login(String loginUrl) throws Exception{
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		
		/*System.setProperty("webdriver.ie.driver", iedriverPath ); 
		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);*/
		
		System.setProperty("webdriver.chrome.driver", chromedriverPath); 
		ChromeOptions chromeOptions = new ChromeOptions(); 
		WebDriver driver = new ChromeDriver(chromeOptions);
		
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(loginUrl);
		Thread.sleep(3000L);
		String pageSource = driver.getPageSource();
		System.out.println(pageSource);
		
		
		
		driver.switchTo().frame("result1"); 
		driver.findElement(By.name("ACC_NO_temp")).sendKeys("5324505099495745");
		driver.findElement(By.name("LOGPASS")).sendKeys("532450"); 
		driver.findElement(By.name("PT_CONFIRM_PWD")).sendKeys("123456"); 
		driver.findElement(By.className("btn")).click();
		
		Thread.sleep(3000L);
		
		System.out.println("---------------------------------------");
		System.out.println(pageSource);
		
		driver.switchTo().parentFrame().switchTo().frame("result1"); 
		
		String text = driver.findElement(By.className("content_fankui_message")).getText();
		
		
		
		Thread.sleep(3000L);
		
		System.out.println("=========================================");
		System.out.println(text);
		
		
	}

}
