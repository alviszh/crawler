package app.service;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.bean.REteryErrorException;
import app.commontracerlog.TracerLog;
import app.unit.CommonUnit;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocServiceLoginAndGet 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.bocchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.bocchina" })
public class BocServiceLoginAndGet extends AbstractChaoJiYingHandler {

	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private AgentService agentService;

	private WebDriver driver = null;

	public WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser  " + driverPath);
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
		driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	/**
	 * 
	 * 
	 * 项目名称：common-microservice-bank-boc 类描述： 调用chrom浏览器 实现登录获取cookie 创建人：hyx
	 * 创建时间：2017年11月1日 上午11:26:01
	 * 
	 * @version 返回值 为WebDriver
	 * 
	 *
	 */
	@Retryable(value = REteryErrorException.class, maxAttempts = 3)
	public WebDriver loginChrome(BankJsonBean bankJsonBean, int i) throws Exception {
		WebDriver driver = null;
		try {
			TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			driver = intiChrome();

			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "https://ebsnew.boc.cn/boc15/login.html";
			driver.get(baseUrl);

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("input_div_password_79445"));
				}
			});

			driver.findElement(By.id("txt_username_79443")).sendKeys(bankJsonBean.getLoginName().trim());
			ele1.click();

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement ele2 = null;
			try {

				ele2 = wait2.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						if (bankJsonBean.getCardType().indexOf("CREDIT_CARD") != -1) {
							return driver.findElement(By.id("input_txt_50531_740884"));
						} else {
							return driver.findElement(By.id("input_div_password_79445_1"));
						}

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (bankJsonBean.getCardType().indexOf("CREDIT_CARD") != -1) {
				driver.findElement(By.id("btn_49_740887")).click();

			} else {
				driver.findElement(By.id("btn_49_741014")).click();

			}

			Thread.sleep(2000);

			if (bankJsonBean.getCardType().indexOf("CREDIT_CARD") != -1) {
				if (driver.getPageSource().indexOf("captcha_creditCard") != -1) {
					ele2.sendKeys(bankJsonBean.getPassword().trim());

					String code = CommonUnit.getVerfiycodeBy(By.id("captcha_creditCard"), driver, this.getClass());
					ele2.sendKeys(bankJsonBean.getPassword());

					driver.findElement(By.id("txt_captcha_740885")).sendKeys(code);

					driver.findElement(By.id("btn_49_740887")).click();
				}
			} else {
				if (driver.getPageSource().indexOf("captcha_debitCard") != -1) {
					ele2.sendKeys(bankJsonBean.getPassword().trim());

					String code = CommonUnit.getVerfiycodeBy(By.id("captcha_debitCard"), driver, this.getClass());
					ele2.sendKeys(bankJsonBean.getPassword());

					driver.findElement(By.id("txt_captcha_741012")).sendKeys(code);

					driver.findElement(By.id("btn_49_741014")).click();
				}
			}

			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}
			wait2 = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			try{
				ele2 = wait2.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {

						if (bankJsonBean.getCardType().indexOf("CREDIT_CARD") != -1) {
							return driver.findElement(By.id("div_account_information_740844"));
						} else {
							return driver.findElement(By.id("div_account_information_740992"));
						}
					}
				});
			}catch(Exception e){
				e.printStackTrace();

				try {
					String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.output("crawler.bank.login.cardnum.exception", path);
				} catch (Exception e1) {

				}
			}
			

			String currentPageURL = driver.getCurrentUrl();

			if (bankJsonBean.getCardType().indexOf("CREDIT_CARD") != -1) {
				if (currentPageURL.indexOf("https://ebsnew.boc.cn/boc15/welcome_ele.html") != -1) {

					tracerLog.output("crawler.bank.login.success", "登陆成功");
					taskBankStatusService.transforCookie(driver, "ebsnew.boc.cn", taskBank, null);
					tracerLog.output("crawler.bank.login.account.success", taskBank.getTaskid());
					System.out.println("=========银行卡号登录  登录成功  222 ===========");
					//
					return driver;
				} else {
					loginErrot(i, bankJsonBean);
				}
			} else {
				if (currentPageURL.indexOf("https://ebsnew.boc.cn/boc15/welcome_ele.html") != -1) {
					tracerLog.output("crawler.bank.login.success", "登陆成功");
					taskBankStatusService.transforCookie(driver, "ebsnew.boc.cn", taskBank, null);
					tracerLog.output("crawler.bank.login.account.success", taskBank.getTaskid());
					System.out.println("=========银行卡号登录  登录成功  222 ===========");
					//
					return driver;
				} else {
					loginErrot(i, bankJsonBean);
				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e) {

			}
			System.out.println("=========登录失败===========");
			String htmlsource3 = driver.getPageSource();
			System.out.println(htmlsource3);
			Document doc = Jsoup.parse(htmlsource3);
			String error_texg = doc.select("span#msgContent").text();

			System.out.println("=============error_texg=============" + error_texg);
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), error_texg,
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
			return null;
		}
		return null;

	}

	public WebDriver loginErrot(int i, BankJsonBean bankJsonBean){
		System.out.println("=========登录失败===========");
		String htmlsource3 = driver.getPageSource();
		Document doc = Jsoup.parse(htmlsource3);
		String error_texg = doc.select("span#msgContent").text();
		if (error_texg.indexOf("验证码") != -1 && error_texg.indexOf("错误") != -1) {
			throw new REteryErrorException("验证码输入错误");

		} else {
			System.out.println("=============error_texg=============" + error_texg);

			if (error_texg.indexOf("操作失败") != -1) {
				throw new REteryErrorException("操作失败");
			}

			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), error_texg,
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), false, bankJsonBean.getTaskid());

			// agentService.releaseInstance(bankJsonBean.getIp(),
			// driver);
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
			return null;
		}
	}

	@Retryable(value = REteryErrorException.class, maxAttempts = 3)
	public WebDriver loginChromeByUserName(BankJsonBean bankJsonBean, int i) throws Exception {
		WebDriver driver = null;

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		driver = intiChrome();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://ebsnew.boc.cn/boc15/login.html";
		driver.get(baseUrl);

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement ele2 = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("input_div_password_79445"));
			}
		});
		driver.findElement(By.id("txt_username_79443")).sendKeys(bankJsonBean.getLoginName().trim());
		ele2.click();
		ele2.sendKeys(bankJsonBean.getPassword());

		Thread.sleep(3000L);
		driver.findElement(By.id("btn_login_79676")).click();

		Thread.sleep(3000L);

		if (driver.getPageSource().indexOf("txt_captcha_79449") != -1) {
			String code = CommonUnit.getVerfiycodeBy(By.id("captcha"), driver, this.getClass());
			ele2.sendKeys(bankJsonBean.getPassword());

			driver.findElement(By.id("txt_captcha_79449")).sendKeys(code);
			driver.findElement(By.id("btn_login_79676")).click();
		}
		// String code = CommonUnit.getVerfiycode(By.id("captcha"), driver);

		try {
			wait2 = new FluentWait<WebDriver>(driver).withTimeout(5, TimeUnit.SECONDS)
					.pollingEvery(30, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			ele2 = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("div_account_information_740992"));
				}
			});
		} catch (Exception e) {
			System.out.println("=========登录失败===========");
			String htmlsource3 = driver.getPageSource();
			System.out.println(htmlsource3);
			Document doc = Jsoup.parse(htmlsource3);
			String error_texg = doc.select("span#msgContent").text();
			if (error_texg.indexOf("验证码") != -1 && error_texg.indexOf("错误") != -1) {
				throw new REteryErrorException("验证码输入错误");
			}

			System.out.println("=============error_texg=============" + error_texg);
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), error_texg,
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
			return null;
		}

		String currentPageURL = driver.getCurrentUrl();
		if (currentPageURL.indexOf("https://ebsnew.boc.cn/boc15/welcome_ele.html") != -1) {
			System.out.println("=========登录成功===========");
			tracerLog.output("crawler.bank.login.success", "登陆成功");
			taskBankStatusService.transforCookie(driver, "ebsnew.boc.cn", taskBank, null);
			tracerLog.output("crawler.bank.login.account.success", taskBank.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
			return driver;
		} else {
			System.out.println("=========登录失败===========");
			String htmlsource3 = driver.getPageSource();
			Document doc = Jsoup.parse(htmlsource3);
			String error_texg = doc.select("span#msgContent").text();
			if (error_texg.indexOf("操作失败") != -1) {
				try {
					String error = doc.select("div#info-con").text();

					if (error.length() > 0) {
						error_texg = error;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), error_texg,
					BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
			return null;
		}

	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 类描述： 获取 银行流水的重要参数方法 创建人：hyx
	 * 创建时间：2017年11月1日 下午2:40:59
	 * 
	 * @version 返回值 为String
	 * 
	 * 
	 */
	public String getCountid(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->20
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":19,\"method\":\"PsnAccBocnetCreateConversation\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
		String counid = CommonUnit
				.getSubUtilSimple(searchPage.getWebResponse().getContentAsString(), "result(.*?)error")
				.replaceAll("\"", "").replaceAll(",", "").replaceAll(":", "").trim();
		return counid;
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 获取银行流水的方法 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 AsyncResult<String>
	 */
	public AsyncResult<String> getTranFlow(String url, WebClient webClient, String countid, String accountSeq,
			String statrdate, String enddate) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->20
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},"
						+ "\"request\":[{\"id\":21,\"method\":\"PsnAccBocnetQryDebitTransDetail\",\"conversationId\":"
						+ "\"" + countid.trim() + "\"," + "\"params\":{\"accountSeq\":" + "\"" + accountSeq.trim()
						+ "\",\"currency\":\"001\",\"cashRemit\":\"\"," + "\"startDate\":\"" + statrdate.trim() + "\","
						+ "\"endDate\":\"" + enddate.trim() + "\"" + "," + "\"" + "pageSize"
						+ "\":\"1000\",\"_refresh\":\"true\",\"currentIndex\":\"0\"}}]}");
		Page searchPage = webClient.getPage(webRequest);
		return new AsyncResult<String>(searchPage.getWebResponse().getContentAsString());
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 AsyncResult<String>
	 */
	public String getUserInfo(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->20
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":6,\"method\":\"PsnAccBocnetQryLoginInfo\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 获取用户开户信息 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 AsyncResult<String>
	 */
	public String getUserInfoOpendate(String url, WebClient webClient, String accountSeq) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->20
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webClient.addRequestHeader("bfw-ctrl", "json");
		// webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":8,\"method\":\"PsnAccBocnetQryDebitDetail\",\"conversationId\":null,"
						+ "\"params\":" + "{\"accountSeq\":" + "\"" + accountSeq.trim() + "\"" + "}}]}");
		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 获取上限信息 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 AsyncResult<String>
	 */
	public String getUserInfoSingleLimit(String url, WebClient webClient, String accountSeq) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->20
		webClient.addRequestHeader("bfw-ctrl", "json");
		webClient.addRequestHeader("Content-Type", "text/json;");
		webClient.addRequestHeader("Host", "ebsnew.boc.cn");
		webClient.addRequestHeader("Origin", "https://ebsnew.boc.cn");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer",
				"https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1");

		webRequest.setRequestBody(
				"{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":10,\"method\":\"PsnAccBocnetQryEcashDetail\",\"conversationId\":null,"
						+ "\"params\":{\"accountSeq\":\"" + accountSeq.trim() + "\"}}]}");
		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean
	 */
	public TaskBank quit(BankJsonBean bankJsonBean) {
		tracerLog.output("quit", bankJsonBean.toString());
		// 关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试 ,
		// description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true, bankJsonBean.getTaskid());
		// 调用公用释放资源方法
		if (taskBank != null) {
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else {
			tracerLog.output("quit taskBank is null", "");
		}
		return taskBank;
	}

}