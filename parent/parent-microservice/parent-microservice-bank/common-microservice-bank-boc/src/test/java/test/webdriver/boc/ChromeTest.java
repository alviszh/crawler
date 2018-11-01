package test.webdriver.boc;

import java.util.Collection;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeTest {

	public static void main(String[] args)    throws  Exception{
		WebDriver driver = intiChrome() ;
		//RemoteWebDriver driver = new RemoteWebDriver(new URL("http://10.167.202.218:32768//wd/hub/"), DesiredCapabilities.chrome());
	    //WebDriver driver = new RemoteWebDriver(new URL("http://10.167.202.218:32768//wd/hub/"), DesiredCapabilities.chrome());
		//driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		//driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://news.qq.com/articleList/rolls/";
		driver.get(baseUrl);
		//driver.manage().window().maximize(); 
		//String currentUrl = driver.getCurrentUrl();
		String htmlsource = driver.getPageSource();
		System.out.println("htmlsource--11--"+htmlsource); 
		
		
		Collection<Cookie> cookies = driver.manage().getCookies();
		
		//driver.manage().addCookie(cookie);
		//driver.getSessionId().toString();
		for(Cookie cookie:cookies){
			System.out.println("cookie---------"+cookie.toString());
		}
		
		driver.quit();
	}
	
	public static WebDriver intiChrome()   throws  Exception{
		String driverPath = "C:\\chromedriver.exe";
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
		//WebDriver driver = new ChromeDriver();  
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver; 
	}

}
