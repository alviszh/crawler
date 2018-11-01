package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaAccountType;
import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaHtml;
import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.CgbChinaAccountTypeRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.CgbChinaHtmlRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.CgbChinaTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.cgbchina.CgbChinaUserInfoRepository;
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
public class ServiceCGBChinaAop implements ICrawlerLogin {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CgbChinaUserInfoRepository cgbChinaUserInfoRepository;
	@Autowired
	private CgbChinaTransFlowRepository cgbChinaTransFlowRepository;
	@Autowired
	private CgbChinaHtmlRepository cgbChinaHtmlRepository;
	@Autowired
	private CgbChinaAccountTypeRepository cgbChinaAccountTypeRepository;
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

	private static String LoginPage = "https://ebanks.cgbchina.com.cn/perbank";
	@Autowired
	private AgentService agentService;

	WebClient webClient = WebCrawler.getInstance().getWebClient();

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		tracerLog.addTag("广发银行（储蓄卡）登录业务进行中...", bankJsonBean.getTaskid());

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = LoginPage;
		driver.get(baseUrl);
		driver.manage().window().maximize();

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
		// String name = "6214623721002119229";
		String name = bankJsonBean.getLoginName().trim();

		username.sendKeys(name);
		try {
			VK.Tab();

			// 获取客户端传过来的密码
			// String password = "12qwaszx";
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

		// 处理未设置网银的银行卡
		if (pageSource2.contains("设置网银登录密码") && pageSource2.contains("网银登录密码确认")) {
			System.out.println("很抱歉！您未设置网银密码！设置完之后再来操作！谢谢配合！");
			taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getPhasestatus(),
					"很抱歉！您未设置网银密码！设置完之后再来操作！谢谢配合！",
					BankStatusCode.BANK_LOGIN_NOT_SET_INTERNETBANK_PWD_ERROR.getError_code(), true,
					bankJsonBean.getTaskid(), windowHandle);
			// 截图
			String path2 = null;
			try {
				path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			} catch (Exception e) {
				System.out.println("未设置网银密码情况！截图异常错误！");

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				// 释放instance ip ，quit webdriver
				tracerLog.addTag("未设置网银密码情况！截图异常错误！", taskBank.getCrawlerHost());
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				e.printStackTrace();
			}
			tracerLog.addTag("很抱歉！您未设置网银密码！", "截图:" + path2);
			System.out.println("很抱歉！您未设置网银密码！" + path2);
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("未设置网银密码情况！截图异常错误！", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else {
			System.out.println("您已设置过网银密码！不需要设置网银密码！");
			if (pageSource2.contains("登录名或密码错误") || pageSource2.contains("密码处理异常") || pageSource2.contains("账号或密码错误")) {
				System.out.println("登录失败-----登录名或密码错误");

				taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "用户名或密码错误!",
						BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(), windowHandle);
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
				tracerLog.addTag("登录名或密码错误", "截图:" + path2);
				System.out.println("登录名或密码错误" + path2);
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
			} else {
				if (pageSource2.contains("上次登录时间")) {

					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);

				} else {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							"LOGIN",
							"ERROR", "登录失败,请您稍后重试！",
							102, true, bankJsonBean.getTaskid(),
							windowHandle);
				}
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

			// 账户信息请求参数请求
			String loginurl3155 = "https://ebanks.cgbchina.com.cn/perbank/scripts/forpage/debitAndCreditMainPageShared.htm.js?JSVERSION=20161201";
			WebRequest webRequestlogin3155;
			webRequestlogin3155 = new WebRequest(new URL(loginurl3155), HttpMethod.GET);
			Page pagelogin3155 = webClient.getPage(webRequestlogin3155);

			String contentAsString3155 = pagelogin3155.getWebResponse().getContentAsString();
			System.out.println(contentAsString3155);

			// 处理账户信息入参
			String[] split4 = contentAsString3155.split("cardNo:b.accountNo,sasbDepositNo:b.sasbDepositNo,");
			String[] split5 = split4[1].split("}");
			String json55 = "{" + split5[0] + "}";
			JSONObject object44 = JSONObject.fromObject(json55);
			// 99
			String currencyType = object44.getString("currencyType");
			System.out.println(currencyType);
			// 999
			String productName = object44.getString("productName");
			System.out.println(productName);
			// A
			String cardState = object44.getString("cardState");
			System.out.println(cardState);
			// FT
			String pageQueryFlag = object44.getString("pageQueryFlag");
			System.out.println(pageQueryFlag);
			// 1
			String turnPageBeginPos = object44.getString("turnPageBeginPos");
			System.out.println(turnPageBeginPos);
			// 请求参数请求
			String loginurl31 = "https://ebanks.cgbchina.com.cn/perbank/AC0002.do?cardNo=6214623721002119229&sasbDepositNo=&currencyType="
					+ currencyType + "&productName=" + productName + "&cardState=" + cardState + "&pageQueryFlag="
					+ pageQueryFlag + "&isAutoDump=&turnPageBeginPos=" + turnPageBeginPos + "&EMP_SID=" + empSid
					+ "&submitTimestamp=20171130112823739&trxCode=b010101";
			WebRequest webRequestlogin31;
			webRequestlogin31 = new WebRequest(new URL(loginurl31), HttpMethod.GET);
			Page pagelogin31 = webClient.getPage(webRequestlogin31);

			String contentAsString31 = pagelogin31.getWebResponse().getContentAsString();
			System.out.println(contentAsString31);

			CgbChinaHtml cgbChinaHtml444 = new CgbChinaHtml();
			cgbChinaHtml444.setTaskid(bankJsonBean.getTaskid());
			cgbChinaHtml444.setHtml(contentAsString31);
			cgbChinaHtml444.setPagenumber(1);
			cgbChinaHtml444.setType("储蓄卡-----账户信息");
			cgbChinaHtml444.setUrl(loginurl31);

			cgbChinaHtmlRepository.save(cgbChinaHtml444);

			JSONObject object3 = JSONObject.fromObject(contentAsString31);
			String ec33 = object3.getString("ec");
			if ("0".equals(ec33)) {
				System.out.println("账户信息获取数据成功！");
				String cd55 = object3.getString("cd");
				JSONObject object255 = JSONObject.fromObject(cd55);
				String iSubHomeAccountList = object255.getString("iSubHomeAccountList");
				JSONArray array = JSONArray.fromObject(iSubHomeAccountList);
				for (int i = 0; i < array.size(); i++) {
					String string = array.get(0).toString();
					JSONObject stringobject2 = JSONObject.fromObject(string);
					// 账户总余额
					String balance = stringobject2.getString("balance");
					// 余额
					String balance2 = stringobject2.getString("balance");
					// 活期余额
					String canUseAmt = stringobject2.getString("canUseAmt");
					// 冻结余额
					String frozenAmount = stringobject2.getString("frozenAmount");
					// 定期余额
					String useVol = stringobject2.getString("useVol");
					// 账号
					String sequeceNo = stringobject2.getString("sequeceNo");
					// 类型
					String subAccountType = stringobject2.getString("subAccountType");
					// 币种
					String currencyType3 = stringobject2.getString("currencyType");

					CgbChinaAccountType cgbChinaAccountType = new CgbChinaAccountType();
					cgbChinaAccountType.setTaskid(bankJsonBean.getTaskid());
					cgbChinaAccountType.setNumber(name);
					cgbChinaAccountType.setBalance(balance);
					cgbChinaAccountType.setBalance2(balance2);
					cgbChinaAccountType.setCanUseAmt(canUseAmt);
					cgbChinaAccountType.setFrozenAmount(frozenAmount);
					cgbChinaAccountType.setUseVol(useVol);
					cgbChinaAccountType.setSequeceNo(sequeceNo);
					cgbChinaAccountType.setSubAccountType(subAccountType);
					cgbChinaAccountType.setCurrencyType(currencyType3);
					cgbChinaAccountTypeRepository.save(cgbChinaAccountType);

				}
			} else {
				System.out.println("账户信息获取数据成功！");
			}

			// 基本信息数据的获取和解析
			// 获取
			String loginurl = "https://ebanks.cgbchina.com.cn/perbank/ECAD0003.do?state=0&EMP_SID=" + empSid
					+ "&SUBMITtIMESTAMP=20171107141707828&trxCode=a030501";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);

			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			// {"ec":"0","em":"","sc":"ECAD0003","cd":{"name":"梅荻","certNo":"420106198410028419","email":null,"phoneNo":"无","address":"北京市丰台区海户西里10号楼603","postCode":null,"state":null,"imagePath":null,"customerAlias":null,"pretentInfo":null,"customerLastLogon":"20171107171030","certType":"10100","flag":"0","loginType":"100","custId":null,"modifyMobileFlow":null,"stat":null,"mobileNoShow":null,"tokenNo":null,"mmsSernoFlow":null,"mobileTemp":"135****0817","sex":"2","birthDate":"1984-10-02","occupation":"10000","companyAddress":"","ecCustId":"101614500118","sysCustIdList":[{"custId":"101614500118","externalSys":null},{"custId":"101614500118","externalSys":null}]}}
			CgbChinaHtml cgbChinaHtml = new CgbChinaHtml();
			cgbChinaHtml.setTaskid(bankJsonBean.getTaskid());
			cgbChinaHtml.setHtml(contentAsString);
			cgbChinaHtml.setPagenumber(1);
			cgbChinaHtml.setType("储蓄卡-----基本信息");
			cgbChinaHtml.setUrl(loginurl);

