package app.service;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaCreditTransFlow;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaCreditTransInfo;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaCreditUserInfo;
import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaHtml;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaCreditTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaCreditTransInfoRepository;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaCreditUserInfoRepository;
import com.microservice.dao.repository.crawler.bank.abcchina.AbcChinaHtmlRepository;
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
public class ServiceABCChinaCreditAop implements ICrawlerLogin, ISms {
	// @Value("${spring.application.name}")
	// String appName;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private AbcChinaCreditUserInfoRepository abcChinaCreditUserInfoRepository;
	@Autowired
	private AbcChinaCreditTransFlowRepository abcChinaCreditTransFlowRepository;
	@Autowired
	private AbcChinaCreditTransInfoRepository abcChinaCreditTransInfoRepository;
	@Autowired
	private AbcChinaHtmlRepository abcChinaHtmlRepository;
	@Autowired
	private TracerLog tracerLog;

	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	@Autowired
	private AgentService agentService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static String LoginPage = "https://perbank.abchina.com/EbankSite/startup.do";

	WebClient webClient = WebCrawler.getInstance().getWebClient();
	WebDriver driver;
	private static Robot robot;

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		try {
			tracerLog.addTag("农业银行（信用卡）登陆业务进行中...", bankJsonBean.getTaskid());

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
			String loginName = bankJsonBean.getLoginName().trim();

			username.sendKeys(loginName);

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

			String pageSource = driver.getPageSource();

			if (pageSource.contains("密码错误") || pageSource.contains("密码输入长度不足") || pageSource.contains("密码内容不能为空")) {
				System.out.println("密码错误!");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("密码错误!", "截图:" + path2);
				System.out.println("密码错误!" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("密码错误!！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("密码已锁定")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "密码已锁定！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("密码已锁定！", "截图:" + path2);
				System.out.println("密码已锁定！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("密码已锁定！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("客户尚未注册用户名")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "客户尚未注册用户名！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("客户尚未注册用户名！", "截图:" + path2);
				System.out.println("客户尚未注册用户名！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("客户尚未注册用户名！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("您的密码过于简单,请您重新设置")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "您的密码过于简单,请您重新设置！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("您的密码过于简单,请您重新设置！", "截图:" + path2);
				System.out.println("您的密码过于简单,请您重新设置！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("您的密码过于简单,请您重新设置！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("帐户未注册渠道服务或已注销")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "帐户未注册渠道服务或已注销！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("帐户未注册渠道服务或已注销！", "截图:" + path2);
				System.out.println("帐户未注册渠道服务或已注销！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("帐户未注册渠道服务或已注销！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("手机号不存在")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "手机号不存在！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("手机号不存在！", "截图:" + path2);
				System.out.println("手机号不存在！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("手机号不存在！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("该用户名不存在")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), "该用户名不存在！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("该用户名不存在！", "截图:" + path2);
				System.out.println("该用户名不存在！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("该用户名不存在！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else if (pageSource.contains("您输入的图形验证码不正确")) {
				System.out.println("您输入的图形验证码不正确！");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), "登录失败！请您稍后再试！",
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("验证码错误！", "截图:" + path2);
				System.out.println("验证码错误！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("验证码错误！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
			} else if (pageSource.contains("id=\"imgError\"")) {
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

					TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("验证码错误！释放服务！", taskBank2.getCrawlerHost());
					agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
				} else {
					System.out.println("异常错误！");
					taskBankStatusService.changeStatus("LOGIN",
							"ERROR", "登录失败！请您稍后再试！",
							102, true, bankJsonBean.getTaskid());

					// 截图
					String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					tracerLog.addTag("异常错误！", "截图:" + path2);
					System.out.println("异常错误！" + path2);

					TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("异常错误！释放服务！", taskBank2.getCrawlerHost());
					agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
				}
			} else if (pageSource.contains("短信验证")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), true, bankJsonBean.getTaskid());
			} else if (pageSource.contains("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！")) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(),
						"尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！",
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！", "截图:" + path2);
				System.out.println("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！释放服务！",
						taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
			} else {
				if (pageSource.contains("<title>中国农业银行个人网银首页</title>")) {
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

					TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("异常错误！释放服务！", taskBank2.getCrawlerHost());
					agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
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
			System.out.println("点击确定结束！验证码发送成功！调用验证验证码接口.....");

			String phone = "";

			WebElement findElement = driver.findElement(By.id("securityPhone"));
			phone = findElement.getAttribute("value").trim();
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
			System.out.println("农业银行（信用卡）短信验证码发送的过程异常...");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("农业银行（信用卡）短信验证码发送的过程异常...", taskBank.getCrawlerHost());
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

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("短信验证码验证失败！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			} else {
				if (base.contains("<title>中国农业银行个人网银首页</title>")) {
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

					TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("尊敬的客户，您在我行留存的身份证件即将超过有效期 ，请尽快携带有效证件到我行网点更新信息。感谢您的理解与支持！释放服务！",
							taskBank2.getCrawlerHost());
					agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
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

					TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("短信验证码验证失败！释放服务！", taskBank2.getCrawlerHost());
					agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);
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
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
						"perbank.abchina.com", cookie.getName(), cookie.getValue()));
			}
			/////////////////////////////////////////// 基本信息///////////////////////////////////////////////
			String loginurl = "https://perbank.abchina.com/EbankSite/MyIntegralQryInitAct.do";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin = webClient.getPage(webRequestlogin);

			String contentAsString = pagelogin.getWebResponse().getContentAsString();

			AbcChinaHtml abcChinaHtml = new AbcChinaHtml();
			abcChinaHtml.setTaskid(bankJsonBean.getTaskid());
			abcChinaHtml.setHtml(contentAsString);
			abcChinaHtml.setPagenumber(1);
			abcChinaHtml.setType("信用卡-----基本信息----积分");
			abcChinaHtml.setUrl(loginurl);

			abcChinaHtmlRepository.save(abcChinaHtml);

			// 积分
			String integral;
			if (contentAsString.contains("客户积分")) {

				String[] split = contentAsString.split("客户积分：");
				String[] split2 = split[1].split("</div>");
				integral = split2[0].trim();
				System.out.println("客户积分：" + integral);
			} else {
				integral = "";
			}

			String loginurl2 = "https://perbank.abchina.com/EbankSite/index.do";
			WebRequest webRequestlogin2;
			webRequestlogin2 = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin2 = webClient.getPage(webRequestlogin2);

			String contentAsString2 = pagelogin2.getWebResponse().getContentAsString();

			AbcChinaHtml abcChinaHtml2 = new AbcChinaHtml();
			abcChinaHtml2.setTaskid(bankJsonBean.getTaskid());
			abcChinaHtml2.setHtml(contentAsString2);
			abcChinaHtml2.setPagenumber(1);
			abcChinaHtml2.setType("信用卡-----基本信息---姓名和用户类别");
			abcChinaHtml2.setUrl(loginurl2);
			abcChinaHtmlRepository.save(abcChinaHtml2);

			// 银行卡号
			String card_no = null;
			if (contentAsString2.contains("cardInfo =")) {
				String[] split = contentAsString2.split("cardInfo =");
				String[] split2 = split[1].split("'");
				String arraybase = split2[1].trim();
				JSONArray jsonarray = JSONArray.fromObject(arraybase);
				for (int i = 0; i < jsonarray.size(); i++) {
					String object = jsonarray.get(i).toString().trim();
					JSONObject jsonobject = JSONObject.fromObject(object);
					String card = jsonobject.getString("card_no").trim();
					if (!"".equals(card)) {
						card_no = card;
					}
				}
				System.out.println("银行卡号：" + card_no);
			}
			// 姓名
			String name;
			if (contentAsString2.contains("custName =")) {
				String[] split = contentAsString2.split("custName =");
				String[] split2 = split[1].split(";");
				name = split2[0].trim();
				System.out.println("姓名：" + name);
			} else {
				name = "";
			}
			// 用户类别
			String userType;
			if (contentAsString2.contains("vipCustLevelName =")) {
				String[] split3 = contentAsString2.split("vipCustLevelName =");
				String[] split22 = split3[1].split(";");
				userType = split22[0].trim();
				System.out.println("用户类别：" + userType);
			} else {
				userType = "";
			}
			AbcChinaCreditUserInfo abcChinaCreditUserInfo = new AbcChinaCreditUserInfo();
			abcChinaCreditUserInfo.setTaskid(bankJsonBean.getTaskid());
			abcChinaCreditUserInfo.setIntegral(integral);
			abcChinaCreditUserInfo.setName(name);
			abcChinaCreditUserInfo.setUserType(userType);
			abcChinaCreditUserInfo.setCardNumber(card_no);
			abcChinaCreditUserInfoRepository.save(abcChinaCreditUserInfo);

			taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());

			/////////////////////////////////////////// 账单信息///////////////////////////////////////////////
			// 从当前的月算起往前12个月
			for (int i = 0; i < 12; i++) {
				SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH, -i);
				String beforeMonth = f.format(c.getTime()) + "07";
				System.out.println(beforeMonth);

				String loginurl4 = "https://perbank.abchina.com/EbankSite/ccBillQry2Act.do?ccCardBillQry=" + card_no
						+ "&billDate_reqry=" + beforeMonth + "&ccBillQryForm_bk=CcBack2";
				WebRequest webRequestlogin4;
				webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.GET);
				Thread.sleep(5000);
				Page pagelogin4 = webClient.getPage(webRequestlogin4);

				String contentAsString4 = pagelogin4.getWebResponse().getContentAsString();

				AbcChinaHtml abcChinaHtml4 = new AbcChinaHtml();
				abcChinaHtml4.setTaskid(bankJsonBean.getTaskid());
				abcChinaHtml4.setHtml(contentAsString4);
				abcChinaHtml4.setPagenumber(1);
				abcChinaHtml4.setType("信用卡-----账户交易基本信息");
				abcChinaHtml4.setUrl(loginurl4);

				abcChinaHtmlRepository.save(abcChinaHtml4);
				// 集合1
				String[] split = contentAsString4.split("billInfoList =");
				String[] split2 = split[1].split(";");
				String billInfoList = split2[0].trim();
				System.out.println(billInfoList);
				// 集合2
				String[] split11 = contentAsString4.split("loanDetailList =");
				String[] split21 = split11[1].split(";");
				String loanDetailList = split21[0].trim();
				System.out.println(loanDetailList);
				// 集合3
				String[] split111 = contentAsString4.split("loanDetailInfoList =");
				String[] split211 = split111[1].split(";");
				String loanDetailInfoList = split211[0].trim();
				System.out.println(loanDetailInfoList);

				if ("[]".equals(billInfoList) && "[]".equals(loanDetailList) && "[]".equals(loanDetailInfoList)) {
					System.out.println("无当月账单信息！");
				} else {
					System.out.println("有当月账单信息！开始解析！");
					// 信用卡交易基本信息
					JSONArray billInfoListArray = JSONArray.fromObject(billInfoList);
					for (int j = 0; j < billInfoListArray.size(); j++) {
						String billInfoListJson = billInfoListArray.get(j).toString();
						JSONObject json = JSONObject.fromObject(billInfoListJson);
						// 账户币种
						String codCcyCn = json.getString("codCcyCn").trim();
						// 信用额度
						String amtCrlm = json.getString("amtCrlm").trim();
						// 上期余额(欠款为-)
						String balBen = json.getString("balBen").trim();
						// 本期新增应还款额
						String amtDbNew = json.getString("amtDbNew").trim();

						AbcChinaCreditTransInfo abcChinaCreditTransInfo = new AbcChinaCreditTransInfo();
						abcChinaCreditTransInfo.setTaskid(bankJsonBean.getTaskid());
						abcChinaCreditTransInfo.setCodCcyCn(codCcyCn);
						abcChinaCreditTransInfo.setAmtCrlm(amtCrlm);
						abcChinaCreditTransInfo.setBalBen(balBen);
						abcChinaCreditTransInfo.setAmtDbNew(amtDbNew);
						abcChinaCreditTransInfoRepository.save(abcChinaCreditTransInfo);

					}

					// 信用卡交易流水
					String loginurl6 = "https://perbank.abchina.com//EbankSite/ccBillQry1107Act.do?ccCardBillQry="
							+ card_no + "&billDate=" + beforeMonth;
					WebRequest webRequestlogin6;
					webRequestlogin6 = new WebRequest(new URL(loginurl6), HttpMethod.GET);
					Thread.sleep(5000);
					Page pagelogin6 = webClient.getPage(webRequestlogin6);

					String contentAsString6 = pagelogin6.getWebResponse().getContentAsString();
					System.out.println(contentAsString6);
					AbcChinaHtml abcChinaHtml6 = new AbcChinaHtml();
					abcChinaHtml6.setTaskid(bankJsonBean.getTaskid());
					abcChinaHtml6.setHtml(contentAsString6);
					abcChinaHtml6.setPagenumber(1);
					abcChinaHtml6.setType("信用卡-----账户交易流水信息");
					abcChinaHtml6.setUrl(loginurl6);
					abcChinaHtmlRepository.save(abcChinaHtml6);

					JSONArray contentAsString6Array = JSONArray.fromObject(contentAsString6);
					for (int j = 0; j < contentAsString6Array.size(); j++) {
						String billInfoListJson = contentAsString6Array.get(j).toString();
						JSONObject json = JSONObject.fromObject(billInfoListJson);
						// 交易日期
						String trDate = json.getString("trDate").trim();
						// 入账日期
						String postDate = json.getString("postDate").trim();
						// 交易摘要
						String trTxt = json.getString("trTxt").trim();
						// 交易地点
						String trAddress = json.getString("trAddress").trim();
						// 入账金额(元)
						String postAmt = json.getString("postAmt").trim();
						// 入账币种
						String postCod;
						postCod = json.getString("postCod").trim();
						if ("01".equals(postCod)) {
							postCod = "人民币CNY";
						}
						if ("14".equals(postCod)) {
							postCod = "美元USD";
						}
						if ("38".equals(postCod)) {
							postCod = "欧元EUR";
						}
						if ("29".equals(postCod)) {
							postCod = "澳大利亚元AUD";
						}
						if ("27".equals(postCod)) {
							postCod = "日元JPY";
						}
						if ("12".equals(postCod)) {
							postCod = "英镑GBP";
						}
						AbcChinaCreditTransFlow abcChinaCreditTransFlow = new AbcChinaCreditTransFlow();
						abcChinaCreditTransFlow.setTaskid(bankJsonBean.getTaskid());
						abcChinaCreditTransFlow.setTrDate(trDate);
						abcChinaCreditTransFlow.setPostDate(postDate);
						abcChinaCreditTransFlow.setTrTxt(trTxt);
						abcChinaCreditTransFlow.setTrAddress(trAddress);
						abcChinaCreditTransFlow.setPostAmt(postAmt);
						abcChinaCreditTransFlow.setPostCod(postCod);
						abcChinaCreditTransFlowRepository.save(abcChinaCreditTransFlow);
					}

					taskBankStatusService.updateTaskBankTransflow(200,
							BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				}
			}
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("农业银行（信用卡）爬取业务完成！释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskBank;
	}
	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
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
}
