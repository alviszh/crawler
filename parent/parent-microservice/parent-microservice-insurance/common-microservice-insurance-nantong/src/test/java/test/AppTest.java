package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
public class AppTest {

	public static String driverPath = "D:\\IEDriverServer_Win32\\chromedriver.exe";

	public static void main(String[] args) throws Exception {

		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "http://www.jsnt.lss.gov.cn:1002/query/";
		driver.get(baseUrl);
		// 用户名
		driver.findElement(By.id("account")).sendKeys("JSC050899974");
		// 密码
		Actions action = new Actions(driver);
		WebElement psddly = driver.findElement(By.xpath("//*[@id=\"psddly\"]"));
		action.moveToElement(psddly).click().sendKeys("1234").perform();

		WebElement source = driver.findElement(By.xpath("//*[@id=\"drag\"]/div[3]"));
		action.moveToElement(source).clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		Thread.sleep(2000);
		// 释放
		action.moveToElement(source).release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		try {
			Action actions = action.build();
			actions.perform();
		} catch (Exception e) {
			Thread.sleep(5000);
			String html = driver.getPageSource();
			System.out.println(html);
			if (html.contains("id=\"mainFrame\"")) {
				System.out.println("登录成功！");
			} else {
				System.out.println("登录失败！");
			}
			driver.quit();
		}
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(500L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(500L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static String getImgStr(String imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(data));
	}
}
