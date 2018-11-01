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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.czbchina.CzbChinaDebitCardHtml;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.czbchina.CzbChinaDebitCardHtmlRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.czbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.czbchina" })
public class CzbChinaService extends WebDriverIEService implements ICrawlerLogin, ISms{

	@Autowired
	private CzbChinaDebitCardHtmlRepository czbChinaDebitCardHtmlRepository;

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

	@Autowired
	private TaskBankRepository taskBankRepository;

	static String pageurl = "https://perbank.czbank.com/PERBANK/EBank";

	static String LoginPage = "https://perbank.czbank.com/PERBANK/logon.jsp";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	/**
	 * @Des 打开登录页面
	 * @param 无
	 */
	public WebDriver openloginCmbChina() {
		driver = webDriverIEService.getNewWebDriver();
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "开始登陆浙商银行登陆页");
		try {
			driver = getPage(driver, LoginPage);
		} catch (NoSuchWindowException e) {
			tracerLog.addTag("打开浙商登录页面报错，尝试重新初始化游览器", e.getMessage());
			driver = getPage(driver, LoginPage);
		}
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "浙商银行登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());
		return driver;
	}

	/**
	 * @Des 开始爬取（异步）
	 * @param bankJsonBean
	 */
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		try {
			String webdriverHandle = bankJsonBean.getWebdriverHandle();// 获取登录步骤的webdriverHandle
			tracerLog.addTag("当前的 webdriverHandle：", driver.getWindowHandle());
			tracerLog.addTag("Task表中的 webdriverHandle：", webdriverHandle);

			TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

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

				String dse_sessionId = "";
				String accNo = "";
				String accBalance = "";
				String avlBalance = "";
				String accType = "";
				String subaccType = "";
				String state = "";
				String openNode = "";
				String currency = "";
				
				try {
					JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
					driver.switchTo().frame("main");
					driver_js.executeScript(
					"javascript:doTranDispatch('PB020002-PB020200');showMenuPlace('PB250000','PB250200','PB020200');");
					Thread.sleep(1000L);
					driver.switchTo().frame("mainframe");
					String json = driver.getPageSource();
					
					Document dse_doc = Jsoup.parse(json);
					dse_sessionId = dse_doc.getElementsByAttributeValue("name", "dse_sessionId").get(0).val();
					
					String mingxiSearch = "javascript:detailInfo(";
					if (json.contains(mingxiSearch)) {
						json = json.substring(json.indexOf(mingxiSearch) + mingxiSearch.length());
						if (json.contains(");")) {
							json = json.substring(0, json.indexOf(");"));
							json = json.replace("'", "");
							String[] split = json.split(",");
							accNo = split[0];
							accBalance = split[1];
							avlBalance = split[2];
							accType = split[3];
							subaccType = split[4];
							state = split[5];
							openNode = split[7];
							currency = split[8];
						}
					}
					driver_js.executeScript("javascript:mingxiSearch('" + accNo + "');");
					
					driver.switchTo().defaultContent();
//					driver_js.executeScript(
//							"javascript:tranSwitch();doTranDispatch('PB250000-PB250000');showMenuPlace('PB250000','','');");
//					Thread.sleep(1000L);
//					driver_js.executeScript(
//							"javascript:doTranDispatch('PB250200-PB250200');DoMenu('PB250200');changLastMenuClass();");
//					Thread.sleep(1000L);
//					driver_js.executeScript(
//							"javascript:doTranDispatch('PB020401-PB020401');changMenuClass('PB020401');");
//					Thread.sleep(1000L);
//					driver.switchTo().frame("mainframe");
//					driver_js.executeScript("javascript:check();");
//					Thread.sleep(1000L);
//					driver.switchTo().defaultContent();
				} catch (Exception e) {
					tracerLog.addTag("driver_js:ERROR", e.getMessage());
					e.printStackTrace();
				}

				WebClient webClient = WebCrawler.getInstance().getWebClient();

				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

				for (org.openqa.selenium.Cookie cookie : cookies) {
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
							"perbank.czbank.com", cookie.getName(), cookie.getValue()));
				}
				String cookieString = CommonUnit.transcookieToJson(webClient); // 存储cookie

				
				String endDate = getDateBefore("yyyyMMdd", 0);
				String startDate = getDateBefore("yyyyMMdd", 12);

				String html = getbilldetailsfirst(cookieString, dse_sessionId, startDate, endDate,
						accNo, 1, bankJsonBean.getTaskid());
				parserService.billDetailsParser(html, bankJsonBean.getTaskid());
				try {
					int pages = 0;
					String count = "";
					try {
						String turnPage = "javascript:turnPage('";
						if(html.contains(turnPage)){
							html = html.substring(html.lastIndexOf(turnPage) + turnPage.length());
							if(html.contains("'")){
								html = html.substring(0, html.indexOf("'"));
								count = html;
							}
						}
						pages = Integer.valueOf(count) + 1;
					} catch (Exception e) {
						e.printStackTrace();
						tracerLog.addTag("页数转化失败html:" + html, e.toString());
					}
					
//					Document doc = Jsoup.parse(html);
//					Elements elementsByClass2 = doc.getElementsByClass("pageText");
//					int pages = 0;
//					String count = elementsByClass2.text();
//					System.out.println("javascript:turnPage"+count);
//					if (count.length() > 5) {
//						count = count.substring(3, count.length() - 2);
//					}
//					try {
//						pages = Integer.valueOf(count) / 10 + 1;
//					} catch (Exception e) {
//						e.printStackTrace();
//						tracerLog.addTag("页数条数转化失败:" + count, e.toString());
//					}
					for (int i = 2; i < pages; i++) {
						try {
							String htmlPage = getbilldetailsfirst(cookieString, dse_sessionId, startDate, endDate,
									accNo, i, bankJsonBean.getTaskid());
							parserService.billDetailsParser(htmlPage, bankJsonBean.getTaskid());
						} catch (Exception e) {
							e.printStackTrace();
							tracerLog.addTag("循环翻页查询明细报错:" + i, e.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					tracerLog.addTag("未找到条数:", e.toString());
				}
				taskBank = taskBankStatusService.updateTaskBankTransflow(200,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				
//				String deposittimeHtml = getdeposit_time(cookieString, dse_sessionId, bankJsonBean.getTaskid());
				
				String userHtml = getUser(cookieString, dse_sessionId, accNo, accBalance, avlBalance, accType,
						subaccType, state, openNode, currency, bankJsonBean.getTaskid());
				parserService.userInfoParser(userHtml, accNo, bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());

			}
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			return taskBank;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CzbChinaService.getDateCombo---ERROR:", e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(404, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
		}
		return null;
	}

	/**
	 * 开户/认购日
	 * 
	 * @param cookieString
	 * @param dse_sessionId
	 * @param certNo
	 * @return
	 * @throws Exception
	 */
	public String getdeposit_time(String cookieString, String dse_sessionId, String taskid) throws Exception {
		WebClient webClient = addcookie(cookieString);
		String url1 = "https://perbank.czbank.com/PERBANK/Trans?" + "dse_sessionId=" + dse_sessionId + "&netType="
				+ "&dse_parentContextName=" + "&dse_operationName=myAssetQrySrvOp"
				// + "&dse_pageId=48"
				+ "&dse_processorState=" + "&dse_processorId=" + "&certType=1" + "&certNo=" + "&qryFlag=0";
		
		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		saveHtml(taskid, "deposit_time", "1", url1, html);

		return html;
	}

	/**
	 * 获取个人信息及定期存款
	 * 
	 * @param cookieString
	 * @param dse_sessionId
	 * @param payAccNo
	 * @return
	 * @throws Exception
	 */
	public String getUser(String cookieString, String dse_sessionId, String accNo, String accBalance, String avlBalance,
			String accType, String subaccType, String state, String openNode,
			String currency, String taskid) throws Exception {
		WebClient webClient = addcookie(cookieString);
		
		String url1 = "https://perbank.czbank.com/PERBANK/Trans";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("dse_sessionId", dse_sessionId));
		paramsList1.add(new NameValuePair("netType", ""));
		paramsList1.add(new NameValuePair("dse_parentContextName", ""));
		paramsList1.add(new NameValuePair("dse_operationName", "accclassifySrvOp"));
		paramsList1.add(new NameValuePair("dse_processorState", ""));
		paramsList1.add(new NameValuePair("dse_processorId", ""));
		paramsList1.add(new NameValuePair("cmd", "0"));
		paramsList1.add(new NameValuePair("accNo", accNo));
		paramsList1.add(new NameValuePair("accBalance", accBalance));
		paramsList1.add(new NameValuePair("avlBalance", avlBalance));
		paramsList1.add(new NameValuePair("accType", accType));
		paramsList1.add(new NameValuePair("subaccType", subaccType));
		paramsList1.add(new NameValuePair("openNode", openNode));
		paramsList1.add(new NameValuePair("accState", state));
		paramsList1.add(new NameValuePair("accCurrency", currency));
		
		//old请求
//		String url1 = "https://perbank.czbank.com/PERBANK/Trans?" + "dse_sessionId=" + dse_sessionId + "&netType="
//				+ "&dse_parentContextName=" + "&dse_operationName=balanceQrySrvOp"
//				// + "&dse_pageId=48"
//				+ "&balanceReqIcoll.0.payAccNo=" + payAccNo + "&balanceReqIcoll.0.payAccType=901"
//				+ "&balanceReqIcoll.0.tranOutCurr=01&receiveNum=1&qryFlag=0&subAccType=null&opDownload=0&isSubAcc=0";

		
		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		saveHtml(taskid, "czbchina_debitcard_deposit", "1", url1, html);

		return html;
	}

	/**
	 * 获取明细
	 * 
	 * @param cookieString
	 * @param dse_sessionId
	 * @param startDate
	 * @param endDate
	 * @param accountNo
	 * @param nowPage
	 * @return
	 * @throws Exception
	 */
	public String getbilldetailsfirst(String cookieString, String dse_sessionId, String startDate, String endDate,
			String accountNo, int nowPage, String taskid) throws Exception {
		
		/**
		 * 下一页
		 */
		/*	dse_sessionId=DJDVIZGTINGNFSEAIBCSIODYGUGYFIIWFKHJBJEN
		 * &netType=&dse_parentContextName=&dse_operationName=tradeListQrySrvOp
		 * &dse_pageId=174&dse_processorState=&dse_processorId=&vonType=0
		 * &nowPage=2
		 * &opDownload=0&zjlxFlag=0&datePeriod=&formOption=1
			*/
		
		WebClient webClient = addcookie(cookieString);
		String url = "https://perbank.czbank.com/PERBANK/Trans?"
				+ "dse_sessionId="+dse_sessionId
				+ "&netType="
				+ "&dse_parentContextName="
				+ "&dse_operationName=tradeListQrySrvOp"
//				+ "&dse_pageId=29"
				+ "&dse_processorState="
				+ "&dse_processorId="
				+ "&vonType=0"
				+ "&menuCode=PB020200"
				+ "&accountNo="+accountNo
				+ "&accountType=901"
				+ "&currCode=01"
				+ "&startDate="+startDate
				+ "&endDate="+endDate
				+ "&curtype=01"
				+ "&datePeriod="
				+ "&zjlxFlag=0"
				+ "&cashRemit="
				+ "&formOption=1";
		//old
//		String url = "https://perbank.czbank.com/PERBANK/Trans?"
//				+ "dse_sessionId="+dse_sessionId
//				+ "&netType="
//				+ "&dse_parentContextName="
//				+ "&dse_operationName=tradeListQrySrvOp"
////				+ "&dse_pageId=33"
//				+ "&dse_processorState="
//				+ "&dse_processorId="
//				+ "&vonType=0"
//				+ "&curtype=01"
//				+ "&cashRemit=1"
//				+ "&startDate="+startDate
//				+ "&endDate="+endDate
//				+ "&menuCode=PB020401"
//				+ "&accountNo="+accountNo
//				+ "&accountType=901"
//				+ "&currCode=01"
//				+ "&opFlag=0"
//				+ "&accNo=";
		if (nowPage > 1) {
			url = url + "&nowPage=" + nowPage;
		}

		Page page1 = getPage(webClient, taskid, url, HttpMethod.GET, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		saveHtml(taskid, "czbchina_debitcard_billdetails", "页数：" + nowPage, url, html);

		return html;
	}

	/**
	 * @Des 登录
	 * @param bankJsonBean
	 */
	public TaskBank loginCombo(BankJsonBean bankJsonBean, int count) throws Exception {
		tracerLog.addTag("loginCombo", "开始登陆浙商银行" + bankJsonBean.getLoginName());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			// 打开IE游览器，访问浙商行的登录页面
			WebDriver webDriver = openloginCmbChina();
			String windowHandle = webDriver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracerLog.addTag("登录浙商银行，打开网页获取网页handler", windowHandle);

			driver.findElement(By.id("flag0")).click();

			driver.findElement(By.id("custId")).sendKeys(bankJsonBean.getLoginName());
			tracerLog.addTag("开始输入Tab", "Tab");
			// 键盘输入Tab，让游览器焦点切换到密码框
			VK.Tab();
			tracerLog.addTag("开始输入密码", bankJsonBean.getPassword());
			// 键盘输入查询密码
			Thread.sleep(500L);
			VK.KeyPress( bankJsonBean.getPassword());

			// 验证码
			String pathcode = WebDriverUnit.saveImg(webDriver, By.id("dynamicPwdPer"));
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					pathcode);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			tracerLog.addTag("验证码code ====>>", code);
			webDriver.findElement(By.id("certifyPin")).sendKeys(code);

			tracerLog.addTag("开始点击登陆按钮", "#LoginBtn");
			// 点击游览器的登录按钮
			webDriver.findElement(By.id("logonBtId")).click();
			Thread.sleep(1000L);
			String errorfinfo = "";
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			try {
				Alert alert = wait.until(new ExpectedCondition<Alert>() {
					@Override
					public Alert apply(WebDriver driver) {
						try {
							return driver.switchTo().alert();
						} catch (NoAlertPresentException e) {
							return null;
						}
					}
				});
				errorfinfo = alert.getText();
				alert.accept();
			} catch (Exception e) {
				e.printStackTrace();
				tracerLog.addTag("#errorfinfo-------", "--------没有Alert");
			}
			String path = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
			if (errorfinfo != null && errorfinfo.length() > 0) {
				tracerLog.addTag("登录失败,错误信息" + errorfinfo, "截图:" + path);
				if (errorfinfo.contains("验证码输入有误") && count < 3) {
					driver.quit();
					loginCombo(bankJsonBean, ++count);
					return null;
				} else {
					if(errorfinfo.contains("验证码输入有误")){
						errorfinfo = "系统超时,请稍后再试";
					}
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);
					// 登录错误，释放资源
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			} else {
				String currentPageURL = webDriver.getCurrentUrl();
				if (pageurl.equals(currentPageURL)) {
					WebElement main = null;
					try {
						main = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(By.id("errArea"));
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (main != null) {
						//截图
						String pathE = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
						errorfinfo = main.getText();
						if (errorfinfo != null && errorfinfo.length() > 0) {
						} else {
							errorfinfo = "登录超时,请重新登录!";
						}
						tracerLog.addTag("登录失败,错误信息" + errorfinfo, "截图:" + pathE);
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						// 登录错误，释放资源
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
						
					} else {
						WebElement reference = null;
						try {
							reference = wait.until(new Function<WebDriver, WebElement>() {
								public WebElement apply(WebDriver driver) {
									return driver.findElement(By.id("reference"));
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
						//截图
						String pathS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
						if (reference != null) {
							tracerLog.addTag("loginczbchina", "需进行短信，开始发送短信验证码");
							taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
									BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
									BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
									BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
									BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(),
									windowHandle);
							tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
							tracerLog.addTag("登录成功", "登录成功截图:" + pathS);
							
						}else{
							taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
									BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
									BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
									BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
									BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false,
									bankJsonBean.getTaskid(), windowHandle);
							tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
							tracerLog.addTag("登录成功", "登录成功截图:" + pathS);
						}
						//bindDevice2
						//reference
						//input_dynpwd
					}
				} else {
					tracerLog.addTag("登录遇到了未知的链接", "未知的链接" + currentPageURL);
					errorfinfo = "登录超时,请重新登录!";
					tracerLog.addTag("登录失败,错误信息", errorfinfo);
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);
					tracerLog.addTag("登录遇到了未知的链接登录失败", "登录失败截图:" + path);
					// 登录错误，释放资源
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
	 * @Des 发送短信验证码
	 * @param BankJsonBean
	 * @return TaskBank
	 */
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
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				return taskBank;
			} else {
				tracerLog.addTag("sendSMS", "开始发送短信验证码" + bankJsonBean.toString());
				
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS)
						.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				
				WebElement reference = null;
				try {
					reference = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("reference"));
						}
					});
				} catch (Exception e) {
					tracerLog.addTag("#reference为空,发送短信验证码未知的错误", bankJsonBean.getTaskid());
					e.printStackTrace();
				}
				if (reference != null) {
					reference.click();
					//点击一次好像没反应。。。。。
					reference.click();
					// 短信验证码已发送，请注意查收,等一秒钟在改状态，从页面点击到真发出短信有一定延迟
					Thread.sleep(1000L);
					/******* 测试 *****设置当前是否为常用设备(默认为是)**/
//					WebElement bindDevice2 = null;
//					try {
//						bindDevice2 = wait.until(new Function<WebDriver, WebElement>() {
//							public WebElement apply(WebDriver driver) {
//								return driver.findElement(By.id("bindDevice2"));
//							}
//						});
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					if (bindDevice2 != null) {
//						bindDevice2.click();
//					}
					/******* 测试 *******/
				}
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CzbChinaService.sendSms---ERROR:", e.toString());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}
	
	
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
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				return taskBank;
			} else {
				tracerLog.addTag("sendSMS", "开始验证短信验证码" + bankJsonBean.toString());
				// 短信验证码的input 输入框
				WebElement input_dynpwd = null;
				try {
					input_dynpwd = driver.findElement(By.id("input_dynpwd"));
				} catch (Exception e) {
					String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("#textSendCode为空，未知的错误", "截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
				}
				if (input_dynpwd == null) {
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					return taskBank;
				} else {
					input_dynpwd.clear();
					input_dynpwd.sendKeys(bankJsonBean.getVerification());
					Thread.sleep(500L);
					driver.findElement(By.id("logonBtId2")).click();
					Thread.sleep(1000L);
					Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(6, TimeUnit.SECONDS)
							.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
					WebElement mainWorkArea = null;
					try {
						mainWorkArea = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(By.id("pb_main")); // 这是不需要短信验证码，直接进入主页面的div的
							}
						});
					} catch (Exception e) {
						// 详单主页面未出现 截图
						String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
						tracerLog.addTag("#mainWorkArea 元素等待10秒未出现,异常:" + e.toString() + " ",
								"截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
						// 判断是否还停留在短信验证页面
						WebElement controlExplain = driver.findElement(By.className("u-errorBox"));
						if (controlExplain == null) {
							tracerLog.addTag("#u-errorBox为空，未知的错误",
									"截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
							taskBank = taskBankStatusService.changeStatus(
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false,
									bankJsonBean.getTaskid());
							agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
							return taskBank;
						} else {
							// 短信验证错误
							String errorfinfo = controlExplain.getText();// errorfinfo
																			// 一般是:动态密码输入错误或动态密码失效!
							taskBank = taskBankStatusService.changeStatus(
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(), errorfinfo,
									BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), false,
									bankJsonBean.getTaskid());
							agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
							return taskBank;
						}
					}

					if (mainWorkArea != null) {
						String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
						tracerLog.addTag("#pb_main 元素不为空，可以直接采集数据",
								"截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
								BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
						return taskBank;
					} else {
						String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
						tracerLog.addTag("#pb_main为空，未知的错误", "截图:" + path + " 当前页面URL:" + driver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
								BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
						return taskBank;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CzbChinaService.verifySms---ERROR:", e.toString());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
		}
		return taskBank;
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
		tracerLog.addTag("czbChinaService.getPage---url:", url + "statusCode:"+statusCode+"---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
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
	public void saveHtml(String taskid, String type, String pageCount, String url, String html) {
		try {
			CzbChinaDebitCardHtml cZbChinaDebitCardHtml = new CzbChinaDebitCardHtml(taskid, type, pageCount, url, html);
			czbChinaDebitCardHtmlRepository.save(cZbChinaDebitCardHtml);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CzbChinaService.saveHtml---ERROR:", e.toString());
		}
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		try {
			loginCombo(bankJsonBean,0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CzbChinaService.login---ERROR:", e.toString());
		}
		return null;
	}

}
