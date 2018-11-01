
import java.net.URL;
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
import com.module.htmlunit.WebCrawler;

import app.crawler.htmlparse.InsuranceLianYunGangParseService;


/**
 * 
 * 项目名称：common-microservice-housingfund-yvlin 类名称：yvlintest 类描述： 创建人：hyx
 * 创建时间：2018年1月4日 上午10:44:24
 * 
 * @version
 */
public class lianyungangtest {

	static String driverPath = "C:\\chromedriver.exe";

	static Boolean headless = false;
	
	static InsuranceLianYunGangParseService insuranceLianYunGangParseService = new InsuranceLianYunGangParseService();

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

	public static WebDriver loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		// WebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.202.218:32768//wd/hub/"),
		// DesiredCapabilities.chrome());
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://218.92.102.28:8010/uaa/personlogin#/personLogin";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.name("username"));
			}
		});
		driver.findElement(By.name("username")).sendKeys("320723198911014467");

		driver.findElement(By.name("password")).sendKeys("014467");

		String code = JOptionPane.showInputDialog("请输入验证码：");

		driver.findElement(By.name("captchaWord")).sendKeys(code);

		driver.findElement(By.xpath("//*[@id='loginForm']/div[8]/button")).click();
//		driver.findElement(By.xpath("//*[text()=" + "登录系统" + "]")).click();
		
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
		try {
			wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.className("modal-header"));
				}
			});
		} catch (Exception e) {

			Document doc = Jsoup.parse(driver.getPageSource());
			System.out.println("=======================================================");
			System.out.println(doc.select("div.alert-danger").text());
			System.out.println("=======================================================");
		}

		// driver.close();
		// System.out.println("===="+htmlsource3);
		return driver;
		//
	}
	
	public static void getPayByDriver(WebDriver driver) throws Exception{
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		
		String url = "http://218.92.102.28:8010/ehrss/si/person/ui/?code=5FEnkI#/rights/payment/payinfo";
		
		driver.get(url);
		
		Thread.sleep(1000);
		System.out.println(driver.getPageSource());
		System.out.println("======================================");
		WebElement form =  driver.findElement(By.className("tab-content"));
		System.out.println(form.getText());
		System.out.println("======================================");

		System.out.println(driver.getPageSource());
		
		WebElement yearButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return 	driver.findElement(By.className("btn-default"));
			}
		});
		yearButton.click();
				
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return 	driver.findElement(By.xpath("//*[text()="+2017+"]"));
			}
		});
		loginByUserButton.click();
		driver.findElement(By.xpath("//*[@id='ng-view']/div/div/div[2]/form/div/div[3]/p/input")).click();
		Thread.sleep(1000);
		
		System.out.println(driver.getPageSource());
	}

	public static Page getAccount(WebClient webClient) throws Exception {
		String url = "http://218.92.102.28:8010/api/security/user";
		Page page = getHtml(url, webClient); //

		System.out.println(page.getWebResponse().getContentAsString());
		return page;
	}
	
//	public static Page getUserBasic(WebClient webClient,String persionId,String accoutId) throws Exception {
//		
//		String url = "http://218.92.102.28:8010/ehrss-si-person/api/buzz/person/baseinfo/"
//				+ persionId;
//		
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webClient.setJavaScriptTimeout(50000);
//		webClient.getOptions().setTimeout(50000); // 15->60
//		
//		webClient.addRequestHeader("Accept", "application/json, text/plain, */*");
//		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate");
//
//		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webClient.addRequestHeader("Host", "218.92.102.28:8010");
//		webClient.addRequestHeader("Referer", "http://218.92.102.28:8010/ehrss/si/person/ui/?code=a1Nn7f");
//		webClient.addRequestHeader("X-HMAC-HASH", "4f4cf2f9a3a4aeff82e5c4edba5005b260d309827205dee49fbcb1925a8d3ba5");
//		webClient.addRequestHeader("X-MICRO-TIME", "1521446730826");
//		webClient.addRequestHeader("X-USER", accoutId.trim());
//		Page page = webClient.getPage(webRequest);
//		
//		System.out.println(page.getWebResponse().getContentAsString());
//		return page;
//	}

	public static Page getPay(WebClient webClient, String personId) throws Exception {
		String url = "http://www.sqsbjf2.cn:9888/ehrss-si-person/api/rights/paymenthome/detail/query?"
				+ "endDate=201802&" + "personId=" +"1000000125593139" + "&startDate=200002";

		Page page = getHtml(url, webClient); //

		System.out.println(page.getWebResponse().getContentAsString());

		return page;
	}
	
	public static Page getPersionId(WebClient webClient, String accoutId) throws Exception {
		String url = "http://218.92.102.28:8010/ehrss-si-person/api/rights/persons/overview/"
				+ accoutId.trim();
		
		Page page = getHtml2(url, webClient,accoutId); //
		System.out.println(url);
		System.out.println(page.getWebResponse().getContentAsString());

		return page;
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	public static Page getHtml2(String url, WebClient webClient,String accoutId) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		
		webClient.addRequestHeader("Accept", "application/json, text/plain, */*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate");

		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Host", "218.92.102.28:8010");
		webClient.addRequestHeader("Referer", "http://218.92.102.28:8010/ehrss/si/person/ui/?code=JfPjBB");
		/*webClient.addRequestHeader("X-HMAC-HASH", "744305a687f8be1bd16fc9c91c16fe41dabf4560749a2b30d1731e5c4d5d78f6");
		webClient.addRequestHeader("X-MICRO-TIME", "1521170079822");
		webClient.addRequestHeader("X-USER", accoutId.trim());*/
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static void main(String[] args) {
		try {

			
		/*	 * LocalDate nowdate = LocalDate.now();
			 * 
			 * String beginDate = nowdate.plusYears(-10).getYear() + "01";
			 * String endDate = nowdate.getYear() + "01"; String month =
			 * nowdate.getMonthValue()+""; if(month.length()==1){ month =
			 * "0"+month; }
			 * 
			 * System.out.println(nowdate.getYear()+"====="+month);*/
			 

			WebDriver driver = loginChrome();
		//getPayByDriver(driver);
			
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
			
			Page searchPage = getAccount(webClient);
			System.out.println(searchPage.getWebResponse().getContentAsString());
//		//	InsuranceBasicSuQianChangZhouLianYunGangBean accoutntbean = insuranceLianYunGangParseService.AccountParse(searchPage.getWebResponse().getContentAsString());
//			System.out.println("============================================");
//			System.out.println(accoutntbean.toString());
//			System.out.println("============================================");
//			String accoutId = accoutntbean.getAccount();
//			
//			System.out.println("======="+accoutId);
			
//			Page persionIdPage = getPersionId(webClient, accoutId);
//			
//			PersionIdJsonRootBean persionIdJsonRootBean = insuranceLianYunGangParseService.PersionIdParse(persionIdPage.getWebResponse().getContentAsString());
//			
//			System.out.println("============================================");
//			
//			System.out.println(persionIdJsonRootBean.toString());
//			System.out.println("============================================");
//			System.out.println(persionIdJsonRootBean.getPersonProfile().getId());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
