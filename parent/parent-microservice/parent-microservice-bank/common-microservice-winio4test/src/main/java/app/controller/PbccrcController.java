package app.controller;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.module.ddxoft.VK; 

@RestController
@RequestMapping("/winio")
public class PbccrcController {

	private WebDriver driver;

	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";

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
		WebDriver webDriver = openloginCmbChina();
		if (driverPath == null) {
			System.out.println("WebDriverIEService initIE RuntimeException  webdriver.ie.driver 初始化失败！");
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}

		// 键盘输入账号
		driver.findElement(By.id("loginname")).clear();
		driver.findElement(By.id("loginname")).sendKeys(username);

		Thread.sleep(500L);
		driver.findElement(By.id("pass")).sendKeys(password);
		Thread.sleep(500);
		VK.KeyPress(password);
		//Thread.sleep(500);
		//driver.findElement(By.name("_@IMGRC@_")).sendKeys(code);
		//Thread.sleep(500);
		
		//driver.close();
		
		return "123";

	}

	/**
	 * @Des 打开招行登录页面
	 * @param 无
	 */
	public WebDriver openloginCmbChina() {
		driver = getNewWebDriver();
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

	public WebDriver getNewWebDriver() {

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

	public WebDriver getPage(WebDriver driver, String url) throws NoSuchWindowException {
		System.out.println("getPage:" + url);
		// driver = getWebDriver();
		driver.get(url);
		return driver;
	}

}
