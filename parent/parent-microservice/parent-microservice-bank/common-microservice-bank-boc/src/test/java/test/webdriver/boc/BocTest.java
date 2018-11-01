package test.webdriver.boc;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class BocTest extends AbstractChaoJiYingHandler {

	// static String driverPath = "C:\\chromedriver.exe";
	// static String driverPath =
	// "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

//	public static void main(String[] args) throws Exception {
//
//		crawlerChrome189();
//
//		// login();
//		// loginChrome() ;
//		// crawlerChrome();
//		 LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
//		// java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
//		// java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
//		// htmluit() ;
//	}

	public static void crawlerChrome189() throws Exception {
		//

		ChromeOptions options = new ChromeOptions();

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		RemoteWebDriver driver = new RemoteWebDriver(new URL("http://10.167.202.218:32771/wd/hub/"), capabilities);// 10.167.202.170/crawler/standalone-chrome-debug:zh

		driver.get("http://login.189.cn/web/login");
		// driver.get("http://news.qq.com/a/20170908/029452.htm");

		driver.findElement(By.id("txtAccount")).sendKeys("17316359776");
		driver.findElement(By.id("txtShowPwd")).click();
		driver.findElement(By.id("txtPassword")).sendKeys("139764");

		driver.findElement(By.id("loginbtn")).click();

		Thread.sleep(3000);

		Collection<Cookie> cookies = driver.manage().getCookies();
		// driver.manage().addCookie(cookie);
		// driver.getSessionId().toString();
		for (Cookie cookie : cookies) {
			System.out.println("cookie---------" + cookie.toString());
		}

		driver.get("http://service.sh.189.cn/service/account");

		for (Cookie cookie : cookies) {
			System.out.println("cookie---------" + cookie.toString());
		}

		//driver.quit();
		// String path =
		// WebDriverUnit.saveImg(driver,By.cssSelector(".titMode"));
		// System.out.println("path---------------"+path);

		// driver.manage().window().maximize();
		// String currentUrl = driver.getCurrentUrl();

	}

	public static void htmluit() throws Exception { 

		WebClient  webClient = new WebClient(BrowserVersion.CHROME);
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(true);
		
		//ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		//JavaScriptEngine nashorn = (JavaScriptEngine) scriptEngineManager.getEngineByName("nashorn");

		//webClient.setJavaScriptEngine(nashorn);
		
		
		HtmlPage page = (HtmlPage)webClient.getPage("http://news.qq.com/articleList/rolls/");
		Thread.sleep(5000);
		
		String html = page.getWebResponse().getContentAsString();
		
		System.out.println(html);

	}

	public static ChromeDriver intiChrome() throws Exception {
		String driverPath = "C:\\chromedriver.exe";
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static WebDriver intiIE() throws Exception {
		String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		return driver;
	}

	public static void crawlerChrome() throws Exception {
		// WebDriver driver = intiChrome() ;
		// RemoteWebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.202.218:32787/wd/hub/"),
		// DesiredCapabilities.chrome());// selenium/standalone-chrome-debug
		// ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）

		ChromeOptions options = new ChromeOptions();
		// options.addArguments("--start-maximized");
		// options.addArguments("--headless");
		// options.addArguments("--disable-gpu");
		// options.addExtensions(paths);

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		RemoteWebDriver driver = new RemoteWebDriver(new URL("http://10.167.202.218:32789/wd/hub/"), capabilities);// 10.167.202.170/crawler/standalone-chrome-debug:zh
		// driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		// driver.manage().timeouts().setScriptTimeout(30,
		// TimeUnit.SECONDS);http://news.qq.com/a/20170908/029452.htm

		driver.get("http://news.qq.com/articleList/rolls/");
		// driver.get("http://news.qq.com/a/20170908/029452.htm");
		driver.manage().window().maximize();

		// driver.manage().window().maximize();
		// String currentUrl = driver.getCurrentUrl();

	}

	public static void loginChrome() throws Exception {
		long start = System.currentTimeMillis();
		// ChromeDriver driver = new ChromeDriver();
		ChromeDriver driver = intiChrome();
		// RemoteWebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.202.218:32777/wd/hub/"),
		// DesiredCapabilities.chrome());

		// driver.
		// driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		// driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://ebsnew.boc.cn/boc15/login.html";
		driver.get(baseUrl);
		// driver.manage().window().maximize();
		// String currentUrl = driver.getCurrentUrl();

		// WebDriverWait wait = new WebDriverWait(driver, 60);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input_div_password_79445")));
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445"));
			}
		});

		String htmlsource = driver.getPageSource();
		System.out.println("htmlsource--11--" + htmlsource);
		driver.findElement(By.id("txt_username_79443")).sendKeys("6216600100003951512");
		ele1.click();

		// WebDriverWait wait2 = new WebDriverWait(driver, 60);
		// wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("input_div_password_79445_1")));
		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele2 = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445_1"));
			}
		});

		String htmlsource2 = driver.getPageSource();
		// ele2.sendKeys("112358");
		ele2.sendKeys("1234561");
		System.out.println("htmlsource--22--" + htmlsource2);

		String code = getVerfiycode(By.id("captcha_debitCard"), driver);

		driver.findElement(By.id("txt_captcha_741012")).sendKeys(code);

		driver.findElement(By.id("btn_49_741014")).click();

		Thread.sleep(5000L);// 等待5秒，根据当前URL是否还是登录页面，判断登录是否成功，等待5秒也是等待登录成功后的页面信息加载
		String currentPageURL = driver.getCurrentUrl();
		System.out.println("currentPageURL--" + currentPageURL);
		String htmlsource3 = driver.getPageSource();
		System.out.println("htmlsource--33--" + htmlsource3);

		long end = System.currentTimeMillis();

		if (currentPageURL.startsWith(baseUrl)) {
			System.out.println("登录失败");
			String msgContent = driver.findElement(By.id("msgContent")).getText();
			System.out.println("msgContent-----" + msgContent);
		}

		System.out.println((end - start) + ":ms");
	}

	public static String getVerfiycode(By by, WebDriver driver) throws Exception {
		String path = WebDriverUnit.saveImg(driver, by);
		System.out.println("path---------------" + path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		return code;
	}

	public static void login() throws Exception {

		WebDriver driver = intiIE();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://ebsnew.boc.cn/boc15/login.html";
		driver.get(baseUrl);
		// driver.manage().window().maximize();
		String currentUrl = driver.getCurrentUrl();
		System.out.println("currentUrl--11--" + currentUrl);
		// String htmlsource = driver.getPageSource();
		// System.out.println("htmlsource--11--"+htmlsource);

		driver.findElement(By.id("txt_username_79443")).sendKeys("6216600100003951512");

		Thread.sleep(1000L);
		InputTab(); //
		Thread.sleep(2500L);

		driver.findElement(By.id("txt_50514_741010")).click();
		InputTab(); //
		Thread.sleep(1000L);
		VirtualKeyBoard.KeyPressEx("1123581", 500);

		String path = WebDriverUnit.saveImg(driver, By.id("captcha_debitCard"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);

		driver.findElement(By.id("txt_captcha_741012")).sendKeys(code);
		Thread.sleep(1000L);
		driver.findElement(By.id("btn_49_741014")).click();

		// String htmlsource22 = driver.getPageSource();
		// System.out.println("htmlsource--22--"+htmlsource22);
		// driver.findElement(By.id("input_div_password_79445")).click();
		// Thread.sleep(3000L);

		// WebDriverWait wait = new WebDriverWait(driver, 5);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txt_50514_741010")));

		// driver.findElement(By.id("txt_50514_741010")).click();

		// InputTab(); //

		/*
		 * WebDriverWait wait = new WebDriverWait(driver, 5);
		 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
		 * "txt_captcha_741012")));
		 * 
		 * InputTab(); //
		 * 
		 * InputTab(); //
		 */
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		System.out.println("--------输入Tab键--------");
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

}
