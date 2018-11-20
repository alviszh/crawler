package app.service;

import app.commontracerlog.TracerLog;
import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;


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

    public WebDriver getNewWebDriver(HttpProxyRes httpProxyRes){

        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

        if (httpProxyRes != null) {
            Proxy proxy = new Proxy();
            String PROXY = ""+httpProxyRes.getIp()+":"+httpProxyRes.getPort()+"";
            proxy.setHttpProxy(PROXY);
            proxy.setFtpProxy(PROXY);
            proxy.setSslProxy(PROXY);
            ieCapabilities.setCapability(CapabilityType.PROXY, proxy);
        }

        System.setProperty("webdriver.ie.driver", driverPath );
        if(driverPath==null){
            tracerLog.qryKeyValue("WebDriverIEService initIE RuntimeException", "webdriver.ie.driver 初始化失败！");
            throw new RuntimeException("webdriver.ie.driver 初始化失败！");
        }

        tracerLog.qryKeyValue("WebDriverIEService initIE Msg", "webdriver.ie.driver 初始化");

        System.out.println("-------------getWebDriver() 初始化新的WebDriver---------"+driverPath);
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
	 
/*
	public void clearWebDriver(){
		driver = null;
	}
	
	public void quitWebDriver(){
		driver.quit();
	}
	*/

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
			tracerLog.qryKeyValue("WebDriverIEService initChrome RuntimeException", "webdriver.chrome.driver 初始化失败！");
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
