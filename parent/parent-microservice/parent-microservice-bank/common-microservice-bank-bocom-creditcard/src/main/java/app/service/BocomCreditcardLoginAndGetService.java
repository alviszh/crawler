package app.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import org.springframework.stereotype.Component;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBillNow;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.bank.unit.CommonUnitForBank;
import app.bean.CodeErrorException;
import app.bean.CodeMsgBean;
import app.commontracerlog.TracerLog;
import app.parser.BocomCreditcardParse;
import app.service.TaskBankStatusService;
import app.unit.AbstractTesseserHandlerByTess4j;
import app.unit.CommonUnitForBocomCreditcard;
import app.unit.GetBankCardUnit;

/**
 * 
 * 项目名称：common-microservice-bank-bocom-creditcard 类名称：BocomCreditcardService
 * 类描述： 创建人：hyx 创建时间：2017年11月22日 下午5:44:33
 * 
 * @version
 */

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.bocom.creditcard" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.bocom.creditcard" })
public class BocomCreditcardLoginAndGetService extends AbstractChaoJiYingHandler {

	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private AgentService agentService;

	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	private WebDriver driver;

	String loginUrl = "https://creditcardapp.bankcomm.com/idm/sso/login.html?service=https://creditcardapp.bankcomm.com/member/shiro-cas";

	public WebDriver intiChrome() throws Exception {
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

	public WebDriver loginForCard(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

		try{
			String banktype = GetBankCardUnit.getname(bankJsonBean.getLoginName().trim());

			if(banktype==null || banktype.indexOf("交通银行")==-1){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
						"您输入的账号不符合银行卡号规则或为其他银行银行卡，请检查输入账号",
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), false, bankJsonBean.getTaskid());
				return null;
			}
			
			
		}catch(Exception e){
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
					"交通银行信用卡仅支持银行卡号、手机号、邮箱登录，请检查输入账号",
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			return null;

		}
		
		
		
		try {
			driver = intiChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);

