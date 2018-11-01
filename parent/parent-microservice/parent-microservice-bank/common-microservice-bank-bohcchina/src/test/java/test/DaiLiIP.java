package test;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DaiLiIP {

		static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
		
		private static Robot robot;

		public static void main(String[] args) throws Exception {
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			System.setProperty("webdriver.ie.driver", driverPath);
			WebDriver driver = new InternetExplorerDriver();
			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "http://118.112.188.109/nethall/login.jsp";
			driver.get(baseUrl);
			
			try {
				robot = new Robot();// 创建Robot对象
			} catch (Exception e) {
				e.printStackTrace();
			}
			robot.keyPress(KeyEvent.VK_ALT);
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_X);
			Thread.sleep(1000);
			robot.keyRelease(KeyEvent.VK_ALT);
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_O);
			for (int i = 0; i < 15; i++) {
				Thread.sleep(1000);
				robot.keyPress(KeyEvent.VK_TAB);
			}
			for (int i = 0; i < 4; i++) {
				Thread.sleep(1000);
				robot.keyPress(KeyEvent.VK_RIGHT);
			}
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_L);
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_X);
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_ENTER);
			
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_ESCAPE);
			Thread.sleep(1000);
			
			driver.quit();
		}

	}
