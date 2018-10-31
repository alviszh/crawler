package test.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;


public class IETest {
	
	static String driverPath = "D:\\software\\";
	
	public static void main(String[] args) {
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath+"IEDriverServer.exe");
		
		WebDriver driver = new InternetExplorerDriver();
		String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";
		//String baseUrl = "https://login.10086.cn/html/login/login.html";
		driver.get(baseUrl);
		
		String htmlsource = driver.getPageSource();
		driver.findElement(By.id("UniLoginPwd_Ctrl")).click();//UniLoginPwd_Ctrl        UniLoginUser_Ctrl
		//System.out.println("htmlsource--------"+htmlsource);
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	    driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		 
		Actions action=new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("1").perform();
		
		//parser.keyDown(Keys.NUMPAD6).keyUp(Keys.NUMPAD6).perform();
		
		//parser.keyDown(Keys.NUMPAD6);
		//driver.findElement(By.id("UniLoginUser_Ctrl")).click();
		
		//System.out.println("user------"+user);
		
		//driver.findElement(By.id("UniLoginPwd_Ctrl")).sendKeys("369258");     
		//driver.findElement(By.id("LoginBtn")).click();
		
		
	}

}
