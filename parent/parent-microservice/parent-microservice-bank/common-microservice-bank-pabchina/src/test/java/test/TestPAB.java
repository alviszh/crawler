package test;

import java.awt.Robot;
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

import com.module.ddxoft.VK;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.jna.winio.WinIOAPI;

public class TestPAB {
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
		String baseUrl = "https://bank.pingan.com.cn/m/member/ibank/index.html";
		driver.get(baseUrl);

		Thread.sleep(7000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

		String source = driver.getPageSource();

		System.out.println(source);

//		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//		WebElement passTologin = wait2.until(new Function<WebDriver, WebElement>() {
//			public WebElement apply(WebDriver driver) {
//				return driver
//						.findElement(ByXPath.cssSelector("//*[@id=\"container\"]/div/div[1]/div/div[2]/div[1]/div[2]"));
//			}
//		});

		//切换登录方式
		driver.findElement(By.xpath("//*[@id=\"root\"]/section/div[4]/div[1]/div/div[2]/div[1]/div[2]")).click();

		Wait<WebDriver> wait21 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait21.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("userName"));
			}
		});

		Thread.sleep(2000L);

		username.click();
		username.clear();
		username.sendKeys("menghongyu1015");

		driver.findElement(By.id("pwdObject1-btn-pan")).click();
		Thread.sleep(1000L);
		driver.findElement(By.id("pa_ui_keyboard_close")).click();

		String password = "syh641012";
		/*
		 * DD模拟输入（安装版）
		 */
		VK.KeyPress(password);
		/*
		 * winio模拟输入
		 */
		// KeyPressEx(password, 200);

		driver.findElement(By.id("login_btn")).click();

		Thread.sleep(5000);

		String source1 = driver.getPageSource();

		System.out.println(source1);

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
