package test.robot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

public class IETest {

	static String driverPath = "D:\\software\\";

	public static void main(String[] args) throws AWTException, IOException {
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath + "IEDriverServer.exe");

		WebDriver driver = new InternetExplorerDriver();
		String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";
		// String baseUrl = "https://login.10086.cn/html/login/login.html";
		driver.get(baseUrl);

		String htmlsource = driver.getPageSource();
		driver.findElement(By.id("UniLoginUser_Ctrl")).click();// UniLoginPwd_Ctrl
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();

		Robot robot = new Robot();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(d);
		// 设置Robot产生一个动作后的休眠时间,否则执行过快
		Rectangle screenRect = new Rectangle(d);
		// 截图（截取整个屏幕图片）
		BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
		// 保存截图
		File file = new File("screenRect.png");
		ImageIO.write(bufferedImage, "png", file);

		// 移动鼠标
		//robot.mouseMove(280, 30);
		robot.mouseMove(1080, 370);
		// 点击鼠标
		// 鼠标左键
		System.out.println("单击");
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.setAutoDelay(1000);
		
		robot.keyPress(KeyEvent.VK_BACK_SPACE);
		robot.keyRelease(KeyEvent.VK_BACK_SPACE);
		robot.setAutoDelay(1000);
		
		robot.keyPress(KeyEvent.VK_6);
		robot.keyRelease(KeyEvent.VK_6);
		robot.setAutoDelay(1000);
		
		robot.keyPress(KeyEvent.VK_2);
		robot.keyRelease(KeyEvent.VK_2);
		robot.setAutoDelay(1000);
		
		robot.keyPress(KeyEvent.VK_2);
		robot.keyRelease(KeyEvent.VK_2);
		robot.setAutoDelay(1000);
		
		robot.keyPress(KeyEvent.VK_5);
		robot.keyRelease(KeyEvent.VK_5);

		// System.out.println("htmlsource--------"+htmlsource);

		// driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

		// Actions parser=new Actions(driver);
		// parser.keyDown(Keys.CONTROL).sendKeys("1").perform();

		// parser.keyDown(Keys.NUMPAD6).keyUp(Keys.NUMPAD6).perform();

		// parser.keyDown(Keys.NUMPAD6);
		// driver.findElement(By.id("UniLoginUser_Ctrl")).click();

		// System.out.println("user------"+user);

		// driver.findElement(By.id("UniLoginPwd_Ctrl")).sendKeys("369258");
		// driver.findElement(By.id("LoginBtn")).click();

	}

}
