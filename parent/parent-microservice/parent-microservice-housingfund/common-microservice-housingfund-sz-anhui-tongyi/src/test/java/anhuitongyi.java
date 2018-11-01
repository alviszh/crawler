import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.gson.Gson;


/**   
*    
* 项目名称：common-microservice-housingfund-sz-anhui-tongyi   
* 类名称：anhuitongyi   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年4月24日 下午3:41:05   
* @version        
*/
public class anhuitongyi {

	private static String baseUrl = "https://sso.ahzwfw.gov.cn/uccp-server/login";
	static String driverPath = "C:\\chromedriver.exe";

	static Boolean headless = false;

	protected static Gson gs = new Gson();

	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static String loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("perName"));
			}
		});

		
		loginname.sendKeys("15856785648");
		driver.findElement(By.id("perPsd")).sendKeys("y15856785648");

		driver.findElement(By.id("psdBtn")).click();
		Thread.sleep(5000);
		System.out.println("sucess");

		
		return null;
	}

	
	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
