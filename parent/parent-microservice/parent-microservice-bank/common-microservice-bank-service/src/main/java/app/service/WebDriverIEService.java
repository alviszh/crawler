package app.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.commontracerlog.TracerLog;


@Component
public class WebDriverIEService {
	
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	
/*	删除driverPathChrome  以免所以依赖了 common-microservice-bank-service 的微服务都要配置  ${webdriver.chrome.driver.path}，一般IE和chrome 用其中一个就可以了
 * 
 * 	@Value("${webdriver.chrome.driver.path}")
	String driverPathChrome;*/
	
	@Autowired
	private TracerLog tracerLog; 
	
	//private WebDriver driver ;
	
	public WebDriver getNewWebDriver(){ 
		 
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        System.out.println("driverPath====>>>"+driverPath);
		System.setProperty("webdriver.ie.driver", driverPath );  
		if(driverPath==null){
			tracerLog.addTag("WebDriverIEService initIE RuntimeException", "webdriver.ie.driver 初始化失败！");
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}
		 
		tracerLog.addTag("WebDriverIEService initIE Msg", "webdriver.ie.driver 初始化");
		 
		System.out.println("-------------getWebDriver() 初始化新的WebDriver---------");
		//WebDriver driver = new EventFiringWebDriver(new InternetExplorerDriver(ieCapabilities)).register(new LogEventListener());
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
	 
		Set<String> handlers = driver.getWindowHandles();
		for(String handler:handlers){
			System.out.println("handler-------------"+handler);
		}
		return driver;
	}
	 
	
	public WebDriver getPage(WebDriver driver,String url) throws NoSuchWindowException{
		System.out.println("getPage:"+url);
		tracerLog.addTag("getPage", url); 
		//driver = getWebDriver(); 
		driver.get(url);
		return driver;
	}
	
	public String getCurrentUrl(WebDriver driver){
		return  driver.getCurrentUrl();
	}
	
	/*
	public WebDriver getPageByChrome(String url){
		
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

	
	public void clickButtonByDomId(String domId)  throws NoSuchWindowException{ 
		webDriver.findElement(By.id(domId)).click();
	}
	
	 */
 
	

}
