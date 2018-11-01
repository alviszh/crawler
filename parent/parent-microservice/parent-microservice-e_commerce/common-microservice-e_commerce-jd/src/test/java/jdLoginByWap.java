import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

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

import app.crawler.htmlparse.HtmlParse;

/**   
*    
* 项目名称：common-microservice-e_commerce-jd   
* 类名称：jdLoginByWap   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年8月20日 上午10:31:33   
* @version        
*/
public class jdLoginByWap {

	static String driverPath = "C:\\chromedriver.exe";

	static Boolean headless = false;

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

	/*
	 * public static String loginChromeByerweima() throws Exception { WebDriver
	 * driver = intiChrome(); // WebDriver driver = new RemoteWebDriver(new //
	 * URL("http://10.167.202.218:32768//wd/hub/"), //
	 * DesiredCapabilities.chrome());
	 * driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	 * driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); String
	 * baseUrl = "https://passport.jd.com/uc/login?ltype=logout";
	 * driver.get(baseUrl);
	 * 
	 * 
	 * String path = WebDriverUnit.saveImg(driver, By.className("qrcode-img"));
	 * 
	 * BufferedImage image = ImageIO.read(new FileInputStream(path));
	 * 
	 * JFrame f2 = new JFrame(); JLabel l = new JLabel(); l.setIcon(new
	 * ImageIcon(image)); f2.getContentPane().add(l); f2.setSize(100, 100);
	 * f2.setTitle("验证码"); f2.setVisible(true);
	 * 
	 * 
	 * return null;
	 * 
	 * }
	 */

	public static String loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		// WebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.202.218:32768//wd/hub/"),
		// DesiredCapabilities.chrome());
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
//		String baseUrl = "https://plogin.m.jd.com/user/login.action";
		String baseUrl = "https://plogin.m.jd.com/user/login.action?appid=461&returnurl=http%3A%2F%2Fhome.m.jd.com%2FmyJd%2Fhome.action&ipChanged=";
		
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("username"));
			}
		});

		loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("username"));
			}
		});
		loginname.sendKeys("18712856001");

		driver.findElement(By.id("password")).sendKeys("hanaiwei1314");
		
		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		driver.findElement(By.id("code")).sendKeys(valicodeStr);

		driver.findElement(By.id("loginBtn")).click();
//		String htmlsource3 = driver.getPageSource();
		Thread.sleep(10000);
		// System.out.println(htmlsource3);

		/*String html = getCoffers(driver);
		 HtmlParse.coffersParse(html, null);*/
		//getReceiverAddress(driver);
		/*driver = getIndent(driver);
		String html = driver.getPageSource();
		HtmlParse.indentParse(html, null);*/
		// driver = getRenZheng(driver);
		/*driver = getQueryBindCard(driver);
		String html = driver.getPageSource();
		HtmlParse.queryBindCardParse(html, null);*/
		driver = getUserInfo(driver);
		String html = driver.getPageSource();
		HtmlParse.userInfoParse(html, null);
		
		return null;
		//
	}
	
	public static WebDriver getBtPrivilege(WebDriver driver) throws Exception {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("baitiao.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String url = "https://baitiao.jd.com/v3/ious/getBtPrivilege";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "baitiao.jd.com");
		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());

		// 获取小白信用分
		url = "https://baitiao.jd.com/v3/ious/score_getScoreInfo";

		webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "baitiao.jd.com");
		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");

		searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());

		return driver;
	}

	// https://easybuy.jd.com/address/getEasyBuyList.action
	public static WebDriver getReceiverAddress(WebDriver driver) throws Exception {

		String url = "https://i.jd.com/user/info";

		driver.get(url);
		Thread.sleep(5000);
		driver.findElement(By.linkText("收货地址")).click();
		
		System.out.println("=================================================================================");
		Thread.sleep(5000);

		String htmlsource3 = driver.getPageSource();
		System.out.println(htmlsource3);

		return driver;
	}

	public static WebDriver getUserInfo(WebDriver driver) throws Exception {
		String url = "https://i.jd.com/user/info";

		driver.get(url);
		return driver;

	}

	public static WebDriver getRenZheng(WebDriver driver) throws Exception {
		String url = "https://authpay.jd.com/auth/toAuthSuccessPage.action";

		driver.get(url);

		String htmlsource3 = driver.getPageSource();
		System.out.println(htmlsource3);

		return driver;
	}
	
	public static WebDriver getQueryBindCard(WebDriver driver) throws Exception {
		String url = "https://authpay.jd.com/card/queryBindCard.action";

		driver.get(url);

		String htmlsource3 = driver.getPageSource();
		System.out.println(htmlsource3);

		return driver;
	}
	
	public static String getCoffers(WebDriver driver) throws Exception {
		String url = "https://xjk.jr.jd.com/gold/account";

		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("xjk.jr.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "xjk.jr.jd.com");
		//webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://xjk.jr.jd.com/gold/page.htm");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());
		return searchPage.getWebResponse().getContentAsString();
	}
	public static WebDriver getIndent(WebDriver driver) throws Exception {
		String url = "https://order.jd.com/center/list.action?d=2015&page=1";

		driver.get(url);

		String htmlsource3 = driver.getPageSource();
		System.out.println(htmlsource3);

		return driver;
	}
	
	/**   
	  *    
	  * 项目名称：common-microservice-e_commerce-jd  
	  * 所属包名：
	  * 类描述：   废弃方法
	  * 创建人：hyx 
	  * 创建时间：2017年12月21日 
	  * @version 1  
	  * 返回值    WebDriver
	  */
	public static WebDriver getIndentDetails(WebDriver driver) throws Exception {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("baitiao.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String url = "https://details.jd.com/normal/item.action?orderid=64460173781&PassKey=6B35C59D771C3EAFE248095488E02176";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "baitiao.jd.com");
		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());

		return driver;
	}
	//https://order.jd.com/center/list.action
	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
