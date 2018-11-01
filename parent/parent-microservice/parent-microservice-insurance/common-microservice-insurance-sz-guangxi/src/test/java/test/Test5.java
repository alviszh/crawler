package test;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class Test5 extends AbstractChaoJiYingHandler{
	
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
		String baseUrl = "https://gx12333.net/index/loginInit.html";
		driver.get(baseUrl);
		Thread.sleep(1500);
	    WebDriverWait wait=new WebDriverWait(driver, 10);
	
		WebElement LOGINID= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.id("aac002_t"));  
            } 
        });
		
//		WebElement LOGINID = driver.findElement(By.name("LOGINID"));
		WebElement PASSWORD1 = driver.findElement(By.id("aac003_t"));
		WebElement IMAGCHECK = driver.findElement(By.id("yzm_t"));
		
		String path = WebDriverUnit.saveImg(driver, By.id("codeimg"), new File("D:\\img\\guangxi.jpg")); 
    	System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 

		WebElement button = driver.findElement(By.xpath("//button[@type='button']"));
		
		LOGINID.sendKeys("4501211992062047");
		PASSWORD1.sendKeys("123456");
		IMAGCHECK.sendKeys(code);
		button.click();
//		
//		Alert alert = null;		
//		 try {
//	             alert = wait.until(new ExpectedCondition<Alert>() {
//	                @Override
//	                public Alert apply(WebDriver driver) {
//	                    try {
//	                        return driver.switchTo().alert();
//	                    } catch (NoAlertPresentException e) {
//	                        return null;
//	                    }
//	                }
//	            });
//	            alert.accept();
//	        } catch (NullPointerException e) {
//	            /* Ignore */
//	            System.out.println("ff2 nullpoint");
//	        }
//		
//		if(null != alert){
//			String alertText = alert.getText();
//			alert.accept();
//		}		
		Alert alert = driver.switchTo().alert();  
        String text = alert.getText();  
		
		
		Thread.sleep(1000);
		System.out.println("page--->"+driver.getPageSource());
		String html=driver.getPageSource();
		if (html.contains("您好")) {
			System.out.println("login success");
		}else{
			System.out.println("login fail");
		}
	
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
//		
	 	String indexurl="https://gx12333.net/wxService/queryBaseInfo.html";
	 
	 	HtmlPage  indexpage=getHtml(indexurl,  webClient);
		
	 	System.out.println(indexpage.getWebResponse().getContentAsString());
		String body="aae140=0";
		String infoUrl = "https://gx12333.net/wxService/queryInsuInfoAfter.html";
		WebRequest webRequest=  new WebRequest(new URL(infoUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "gx12333.net");
		webRequest.setAdditionalHeader("Origin", "ttps://gx12333.net");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	//Token 1620726984
		webRequest.setRequestBody(body);
		Page page=webClient.getPage(webRequest);
		System.out.println("page="+page.getWebResponse().getContentAsString());

 	
	  	String yiliaoUrl="https://gx12333.net/wxService/queryJFXX.html";	                    
	  	String body2="aae140=&aae003=201705";
		WebRequest webRequest2=  new WebRequest(new URL(yiliaoUrl), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
	   	webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
     	webRequest2.setAdditionalHeader("Host", "gx12333.net");
     	webRequest2.setAdditionalHeader("Origin", "https://gx12333.net");
     	webRequest2.setAdditionalHeader("Referer", "ttps://gx12333.net/wxService/queryJFXXBe.html");	
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	//Token 1620726984
		webRequest2.setRequestBody(body2);
		Page page2=webClient.getPage(webRequest2);
		System.out.println("page2="+page2.getWebResponse().getContentAsString());
	  
    	

	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
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
