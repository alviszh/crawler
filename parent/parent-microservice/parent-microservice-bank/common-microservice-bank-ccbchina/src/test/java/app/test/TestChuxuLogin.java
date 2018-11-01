package app.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestChuxuLogin extends AbstractChaoJiYingHandler{
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";
	private static WebDriver driver = null;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) {
		String url = "http://accounts.ccb.com/accounts/login_savings_ope.gsp";
		try {
			seleniumLogin(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void seleniumLogin(String url) throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu"); 
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(chromeOptions);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(url);
		
		WebElement ele = driver.findElement(By.tagName("iframe")); 
		Point point = ele.getLocation(); 
		
		driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
		//用户名输入框
		WebElement usernameInput = driver.findElement(By.id("ACC_NO_temp"));
		//密码输入框
		WebElement passwordInput = driver.findElement(By.name("LOGPASS"));			
		//图片验证码输入框
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		//点击登录按钮
		WebElement button = driver.findElement(By.className("btn"));
		//使用键盘输入的按钮
		WebElement keyButton = driver.findElement(By.id("useKey"));
		
		//有数字键盘，改成用本地键盘录入。
		passwordInput.click();
		keyButton.click();
		
		//输入用户名密码
		usernameInput.sendKeys("6217000010043278798");
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
				
		passwordInput.sendKeys("838876");
		
		String code = null;
		String path = WebDriverUnit.saveImg(driver,By.id("fujiama"),point.getX(),point.getY());
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
			
		imgInput.sendKeys("21314");
		button.click();
		
		//判断是否需要发送短信验证码
		//等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识btnConf1 则表示登录失败
		try{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement eleHomePage = wait.until(new Function<WebDriver, WebElement>() {  
				public WebElement apply(WebDriver driver) {   
					return driver.findElement(By.id("error_main"));  
				}  
			});  		
		}catch(Exception e){
			String loginUrl = driver.getCurrentUrl();
			if(!loginUrl.contains("main_savings")){
				if(null != driver.findElement(By.cssSelector("div.warning_span"))){
					System.out.println("当前查询密码无法继续使用，请您到建行官网重新设置查询密码！");
				}else{
					WebElement error = driver.findElement(By.cssSelector("p.fs_16 + p"));
					System.out.println(error.getText());			
				}
			}
		}
	}

}
