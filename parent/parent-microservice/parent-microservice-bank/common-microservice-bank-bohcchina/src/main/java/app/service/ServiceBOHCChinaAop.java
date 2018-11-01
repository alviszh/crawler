package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bohcchina.BohcChinaAccountType;
import com.microservice.dao.entity.crawler.bank.bohcchina.BohcChinaHtml;
import com.microservice.dao.entity.crawler.bank.bohcchina.BohcChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.bohcchina.BohcChinaUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.bohcchina.BohcChinaAccountTypeRepository;
import com.microservice.dao.repository.crawler.bank.bohcchina.BohcChinaHtmlRepository;
import com.microservice.dao.repository.crawler.bank.bohcchina.BohcChinaTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.bohcchina.BohcChinaUserInfoRepository;
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
		"com.microservice.dao.entity.crawler.bank.bohcchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.bohcchina" })
public class ServiceBOHCChinaAop implements ICrawlerLogin, ISms {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private BohcChinaUserInfoRepository bohcChinaUserInfoRepository;
	@Autowired
	private BohcChinaTransFlowRepository bohcChinaTransFlowRepository;
	@Autowired
	private BohcChinaAccountTypeRepository bohcChinaAccountTypeRepository;
	@Autowired
	private BohcChinaHtmlRepository bohcChinaHtmlRepository;
	@Autowired
	private TracerLog tracerLog;
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	// @Value("${spring.application.name}")
	// String appName;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static String LoginPage = "https://ebank.cbhb.com.cn/pWeb/static/login.html";
	WebDriver driver;
	WebClient webClient = WebCrawler.getInstance().getWebClient();
	@Autowired
	private AgentService agentService;

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);
		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(LoginPage);
		driver.manage().window().maximize();

		String windowHandle = driver.getWindowHandle();

		// 这里需要休息2秒，不然点击事件可能无法弹出登录框
		try {
			Thread.sleep(5000L);

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("LoginName"));
				}
			});
			username.click();
			username.clear();
			username.sendKeys(bankJsonBean.getLoginName().trim());
			// InputTab();

			String password = bankJsonBean.getPassword().trim();
			// KeyPressEx(password, 100);

			driver.findElement(By.id("loginpass")).sendKeys(Keys.ENTER);
			VK.KeyPress(password);

			String path = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("_tokenName")).sendKeys(code);

			driver.findElement(By.xpath("//input[@type='submit']")).click();

			Thread.sleep(10000);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String source = driver.getPageSource();
		// 问题返回值
		if (source.contains("用户名或密码错误") || source.contains("登录密码输入错误：输入长度不足") || source.contains("登录密码输入错误：输入内容不合规")
				|| source.contains("内容不能为空")) {
			System.out.println("登陆失败！用户名或密码错误!");
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getDescription(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);

			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("截图异常！");
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("截图异常...", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}

			tracerLog.addTag("登陆失败！用户名或密码错误!", "截图:" + path2);
			System.out.println("登陆失败！用户名或密码错误!" + path2);

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("登陆失败！用户名或密码错误!释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

		} else if (source.contains("密码连续输错已被锁定")) {
			System.out.println("登陆失败！密码连续输错已被锁定!");
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), "密码连续输错已被锁定!",
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);

			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("截图异常！");
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("截图异常...", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}

			tracerLog.addTag("密码连续输错已被锁定!", "截图:" + path2);
			System.out.println("密码连续输错已被锁定!" + path2);

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("密码连续输错已被锁定!释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

		} else if (source.contains("用户渠道已被锁定")) {
			System.out.println("登陆失败！用户渠道已被锁定!");
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), "用户渠道已被锁定!",
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);

			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("截图异常！");
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("截图异常...", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}

			tracerLog.addTag("用户渠道已被锁定!", "截图:" + path2);
			System.out.println("用户渠道已被锁定!" + path2);

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("用户渠道已被锁定!释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

		} else if (source.contains("验证码错误")) {
			System.out.println("登陆失败！图片验证码错误!");
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), "登录失败！请您稍后再试！",
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(), true, bankJsonBean.getTaskid());
			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("截图异常！");
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("截图异常...", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
			tracerLog.addTag("验证码错误！", "截图:" + path2);
			System.out.println("验证码错误！" + path2);

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("验证码错误！释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else {
			try {
				WebElement TrsPwdReset2PreloginPasswordDiv = driver
						.findElement(By.id("TrsPwdReset2PreloginPasswordDiv"));
				TrsPwdReset2PreloginPasswordDiv.click();
				System.out.println("很抱歉！您未设置交易密码！设置完之后再来操作！谢谢配合！");
				taskBankStatusService.changeStatusbyWebdriverHandle(
						BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getDescription(),
						BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getError_code(), true,
						bankJsonBean.getTaskid(), windowHandle);

				// 截图
				String path2 = null;
				try {
					path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				} catch (Exception e) {
					System.out.println("截图异常！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("截图异常...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				tracerLog.addTag("很抱歉！您未设置交易密码！设置完之后再来操作！谢谢配合！", "截图:" + path2);
				System.out.println("很抱歉！您未设置交易密码！设置完之后再来操作！谢谢配合！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("很抱歉！您未设置交易密码！设置完之后再来操作！谢谢配合！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			} catch (Exception e) {
				System.out.println("不需要统一交易密码！");
				e.printStackTrace();
			}
			try {
				WebElement Answer1 = driver.findElement(By.xpath("//input[@name='Answer1']"));
				Answer1.click();
				System.out.println("很抱歉！您未设置安全问题！设置完之后再来操作！谢谢配合！");
				taskBankStatusService.changeStatusbyWebdriverHandle(
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getDescription(),
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getError_code(), true,
						bankJsonBean.getTaskid(), windowHandle);

				// 截图
				String path2 = null;
				try {
					path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				} catch (Exception e) {
					System.out.println("截图异常！");
					taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					// 释放instance ip ，quit webdriver
					tracerLog.addTag("截图异常...", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					e.printStackTrace();
				}
				tracerLog.addTag("很抱歉！您未设置安全问题！设置完之后再来操作！谢谢配合！", "截图:" + path2);
				System.out.println("很抱歉！您未设置安全问题！设置完之后再来操作！谢谢配合！" + path2);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("很抱歉！您未设置安全问题！设置完之后再来操作！谢谢配合！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			} catch (Exception e) {
				System.out.println("不需要设置完全问题！");
				e.printStackTrace();
			}
			if (source.contains("验证安全问题")) {
				System.out.println("需要进行动态密码的验证！");
				taskBankStatusService.changeStatusbyWebdriverHandle(
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), true, bankJsonBean.getTaskid(),
						windowHandle);
			}
			if (source.contains("您上次登录的时间")) {
				System.out.println("不需要进行动态密码的验证，直接进入主界面！");
				taskBankStatusService.changeStatusbyWebdriverHandle(
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), true, bankJsonBean.getTaskid(),
						windowHandle);
			}
		}
		return taskBank;
	}

	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		TaskBank taskBank = new TaskBank();
		String anwter = null;
		String source = driver.getPageSource();
		String windowHandle = driver.getWindowHandle();
		Document doc = Jsoup.parse(source);
		Elements forms = doc.getElementsByAttributeValue("name", "AnswerVerifyForm");
		int size = forms.size();
		if (size == 1) {
			System.out.println("需要进行动态密码的验证！");
			Elements spans = forms.get(0).getElementsByClass("v-binding");
			// 问题验证的问题----第一个接口返回的值
			anwter = spans.get(1).text();
			System.out.println(anwter);

			taskBank.setQuestion(anwter);
			// 认证方式:两种
			List<WebElement> findElements2 = driver.findElements(By.xpath("//input[@name='SafetyPrdFlag']"));
			// get(0)：动态口令
			// get(1)：数字证书
			findElements2.get(2).click();
			System.out.println("点击完之后");
			// 准备发验证码
			WebElement findElements3 = driver.findElement(By.xpath("//input[@name='AnswerVerifyyzm']"));
			findElements3.click();
			System.out.println("第一步验证码已发送！请注意查收！");

			taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
					BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
					BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription(),
					BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);
		} else {
			taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
		}
		return taskBank;
	}

	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		String verification = bankJsonBean.getVerification().trim();
		String answer = bankJsonBean.getAnswer().trim();
		System.out.println("第一次输入的验证码-----" + verification);
		System.out.println("第一次输入的问题的答案-----" + answer);

		WebElement findElement = driver.findElement(By.xpath("//input[@title='答案']"));
		findElement.sendKeys(answer);

		driver.findElement(By.id("AnswerVerifyPassword")).sendKeys(verification);
		// 点击确定
		List<WebElement> center = driver.findElements(By.tagName("center"));
		center.get(3).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String source2 = driver.getPageSource();
		if (source2.contains("动态密码错误") || source2.contains("验证错误") || source2.contains("缺少交易认证方式")
				|| source2.contains("动态口令格式错误")) {
			System.out.println("第一步验证失败！动态密码或问题验证错误！");
			taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getPhase(),
					BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getPhasestatus(),
					BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getDescription(),
					BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getError_code(), true, bankJsonBean.getTaskid());

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("第一步验证失败！动态密码或问题验证错误！释放服务！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

		} else {
			if (source2.contains("您上次登录的时间")) {
				System.out.println("短信验证码验证成功！");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(), true, bankJsonBean.getTaskid());
			} else {
				System.out.println("短信验证码验证失败！");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getPhase(),
						BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getPhasestatus(),
						BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getDescription(),
						BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getError_code(), true, bankJsonBean.getTaskid());

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("第一步验证失败！动态密码或问题验证错误！释放服务！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			}
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
			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ebank.cbhb.com.cn",
					cookie.getName(), cookie.getValue()));
		}
		// 账户编号流水信息的参数
		String acNo = null;
		try {
			String loginurl = "https://ebank.cbhb.com.cn/pWeb/FP010199.do";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Thread.sleep(5000);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();

			BohcChinaHtml abcChinaHtml44 = new BohcChinaHtml();
			abcChinaHtml44.setTaskid(bankJsonBean.getTaskid());
			abcChinaHtml44.setHtml(contentAsString);
			abcChinaHtml44.setPagenumber(1);
			abcChinaHtml44.setType("账户类型信息（定期、活期）和用户基本信息");
			abcChinaHtml44.setUrl(loginurl);

			bohcChinaHtmlRepository.save(abcChinaHtml44);

			System.out.println("//////////////////////////////////// 爬取账户信息////////////////////////////////////////");
			JSONObject object = JSONObject.fromObject(contentAsString);
			String aLLAccList = object.getString("ALLAccList").toString().trim();
			JSONArray array = JSONArray.fromObject(aLLAccList);
			for (int i = 0; i < array.size(); i++) {
				String string = array.get(i).toString();
				JSONObject objectstring = JSONObject.fromObject(string);
				// 账号 6214530105265917
				String cardNo = objectstring.getString("CardNo").trim();
				System.out.println("账号----" + cardNo);
				// 账户类型 活期账户
				String prductCode_Name = "活期账户";
				System.out.println("账户类型----" + prductCode_Name);
				// 账户类型2 个人人民币结算存款
				String prductCode_Name1 = objectstring.getString("PrductCode_Name").trim();
				System.out.println("账户类型2----" + prductCode_Name1);
				// 凭证种类 借记卡
				String vouchertype;
				vouchertype = objectstring.getString("OpnVouFlg").trim();
				if ("001000".equals(vouchertype)) {
					vouchertype = "借记卡";
				}
				System.out.println("凭证种类----" + vouchertype);
				// 币种
				String currencytype = objectstring.getString("Ccy_Name").trim();
				System.out.println("币种----" + currencytype);
				// 可用余额
				String usableyue = objectstring.getString("Bal1").trim();
				System.out.println("可用余额----" + usableyue);
				// 账户状态
				String callStat;
				callStat = objectstring.getString("CallStat").trim();
				if ("0000000".equals(callStat)) {
					callStat = "正常";
				}
				System.out.println("账户状态----" + callStat);
				// 开户地
				String cityName = objectstring.getString("CityName").trim();
				System.out.println("开户地----" + cityName);
				// 开户时间
				String openDate = objectstring.getString("OpenDate").trim();
				System.out.println("开户时间----" + openDate);
				BohcChinaAccountType bohcChinaAccountType = new BohcChinaAccountType();
				bohcChinaAccountType.setTaskid(bankJsonBean.getTaskid());
				bohcChinaAccountType.setCardNo(cardNo);
				bohcChinaAccountType.setPrductCode_Name(prductCode_Name);
				bohcChinaAccountType.setPrductCode_Name1(prductCode_Name1);
				bohcChinaAccountType.setVouchertype(vouchertype);
				bohcChinaAccountType.setCurrencytype(currencytype);
				bohcChinaAccountType.setUsableyue(usableyue);
				bohcChinaAccountType.setCallStat(callStat);
				bohcChinaAccountType.setCityName(cityName);
				bohcChinaAccountType.setOpenDate(openDate);
				bohcChinaAccountTypeRepository.save(bohcChinaAccountType);

			}
			System.out.println("//////////////////////////////////// 爬取基本信息////////////////////////////////////////");
			String AcBalanceRes = object.getString("AcBalanceRes").toString().trim();
			JSONArray array2 = JSONArray.fromObject(AcBalanceRes);

			for (int i = 0; i < array2.size(); i++) {
				String string = array2.get(i).toString();
				JSONObject objectstring = JSONObject.fromObject(string);
				// 账户编号
				acNo = objectstring.getString("AcctNo").toString();
				System.out.println("账户编号----" + acNo);
				// 账户姓名
				String acname = objectstring.getString("CustName").toString();
				System.out.println("账户姓名----" + acname);
				// 币种
				String currName = objectstring.getString("Ccy_Name").toString();
				System.out.println("币种----" + currName);
				// 卡号
				String cardNo = objectstring.getString("CardNo").toString();
				System.out.println("卡号----" + cardNo);
				// 开户地
				String cityName = objectstring.getString("CityName").toString();
				System.out.println("开户地----" + cityName);
				// 开户时间
				String openDate = objectstring.getString("OpenDate").toString();
				System.out.println("开户时间----" + openDate);
				// 可用余额
				String usableyue = objectstring.getString("Bal1").toString();
				System.out.println("可用余额----" + usableyue);

				BohcChinaUserInfo bohcChinaUserInfo = new BohcChinaUserInfo();
				bohcChinaUserInfo.setTaskid(bankJsonBean.getTaskid());
				bohcChinaUserInfo.setAcNo(acNo);
				bohcChinaUserInfo.setAcname(acname);
				bohcChinaUserInfo.setCurrName(currName);
				bohcChinaUserInfo.setCardNo(cardNo);
				bohcChinaUserInfo.setCityName(cityName);
				bohcChinaUserInfo.setOpenDate(openDate);
				bohcChinaUserInfo.setUsableyue(usableyue);
				bohcChinaUserInfoRepository.save(bohcChinaUserInfo);
			}
			taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
		} catch (Exception e) {
			System.out.println("爬取账户信息和基本信息异常！");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("爬取账户信息和基本信息异常...", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}
		// 整理出一年的时间间隔一年分4个三月
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 字符串时间集合
		List<String> list = new ArrayList<String>();

		for (int k = 0; k < 12; k += 3) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -k);
			String beforeMonth2 = df.format(c.getTime());
			list.add(beforeMonth2);

			Calendar c1 = Calendar.getInstance();
			int j1 = k + 3;
			c1.add(Calendar.MONTH, -j1);
			String beforeMonth1 = df.format(c1.getTime());
			String specifiedDayBefore = getSpecifiedDayBefore(beforeMonth1);
			if (k == 9) {
				Calendar c11 = Calendar.getInstance();
				c11.add(Calendar.YEAR, -1);
				String beforeMonth21 = df.format(c11.getTime());
				String specifiedDayBefore2 = getSpecifiedDayAfter(beforeMonth21);
				list.add(specifiedDayBefore2);
			} else {
				list.add(specifiedDayBefore);
			}
		}

		System.out.println(list.toString());

		for (int k = 0; k < 4; k++) {
			String BeginDate = "";
			String EndDate = "";
			if (k == 0) {
				// 2018-03-01----------2018-06-01
				BeginDate = list.get(2).toString().trim();
				EndDate = list.get(0).toString().trim();
			}
			if (k == 1) {
				// 2017-12-01----------2018-02-28
				BeginDate = list.get(4).toString().trim();
				EndDate = list.get(1).toString().trim();
			}
			if (k == 2) {
				// 2017-09-01----------2017-11-30
				BeginDate = list.get(6).toString().trim();
				EndDate = list.get(3).toString().trim();
			}
			if (k == 3) {
				// 2017-08-31----------2017-06-02
				BeginDate = list.get(7).toString().trim();
				EndDate = list.get(5).toString().trim();
			}
			System.out.println(BeginDate + "---------------" + EndDate);
			try {
				System.out.println("//////////////////////////////////// 爬取流水信息:" + BeginDate + "" + "---" + ""
						+ EndDate + "////////////////////////////////////////");
				String loginurl1 = "https://ebank.cbhb.com.cn/pWeb/FP010301.do?AcNo=" + acNo + "&BeginDate=" + BeginDate
						+ "&EndDate=" + EndDate;
				WebRequest webRequestlogin1;
				webRequestlogin1 = new WebRequest(new URL(loginurl1), HttpMethod.GET);
				Thread.sleep(5000);
				Page pagelogin1 = webClient.getPage(webRequestlogin1);
				String contentAsString1 = pagelogin1.getWebResponse().getContentAsString();

				BohcChinaHtml abcChinaHtml = new BohcChinaHtml();
				abcChinaHtml.setTaskid(bankJsonBean.getTaskid());
				abcChinaHtml.setHtml(contentAsString1);
				abcChinaHtml.setPagenumber(1);
				abcChinaHtml.setType("流水信息");
				abcChinaHtml.setUrl(loginurl1);
				bohcChinaHtmlRepository.save(abcChinaHtml);

				JSONObject objectstring = JSONObject.fromObject(contentAsString1);
				String List = objectstring.getString("List").toString();
				JSONArray arrayList = JSONArray.fromObject(List);
				int size2 = arrayList.size();
				if (size2 == 0) {
					System.out.println("获取数据成功！但是没有数据！");
				} else {
					for (int i = 0; i < size2; i++) {
						String string = arrayList.get(i).toString();
						JSONObject objectarrayList = JSONObject.fromObject(string);
						// 交易时间
						String dealTime = objectarrayList.getString("Trandate").trim() + "-"
								+ objectarrayList.getString("TranTime").trim();
						System.out.println("交易时间-----" + dealTime);
						// 交易金额
						String tranAmt = objectarrayList.getString("TranAmt").trim();
						System.out.println("交易金额-----" + tranAmt);
						// 账户余额
						String bal = objectarrayList.getString("Bal").trim();
						System.out.println("账户余额-----" + bal);
						// 对方账户
						String oppAcctNo = objectarrayList.getString("OppAcctNo").trim();
						System.out.println("对方账户-----" + oppAcctNo);
						// 对方户名
						String customName2 = objectarrayList.getString("CustomName2").trim();
						System.out.println("对方户名-----" + customName2);
						// 对方开户行
						String oppBankName = objectarrayList.getString("OppBankName").trim();
						System.out.println("对方开户行-----" + oppBankName);
						// 交易类型
						String memo = objectarrayList.getString("Memo").trim();
						System.out.println("交易类型-----" + memo);
						// 附言
						String tranBrief = objectarrayList.getString("TranBrief").trim();
						System.out.println("附言-----" + tranBrief);
						BohcChinaTransFlow bohcChinaTransFlow = new BohcChinaTransFlow();
						bohcChinaTransFlow.setTaskid(bankJsonBean.getTaskid());
						bohcChinaTransFlow.setDealTime(dealTime);
						bohcChinaTransFlow.setTranAmt(tranAmt);
						bohcChinaTransFlow.setBal(bal);
						bohcChinaTransFlow.setOppAcctNo(oppAcctNo);
						bohcChinaTransFlow.setCustomName2(customName2);
						bohcChinaTransFlow.setOppBankName(oppBankName);
						bohcChinaTransFlow.setMemo(memo);
						bohcChinaTransFlow.setTranBrief(tranBrief);
						bohcChinaTransFlowRepository.save(bohcChinaTransFlow);
					}
					taskBankStatusService.updateTaskBankTransflow(200,
							BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				}
			} catch (Exception e) {
				System.out.println("爬取流水信息异常！");
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("爬取流水信息异常...", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
		}
		taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());
		taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

		return taskBank;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
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