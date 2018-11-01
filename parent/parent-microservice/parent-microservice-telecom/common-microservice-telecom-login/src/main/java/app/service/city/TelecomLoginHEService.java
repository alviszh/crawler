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
import app.crawler.telecom.htmlparse.TelecomParseCommon;
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
public class TelecomLoginHEService extends TelecomBasicService{
	

	public WebParamTelecomByChrome<?> telecomToHe(WebDriver driver,MessageLogin messageLogin,WebParamTelecomByChrome<?> webParamTelecomByChrome){
		tracerLog.addTag("匹配城市：", "河北");
		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00380407");
		driver.get("http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he");
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("postValid"));
			}
		});

		Boolean bool = true;
		while (bool) {

			if (driver.getCurrentUrl().indexOf(
					"http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he") != -1) {
				bool = false;
				tracerLog.addTag("转换到特定url", "========转换成功==========" + driver.getCurrentUrl());
				String citycodeForHeBei = TelecomParseCommon.parserCityCodeForHeBei(driver.getPageSource());

				webParamTelecomByChrome.setCityCodeForHeBei(citycodeForHeBei);

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

		for (org.openqa.selenium.Cookie cookie : cookiesDriver2) {
			Cookie cookieWebClient = new Cookie("he.189.cn", cookie.getName(), cookie.getValue());

			tracerLog.addTag(cookieWebClient.getName(), cookieWebClient.getValue());

			webClient2.getCookieManager().addCookie(cookieWebClient);
		}

		// quit(messageLogin);
		quit(messageLogin,driver);

		webParamTelecomByChrome.setWebClient(webClient2);
		
		return webParamTelecomByChrome;
	}
	
}
