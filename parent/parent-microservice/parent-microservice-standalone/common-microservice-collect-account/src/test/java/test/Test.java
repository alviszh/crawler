package test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

public class Test {

	private static AndroidDriver<WebElement> driver;

	public static void main(String[] args) {
		// 设置apk的路径
		File clasPathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(clasPathRoot, "apps");
		File app = new File(appDir, "modeng.apk");
		// 设置自动化相关参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		// 设置安卓系统版本和平台
		capabilities.setCapability("platformVersion", "4.4");
		capabilities.setCapability("platformName", "Android");
		// 设置apk路径
		capabilities.setCapability("app", app.getAbsolutePath());
		// 设置app的主包名和主类名
		capabilities.setCapability("appPackage", "cn.com.pclady.modern");
		capabilities.setCapability("appActivity", "cn.com.pclady.modern.module.launcher.SplashActivity");

		// 初始化
		try {
			driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			Thread.sleep(10000);
			// driver.findElement(By.id("com.android.packageinstaller:id/ok_button")).click();
			Thread.sleep(10000);
			driver.quit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