			return null;
		}
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(loginUrl);
		driver.findElement(By.id("tabCardNo")).click();

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(By.id("cardNo"));
			}
		});
		driver.findElement(By.id("cardNo")).clear();
		driver.findElement(By.id("cardNo")).sendKeys(bankJsonBean.getLoginName().trim());

		WebElement eleCardpassword = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(By.id("cardpassword"));
			}
		});
		eleCardpassword.click(); // 点击密码输入框，为了让安全键盘弹出，以便截图
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pot = null;
		try {
			pot = getpot(bankJsonBean);
		} catch (Exception e) {
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，密码验证错误，请检查是否为信用卡卡号",
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
		}

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('cardpassword').setAttribute('data-val', '" + pot + "')");

		try {
			WebElement eleDetermine_btn_fuceng = driver.findElement(By.className("close_overlay"));
			eleDetermine_btn_fuceng.click();// 关闭页面浮层,防止浮层影响点击
		} catch (Exception e) {
			e.printStackTrace();
		}

		WebElement eleDetermine_btn = driver.findElement(By.className("determine_btn"));

		eleDetermine_btn.click(); // 点击安全键盘上的确认键,关闭安全键盘
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver.findElement(By.id("cardLogin")).click();

		try {
			String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("crawler.bank.login.cardnum.exception", path);
		} catch (Exception e1) {

		}
		try {
			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(By.className("userinf"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.bank.login.error", e.getMessage());
			tracerLog.output("crawler.bank.login.account.error", taskBank.getTaskid());
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}
			if (driver.getPageSource().indexOf("尊敬的用户，由于您长期未登录我行信用卡官方网站，为了您账户安全，请您完成验证，验证成功后即可完成登录，感谢配合") != -1) {

				tracerLog.output("crawler.bank.login.success", "登陆成功 ,但需要用户短信验证码验证");
				changSucessNeeSMS(taskBank);
//				Document doc = Jsoup.parse(driver.getPageSource());
//
//				String username = doc.select("input#username").attr("value");

//				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//
//				taskBank.setCookies(CommonUnitForBank.transcookieToJsonBySelenium(driver));
//				taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase());
//				taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus());
//				taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription());
//				taskBank.setError_code(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code());
//				taskBank.setParam(username);
//				taskBank = taskBankRepository.save(taskBank);
//
//				agentService.releaseInstance(bankJsonBean.getIp(), driver);
				return null;
			}

			if (driver.getCurrentUrl().indexOf("https://creditcardapp.bankcomm.com/idm/sso/auth.html") != -1) {
				Document doc = Jsoup.parse(driver.getPageSource());
				String error_texg = doc.select("div.errormsg").first().text();

				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), error_texg,
						BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
				agentService.releaseInstance(bankJsonBean.getIp(), driver);
				return null;
			}

			String htmlsource3 = driver.getPageSource();
			tracerLog.output("错误失败页面", "<xmp>" + htmlsource3 + "</xmp>");
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			// driver.quit();
			agentService.releaseInstance(bankJsonBean.getIp(), driver);

			return null;
		}

		tracerLog.output("crawler.bank.login.success", "登陆成功");
		tracerLog.output("crawler.bank.login.account.success", taskBank.getTaskid());
		taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid());

		return driver;

	}

	public WebDriver loginForPhone(BankJsonBean bankJsonBean) {
//		

		try {
			driver = intiChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);

			return null;
		}
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(loginUrl);
		driver.findElement(By.id("tabMobileEmail")).click();

		// tabMobileEmail

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(By.id("mobileOrEmail"));
			}
		});
		driver.findElement(By.id("mobileOrEmail")).clear();
		driver.findElement(By.id("mobileOrEmail")).sendKeys(bankJsonBean.getLoginName().trim());

		WebElement eleCardpassword = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(By.id("password"));
			}
		});
		eleCardpassword.click(); // 点击密码输入框，为了让安全键盘弹出，以便截图
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pot = null;
		try {
			pot = getpot(bankJsonBean);
		} catch (Exception e) {
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，密码验证错误，请检查是否为手机号或邮箱",
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			agentService.releaseInstance(bankJsonBean.getIp(), driver);
		}

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('password').setAttribute('data-val', '" + pot + "')");

		try {
			WebElement eleDetermine_btn_fuceng = driver.findElement(By.className("close_overlay"));
			eleDetermine_btn_fuceng.click();// 关闭页面浮层,防止浮层影响点击
		} catch (Exception e) {
			e.printStackTrace();
		}

		WebElement eleDetermine_btn = driver.findElement(By.className("determine_btn"));

		eleDetermine_btn.click(); // 点击安全键盘上的确认键,关闭安全键盘
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver.findElement(By.id("loginBtn")).click();

		try {
			String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("crawler.bank.login.cardnum.exception", path);
		} catch (Exception e1) {

		}
		try {
			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(By.className("userinf"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			
			tracerLog.output("crawler.bank.login.error", e.getMessage());
			tracerLog.output("crawler.bank.login.account.error", bankJsonBean.getTaskid());
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}

			if (driver.getPageSource().indexOf("尊敬的用户，由于您长期未登录我行信用卡官方网站，为了您账户安全，请您完成验证，验证成功后即可完成登录，感谢配合") != -1) {
				TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

				// 修改状态为需要发送短信验证码，并点击发送短信验证码按钮
				changSucessNeeSMS(taskBank);

				return null;
			}

			if (driver.getCurrentUrl().indexOf("https://creditcardapp.bankcomm.com/idm/sso/auth.html") != -1) {
				Document doc = Jsoup.parse(driver.getPageSource());
				String error_texg = doc.select("div.errormsg").first().text();

				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), error_texg,
						BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
				agentService.releaseInstance(bankJsonBean.getIp(), driver);
				return null;
			}

			String htmlsource3 = driver.getPageSource();
			tracerLog.output("错误失败页面", "<xmp>" + htmlsource3 + "</xmp>");
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			// driver.quit();
			agentService.releaseInstance(bankJsonBean.getIp(), driver);

			return null;
		}

		tracerLog.output("crawler.bank.login.success", "登陆成功");
		tracerLog.output("crawler.bank.login.account.success", bankJsonBean.getTaskid());
		taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid());

		return driver;

	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-bocom-creditcard 所属包名：app.service 类描述：
	 * 创建人：hyx 创建时间：2017年11月22日
	 * 
	 * @version 1 返回值 Page
	 */
	public String getBill(WebClient webClient, String cardnum, String billdate) throws Exception, IOException {
		String url = "https://creditcardapp.bankcomm.com/member/member/service/billing/finished.html" + "?cardNo="
				+ cardnum.trim() + "&billDate=" + billdate.trim();

		tracerLog.output("getBill url :" + url, url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");

		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-bocom-creditcard 所属包名：app.service 类描述：
	 * 创建人：hyx 创建时间：2017年11月22日
	 * 
	 * @version 1 返回值 String
	 */
	public String getUserInfo(WebClient webClient) throws Exception, IOException {

		String url = "https://creditcardapp.bankcomm.com/sac/user/account/index/baseinfo.json";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();
	}

	public String getBillNow(WebClient webClient, String cardNo) throws Exception, Exception {

		String url = "https://creditcardapp.bankcomm.com/member/member/limit/info.json";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("cardNo", cardNo));

		webRequest.setRequestParameters(paramsList);
		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}

	public BocomCreditcardBillNow getBillNow2(WebClient webClient, String cardNo,
			BocomCreditcardBillNow bocomCreditcardBillNow) throws Exception, Exception {

		String url = "https://creditcardapp.bankcomm.com/member/member/service/billing/balanceQry.html?" + "cardNo="
				+ (cardNo.trim().replaceAll(" ", "%20")) + "&_=" + System.currentTimeMillis();

		// String url =
		// "https://creditcardapp.bankcomm.com/member/member/service/billing/balanceQry.html?"
		// + "cardNo=5218%20****%20****%209795"
		// + "&_=1528253743915";

		System.out.println(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		Page searchPage = webClient.getPage(webRequest);

		bocomCreditcardBillNow = BocomCreditcardParse.billnow_parse2(searchPage.getWebResponse().getContentAsString(),
				bocomCreditcardBillNow);

		return bocomCreditcardBillNow;

	}

	public CodeMsgBean getCode(String username, WebClient webClient) throws Exception {

		String getcode_url = "https://creditcardapp.bankcomm.com/idm/sso/sendDynamicCode.json?v=0.5344162919169624";

		WebRequest webRequest = new WebRequest(new URL(getcode_url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webRequest.setCharset(Charset.forName("UTF-8"));

		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "creditcardapp.bankcomm.com");
		webRequest.setAdditionalHeader("Origin", "https://creditcardapp.bankcomm.com");
		webRequest.setAdditionalHeader("Referer", "https://creditcardapp.bankcomm.com/idm/sso/auth.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", username));
		paramsList.add(new NameValuePair("mobile", "18610007920"));

		webRequest.setRequestParameters(paramsList);

		Page getcode_page = webClient.getPage(webRequest);

		CodeMsgBean codeMsgBean = BocomCreditcardParse
				.codeMsg_parse(getcode_page.getWebResponse().getContentAsString());

		return codeMsgBean;
	}

	public String getCardNo(WebClient webClient) throws Exception {

		String url = "https://creditcardapp.bankcomm.com/member/member/service/billing/index.html";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		Page searchPage = webClient.getPage(webRequest);

		Document doc = Jsoup.parse(searchPage.getWebResponse().getContentAsString());

		String cardNo = doc.select("div.slider-content").attr("val");

		return cardNo;
	}

	@Retryable(value = CodeErrorException.class, maxAttempts = 3)
	public String getpot(BankJsonBean bankJsonBean) {

		try {
			String path = CommonUnitForBocomCreditcard.saveImgXY(driver, By.cssSelector("div.key-con li:nth-child(1)"),
					By.cssSelector("div.key-con li:nth-child(10)"));
			tracerLog.output("截图路径path", path);

			Thread.sleep(1000L);

			String code = AbstractTesseserHandlerByTess4j.getVerifycodeByTess4j(new File(path));

			String pot = CommonUnitForBocomCreditcard.getPot(code, bankJsonBean.getPassword().trim(), 0);
			tracerLog.output("pot", pot);

			return pot;
		} catch (Exception e) {
			throw new CodeErrorException("密码截图验证错误");
		}
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

	public void changSucessNeeSMS(TaskBank taskBank) {
		tracerLog.output("crawler.bank.login.success", "登陆成功 ,但需要用户短信验证码验证");

		Document doc = Jsoup.parse(driver.getPageSource());

		String username = doc.select("input#username").attr("value");

		taskBank.setCookies(CommonUnitForBank.transcookieToJsonBySelenium(driver));
		taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription());
		taskBank.setError_code(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code());
		taskBank.setParam(username);
		taskBank = taskBankRepository.save(taskBank);

		driver.findElement(By.id("send_Button")).click();

		try {
			String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("crawler.bank.login.changSucessNeeSMS.exception", path);
		} catch (Exception e1) {

		}

		Document doc2 = Jsoup.parse(driver.getPageSource());
		String error_texg = doc2.select("span.send-to-moible").first().text();

		taskBank.setCookies(CommonUnitForBank.transcookieToJsonBySelenium(driver));
		taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription() + "    " + error_texg);
		taskBank.setError_code(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code());
		taskBank.setParam(username);
		taskBank = taskBankRepository.save(taskBank);

	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-bocom-creditcard 所属包名：app.service 类描述：
	 * 填入验证码并点击（未完成） 创建人：hyx 创建时间：2018年6月14日
	 * 
	 * @version 1 返回值 void
	 */
	public void getSMSCode(BankJsonBean bankJsonBean) {

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

		driver.findElement(By.id("send_Button")).click();

		try {
			String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("crawler.bank.login.changSucessNeeSMS.exception", path);
		} catch (Exception e1) {

		}

		Document doc2 = Jsoup.parse(driver.getPageSource());
		String error_texg = doc2.select("span.send-to-moible").first().text();

		taskBank.setCookies(CommonUnitForBank.transcookieToJsonBySelenium(driver));
		taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription() + "    " + error_texg);
		taskBank.setError_code(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code());
		taskBank = taskBankRepository.save(taskBank);

	}
	
	/**   
	  *    
	  * 项目名称：common-microservice-bank-bocom-creditcard  
	  * 所属包名：app.service
	  * 类描述：  验证短信验证码
	  * 创建人：hyx 
	  * 创建时间：2018年6月21日 
	  * @version 1  
	  * 返回值    void
	 * @return 
	  */
	public WebDriver setSMSCode(BankJsonBean bankJsonBean) {

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

		driver.findElement(By.id("moibleMessages")).sendKeys(bankJsonBean.getVerification().trim());
		driver.findElement(By.id("submit")).click();

		try {
			String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("crawler.bank.login.changSucessNeeSMS.exception", path);
		} catch (Exception e1) {

		}
		
		try {
			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(By.className("userinf"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.bank.login.error", e.getMessage());
			tracerLog.output("crawler.bank.login.account.error", taskBank.getTaskid());
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}
			Document doc2 = Jsoup.parse(driver.getPageSource());
			String error_texg = doc2.select("span.send-to-moible").first().text();

			taskBank.setCookies(CommonUnitForBank.transcookieToJsonBySelenium(driver));
			taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
			taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription() + "    " + error_texg);
			taskBank.setError_code(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code());
			taskBank = taskBankRepository.save(taskBank);
			String htmlsource3 = driver.getPageSource();
			
			tracerLog.output("错误失败页面", "<xmp>" + htmlsource3 + "</xmp>");
		
			// driver.quit();
			agentService.releaseInstance(bankJsonBean.getIp(), driver);

			return null;
		}

		tracerLog.output("crawler.bank.login.success", "登陆成功");
		tracerLog.output("crawler.bank.login.account.success", taskBank.getTaskid());
		 taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
				BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid());
		 
		 return driver;

	}

}
