package app.service;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaAccountType;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaHtml;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaUserInfo;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaAccountTypeRepository;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaHtmlRepository;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaUserInfoRepository;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.abcchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.abcchina" })
public class ServiceABCChinaAop implements ICrawlerLogin, ISms {
	// @Value("${spring.application.name}")
	// String appName;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private AbcChinaUserInfoRepository abcChinaUserInfoRepository;
	@Autowired
	private AbcChinaTransFlowRepository abcChinaTransFlowRepository;
	@Autowired
	private AbcChinaHtmlRepository abcChinaHtmlRepository;
	@Autowired
	private AbcChinaAccountTypeRepository abcChinaAccountTypeRepository;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private AgentService agentService;
	@Value("${webdriver.ie.driver.path}")
	String driverPath;

	private static Robot robot;

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static String LoginPage = "https://perbank.abchina.com/EbankSite/startup.do";
	WebClient webClient = WebCrawler.getInstance().getWebClient();
	WebDriver driver;

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		try {
			tracerLog.addTag("农业银行（储蓄卡）登陆业务进行中...", bankJsonBean.getTaskid());

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

			System.setProperty("webdriver.ie.driver", driverPath);

			driver = new InternetExplorerDriver(ieCapabilities);

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = LoginPage;
			driver.get(baseUrl);
			driver.manage().window().maximize();

			Thread.sleep(2000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

			String windowHandle = driver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("username"));
				}
			});

			username.click();
			username.clear();

			// 获取客户端传过来的用户名
			String name = bankJsonBean.getLoginName().trim();

			username.sendKeys(name);
			VK.Tab();
			// 获取客户端传过来的密码
			String password = bankJsonBean.getPassword().trim();
			VK.KeyPress(password);

			String path = WebDriverUnit.saveImg(driver, By.id("vCode"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("code")).sendKeys(code);

			driver.findElement(By.id("logo")).click();

			Thread.sleep(10000);

			String pageSource2 = driver.getPageSource();

			if (pageSource2.contains("密码错误") || pageSource2.contains("密码输入长度不足") || pageSource2.contains("密码内容不能为空")) {
				System.out.println("密码错误!");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("密码错误!", "截图:" + path2);
				System.out.println("密码错误!" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("密码错误!！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("密码已锁定")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "密码已锁定！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("密码已锁定！", "截图:" + path2);
				System.out.println("密码已锁定！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("密码已锁定！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("客户尚未注册用户名")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "客户尚未注册用户名！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("客户尚未注册用户名！", "截图:" + path2);
				System.out.println("客户尚未注册用户名！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("客户尚未注册用户名！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("帐户未注册渠道服务或已注销")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "帐户未注册渠道服务或已注销！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("账号错误！", "截图:" + path2);
				System.out.println("账号错误！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("账号错误！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("您的密码过于简单,请您重新设置")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "您的密码过于简单,请您重新设置！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("您的密码过于简单,请您重新设置！", "截图:" + path2);
				System.out.println("您的密码过于简单,请您重新设置！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("您的密码过于简单,请您重新设置！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("手机号不存在")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "手机号不存在！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("手机号不存在！", "截图:" + path2);
				System.out.println("手机号不存在！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("手机号不存在！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("该用户名不存在")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "该用户名不存在！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("账号错误！", "截图:" + path2);
				System.out.println("账号错误！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("账号错误！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("您输入的图形验证码不正确")) {
				System.out.println("您输入的图形验证码不正确！");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), "登录失败！请您稍后再试！",
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("验证码错误！", "截图:" + path2);
				System.out.println("验证码错误！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("验证码错误！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else if (pageSource2.contains("id=\"imgError\"")) {
				// 判断是否是在登录界面，并且验证码错误
				WebElement findElement = driver.findElement(By.id("imgError"));
				String attribute = findElement.getAttribute("class");
				if (attribute.contains("wrong")) {
					System.out.println("您输入的图形验证码不正确！");
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), "登录失败！请您稍后再试！",
							BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(), true, bankJsonBean.getTaskid());
					// 截图
					String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("验证码错误！", "截图:" + path2);
					System.out.println("验证码错误！" + path2);

					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("验证码错误！释放服务！", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				} else {
					System.out.println("异常错误！");
					taskBankStatusService.changeStatus("LOGIN",
							"ERROR", "登录失败！请您稍后再试！",
							102, true, bankJsonBean.getTaskid());

					// 截图
					String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("异常错误！", "截图:" + path2);
					System.out.println("异常错误！" + path2);

					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("异常错误！释放服务！", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			} else if (pageSource2.contains("短信验证")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), true, bankJsonBean.getTaskid());
			} else if (pageSource2.contains("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！")) {
				taskBankStatusService.changeStatus("LOGIN",
						"ERROR",
						"尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！",
						102, true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！", "截图:" + path2);
				System.out.println("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！释放服务！",
						taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			} else {
				if (pageSource2.contains("<title>中国农业银行个人网银首页</title>")) {
					System.out.println("登录成功！");
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), true, bankJsonBean.getTaskid());
				} else {
					System.out.println("异常错误！");
					taskBankStatusService.changeStatus("LOGIN",
							"ERROR", "登录失败！请您稍后再试！",
							102, true, bankJsonBean.getTaskid());

					// 截图
					String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("异常错误！", "截图:" + path2);
					System.out.println("异常错误！" + path2);

					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("异常错误！释放服务！", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskBank;
	}

	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		driver.findElement(By.id("dynamicPswText_sendSms")).click();

		try {

			Thread.sleep(5000);

			robot = new Robot();// 创建Robot对象

			robot.keyPress(KeyEvent.VK_ENTER);
			System.out.println("点击确定结束！验证码发送成功！");

			String phone = "";
			WebElement findElement1 = driver.findElement(By.id("securityPhone"));
			phone = findElement1.getAttribute("value").trim();
			System.out.println("短信发送的目标手机号---" + phone);
			if (StringUtils.isNotBlank(phone)) {

				taskBankStatusService.changeStatus(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
						"短信验证码已发送，请注意查收，预留手机号" + "[" + phone + "]",
						BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), true, bankJsonBean.getTaskid());
			} else {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),
						BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
						BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),
						BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			}

		} catch (Exception e) {
			System.out.println("农业银行（储蓄卡）短信验证码发送的过程异常...");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("农业银行（储蓄卡）短信验证码发送的过程异常...", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}
		return taskBank;
	}

	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {

		TaskBank taskBank = null;
		try {
			String verification = bankJsonBean.getVerification();
			System.out.println("获取客户端传来的短信验证码---" + verification);
			driver.findElement(By.id("dynamicPswText")).sendKeys(verification.trim());
			// 验证完成
			driver.findElement(By.id("orangeBtn")).click();

			Thread.sleep(5000);

			String base = driver.getPageSource();

			if (base.contains("验证码输入有误")) {
				System.out.println("短信验证码验证失败！");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());

				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("短信验证码验证失败！", "截图:" + path2);
				System.out.println("短信验证码验证失败！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("短信验证码验证失败！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			} else {
				if (base.contains("<title>中国农业银行个人网银首页</title>")) {
					System.out.println("短信验证码验证成功！");
					taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(), true, bankJsonBean.getTaskid());
				} else if (base.contains("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！")) {
					taskBankStatusService.changeStatus("LOGIN",
							"ERROR",
							"尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！",
							102, true, bankJsonBean.getTaskid());
					// 截图
					String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！", "截图:" + path2);
					System.out.println("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！" + path2);

					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！释放服务！",
							taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				} else {
					System.out.println("短信验证码验证失败！");
					taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid());

					// 截图
					String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("短信验证码验证失败！", "截图:" + path2);
					System.out.println("短信验证码验证失败！" + path2);

					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("短信验证码验证失败！释放服务！", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
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
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

		for (org.openqa.selenium.Cookie cookie : cookies) {
			System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("perbank.abchina.com",
					cookie.getName(), cookie.getValue()));
		}
		try {
			////////////////////////////////////////////// 账户信息///////////////////////////////////////////////////////
			String loginurl77 = "https://perbank.abchina.com/EbankSite/AcctBalanceQryAct.do?multiAcctsString="
					+ bankJsonBean.getLoginName().trim();
			WebRequest webRequestlogin77;
			webRequestlogin77 = new WebRequest(new URL(loginurl77), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin77 = webClient.getPage(webRequestlogin77);
			String contentAsString77 = pagelogin77.getWebResponse().getContentAsString();
			JSONArray array77 = JSONArray.fromObject(contentAsString77);
			int size = array77.size();
			String acctNo = null;
			String acctType7 = null;
			if (size == 1) {
				String json = array77.get(0).toString();
				JSONObject object = JSONObject.fromObject(json);
				// 6228480010622143419
				acctNo = object.getString("acctNo");
				System.out.println(acctNo);
				// 401
				acctType7 = object.getString("acctType");
				System.out.println(acctType7);

			}
			String loginurl00 = "https://perbank.abchina.com/EbankSite/SubAcctCurrQueryAct.do?AcctId=" + acctNo
					+ "&AcctType=" + acctType7 + "&AcctCurrCode=156&AcctOofFlag=0";
			WebRequest webRequestlogin00;
			webRequestlogin00 = new WebRequest(new URL(loginurl00), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin00 = webClient.getPage(webRequestlogin00);
			String contentAsString00 = pagelogin00.getWebResponse().getContentAsString();

			System.out.println("储蓄卡-----账户类型信息（定期、活期）---" + contentAsString00);

			AbcChinaHtml abcChinaHtml44 = new AbcChinaHtml();
			abcChinaHtml44.setTaskid(bankJsonBean.getTaskid());
			abcChinaHtml44.setHtml(contentAsString00);
			abcChinaHtml44.setPagenumber(1);
			abcChinaHtml44.setType("储蓄卡-----账户类型信息（定期、活期）");
			abcChinaHtml44.setUrl(loginurl00);
			abcChinaHtmlRepository.save(abcChinaHtml44);

			JSONObject object = JSONObject.fromObject(contentAsString00);
			String rsCode = object.getString("rsCode");
			if ("0000".equals(rsCode)) {
				System.out.println("获取账户类型数据成功！");
				String table = object.getString("table");
				JSONArray tablearray = JSONArray.fromObject(table);
				for (int i = 0; i < tablearray.size(); i++) {
					String tableobject = tablearray.get(i).toString();
					JSONObject tableobjectjson = JSONObject.fromObject(tableobject);
					// 账户种类
					String accounttype = tableobjectjson.getString("账户种类");
					System.out.println(accounttype);
					// 币种
					String currency = tableobjectjson.getString("币种");
					System.out.println(currency);
					// 钞汇标志
					String speculate = tableobjectjson.getString("钞汇标志");
					System.out.println(speculate);
					// 开户日期
					String opendate = tableobjectjson.getString("开户日期");
					System.out.println(opendate);
					// 存期
					String deposit = tableobjectjson.getString("存期");
					System.out.println(deposit);
					// 可用余额
					String usableyue = tableobjectjson.getString("可用余额");
					System.out.println(usableyue);
					// 余额
					String yue = tableobjectjson.getString("余额");
					System.out.println(yue);
					// 账户状态
					String accountstatus = tableobjectjson.getString("账户状态");
					System.out.println(accountstatus);
					// 账户状态含义
					String accountstatusmeaning = tableobjectjson.getString("账户状态含义");
					System.out.println(accountstatusmeaning);
					// 年利率
					String annual = tableobjectjson.getString("年利率");
					System.out.println(annual);
					// 到期日期
					String datedue = tableobjectjson.getString("到期日期");
					System.out.println(datedue);
					// 存期处理
					String depositdispose = tableobjectjson.getString("存期处理");
					System.out.println(depositdispose);
					// 账户类型名
					String accounttypename = tableobjectjson.getString("账户类型名");
					System.out.println(accounttypename);
					// 币种名
					String currencyname = tableobjectjson.getString("币种名");
					System.out.println(currencyname);
					// 钞汇标志名
					String speculatename = tableobjectjson.getString("钞汇标志名");
					System.out.println(speculatename);
					AbcChinaAccountType abcChinaAccountType = new AbcChinaAccountType();
					abcChinaAccountType.setTaskid(bankJsonBean.getTaskid());
					abcChinaAccountType.setAccounttype(accounttype);
					abcChinaAccountType.setCurrency(currency);
					abcChinaAccountType.setSpeculate(speculate);
					abcChinaAccountType.setOpendate(opendate);
					abcChinaAccountType.setDeposit(deposit);
					abcChinaAccountType.setUsableyue(usableyue);
					abcChinaAccountType.setYue(yue);
					abcChinaAccountType.setAccountstatus(accountstatus);
					abcChinaAccountType.setAccountstatusmeaning(accountstatusmeaning);
					abcChinaAccountType.setAnnual(annual);
					abcChinaAccountType.setDatedue(datedue);
					abcChinaAccountType.setDepositdispose(depositdispose);
					abcChinaAccountType.setAccounttypename(accounttypename);
					abcChinaAccountType.setCurrencyname(currencyname);
					abcChinaAccountType.setSpeculatename(speculatename);

					abcChinaAccountTypeRepository.save(abcChinaAccountType);

				}
			} else {
				System.out.println("获取账户类型数据失败！");
			}
		} catch (Exception e) {
			System.out.println("储蓄卡-----解析账户类型数据失败！");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("储蓄卡-----解析账户类型数据失败...", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}

		///////////////////////////////////////////////// 流水信息////////////////////////////////////////////////////
		try {
			String loginurl0 = "https://perbank.abchina.com/EbankSite/AccountTradeDetailQueryInitAct.do?acctId="
					+ bankJsonBean.getLoginName().trim();
			WebRequest webRequestlogin0;
			webRequestlogin0 = new WebRequest(new URL(loginurl0), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin0 = webClient.getPage(webRequestlogin0);
			String contentAsString0 = pagelogin0.getWebResponse().getContentAsString();

			// acctName的参数("acctName", "万锦峰")
			String[] split11 = contentAsString0.split("optionsText =");
			String[] split211 = split11[1].split("\"");
			String parameter11 = split211[1].replaceAll("\\|", "\"");
			String[] split311 = parameter11.split("\"");
			String acctName = split311[4].trim();
			System.out.println(acctName);

			// busCode的参数("busCode", "200002")
			String[] split111 = contentAsString0.split("busCode=");
			String[] split4 = split111[1].split("\"");
			String busCode = split4[2].trim();
			System.out.println(busCode);

			// 大部分参数
			String[] split5 = contentAsString0.split("optionsVal = ");
			String[] split2 = split5[1].split("\"");
			String parameter = split2[1].replaceAll("\\|", "\"");
			String[] split3 = parameter.split("\"");

			// 6228480010622143419
			String acctId = split3[0].trim();
			System.out.println(acctId);
			// 401
			String acctType = split3[1].trim();
			System.out.println(acctType);
			// 0
			String oofeFlg = split3[2].trim();
			System.out.println(oofeFlg);
			// 156
			String acctCurCode = split3[3].trim();
			System.out.println(acctCurCode);
			// 30949
			String acctOpenBankId = split3[4].trim();
			System.out.println(acctOpenBankId);
			// 11
			String provCode = split3[5].trim();
			System.out.println(provCode);
			// 获取当前的时间
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String format = df.format(new Date());
			System.out.println(format);

			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -3);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);

			String loginurl = "https://perbank.abchina.com/EbankSite/AccountTradeDetailQueryAct.do";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters().add(new NameValuePair("trnStartDt", beforeMonth));
			webRequestlogin.getRequestParameters().add(new NameValuePair("trnEndDt", format));
			webRequestlogin.getRequestParameters().add(new NameValuePair("acctId", acctId));
			webRequestlogin.getRequestParameters().add(new NameValuePair("acctType", acctType));
			webRequestlogin.getRequestParameters().add(new NameValuePair("acctName", acctName));
			webRequestlogin.getRequestParameters().add(new NameValuePair("acctOpenBankId", acctOpenBankId));
			webRequestlogin.getRequestParameters().add(new NameValuePair("provCode", provCode));
			webRequestlogin.getRequestParameters().add(new NameValuePair("busCode", busCode));
			webRequestlogin.getRequestParameters().add(new NameValuePair("oofeFlg", oofeFlg));
			webRequestlogin.getRequestParameters().add(new NameValuePair("acctCurCode", acctCurCode));
			webRequestlogin.getRequestParameters().add(new NameValuePair("nextPageKey", ""));
			Thread.sleep(5000);
			Page pagelogin = webClient.getPage(webRequestlogin);

			String contentAsString = pagelogin.getWebResponse().getContentAsString();

			AbcChinaHtml abcChinaHtml = new AbcChinaHtml();
			abcChinaHtml.setTaskid(bankJsonBean.getTaskid());
			abcChinaHtml.setHtml(contentAsString);
			abcChinaHtml.setPagenumber(1);
			abcChinaHtml.setType("储蓄卡-----流水信息");
			abcChinaHtml.setUrl(loginurl);
			abcChinaHtmlRepository.save(abcChinaHtml);

			if (contentAsString.contains("{\"table\":[],\"nextPageKey\":\"\"}")) {
				System.out.println("流水信息不存在！");
				taskBankStatusService.updateTaskBankTransflow(201,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
			} else {
				System.out.println("流水信息存在！开始解析！");

				// 交易时间
				String dealTime = null;
				// 交易金额
				String dealMoney = null;
				// 本次余额
				String thisYue = null;
				// 交易类型
				String dealType = null;
				// 交易摘要
				String dealDigest = null;

				JSONObject json = JSONObject.fromObject(contentAsString);
				String table = json.getString("table");
				JSONArray jsonArray = JSONArray.fromObject(table);
				for (int i = 0; i < jsonArray.size(); i++) {
					String array = jsonArray.get(i) + "";
					JSONArray array2 = JSONArray.fromObject(array);
					System.out.println(array2);
					String strarray = array2 + "";
					String[] split = strarray.split("\"");
					dealTime = split[1];
					dealMoney = split[5];
					thisYue = split[7];
					dealType = split[17];
					dealDigest = split[21];
					System.out.println(dealTime);
					System.out.println(dealMoney);
					System.out.println(thisYue);
					System.out.println(dealType);
					System.out.println(dealDigest);
					AbcChinaTransFlow abcChinaTransFlow = new AbcChinaTransFlow();
					abcChinaTransFlow.setTaskid(bankJsonBean.getTaskid().trim());
					abcChinaTransFlow.setAccount(acctId);
					abcChinaTransFlow.setDealTime(dealTime);
					abcChinaTransFlow.setDealMoney(dealMoney);
					abcChinaTransFlow.setThisYue(thisYue);
					abcChinaTransFlow.setDealType(dealType);
					abcChinaTransFlow.setDealDigest(dealDigest);
					abcChinaTransFlowRepository.save(abcChinaTransFlow);

					taskBankStatusService.updateTaskBankTransflow(200,
							BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				}
			}

		} catch (Exception e) {
			System.out.println("储蓄卡-----解析流水信息失败！");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("储蓄卡-----解析流水信息失败...", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}
		////////////////////////////////////// 基本信息///////////////////////////////////////////
		try {

			String loginurl001 = "https://perbank.abchina.com/EbankSite/CustomerPersonInfoMngInitAct.do";
			WebRequest webRequestlogin001;
			webRequestlogin001 = new WebRequest(new URL(loginurl001), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin001 = webClient.getPage(webRequestlogin001);
			String contentAsString001 = pagelogin001.getWebResponse().getContentAsString();

			AbcChinaHtml abcChinaHtml7 = new AbcChinaHtml();
			abcChinaHtml7.setTaskid(bankJsonBean.getTaskid());
			abcChinaHtml7.setHtml(contentAsString001);
			abcChinaHtml7.setPagenumber(1);
			abcChinaHtml7.setType("储蓄卡-----基本信息");
			abcChinaHtml7.setUrl(loginurl001);
			abcChinaHtmlRepository.save(abcChinaHtml7);
			// 客户号
			String khh = "";
			// 客户姓名
			String khxm = "";
			// 证件类型
			String zjlx = "";
			// 证件号
			String zjh = "";
			// 证件有效期起始日期
			String zjyxqqsrq = "";
			// 证件有效期结束日期
			String zjyxqjsrq = "";
			// 网银注册行
			String wyzch = "";
			// 出生日期
			String csrq = "";
			// 国籍
			String gj = "";
			// 移动电话
			String yddh = "";
			// Email
			String email = "";
			// 单位名称
			String dwmc = "";
			// 单位电话
			String dwdh = "";
			// 单位邮编
			String dwyb = "";
			// 家庭电话
			String jtdh = "";
			// 家庭邮编
			String jtyb = "";
			if (contentAsString001.contains("id=\"form1\"")) {
				System.out.println("获取基本信息数据成功！");
				Document doc = Jsoup.parse(contentAsString001);
				Element elementById = doc.getElementById("form1");
				Element elementsByClass = elementById.getElementsByClass("personal-table").get(0);
				Elements trs = elementsByClass.getElementsByTag("tr");
				try {
					Element tr0 = trs.get(0);
					Elements tds = tr0.getElementsByTag("td");
					// 客户号
					khh = tds.get(1).text().trim();
					System.out.println("客户号-----" + khh);
					// 客户姓名
					khxm = tds.get(3).text().trim();
					System.out.println("客户姓名-----" + khxm);
				} catch (Exception e) {
					System.out.println("客户号   和   客户姓名获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("客户号   和   客户姓名获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(1);
					Elements tds = tr0.getElementsByTag("td");
					// 证件类型
					zjlx = tds.get(1).text().trim();
					System.out.println("证件类型-----" + zjlx);
					// 证件号
					zjh = tds.get(3).text().trim();
					System.out.println("证件号-----" + zjh);
				} catch (Exception e) {
					System.out.println("证件类型   和   证件号获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("证件类型   和   证件号获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(2);
					Elements tds = tr0.getElementsByTag("td");
					// 证件有效期起始日期
					zjyxqqsrq = tds.get(1).text().trim();
					System.out.println("证件有效期起始日期-----" + zjyxqqsrq);
					// 证件有效期结束日期
					zjyxqjsrq = tds.get(3).text().trim();
					System.out.println("证件有效期结束日期-----" + zjyxqjsrq);
				} catch (Exception e) {
					System.out.println("证件有效期起始日期   和   证件有效期结束日期获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("证件有效期起始日期   和   证件有效期结束日期获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(3);
					Elements tds = tr0.getElementsByTag("td");
					// 网银注册行
					wyzch = tds.get(1).text().trim();
					System.out.println("网银注册行 -----" + wyzch);
					// 出生日期
					csrq = tds.get(3).text().trim();
					System.out.println("出生日期-----" + csrq);
				} catch (Exception e) {
					System.out.println("网银注册行   和   出生日期获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("网银注册行   和   出生日期获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(5);
					Elements tds = tr0.getElementsByTag("td");
					// 国籍
					gj = tds.get(1).text().trim();
					System.out.println("国籍 -----" + gj);
				} catch (Exception e) {
					System.out.println("国籍获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("国籍获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(7);
					Elements tds = tr0.getElementsByTag("td");
					// 移动电话
					yddh = tds.get(1).text().trim();
					System.out.println("移动电话 -----" + yddh);
					// Email
					email = tds.get(3).getElementById("Email").val();
					System.out.println("Email-----" + email);
				} catch (Exception e) {
					System.out.println("移动电话   和   email获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("移动电话   和   email获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(8);
					Elements tds = tr0.getElementsByTag("td");
					// 单位名称
					dwmc = tds.get(1).getElementById("workPlaceName").val();
					System.out.println("单位名称 -----" + dwmc);
				} catch (Exception e) {
					System.out.println("单位名称获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("单位名称获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(9);
					Elements tds = tr0.getElementsByTag("td");
					// 单位电话
					dwdh = tds.get(1).getElementById("OffIPhone").val() + "-"
							+ tds.get(1).getElementById("OffDPhone").val() + "-"
							+ tds.get(1).getElementById("OffPhone").val() + "-"
							+ tds.get(1).getElementById("OffPhoneEx").val();
					System.out.println("单位电话  -----" + dwdh);
					// 单位邮编
					dwyb = tds.get(3).getElementById("OffPostCode").val();
					System.out.println("单位邮编 -----" + dwyb);
				} catch (Exception e) {
					System.out.println("单位电话   和   单位邮编 获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("单位电话   和   单位邮编 获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				try {
					Element tr0 = trs.get(11);
					Elements tds = tr0.getElementsByTag("td");
					// 家庭电话
					jtdh = tds.get(1).getElementById("HomeIPhone").val() + "-"
							+ tds.get(1).getElementById("HomeDPhone").val() + "-"
							+ tds.get(1).getElementById("HomePhone").val() + "-"
							+ tds.get(1).getElementById("HomePhoneEx").val();
					System.out.println("家庭电话  -----" + jtdh);
					// 家庭邮编
					jtyb = tds.get(3).getElementById("HomePostCode").val();
					System.out.println("家庭邮编 -----" + jtyb);
				} catch (Exception e) {
					System.out.println("家庭电话   和   家庭邮编  获取失败！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("家庭电话   和   家庭邮编  获取失败...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}

				AbcChinaUserInfo abcChinaUserInfo = new AbcChinaUserInfo();
				abcChinaUserInfo.setTaskid(bankJsonBean.getTaskid().trim());
				abcChinaUserInfo.setKhh(khh);
				abcChinaUserInfo.setKhxm(khxm);
				abcChinaUserInfo.setZjlx(zjlx);
				abcChinaUserInfo.setZjh(zjh);
				abcChinaUserInfo.setZjyxqqsrq(zjyxqqsrq);
				abcChinaUserInfo.setZjyxqjsrq(zjyxqjsrq);
				abcChinaUserInfo.setWyzch(wyzch);
				abcChinaUserInfo.setCsrq(csrq);
				abcChinaUserInfo.setGj(gj);
				abcChinaUserInfo.setYddh(yddh);
				abcChinaUserInfo.setEmail(email);
				abcChinaUserInfo.setDwmc(dwmc);
				abcChinaUserInfo.setDwdh(dwdh);
				abcChinaUserInfo.setDwyb(dwyb);
				abcChinaUserInfo.setJtdh(jtdh);
				abcChinaUserInfo.setJtyb(jtyb);
				abcChinaUserInfoRepository.save(abcChinaUserInfo);

				taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
						bankJsonBean.getTaskid());
			} else {
				System.out.println("获取基本信息数据失败！");
			}
		} catch (Exception e) {
			System.out.println("储蓄卡-----解析基本信息数据失败！");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("储蓄卡-----解析基本信息数据失败...", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}

		taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());
		TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		// 释放instance ip ，quit webdriver
		tracerLog.addTag("农业银行（储蓄卡）爬取业务完成！释放服务！", taskBank2.getCrawlerHost());
		agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

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
