package app.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.web.server.WebHandler;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLoginByCard extends AbstractChaoJiYingHandler{
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";
	private static String SKEY = "";
	private static WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
	private static WebDriver driver = null;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) {
		
		String baseUrl = "http://accounts.ccb.com/tran/WCCMainB1L1?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainB1L1&TXCODE=E3CX00";
		try {
			seleniumLogin(baseUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		
//		String detail = "http://accounts.ccb.com/accounts/list_savings_ope.gsp?query=true&q_from_date=2015-07-26&q_to_date=2017-10-25&to_page=1";
//		
//		try {
//			WebRequest request = new WebRequest(new URL(detail),HttpMethod.GET);
//			HtmlPage page = webClient.getPage(request);			
//			System.out.println("*****************************************************    该账户流水");
//			System.out.println(page.getWebResponse().getContentAsString());
//			parser(page.getWebResponse().getContentAsString());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (FailingHttpStatusCodeException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
//		
		
	}

	private static void parser(String contentAsString) {
		Document doc = Jsoup.parse(contentAsString);
		Element a = doc.select("a[title=最后一页]").first();
		String class1 = a.attr("onclick");
		String page = class1.substring(10, class1.length()-2);
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa          "+page);
		Element table = doc.getElementById("t_data");
		Elements trs = table.select("tr");
		if(null != trs && trs.size()>3){
			for(int i=1;i<trs.size()-1;i++){
				Element tr = trs.get(i);
				String tallyDate = tr.child(0).text();
				String dealDate = tr.child(1).text();
				String dealPlace = tr.child(2).text();
				String expend = tr.child(3).text();
				String income = tr.child(4).text();
				String balance = tr.child(5).text();
				String currency = tr.child(6).text();
				String digest = tr.child(7).text();
				
				System.out.println("记账日期 : "+tallyDate);
				System.out.println("交易日期: "+dealDate);
				System.out.println("交易地点 : "+dealPlace);
				System.out.println("支出 : "+expend);
				System.out.println("存入 : "+income);
				System.out.println("余额 : "+balance);
				System.out.println("币种 : "+currency);
				System.out.println("摘要 : "+digest);
				System.out.println("*************************************************当前条数   "+i);

			}
		}
	}

	private static void seleniumLogin(String baseUrl) throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath); 
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu"); 
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(chromeOptions);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(baseUrl);
		
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page 
		WebElement ele = driver.findElement(By.id("result1")); 
		Point point = ele.getLocation(); 
		
		driver.switchTo().frame(ele);
		writer(driver.getPageSource(),"C:/home/frame.txt");
		//用户名输入框
		WebElement usernameInput = driver.findElement(By.name("ACC_NO_temp"));
//		System.out.println("*******************************************");
//		System.out.println(usernameInput.toString());
		//密码输入框
		WebElement passwordInput = driver.findElement(By.name("LOGPASS"));			
		//图片验证码输入框
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		//点击登录按钮
		WebElement button = driver.findElement(By.className("btn"));
		
		//有数字键盘，改成用本地键盘录入。
		passwordInput.click();
		//使用键盘输入的按钮
		WebElement keyButton = driver.findElement(By.className("keyInfoBtn"));
		keyButton.click();
		usernameInput.sendKeys("6253624055106106");
//		passwordInput.sendKeys("147258");
		passwordInput.sendKeys("213456");
		
		String path = WebDriverUnit.saveImg(driver,By.id("fujiama"),point.getX(),point.getY());
		
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
		
		imgInput.sendKeys(code);
		button.click();
		Thread.sleep(3000L); 
		System.out.println("*************************************   点击登录按钮后");
		
		
		String url = driver.getCurrentUrl();
		if(url.contains("TXCODE")){
			WebElement error = driver.findElement(By.cssSelector("p.fs_16 + p"));
			System.out.println(error.getText());
		}
		
//		driver.switchTo().frame("result1"); 
//		http://accounts.ccb.com/tran/WCCMainB1L1?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainB1L1&TXCODE=E3CX00
		
		writer(driver.getPageSource(),"C:/home/hahahahahahha.txt");
//		System.out.println(driver.getPageSource());
		
//		parserUserinfo(driver.getPageSource());
		
//		Set<org.openqa.selenium.Cookie> cookies1 = driver.manage().getCookies();
//		for(org.openqa.selenium.Cookie cookie : cookies1){
//			Cookie cookieWebClient = new Cookie("accounts.ccb.com", cookie.getName(), cookie.getValue());
//			webClient.getCookieManager().addCookie(cookieWebClient);
//		}
		
//		driver.switchTo().frame("result1");
//		System.out.println(driver.getPageSource());
//		WebElement startDate = driver.findElement(By.id("startDate"));
//		WebElement endDate = driver.findElement(By.id("endDate"));
//		WebElement imageField = driver.findElement(By.name("imageField"));
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		startDate.sendKeys("20150101");
//		endDate.sendKeys(sdf.format(new Date()));
//		
//		imageField.click();
		
//		driver.switchTo().frame("deal_iframe1");
		WebElement accountSearch = driver.findElement(By.id("div_E13202"));
		accountSearch.click();
		Thread.sleep(500l);
		writer(driver.getPageSource(),"C:/home/accountSearch.txt");
		driver.switchTo().frame("result1");
		writer(driver.getPageSource(),"C:/home/ccb.txt");
		
		
//	String UNEdDt = driver.findElement(By.id("UNEdDt")).getAttribute("value");
//	String TXCODE = driver.findElement(By.id("TXCODE")).getAttribute("value");
		
		List<WebElement> bills = driver.findElements(By.className("bill_search"));
		String handle = driver.getWindowHandle();

		bills.get(0).click();
		Thread.sleep(500);
////		System.out.println(driver.getPageSource());
		writer(driver.getPageSource(),"C:/home/billccb0.txt");
		driver.switchTo().window(handle);
		System.out.println("***********************************************************driver");
//		System.out.println(driver.getPageSource());
		Thread.sleep(1000);

		for(int i=1;i<bills.size();i++){
			
			accountSearch = driver.findElement(By.id("div_E13202"));
			accountSearch.click();
			Thread.sleep(500l);
			writer(driver.getPageSource(),"C:/home/accountSearch.txt");
			driver.switchTo().frame("result1");
			writer(driver.getPageSource(),"C:/home/ccb.txt");
			
//		String UNEdDt = driver.findElement(By.id("UNEdDt")).getAttribute("value");
//		String TXCODE = driver.findElement(By.id("TXCODE")).getAttribute("value");
			
			bills = driver.findElements(By.className("bill_search"));		
			System.out.println("===========================================================");
			System.out.println("当前的bills的长度："+bills.size()+"    当前的i的数值："+i);
			bills.get(i).click();
			Thread.sleep(500l);
////			System.out.println(driver.getPageSource());
			writer(driver.getPageSource(),"C:/home/billccb"+i+".txt");
			driver.switchTo().window(handle);
			System.out.println("***********************************************************driver");
//			System.out.println(driver.getPageSource());
			Thread.sleep(1000);
		}

//		Set<org.openqa.selenium.Cookie> cookies1 = driver.manage().getCookies();
//		for(org.openqa.selenium.Cookie cookie : cookies1){
//			System.out.println(cookie.getName()+":  "+cookie.getValue());
//			Cookie cookieWebClient = new Cookie("accounts.ccb.com", cookie.getName(), cookie.getValue());
//			webClient.getCookieManager().addCookie(cookieWebClient);
//		}
//		
//		WebRequest requestSettings = new WebRequest(new URL(detail),HttpMethod.POST);
//		
//		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//	    requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//	    requestSettings.setAdditionalHeader("Origin", "http://accounts.ccb.com");
//	    requestSettings.setAdditionalHeader("Host", "accounts.ccb.com");
//	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
//	    requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
//	    requestSettings.setAdditionalHeader("Referer", "http://accounts.ccb.com/tran/WCCMainPlatV5?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainPlatV5");
//	    requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1"); 
//	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//	    
////	    requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
////		requestSettings.getRequestParameters().add(new NameValuePair("CCB_IBSVersion", "V5"));
////		requestSettings.getRequestParameters().add(new NameValuePair("SERVLET_NAME", "WCCMainB1L1"));
//		requestSettings.setRequestBody(requestBody);
//		HtmlPage page = webClient.getPage(requestSettings);		
//		System.out.println("requestBody  : "+requestBody);
//		System.out.println("*****************************************************    该账户流水");
//		System.out.println(page.getWebResponse().getContentAsString());
//		
//		
////		System.out.println(driver.getPageSource());
//		System.out.println("UNEdDt ------------------------->"+UNEdDt);
//		System.out.println("TXCODE ------------------------->"+TXCODE);
//		System.out.println("UniEncodeStr ------------------------->"+UniEncodeStr);
//		driver.quit();
	}

	private static void parserUserinfo(String pageSource) {
		Document doc = Jsoup.parse(pageSource);
		String name = doc.select("span.f_14_blue").get(0).text();
		String account = doc.select("span.f_14_blue").get(1).text().replace("账号： ", "");
		String accountType = doc.select("#dline").get(0).text();
		String currency = doc.select("#dline").get(1).text();
		String openDate = doc.select("#dline").get(2).text();
		String balance = doc.select("#dline").get(3).text();
		String accountStatus = doc.select("#dline").get(5).text();
		
		System.out.println("************************************************************************");
		System.out.println("姓名   ："+name);
		System.out.println("账户   ："+account);
		System.out.println("账户类型   ："+accountType);
		System.out.println("币种   ："+currency);
		System.out.println("开户日期   ："+openDate);
		System.out.println("账户余额   ："+balance);
		System.out.println("账户状态   ："+accountStatus);
	}

	public static void writer(String page, String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 byte bt[] = new byte[1024];  
	        bt = page.getBytes();  
	        try {  
	            FileOutputStream in = new FileOutputStream(file);  
	            try {  
	                in.write(bt, 0, bt.length);  
	                in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	}
	
	
	
	
}
