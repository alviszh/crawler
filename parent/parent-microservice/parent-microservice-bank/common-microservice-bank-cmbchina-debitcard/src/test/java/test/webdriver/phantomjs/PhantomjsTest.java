package test.webdriver.phantomjs;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.module.jna.webdriver.WebDriverUnit;

public class PhantomjsTest {

	public static void main(String[] args) throws Exception {
		System.setProperty("phantomjs.binary.path", "D:\\software\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
	    //    WebDriver driver = new FirefoxDriver();
//        WebDriver driver = new PhantomJSDriver();
		WebDriver driver = null;
        driver.get("https://login.10086.cn/login.html");
        System.out.println(driver.getCurrentUrl());
        WebElement p_name = driver.findElement(By.id("p_name"));
        p_name.sendKeys("13520800817");
        
        WebElement p_pwd = driver.findElement(By.id("p_pwd"));
        p_pwd.sendKeys("958575");
        
        WebDriverUnit.saveScreenshotByPath(driver,"D:\\software\\phantomjs-2.1.1-windows\\bin\\");
        
        JavascriptExecutor driver_js= ((JavascriptExecutor) driver); 
		Thread.sleep(1500L);
		Object oo = driver_js.executeScript("encrypt('13520800817')");
		Thread.sleep(1000L);
        
        WebElement submit_bt = driver.findElement(By.id("submit_bt"));
        submit_bt.click();
        Thread.sleep(2000L);
        System.out.println("-------------------------------------");
        System.out.println(driver.getPageSource());
        
        WebElement sms_pwd = driver.findElement(By.id("sms_pwd"));
        sms_pwd.sendKeys("774682");
        Thread.sleep(2000L);
        WebDriverUnit.saveScreenshotByPath(driver,"D:\\software\\phantomjs-2.1.1-windows\\bin\\");
        
       
	}

}
