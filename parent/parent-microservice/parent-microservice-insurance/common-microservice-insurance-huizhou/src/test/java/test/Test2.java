package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.ChaoJiYingOcrService;

public class Test2 extends AbstractChaoJiYingHandler{
	
	static String driverPath = "D:\\ChromeServer\\chromedriver.exe";

	static Boolean headless = false;
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) throws Exception{
		/*String url = "http://219.132.4.6:6012/web/ggfw/app/index.html#/ggfw/home";
		String loginImgUrl = "http://219.132.4.6:6012/web/ImageCheck.jpg";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Page page = webClient.getPage(url);
		WebRequest webRequest = new WebRequest(new URL(loginImgUrl), HttpMethod.GET);
		Page loginPage = webClient.getPage(webRequest);
//		File file = new File("E:\\Codeimg\\zhanjiang.jpg");
		InputStream inputStream = loginPage.getWebResponse().getContentAsStream();
		
		FileOutputStream fileOut = new FileOutputStream(new File("E:\\Codeimg\\zhanjiang.jpg"));  
        byte[] buf = new byte[1024 * 8];  
        while (true) {  
            int read = 0;  
            if (inputStream != null) {  
                read = inputStream.read(buf);  
            }  
            if (read == -1) {  
                break;  
            }  
            fileOut.write(buf, 0, read);  
        } 
        
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		
		String loginUrl = "http://219.132.4.6:6012/web/ajaxlogin.do";
		webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "219.132.4.6:6012");
		webRequest.setAdditionalHeader("Origin", "http://219.132.4.6:6012");
		webRequest.setAdditionalHeader("Referer", "http://219.132.4.6:6012/web/ggfw/app/index.html");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("LOGINID=440803199106231129&&PASSWORD=32313a3230373a3233323a3135373a3230333a3231333a3234333a3138323a3133323a3138373a3232313a3134333a38363a33333a3139303a3139393a3131353a3133323a35323a31393a3130373a3232393a38393a31363a32313a3230373a3134373a36363a35373a3232363a3138333a3235&&IMAGCHECK="+input+"&&OPERTYPE2=3");
		Page loginedPage = webClient.getPage(webRequest);
		System.out.println("loginedPage===>"+loginedPage.getWebResponse().getContentAsString());*/
		loginBySelenium();
	}
	
	public static void loginBySelenium() throws Exception{
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		String baseUrl = "http://219.132.4.6:6012/web/ggfw/app/index.html";
		driver.get(baseUrl);
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
		WebElement notice= wait.until(new ExpectedCondition<WebElement>() {  
		            public WebElement apply(WebDriver driver) {  
		                return driver.findElement(By.xpath("//a[@title='cancel']"));  
		            } 
		        });
		notice.click();
		
		WebElement loginFrame= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.xpath("//a[@ng-click='login()']"));  
            } 
        });
		loginFrame.click();

		WebElement LOGINID= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.name("LOGINID"));  
            } 
        });
		
//		WebElement LOGINID = driver.findElement(By.name("LOGINID"));
		WebElement PASSWORD1 = driver.findElement(By.name("PASSWORD1"));
		WebElement IMAGCHECK = driver.findElement(By.name("IMAGCHECK"));
		
		String path = WebDriverUnit.saveImg(driver, By.id("authImg"), new File("D:\\img\\zhanjiang.jpg")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 

		WebElement button = driver.findElement(By.className("button"));
		
		LOGINID.sendKeys("440803199106231129");
		PASSWORD1.sendKeys("xian873569770.");
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
		
		if(null != alert){
			String alertText = alert.getText();
			System.out.println("弹框提示=="+alertText);
			alert.accept();
		}
		
		Thread.sleep(1000);
		System.out.println("page--->"+driver.getPageSource());
		Document doc = Jsoup.parse(driver.getPageSource());
		String username = doc.body().select(".ng-binding").text();
		System.out.println("登陆后的名字==》"+username);
		String msg = username.substring(4);
		if(msg.contains("登录失败")){
			System.out.println("login Fail"+msg);
		}else{
			System.out.println("login success"+msg);
		}
		
		String infoUrl = "http://219.132.4.6:6012/web/ajax.do?r=0.7635891010868991&_isModel=true&params={'oper':'JbgrxxcxAction.query','params':{'MenuId':'104014'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129'}}}}";
		driver.get(infoUrl);
		System.out.println("infopage"+driver.getPageSource());
		Document document = Jsoup.parse(driver.getPageSource());
		String html = document.body().text();
		System.out.println("要解析的html"+html);
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject)parser.parse(html);
		JsonObject datas = obj.get("datas").getAsJsonObject();
		JsonObject personInfo = datas.get("ncm_gt_个人基本资料").getAsJsonObject();
		JsonObject params = personInfo.get("params").getAsJsonObject();
		String idNum = params.get("证件号码").getAsString();
		System.out.println(idNum);
		Thread.sleep(500);
		
		for (int i = 1; i < 6; i++) {
			String Url = "http://219.132.4.6:6012/web/ajax.do?_isModel=true&params={'oper':'ZbgrjfqkcxAction.query','params':{'MenuId':'104020'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129','险种类型':'"+i+"0'}},'ncm_glt_个人已缴历史明细':{'heads':[],'params':{'pageSize':500,'curPageNum':1,'rowsCount':0,'Total_showMsg':null,'Total_showMsgCell':null,'Total_Cols':[]},'heads_change':[],'dataset':[]}}}";
			driver.get(Url);
			System.out.println(i+"----"+driver.getPageSource());
			Thread.sleep(500);
		}
		
		
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
//		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
//			Cookie cookieWebClient = new Cookie("219.132.4.6:6012", cookie.getName(), cookie.getValue());
//			webClient.getCookieManager().addCookie(cookieWebClient);
//		}
//		
//		WebRequest webRequest = new WebRequest(new URL(infoUrl), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		webRequest.setAdditionalHeader("Host", "219.132.4.6:6012");
//		webRequest.setAdditionalHeader("Origin", "http://219.132.4.6:6012");
//		webRequest.setAdditionalHeader("Referer", "http://219.132.4.6:6012/web/ggfw/app/index.html");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		webRequest.setRequestBody("_isModel=true&params={'oper':'JbgrxxcxAction.query','params':{'MenuId':'104014'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129'}}}}");
//		Page infoPage = webClient.getPage(webRequest);
//		System.out.println("loginedPage===>"+infoPage.getWebResponse().getContentAsString());
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
	
	public static String js(String e) throws Exception{
		
		String g = "";
		String j = "";
		String[] k = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
		for (int f = 0; f < e.length(); f++){
			if(f == 0){
				g = Integer.toHexString((int)e.charAt(f));
			}else{
				g += Integer.toHexString((int)e.charAt(f));
			}
			System.out.println("g==="+g);
			for (f = 0; f < g.length(); f++){
				j += k[g.charAt(f) >> 4] + k[g.charAt(f) & 15];
			}
		}
		
		return j;
	}
}
