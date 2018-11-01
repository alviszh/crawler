package test;

import java.awt.Robot;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ddxoft.VK;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.jna.winio.WinIOAPI;

public class Bohc_base {
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception {
		/////////////////////////////////////////////// 登录接口///////////////////////////////////////////////////
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();

		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://ebank.cbhb.com.cn/pWeb/static/login.html";
		driver.get(baseUrl);
		driver.manage().window().maximize();

		Thread.sleep(5000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("LoginName"));
			}
		});
		username.click();
		
		username.clear();
		username.sendKeys("6214530105265917");
		Thread.sleep(2000);
		/*
		 * DD模拟输入（安装版）
		 */
		driver.findElement(By.id("loginpass")).sendKeys(Keys.ENTER);		
		VK.KeyPress("216832");
		/*
		 * Winio模拟输入
		 */
//		InputTab();
//
//		String password = "216832";
//		KeyPressEx(password, 100);

		String path = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
																														// 1~5位英文数字
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);

		driver.findElement(By.id("_tokenName")).sendKeys(code);

		driver.findElement(By.xpath("//input[@type='submit']")).click();

		Thread.sleep(10000);

		String source = driver.getPageSource();

		System.out.println(source);

		if (source.contains("用户名或密码错误") || source.contains("登录密码输入错误：输入长度不足") || source.contains("登录密码输入错误：输入内容不合规")
				|| source.contains("内容不能为空")) {
			System.out.println("登陆失败！用户名或密码错误!");

		} else if (source.contains("验证码错误")) {
			System.out.println("登陆失败！验证码错误!");

		} else {
			try {
				WebElement TrsPwdReset2PreloginPasswordDiv = driver
						.findElement(By.id("TrsPwdReset2PreloginPasswordDiv"));
				TrsPwdReset2PreloginPasswordDiv.click();
				System.out.println("很抱歉！您未设置交易密码！设置完之后再来操作！谢谢配合！");
			} catch (Exception e) {
				System.out.println("不需要统一交易密码！");
			}
			try {
				
				WebElement Answer1 = driver.findElement(By.xpath("//input[@name='Answer1']"));
				Answer1.click();
				System.out.println("很抱歉！您未设置安全问题！设置完之后再来操作！谢谢配合！");
			} catch (Exception e) {
				System.out.println("不需要设置完全问题！");
			}
			if(source.contains("您上次登录的时间")){
				System.out.println("登陆成功!");
			}else{
				System.out.println("登陆失败!");
			}
			
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
