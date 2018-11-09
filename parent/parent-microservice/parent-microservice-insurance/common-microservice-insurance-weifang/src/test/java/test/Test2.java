package test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class Test2 extends AbstractChaoJiYingHandler{
	
	static String driverPath = "D:\\ChromeDriver\\chromedriver.exe";

	static Boolean headless = false;
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) throws Exception{
		loginBySelenium();
	}
	
	public static void loginBySelenium() throws Exception{
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
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
		
		String path = WebDriverUnit.saveImg(driver, By.id("validatecode1"), new File("D:\\img\\zhanjiang.jpg")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 

		WebElement button = driver.findElement(By.name("login_btn"));
		
		username.sendKeys("G19040754");
		password.sendKeys("gl666124");
		IMAGCHECK.sendKeys(code);
		button.click();
//		Thread.sleep(10000);
		Alert alert = null;
		try {
			alert = wait.until(new Function<WebDriver, Alert>() {  
	            public Alert apply(WebDriver driver) {  
	                return driver.switchTo().alert(); 
	            }
	        });
		} catch (Exception e) {
			System.out.println("没有alert提示框");
			
		}
		//https://www.sdwfhrss.gov.cn/hsp/mainFrame.jsp?&__usersession_uuid=USERSESSION_8d06038f_74ac_4aac_a902_2460d3476f1e&_width=1600&_height=458
		//https://www.sdwfhrss.gov.cn/hsp/mainFrame.jsp?&__usersession_uuid=USERSESSION_b34a0ecb_aee8_45d4_985a_8ff2de742189&_width=1600&_height=794
		if (driver.getCurrentUrl().indexOf("https://www.sdwfhrss.gov.cn/hsp/mainFrame.jsp")!=-1) {
			System.out.println("登陆成功");
		}
		System.out.println(driver.getCurrentUrl());
		String filename=driver.getCurrentUrl();
		int begin=filename.indexOf("__usersession_uuid=");
		int last=filename.indexOf("&_width");
		System.out.println(filename.substring(begin+19,last)); 
		String __usersession_uuid=filename.substring(begin+19,last);

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
		
		WebClient webClient= addcookie(cookieJson);
		//perinfodel
		
//		WebRequest requestSettings = new WebRequest(new URL("http://60.216.99.138/hsp/logon.do"), HttpMethod.POST);
//		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//		requestSettings.getRequestParameters().add(new NameValuePair("method", "doLogon"));
//		requestSettings.getRequestParameters()
//				.add(new NameValuePair("_xmlString",
//						"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s userid=\"" + parameter.getUsername()
//								+ "\"/><s usermm=\"" + md5 + "\"/><s authcode=\"" + code1
//								+ "\"/><s yxzjlx=\"A\"/><s appversion=\"" + hhz + "\"/><s dlfs=\"1\"/></p>"));
		
//		method: doLogon
//		_xmlString: <?xml version="1.0" encoding="UTF-8"?><p><s userid="G19040754"/><s usermm="bc140e3b1db25d551ffc9e3dd937d153"/><s authcode="1399"/>
//      <s yxzjlx="A"/><s appversion="985339811187291166723"/><s dlfs="3"/></p>
//		_random: 0.17697590750054282


//		String infoUrl = "http://219.132.4.6:6012/web/ajax.do?r=0.7635891010868991&_isModel=true&params={'oper':'JbgrxxcxAction.query','params':{'MenuId':'104014'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129'}}}}";
//		driver.get(infoUrl);
//		System.out.println("infopage"+driver.getPageSource());
//		Document document = Jsoup.parse(driver.getPageSource());
//		String html = document.body().text();
//		System.out.println("要解析的html"+html);
//		JsonParser parser = new JsonParser();
//		JsonObject obj = (JsonObject)parser.parse(html);
//		JsonObject datas = obj.get("datas").getAsJsonObject();
//		JsonObject personInfo = datas.get("ncm_gt_个人基本资料").getAsJsonObject();
//		JsonObject params = personInfo.get("params").getAsJsonObject();
//		String idNum = params.get("证件号码").getAsString();
//		System.out.println(idNum);
//		Thread.sleep(500);
//		
//		for (int i = 1; i < 6; i++) {
//			String Url = "http://219.132.4.6:6012/web/ajax.do?_isModel=true&params={'oper':'ZbgrjfqkcxAction.query','params':{'MenuId':'104020'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129','险种类型':'"+i+"0'}},'ncm_glt_个人已缴历史明细':{'heads':[],'params':{'pageSize':500,'curPageNum':1,'rowsCount':0,'Total_showMsg':null,'Total_showMsgCell':null,'Total_Cols':[]},'heads_change':[],'dataset':[]}}}";
//			driver.get(Url);
//			System.out.println(i+"----"+driver.getPageSource());
//			Thread.sleep(500);
//		}
		
		

	}
	
	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	public static WebClient addcookie(String cookiesIn) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookiesIn);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	

}
