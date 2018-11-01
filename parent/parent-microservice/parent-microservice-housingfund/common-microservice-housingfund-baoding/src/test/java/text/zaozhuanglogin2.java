package text;

import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.service.ChaoJiYingOcrService;

public class zaozhuanglogin2 {
	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	public static void main(String[] args) {
		try {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);
		String url = "http://www.zzzfgjj.com/wt-web/login";
		driver.get(url);
		Thread.sleep(2000);
		
		String user = "37040519870324134X";
		driver.findElement(By.id("username_sfz")).sendKeys(user);
		Thread.sleep(2000);
		driver.findElement(By.id("a001")).click();
		Thread.sleep(2000);
		String pass = "675725";
		driver.findElement(By.id("password")).sendKeys(pass);
		Thread.sleep(2000);
		String path = WebDriverUnit.saveImg(driver, By.xpath("//img[@src='/wt-web/captcha']"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
				path); // 1005
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		Thread.sleep(2000);
		driver.findElement(By.id("captcha")).sendKeys(code);
		
		driver.findElement(By.xpath("//button[@onclick='submitForm();']")).click();
		Thread.sleep(5000);
		
		
		String url2 = "http://www.zzzfgjj.com/wt-web/personal/jcmxlist?"
				+"UserId=1&beginDate=2017-01-01&endDate=2017-12-25&userId=1&pageNum=1&pageSize=10";
		driver.get(url2);
		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
