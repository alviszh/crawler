package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.pabchina.PabChinaAccountType;
import com.microservice.dao.entity.crawler.bank.pabchina.PabChinaHtml;
import com.microservice.dao.entity.crawler.bank.pabchina.PabChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.pabchina.PabChinaUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.PabChinaAccountTypeRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.PabChinaHtmlRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.PabChinaTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.PabChinaUserInfoRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.pabchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.pabchina" })
public class ServicePABChinaAop implements ICrawlerLogin {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private PabChinaUserInfoRepository pabChinaUserInfoRepository;
	@Autowired
	private PabChinaTransFlowRepository pabChinaTransFlowRepository;
	@Autowired
	private PabChinaHtmlRepository pabChinaHtmlRepository;
	@Autowired
	private PabChinaAccountTypeRepository pabChinaAccountTypeRepository;
	@Autowired
	private TracerLog tracerLog;
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	// @Value("${spring.application.name}")
	// String appName;

	private static String LoginPage = "https://bank.pingan.com.cn/ibp/bank/index.html#home/home/index";
	WebDriver driver;
	WebClient webClient = WebCrawler.getInstance().getWebClient();
	@Autowired
	private AgentService agentService;

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		try {
			tracerLog.addTag("平安银行（储蓄卡）业务登录的调用...", bankJsonBean.getTaskid());

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

			System.setProperty("webdriver.ie.driver", driverPath);

			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = LoginPage;
			driver.get(baseUrl);

			Thread.sleep(30000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

			String windowHandle = driver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);

			// 切换登录方式
			driver.findElement(By.xpath("//*[@id='container']/div/div[1]/div/div[2]/div[1]/div[2]")).click();

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("userName"));
				}
			});

			Thread.sleep(2000L);

			username.click();
			username.clear();
			String loginname = bankJsonBean.getLoginName().trim();
			username.sendKeys(loginname);
			// username.sendKeys("menghongyu1015");

			// InputTab();
			driver.findElement(By.id("pwdObject1-btn-pan")).click();
			Thread.sleep(1000L);
			driver.findElement(By.id("pa_ui_keyboard_close")).click();
			// String password = "syh641012";
			// KeyPressEx(password, 200);

			String password = bankJsonBean.getPassword().trim();
			VK.KeyPress(password);

			driver.findElement(By.id("login_btn")).click();

			Thread.sleep(15000);

			String source = driver.getPageSource();

			System.out.println(source);
			if (source.contains("用户名、密码验证未通过") || source.contains("密码不能为空") || source.contains("密码长度不足")
					|| source.contains("用户名或密码错误")) {
				System.out.println("登陆失败！");
				taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "用户名或密码错误！",
						BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
				// 截图
				String path2 = null;
				try {
					path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				} catch (Exception e) {
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("登陆失败情况的截图异常", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				tracerLog.addTag("登陆失败！", "截图:" + path2);
				System.out.println("登陆失败！" + path2);
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("登陆失败情况的截图异常", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			} else {
				if (source.contains("上次登录时间")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);
				} else {
					taskBankStatusService.changeStatusbyWebdriverHandle("LOGIN", "ERROR", "登录失败，请您稍后重试！", 102, true,
							bankJsonBean.getTaskid(), windowHandle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskBank;
	}

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		try {
			// 姓名
			String name = null;
			String text = driver.findElement(By.id("clientName")).getText();
			if (null == text) {
				System.out.println("获取姓名失败！");
			} else {
				System.out.println("获取姓名成功！");
				String[] split = text.split("，");
				name = split[1].trim();
				System.out.println(name);
			}
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
						"bank.pingan.com.cn", cookie.getName(), cookie.getValue()));
			}

			String loginurl = "https://bank.pingan.com.cn/ibp/ibp4pc/work/account/queryBalance.do?channelType=d&responseDataType=JSON";

			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin = webClient.getPage(webRequestlogin);

			String contentAsString = pagelogin.getWebResponse().getContentAsString();

			PabChinaHtml pabChinaHtml = new PabChinaHtml();
			pabChinaHtml.setTaskid(bankJsonBean.getTaskid());
			pabChinaHtml.setHtml(contentAsString);
			pabChinaHtml.setPagenumber(1);
			pabChinaHtml.setType("储蓄卡-----基本信息");
			pabChinaHtml.setUrl(loginurl);

			pabChinaHtmlRepository.save(pabChinaHtml);

			JSONObject json = JSONObject.fromObject(contentAsString);
			String responseBody = json.getString("responseBody");
			JSONObject jsonresponseBody = JSONObject.fromObject(responseBody);
			String accList = jsonresponseBody.getString("accList");
			JSONArray jsonArray = JSONArray.fromObject(accList);
			//////////////////////////////////////// 基本信息///////////////////////////////////////////
			// 城市
			String position = null;
			// 卡号
			String accNum = null;
			// 余额
			String rbmBalance = null;
			// 卡类型
			String cardType = null;
			// 开户行
			String openAccBank = null;
			for (int i = 0; i < jsonArray.size(); i++) {

				String string = jsonArray.get(i).toString();
				JSONObject jsonstring = JSONObject.fromObject(string);

				position = jsonstring.getString("position").toString();
				System.out.println(position);
				accNum = jsonstring.getString("accNum").toString();
				System.out.println(accNum);
				rbmBalance = jsonstring.getString("rbmBalance").toString();
				System.out.println(rbmBalance);
				cardType = jsonstring.getString("cardType").toString();
				System.out.println(cardType);
				openAccBank = jsonstring.getString("openAccBank").toString();
				System.out.println(openAccBank);
			}

			PabChinaUserInfo pabChinaUserInfo = new PabChinaUserInfo();
			pabChinaUserInfo.setTaskid(bankJsonBean.getTaskid());
			pabChinaUserInfo.setName(name);
			pabChinaUserInfo.setPosition(position);
			pabChinaUserInfo.setAccNum(accNum);
			pabChinaUserInfo.setRbmBalance(rbmBalance);
			pabChinaUserInfo.setCardType(cardType);
			pabChinaUserInfo.setOpenAccBank(openAccBank);

			pabChinaUserInfoRepository.save(pabChinaUserInfo);

			taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());

			String accBalanceList = jsonresponseBody.getString("accBalanceList");
			JSONArray accBalanceListArray = JSONArray.fromObject(accBalanceList);
			//////////////////////////////////////// 账户类型///////////////////////////////////////////
			// 卡号
			String accNum2 = null;
			// 币种
			String currType = null;
			// 余额
			String balance = null;
			// 账户类型
			String accType = null;
			for (int i = 0; i < accBalanceListArray.size(); i++) {

				String string = accBalanceListArray.get(i).toString();
				JSONObject jsonstring = JSONObject.fromObject(string);

				accNum2 = jsonstring.getString("accNum").toString();
				System.out.println(accNum2);
				currType = jsonstring.getString("currType").toString();
				System.out.println(currType);
				balance = jsonstring.getString("balance").toString();
				System.out.println(balance);
				accType = jsonstring.getString("accType").toString();
				System.out.println(accType);

			}

			PabChinaAccountType pabChinaAccountType = new PabChinaAccountType();
			pabChinaAccountType.setTaskid(bankJsonBean.getTaskid());
			pabChinaAccountType.setAccNum(accNum2);
			pabChinaAccountType.setCurrType(currType);
			pabChinaAccountType.setBalance(balance);
			pabChinaAccountType.setAccType(accType);
			pabChinaAccountTypeRepository.save(pabChinaAccountType);

			//////////////////////////////////////// 交易记录///////////////////////////////////////////
			SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.MONTH, -11);
			String beforeMonth1 = f1.format(c1.getTime());
			System.out.println(beforeMonth1);

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 0);
			String beforeMonth = f.format(c.getTime());
			System.out.println(beforeMonth);

			String loginurl2 = "https://bank.pingan.com.cn/ibp/ibp4pc/work/pcqryTransList.do?channelType=d&responseDataType=JSON&startDate="
					+ beforeMonth1 + "&endDate=" + beforeMonth + "&bankType=0&currType=RMB&accNo=" + accNum
					+ "&pageIndex=1&accNumSelected=" + accNum + "&queryAccType=1";

			WebRequest webRequestlogin2;
			webRequestlogin2 = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin2 = webClient.getPage(webRequestlogin2);

			String contentAsString2 = pagelogin2.getWebResponse().getContentAsString();

			PabChinaHtml pabChinaHtml2 = new PabChinaHtml();
			pabChinaHtml2.setTaskid(bankJsonBean.getTaskid());
			pabChinaHtml2.setHtml(contentAsString2);
			pabChinaHtml2.setPagenumber(1);
			pabChinaHtml2.setType("储蓄卡-----流水信息");
			pabChinaHtml2.setUrl(loginurl2);

			pabChinaHtmlRepository.save(pabChinaHtml2);

			JSONObject json2 = JSONObject.fromObject(contentAsString2);
			String responseBody2 = json2.getString("responseBody");
			JSONObject jsonresponseBody2 = JSONObject.fromObject(responseBody2);
			String transList = jsonresponseBody2.getString("transList");
			JSONArray jsonArray2 = JSONArray.fromObject(transList);
			for (int i = 0; i < jsonArray2.size(); i++) {
				String string = jsonArray2.get(0).toString();
				JSONObject jsonstring = JSONObject.fromObject(string);
				// 交易时间
				String dealTime = jsonstring.getString("tranDate").toString();
				System.out.println(dealTime);
				// 交易方姓名
				String dealname = jsonstring.getString("targetAccName").toString();
				System.out.println(dealname);
				// 币种
				String currency = jsonstring.getString("currType").toString();
				System.out.println(currency);
				// 交易金额
				String dealMoney = jsonstring.getString("tranAmt").toString();
				System.out.println(dealMoney);
				// 账户余额
				String yue = jsonstring.getString("balance").toString();
				System.out.println(yue);
				// 交易类型
				String dealtype = jsonstring.getString("typeName").toString();
				System.out.println(dealtype);
				// 流水号
				String serial = jsonstring.getString("tranSysNo").toString();
				System.out.println(serial);
				// 用户备注
				String userRemark = jsonstring.getString("userRemark").toString();
				System.out.println(userRemark);

				PabChinaTransFlow pabChinaTransFlow = new PabChinaTransFlow();
				pabChinaTransFlow.setTaskid(bankJsonBean.getTaskid());
				pabChinaTransFlow.setDealTime(dealTime);
				pabChinaTransFlow.setDealname(dealname);
				pabChinaTransFlow.setCurrency(currency);
				pabChinaTransFlow.setDealMoney(dealMoney);
				pabChinaTransFlow.setYue(yue);
				pabChinaTransFlow.setDealtype(dealtype);
				pabChinaTransFlow.setSerial(serial);
				pabChinaTransFlow.setUserremark(userRemark);
				pabChinaTransFlowRepository.save(pabChinaTransFlow);
			}
			taskBankStatusService.updateTaskBankTransflow(200, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());

			// task 状态修改 finish = ture
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("所有业务完成！释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskBank;
	}

	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean
	 */
	public TaskBank quit(BankJsonBean bankJsonBean) {
		tracerLog.addTag("quit", bankJsonBean.toString());
		// 关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试 ,
		// description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true, bankJsonBean.getTaskid());
		// 调用公用释放资源方法
		if (taskBank != null) {
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else {
			tracerLog.addTag("quit taskBank is null", "");
		}
		return taskBank;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
