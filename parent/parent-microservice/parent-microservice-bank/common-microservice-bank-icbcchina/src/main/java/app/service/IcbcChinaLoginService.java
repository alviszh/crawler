package app.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.module.ddxoft.VK;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;

@Component
public class IcbcChinaLoginService {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private WebDriverIEService webDriverIEService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	private WebDriver driver;
	
	/**
	 * 登录，重试机制
	 * @param bankJsonBean
	 * @return WebDriver
	 * @throws IllegalAccessException 
	 * @throws NativeException 
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebDriver retryLogin(BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, InterruptedException {
		driver = webDriverIEService.getNewWebDriver();
		String loginUrl = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_index.jsp";
//		String loginUrl = "https://epass.icbc.com.cn/login/login.jsp";
		
		//打开建行登录页面
		driver = webDriverIEService.getPage(driver, loginUrl);
		driver.get(loginUrl);
		
		//切换到登录的frame
		driver.switchTo().frame("ICBC_login_frame");
		
		//输入用户名
		WebDriverWait wait=new WebDriverWait(driver, 10);
		WebElement logonCardNum = wait.until(new ExpectedCondition<WebElement>() {  
		            public WebElement apply(WebDriver driver) {  
		                return driver.findElement(By.id("logonCardNum"));  
		            } 
		        });
		logonCardNum.sendKeys(bankJsonBean.getLoginName());
//		jNativeService.Input(bankJsonBean.getLoginName());
		
		try {
			// 模拟tab按键
			VK.Tab();

			// 输入密码
			VK.KeyPress(bankJsonBean.getPassword());

			// 切换到默认frame然后截屏获取验证码
			driver.switchTo().defaultContent();
			String path = WebDriverUnit.saveImg(driver, "ICBC_login_frame", By.id("VerifyimageFrame"));
			String code = chaoJiYingOcrService.callChaoJiYingService(path, "1902");

			// 输入验证码
			WebDriverWait wait2=new WebDriverWait(driver, 10);
			WebElement verifyCodeCn = wait2.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.id("verifyCodeCn"));  
			            } 
			        });
			verifyCodeCn.sendKeys(code);

			// 模拟Enter按键
			VK.Enter();

			Thread.sleep(1500);
			// 模拟 I键 按键3次 为了确保第一次登陆的账户需要安装安全控件，按i键可同意安装
			VK.KeyPress("i");
			Thread.sleep(1000);
			VK.KeyPress("i");
			Thread.sleep(1000);
			VK.KeyPress("i");
			Thread.sleep(1000);
			tracer.addTag("login.logined.pageSource", "<xmp>" + driver.getPageSource() + "</xmp>");
			// 截图
			String shotpath = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracer.addTag("点击登录Screenshot", "截图:" + shotpath);
		} catch (IllegalAccessException e) {
			tracer.addTag("loginRetry.exception", e.toString());
			e.printStackTrace();
			throw e;
		} catch (NativeException e) {
			tracer.addTag("loginRetry.exception", e.toString());
			e.printStackTrace();
			throw e;
		} catch (InterruptedException e) {
			tracer.addTag("loginRetry.exception", e.toString());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//截图
		String path = "";
		try {
			path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
		} catch (Exception e) {
			tracer.addTag("点击登录后，截图出错", e.getMessage());
		}
		if(driver.getPageSource().contains("验证码输入错误或已超时失效")){
			tracer.addTag(path, "图片验证码错误，触发retry机制");
			driver.quit();
			throw new RuntimeException("请输入正确的附加码");
		}else{
			return driver;
		}
		
	}
}
