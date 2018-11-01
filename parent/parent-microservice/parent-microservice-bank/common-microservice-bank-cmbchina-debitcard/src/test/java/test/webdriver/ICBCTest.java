package test.webdriver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ICBCTest {

static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	//static String driverPath = "C:\\Program Files\\Internet Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
       
        
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();
		 driver = new InternetExplorerDriver(ieCapabilities);
		//��ʱ30s
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//String baseUrl = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_index.jsp";
		String baseUrl = "https://epass.icbc.com.cn/login/login.jsp";
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
		System.out.println("currentUrl--11--"+currentUrl); 
		
		driver.findElement(By.id("logonNameHolder")).sendKeys("66666");    
		
		//driver.switchTo().frame("ICBC_login_frame");    
		
		//JavascriptExecutor driver_js= ((JavascriptExecutor) driver); 
		//driver_js.executeScript("showVCode()");
		
		String htmlsource3 = driver.getPageSource(); 
		System.out.println("htmlsource--33--"+htmlsource3); 
		
		// Get entire page screenshot
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(screenshot);
		//全屏截屏
		FileUtils.copyFile(screenshot, new File("D:\\img\\icbc1.jpg"));
		
		
		//截取验证码
		WebElement verifyimageFrame = driver.findElement(By.id("VerifyimageFrame"));
		
		
		
		// Get the location of element on the page
		Point point = verifyimageFrame.getLocation();

		// Get width and height of the element
		int eleWidth = verifyimageFrame.getSize().getWidth();
		int eleHeight = verifyimageFrame.getSize().getHeight();
		
		// Crop the entire page screenshot to get only element screenshot
		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
		    eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);

		// Copy the element screenshot to disk
		File screenshotLocation = new File("D:\\img\\verifyimageFrame.png");
		FileUtils.copyFile(screenshot, screenshotLocation);
		
		
		
		
		
		

	}

}
