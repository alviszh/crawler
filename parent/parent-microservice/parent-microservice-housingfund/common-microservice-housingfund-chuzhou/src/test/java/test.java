import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ChromeDriver driver = intiChrome();
		//driver.manage().window().maximize();
		String url = "http://gjjcx.chuzhou.gov.cn/default.aspx";
		//driver.manage().window().maximize();

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.get(url);
		driver.findElement(By.cssSelector("#DropDownList1 > option:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("TxtName")).sendKeys("1003380");
		Thread.sleep(1000);
		driver.findElement(By.id("TxtIdent")).sendKeys("00044");
		Thread.sleep(1000);
		driver.findElement(By.id("Txt3")).sendKeys("李蓉蓉");
		Thread.sleep(1000);
		driver.findElement(By.id("BtnSearch")).click();
		Thread.sleep(1000*5);
		 try{  
			 Alert alt = driver.switchTo().alert();
		     alt.accept();  
	           
	     }catch (NoAlertPresentException Ex)  {  
	            
	     }     
		String htmlsource2 = driver.getPageSource();
		System.out.println(htmlsource2);
		if(htmlsource2.contains("摘要")){
			System.out.println("1111");
		}else{
			System.out.println("222");
		}
	}
	public static  ChromeDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", "D:\\zhaohui\\chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
}
