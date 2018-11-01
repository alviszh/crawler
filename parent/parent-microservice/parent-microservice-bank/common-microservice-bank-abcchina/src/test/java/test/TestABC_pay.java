package test;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class TestABC_pay {
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static Robot robot;
	public static void main(String[] args)  throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();

		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://perbank.abchina.com/EbankSite/startup.do";
		driver.get(baseUrl);
//		driver.manage().window().maximize();

		Thread.sleep(2000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("username"));
			}
		});

		username.click();
		username.clear();
		username.sendKeys("6228480010622143419");

		/**
		 * DD模拟输入（安装板）
		 */
//		VK.Tab();
//		VK.KeyPress("201710WAN");
		
		/**
		 * winio模拟输入
		 */
//		InputTab();
//
//		String password = "201710WAN";
//		VirtualKeyBoard.KeyPressEx(password, 50);

		String path = WebDriverUnit.saveImg(driver, By.id("vCode"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
																														// 1~5位英文数字
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);

		driver.findElement(By.id("code")).sendKeys(code);

		driver.findElement(By.id("logo")).click();

		Thread.sleep(2000);

		String pageSource = driver.getPageSource();
		
		System.out.println(pageSource);
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
//		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
//
//		for (org.openqa.selenium.Cookie cookie : cookies) {
//			System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
//			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("perbank.abchina.com",
//					cookie.getName(), cookie.getValue()));
//		}
//
//		String loginurl = "https://perbank.abchina.com/EbankSite/AccountTradeDetailQueryAct.do";
//		WebRequest webRequestlogin;
//		webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
//		webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
//		webRequestlogin.getRequestParameters().add(new NameValuePair("trnStartDt", "20170731"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("trnEndDt", "20171031"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("acctId", "6228480010622143419"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("acctType", "401"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("acctName", "万锦峰"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("acctOpenBankId", "30949"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("provCode", "11"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("busCode", "200002"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("oofeFlg", "0"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("acctCurCode", "156"));
//		webRequestlogin.getRequestParameters().add(new NameValuePair("nextPageKey", ""));
//		Thread.sleep(5000);
//		Page pagelogin = webClient.getPage(webRequestlogin);
//
//		String contentAsString = pagelogin.getWebResponse().getContentAsString();
//
//		System.out.println(contentAsString);

		
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

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void keyPress(Robot r, int key) {
		r.keyPress(key);
		r.keyRelease(key);
		r.delay(100);
	}

	public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
		}
	}
}
