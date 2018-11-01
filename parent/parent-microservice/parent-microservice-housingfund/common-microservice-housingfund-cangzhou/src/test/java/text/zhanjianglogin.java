package text;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.service.ChaoJiYingOcrService;


public class zhanjianglogin {
	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	public static void main(String[] args) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://219.132.4.6:6012/web/ggfw/app/index.html#/ggfw/home";
		driver.get(baseUrl);
		Thread.sleep(2000L);
		
		WebElement element = driver.findElement(By.xpath("//a[@class = 'btn btn_关闭 nc-btn btn-info']"));
		element.click();
		Thread.sleep(5000L);
		
		WebElement element2 = driver.findElement(By.xpath("//a[@ng-click = 'login()']"));
		element2.click();
		
		driver.findElement(By.name("LOGINID")).sendKeys("440803199106231129");//帐号  440803199106231129
		
		Thread.sleep(2000L);
		robot = new Robot();
		//密码
		robot.keyPress(KeyEvent.VK_TAB);
		String password = "xian873569770.";//xian873569770.
		VirtualKeyBoard.KeyPressEx(password, 50);
		Thread.sleep(2000L);
		
		//验证码
		String path = WebDriverUnit.saveImg(driver, By.id("authImg"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG,
				path); // 1005
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		robot.keyPress(KeyEvent.VK_TAB);
		
		VirtualKeyBoard.KeyPressEx(code, 50);
		
		driver.findElement(By.className("button")).click();
		
//		Thread.sleep(2000L);
//		robot.keyPress(KeyEvent.VK_ENTER);//按下回车键
//		robot.keyRelease(KeyEvent.VK_ENTER);//释放回车键
		
		Thread.sleep(2000L);
	String url = "http://219.132.4.6:6012/web/ajax.do?"
			+ "r=0.04723253923962423"
			+ "&_isModel=true"
			+ "&params={%22oper%22:%22JbgrxxcxAction.query%22,%22params%22:{%22MenuId%22:%22104014%22},%22datas%22:{%22ncm_gt_%E6%9F%A5%E8%AF%A2%E6%9D%A1%E4%BB%B6%22:{%22params%22:{%22%E8%AF%81%E4%BB%B6%E5%8F%B7%E7%A0%81%22:%22440803199106231129%22}}}}";
		driver.get(url);
		
	}
	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
	
}
