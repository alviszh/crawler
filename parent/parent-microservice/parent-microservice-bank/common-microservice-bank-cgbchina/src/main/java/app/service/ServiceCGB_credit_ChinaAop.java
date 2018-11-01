package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
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
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaHtml;
import com.microservice.dao.entity.crawler.bank.cgbchina.Cgb_credit_ChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.cgbchina.Cgb_credit_ChinaTransInfo;
import com.microservice.dao.entity.crawler.bank.cgbchina.Cgb_credit_ChinaUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.CgbChinaHtmlRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.Cgb_credit_ChinaTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.Cgb_credit_ChinaTransInfoRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.Cgb_credit_ChinaUserInfoRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.cgbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.cgbchina" })
public class ServiceCGB_credit_ChinaAop implements ICrawlerLogin {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private Cgb_credit_ChinaUserInfoRepository cgb_credit_ChinaUserInfoRepository;
	@Autowired
	private Cgb_credit_ChinaTransFlowRepository cgb_credit_ChinaTransFlowRepository;
	@Autowired
	private Cgb_credit_ChinaTransInfoRepository cgb_credit_ChinaTransInfoRepository;
	@Autowired
	private CgbChinaHtmlRepository cgbChinaHtmlRepository;
	@Autowired
	private TracerLog tracerLog;
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	// @Value("${spring.application.name}")
	// String appName;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	WebDriver driver;
	@Autowired
	private AgentService agentService;
	private static String LoginPage = "https://ebanks.cgbchina.com.cn/perbank";
	WebClient webClient = WebCrawler.getInstance().getWebClient();

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		try {
			tracerLog.addTag("广发银行（信用卡）登录业务进行中...", bankJsonBean.getTaskid());

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

			System.setProperty("webdriver.ie.driver", driverPath);

			driver = new InternetExplorerDriver(ieCapabilities);

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = LoginPage;
			driver.get(baseUrl);
			driver.manage().window().maximize();
		} catch (Exception e) {
			System.out.println("IE驱动打开地址异常");
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}
		String windowHandle = driver.getWindowHandle();
		bankJsonBean.setWebdriverHandle(windowHandle);

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("loginId"));
			}
		});

		username.click();
		username.clear();

		// 获取客户端传过来的用户名
		String name = bankJsonBean.getLoginName().trim();

		username.sendKeys(name);
		try {
			VK.Tab();

			// 获取客户端传过来的密码
			String password = bankJsonBean.getPassword().trim();
			VK.KeyPress(password);
			String path = WebDriverUnit.saveImg(driver, By.id("verifyImg"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("captcha")).sendKeys(code);

			driver.findElement(By.id("loginButton")).click();

			Thread.sleep(15000);

		} catch (Exception e) {
			System.out.println("登录过程异常错误！");

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("登录过程异常错误！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			e.printStackTrace();
		}

		try {
			Alert alt = driver.switchTo().alert();
			String text = alt.getText();
			if (text.contains("验证码错误")) {
				System.out.println(text);
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), "登录失败！请您稍后重试！",
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(), true, bankJsonBean.getTaskid());
				// 截图
				String path2 = null;
				try {
					path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				} catch (Exception e) {
					e.printStackTrace();
				}
				tracerLog.addTag("验证码错误！", "截图:" + path2);
				System.out.println("验证码错误！" + path2);

				TaskBank taskBank2 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("验证码错误！释放服务！", taskBank2.getCrawlerHost());
				agentService.releaseInstance(taskBank2.getCrawlerHost(), driver);

			}
		} catch (Exception e) {
			System.out.println("图片验证码正确！（没有alert弹框）");
			e.printStackTrace();
		}
		String pageSource2 = driver.getPageSource();

		if (pageSource2.contains("登录名或密码错误") || pageSource2.contains("密码处理异常") || pageSource2.contains("账号或密码错误")) {
			System.out.println("登录失败-----登录名或密码错误");
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), "用户名或密码错误!",
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);
			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("密码错误的情况，截图异常！");

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("密码错误的情况，截图异常！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
			tracerLog.addTag("登录名或密码错误！", "截图:" + path2);
			System.out.println("登录名或密码错误！" + path2);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("密码错误的情况，截图异常！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

		} else if (pageSource2.contains("尚未设置提现密码")) {
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), "尚未设置提现密码！",
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);
			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("尚未设置提现密码的情况，截图异常！");

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("尚未设置提现密码的情况，截图异常！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
			tracerLog.addTag("尚未设置提现密码！", "截图:" + path2);
			System.out.println("尚未设置提现密码！" + path2);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("尚未设置提现密码的情况，截图异常！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else if (pageSource2.contains("设置网银登录密码")) {
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), "设置网银登录密码！",
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);
			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("设置网银登录密码的情况，截图异常！");

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("设置网银登录密码的情况，截图异常！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
			tracerLog.addTag("设置网银登录密码！", "截图:" + path2);
			System.out.println("设置网银登录密码！" + path2);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("尚未设置提现密码的情况，截图异常！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else if (pageSource2.contains("欢迎您进入我行个人网银，请设置您的登录信息")) {
			System.out.println("请完善您的个人资料");
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(), "此账号需在网银官网完善个人资料后，才能进行数据采集！",
					BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					windowHandle);
			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("请完善您的个人资料的情况，截图异常！");

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("请完善您的个人资料的情况，截图异常！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
			tracerLog.addTag("请完善您的个人资料错误！", "截图:" + path2);
			System.out.println("请完善您的个人资料错误！" + path2);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("请完善您的个人资料的情况，截图异常！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else {
			if (pageSource2.contains("上次登录时间")) {
				taskBankStatusService.changeStatusbyWebdriverHandle(
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), true, bankJsonBean.getTaskid(),
						windowHandle);
			} else {
				taskBankStatusService.changeStatusbyWebdriverHandle("LOGIN",
						"ERROR", "登录失败，请您稍后重试！",
						102, true, bankJsonBean.getTaskid(),
						windowHandle);
			}
		}
		return taskBank;
	}

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		String name = bankJsonBean.getLoginName().trim();
		String pageSource2 = driver.getPageSource();
		String[] split = pageSource2.split("_emp_sid");
		String[] split2 = split[1].split("cipURL");
		String[] split3 = split2[0].split("'");
		// 请求的入参
		String empSid = split3[1].trim();
		System.out.println("入参：" + empSid);

		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

		for (org.openqa.selenium.Cookie cookie : cookies) {
			System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
					"ebanks.cgbchina.com.cn", cookie.getName(), cookie.getValue()));
		}
		try {
			// 基本信息-信用卡数据的获取
			String loginurl2 = "https://ebanks.cgbchina.com.cn/perbank/ECAD0003.do?state=0&EMP_SID=" + empSid
					+ "&submitTimestamp=20171120200112945&trxCode=a030501";
			WebRequest webRequestlogin2;
			webRequestlogin2 = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page pagelogin2 = webClient.getPage(webRequestlogin2);

			String contentAsString2 = pagelogin2.getWebResponse().getContentAsString();
			// {"ec":"0","em":"","sc":"ECAD0003","cd":{"name":"王新洁","certNo":"110227198509300085","email":"wang_xinjie@outlook.com","phoneNo":"18612737393","address":null,"postCode":null,"state":null,"imagePath":null,"customerAlias":null,"pretentInfo":null,"customerLastLogon":"20171120201355","certType":"10100","flag":"0","loginType":"100","custId":null,"modifyMobileFlow":null,"stat":null,"mobileNoShow":null,"tokenNo":null,"mmsSernoFlow":null,"mobileTemp":"186****7393","sex":"3","birthDate":"1985-09-30","occupation":"99991","companyAddress":"宜信普惠","ecCustId":"100812936502","sysCustIdList":[{"custId":"100812936502","externalSys":null},{"custId":"156#1030053460","externalSys":null}]}}
			System.out.println(contentAsString2);

			CgbChinaHtml cgbChinaHtml = new CgbChinaHtml();
			cgbChinaHtml.setTaskid(bankJsonBean.getTaskid());
			cgbChinaHtml.setHtml(contentAsString2);
			cgbChinaHtml.setPagenumber(1);
			cgbChinaHtml.setType("信用卡-----基本信息");
			cgbChinaHtml.setUrl(loginurl2);

			cgbChinaHtmlRepository.save(cgbChinaHtml);

			// 解析基本信息-信用卡
			JSONObject json = JSONObject.fromObject(contentAsString2);
			String ec = json.getString("ec");
			if ("0".equals(ec)) {
				System.out.println("基本信息-信用卡获取数据成功！");
				String cd = json.getString("cd");
				JSONObject cdjson = JSONObject.fromObject(cd);
				// 姓名
				String name2 = cdjson.getString("name");
				System.out.println(name2);
				// 安全手机号
				String phone = cdjson.getString("mobileTemp");
				System.out.println(phone);
				// 出生日期
				String date = cdjson.getString("birthDate");
				System.out.println(date);
				// 性别
				String sex = null;
				String sex2 = cdjson.getString("sex");
				if ("2".equals(sex2)) {
					sex = "男";
				} else {
					sex = "女";
				}
				System.out.println(sex);
				// 联系电话
				String phone2 = cdjson.getString("phoneNo");
				System.out.println(phone2);
				// email
				String email = cdjson.getString("email");
				System.out.println(email);
				// 联系地址
				String attr = cdjson.getString("address");
				System.out.println(attr);

				Cgb_credit_ChinaUserInfo cgbChinaUserInfo = new Cgb_credit_ChinaUserInfo();
				cgbChinaUserInfo.setTaskid(bankJsonBean.getTaskid());
				cgbChinaUserInfo.setName(name2);
				cgbChinaUserInfo.setPhone(phone);
				cgbChinaUserInfo.setDate(date);
				cgbChinaUserInfo.setSex(sex);
				cgbChinaUserInfo.setPhone2(phone2);
				cgbChinaUserInfo.setEmail(email);
				cgbChinaUserInfo.setAttr(attr);

				cgb_credit_ChinaUserInfoRepository.save(cgbChinaUserInfo);

				taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
						bankJsonBean.getTaskid());
			} else {
				System.out.println("基本信息-信用卡获取数据失败！");
			}

			// 请求参数请求
			String loginurl31 = "https://ebanks.cgbchina.com.cn/perbank/sessionData.do?EMP_SID=" + empSid;
			WebRequest webRequestlogin31;
			webRequestlogin31 = new WebRequest(new URL(loginurl31), HttpMethod.GET);
			Page pagelogin31 = webClient.getPage(webRequestlogin31);

			String contentAsString31 = pagelogin31.getWebResponse().getContentAsString();
			System.out.println(contentAsString31);
			JSONObject json22 = JSONObject.fromObject(contentAsString31);
			String ec22 = json22.getString("ec");
			// 解析交易记录信息
			if ("0".equals(ec22)) {
				String cd22 = json22.getString("cd");
				JSONObject jsoncd = JSONObject.fromObject(cd22);
				String session_iAccountInfo = jsoncd.getString("session_iAccountInfo");
				JSONArray array = JSONArray.fromObject(session_iAccountInfo);
				String string = array.get(0).toString();
				JSONObject jsoncdstring = JSONObject.fromObject(string);
				System.out.println(jsoncdstring);

				String accountNo = jsoncdstring.getString("accountNo");
				String accountType = jsoncdstring.getString("accountType");
				String accountName = jsoncdstring.getString("accountName");
				String accountAlias = jsoncdstring.getString("accountAlias");
				String currencyType = jsoncdstring.getString("currencyType");
				String openNode = jsoncdstring.getString("openNode");
				String openNodeName = jsoncdstring.getString("openNodeName");
				String owner = jsoncdstring.getString("owner");
				String right = jsoncdstring.getString("right");
				String sasbDepositNo = jsoncdstring.getString("sasbDepositNo");
				String icon = jsoncdstring.getString("icon");
				String oldAccNo = jsoncdstring.getString("oldAccNo");
				String sortNo = jsoncdstring.getString("sortNo");
				String accountProperty = jsoncdstring.getString("accountProperty");
				String cardTypeNo = jsoncdstring.getString("cardTypeNo");
				String locationNo = jsoncdstring.getString("locationNo");
				System.out.println(accountNo);
				System.out.println(accountType);
				System.out.println(accountName);
				System.out.println(accountAlias);
				System.out.println(currencyType);
				System.out.println(openNode);
				System.out.println(openNodeName);
				System.out.println(owner);
				System.out.println(right);
				System.out.println(sasbDepositNo);
				System.out.println(icon);
				System.out.println(oldAccNo);
				System.out.println(sortNo);
				System.out.println(accountProperty);
				System.out.println(cardTypeNo);
				System.out.println(locationNo);

				SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH, 0);
				String beforeMonth = f.format(c.getTime());
				System.out.println(beforeMonth);

				SimpleDateFormat f2 = new SimpleDateFormat("yyyyMMdd");
				Calendar c2 = Calendar.getInstance();
				c2.add(Calendar.MONTH, -12);
				String beforeMonth2 = f2.format(c2.getTime());
				System.out.println(beforeMonth2);
				for (int j = 1; j < 10; j++) {
					int k = (j - 1) * 10 + 1;
					String sk = k + "";
					// 流水信息-信用卡获取
					String loginurl3 = "https://ebanks.cgbchina.com.cn/perbank/CR0002.do?accountNo=" + accountNo
							+ "&accountType=" + accountType + "&accountName=" + accountName + "&accountAlias="
							+ accountAlias + "&currencyType=" + "156" + "&openNode=" + openNode + "&openNodeName="
							+ openNodeName + "&owner=" + owner + "&right=" + right + "&sasbDepositNo=" + sasbDepositNo
							+ "&icon=" + icon + "&oldAccNo=" + oldAccNo + "&sortNo=" + sortNo + "&accountProperty="
							+ accountProperty + "&cardTypeNo=" + cardTypeNo + "&locationNo=" + locationNo
							+ "&index=1&beginDate=" + beforeMonth2 + "&endDate=" + beforeMonth + "&cardNo=" + name
							+ "&turnPageBeginPos=" + sk + "&turnPageShowNum=10&bigAccountSrcSign=0&turnPageBeginPage="
							+ j + "&EMP_SID=" + empSid;
					WebRequest webRequestlogin3;
					webRequestlogin3 = new WebRequest(new URL(loginurl3), HttpMethod.GET);
					Page pagelogin3 = webClient.getPage(webRequestlogin3);

					String contentAsString3 = pagelogin3.getWebResponse().getContentAsString();

					CgbChinaHtml cgbChinaHtml2 = new CgbChinaHtml();
					cgbChinaHtml2.setTaskid(bankJsonBean.getTaskid());
					cgbChinaHtml2.setHtml(contentAsString3);
					cgbChinaHtml2.setPagenumber(1);
					cgbChinaHtml2.setType("信用卡-----交易记录信息");
					cgbChinaHtml2.setUrl(loginurl3);

					cgbChinaHtmlRepository.save(cgbChinaHtml2);

					// 流水信息-信用卡解析
					JSONObject json2 = JSONObject.fromObject(contentAsString3);
					String ec2 = json2.getString("ec");
					// 解析交易记录信息
					if ("0".equals(ec2)) {
						System.out.println("交易记录-信用卡信息获取数据成功！");
						String cd = json2.getString("cd");
						JSONObject json4 = JSONObject.fromObject(cd);
						String iTransferDetailList = json4.getString("credResult");
						JSONArray json3 = JSONArray.fromObject(iTransferDetailList);
						System.out.println(json3);
						int size = json3.size();
						if (size == 0) {
							System.out.println("信用卡-----流水信息没有数据！");
						} else {
							for (int i = 0; i < size; i++) {
								String object = json3.get(i) + "";
								System.out.println(object);
								JSONObject object2 = JSONObject.fromObject(object);
								// 交易时间
								String transferDate = null;
								transferDate = object2.getString("transferDate");
								System.out.println("交易时间-------" + transferDate);
								// 交易摘要
								String noteCode = null;
								noteCode = object2.getString("noteCode");
								System.out.println("交易摘要-------" + noteCode);
								// 交易币种
								String currency1 = null;
								String currency2 = object2.getString("currencyType");
								if ("156".equals(currency2)) {
									currency1 = "人民币";
								} else {
									currency1 = "其他货币";
								}
								System.out.println("交易币种-------" + currency1);
								// 交易金额
								String transamt = null;
								transamt = object2.getString("transamt");
								System.out.println("交易金额-------" + transamt);
								// 入账币种
								String currency3 = null;
								String currency = object2.getString("currency");
								if ("156".equals(currency)) {
									currency3 = "人民币";
								} else {
									currency3 = "其他货币";
								}
								System.out.println("入账币种-------" + currency3);
								// 入账金额
								String debitAmt = null;
								debitAmt = object2.getString("debitAmt");
								System.out.println("入账金额-------" + debitAmt);
								// 卡号后四位
								String last4OfCardNo = null;
								last4OfCardNo = object2.getString("last4OfCardNo");
								System.out.println("卡号后四位-------" + last4OfCardNo);

								Cgb_credit_ChinaTransFlow cgb_credit_ChinaTransFlow = new Cgb_credit_ChinaTransFlow();
								cgb_credit_ChinaTransFlow.setTaskid(bankJsonBean.getTaskid());
								cgb_credit_ChinaTransFlow.setTransferDate(transferDate);
								cgb_credit_ChinaTransFlow.setNoteCode(noteCode);
								cgb_credit_ChinaTransFlow.setCurrency1(currency1);
								cgb_credit_ChinaTransFlow.setTransamt(transamt);
								cgb_credit_ChinaTransFlow.setCurrency3(currency3);
								cgb_credit_ChinaTransFlow.setDebitAmt(debitAmt);
								cgb_credit_ChinaTransFlow.setLast4OfCardNo(last4OfCardNo);
								cgb_credit_ChinaTransFlowRepository.save(cgb_credit_ChinaTransFlow);
							}
							taskBankStatusService.updateTaskBankTransflow(200,
									BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
						}
					} else {
						System.out.println("交易记录-信用卡信息获取数据失败！");
					}
				}
			} else {
				System.out.println("获取流水信息入参失败！");

			}

			// 每期账户信息以及每期积分
			for (int i = 0; i < 12; i++) {
				SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH, -i);
				String beforeMonth = f.format(c.getTime());
				System.out.println(beforeMonth);

				String loginurl311 = "https://ebanks.cgbchina.com.cn/perbank/CR1080.do?currencyType=&creditCardNo="
						+ name + "&billDate=" + beforeMonth
						+ "&billType=1&abundantFlag=0&terseFlag=0&showWarFlag=0&EMP_SID=" + empSid;

				WebRequest webRequestlogin311;
				webRequestlogin311 = new WebRequest(new URL(loginurl311), HttpMethod.GET);
				Page pagelogin311 = webClient.getPage(webRequestlogin311);
				String contentAsString311 = pagelogin311.getWebResponse().getContentAsString();

				CgbChinaHtml cgbChinaHtml2 = new CgbChinaHtml();
				cgbChinaHtml2.setTaskid(bankJsonBean.getTaskid());
				cgbChinaHtml2.setHtml(contentAsString311);
				cgbChinaHtml2.setPagenumber(1);
				cgbChinaHtml2.setType("信用卡-----每期账户信息");
				cgbChinaHtml2.setUrl(loginurl311);
				cgbChinaHtmlRepository.save(cgbChinaHtml2);

				if (contentAsString311.contains("billErrMsg = '该月份账单尚未生成，请于账单日后再查询';")) {
					System.out.println("账单尚未生成或不存在，请于账单日后查询。");
				} else {
					Document doc = Jsoup.parse(contentAsString311);
					Element fixBand1 = doc.getElementById("fixBand1");
					System.out
							.println("///////////////////////////////////////账单周期部分//////////////////////////////////");
					Element fixBand75 = fixBand1.getElementById("fixBand75");
					Element fixBand2_zd = fixBand75.getElementById("fixBand2");
					////////////////////////////////////////////////////////////////////// 账单周期部分------账单周期
					Element fixBand5 = fixBand2_zd.getElementById("fixBand5");
					Elements fonts = fixBand5.getElementsByTag("font");
					String period = fonts.get(1).text().toString();
					System.out.println("账单周期---" + period);
					////////////////////////////////////////////////////////////////////// 账单周期部分------剩下的部分
					Element fixBand7 = fixBand2_zd.getElementById("fixBand7");
					Elements fonts2 = fixBand7.getElementsByTag("font");
					// 卡号
					String cardnumber = fonts2.get(0).text().toString();
					System.out.println("卡号---" + cardnumber);
					// 本期应还总额
					String newbalance = fonts2.get(1).text().toString();
					System.out.println("本期应还总额---" + newbalance);
					// 最低还款额
					String minpayment = fonts2.get(2).text().toString();
					System.out.println("最低还款额---" + minpayment);
					// 最后还款日
					String paymentdate = fonts2.get(3).text().toString();
					System.out.println("最后还款日---" + paymentdate);
					// 清算货币
					String currency = fonts2.get(4).text().toString();
					System.out.println("清算货币---" + currency);
					// 户口消费额度
					String creditlimit = fonts2.get(5).text().toString();
					System.out.println("户口消费额度---" + creditlimit);

					Element fixBand71 = fixBand1.getElementById("fixBand71");
					if (fixBand71 == null) {
						System.out.println("不存在积分部分！");
					} else {
						System.out.println(
								"///////////////////////////////////////积分部分//////////////////////////////////");
						Element fixBand7_jf = fixBand71.getElementById("fixBand7");
						Elements fonts3 = fixBand7_jf.getElementsByTag("font");
						// 积分类型
						String integraltype = fonts3.get(1).text().toString();
						System.out.println("积分类型---" + integraltype);
						// 上期余额
						String periodyue = fonts3.get(2).text().toString();
						System.out.println("上期余额---" + periodyue);
						// 本期新增
						String add = fonts3.get(3).text().toString();
						System.out.println("本期新增---" + add);
						// 本期扣减
						String subtract = fonts3.get(4).text().toString();
						System.out.println("本期扣减---" + subtract);
						// 本期余额
						String yue = fonts3.get(5).text().toString();
						System.out.println("本期余额---" + yue);
						Cgb_credit_ChinaTransInfo cgb_credit_ChinaTransInfo = new Cgb_credit_ChinaTransInfo();
						cgb_credit_ChinaTransInfo.setTaskid(bankJsonBean.getTaskid());
						cgb_credit_ChinaTransInfo.setPeriod(period);
						cgb_credit_ChinaTransInfo.setCurrency(currency);
						cgb_credit_ChinaTransInfo.setCreditlimit(creditlimit);
						cgb_credit_ChinaTransInfo.setCardnumber(cardnumber);
						cgb_credit_ChinaTransInfo.setNewbalance(newbalance);
						cgb_credit_ChinaTransInfo.setPaymentdate(paymentdate);
						cgb_credit_ChinaTransInfo.setMinpayment(minpayment);
						cgb_credit_ChinaTransInfo.setIntegraltype(integraltype);
						cgb_credit_ChinaTransInfo.setPeriodyue(periodyue);
						cgb_credit_ChinaTransInfo.setAdd(add);
						cgb_credit_ChinaTransInfo.setSubtract(subtract);
						cgb_credit_ChinaTransInfo.setYue(yue);
						cgb_credit_ChinaTransInfoRepository.save(cgb_credit_ChinaTransInfo);
					}
				}
			}
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());

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
