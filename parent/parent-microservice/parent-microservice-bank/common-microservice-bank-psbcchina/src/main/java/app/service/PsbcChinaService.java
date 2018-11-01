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

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.psbcchina.PsbcChinaDebitCardHtml;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.psbcchina.PsbcChinaDebitCardHtmlRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.psbcchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.psbcchina" })
public class PsbcChinaService extends WebDriverIEService implements ICrawlerLogin{

	@Autowired
	private PsbcChinaDebitCardHtmlRepository psbcChinaDebitCardHtmlRepository;

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

	static String LoginPage = "https://pbank.psbc.com/perbank/html/system/login.html";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	/**
	 * @Des 打开登录页面
	 * @param 无
	 */
	public WebDriver openloginCmbChina() {
		driver = webDriverIEService.getNewWebDriver();
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "开始登陆邮政银行登陆页");
		try {
			driver = getPage(driver, LoginPage);
		} catch (NoSuchWindowException e) {
			tracerLog.addTag("打开邮政登录页面报错，尝试重新初始化游览器", e.getMessage());
			driver = getPage(driver, LoginPage);
		}
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "邮政银行登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());
		return driver;
	}

	/**
	 * @Des 开始爬取（异步）
	 * @param bankJsonBean
	 */
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		String taskid = bankJsonBean.getTaskid();
		tracerLog.addTag("crawler", "taskid---"+taskid);
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
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
						BankStatusCode.BANK_WEBDRIVER_ERROR.getError_code(), true, taskid);

				// 释放instance ip ，quit webdriver
				tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				return taskBank;
			} else {
				tracerLog.addTag("WindowHandle 匹配，获取数据开始", "getDateCombo()");
				
				WebClient webClient = WebCrawler.getInstance().getWebClient();

				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

				for (org.openqa.selenium.Cookie cookie : cookies) {
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
							"pbank.psbc.com", cookie.getName(), cookie.getValue()));
				}
				String cookieString = CommonUnit.transcookieToJson(webClient); // 存储cookie
				