			cgbChinaHtmlRepository.save(cgbChinaHtml);

			// 解析基本信息
			JSONObject json = JSONObject.fromObject(contentAsString);
			String ec = json.getString("ec");
			if ("0".equals(ec)) {
				System.out.println("基本信息获取数据成功！");
				String cd = json.getString("cd");
				JSONObject cdjson = JSONObject.fromObject(cd);
				// 姓名
				String name2 = cdjson.getString("name");
				// 安全手机号
				String phone = cdjson.getString("mobileTemp");
				// 出生日期
				String date = cdjson.getString("birthDate");
				// 性别
				String sex = null;
				String sex2 = cdjson.getString("sex");
				if ("2".equals(sex2)) {
					sex = "男";
				} else {
					sex = "女";
				}
				// 联系电话
				String phone2 = cdjson.getString("phoneNo");
				// email
				String email = cdjson.getString("email");
				// 联系地址
				String attr = cdjson.getString("address");

				CgbChinaUserInfo cgbChinaUserInfo = new CgbChinaUserInfo();
				cgbChinaUserInfo.setTaskid(bankJsonBean.getTaskid());
				cgbChinaUserInfo.setName(name2);
				cgbChinaUserInfo.setPhone(phone);
				cgbChinaUserInfo.setDate(date);
				cgbChinaUserInfo.setSex(sex);
				cgbChinaUserInfo.setPhone2(phone2);
				cgbChinaUserInfo.setEmail(email);
				cgbChinaUserInfo.setAttr(attr);

				cgbChinaUserInfoRepository.save(cgbChinaUserInfo);

				taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
						bankJsonBean.getTaskid());

			} else {
				System.out.println("基本信息获取数据失败！");
			}

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

			// 交易明细信息数据的获取和解析
			String loginurl2 = "https://ebanks.cgbchina.com.cn/perbank/AC0003.do?pageQueryFlag=FT&accNo="
					+ bankJsonBean.getLoginName().trim() + "&currency=999&beginDate=" + beforeMonth2 + "&endDate="
					+ beforeMonth
					+ "&transferDirection=0&turnPageShowNum=10&mediumNo=&newBusinessType=YSV&sasbDepositNo=&ctlMark=0&turnPageBeginPage=0&turnPageTotalNum=0&turnPageCurAccNum=0&EMP_SID="
					+ empSid;
			WebRequest webRequestlogin2;
			webRequestlogin2 = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page pagelogin2 = webClient.getPage(webRequestlogin2);

			String contentAsString2 = pagelogin2.getWebResponse().getContentAsString();

			CgbChinaHtml cgbChinaHtml2 = new CgbChinaHtml();
			cgbChinaHtml2.setTaskid(bankJsonBean.getTaskid());
			cgbChinaHtml2.setHtml(contentAsString2);
			cgbChinaHtml2.setPagenumber(1);
			cgbChinaHtml2.setType("储蓄卡-----交易记录信息");
			cgbChinaHtml2.setUrl(loginurl2);

			cgbChinaHtmlRepository.save(cgbChinaHtml2);

			JSONObject json2 = JSONObject.fromObject(contentAsString2);
			String ec2 = json2.getString("ec");
			// 解析交易记录信息
			if ("0".equals(ec2)) {
				System.out.println("交易记录信息获取数据成功！");
				String cd = json2.getString("cd");
				JSONObject json4 = JSONObject.fromObject(cd);
				String iTransferDetailList = json4.getString("iTransferDetailList");
				JSONArray json3 = JSONArray.fromObject(iTransferDetailList);
				for (int i = 0; i < json3.size(); i++) {
					String object = json3.get(i) + "";
					System.out.println(object);
					JSONObject object2 = JSONObject.fromObject(object);
					// 交易时间
					String dealTime = null;
					dealTime = object2.getString("transDate") + object2.getString("transTime");
					// 交易渠道
					String dealWay = null;
					dealWay = object2.getString("channelType");
					// 币种
					String currency = null;
					String currency2 = object2.getString("currencyType");
					if ("156".equals(currency2)) {
						currency = "人民币";
					}
					String transferType = object2.getString("transferType");
					String transferAmt = object2.getString("transferAmt");
					// 转入
					String shiftTo = null;
					if ("C".equals(transferType)) {
						shiftTo = transferAmt + "分";
					}
					// 转出
					String rollOut = null;
					if ("D".equals(transferType)) {
						rollOut = transferAmt + "分";
					}
					// 账户余额
					String yue = null;
					yue = object2.getString("usableBalance") + "分";
					// 对方姓名
					String oppName = null;
					oppName = object2.getString("opponencyAcctName");
					// 对方账号
					String oppNumber = null;
					oppNumber = object2.getString("opponencyAccount");
					// 摘要
					String digest = null;
					digest = object2.getString("handAmount");

					System.out.println(dealTime);
					System.out.println(dealWay);
					System.out.println(currency);
					System.out.println(shiftTo);
					System.out.println(rollOut);
					System.out.println(yue);
					System.out.println(oppName);
					System.out.println(oppNumber);
					System.out.println(digest);

					CgbChinaTransFlow cgbChinaTransFlow = new CgbChinaTransFlow();
					cgbChinaTransFlow.setTaskid(bankJsonBean.getTaskid());
					cgbChinaTransFlow.setDealTime(dealTime);
					cgbChinaTransFlow.setDealWay(dealWay);
					cgbChinaTransFlow.setCurrency(currency);
					cgbChinaTransFlow.setShiftTo(shiftTo);
					cgbChinaTransFlow.setRollOut(rollOut);
					cgbChinaTransFlow.setYue(yue);
					cgbChinaTransFlow.setOppName(oppName);
					cgbChinaTransFlow.setOppNumber(oppNumber);
					cgbChinaTransFlow.setDigest(digest);

					cgbChinaTransFlowRepository.save(cgbChinaTransFlow);
				}
				taskBankStatusService.updateTaskBankTransflow(200,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
			} else {
				System.out.println("交易记录信息获取数据失败！");
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
