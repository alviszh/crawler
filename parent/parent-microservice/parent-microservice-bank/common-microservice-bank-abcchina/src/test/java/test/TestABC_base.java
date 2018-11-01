package test;

import java.awt.Robot;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.jna.winio.WinIOAPI;

public class TestABC_base {
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	public static void main(String[] args) throws Exception {

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
//
//		Thread.sleep(2000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框
//
//		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
//			public WebElement apply(WebDriver driver) {
//				return driver.findElement(By.id("username"));
//			}
//		});
//		username.click();
//		username.clear();
////		username.sendKeys("6228480038881522676");
//		username.sendKeys("431103199010120011");
//
//		InputTab();
//
////		String password = "wxf104122";
//		String password = "feel641217";
//		KeyPressEx(password, 50);
//
//		Thread.sleep(1000);
//		String text = driver.findElement(By.id("powerpass_ie_dyn_Msg")).getText();
//		System.out.println(text);
//		
//		String path = WebDriverUnit.saveImg(driver, By.id("vCode"));
//		System.out.println("path---------------" + path);
//		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG, path); // 1005
//																														// 1~5位英文数字
//		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
//		Gson gson = new GsonBuilder().create();
//		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//		System.out.println("code ====>>" + code);
//
//		driver.findElement(By.id("code")).sendKeys(code);
//
//		driver.findElement(By.id("logo")).click();

		Thread.sleep(5000);
		String page = driver.getPageSource();
		System.out.println(page);
		
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
