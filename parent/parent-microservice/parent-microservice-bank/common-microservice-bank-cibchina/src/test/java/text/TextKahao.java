package text;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TextKahao {
	private static WebDriver driver;
	static String LoginPage = "https://personalbank.cib.com.cn/pers/main/login.do";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final WebDriver webDriver = openloginCmbChina();
		Thread.sleep(1000);
		webDriver.findElement(By.cssSelector("label[for='logintype1']")).click();
		Thread.sleep(1000);
		webDriver.findElement(By.id("loginNameTemp")).sendKeys("6250851012955101");//卡号
		Thread.sleep(1000);
		webDriver.findElement(By.id("loginNextBtn")).click();
		String htmlsource2 = null;
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		boolean a=pattern.matcher("6250851012955101").matches();
		if("6250851012955101".length()>9&&a ==true){
			htmlsource2 = webDriver.getPageSource();
		}else{
			htmlsource2 = "银行账号不能少于10位！";
		}
		Document doc = Jsoup.parse(htmlsource2);
		String eles = doc.select("div.cib-dialog-msg > label").text();
		System.out.println(eles);
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