//				// 获取个人信息
//				String userHtml = driver.getPageSource();
//				saveHtml(taskid, "psbcchina_debitcard_userinfo", "1", driver.getCurrentUrl(), userHtml);
//				parserService.userInfoParser(userHtml, taskid);
				
				JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
				String  emp_sid = driver_js.executeScript("return $.lily.CONFIG_SESSION_ID;").toString();
				
				String endDate = getDateBefore("yyyyMMdd", 0, 0, 0);
				String startDate = getDateBefore("yyyyMMdd", -1, 0, 0);
				try {
					String url = "https://pbank.psbc.com/perbank/queryAllAccounts.do?currentBusinessCode=00000504&EMP_SID="
							+ emp_sid + "&responseFormat=JSON&channel=1101&version=stanver";
					String parameterHtml = getparameter(cookieString, url, taskid);
					List<String> list = new ArrayList<String>();
					JSONObject jsonObj = JSONObject.fromObject(parameterHtml);
					String ctn = jsonObj.getString("iAccountInfoList");
					JSONArray jsonArray = JSONArray.fromObject(ctn);
					for (Object object : jsonArray) {
						JSONObject obj = JSONObject.fromObject(object);
						String data = obj.getString("accountNo");
						list.add(data);
					}
					for (String string : list) {
						try {
							String html = getbilldetails(cookieString, startDate, endDate, string, emp_sid,
									taskid);
							parserService.billDetailsParser(html, taskid);
						} catch (Exception e) {
							e.printStackTrace();
							tracerLog.addTag(string+"_ERROR:", e.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					tracerLog.addTag("accountNo_ERROR:", e.toString());
				}
				taskBank = taskBankStatusService.updateTaskBankTransflow(200,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), taskid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.getDateCombo---ERROR:", e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					taskid);
			taskBankStatusService.updateTaskBankTransflow(404, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(),
					taskid);
		}
		taskBankStatusService.changeTaskBankFinish(taskid);
		// 释放instance ip ，quit webdriver
		tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		return taskBank;
	}

	/**
	 * 获取参数
	 * 
	 * @param cookieString
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	public String getparameter(String cookieString, String url, String taskid) throws Exception {
		WebClient webClient = addcookie(cookieString);
		
		Page page1 = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		saveHtml(taskid, "accountNo", "1", url, html);

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
	public String getbilldetails(String cookieString, String beginDate, String endDate, String accountNo,
			String emp_sid, String taskid) throws Exception {

		WebClient webClient = addcookie(cookieString);
		String url = "https://pbank.psbc.com/perbank/dealDetailQuery.do";
		
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("currentBusinessCode", "00312000"));
		paramsList1.add(new NameValuePair("turnPageBeginPos", "1"));
		paramsList1.add(new NameValuePair("turnPageShowNum", "10"));
		paramsList1.add(new NameValuePair("tranType", "1"));
		paramsList1.add(new NameValuePair("beginDate", beginDate));
		paramsList1.add(new NameValuePair("endDate", endDate));
		paramsList1.add(new NameValuePair("inOut", "0"));
		paramsList1.add(new NameValuePair("accNo", ""));
		paramsList1.add(new NameValuePair("accountNo", accountNo));
		paramsList1.add(new NameValuePair("bkBookFlg", "2"));
		paramsList1.add(new NameValuePair("EMP_SID", emp_sid));
		paramsList1.add(new NameValuePair("responseFormat", "JSON"));

		Page page1 = getPage(webClient, taskid, url, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		saveHtml(taskid, "psbcchina_debitcard_billdetails",beginDate+"----"+endDate, url, html);

		return html;
	}

	/**
	 * @Des 登录
	 * @param bankJsonBean
	 */
	public TaskBank loginCombo(BankJsonBean bankJsonBean, int i) throws Exception {
		tracerLog.addTag("第"+i+"次"+"loginCombo", "开始登陆邮政银行" + bankJsonBean.getLoginName());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			// 打开IE游览器，访问邮政行的登录页面
			WebDriver webDriver = openloginCmbChina();

			String windowHandle = webDriver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracerLog.addTag("第"+i+"次"+"登录邮政银行，打开网页获取网页handler", windowHandle);
			
			String loginType = bankJsonBean.getLoginType();
			
			if (loginType.equals(StatusCodeLogin.IDNUM)) {//身份证号登录：证件号
				
			} else if (loginType.equals(StatusCodeLogin.PHONE_NUM)) {//手机号：
				webDriver.findElement(By.id("mobType")).click();
			} else if (loginType.equals(StatusCodeLogin.ACCOUNT_NUM)) {// 个人账户号： 用户名
				webDriver.findElement(By.id("logType")).click();
			} else if (loginType.equals(StatusCodeLogin.CARD_NUM)) {//卡号：账号/卡号
				webDriver.findElement(By.id("accType")).click();
			}
			webDriver.findElement(By.id("logonId")).sendKeys(bankJsonBean.getLoginName());
			tracerLog.addTag("第"+i+"次"+"开始输入Tab", "Tab");
			// 键盘输入Tab，让游览器焦点切换到密码框
			Thread.sleep(1100);
			tracerLog.addTag("第"+i+"次"+"开始输入密码", bankJsonBean.getPassword());
			// 键盘输入查询密码
//			VK.Tab();
			driver.findElement(By.id("_ocx_password")).sendKeys(Keys.DOWN);
			VK.KeyPress(bankJsonBean.getPassword());
			
//			String password = bankJsonBean.getPassword();
//			for (int j = 0; j < password.length(); j++) {
//				char chr = password.charAt(j);  
//				VK.KeyPress(chr+"");
//				Thread.sleep(1000L);
//			}
			
			// 验证码
			String pathcode = WebDriverUnit.saveImg(webDriver, By.xpath("//*[@id=\"verifyImg\"]/../.."));
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("6004", LEN_MIN, TIME_ADD, STR_DEBUG,
					pathcode);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			tracerLog.addTag("第"+i+"次"+"验证码code ====>>", code);
			webDriver.findElement(By.id("checkCode")).sendKeys(code);

			tracerLog.addTag("第"+i+"次"+"开始点击登陆按钮", "#LoginBtn");
			Thread.sleep(500L);
			String path1 = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
			tracerLog.addTag("第"+i+"次"+"登录信息", "截图:" + path1);
			// 点击游览器的登录按钮
			webDriver.findElement(By.id("loginIn")).click();
			Thread.sleep(1000L);
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			
			WebElement welMes = null;
			try {
				welMes = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("welMes"));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (welMes != null) {
				String pathS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				
				//您的登录密码已经超过90天未修改，为保障账户安全，请您及时
				String html = webDriver.getPageSource();
				if(html.contains("您的登录密码已经超过90天未修改")){
					JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
					driver_js.executeScript("dook('0','')");
				}
				tracerLog.addTag("第" + i + "次" + "登录成功", "登录成功截图:" + pathS);
				Thread.sleep(1000L);
				webDriver.findElement(By.xpath("//*[@id=\"acc\"]/div[1]/div[1]/div/span[2]")).click();
				Thread.sleep(2000L);
				// 获取个人信息
				String userHtml = driver.getPageSource();
				saveHtml(bankJsonBean.getTaskid(), "psbcchina_debitcard_userinfo", "1", driver.getCurrentUrl(), userHtml);
				parserService.userInfoParser(userHtml, bankJsonBean.getTaskid());
				String pathSS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				tracerLog.addTag("查询个人信息", "登录成功查询个人信息截图:" + pathSS);
				taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(),
						windowHandle);
				
			}else{
				String pathS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				String html = webDriver.getPageSource();
				if(html.contains("首次登录个人网银")){
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "首次登录个人网银,请前往官网完善个人信息！",
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);
					// 首次登录个人网银
					tracerLog.addTag("第" + i + "次" + "释放instance ip ，quit webdriver,首次登录个人网银", taskBank.getCrawlerHost());
					tracerLog.addTag("首次登录个人网银", "登录成功首次登录个人网银截图:" + pathS);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					return taskBank;
				}
				
				String errorfinfo = "";
				WebElement errorfinfomain = null;
				try {
					errorfinfomain = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.className("wrong_con"));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (errorfinfomain != null) {
					errorfinfo = errorfinfomain.getText();
				}else{
					try {
						String tipLogon = webDriver.findElement(By.id("tipLogonId")).getText();
						if (StringUtils.isNoneBlank(tipLogon)) {
							errorfinfo = errorfinfo+","+tipLogon;
						} 
						String tipPassword = webDriver.findElement(By.id("tipPasswordId")).getText();
						if (StringUtils.isNoneBlank(tipPassword)) {
							errorfinfo = errorfinfo+","+tipPassword;
						} 
						String tipCheck = webDriver.findElement(By.id("tipCheckCode")).getText();
						if (StringUtils.isNoneBlank(tipCheck)) {
							errorfinfo = errorfinfo+","+tipCheck;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String path = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				if (errorfinfo != null && !errorfinfo.equals("") && errorfinfo.length() > 0) {
					tracerLog.addTag("第"+i+"次"+"登录失败,错误信息" + errorfinfo, "截图:" + path);
					i++;
					if (errorfinfo.contains("验证码") && i < 4) {
						tracerLog.addTag("第"+i+"次"+"登录失败,错误信息" + errorfinfo, "次数:" + i + "---------截图:" + path);
						webDriver.quit();
						loginCombo(bankJsonBean, i);
					} else {
						if(errorfinfo.contains("验证码")){
							errorfinfo = "系统超时,请稍后再试";
						}
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						// 登录错误，释放资源
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("第"+i+"次"+"释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					}
				} else {
					tracerLog.addTag("第" + i + "次" + "登录遇到了未知的错误", "截图:" + path);
					errorfinfo = "登录超时,请重新登录!";
					tracerLog.addTag("第" + i + "次" + "登录失败,错误信息", errorfinfo);
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorfinfo,
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);
					// 登录错误，释放资源
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("第" + i + "次" + "释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), "");
			tracerLog.addTag("第"+i+"次"+"登录遇到了未知报错", "MSG:" + e.getMessage());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("第"+i+"次"+"释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
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

	/**
	 * @Des 获取当前时间前后年，月份或天数的时间
	 * @param fmt
	 * @param monthCount
	 * @param dateCount
	 * @return
	 * @throws Exception
	 */
	public static String getDateBefore(String fmt, int yearCount, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, yearCount);
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
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
		tracerLog.addTag("PsbcChinaService.getPage---url:", url + "statusCode:" + statusCode + "---taskId:" + taskid);
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
			PsbcChinaDebitCardHtml psbcChinaDebitCardHtml = new PsbcChinaDebitCardHtml(taskid, type, pageCount, url,
					html);
			psbcChinaDebitCardHtmlRepository.save(psbcChinaDebitCardHtml);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("PsbcChinaService.saveHtml---ERROR:", e.toString());
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
			loginCombo(bankJsonBean,1);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("PsbcChinaService.login---ERROR:", e.toString());
		}
		return null;
	}

}
