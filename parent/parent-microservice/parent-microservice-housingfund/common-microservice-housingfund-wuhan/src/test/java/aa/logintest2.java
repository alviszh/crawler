package aa;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

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
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
public class logintest2 {

	
	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	
	public static void main(String[] args) {
		
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);
		try {
		String url = "https://whgjj.hkbchina.com/portal/pc/login.html";
		driver.get(url);
		Thread.sleep(2000);
		
		String LoginId = "420106199011238421";
		driver.findElement(By.id("LoginId")).sendKeys(LoginId);
		robot = new Robot();
		//密码
		robot.keyPress(KeyEvent.VK_TAB);
		String password = "12qwaszx";
		VirtualKeyBoard.KeyPressEx(password, 50);
		Thread.sleep(2000);
		
		String path = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
				path); // 1005
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		Thread.sleep(2000);
		driver.findElement(By.id("_vTokenName")).sendKeys(code);
		
		Thread.sleep(5000);
		List<WebElement> findElements = driver.findElements(By.xpath("//div[@class = 'loginBtn']"));
		for (WebElement webElement : findElements) {
			webElement.click();
			webElement.click();
		}
		
		Thread.sleep(5000);
		
		Set<Cookie> cookies2 = driver.manage().getCookies();
		for (Cookie cookie2 : cookies2) {
			System.out.println(cookie2.getName() + "-------cookies--------" + cookie2.getValue());
			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("whgjj.hkbchina.com",
					cookie2.getName(), cookie2.getValue()));
		}
		String cookieString = CommonUnit.transcookieToJson(webClient);
		Set<com.gargoylesoftware.htmlunit.util.Cookie> set = CommonUnit.transferJsonToSet(cookieString);
		Iterator<com.gargoylesoftware.htmlunit.util.Cookie> j = set.iterator();
		while(j.hasNext()){
			webClient.getCookieManager().addCookie(j.next());
		}
		String url5 = "https://whgjj.hkbchina.com/portal/GJJAcctInfoQry.do?AcctNo=123";
	//	String url5 = "https://whgjj.hkbchina.com/portal/pc/htmls/AccountInfo/UserInfo/UserInfoMod.html";
//		List<NameValuePair> paramsList = new ArrayList();
//		paramsList.add(new NameValuePair("AcctNo", "123"));
//		webClient.addRequestHeader("Accept", "application/json, text/plain, */*");
//		webClient.addRequestHeader("Host", "whgjj.hkbchina.com");
//		webClient.addRequestHeader("Referer", "https://whgjj.hkbchina.com/portal/pc/main.html");
//		webClient.addRequestHeader("Content-Type", "application/json;charset=utf-8");
//		webClient.addRequestHeader("$Referer", "/");
//		webClient.addRequestHeader("Accept-Language", "zh-CN");
//		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate");
//		webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
//		webClient.addRequestHeader("DNT", "1");
//		webClient.addRequestHeader("Connection", "Keep-Alive");
//		webClient.addRequestHeader("Cache-Control", "no-cache");
		
		Page page2 = LoginAndGetCommon.getHtml(url5, webClient);
//		Page page2 = gethtmlPost(webClient, paramsList, url5);
		String html = page2.getWebResponse().getContentAsString();
		
		
		
		System.out.println(html);
		
		
		
		
		
		
		
//	selenium	
		
		
		
//		
//		WebElement webElement = driver.findElement(By.xpath("//li[@class = 'FundDeposit']"));
//		webElement.click();
		
//		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//		WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
//			public WebElement apply(WebDriver driver) {
//				return driver.findElement(By.className("fundInfo"));
//			}
//		});
		
//		WebElement findElement = driver.findElement(By.className("fundInfo"));
//		
//		findElement.click();
//		
//		String html = driver.getPageSource();
//		System.out.println(html);
		
		
		
		
		
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Host", "whgjj.hkbchina.com");
		webRequest.setAdditionalHeader("Referer", "https://whgjj.hkbchina.com/portal/pc/main.html");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("$Referer", "/");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		webRequest.setAdditionalHeader("DNT", "1");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
//		webRequest.setAdditionalHeader("Cookie","");
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

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

	public static void save1(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int bytesWritten = 0; 
		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
		outputStream.write(bytes, bytesWritten, byteCount); 
		bytesWritten += byteCount;
		} 
		inputStream.close(); 
		outputStream.close(); 
		}
	public static void savefile(String filePath, String fileTxt) throws Exception{ 
		File fp=new File(filePath); 
		PrintWriter pfp= new PrintWriter(fp); 
		pfp.print(fileTxt); 
		pfp.close(); 
		}
}
