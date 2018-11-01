package app.service;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.commontracerlog.TracerLog;

@Component
public class WebDriverChromeService {

	@Value("${webdriver.chrome.driver.path}")
	String driverPathChrome;

	@Autowired
	private TracerLog tracerLog;

	WebDriver driver ;


	public WebDriver getPageByChrome(String url) throws Exception{

		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPathChrome);

		if(driverPathChrome==null){
			tracerLog.addTag("WebDriverIEService initChrome RuntimeException", "webdriver.chrome.driver 初始化失败！");
			throw new RuntimeException("webdriver.chrome.driver 初始化失败！");
		}


		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu");

		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.get(url);

		return driver;

	}

	public void clearWebDriver(){
		driver = null;
	}

	public void quitWebDriver(){
		driver.quit();
	}

	public void clickButtonByDomId(WebDriver webDriver,String domId)  throws NoSuchWindowException{
		webDriver.findElement(By.id(domId)).click();
	}



}
