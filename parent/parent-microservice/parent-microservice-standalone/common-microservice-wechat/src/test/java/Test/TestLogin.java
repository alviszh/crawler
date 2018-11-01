package Test;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class TestLogin {

	static String driverPath = "D:\\ChromeDriver1\\chromedriver.exe";
	// private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static WebDriver driver;
	public static void main(String[] args) throws Exception {
		
		WebDriver driver = intiChrome();
		// 设置超时时间界面加载和js加载
		String baseUrl = "https://wx.qq.com/";
		driver.get(baseUrl);
//		driver.manage().window().maximize();
		String json = driver.getPageSource();
		System.out.println(json);
		
		String[] split = json.split("/head");
//		System.out.println(split[0]);
		String[] split2 = split[0].split("head");
//		System.out.println(split2[1]);
		String substring = split2[1].substring(23, 156);
//		System.out.println(substring);
		String replace = substring.replace("amp;", "");
		System.out.println(replace);
		String replaceAll = replace.replaceAll("tip=1", "tip=1");
		System.out.println(replaceAll);
		
		
		 WebDriverWait wait=new WebDriverWait(driver, 15);
		 WebElement qrCodeImg= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.className("qrcode")); 
			            } 
			        });
		Thread.sleep(6000);
		 wait=new WebDriverWait(driver, 15);
		 WebElement search_bar= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.id("search_bar")); 
			            } 
			        });
		
		 
		String a="https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=xxx&tip=1";
		
			 
			 
			 
		if(driver.getPageSource().contains("account.NickName"))
		{
//			System.out.println("333333333333333"+driver.getPageSource());
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			for (org.openqa.selenium.Cookie cookie : cookies) {
				Cookie cookieWebClient = new Cookie("", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			WebRequest webRequest = new WebRequest(new URL(replaceAll), HttpMethod.POST);
			Page page2 = webClient.getPage(webRequest);
			System.out.println(page2.getWebResponse().getContentAsString());
			String substring2 = page2.getWebResponse().getContentAsString().substring(38, page2.getWebResponse().getContentAsString().length()-2);
			
			substring2+="&fun=new&version=v2";
			System.out.println("substring2"+substring2);                                   
			webRequest = new WebRequest(new URL(substring2), HttpMethod.POST);
			Page page3 = webClient.getPage(webRequest);
			System.out.println("wechat1234"+page3.getWebResponse().getContentAsString());
			
			int indexOf = page3.getWebResponse().getContentAsString().indexOf("<skey>");
			int indexOf2 = page3.getWebResponse().getContentAsString().indexOf("</skey>");
			String substring3 = page3.getWebResponse().getContentAsString().substring(indexOf, indexOf2);
			
			int indexOf3 = page3.getWebResponse().getContentAsString().indexOf("<pass_ticket>");
			int indexOf4 = page3.getWebResponse().getContentAsString().indexOf("</pass_ticket>");
			String substring4 = page3.getWebResponse().getContentAsString().substring(indexOf3, indexOf4);
			
			int indexOf5 = page3.getWebResponse().getContentAsString().indexOf("<wxsid>");
			int indexOf6 = page3.getWebResponse().getContentAsString().indexOf("</wxsid>");
			String substring5 = page3.getWebResponse().getContentAsString().substring(indexOf5, indexOf6);
			
			
			String url1="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?pass_ticket="+substring4+"&r=1539670736426&seq=0&skey="+substring3;
			webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page page4 = webClient.getPage(webRequest);
			System.out.println("++++++++++++++4页面+++++++++++++++++"+page4.getWebResponse().getContentAsString());
			
			String url2="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact?type=ex&r=1540866669193&pass_ticket="+substring4;
			webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page page5 = webClient.getPage(webRequest);
			System.out.println("++++++++++++++5页面+++++++++++++++++"+page5.getWebResponse().getContentAsString());
			                                                                 
			String url3="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid="+substring5+"&skey="+substring3+"&pass_ticket="+substring4;
			webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page page6 = webClient.getPage(webRequest);
			System.out.println("++++++++++++++6页面+++++++++++++++++"+page6.getWebResponse().getContentAsString());
			
//			String url3="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetheadimg?seq=671129551&username=@@4440ac249086de20bbd17217e996f894a199f3135d831f146d0c30a47152ed4a&skey="+substring3;
//			webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
//			webRequest.setCharset(Charset.forName("UTF-8"));
//			Page page6 = webClient.getPage(webRequest);
//			System.out.println("++++++++++++++6页面+++++++++++++++++"+page6.getWebResponse().getContentAsString());
		}
		
	}

	public static WebDriver intiChrome() throws Exception {
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		return driver;
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
		}
	}

}
