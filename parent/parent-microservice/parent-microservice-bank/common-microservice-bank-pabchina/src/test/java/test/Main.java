package test;

import java.awt.Robot;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.jna.winio.WinIOAPI;

public class Main {
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static Robot robot;

	public static void main(String[] args) throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();

		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://www.pingan.com.cn/pinganone/pa/index.screen";
		driver.get(baseUrl);

		Thread.sleep(1000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

		String pageSource = driver.getPageSource();
		System.out.println(pageSource);

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("j_username"));
			}
		});
		username.click();
		username.clear();
		// username.sendKeys("532128199011123339");//一账通（既是储蓄卡又是信用卡）
		// username.sendKeys("15260899261");//信用卡（获取不到数据）
		// username.sendKeys("440181199109144259");//信用卡
		// username.sendKeys("15970479472");//信用卡 （获取不到数据）
		// username.sendKeys("362421198808133214");//信用卡
		// username.sendKeys("15635366806");//信用卡 （没有信用卡）
		username.sendKeys("18740376499");// 信用卡 （没有信用卡）

		driver.findElement(By.id("key1")).click();
		Thread.sleep(1000L);
		driver.findElement(By.className("kb-tl-switch")).click();

		// String password = "ddhs2018";
		// String password = "zlx615312";
		// String password = "jiani13760079232";
		// String password = "Zs501249";
		// String password = "293664";
		// String password = "HH901103";
		String password = "dengwei82";
		KeyPressEx(password, 500);

		String path = WebDriverUnit.saveImg(driver, By.id("validateImg"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
																														// 1~5位英文数字
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);

		driver.findElement(By.id("check_code")).sendKeys(code);

		driver.findElement(By.id("loginlink")).click();
		Thread.sleep(10000);
		String source = driver.getPageSource();

		System.out.println(source);

		if (source.contains("<title>中国平安-一账通</title>")) {
			System.out.println("登陆成功！");

			WebClient webClient = WebCrawler.getInstance().getWebClient();
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
						"www.pingan.com.cn", cookie.getName(), cookie.getValue()));
			}

			// 获取一个账号所有的卡片信息
			String loginurl4q = "https://www.pingan.com.cn/pinganone/pa/searchBindList.do";
			WebRequest webRequestlogin4q;
			webRequestlogin4q = new WebRequest(new URL(loginurl4q), HttpMethod.POST);
			webRequestlogin4q.setAdditionalHeader("Referer",
					"https://www.pingan.com.cn/pinganone/pa/searchNewHeaderService.do");
			webRequestlogin4q.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequestlogin4q.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestlogin4q.setAdditionalHeader("Accept", "*/*");
			webRequestlogin4q.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequestlogin4q.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestlogin4q.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequestlogin4q.setAdditionalHeader("Host", "www.pingan.com.cn");
			webRequestlogin4q.setAdditionalHeader("Connection", "Keep-Alive");
			webRequestlogin4q.setAdditionalHeader("Cache-Control", "no-cache");
			Page pagelogin2q = webClient.getPage(webRequestlogin4q);
			String contentAsString2q11 = pagelogin2q.getWebResponse().getContentAsString();
			System.out.println(contentAsString2q11);

			if (contentAsString2q11.contains("creditcard")) {
				System.out.println("有信用卡！");
			} else {
				System.out.println("没有信用卡！");
				String loginurl4q11 = "https://www.pingan.com.cn/pinganone/pa/newFace.do";
				WebRequest webRequestlogin4q11;
				webRequestlogin4q11 = new WebRequest(new URL(loginurl4q11), HttpMethod.GET);
				HtmlPage pagelogin2q11 = webClient.getPage(webRequestlogin4q11);
				DomElement elementById = pagelogin2q11.getElementById("name");
				String xm = elementById.getAttribute("value").trim();
				System.out.println("姓名---"+xm);
				
				DomElement elementById1 = pagelogin2q11.getElementById("birthDate");
				String csrq = elementById1.getAttribute("value").trim();
				System.out.println("出生日期---"+csrq);
				
				DomElement elementById2 = pagelogin2q11.getElementById("email");
				String dzyx = elementById2.getAttribute("value").trim();
				System.out.println("电子邮箱---"+dzyx);
				
			}
		} else {
			System.out.println("登陆失败！");
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

	// 虚拟键盘
	// 支持英文大小写和特殊字符，自定义休眠时间
	public static void KeyPressEx(String s, long sleeptime) throws Exception {
		Thread.sleep(1500L);
		for (int i = 0; i < s.length(); i++) {
			char chr = s.charAt(i);
			Integer is = VKMapping.getMapValue("" + chr);
			if (is != null) {
				KeyPress(VKMapping.toScanCode("" + s.charAt(i)));//
			} else {
				KeyPressUppercase(VKMapping.toScanCodeUppercase("" + s.charAt(i)));//
			}
			Thread.sleep(sleeptime);
		}
	}

	public static void KeyPress(int key) throws Exception {
		KeyDown(key);
		KeyUp(key);
	}

	public static void KeyPressUppercase(int key) throws Exception {
		KeyDown(VKMapping.toScanCode("Shift"));
		KeyPress(key);
		KeyUp(VKMapping.toScanCode("Shift"));
	}

	public static void KeyDown(int key) throws Exception {
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT, 0xd2, 1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT, key, 1);
	}

	public static void KeyUp(int key) throws Exception {
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT, 0xd2, 1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT, (key | 0x80), 1);

	}
}
