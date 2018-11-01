package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaCreditCardHtml;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.cmbchina.CmbChinaCreditCardHtmlRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.cmbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.cmbchina" })
public class CmbChinaService extends WebDriverIEService implements ICrawlerLogin, ISms {

	@Autowired
	private CmbChinaCreditCardHtmlRepository cmbChinaCreditCardHtmlRepository;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private ParserService parserService;

	@Autowired
	private TaskBankStatusService taskBankStatusService;
	
	@Autowired
	private WebDriverIEService webDriverIEService;

	@Autowired
	private AgentService agentService;

	private WebDriver driver;

	static String GenIndex = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx";

	static String genLoginVerifyM2 = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";

	static String LoginPage = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";

	@Autowired
	private TaskBankRepository taskBankRepository;
	/**
	 * @Des 输入验证码,开始短信验证
	 * @param BankJsonBean
	 */
	@Async
	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		String webdriverHandle = bankJsonBean.getWebdriverHandle();// 获取登录步骤的webdriverHandle
		tracerLog.addTag("当前的 webdriverHandle：", driver.getWindowHandle());
		tracerLog.addTag("Task表中的 webdriverHandle：", webdriverHandle);
		TaskBank taskBank = null;
		try {
			// 判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
			// 登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle
			// 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
			if (webdriverHandle == null || !webdriverHandle.equals(driver.getWindowHandle())) {
				tracerLog.addTag("RuntimeException", "当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(), false, bankJsonBean.getTaskid());
				return taskBank;
			} else {
				tracerLog.addTag("sendSMS", "开始验证短信验证码" + bankJsonBean.toString());
				// 短信验证码的input 输入框
				WebElement textSendCode = null;
				try {
					textSendCode = driver.findElement(By.id("txtSendCode"));
				} catch (NoSuchElementException e) {
					String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("#textSendCode为空，未知的错误", "截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
				}
				if (textSendCode == null) {
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
					return taskBank;
				} else {
					textSendCode.clear();
					textSendCode.sendKeys(bankJsonBean.getVerification());
					Thread.sleep(500L);
					driver.findElement(By.id("btnVerifyCode")).click();
					Thread.sleep(1000L);
					Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS)
							.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
					WebElement mainWorkArea = null;
					try {
						mainWorkArea = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(By.id("mainWorkArea")); // 这是不需要短信验证码，直接进入主页面的div的
																					// ID
							}
						});
					} catch (Exception e) {
						// 详单主页面未出现 截图
						String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
						tracerLog.addTag("#mainWorkArea 元素等待10秒未出现,异常:" + e.toString() + " ",
								"截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());

						// 判断是否还停留在短信验证页面
						WebElement controlExplain = driver.findElement(By.className("control-explain"));
						if (controlExplain == null) {
							tracerLog.addTag("#control-explain为空，未知的错误",
									"截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
							taskBank = taskBankStatusService.changeStatus(
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false,
									bankJsonBean.getTaskid());
							return taskBank;
						} else {
							// 短信验证错误
							String errorfinfo = controlExplain.getText();// errorfinfo
																			// 一般是:
																			// "验证码校验失败，原因：请输入正确的验证码或重新申请验证码。ErrCode:02"
							taskBank = taskBankStatusService.changeStatus(
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(), errorfinfo,
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), false,
									bankJsonBean.getTaskid());
							return taskBank;
						}
					}

					if (mainWorkArea != null) {
						String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
						tracerLog.addTag("#mainWorkArea 元素不为空，可以直接采集数据",
								"截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
						return taskBank;
					} else {
						String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
						tracerLog.addTag("#mainWorkArea为空，未知的错误", "截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
						return taskBank;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.verifySms---ERROR:", e.toString());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	/**
	 * @Des 打开招行登录页面
	 * @param 无
	 */
	public WebDriver openloginCmbChina() {
		driver = webDriverIEService.getNewWebDriver();
		System.out.println("WebDriverIEService loginCmbChina Msg 开始登陆招商银行登陆页");
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "开始登陆招商银行登陆页");
		try {
			driver = getPage(driver, LoginPage);
		} catch (NoSuchWindowException e) {
			System.out.println("打开招行登录页面报错，尝试重新初始化游览器" + e.getMessage());
			tracerLog.addTag("打开招行登录页面报错，尝试重新初始化游览器", e.getMessage());
			driver = getPage(driver, LoginPage);
		}
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "招商银行登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());
		return driver;
	}

	/**
	 * @Des 发送短信验证码
	 * @param BankJsonBean
	 * @return TaskBank
	 */
	@Async
	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		String webdriverHandle = bankJsonBean.getWebdriverHandle();// 获取登录步骤的webdriverHandle
		tracerLog.addTag("当前的 webdriverHandle：", driver.getWindowHandle());
		tracerLog.addTag("Task表中的 webdriverHandle：", webdriverHandle);
		TaskBank taskBank = null;
		try {
			// 判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，此判断非常重要！
			// 登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle
			// 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
			if (webdriverHandle == null || !webdriverHandle.equals(driver.getWindowHandle())) {
				tracerLog.addTag("RuntimeException", "当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(), false, bankJsonBean.getTaskid());
				return taskBank;
			} else {
				tracerLog.addTag("sendSMS", "开始发送短信验证码" + bankJsonBean.toString());
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS)
						.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("btnSendCode"));
					}
				});
				ele1.click();
				// 短信验证码已发送，请注意查收,等一秒钟在改状态，从页面点击到真发出短信有一定延迟
				Thread.sleep(1000L);
				
				//span
				//lbMobileNo
				String mobile  = "";
				
				String inputValue = ele1.getAttribute("value");
				tracerLog.addTag("inputValue", inputValue);
				/*
				 * if(inputValue.contains("")){
				 * 
				 * }
				 */
				WebElement main = null;
				try {
					main = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("lbMobileNo"));
						}
					});
				} catch (Exception e) {
					tracerLog.addTag("短信验证码发送手机号码未找到", "MSG:" + e.getMessage());
					e.printStackTrace();
				}
				if (main != null) {
					mobile = main.getText();
				}
				tracerLog.addTag(bankJsonBean.getTaskid()+"短信验证码发送手机号码mobile",mobile);
				
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription() + ",预留手机号[" + mobile + "]",
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.sendSms---ERROR:", e.toString());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	/**
	 * @Des 开始爬取（异步）
	 * @param bankJsonBean
	 */
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		tracerLog.addTag("crawler:", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			String webdriverHandle = bankJsonBean.getWebdriverHandle();// 获取登录步骤的webdriverHandle
			tracerLog.addTag("当前的 webdriverHandle：", driver.getWindowHandle());
			tracerLog.addTag("Task表中的 webdriverHandle：", webdriverHandle);

			// 判断数据库中的WindowHandle和当前游览器的WindowHandle是否一致，次判断非常重要！
			// 登录后，需要短信验证码或爬取，需要基于登录环节的seleunim WindowHandle
			// 继续下去，如果WindowHandle不匹配。则无法连贯继续下去。例如登录在机器A，爬取在机器B，
			if (webdriverHandle == null || !webdriverHandle.equals(driver.getWindowHandle())) {
				tracerLog.addTag("RuntimeException", "当前的webdriverHandle 和 数据库中的 webdriverHandle 不匹配");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WEBDRIVER_ERROR.getPhase(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getPhasestatus(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getDescription(),
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(), true, bankJsonBean.getTaskid());

				// 释放instance ip ，quit webdriver
				tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

				return taskBank;
			} else {
				tracerLog.addTag("WindowHandle 匹配，获取数据开始", "getDateCombo()");
				boolean cardType = false;
				String CardsInfoForType = driver.findElement(By.id("CardsInfoForType")).getAttribute("value");
				JSONObject jsonObj = JSONObject.fromObject(CardsInfoForType);
				if(jsonObj.has("C")){
					String type = jsonObj.getString("C");
					if("true".equals(type)){
						cardType = true;
					}
				}
				if (!cardType) {
					tracerLog.addTag(bankJsonBean.getTaskid() + "cardType", "卡类型有误"+CardsInfoForType);
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(),
							"对不起，您没有绑定信用卡，无法使用相关功能！",
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(), true, bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

					return taskBank;
				}
				
				JavascriptExecutor driver_js = ((JavascriptExecutor) driver);

				// 客户管理--------->客户综合查询
				driver_js.executeScript(
						"CallFuncEx2('C','CBANK_CREDITCARD_CUSTOMER','Customer/cm_QueryCustomInfo.aspx','FORM',null);");

				Thread.sleep(1000L);
				driver.switchTo().frame("mainWorkArea");

				String pageSource = driver.getPageSource();

				tracerLog.addTag(bankJsonBean.getTaskid() + "客户管理--------->客户综合查询页面", "<xmp>" + pageSource + "</xmp>");

				parserService.userInfoParser(pageSource, bankJsonBean.getTaskid());
				saveHtml(bankJsonBean.getTaskid(), "cmbchina_creditcard_userinfo", "1");

				driver.switchTo().defaultContent();

				try {
					// 积分管理--------->积分查询
					driver_js.executeScript(
							"CallFuncEx2('C','CBANK_CREDITCARD_CUSTOMER','Points/pm_QueryHistPoints.aspx','FORM',null);");
					Thread.sleep(1000L);
					driver.switchTo().frame("mainWorkArea");
					
					String pageSourceIntegral = driver.getPageSource();
					tracerLog.addTag(bankJsonBean.getTaskid() + "积分管理--------->历史积分查询",
							"<xmp>" + pageSourceIntegral + "</xmp>");

					Document docIntegral = Jsoup.parse(pageSourceIntegral);
					String __EVENTTARGET = docIntegral.getElementById("__EVENTTARGET").val();
					String __EVENTARGUMENT = docIntegral.getElementById("__EVENTARGUMENT").val();
					String __VIEWSTATE = docIntegral.getElementById("__VIEWSTATE").val();
					String __VIEWSTATEGENERATOR = docIntegral.getElementById("__VIEWSTATEGENERATOR").val();
					String __EVENTVALIDATION = docIntegral.getElementById("__EVENTVALIDATION").val();
//					String ddlYearMonthList = "201711";
					String BtnQuery = docIntegral.getElementById("BtnQuery").val();
					String ClientNo1 = docIntegral.getElementById("ClientNo").val();
					String FunctionName = docIntegral.getElementById("FunctionName").val();

					WebClient webClient1 = WebCrawler.getInstance().getWebClient();
					Set<org.openqa.selenium.Cookie> cookies1 = driver.manage().getCookies();
					for (org.openqa.selenium.Cookie cookie : cookies1) {
						webClient1.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
								"pbsz.ebank.cmbchina.com", cookie.getName(), cookie.getValue()));
					}
					String cookieString1 = CommonUnit.transcookieToJson(webClient1); // 存储cookie

					for (int i = 0; i < 12; i++) {
						String yearmonth = getDateBefore("yyyyMM", i);
						String html = getIntegral(cookieString1, __EVENTTARGET, __EVENTARGUMENT, __VIEWSTATE,
								__VIEWSTATEGENERATOR, __EVENTVALIDATION, yearmonth, BtnQuery, ClientNo1,
								FunctionName);
						parserService.integralParser(html, bankJsonBean.getTaskid(), yearmonth);
						saveHtml(bankJsonBean.getTaskid(), "cmbchina_creditcard_integral", yearmonth);
					}
				} catch (Exception e) {
					e.printStackTrace();
					tracerLog.addTag("CmbChinaService.积分---ERROR:", e.toString());
				}

				driver.switchTo().defaultContent();

				// 账户管理--------->账户查询
//				driver_js.executeScript(
//						"CallFuncEx2('C','CBANK_CREDITCARD_ACCOUNT', 'Account/am_QueryAccount.aspx','FORM', null);");
				
				driver_js.executeScript(
						"CallFuncEx2('C','CBANK_CREDITCARD_LOAN','Loan/Pro8/am_QueryReckoningSurveyNew.aspx','FORM',null);");
				Thread.sleep(1000L);
				driver.switchTo().frame("mainWorkArea");
				
//				driver.findElement(By.id("UcTabControl1_panelTd1")).findElement(By.tagName("a")).click();

				String pageSource1 = driver.getPageSource();
				tracerLog.addTag(bankJsonBean.getTaskid() + "账户管理--------->账户查询", "<xmp>" + pageSource1 + "</xmp>");

				parserService.billGeneralParser(pageSource1, bankJsonBean.getTaskid());
				saveHtml(bankJsonBean.getTaskid(), "cmbchina_creditcard_billgeneral", "1");

				Document doc = Jsoup.parse(pageSource1);
				String ClientNo = doc.getElementById("ClientNo").val();
				System.out.println("ClientNo-----------" + ClientNo);
				tracerLog.addTag(bankJsonBean.getTaskid() + "----ClientNo-----------", ClientNo);

				String CreditAccNo = doc.getElementsByAttributeValue("selected", "selected").val();
				System.out.println("CreditAccNo-----------" + CreditAccNo);
				tracerLog.addTag(bankJsonBean.getTaskid() + "----CreditAccNo-----------", CreditAccNo);

				if (ClientNo == null || "".equals(ClientNo) || CreditAccNo == null || "".equals(CreditAccNo)) {
					tracerLog.addTag(bankJsonBean.getTaskid() + "RuntimeException", "关键参数为空，无法获取数据");
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(),
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(),
							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(), true, bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

					return taskBank;
				}

				WebClient webClient = WebCrawler.getInstance().getWebClient();
				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
				for (org.openqa.selenium.Cookie cookie : cookies) {
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
							"pbsz.ebank.cmbchina.com", cookie.getName(), cookie.getValue()));
				}
				String cookieString = CommonUnit.transcookieToJson(webClient); // 存储cookie
				for (int i = 0; i < 12; i++) {
					String yearmonth = getDateBefore("yyyyMM", i);
					String html = getBillDetails(cookieString, ClientNo, CreditAccNo, yearmonth);
					// tracerLog.addTag("CmbChinaService.getDateCombo:" +
					// yearmonth, "<xmp>" + html + "</xmp>");
					parserService.billDetailsParser(html, bankJsonBean.getTaskid(), yearmonth);
					saveHtml(bankJsonBean.getTaskid(), "cmbchina_creditcard_billdetails", yearmonth);
				}
				taskBank = taskBankStatusService.updateTaskBankTransflow(200,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.getDateCombo---ERROR:", e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(404, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
		}
		// 释放instance ip ，quit webdriver
		tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		return taskBank;
	}

	/**
	 * @Des 招商银行通过银行卡号登录
	 * @param bankJsonBean
	 */
	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		tracerLog.addTag("loginCombo", "开始登陆招商银行" + bankJsonBean.getLoginName());
		TaskBank taskBank = null;
		try {
			// 打开IE游览器，访问招行的登录页面
			WebDriver webDriver = openloginCmbChina();

			String windowHandle = webDriver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracerLog.addTag("登录招商银行，打开网页获取网页handler", windowHandle);
			
			
			driver.findElement(By.id("goPassWordLogin")).click();

			tracerLog.addTag("开始输入卡号", bankJsonBean.getLoginName());
			// 键盘输入卡号
			Thread.sleep(500L);
			VK.KeyPress(bankJsonBean.getLoginName());
			
			tracerLog.addTag("开始输入Tab", "Tab");
			// 键盘输入Tab，让游览器焦点切换到密码框
			Thread.sleep(500L);
			VK.Tab();

			tracerLog.addTag("开始输入密码", bankJsonBean.getPassword());
			// 键盘输入查询密码
			Thread.sleep(500L);
			VK.KeyPress(bankJsonBean.getPassword());

			tracerLog.addTag("开始点击登陆按钮", "#LoginBtn");
			System.out.println("开始点击登陆按钮" + "#LoginBtn");
			// 点击游览器的登录按钮
			Thread.sleep(500L);
			webDriver.findElement(By.id("LoginBtn")).click();

			// 等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识form#HomePage(无须短信验证) 则表示登录失败
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement btnVerifyCode = null;
			WebElement mainWorkArea = null;
			// 截图
			String path = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
			try {
				btnVerifyCode = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("btnVerifyCode")); // 这是不需要短信验证码，直接进入主页面的div的
																			// ID
					}
				});

			} catch (Exception e) {
				tracerLog.addTag("#btnVerifyCode 元素等待10秒未出现,异常:" + e.toString() + " 开始判断是否不需要短信",
						"截图:" + path + " 当前页面URL:" + webDriver.getCurrentUrl());
			}
			// 开始判断是否不需要短信
			try {
				mainWorkArea = driver.findElement(By.id("mainWorkArea"));
			} catch (Exception e) {
				tracerLog.addTag("#mainWorkArea未出现,异常:" + e.toString() + " ",
						"截图:" + path + " 当前页面URL:" + webDriver.getCurrentUrl());
			}

			String currentPageURL = webDriver.getCurrentUrl();
			tracerLog.addTag("当前页面URL", currentPageURL);
			if (mainWorkArea == null && btnVerifyCode == null) {
				tracerLog.addTag("登录失败", currentPageURL);
				// 截图
				String pathE = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				// 如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
				if (LoginPage.equals(currentPageURL)) {
					tracerLog.addTag("登录失败.当前页面任是登录页面", currentPageURL);
					String errorfinfo = webDriver.findElement(By.id("errMsgSpan")).getText();
					if (errorfinfo != null && errorfinfo.length() > 0) {
						tracerLog.addTag("登录失败,错误信息", errorfinfo);
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						tracerLog.addTag("登录失败", "登录失败截图:" + pathE);
						// 登录错误，释放资源
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

					} else {
						tracerLog.addTag("登录失败，且没有登录失败错误信息", "截图:" + path + " 当前页面URL:" + webDriver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，未知的错误",
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						tracerLog.addTag("登录失败", "登录失败截图:" + pathE);
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					}
				}
			} else {
				tracerLog.addTag("登录成功", currentPageURL);
				tracerLog.addTag("登录成功 ，登录招商银行，登录成功网页handler", webDriver.getWindowHandle());
				// 截图
				String pathS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				
				if (currentPageURL.equals(GenIndex)) {
					tracerLog.addTag("logincmbchina", "GenIndex 无需进行短信，直接开始获取数据");
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(),
							windowHandle);

					// cmbChinaService.getDateCombo();
					tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
					tracerLog.addTag("登录成功", "登录成功截图:" + pathS);

				} else if (currentPageURL.equals(genLoginVerifyM2)) {
					tracerLog.addTag("logincmbchina", "GenLoginVerifyM2 需进行短信，开始发送短信验证码");
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(),
							windowHandle);

					// cmbChinaService.sendSMS();
					tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
					tracerLog.addTag("登录成功", "登录成功截图:" + pathS);

				} else {
					// TODO 设个地方需要用seleunim 截图并保持url和html，可能是未处理的情况（账号登录成功后，例如
					// 还未绑定银行卡、还未设置安全提问、还未认证等等）
					tracerLog.addTag("登录遇到了未知的链接", "未知的链接" + currentPageURL);
					tracerLog.addTag("登录遇到了未知的链接图片截图", pathS);
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), "");
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), "");
			tracerLog.addTag("登录遇到了未知报错", "MSG:" + e.getMessage());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
		return taskBank;

	}

	/**
	 * 账户查询明细
	 */
	public String getBillDetails(String cookieString, String ClientNo, String CreditAccNo, String month) {
		try {
			WebClient webClient = addcookie(cookieString);
			String url = "https://pbsz.ebank.cmbchina.com/CmbBank_CreditCard_Loan/UI/CreditCard/Loan/am_QueryReckoningListNew.aspx";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("ClientNo", ClientNo));
			paramsList.add(new NameValuePair("O_STMT_FLAG", "Y"));
			paramsList.add(new NameValuePair("IN_YYYYMM", month));
			paramsList.add(new NameValuePair("CreditAccNo", CreditAccNo));
			Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.getBillDetails---ERROR:", e.toString());
		}
		return "";
	}

	/**
	 * 积分查询明细
	 */
	public String getIntegral(String cookieString, String __EVENTTARGET, String __EVENTARGUMENT, String __VIEWSTATE,
			String __VIEWSTATEGENERATOR, String __EVENTVALIDATION, String ddlYearMonthList, String BtnQuery,
			String ClientNo, String FunctionName) {
		try {
			WebClient webClient = addcookie(cookieString);
			String url = "https://pbsz.ebank.cmbchina.com/CmbBank_CreditCard_Customer/UI/CreditCard/Points/pm_QueryHistPoints.aspx";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("__EVENTTARGET", __EVENTTARGET));
			paramsList.add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
			paramsList.add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			paramsList.add(new NameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
			paramsList.add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
			paramsList.add(new NameValuePair("ddlYearMonthList", ddlYearMonthList));
			paramsList.add(new NameValuePair("BtnQuery", BtnQuery));
			paramsList.add(new NameValuePair("ClientNo", ClientNo));
			paramsList.add(new NameValuePair("FunctionName", FunctionName));

			Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.getIntegral---ERROR:", e.toString());
		}
		return "";
	}

	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean
	 */
	public TaskBank quit(BankJsonBean bankJsonBean) {
		// 关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试 ,
		// description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true, bankJsonBean.getTaskid());
		// 调用公用释放资源方法
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		return taskBank;
	}

	/**
	 * @Des 返回当前webdriver的handle ID（测试用）
	 */
	public String getCurrentHandle() {
		if (driver == null) {
			return null;
		} else {
			String currentHandle = driver.getWindowHandle();
			return currentHandle;
		}

	}

	/**
	 * @Des 返回当前webdriver所有的handle ID（测试用）
	 */
	public Set<String> getHandles() {
		if (driver == null) {
			return null;
		} else {
			Set<String> handles = driver.getWindowHandles();
			return handles;
		}

	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, String taskid, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {
		tracerLog.addTag("CmbChinaService.getPage---url:", url + "---taskId:" + taskid);
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracerLog.addTag("CmbChinaService.getPage.statusCode:" + statusCode, url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	public WebClient addcookie(String cookieString) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}

	/**
	 * 保存Html
	 * 
	 * @param taskid
	 * @param type
	 * @param pageCount
	 */
	public void saveHtml(String taskid, String type, String pageCount) {
		try {
			CmbChinaCreditCardHtml cmbChinaCreditCardHtml = new CmbChinaCreditCardHtml(taskid, type, pageCount,
					driver.getCurrentUrl(), driver.getPageSource());
			cmbChinaCreditCardHtmlRepository.save(cmbChinaCreditCardHtml);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.saveHtml---ERROR:", e.toString());
		}
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
