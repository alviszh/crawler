package text;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.module.jna.webdriver.WebDriverUnit;
@EnableRetry
public class test {
	private static WebDriver driver;
	static String LoginPage = "https://personalbank.cib.com.cn/pers/main/login.do";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String htmlsource1 = html();
		
	}
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public static String html() throws Exception{
		final WebDriver webDriver = openloginCmbChina();
		webDriver.findElement(By.cssSelector("label[for='logintype1']")).click();
		Thread.sleep(1000);
		webDriver.findElement(By.id("loginNameTemp")).sendKeys("15117963346");//手机号
		Scanner input = new Scanner(System.in);
		System.out.print("请输入：");
        String val = input.next();
		webDriver.findElement(By.id("mobilecaptchafield")).sendKeys(val);//验证码
		Thread.sleep(1000);
		webDriver.findElement(By.id("btnSendSms")).click();
		Thread.sleep(2000);
		String htmlsource1 = webDriver.getPageSource();
		if(htmlsource1.contains("验证码不能为空")){
			System.out.println("请输入正确的附加码");
			webDriver.quit();
			throw new RuntimeException("请输入正确的附加码");
			
		}else {
			
			String htmlsource2 = webDriver.getPageSource();
			Document doc = Jsoup.parse(htmlsource2);
			String eles = doc.select("#sms-lefttime").text();
			if (eles.contains("已发送")) {
				
				System.out.println("获取短信成功");
				
			}else {
				System.out.println("获取短信失败");
			}
		}
		return htmlsource1;
	}
	
    public static WebDriver openloginCmbChina()throws Exception{ 
		
		//driver.manage().window().maximize();
		
			System.out.println("launching chrome browser");
			System.setProperty("webdriver.chrome.driver", "D:\\zhaohui\\chromedriver.exe");
			try {
			
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("disable-gpu"); 

			driver = new ChromeDriver(chromeOptions);
			 
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			
			
				//driver.manage().window().maximize();
				driver.get(LoginPage);
				return driver;
			} catch (Exception e) {
				System.out.println("网络超时");
				
			}
			return null;
		
		
	} 

}
