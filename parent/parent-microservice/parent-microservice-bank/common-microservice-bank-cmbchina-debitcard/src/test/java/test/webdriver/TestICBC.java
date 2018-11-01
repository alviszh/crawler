package test.webdriver;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class TestICBC extends AbstractChaoJiYingHandler{
	
	static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static final String OCR_FILE_PATH = "/home/img";
	private static String uuid = UUID.randomUUID().toString();
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception {
		
		getCookie();
		
		/*DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_index.jsp"; 
		driver.manage().window().maximize();
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
//		System.out.println("currentUrl--11--"+currentUrl); 
		String htmlsource = driver.getPageSource(); 
		
		driver.switchTo().frame("ICBC_login_frame");
		WebElement element = driver.findElement(By.id("logonCardNum"));
		element.click();
		
		String[] accountNum = { "6", "2", "1", "2", "2", "6", "0", "2", "0", "0", "1", "3", "3", "7", "6", "6", "7", "8", "8" };
		
		String[] password = { "0", "1", "2", "3", "4", "0", "0", "1", "4" }; 
		
		Thread.sleep(1000L);
		
		Input(accountNum);
		InputTab(); 
		Input(password);
		InputTab(); 
		
        Set<String> handles = driver.getWindowHandles(); 
        Iterator<String> it = handles.iterator();
        while(it.hasNext()){
        	 String handle = it.next();
        	 WebDriver window = driver.switchTo().window(handle);
        	 System.out.println("url  =======>>"+window.getCurrentUrl());
        	 File srcFile = ((TakesScreenshot)driver).  
                     getScreenshotAs(OutputType.FILE);  
     		
             FileUtils.copyFile  
             (srcFile,getImageLocalPath());
             
        	 
        }
         
         
		
		String currentUrl3 = driver.getCurrentUrl();
		String htmlsource3 = driver.getPageSource(); 		
//		System.out.println("登录页 ===》》"+htmlsource3);
		
		WebElement button = driver.findElement(By.className("login-text"));
//		System.out.println("htmlsource--11--"+htmlsource); 
//		System.out.println(driver.getWindowHandle());		windows 窗口所拥有的唯一标识，就是句柄		
			
			
		System.out.println(User32.GetWindowText(User32.GetForegroundWindow()));
		System.out.println(System.getProperty("java.library.path"));	
		
//		driver.switchTo().frame("VerifyimageFrame");
		
		WebElement ele = driver.findElement(By.id("VerifyimageFrame"));
		System.out.println("图片的路径 ====>>"+ele.getAttribute("src"));
		
		String filePath = saveImg(driver,ele);	
		
		
		System.out.println(filePath);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, filePath);
		System.out.println("chaoJiYingResult ====>>"+chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>"+code);
		
		String[] verify = code.split("");	
		
		Input(verify);

		button.click();
*/
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
	
	public static void InputCapsLock() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("CapsLock"));
		}
	}
	
	public static File getImageLocalPath(){
		
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		
		String imageName = uuid + ".jpg";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
				
		return codeImageFile;
		
	}
	
	
	public static String saveImg(WebDriver driver, WebElement ele) throws Exception{
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		Point point = ele.getLocation();
			
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();

		// Crop the entire page screenshot to get only element screenshot
		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
				eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "jpg", screenshot);
			
		File file = getImageLocalPath();		
		FileUtils.copyFile(screenshot, file);
		

		return file.getAbsolutePath();
	}
	
	
	public static void getCookie() throws Exception{
		
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://epass.icbc.com.cn/login/login.jsp?StructCode=1&orgurl=0&STNO=30"; 
		driver.manage().window().maximize();
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
//		System.out.println("currentUrl--11--"+currentUrl); 
		String htmlsource = driver.getPageSource(); 
		
		WebElement element = driver.findElement(By.id("logonCardNum"));
		element.click();
		
		WebElement button = driver.findElement(By.className("login-text"));
		WebElement ele = driver.findElement(By.id("VerifyimageFrame"));
		System.out.println("图片的路径 ====>>"+ele.getAttribute("src"));
		String filePath = saveImg(driver,ele);	
		
		System.out.println(filePath);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, filePath);
		System.out.println("chaoJiYingResult ====>>"+chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>"+code);
		
		String[] verify = code.split("");	
		
		String[] accountNum = { "6", "2", "1", "2", "2", "6", "0", "2", "0", "0", "1", "3", "3", "7", "6", "6", "7", "8", "8"};
		
		String[] capslk = {"Z"};
		
		String[] password = { "0", "1", "2", "3", "4", "0", "0", "1", "4" }; 
		
		Thread.sleep(1000L);
		
		InputCapsLock();
		Input(accountNum);
		InputTab();
		Input(capslk);
		InputCapsLock();
		Input(password);
		InputTab(); 
		Input(verify);
		button.click();
		JavascriptExecutor driver_window= ((JavascriptExecutor) driver);
		driver_window.executeScript("loginSubmit()");
		
		System.out.println("登陆后的url  =====>> "+driver.getCurrentUrl());
//		System.out.println("登录后的页面  =======>>"+driver.getPageSource());

	}

}
