package app.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLoginByUser extends AbstractChaoJiYingHandler{
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";
	private static String SKEY = "";
	private static WebDriver driver = null;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) {
		String url = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_00?SERVLET_NAME=B2CMainPlat_00&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2";
		
		try {
			login(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void login(String url) throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
//		ChromeOptions chromeOptions = new ChromeOptions();
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
//		chromeOptions.addArguments("disable-gpu"); 
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(dc);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(url);
		
		
		WebElement usernameInput = driver.findElement(By.id("USERID"));
		WebElement passwordInput = driver.findElement(By.id("LOGPASS"));
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		usernameInput.click();
		
		usernameInput.sendKeys("360622198812170013");
		passwordInput.sendKeys("886565");
		
		String path = null;
		try {
			path = WebDriverUnit.saveImg(driver,By.id("fujiama"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
		
		imgInput.sendKeys(code);
		driver.findElement(By.id("loginButton")).click();
		
		Thread.sleep(2000L);
		
//		System.out.println("====================================================================================");
//		System.out.println(driver.getPageSource());
//		System.out.println("====================================================================================");
		Alert alert = driver.switchTo().alert();
		System.out.println("alert的内容："+alert.getText());
		alert.accept();
		
		System.out.println("====================================================================================");
		writer(driver.getPageSource(),"C:/home/123.txt");
		System.out.println("====================================================================================");
		
		driver.switchTo().frame("mainfrm1");
		
		WebElement sms = driver.findElement(By.id("TRANSMSCODE"));
		//获取确认按钮
		WebElement btn = driver.findElement(By.id("btnNext"));
		
		sms.sendKeys("123456");
		//点击确认按钮
		btn.click();
		
		
	}
	
	public static void writer(String page, String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 byte bt[] = new byte[1024];  
	        bt = page.getBytes();  
	        try {  
	            FileOutputStream in = new FileOutputStream(file);  
	            try {  
	                in.write(bt, 0, bt.length);  
	                in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	}

}
