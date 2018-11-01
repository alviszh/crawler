package app.service.login;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

import app.bean.ValidationLoginDataObject;
import app.bean.ValidationLoginRoot;
import app.bean.WebParamTelecomByChrome;
import app.crawler.telecom.htmlparse.TelecomParseCommon;
import app.service.city.TelecomLoginHBService;
import app.service.city.TelecomLoginHEService;
import app.service.city.TelecomLoginHLJService;
import app.service.city.TelecomLoginNXService;
import app.service.common.LoginAndGetCommon;
import app.service.common.TelecomBasicService;
import app.unit.CommonUnit;

@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.common")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.common")
public class TelecomLoginService extends TelecomBasicService {

	public static final Logger log = LoggerFactory.getLogger(TelecomLoginService.class);

	private WebDriver driver = null;

	// @Autowired
	// private AgentService agentService;

	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	@Autowired
	public TelecomLoginNXService telecomLoginNXService;

	@Autowired
	public TelecomLoginHEService telecomLoginHEService;

	@Autowired
	public TelecomLoginHBService telecomLoginHBService;

	@Autowired
	public TelecomLoginHLJService telecomLoginHLJService;

	public WebDriver intiChrome() throws Exception {
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

		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		return driver;
	}

	public WebParamTelecomByChrome<?> login(MessageLogin messageLogin) throws Exception {

		WebParamTelecomByChrome<?> webParamTelecomByChrome = loginByChrome(messageLogin, 0);

		if (webParamTelecomByChrome.getErrormessage() != null) {

			return webParamTelecomByChrome;
		}

		Set<org.openqa.selenium.Cookie> cookiesDriver = webParamTelecomByChrome.getDriver().manage().getCookies();

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			tracerLog.addTag(cookieWebClient.getName(), cookieWebClient.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		String url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
		tracerLog.addTag("电信登录：", "<xmp>" + page.asXml() + "</xmp>");

		ValidationLoginDataObject dataObject = ValidationLogin(webClient);
		tracerLog.addTag("电信登录：", "<xmp>" + dataObject.toString() + "</xmp>");

		if (dataObject.getNickName() == null) {
			driver.close();
			webParamTelecomByChrome.setErrormessage(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			return webParamTelecomByChrome;
		}

		WebDriver driver = webParamTelecomByChrome.getDriver();
		driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
		tracerLog.addTag("获取到的地址为", driver.getCurrentUrl());
		if (driver.getCurrentUrl().indexOf("http://www.189.cn/nx/") != -1) {
			tracerLog.addTag("匹配城市：", "宁夏");
			// driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000501");
			// driver.get("http://nx.189.cn/jt/bill/xd/?fastcode=10000501&cityCode=nx");
			// Wait<WebDriver> wait = new
			// FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
			// .pollingEvery(2,
			// TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			// wait.until(new Function<WebDriver, WebElement>() {
			// public WebElement apply(WebDriver driver) {
			// return driver.findElement(By.id("hqyzm"));
			// }
			// });
			//
			// Boolean bool = true;
			// while (bool) {
			//
			// if (driver.getCurrentUrl()
			// .indexOf("http://nx.189.cn/jt/bill/xd/?fastcode=10000501&cityCode=nx")
			// != -1) {
			// bool = false;
			// tracerLog.addTag("转换到特定url", "========转换成功==========" +
			// driver.getCurrentUrl());
			// } else {
			// Thread.sleep(1000);
			// }
			//
			// }
			//
			// WebClient webClient2 =
			// WebCrawler.getInstance().getNewWebClient();
			// Set<org.openqa.selenium.Cookie> cookiesDriver2 =
			// driver.manage().getCookies();
			//
			// for (org.openqa.selenium.Cookie cookie2 : cookiesDriver2) {
			// Cookie cookieWebClient2 = new Cookie(cookie2.getDomain(),
			// cookie2.getName(), cookie2.getValue());
			// tracerLog.addTag(cookieWebClient2.getName(),
			// cookieWebClient2.getValue());
			// webClient2.getCookieManager().addCookie(cookieWebClient2);
			// }
			// // quit(messageLogin);
			// quit(messageLogin);
			//
			// webParamTelecomByChrome.setWebClient(webClient2);
			//
			// return webParamTelecomByChrome;

			return telecomLoginNXService.telecomToNx(driver, messageLogin, webParamTelecomByChrome);
		} else if (driver.getCurrentUrl().indexOf("http://www.189.cn/he/") != -1) {
			// tracerLog.addTag("匹配城市：", "河北");
			// driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00380407");
			// driver.get("http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he");
			// Wait<WebDriver> wait = new
			// FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
			// .pollingEvery(2,
			// TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			// wait.until(new Function<WebDriver, WebElement>() {
			// public WebElement apply(WebDriver driver) {
			// return driver.findElement(By.id("postValid"));
			// }
			// });
			//
			// Boolean bool = true;
			// while (bool) {
			//
			// if (driver.getCurrentUrl().indexOf(
			// "http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he")
			// != -1) {
			// bool = false;
			// tracerLog.addTag("转换到特定url", "========转换成功==========" +
			// driver.getCurrentUrl());
			// String citycodeForHeBei =
			// TelecomParseCommon.parserCityCodeForHeBei(driver.getPageSource());
			//
			// webParamTelecomByChrome.setCityCodeForHeBei(citycodeForHeBei);
			//
			// } else {
			// Thread.sleep(1000);
			// }
			//
			// }
			//
			// WebClient webClient2 =
			// WebCrawler.getInstance().getNewWebClient();
			// Set<org.openqa.selenium.Cookie> cookiesDriver2 =
			// driver.manage().getCookies();
			//
			// for (org.openqa.selenium.Cookie cookie : cookiesDriver2) {
			// Cookie cookieWebClient = new Cookie("he.189.cn",
			// cookie.getName(), cookie.getValue());
			//
			// tracerLog.addTag(cookieWebClient.getName(),
			// cookieWebClient.getValue());
			//
			// webClient2.getCookieManager().addCookie(cookieWebClient);
			// }
			// quit(messageLogin);
			// webParamTelecomByChrome.setWebClient(webClient2);
			//
			// return webParamTelecomByChrome;
			return telecomLoginHEService.telecomToHe(driver, messageLogin, webParamTelecomByChrome);

		} else if (driver.getCurrentUrl().indexOf("http://www.189.cn/hb/") != -1) {
			// tracerLog.addTag("匹配城市：", "湖北");
			// long startTime=System.currentTimeMillis();
			// driver.get("http://hb.189.cn/pages/selfservice/custinfo/userinfo/userInfo.action?trackPath=SYDH");
			//// driver.get("http://hb.189.cn/service/integral/qryIndex.action");
			// if
			// (driver.getCurrentUrl().indexOf("http://login.189.cn/web/login")
			// != -1) {
			// driver.get(driver.getCurrentUrl());
			// Wait<WebDriver> wait2 = new
			// FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
			// .pollingEvery(2,
			// TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			// WebElement loginByUserButton2 = wait2.until(new
			// Function<WebDriver, WebElement>() {
			// public WebElement apply(WebDriver driver) {
			// return driver.findElement(By.id("txtAccount"));
			// }
			// });
			//
			// loginByUserButton2.click();
			// wait2 = new FluentWait<WebDriver>(driver).withTimeout(60,
			// TimeUnit.SECONDS)
			// .pollingEvery(2,
			// TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			// wait2.until(new Function<WebDriver, WebElement>() {
			// public WebElement apply(WebDriver driver) {
			// return driver.findElement(By.id("txtAccount"));
			// }
			// });
			//
			// driver.findElement(By.id("txtShowPwd")).click();
			// driver.findElement(By.id("txtPassword")).sendKeys(messageLogin.getPassword().trim());
			// String code;
			// try {
			// code = CommonUnit.getVerfiycodeBy(By.id("imgCaptcha"), driver,
			// this.getClass());
			// driver.findElement(By.id("txtCaptcha")).sendKeys(code.trim());
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// quit(messageLogin);
			// webParamTelecomByChrome.setErrormessage(e.getMessage());
			// return webParamTelecomByChrome;
			// }
			// try {
			// driver.findElement(By.id("loginbtn")).click();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// //Robot
			// try{
			//// Robot robot = new Robot();
			//// //设置Robot产生一个动作后的休眠时间,否则执行过快
			//// robot.setAutoDelay(1000);
			//// tracerLog.addTag("湖北按下ESC键start：", "按下ESCstart");
			//// pressKey(robot);
			//// tracerLog.addTag("湖北按下ESC键end：", "按下ESCend");
			// tracerLog.addTag("湖北按下ESC键start：", "按下ESCstart");
			// Actions action = new Actions(driver);
			// action.sendKeys(Keys.ESCAPE);
			// action.sendKeys(Keys.ESCAPE);
			// action.perform();
			// tracerLog.addTag("湖北按下ESC键end：", "按下ESCend");
			// }catch(Exception e){
			// e.printStackTrace();
			// tracerLog.addTag("湖北电信Exception", "湖北电信Exception");
			// }
			//
			// try{
			// Wait<WebDriver> wait = new
			// FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
			// .pollingEvery(2,
			// TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			// wait.until(new Function<WebDriver, WebElement>() {
			// public WebElement apply(WebDriver driver) {
			// return driver.findElement(By.id("showTable01"));
			// }
			// });
			// }catch(Exception e){
			// e.printStackTrace();
			// }
			//
			// try{
			// WebClient webClient2 =
			// WebCrawler.getInstance().getNewWebClient();
			// Set<org.openqa.selenium.Cookie> cookiesDriver2 =
			// driver.manage().getCookies();
			// for (org.openqa.selenium.Cookie cookie : cookiesDriver2) {
			// Cookie cookieWebClient = new Cookie("hb.189.cn",
			// cookie.getName(), cookie.getValue());
			// webClient2.getCookieManager().addCookie(cookieWebClient);
			// }
			// quit(messageLogin);
			// webParamTelecomByChrome.setWebClient(webClient2);
			// }catch(Exception e){
			// System.out.println("无法获取cookie");
			// }
			// tracerLog.addTag("湖北电信登陆成功：", "湖北电信登陆成功");
			// long endTime=System.currentTimeMillis();
			// tracerLog.addTag("湖北电信二次登陆共用时：", (endTime-startTime)+"");
			// return webParamTelecomByChrome;
			// }

			return telecomLoginHBService.telecomToHb(driver, messageLogin, webParamTelecomByChrome);
		} else if (driver.getCurrentUrl().indexOf("http://www.189.cn/hl/") != -1) {

			return telecomLoginHLJService.telecomToHlj(driver, messageLogin, webParamTelecomByChrome);
		}

		quit(messageLogin);
		webParamTelecomByChrome.setWebClient(webClient);
		return webParamTelecomByChrome;
	}

	// private static void pressKey(Robot robot){
	// robot.delay(500);
	// robot.keyPress(KeyEvent.VK_ESCAPE);
	// robot.keyRelease(KeyEvent.VK_ESCAPE);
	// robot.delay(500);
	// }
	private WebParamTelecomByChrome<?> loginByChrome(MessageLogin messageLogin, int errornum) {
		WebParamTelecomByChrome<?> webParamTelecomByChrome = new WebParamTelecomByChrome<Object>();

		try {
			driver = intiChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			quit(messageLogin);
			webParamTelecomByChrome.setErrormessage(webParamTelecomByChrome.toString());
			return webParamTelecomByChrome;
		}
		driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
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
		loginname.sendKeys(messageLogin.getName().trim());

		driver.findElement(By.id("txtShowPwd")).click();
		driver.findElement(By.id("txtPassword")).sendKeys(messageLogin.getPassword().trim());
		String code;
		try {
			code = CommonUnit.getVerfiycodeBy(By.id("imgCaptcha"), driver, this.getClass());
			driver.findElement(By.id("txtCaptcha")).sendKeys(code.trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			quit(messageLogin);
			webParamTelecomByChrome.setErrormessage(e.getMessage());
			return webParamTelecomByChrome;
		}

		driver.findElement(By.id("loginbtn")).click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			quit(messageLogin);
			webParamTelecomByChrome.setErrormessage(e.getMessage());
			return webParamTelecomByChrome;
		}

		if (driver.getPageSource().indexOf("登录失败") != -1) {
			webParamTelecomByChrome = TelecomParseCommon.loginerror_Parser(driver.getPageSource(),
					webParamTelecomByChrome);
			if (webParamTelecomByChrome.getErrormessage().indexOf("验证码不正确") != -1) {
				if (errornum < 3) {
					quit(messageLogin);
					return loginByChrome(messageLogin, errornum++);
				} else {
					quit(messageLogin);
					webParamTelecomByChrome.setErrormessage(StatusCodeRec.MESSAGE_LOGIN_ERROR_six.getMessage());
					return webParamTelecomByChrome;
				}

			} else {
				quit(messageLogin);
				return webParamTelecomByChrome;
			}
		}

		webParamTelecomByChrome.setErrormessage(null);
		webParamTelecomByChrome.setDriver(driver);
		return webParamTelecomByChrome;
	}

	private ValidationLoginDataObject ValidationLogin(WebClient webClient) {

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

	public void quit(MessageLogin messageLogin) {
		if (driver != null) {

			try {
				driver.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// agentService.releaseInstance(messageLogin.getIp(), driver);
	}

	// public void quit(MessageLogin messageLogin,WebDriver driver){
	// driver.close();
	//// agentService.releaseInstance(messageLogin.getIp(), driver);
	// }
}