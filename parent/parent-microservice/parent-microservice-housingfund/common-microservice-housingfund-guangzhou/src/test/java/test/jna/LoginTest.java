package test.jna;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class LoginTest extends AbstractChaoJiYingHandler{

	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	// static String driverPath = "C:\\Program Files\\Internet
	// Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {

		//VirtualKeyBoard.KeyPressEx("201710WAN",50);// 201710WAN
		
	    login();
		 
		//VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Delete"));//a1212.
		
		 
	}
	
	public static void login()  throws  Exception{

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		// ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
		// true);

		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://gzgjj.gov.cn/wsywgr/";
		driver.get(baseUrl);
	
		
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("input[onclick='changeCert(1)']")).click();
		InputTab(); //     
		VirtualKeyBoard.KeyPressEx("36220219910102442200",600);//貌似不能模拟输入中文    
		
		InputTab(); //     
		VirtualKeyBoard.KeyPressEx("699192",600);//  
		
		
		InputTab(); //  
		String path = WebDriverUnit.saveImg(driver, By.id("safecode")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code);
	
		
		
		driver.findElement(By.id("captcha")).sendKeys(code);    
		
		Thread.sleep(1000L);
		
//		driver.findElement(By.cssSelector("img[onclick='doLogin()']")).click();
//		driver.findElement(By.cssSelector("img[onclick='doLogin()']")).click();
		
		
		String url="https://gzgjj.gov.cn/wsywgr/CheckAction!createYZM.parser?d=1508223205102";
		WebClient webClient = WebCrawler.getInstance().getWebClient();// 
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);   
		requestSettings.setAdditionalHeader("Host", "gzgjj.gov.cn");
		requestSettings.setAdditionalHeader("Referer", "https://gzgjj.gov.cn/wsywgr/");
		Page page = webClient.getPage(requestSettings);
		System.out.println("获取到的验证码刷新后的页面是："+page.getWebResponse().getContentAsString());

	}
	
	 
	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

}
