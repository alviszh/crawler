
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
 * 项目名称：common-microservice-e_commerce-jd 类名称：jdlogin 类描述： 创建人：hyx
 * 创建时间：2017年12月8日 下午6:04:36
 * 
 * @version
 */
public class jdlogin {

	static String driverPath = "F:\\IEDriverServer_Win32\\chromedriver.exe";

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
		String baseUrl = "https://passport.jd.com/uc/login?ltype=logout";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("login-tab-r"));
			}
		});

		loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("loginname"));
			}
		});
		loginname.sendKeys("18712856001");

		driver.findElement(By.id("nloginpwd")).sendKeys("hanaiwei1314");

	
		// System.out.println(htmlsource3);

		/*
		 * String html = getCoffers(driver); HtmlParse.coffersParse(html, null);
		 */
		// getReceiverAddress(driver);
		/*
		 * driver = getIndent(driver); String html = driver.getPageSource();
		 * HtmlParse.indentParse(html, null);
		 */
		// driver = getRenZheng(driver);
		/*
		 * driver = getQueryBindCard(driver); String html =
		 * driver.getPageSource(); HtmlParse.queryBindCardParse(html, null);
		 */

		////////////////////////// 滑动验证码尝试破解////////////////////////
		
		
		/*driver.findElement(By.className("authcode-btn")).click();	
		
		Thread.sleep(2000);
		WebElement element = driver.findElement(By.className("JDJRV-slide-btn"));// (".gt_slider_knob"));
		Point location = element.getLocation();
		element.getSize();
		Actions action = new Actions(driver);
		// action.clickAndHold().perform();// 鼠标在当前位置点击后不释放
		// action.clickAndHold(element).perform();// 鼠标在 onElement 元素的位置点击后不释放
		// action.clickAndHold(element).moveByOffset(location.x+99,location.y).release().perform();
		// //选中source元素->拖放到（xOffset,yOffset）位置->释放左键
		
		Integer   moveX = Integer.parseInt(JOptionPane.showInputDialog("请输入验证码："));
		
		action.dragAndDropBy(element, location.x + moveX, location.y).perform();*/
		// action.dragAndDrop(element,newelement).perform();
//		String pageSource = driver.getPageSource();

		
		
		driver.findElement(By.className("login-btn")).click();
//		String htmlsource3 = driver.getPageSource();
		Thread.sleep(10000);
		
//		driver = getUserInfo(driver);
//		String html = driver.getPageSource();
//		HtmlParse.userInfoParse(html, null);
		
		Page page = getIndentForImgAndType(driver);
		String html = "{'JDIndentImg':"+page.getWebResponse().getContentAsString()+"}";

		HtmlParse.JDIndentImgAndTypeBeanParse(html, null);
		
		System.out.println();
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
	
	public static Page getIndentForImgAndType(WebDriver driver) throws Exception {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("order.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String url = "https://order.jd.com/lazy/getOrderProductInfo.action?orderWareIds="
				+ "3367194,6446356,5885734,1023935,1023247,6610598,4599596,7556979,7387053,7134340,1552946";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
//		webClient.addRequestHeader("Host", "baitiao.jd.com");
//		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");

		Page searchPage = webClient.getPage(webRequest);

		

		return searchPage;
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
		// webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
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
	 * 项目名称：common-microservice-e_commerce-jd 所属包名： 类描述： 废弃方法 创建人：hyx
	 * 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
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

	// https://order.jd.com/center/list.action
	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
