package app.parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.mobile.json.CookieJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;

@Component
public class InsuranceWeiFangParser extends AbstractChaoJiYingHandler{
	@Autowired
	private TracerLog tracer;
	public WebParam login(InsuranceRequestParameters parameter,WebDriver driver){
		WebParam webParam = new WebParam();
		try {
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "https://www.sdwfhrss.gov.cn/hsp/logonDialog.jsp";
			driver.get(baseUrl);
			WebDriverWait wait=new WebDriverWait(driver, 10);	
			WebElement username= wait.until(new ExpectedCondition<WebElement>() {  
	            public WebElement apply(WebDriver driver) {  
	                return driver.findElement(By.id("yhmInput"));  
	            } 
	        });
			
			//WebElement username = driver.findElement(By.id("yhmInput"));
			WebElement password = driver.findElement(By.id("mmInput"));
			WebElement IMAGCHECK = driver.findElement(By.id("validatecodevalue1"));
			
			String path = WebDriverUnit.saveImg(driver, By.id("validatecode1"));
			tracer.addTag("parser.login.codeimg.path", path);
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", "0", "0", "a", path); 
			tracer.addTag("parser.login.codeimg.chaoJiYingResult", chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
			tracer.addTag("parser.login.codeimg.code", code);

			WebElement button = driver.findElement(By.name("login_btn"));
			
			username.sendKeys(parameter.getUsername());
			password.sendKeys(parameter.getPassword());
			IMAGCHECK.sendKeys(code);
			button.click();			
			Boolean flag = true;
			while (flag) {
				if (driver.getCurrentUrl()
						.indexOf("https://www.sdwfhrss.gov.cn/hsp/mainFrame.jsp") != -1) {
					webParam.setCode(1);
					flag = false;
					tracer.addTag("转换到特定url", "========转换成功==========" + driver.getCurrentUrl());
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}

			}			
			webParam.setDriver(driver);
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
			for (org.openqa.selenium.Cookie cookie : cookies) {
				CookieJson cookiejson = new CookieJson();
				cookiejson.setDomain(cookie.getDomain());
				cookiejson.setKey(cookie.getName());
				cookiejson.setValue(cookie.getValue());
				cookiesSet.add(cookiejson);
			}
			String cookieJson = new Gson().toJson(cookiesSet);
			// 借用个url传递cookie
			webParam.setCookies(cookieJson);
			
			String filename=driver.getCurrentUrl();
			int begin=filename.indexOf("__usersession_uuid=");
			int last=filename.indexOf("&_width");
			String sessionid=filename.substring(begin+19,last);
			webParam.setSessionid(sessionid);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.login.Exception", e.getMessage());
		}
		return webParam;
	}



}
