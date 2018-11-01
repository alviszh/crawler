package app.service;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.exceptions.NativeException;
import com.crawler.bank.json.BankJsonBean;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.VirtualKeyBoard;

import app.commontracerlog.TracerLog;

/**
 * 交行-储蓄卡登录接口（遇到图片验证码错误后，重试机制）
 * @author zz
 *
 */
@Component
public class BocomLoginService {
	
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private WebDriverIEService webDriverIEService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private JNativeService jNativeService;
	
	private WebDriver driver;
	String loginUrl = "https://pbank.95559.com.cn/personbank/index.html";
	
	/**
	 * 登录，重试机制
	 * @param bankJsonBean
	 * @return WebDriver
	 * @throws Exception
	 */
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebDriver retryLogin(BankJsonBean bankJsonBean) {
		
		Properties props=System.getProperties(); 
		String osName = props.getProperty("os.name"); //操作系统名称
		String osArch = props.getProperty("os.arch"); //操作系统构架
		String osVersion = props.getProperty("os.version"); //操作系统版本
		
		System.out.println("当前操作系统名称："+osName);
		System.out.println("当前操作系统构架："+osArch);
		System.out.println("当前操作系统版本："+osVersion);
		
		driver = webDriverIEService.getNewWebDriver();
		
		//ie浏览器打开银行登录页
		driver = webDriverIEService.getPage(driver,loginUrl);
		//获取登录框所在的位置
		WebElement ele = driver.findElement(By.id("bannerLogin"));
		Point point = ele.getLocation();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		int pX = point.getX();
		int pY = point.getY();

//		tracerLog.output("crawler.bank.bocom.login.page", "<xmp>"+page+"</xmp>");
		driver.switchTo().frame("bannerLogin");
		String pageSource = driver.getPageSource();
//		tracerLog.output("crawler.bank.bocom.login.page", "<xmp>"+page+"</xmp>");
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement usernameInput = null;
		try {
			//用户名输入框
			usernameInput = wait.until(new Function<WebDriver, WebElement>() {  
				public WebElement apply(WebDriver driver) {   
					return driver.findElement(By.id("alias"));  
				}  
			});  
		} catch (Exception e) {		
			tracerLog.output("未找到用户名输入框", e.getMessage());
		}
		
		tracerLog.output("用户名输入框：", usernameInput.toString());
		//登录点击按钮
		WebElement button = driver.findElement(By.id("login"));
		
		//判断是否需要图片验证码，为true则需要。
		if(pageSource.contains("var captchaEnable = 'true'")){
			//截图，并保存图片验证码到本地
			String path = null;
			try {
				path = WebDriverUnit.saveImg(driver,By.className("captchas-img-bg"),pX,pY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//图片验证码输入框
			WebElement imageInput = driver.findElement(By.id("input_captcha"));
			
			String code = chaoJiYingOcrService.callChaoJiYingService(path, "1902");
			tracerLog.output("crawler.bank.bocom.login.code", code);
			
			usernameInput.sendKeys(bankJsonBean.getLoginName().trim());
			
			try {
				jNativeService.InputTab();
				Thread.sleep(1000);
				VirtualKeyBoard.KeyPressEx(bankJsonBean.getPassword().trim());
				jNativeService.InputTab();
				Thread.sleep(1000);
				imageInput.sendKeys(code);
			} catch (IllegalAccessException e) {
				tracerLog.output("bocomloginservice.IllegalAccessException", e.getMessage());
			} catch (NativeException e) {
				tracerLog.output("bocomloginservice.NativeException", e.getMessage());
			} catch (Exception e) {
				tracerLog.output("bocomloginservice.Exception", e.getMessage());
			}
		}else{
			//不需要图片验证码的情况
			try {
				usernameInput.sendKeys(bankJsonBean.getLoginName().trim());
				jNativeService.InputTab();
				Thread.sleep(1000);
				VirtualKeyBoard.KeyPressEx(bankJsonBean.getPassword().trim(), 500);
			} catch (Exception e) {
				tracerLog.output("bocomloginservice.Exception", e.getMessage());
			}
			
		}
		
		button.click();
		button.click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
		tracerLog.output("交通银行储蓄卡点击登录后页面url", driver.getCurrentUrl());
		if(driver.getCurrentUrl().equals(loginUrl)){
			tracerLog.output("loginservice.error", "图片验证码错误，触发retry机制");
			driver.quit();
			throw new RuntimeException("请输入正确的附加码");
		}else{			
			return driver;
		}	
	}

}
