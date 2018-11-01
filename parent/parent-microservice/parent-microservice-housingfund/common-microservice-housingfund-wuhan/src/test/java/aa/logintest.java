package aa;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangShiyeInfoqqq;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class logintest {
	// static String driverPath = "D:\\IEDriverServer_x64\\IEDriverServer.exe";
	//https://whgjj.hkbchina.com/portal/sendMsgRfdsLogin.do    发送验证码地址
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

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://www.cebbank.com/per/prePerlogin.do?_locale=zh_CN";
		driver.get(baseUrl);
		Thread.sleep(2000L);
		
		driver.findElement(By.id("skey")).sendKeys("123448");
		Thread.sleep(2000L);
		robot = new Robot();
		//密码
		robot.keyPress(KeyEvent.VK_TAB);
		//huangwei1987924
		String password = "huangwei1987924";
		VirtualKeyBoard.KeyPressEx(password, 50);
		Thread.sleep(2000L);
		//shouzhi
		WebElement element = driver.findElement(By.xpath("//img[@class = 'shouzhi']"));
		element.click();
		
//		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//		WebElement login_person = wait.until(new Function<WebDriver, WebElement>() {
//			public WebElement apply(WebDriver driver) {
//				return driver.findElement(By.id("image3"));
//			}
//		});
		
		
//		System.out.println("获取到login_person---->" + login_person.getText());
		
//		Thread.sleep(2000L);
		 // 这里需要休息2秒，不然点击事件可能无法弹出登录框
//		login_person.click();
//		Thread.sleep(1000L);
//		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			//身份证 帐号
//		Thread.sleep(2000L);
//			
//			String LoginId = "420107198709061535";
//			VirtualKeyBoard.KeyPressEx(LoginId, 50);
			
			
			Thread.sleep(2000L);
			
//		
//			Set<Cookie> cookies2 = driver.manage().getCookies();
//			WebClient webClient = WebCrawler.getInstance().getWebClient();//
//			
//			for (Cookie cookie2 : cookies2) {
//				System.out.println(cookie2.getName() + "-------cookies--------" + cookie2.getValue());
//				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("https://whgjj.hkbchina.com/portal/pc/login.html",
//						cookie2.getName(), cookie2.getValue()));
//			}
//			//验证码
//			String url2="https://whgjj.hkbchina.com/portal/pc/htmls/LoginContent/loginContent.html";
//			//WebDriver driver2 = new InternetExplorerDriver();
//			HtmlPage page1 = getHtml(url2, webClient);
//			String html1 = page1.getWebResponse().getContentAsString();
//			HtmlImage image = (HtmlImage) page1.getFirstByXPath("//img[@id='_tokenImg']");
//			String code = chaoJiYingOcrService.getVerifycode(image, "1902");
//			
//			System.out.println("11");
//			//验证码
//			/*String path = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
//			System.out.println("path---------------" + path);
//			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG,
//					path); // 1005
//			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
//			Gson gson = new GsonBuilder().create();
//			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//			System.out.println("code ====>>" + code);*/
//			
//			InputTab();
//			VirtualKeyBoard.KeyPressEx(code, 50);
//			
//			Thread.sleep(8000L);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception{

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		return page;

	}
	
}
