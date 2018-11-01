package app.test;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class GuangzhouTest {
	// static String driverPath = "D:\\IEDriverServer_x64\\IEDriverServer.exe";
	static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	//为了抽出验证码错误，重试的方法，将driver设置为静态对象
	private static WebDriver driver;
	
	public static void main(String[] args) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
//		WebDriver driver = new InternetExplorerDriver();
		
		driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://gzgjj.gov.cn/wsywgr/";
		driver.get(baseUrl);

		// Thread.sleep(2000L);//这里需要休息2秒，不然点击事件可能无法弹出登录框

		// 用户名：//*[@id="certno"]

		// 密码：//*[@id="password"]
		String password = "699192";
		VirtualKeyBoard.KeyPressEx(password, 50);

		InputTab(); // 敲6下tab到用户名那里
		InputTab();
		InputTab();
		InputTab();
		InputTab();
		InputTab();

		String username = "36220219910102442200";
		VirtualKeyBoard.KeyPressEx(username, 50);
		// username.sendKeys("36220219910102442200");

		String path = WebDriverUnit.saveImg(driver, By.id("safecode"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
																														// 1~5位英文数字
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code验证码是： ====>>" + code);

		InputTab(); // 第8下到验证码那里
		InputTab();
		driver.findElement(By.id("captcha")).sendKeys(code);


		// 点击登录按钮

		// 之前的写法：
		// driver.findElements(By.tagName("img")).get(1).click(); //这样写不行

		// 先获取集合，再定位到登录部分就可以
		// List<WebElement> findElements =
		 List<WebElement> findElements = driver.findElements(By.tagName("img"));
		 for (WebElement webElement : findElements) {
			 System.out.println("获取的每一个图片对象是："+webElement);
		 }
		 //该网站有时候点击一次登录不起作用，故后期决定设置为用是否没有相关错误，且进入了主页面的方式来决定是否还要执行findElements.get(1).click();
		 findElements.get(1).click();
		 String html = driver.getPageSource();
		 System.out.println("获取到的页面内容是1："+html);
		 Document doc = Jsoup.parse(html);
		 Element elementErrorMsg= doc.getElementById("actionmessage");
		 if(elementErrorMsg.text().contains("验证码错误")){
			 System.out.println("验证码错误");
		 }
		 if(elementErrorMsg.text().contains("公积金账号或证件号不存在")){
			 System.out.println("公积金账号或证件号不存在");
		 }
		 //相关信息输入有误，请重试！
		 
		 findElements.get(1).click();
		 html = driver.getPageSource();
		 System.out.println("获取到的页面内容是2："+html);

		 Thread.sleep(5000);   	 //等待跳转，看看登录后的页面是不是首页面
	
	     String currentUrl = driver.getCurrentUrl();  
	     System.out.println("登录之后获取的首页面的链接是："+currentUrl);
	     
	     if("https://gzgjj.gov.cn/wsywgr/index.jsp".equals(currentUrl)){
	    	 System.out.println("欢迎来到首页面");
	     }else{
	    	 System.out.println("相关信息输入有误，请重试！");
	     }
	     
//	     Thread.sleep(5000);   	 //等待跳转，看看登录后的页面是不是首页面
		// 获取cookies
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
		for (org.openqa.selenium.Cookie cookie : cookies) {
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson);
		}

		String cookieJson = new Gson().toJson(cookiesSet);
		System.out.println(cookieJson);
		
		//=============================================================
		
		WebClient webClient = WebCrawler.getInstance().getWebClient();//  
		
		for(org.openqa.selenium.Cookie cookie:cookies){
			System.out.println(cookie.getName()+"---------------"+cookie.getValue());
			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("gzgjj.gov.cn",cookie.getName(),cookie.getValue()));
		}
		String url2="https://gzgjj.gov.cn/wsywgr/TQAction!getDetailInfo.parser";
		WebRequest webRequestwdzl2 = new WebRequest(new URL(url2), HttpMethod.GET);
		webRequestwdzl2.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		webRequestwdzl2.setAdditionalHeader("Referer", "https://gzgjj.gov.cn/wsywgr/index.jsp");
		webRequestwdzl2.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequestwdzl2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		webRequestwdzl2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequestwdzl2.setAdditionalHeader("Host", "gzgjj.gov.cn");
		webRequestwdzl2.setAdditionalHeader("Connection", "Keep-Alive");
		webRequestwdzl2.setAdditionalHeader("Cache-Control", "no-cache");
		Page wdzl2 = webClient.getPage(webRequestwdzl2);
		System.out.println("最终需要的页面是："+wdzl2.getWebResponse().getContentAsString());
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
	
	
	//由于该网站在出现任何登录错误信息提示之后，都会将输入框清空，故程序中没有涉及到清空操作，如下方法是刷新验证码，并重新登录
	public static void retryLogin() throws Exception{
		String password = "699192";
		VirtualKeyBoard.KeyPressEx(password, 50);

		InputTab(); // 敲6下tab到用户名那里
		InputTab();
		InputTab();
		InputTab();
		InputTab();
		InputTab();

		String username = "36220219910102442200";
		VirtualKeyBoard.KeyPressEx(username, 50);
		// username.sendKeys("36220219910102442200");

		String path = WebDriverUnit.saveImg(driver, By.id("safecode"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
																														// 1~5位英文数字
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code验证码是： ====>>" + code);

		InputTab(); // 第8下到验证码那里
		InputTab();
		driver.findElement(By.id("captcha")).sendKeys(code);


		// 点击登录按钮

		// 之前的写法：
		// driver.findElements(By.tagName("img")).get(1).click(); //这样写不行

		// 先获取集合，再定位到登录部分就可以
		// List<WebElement> findElements =
		 List<WebElement> findElements = driver.findElements(By.tagName("img"));
		 for (WebElement webElement : findElements) {
			 System.out.println("获取的每一个图片对象是："+webElement);
		 }
		 //该网站有时候点击一次登录不起作用，故后期决定设置为用是否没有相关错误，且进入了主页面的方式来决定是否还要执行findElements.get(1).click();
		 findElements.get(1).click();
		 String html = driver.getPageSource();
		 System.out.println("获取到的页面内容是1："+html);
		 Document doc = Jsoup.parse(html);
		 Element elementErrorMsg= doc.getElementById("actionmessage");
		 if(elementErrorMsg.text().contains("验证码错误")){
			 System.out.println("验证码错误");
		 }
	}
}
