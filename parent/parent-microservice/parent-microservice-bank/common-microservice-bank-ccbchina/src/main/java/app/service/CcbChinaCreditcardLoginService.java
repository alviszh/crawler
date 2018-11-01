package app.service;

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
import com.crawler.bank.json.BankJsonBean;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;

/**
 * 建行-信用卡登录接口（遇到图片验证码错误后，重试机制）
 * @author zz
 *
 */
@Component
public class CcbChinaCreditcardLoginService {
	
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private WebDriverChromeService webDriverChromeService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	private WebDriver driver;
	
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
		String loginUrl = "http://accounts.ccb.com/tran/WCCMainB1L1?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainB1L1&TXCODE=E3CX00";
		
		//打开建行登录页面
		try{
			driver = webDriverChromeService.getPageByChrome(loginUrl);
		}catch(Exception e){
			tracerLog.output("webDriverChromeService.getPageByChrome.error", e.getMessage());
			return null;
		}
		
		// Get the location of element on the page 
		WebElement ele = driver.findElement(By.id("result1")); 
		Point point = ele.getLocation(); 
				
		driver.switchTo().frame(ele);
		
//		tracerLog.output("登录页面源码：", "<xmp>"+driver.getPageSource()+"</xmp>");
		//用户名输入框
		WebElement usernameInput = driver.findElement(By.name("ACC_NO_temp"));
		//密码输入框
		WebElement passwordInput = driver.findElement(By.name("LOGPASS"));			
		//图片验证码输入框
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		//点击登录按钮
		WebElement button = driver.findElement(By.className("btn"));
				
		//有数字键盘，改成用本地键盘录入。
		passwordInput.click();
		//使用键盘输入的按钮
		WebElement keyButton = driver.findElement(By.className("keyInfoBtn"));
		keyButton.click();
		//输入用户名
		usernameInput.sendKeys(bankJsonBean.getLoginName());
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//输入密码
		passwordInput.sendKeys(bankJsonBean.getPassword().trim());
		
		String code = null;
		try {
			String path = WebDriverUnit.saveImg(driver,By.id("fujiama"),point.getX(),point.getY());
			code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
			
			imgInput.sendKeys(code);
			button.click();
			Thread.sleep(2000L); 
			//判断是否需要发送短信验证码
			//等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识btnConf1 则表示登录失败
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement errorMain = wait.until(new Function<WebDriver, WebElement>() {  
				public WebElement apply(WebDriver driver) {   
				return driver.findElement(By.id("error_main"));  
				}  
			});  
		} catch (Exception e) {
			tracerLog.output("getHtml.error", e.getMessage());
		}
		String path = "";
		//截图
		try {
			path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
		} catch (Exception e) {
			tracerLog.output("点击登录后，截图出错", e.getMessage());
		}
		
		if(driver.getPageSource().contains("请输入正确的附加码")){
			tracerLog.output(path, "图片验证码错误，触发retry机制");
			driver.quit();
			throw new RuntimeException("请输入正确的附加码");
		}else{
			return driver;
		}

	}
	
	

}
