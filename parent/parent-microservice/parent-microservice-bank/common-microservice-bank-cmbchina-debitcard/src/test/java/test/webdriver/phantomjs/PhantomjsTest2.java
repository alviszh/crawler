package test.webdriver.phantomjs;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.module.jna.webdriver.WebDriverUnit;

public class PhantomjsTest2 {

	public static void main(String[] args) throws Exception {
		System.setProperty("phantomjs.binary.path", "D:\\software\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
//        WebDriver driver = new PhantomJSDriver();
        WebDriver driver = null;
        driver.get("https://login.10086.cn/login.html");
        
        JavascriptExecutor driver_js= ((JavascriptExecutor) driver); 
		Thread.sleep(1500L);
		Object oo = driver_js.executeScript("encrypt('13520800817')");
		Thread.sleep(1000L);
       
		System.out.println(oo);
        
       
	}

}
