package app.controller;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.module.ddxoft.VK;

@RestController
@RequestMapping("/winio/dd")
public class PbccrcDDController {

	// static String driverPath = "C:\\chromedriver.exe";
	// String driverPath = "C:\\IEDriverServer_Win32\\chromedriver.exe";
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";
	Boolean headless = false;

	// public WebDriver intiChrome() throws Exception {
	// System.out.println("launching chrome browser");
	// System.setProperty("webdriver.chrome.driver", driverPath);
	//
	// // WebDriver driver = new ChromeDriver();
	// ChromeOptions chromeOptions = new ChromeOptions();
	// // 设置为 headless 模式 （必须）
	// System.out.println("headless-------" + headless);
	// // if(headless){
	// // chromeOptions.addArguments("headless");// headless mode
	// // }
	//
	// chromeOptions.addArguments("disable-gpu");
	// // 设置浏览器窗口打开大小 （非必须）
	// // chromeOptions.addArguments("--window-size=1920,1080");
	// WebDriver driver = new ChromeDriver(chromeOptions);
	// return driver;
	// }

	public WebDriver intiChrome() throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);
		if (driverPath == null) {
			System.out.println("WebDriverIEService initIE RuntimeException webdriver.ie.driver 初始化失败！");
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}

		System.out.println("WebDriverIEService initIE Msg webdriver.ie.driver 初始化");

		System.out.println("-------------getWebDriver() 初始化新的WebDriver---------");
		// WebDriver driver = new EventFiringWebDriver(new
		// InternetExplorerDriver(ieCapabilities)).register(new
		// LogEventListener());
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);// 页面加载timeout
																			// 30秒
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); // JavaScript加载timeout
																			// 30秒

		Set<String> handlers = driver.getWindowHandles();
		for (String handler : handlers) {
			System.out.println("handler-------------" + handler);
		}
		return driver;
	}

	@GetMapping(path = "/dd")
	public String loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.get(LoginPage);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445"));
			}
		});

		// driver.findElement(By.id("txt_username_79443")).sendKeys("6216600100003951512");
		driver.findElement(By.id("txt_username_79443")).click();
		VK.KeyPress("6216600100003951512");

		ele1.click();

		// WebDriverWait wait2 = new WebDriverWait(driver, 5);
		// wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("input_div_password_79445_1")));
		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele2 = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445_1"));
			}
		});

		String htmlsource2 = driver.getPageSource();
		ele2.click();

		VK.KeyPress("112358");

		System.out.println("htmlsource--22--" + htmlsource2);

		String code = JOptionPane.showInputDialog("请输入验证码：");

		driver.findElement(By.id("txt_captcha_741012")).click();
		VK.KeyPress(code);
		driver.findElement(By.id("btn_49_741014")).click();

		// Thread.sleep(10000L);//
		// 等待10秒，根据当前URL是否还是登录页面，判断登录是否成功，等待5秒也是等待登录成功后的页面信息加载

		wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		ele2 = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("div_account_information_740992"));
			}
		});

		String currentPageURL = driver.getCurrentUrl();
		System.out.println("currentPageURL--" + currentPageURL);
		if (currentPageURL.indexOf("https://ebsnew.boc.cn/boc15/welcome_ele.html") != -1) {
			System.out.println("==========登录成功=========");
		} else {
			String htmlsource3 = driver.getPageSource();
			System.out.println(htmlsource3);
			Document doc = Jsoup.parse(htmlsource3);
			String error_texg = doc.select("span#msgContent").text();
			System.out.println("=============error_texg=============" + error_texg);
		}

		return null;

	}

	/**
	 * @Des POST 登录接口
	 * @param bankJsonBean
	 */
	@GetMapping(path = "/pbccrc")
	public String loginpbccrc(String username, String password, String code) throws Exception {
		System.out.println("username-------" + username);
		System.out.println("password-------" + password);
		System.out.println("code-------" + code);
		// 打开招行页面输入用户名密码登录 (异步)
		WebDriver driver = openloginCmbChina();
		if (driverPath == null) {
			System.out.println("WebDriverIEService initIE RuntimeException  webdriver.ie.driver 初始化失败！");
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}

		// 键盘输入账号
		driver.findElement(By.id("loginname")).click();
		;
		VK.KeyPress("6216600100003951512");
		// driver.findElement(By.id("loginname")).sendKeys(username);

		Thread.sleep(500L);
		driver.findElement(By.id("pass")).click();
		Thread.sleep(500);
		VK.KeyPress("6216600100003951512");
		// Thread.sleep(500);
		// driver.findElement(By.name("_@IMGRC@_")).sendKeys(code);
		// Thread.sleep(500);

		// driver.close();

		return "123";

	}

	/**
	 * @Des 打开招行登录页面
	 * @param 无
	 * @throws Exception
	 */
	public WebDriver openloginCmbChina() throws Exception {

		WebDriver driver = intiChrome();

		System.out.println("WebDriverIEService loginCmbChina Msg 开始登陆招商银行登陆页");
		try {
			driver = getPage(driver, LoginPage);
		} catch (NoSuchWindowException e) {
			System.out.println("打开招行登录页面报错，尝试重新初始化游览器" + e.getMessage());
			driver = getPage(driver, LoginPage);
		}
		System.out.println("WebDriverIEService loginCmbChina Msg 招商银行登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());
		return driver;
	}

	public WebDriver getPage(WebDriver driver, String url) throws NoSuchWindowException {
		System.out.println("getPage:" + url);
		// driver = getWebDriver();
		driver.get(url);
		return driver;
	}

	// public static void main(String[] args) throws Exception {
	// loginChrome();
	// }

}
