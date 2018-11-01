/**   
*    
* 项目名称：common-microservice-telecom-login   
* 类名称：helongintest   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年3月6日 下午2:23:12   
* @version        
*/

import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;
import app.bean.ValidationLoginDataObject;
import app.bean.ValidationLoginRoot;
import app.crawler.telecom.htmlparse.TelecomParseCommon;
import app.service.common.LoginAndGetCommon;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：jdlogin 类描述： 创建人：hyx
 * 创建时间：2017年12月8日 下午6:04:36
 * 
 * @version
 */
public class helongintest {

	static String driverPath = "C:\\chromedriver.exe";
	//private static String driverPath = "D:\\software\\IEDriverServer_Win32\\chromedriver.exe";

	static Boolean headless = false;

	protected static Gson gs = new Gson();

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

	public static String loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://login.189.cn/web/login";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("txtAccount"));
			}
		});

		loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("txtAccount"));
			}
		});
		loginname.sendKeys("18131003055");
		driver.findElement(By.id("txtShowPwd")).click();
		driver.findElement(By.id("txtPassword")).sendKeys("900628");
		String code = JOptionPane.showInputDialog("请输入验证码：");
		driver.findElement(By.id("txtCaptcha")).sendKeys(code.trim());

		driver.findElement(By.id("loginbtn")).click();
		Thread.sleep(5000);
		System.out.println("sucess");
		String htmlsource3 = driver.getPageSource();
		System.out.println("============" + driver.getCurrentUrl());
		System.out.println("====================clieck===================");
		
		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00380407"); 
		 wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement senbutton2 = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("bodyIframe"));
			}
		});
		System.out.println("======================");
		driver.switchTo().frame("bodyIframe");
		
		String citycodeForHeBei = TelecomParseCommon.parserCityCodeForHeBei(driver.getPageSource());
		System.out.println(citycodeForHeBei);
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Map<String, String> cookiemap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			cookiemap.put(cookie.getName(), cookie.getValue());
			System.out.println(
					cookieWebClient.getDomain() + ":" + cookieWebClient.getName() + ":" + cookieWebClient.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		driver.close();
		sendsms(webClient);
		return null;
	}
	private static void getCallRec(WebClient webClient,String sms) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();  
		calendar.add(Calendar.MONTH, -1);  
		calendar.set(Calendar.DAY_OF_MONTH, 1);		
		String BEGIN_DATE = sdf.format(calendar.getTime());
		
		calendar.add(Calendar.MONTH, 1);  
		calendar.set(Calendar.DAY_OF_MONTH, 0);  
		System.out.println(sdf.format(calendar.getTime()));
		String END_DATE = sdf.format(calendar.getTime());
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		c1.add(Calendar.MONTH, -1);
		Date date1 = c1.getTime();
		String currentmon = sdf2.format(date1);
		
		String callUrl = "http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp";
		System.out.println("通话记录的url："+callUrl);
		Page page = getPage(callUrl, webClient,sms);
		
		System.out.println("通话记录page："+page.getWebResponse().getContentAsString());
	}



	private static void sendsms(WebClient webClient) throws Exception {
		String smsUrl = "http://he.189.cn/service/transaction/postValidCode.jsp?"
				+ "reDo=Tue+Feb+27+2018+14%3A39%3A53+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)"
				+ "&OPER_TYPE=CR1&RAND_TYPE=006&PRODTYPE=2020966&MOBILE_NAME=18131003055&MOBILE_NAME_PWD=&NUM=18131003055&AREA_CODE=186&LOGIN_TYPE=21";
		
		XmlPage page = (XmlPage) LoginAndGetCommon.getHtml(smsUrl, webClient);
		System.out.println("发送短信后返送的内容："+page.getWebResponse().getContentAsString());
		
		
	}

	private static ValidationLoginDataObject ValidationLogin(WebClient webClient) {

		try {
			String url = "http://www.189.cn/login/index.do";
			Page page = getHtml(url, webClient);

			System.out.println("*************************************** index.do");
			System.out.println(page.getWebResponse().getContentAsString());

			ValidationLoginRoot jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(),
					ValidationLoginRoot.class);

			return jsonObject.getDataObject();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "www.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/html/login/right.html");
		webRequest.setAdditionalHeader("Origin", "http://www.czgjj.com");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	
	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Page getPage(String url, WebClient webClient,String sms) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");

		webRequest.setAdditionalHeader("Host", "he.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he");
		webRequest.setAdditionalHeader("Origin", "http://he.189.cn");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("ACC_NBR", "18131003055"));
		webRequest.getRequestParameters().add(new NameValuePair("CITY_CODE", "186"));
		webRequest.getRequestParameters().add(new NameValuePair("BEGIN_DATE", "2018-02-01 00:00:00"));
		webRequest.getRequestParameters().add(new NameValuePair("END_DATE", "2018-02-28 23:59:59"));
		webRequest.getRequestParameters().add(new NameValuePair("FEE_DATE", "201802"));
		webRequest.getRequestParameters().add(new NameValuePair("SERVICE_KIND", "8"));
		webRequest.getRequestParameters().add(new NameValuePair("retCode", "0000"));
		webRequest.getRequestParameters().add(new NameValuePair("QUERY_FLAG", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("QUERY_TYPE_NAME", "移动语音详单"));
		webRequest.getRequestParameters().add(new NameValuePair("QUERY_TYPE", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("radioQryType", "on"));
		webRequest.getRequestParameters().add(new NameValuePair("QRY_FLAG", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("ACCT_DATE", "201802"));
		webRequest.getRequestParameters().add(new NameValuePair("ACCT_DATE_1", "201803"));
		webRequest.getRequestParameters().add(new NameValuePair("sjmput", sms));
		
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	
	public static String parserCityCode(String html){
		Document doc = Jsoup.parse(html);
		String cityCode = doc.select("[name=AREA_CODE]").first().val();
		System.out.println("城市代码："+cityCode);
		return cityCode;
	}
}

