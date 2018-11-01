package testt;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.ChaoJiYingOcrService;

public class TestWLMJ extends AbstractChaoJiYingHandler{

	static String driverPath = "D:\\ChromeDriver\\chromedriver.exe";

	static Boolean headless = false;
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception{
//		login();
		loginByPOST();
//		loginBySelenium();
	}
	
	
	public static void login() throws Exception{
		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		
		String loginUrl = "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
//		System.out.println("loginPage---"+loginPage.asXml());
		
		HtmlTextInput username = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='username']");
		HtmlPasswordInput in_password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='in_password']");
		HtmlTextInput captcha = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='captcha']");
		HtmlImage captcha_img = (HtmlImage)loginPage.getFirstByXPath("//img[@id='captcha_img']");
		HtmlButtonInput loginbtn = loginPage.getFirstByXPath("//input[@id='gr_login']");
		
		if(null != loginbtn){
			System.out.println("123123");
		}else{
			System.out.println("321321");
		}
		
		File file = new File("E:\\Codeimg\\wulumuqi.jpg");
		captcha_img.saveAs(file);
		
		String code = chaoJiYingOcrService.getVerifycode(captcha_img, "1004");
		System.out.println("验证码是==》"+code);
		
		username.setText("13565860057");
		in_password.setText("801013");
		captcha.setText(code);
		
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		final Invocable invocable = (Invocable) engine;  
		HtmlPage loginedPage1 = (HtmlPage) invocable.invokeFunction("individualSubmitForm");
//		HtmlPage loginedPage1 = loginbtn.click();
		System.out.println("loginedPage1---"+loginedPage1.asXml());
		
		
	}
	
	public static void loginByPOST() throws Exception{
		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		
		String loginUrl = "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		
		HtmlImage captcha_img = (HtmlImage)loginPage.getFirstByXPath("//img[@id='captcha_img']");
		File file = new File("E:\\Codeimg\\wulumuqi.jpg");
		captcha_img.saveAs(file);
		
		String code = chaoJiYingOcrService.getVerifycode(captcha_img, "1004");
		System.out.println("验证码是==》"+code);
		
		String url = "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
		String reqBody = "grloginDxyz=0&username=13565860057&password=88eaf2901bfd0631c76e775f9acb71c6&force=&captcha="+code.trim()+"&sliderCaptcha=";
		webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "www.wlmqgjj.com:18080");
//		webRequest.setAdditionalHeader("Origin", "http://www.wlmqgjj.com:18080");
//		webRequest.setAdditionalHeader("Referer", "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");		
		webRequest.setRequestBody(reqBody);
		Page page = webClient.getPage(webRequest);
		Thread.sleep(2000);
		System.out.println("logined======"+page.getWebResponse().getContentAsString());
		
		String payurl = "http://www.wlmqgjj.com:18080/wt-web-gr/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2000-01-01&jsrq=2018-04-24&grxx=016161220072159378&fontSize=13px&pageNum=1&pageSize=500";
		webRequest = new WebRequest(new URL(payurl), HttpMethod.GET);
		Page payPage = webClient.getPage(webRequest);
		System.out.println("流水信息是--"+payPage.getWebResponse().getContentAsString());
		
		String userurl = "http://www.wlmqgjj.com:18080/wt-web-gr/jcr/jcrkhxxcx_mh.service";
		String reqbody2 = "grxx=grbh&ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		webRequest = new WebRequest(new URL(userurl), HttpMethod.POST);
		webRequest.setRequestBody(reqbody2);
		Page userinfoPage = webClient.getPage(webRequest);
		System.out.println("个人信息是--"+userinfoPage.getWebResponse().getContentAsString());
		
	}
	
	public static void loginBySelenium() throws Exception{
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String baseUrl = "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
		driver.get(baseUrl);
		WebDriverWait wait=new WebDriverWait(driver, 10);
		/*WebElement notice= wait.until(new ExpectedCondition<WebElement>() {  
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
		loginFrame.click();*/

		WebElement username= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.id("username"));  
            } 
        });
		
		WebElement in_password = driver.findElement(By.id("in_password"));
		WebElement captcha = driver.findElement(By.id("captcha"));
		
		String path = WebDriverUnit.saveImg(driver, By.id("captcha_img"), new File("E:\\Codeimg\\wulumuqi.jpg")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 

		WebElement button = driver.findElement(By.id("gr_login"));
		
		username.sendKeys("13565860057");
		in_password.sendKeys("801013");
		captcha.sendKeys(code);
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
//			alert.accept();
		}else{
			System.out.println("没有提示框~");
		}
		
//		WebElement sendButton = driver.findElement(By.id("fsyzmdiv"));
		
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
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
}
