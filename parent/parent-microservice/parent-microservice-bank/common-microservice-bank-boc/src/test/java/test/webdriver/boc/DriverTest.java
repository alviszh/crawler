package test.webdriver.boc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverTest {

	public static void main(String[] args) { 
		String driverPath = "C:\\chromedriver.exe";
//		String driverPath2 = "F:\\chromedriver2.32\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1600,900");
		
		//chromeOptions.addArguments("--disable-gpu"); 
		  
		WebDriver driver = new ChromeDriver(chromeOptions);
		String baseUrl = "http://news.qq.com/articleList/rolls/";
		driver.get(baseUrl);
		//driver.manage().window().maximize(); 
		//String currentUrl = driver.getCurrentUrl();
		String htmlsource = driver.getPageSource();
		System.out.println("htmlsource--11--"+htmlsource); 
		driver.quit();
	}

}
