package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class driver {
	public  ChromeDriver intiChrome() throws Exception {
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
	
	public static void main(String[] args) {
		String url = "https://bg.gjj.dl.gov.cn/person/logon.jsp";
		WebRequest webRequest = null;;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = null;
		try {
			searchPage = webClient.getPage(webRequest);
			System.out.println(searchPage.asXml());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ChromeDriver driver = intiChrome();
//		driver.manage().window().maximize();
//		String url = "http://www.qzgjj.com/PAFundQuery/Index";
//		driver.manage().window().maximize();
//
//		// 设置超时时间界面加载和js加载
//		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
//		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
//		driver.get(url);
//		driver.findElement(By.id("t_user")).sendKeys(num);
//		Thread.sleep(1000);
//		driver.findElement(By.id("t_pass")).sendKeys(password);
//		Thread.sleep(1000);
//		String code = getVerfiycode(By.id("img_CheckCode_Layout"), driver);
//		driver.findElement(By.id("Tb_CheckCode_Layout")).sendKeys(code);
//		Thread.sleep(1000);
//		driver.findElement(By.id("btn_Login")).click();
//	}
	}
}
