package app;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.handan.HousingHandanPay;
import com.microservice.dao.entity.crawler.housing.handan.HousingHandanUserinfo;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLoginBySelenium extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";
	private static WebDriver driver = null;
//	private static String url = "http://www.hdgjj.cn/olbh/index";
	static String _csrf ="";
	
	public static void main(String[] args) throws Exception {
		login();
		
		getUserInfo();
		
	}


	private static void getUserInfo() throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
			Cookie cookieWebClient = new Cookie("www.hdgjj.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		URL smsAction = new URL("http://www.hdgjj.cn/olbh/qryPersonInfo.do?_csrf"+_csrf);
		WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("_csrf", _csrf));
		
		requestSettings.setAdditionalHeader("Host", "www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Origin", "http://www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Referer", "http://www.hdgjj.cn/olbh/pub/home");
		
		Page page = webClient.getPage(requestSettings); 
		System.out.println("用户信息=====================================================================");
		System.out.println(page.getWebResponse().getContentAsString());
		
		parserUserinfo(page.getWebResponse().getContentAsString());
		
		getDatail(webClient);
		
	}

	private static void parserUserinfo(String contentAsString) {
		HousingHandanUserinfo userinfo = new Gson().fromJson(contentAsString, HousingHandanUserinfo.class);
		System.out.println(userinfo.toString());
	}


	private static void getDatail(WebClient webClient) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String url = "http://www.hdgjj.cn/olbh/qryPersonAccDetails.do";
		URL smsAction = new URL(url);
		WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("_csrf", _csrf));
		requestSettings.getRequestParameters().add(new NameValuePair("begDate", "2010-01-01"));
		requestSettings.getRequestParameters().add(new NameValuePair("currentPage", "1"));
		requestSettings.getRequestParameters().add(new NameValuePair("endDate", sdf.format(new Date())));
		requestSettings.getRequestParameters().add(new NameValuePair("limits", "1000"));
		requestSettings.setAdditionalHeader("Host", "www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Origin", "http://www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Referer", "http://www.hdgjj.cn/olbh/pub/home");
		
		Page page = webClient.getPage(requestSettings); 
		System.out.println("缴费流水--------------------------------------------------------");
		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("-------------------------------------------------------------");
		
		parserDetail(page.getWebResponse().getContentAsString());
	}

	private static void parserDetail(String contentAsString) {
		PayBean payBean = new Gson().fromJson(contentAsString, PayBean.class);
		List<HousingHandanPay> data = payBean.getResult();
		for(HousingHandanPay pay : data){
			System.out.println(pay.toString());
		}
	}


	private static void login(){
	
		String url = "http://www.hdgjj.cn/olbh/index";
		for(int i = 0 ; i<4;i++){
			try {
				driver = getPageByChrome(url);
				break;
			} catch (Exception e) {
				driver.quit();
				continue;
			}
			
		}

		
		//radio选择框
		WebElement radio = driver.findElement(By.id("perRadio"));
		radio.click();
		
		WebElement element = driver.findElement(By.name("_csrf"));
		_csrf = element.getAttribute("value");
		
		//身份证号输入框
		WebElement idnum = driver.findElement(By.id("idNumber"));
		//密码输入框
		WebElement password = driver.findElement(By.id("password"));
		//点击登录按钮
		WebElement button = driver.findElement(By.xpath("//button[@class='btn btn-default btn_login']"));		
		//图片验证码输入框
		WebElement sms = driver.findElement(By.id("verifyCode"));
		
		String path = null;
		try {
			path = WebDriverUnit.saveImg(driver,By.id("person_pic"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
		
		idnum.sendKeys("130434199106167526");
		password.sendKeys("123456");
		sms.sendKeys(code);
		
		button.click();
		
		System.out.println(driver.getPageSource());
	}
	
	
	public static WebDriver getPageByChrome(String url) throws Exception{

		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		if(driverPath==null){
//			tracer.addTag("WebDriverIEService initChrome RuntimeException", "webdriver.chrome.driver 初始化失败！");
			throw new RuntimeException("webdriver.chrome.driver 初始化失败！");
		}


		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu");

		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.get(url);


		return driver;

	}

}
