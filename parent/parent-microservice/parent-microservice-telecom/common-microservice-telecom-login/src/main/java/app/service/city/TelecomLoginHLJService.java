package app.service.city;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecomByChrome;
import app.service.common.TelecomBasicService;

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
public class TelecomLoginHLJService extends TelecomBasicService{
	

	public WebParamTelecomByChrome<?> telecomToHlj(WebDriver driver,MessageLogin messageLogin,WebParamTelecomByChrome<?> webParamTelecomByChrome){
		tracerLog.addTag("匹配城市：", "黑龙江");
		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000846");
				
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver.get("http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("sendPwd"));
			}
		});

		Boolean bool = true;
		while (bool) {

			if (driver.getCurrentUrl()
					.indexOf("http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl") != -1) {
				bool = false;
				tracerLog.addTag("转换到特定url", "========转换成功==========" + driver.getCurrentUrl());
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		Set<org.openqa.selenium.Cookie> cookiesDriver2 = driver.manage().getCookies();

		for (org.openqa.selenium.Cookie cookie2 : cookiesDriver2) {
			Cookie cookieWebClient2 = new Cookie(cookie2.getDomain(), cookie2.getName(), cookie2.getValue());
			tracerLog.addTag(cookieWebClient2.getName(), cookieWebClient2.getValue());
			webClient2.getCookieManager().addCookie(cookieWebClient2);
		}
		// quit(messageLogin);
		quit(messageLogin,driver);

		webParamTelecomByChrome.setWebClient(webClient2);
		
		return webParamTelecomByChrome;
	}
	
}
