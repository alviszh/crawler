package app.service.city;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecomByChrome;
import app.service.common.TelecomBasicService;
import app.unit.CommonUnit;

/**   
*    
* 项目名称：common-microservice-telecom-login   
* 类名称：TelecomLoginNXService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年10月24日 上午10:48:09   
* @version        
*/
@Component
public class TelecomLoginHBService extends TelecomBasicService{
	


	public WebParamTelecomByChrome<?> telecomToHb(WebDriver driver,MessageLogin messageLogin,WebParamTelecomByChrome<?> webParamTelecomByChrome){
		tracerLog.addTag("匹配城市：", "湖北");
		long startTime=System.currentTimeMillis();
		driver.get("http://hb.189.cn/pages/selfservice/custinfo/userinfo/userInfo.action?trackPath=SYDH");
//		driver.get("http://hb.189.cn/service/integral/qryIndex.action");
		if (driver.getCurrentUrl().indexOf("http://login.189.cn/web/login") != -1) {
			driver.get(driver.getCurrentUrl());
			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement loginByUserButton2 = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("txtAccount"));
				}
			});

			loginByUserButton2.click();
			wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("txtAccount"));
				}
			});

			driver.findElement(By.id("txtShowPwd")).click();
			driver.findElement(By.id("txtPassword")).sendKeys(messageLogin.getPassword().trim());
			String code;
			try {
				code = CommonUnit.getVerfiycodeBy(By.id("imgCaptcha"), driver, this.getClass());
				driver.findElement(By.id("txtCaptcha")).sendKeys(code.trim());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				quit(messageLogin,driver);
				webParamTelecomByChrome.setErrormessage(e.getMessage());
				return webParamTelecomByChrome;
			}
			try {
			  driver.findElement(By.id("loginbtn")).click();
			} catch (Exception e) {
               e.printStackTrace();
			}
			//Robot
			try{
//		    Robot robot = new Robot();
//			//设置Robot产生一个动作后的休眠时间,否则执行过快
//			robot.setAutoDelay(1000);
//			tracerLog.addTag("湖北按下ESC键start：", "按下ESCstart");
//			pressKey(robot);
//			tracerLog.addTag("湖北按下ESC键end：", "按下ESCend");
			tracerLog.addTag("湖北按下ESC键start：", "按下ESCstart");
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ESCAPE);
			action.sendKeys(Keys.ESCAPE);
			action.perform();	
			tracerLog.addTag("湖北按下ESC键end：", "按下ESCend");
			}catch(Exception  e){
				e.printStackTrace();
				tracerLog.addTag("湖北电信Exception", "湖北电信Exception");
			}
					
			try{
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
						.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("showTable01"));
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}	
			
			try{
				WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
				Set<org.openqa.selenium.Cookie> cookiesDriver2 = driver.manage().getCookies();
				for (org.openqa.selenium.Cookie cookie : cookiesDriver2) {
					Cookie cookieWebClient = new Cookie("hb.189.cn", cookie.getName(), cookie.getValue());
					webClient2.getCookieManager().addCookie(cookieWebClient);
				}
				webParamTelecomByChrome.setWebClient(webClient2);
			}catch(Exception e){
				System.out.println("无法获取cookie");
			}
			tracerLog.addTag("湖北电信登陆成功：", "湖北电信登陆成功");
			long endTime=System.currentTimeMillis();
			tracerLog.addTag("湖北电信二次登陆共用时：", (endTime-startTime)+"");
			quit(messageLogin,driver);
		}
		return webParamTelecomByChrome;

		
	}
	
	
}
