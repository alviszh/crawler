package app.test;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

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
 * @description:  此方法用的是driver浏览器和htmlunit相结合的登录方式(selenium方式获取登录所需的验证码，htmlunit将相关信息进行统一校验)
 * 			相比较lasthopetest测试类而言，此测试类证明了登录成功之后不需要再次跳转到主页面，登录成功后，页面上出现了欢迎某某某字样的时候即可爬取
 * @author: sln 
 * @date: 2017年12月15日 下午6:14:15 
 */


public class LastHopeTest3 {
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
//			driver.findElement(By.className("gn3")).click();
			driver.findElement(By.id("loginbutton")).click();
			//等待登录页面
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.name("j_username"));
				}
			});
			// 验证码
			String path = WebDriverUnit.saveImg(driver, By.cssSelector("img[id='captchaimg']"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,path); 
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
			
			Set<Cookie> cookies =  driver.manage().getCookies();
	 		WebClient webClient = WebCrawler.getInstance().getWebClient();//  
	 		for(Cookie cookie:cookies){
	 			System.out.println(cookie.getName()+"---------------"+cookie.getValue());
	 			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ggfw.hsrsw.gov.cn",cookie.getName(),cookie.getValue()));
	 		}
			//在此处用请求链接的方式来登录，因为此链接会将错误信息响应回来
			String url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/login?j_username=131102199111230220&j_password=911018&j_captcha="+code+"";
			WebRequest webRequest=new WebRequest(new URL(url), HttpMethod.POST);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				System.out.println("用链接进行登录信息的校验，返回的信息是；"+html);
				if(html.contains("0000")){
					//登录成功
					//获取相关信息的请求必须带参数——个人编号，该参数如下链接请求：
//			 		 url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/curuser?_=1513655516710";
			 		 url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/curuser?_=1513655516710";
			 		 webRequest=new WebRequest(new URL(url), HttpMethod.GET);
					 page = webClient.getPage(webRequest);
					 if(page!=null){
						 html=page.getWebResponse().getContentAsString();
						 System.out.println("获取的个人编号参数信息的html是:"+html);
						 String pernum = JSONObject.fromObject(html).getJSONArray("userbussList").getJSONObject(0).getString("grbh");
						//请求必须带参数
						url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/getYalRyjbxx?grbh="+pernum+"";
						webRequest=new WebRequest(new URL(url), HttpMethod.GET);
						page = webClient.getPage(webRequest);
						if(page!=null){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的用户信息的html是:"+html);
						}
						
						
						//测试爬取个人缴费明细（通过测试知道，可以通过改变pagesize来改变每页显示的数据个数，故一次爬取干净）
						url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/getYalJfxxlist?aac001=1311011460893"
								+ "&aae041=201612&aae042=201712"
								+ "&_search=false&nd=1513588995488"
								+ "&pagesize=100&pageno=1&sidx=&sord=asc";
						webRequest=new WebRequest(new URL(url), HttpMethod.GET);
						page = webClient.getPage(webRequest);
						if(page!=null){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的养老缴费明细信息的html是:"+html);
						}
					 }
				}else if(html.contains("验证码错误")){
					System.out.println("验证码错误");
				}else if(html.contains("用户名或密码错误")){
					System.out.println("用户名或密码错误，或者验证码输入错误");
				}else{
					System.out.println("出现了其他登录错误");
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		
		}
	}
}
