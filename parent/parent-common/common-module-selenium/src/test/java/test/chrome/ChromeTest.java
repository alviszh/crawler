package test.chrome;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeTest {

	public static void main(String[] args) throws Exception {
		WebDriver driver = intiChrome(); 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		 
		 
		
		//第一个参数：表示服务器的地址。第二个参数：表示预期的执行对象，其他的浏览器都可以以此类推
        //WebDriver driver = new RemoteWebDriver(new URL("http://10.167.202.218:32768//wd/hub/"), DesiredCapabilities.chrome());
        //driver.manage().window().maximize();
        //driver.get("http://news.qq.com/articleList/rolls/");
		driver.get("http://www.baidu.com");
        String html = driver.getPageSource();
		
		System.out.println("html-------"+html);
	}
	
	public static WebDriver intiChrome() throws Exception {
		String driverPath = "C:\\chromedriver.exe";//如果想使用Headless Chrome 对Chrome版本有一定的要求，从官方文档我们可以看出，mac和linux环境要求chrome版本是59+，而windows版本的chrome要求是60+，同时chromedriver要求2.30+版本
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions(); 
		// 设置为 headless 模式 （必须）
		chromeOptions.addArguments("--headless");
		//chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
		 
	}

}
