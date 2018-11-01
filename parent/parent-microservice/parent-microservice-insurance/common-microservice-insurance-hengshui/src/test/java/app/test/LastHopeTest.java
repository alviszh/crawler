package app.test;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import net.sf.json.JSONObject;

/**
 * @description:  该测试类可用  用元素是否显示的方式来证明是否已经跳转到了相应的页面
 * @author: sln 
 * @date: 2017年12月15日 下午6:14:15 
 */


public class LastHopeTest {
	public static void main(String[] args) {
		String driverPath="C:\\ChromeServer\\chromedriver.exe";

		final String LEN_MIN = "0";
		final String TIME_ADD = "0";
		final String STR_DEBUG = "a";
		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		//设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		try{
			String loginUrl="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/";
			driver.get(loginUrl);
			Thread.sleep(1000);
			
			//选择个人社保信息查询登录方式
			driver.findElement(By.className("gn3")).click();
			//等待登录页面
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement loginUserName = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.name("j_username"));
				}
			});
			//输入用户名
//			driver.findElement(By.name("j_username")).sendKeys("131102199111230220");
			loginUserName.sendKeys("131102199111230220");
			//输入密码
			driver.findElement(By.name("j_password")).sendKeys("911018");
			// 验证码
			String path = WebDriverUnit.saveImg(driver, By.cssSelector("img[id='captchaimg']"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,path); 
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
	
			driver.findElement(By.name("j_captcha")).sendKeys(code);
			
			driver.findElement(By.name("btnlogin")).sendKeys(Keys.UP); //必须失去焦点，不然输入验证码以后焦点还在此处，模拟点击不起作用
			Thread.sleep(500);
			
			// 模拟点击登录
			driver.findElement(By.name("btnlogin")).click();
			try {
				WebDriverWait wait3 = new WebDriverWait(driver, 10);   
				//通过判断元素是否可见来决定是否点击登录
				WebElement until = wait3.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("gn3"))));
				if(null!=until){
					driver.findElement(By.className("gn3")).click();
					Thread.sleep(1000);
					
					//跳转到了登录后的页面
			    	System.out.println("登录成功，欢迎来到首页面");

					 Set<Cookie> cookies =  driver.manage().getCookies();
			 		 WebClient webClient = WebCrawler.getInstance().getWebClient();//  
			 		 for(Cookie cookie:cookies){
			 			 System.out.println(cookie.getName()+"---------------"+cookie.getValue());
			 			 webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ggfw.hsrsw.gov.cn",cookie.getName(),cookie.getValue()));
			 		 }
			 		 
			 		//获取相关信息的请求必须带参数——个人编号，该参数如下链接请求：
			 		 String url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/curuser?_=1513570750019";
			 		 WebRequest webRequest=new WebRequest(new URL(url), HttpMethod.GET);
					 Page page = webClient.getPage(webRequest);
					 if(page!=null){
						 String html=page.getWebResponse().getContentAsString();
						 System.out.println("获取的个人编号参数信息的html是:"+html);
						 String pernum = JSONObject.fromObject(html).getJSONArray("userbussList").getJSONObject(0).getString("grbh");
						//请求必须带参数
						url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/getYalRyjbxx?grbh="+pernum+"";
//							String url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/login?j_username=131102199111230220&j_password=911018&j_captcha=3133";
						webRequest=new WebRequest(new URL(url), HttpMethod.GET);
						page = webClient.getPage(webRequest);
						if(page!=null){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的用户信息的html是:"+html);
						}
					 }
			 		 
					
				}
			} catch (Exception e) {
				System.out.println("在进入最终的首页面时出现异常"+e.toString());
			}
		
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		
		}
	}
}
