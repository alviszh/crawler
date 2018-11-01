package app.service;

import java.net.URL;
import java.util.Map;
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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.pabchina.Pab_credit_ChinaAccountType;
import com.microservice.dao.entity.crawler.bank.pabchina.Pab_credit_ChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.pabchina.Pab_credit_ChinaUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.Pab_credit_ChinaAccountTypeRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.Pab_credit_ChinaTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.pabchina.Pab_credit_ChinaUserInfoRepository;
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
public class ServicePABChinaCreditAop implements ICrawlerLogin {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private Pab_credit_ChinaUserInfoRepository pabChinaUserInfoRepository;
	@Autowired
	private Pab_credit_ChinaTransFlowRepository pabChinaTransFlowRepository;
	@Autowired
	private Pab_credit_ChinaAccountTypeRepository pabChinaAccountTypeRepository;
	@Autowired
	private TracerLog tracerLog;
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	// @Value("${spring.application.name}")
	// String appName;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static String LoginPage = "https://www.pingan.com.cn/pinganone/pa/index.screen";
	WebDriver driver;
	WebClient webClient = WebCrawler.getInstance().getWebClient();
	@Autowired
	private AgentService agentService;

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = null;
		try {
			tracerLog.addTag("平安银行（信用卡）业务登录的调用...", bankJsonBean.getTaskid());

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

			System.setProperty("webdriver.ie.driver", driverPath);

			WebDriver driver = new InternetExplorerDriver();

			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			driver.get(LoginPage);

			Thread.sleep(2000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

			String windowHandle = driver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("j_username"));
				}
			});

			username.click();
			username.clear();
			username.sendKeys(bankJsonBean.getLoginName().trim());
			// username.sendKeys("15810601089");

			driver.findElement(By.id("key1")).click();
			Thread.sleep(1000L);
			driver.findElement(By.className("kb-tl-switch")).click();

			String password = bankJsonBean.getPassword().trim();
			VK.KeyPress(password);
			// String password = "z330387485";
			// KeyPressEx(password, 500);

			String path = WebDriverUnit.saveImg(driver, By.id("validateImg"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("check_code")).sendKeys(code);

			driver.findElement(By.id("loginlink")).click();
			Thread.sleep(15000);
			String source = driver.getPageSource();

			// 最新的获取提示信息的方式
			String text = driver.findElement(By.id("activeCustomerMsg")).getText();
			System.out.println("提示的信息---" + text);
			if (text != null || !"".equals(text)) {
				System.out.println("登陆失败！");
				taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), text,
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
				if (source.contains("用户名、密码验证未通过") || source.contains("密码长度不足") || source.contains("用户名或密码错误")) {
					System.out.println("登陆失败！");
					taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "用户名或密码错误！",
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
							windowHandle);
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
				} else if (source.contains("验证码已失效")) {
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
					if (source.contains("<title>中国平安-一账通</title>")) {
						taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
								BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
								BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
								BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), true,
								bankJsonBean.getTaskid());
					} else {
						taskBankStatusService.changeStatus("LOGIN", "ERROR", "登录失败，请您重新登录！", 102, true,
								bankJsonBean.getTaskid());
					}
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
						"www.pingan.com.cn", cookie.getName(), cookie.getValue()));
			}

			// 获取一个账号所有的卡片信息
			String loginurl4q = "https://www.pingan.com.cn/pinganone/pa/searchBindList.do";
			WebRequest webRequestlogin4q;
			webRequestlogin4q = new WebRequest(new URL(loginurl4q), HttpMethod.POST);
			webRequestlogin4q.setAdditionalHeader("Referer",
					"https://www.pingan.com.cn/pinganone/pa/searchNewHeaderService.do");
			webRequestlogin4q.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequestlogin4q.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestlogin4q.setAdditionalHeader("Accept", "*/*");
			webRequestlogin4q.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequestlogin4q.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestlogin4q.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequestlogin4q.setAdditionalHeader("Host", "www.pingan.com.cn");
			webRequestlogin4q.setAdditionalHeader("Connection", "Keep-Alive");
			webRequestlogin4q.setAdditionalHeader("Cache-Control", "no-cache");
			Page pagelogin2q = webClient.getPage(webRequestlogin4q);
			String contentAsString2q11 = pagelogin2q.getWebResponse().getContentAsString();
			System.out.println(contentAsString2q11);

			if (contentAsString2q11.contains("creditcard")) {
				System.out.println("有信用卡！");
				String loginurl21 = "https://www.pingan.com.cn/pinganone/pa/accounts_overview/setPFToken.jsp?url=https://www.pingan.com.cn/idp/startSSO.ping?PartnerSpId=bankSP%26TargetResource=https://bank.pingan.com.cn/ibp/work/toaindex.do";
				WebRequest webRequestlogin21;
				webRequestlogin21 = new WebRequest(new URL(loginurl21), HttpMethod.GET);
				webRequestlogin21.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
				webRequestlogin21.setAdditionalHeader("Referer",
						"https://www.pingan.com.cn/pinganone/pa/searchNewHeaderService.do");
				webRequestlogin21.setAdditionalHeader("Accept-Language", "zh-CN");
				webRequestlogin21.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				webRequestlogin21.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequestlogin21.setAdditionalHeader("Host", "www.pingan.com.cn");
				webRequestlogin21.setAdditionalHeader("Connection", "Keep-Alive");
				webRequestlogin21.setAdditionalHeader("Cache-Control", "no-cache");
				Thread.sleep(5000);
				webClient.getPage(webRequestlogin21);

				String loginurl211 = "https://bank.pingan.com.cn/ibp/work/creditcard/setPFToken.jsp?url=https://bank.pingan.com.cn/idp/startSSO.ping?PartnerSpId=creditBankSP%26TargetResource=https://creditcard.pingan.com.cn/financing/ccuser/IBPSSOCreditCard.do";
				WebRequest webRequestlogin211;
				webRequestlogin211 = new WebRequest(new URL(loginurl211), HttpMethod.GET);
				webRequestlogin211.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
				webRequestlogin211.setAdditionalHeader("Referer",
						"https://bank.pingan.com.cn/ibp/work/creditcard/ssoCredit.jsp?targetURL=https://creditcard.pingan.com.cn/financing/ccuser/UserLoginTOA.do?shortcut=enterSysIndex&target=top&serviceID=toa");
				webRequestlogin211.setAdditionalHeader("Accept-Language", "zh-CN");
				webRequestlogin211.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				webRequestlogin211.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequestlogin211.setAdditionalHeader("Host", "bank.pingan.com.cn");
				webRequestlogin211.setAdditionalHeader("Connection", "Keep-Alive");
				webRequestlogin211.setAdditionalHeader("Cache-Control", "no-cache");
				Thread.sleep(5000);
				webClient.getPage(webRequestlogin211);

				String loginurl4q1 = "https://creditcard.pingan.com.cn/financing/newccuser/QueryPostedBillSum.do?0.2098757360864993";
				WebRequest webRequestlogin4q1;
				webRequestlogin4q1 = new WebRequest(new URL(loginurl4q1), HttpMethod.GET);
				Page pagelogin2q1 = webClient.getPage(webRequestlogin4q1);
				String contentAsString2q111 = pagelogin2q1.getWebResponse().getContentAsString();
				System.out.println("（判断是否需要重新登录）账单信息-----" + contentAsString2q111);

				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				tracerLog.addTag("（判断是否需要重新登录）账单信息-----", taskBank.getCrawlerHost());

				if (contentAsString2q111.contains("页面已超时或您还未登录") && contentAsString2q111.contains("重新登录")) {

					System.out.println("手机号登陆的情况----需要用以前获取数据的方式！");
					TaskBank taskBank1 = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
					tracerLog.addTag("手机号登陆的情况----需要用以前获取数据的方式！", taskBank1.getCrawlerHost());

					String loginurl2 = "https://www.pingan.com.cn/pinganone/pa/accounts_overview/setPFToken.jsp?url=https://www.pingan.com.cn/idp/startSSO.ping?PartnerSpId=creditSP%26TargetResource=https://creditcard.pingan.com.cn/financing/logintoa.jsp";
					WebRequest webRequestlogin2;
					webRequestlogin2 = new WebRequest(new URL(loginurl2), HttpMethod.GET);
					webRequestlogin2.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
					webRequestlogin2.setAdditionalHeader("Referer",
							"https://www.pingan.com.cn/pinganone/pa/searchNewHeaderService.do");
					webRequestlogin2.setAdditionalHeader("Accept-Language", "zh-CN");
					webRequestlogin2.setAdditionalHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
					webRequestlogin2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
					webRequestlogin2.setAdditionalHeader("Host", "www.pingan.com.cn");
					webRequestlogin2.setAdditionalHeader("Connection", "Keep-Alive");
					webRequestlogin2.setAdditionalHeader("Cache-Control", "no-cache");
					Thread.sleep(5000);
					webClient.getPage(webRequestlogin2);
					// 账单信息
					String loginurl4q11 = "https://creditcard.pingan.com.cn/financing/newccuser/QueryPostedBillSum.do?0.2098757360864993";
					WebRequest webRequestlogin4q11;
					webRequestlogin4q11 = new WebRequest(new URL(loginurl4q11), HttpMethod.GET);
					Page pagelogin2q11 = webClient.getPage(webRequestlogin4q11);
					String contentAsString2q = pagelogin2q11.getWebResponse().getContentAsString();
					System.out.println("账单信息-----" + contentAsString2q);

					JSONObject zdjson = JSONObject.fromObject(contentAsString2q);
					String sumbilldub = zdjson.getString("ACCOUNT_SUMMARY_BILL").trim();
					JSONObject zdjson2 = JSONObject.fromObject(sumbilldub);
					String payoffDetail = zdjson2.getString("payoffDetail").trim();
					JSONArray array = JSONArray.fromObject(payoffDetail);
					String object = array.get(0).toString();
					JSONObject zdjson3 = JSONObject.fromObject(object);
					String summaryRecords = zdjson3.getString("summaryRecords").trim();
					JSONArray summaryRecordsarray = JSONArray.fromObject(summaryRecords);
					for (int i = 0; i < summaryRecordsarray.size(); i++) {
						String string = summaryRecordsarray.get(i).toString();
						JSONObject zdjson21 = JSONObject.fromObject(string);
						// 账单月份
						String zdyf = zdjson21.getString("month").trim();
						System.out.println("账单月份-----" + zdyf);
						// 人民币
						String rmb = zdjson21.getString("currentPaymentDue").trim();
						System.out.println("人民币-----" + rmb);
						// 美元
						String my = zdjson21.getString("dollarcurrentPaymentDue").trim();
						System.out.println("美元-----" + my);

						Pab_credit_ChinaAccountType pab_credit_ChinaAccountType = new Pab_credit_ChinaAccountType();
						pab_credit_ChinaAccountType.setTaskid(bankJsonBean.getTaskid().trim());
						pab_credit_ChinaAccountType.setMy(my);
						pab_credit_ChinaAccountType.setRmb(rmb);
						pab_credit_ChinaAccountType.setZdyf(zdyf);
						pabChinaAccountTypeRepository.save(pab_credit_ChinaAccountType);

						// 明细入参1
						String mxrc = zdjson21.getString("maskQryAccountNo").trim();
						System.out.println("明细入参1-----" + mxrc);
						// 明细入参2
						String mxrc2 = zdjson21.getString("qryAccountIndex").trim();
						System.out.println("明细入参2-----" + mxrc2);
						// 明细信息
						String loginurl4qm = "https://creditcard.pingan.com.cn/financing/newccuser/printHistoryBillPayoffDetail.do?0.765770673229819&accountNo="
								+ mxrc + "&accountSign=2&accountNoIndex=" + mxrc2 + "&queryMonth=" + zdyf
								+ "&acountType=0&currentPage=1&defaultBillDate=" + zdyf + "";
						WebRequest webRequestlogin4qm;
						webRequestlogin4qm = new WebRequest(new URL(loginurl4qm), HttpMethod.GET);
						Page pagelogin2qm = webClient.getPage(webRequestlogin4qm);
						String contentAsString2qm = pagelogin2qm.getWebResponse().getContentAsString();
						System.out.println("明细信息-----" + contentAsString2qm);

						JSONObject jsonmx = JSONObject.fromObject(contentAsString2qm);
						String payoffDetailDto = jsonmx.getString("payoffDetailDto").toString().trim();
						JSONObject jsonmx2 = JSONObject.fromObject(payoffDetailDto);
						String currentPayRecords = jsonmx2.getString("currentPayRecords").toString().trim();
						JSONArray arraymx = JSONArray.fromObject(currentPayRecords);
						for (int j = 0; j < arraymx.size(); j++) {
							String trim = arraymx.get(j).toString().trim();
							JSONObject jsonmx3 = JSONObject.fromObject(trim);
							// 交易时间
							String jysj = jsonmx3.getString("txnDate").trim();
							System.out.println("交易时间-----" + jysj);
							// 记账日期
							String jzrq = jsonmx3.getString("settleDate").trim();
							System.out.println("记账日期-----" + jzrq);
							// 卡号末四位
							String khmsw = jsonmx3.getString("cardNo").trim();
							System.out.println("卡号末四位-----" + khmsw);
							// 交易摘要
							String jyzy = jsonmx3.getString("txnDescTxt").trim();
							System.out.println("交易摘要-----" + jyzy);
							// 人民币金额
							String rmbje = jsonmx3.getString("txnAmount").trim();
							System.out.println("人民币金额-----" + rmbje);
							Pab_credit_ChinaTransFlow pab_credit_ChinaTransFlow = new Pab_credit_ChinaTransFlow();
							pab_credit_ChinaTransFlow.setTaskid(bankJsonBean.getTaskid().trim());
							pab_credit_ChinaTransFlow.setRmbje(rmbje);
							pab_credit_ChinaTransFlow.setJyzy(jyzy);
							pab_credit_ChinaTransFlow.setKhmsw(khmsw);
							pab_credit_ChinaTransFlow.setJzrq(jzrq);
							pab_credit_ChinaTransFlow.setJysj(jysj);
							pabChinaTransFlowRepository.save(pab_credit_ChinaTransFlow);
						}
					}
					taskBankStatusService.updateTaskBankTransflow(200,
							BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());

					// 卡片管理
					String loginurl4 = "https://creditcard.pingan.com.cn/financing/newccuser/listAccountforCardInfo.do?random=0.18131410963431765";
					WebRequest webRequestlogin4;
					webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.GET);
					Page pagelogin2 = webClient.getPage(webRequestlogin4);

					String contentAsString2 = pagelogin2.getWebResponse().getContentAsString();

					System.out.println("卡片管理：" + contentAsString2);
					JSONObject json = JSONObject.fromObject(contentAsString2);
					String accounts = json.getString("accounts").trim();
					JSONArray array1 = JSONArray.fromObject(accounts);
					String string = array1.get(0).toString();
					JSONObject json2 = JSONObject.fromObject(string);
					String cardSet = json2.getString("cardSet").trim();
					JSONArray array2 = JSONArray.fromObject(cardSet);
					for (int i = 0; i < array2.size(); i++) {

						Pab_credit_ChinaUserInfo pab_credit_ChinaUserInfo = new Pab_credit_ChinaUserInfo();
						pab_credit_ChinaUserInfo.setTaskid(bankJsonBean.getTaskid().trim());
						String string2 = array2.get(i).toString();
						JSONObject json3 = JSONObject.fromObject(string2);
						// 卡号
						String kh = json3.getString("cardNo").trim();
						System.out.println("卡号-----" + kh);
						pab_credit_ChinaUserInfo.setKh(kh);
						// 卡种
						String kz = json3.getString("description").trim();
						System.out.println("卡种-----" + kz);
						pab_credit_ChinaUserInfo.setKz(kz);
						// 卡面
						String km = json3.getString("cardFaceDescription").trim();
						System.out.println("卡面-----" + km);
						pab_credit_ChinaUserInfo.setKm(km);

						Thread.sleep(2000);

						// 获取姓名
						String loginurl3 = "https://creditcard.pingan.com.cn/financing/newccuser/getSetAccountControllerRest.do";
						WebRequest webRequestlogin3;
						webRequestlogin3 = new WebRequest(new URL(loginurl3), HttpMethod.GET);
						Page pagelogin = webClient.getPage(webRequestlogin3);
						String contentAsString = pagelogin.getWebResponse().getContentAsString();

						JSONObject json4 = JSONObject.fromObject(contentAsString);
						String CUSTOMER = json4.getString("CUSTOMER").trim();
						JSONObject json42 = JSONObject.fromObject(CUSTOMER);
						// 姓名
						String xm = json42.getString("userName").trim();
						System.out.println("姓名-----" + xm);
						pab_credit_ChinaUserInfo.setXm(xm);
						Thread.sleep(2000);

						// 账户信息
						String loginurl31 = "https://creditcard.pingan.com.cn/financing/newccuser/resetShowIframefirstView.do?0.9530269105697611";
						WebRequest webRequestlogin31;
						webRequestlogin31 = new WebRequest(new URL(loginurl31), HttpMethod.GET);
						Page pagelogin1 = webClient.getPage(webRequestlogin31);

						String contentAsString1 = pagelogin1.getWebResponse().getContentAsString();

						JSONObject json41 = JSONObject.fromObject(contentAsString1);
						String SERVICE_RESPONSE_RESULT = json41.getString("SERVICE_RESPONSE_RESULT").trim();
						JSONObject json5 = JSONObject.fromObject(SERVICE_RESPONSE_RESULT);
						// 每月账单日
						String zdr = json5.getString("settleDate").trim();
						System.out.println("每月账单日-----" + zdr);
						pab_credit_ChinaUserInfo.setZdr(zdr);
						// 信用额度
						String xyed = json5.getString("limit").trim();
						System.out.println("信用额度 -----" + xyed);
						pab_credit_ChinaUserInfo.setXyed(xyed);
						Thread.sleep(2000);

						// 个人信息管理
						String loginurl5 = "https://creditcard.pingan.com.cn/financing/newccuser/showConnectInfoRest.do?652488.0073194891&emailAddr=&newZoneNO=&newTelNO=&oldZoneNO=&oldTelNO=&oldTelNOZoneNO=&empPostCode=&oldEmpPostCode=";
						WebRequest webRequestlogin5;
						webRequestlogin5 = new WebRequest(new URL(loginurl5), HttpMethod.GET);
						Page pagelogin3 = webClient.getPage(webRequestlogin5);

						String contentAsString3 = pagelogin3.getWebResponse().getContentAsString();

						System.out.println("个人信息管理：" + contentAsString3);

						JSONObject json6 = JSONObject.fromObject(contentAsString3);
						String contactInfoDTO = json6.getString("contactInfoDTO").trim();
						JSONObject json7 = JSONObject.fromObject(contactInfoDTO);
						// 电子邮箱
						String dzyx = json7.getString("emailAddr").trim();
						System.out.println("电子邮箱-----" + dzyx);
						pab_credit_ChinaUserInfo.setDzyx(dzyx);
						// 住宅地址
						String zzdz = json7.getString("homeAddr").trim();
						System.out.println("住宅地址-----" + zzdz);
						pab_credit_ChinaUserInfo.setZzdz(zzdz);
						// 单位电话
						String dwdh = json7.getString("corpPhoneNo").trim();
						System.out.println("单位电话-----" + dwdh);
						pab_credit_ChinaUserInfo.setDwdh(dwdh);
						// 单位地址
						String dwdz = json7.getString("empAddr").trim();
						System.out.println("单位地址-----" + dwdz);
						pab_credit_ChinaUserInfo.setDwdz(dwdz);
						// 账单地址
						String zddz = json7.getString("billAddress1").trim();
						System.out.println("账单地址-----" + zddz);
						pab_credit_ChinaUserInfo.setZddz(zddz);

						String loginurl4q111 = "https://www.pingan.com.cn/pinganone/pa/newFace.do";
						WebRequest webRequestlogin4q111;
						webRequestlogin4q111 = new WebRequest(new URL(loginurl4q111), HttpMethod.GET);
						HtmlPage pagelogin2q111 = webClient.getPage(webRequestlogin4q111);

						DomElement elementById1 = pagelogin2q111.getElementById("birthDate");
						String csrq = elementById1.getAttribute("value").trim();
						System.out.println("出生日期---" + csrq);
						pab_credit_ChinaUserInfo.setCsrq(csrq);

						pabChinaUserInfoRepository.save(pab_credit_ChinaUserInfo);
					}
					taskBankStatusService.updateTaskBankUserinfo(200,
							BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());

				} else {
					System.out.println("不是手机号登录的情况！");

					JSONObject zdjson = JSONObject.fromObject(contentAsString2q111);
					String sumbilldub = zdjson.getString("ACCOUNT_SUMMARY_BILL").trim();
					JSONObject zdjson2 = JSONObject.fromObject(sumbilldub);
					String payoffDetail = zdjson2.getString("payoffDetail").trim();
					JSONArray array = JSONArray.fromObject(payoffDetail);
					String object = array.get(0).toString();
					JSONObject zdjson3 = JSONObject.fromObject(object);
					String summaryRecords = zdjson3.getString("summaryRecords").trim();
					JSONArray summaryRecordsarray = JSONArray.fromObject(summaryRecords);
					for (int i = 0; i < summaryRecordsarray.size(); i++) {
						String string = summaryRecordsarray.get(i).toString();
						JSONObject zdjson21 = JSONObject.fromObject(string);
						// 账单月份
						String zdyf = zdjson21.getString("month").trim();
						System.out.println("账单月份-----" + zdyf);
						// 人民币
						String rmb = zdjson21.getString("currentPaymentDue").trim();
						System.out.println("人民币-----" + rmb);
						// 美元
						String my = zdjson21.getString("dollarcurrentPaymentDue").trim();
						System.out.println("美元-----" + my);

						Pab_credit_ChinaAccountType pab_credit_ChinaAccountType = new Pab_credit_ChinaAccountType();
						pab_credit_ChinaAccountType.setTaskid(bankJsonBean.getTaskid().trim());
						pab_credit_ChinaAccountType.setMy(my);
						pab_credit_ChinaAccountType.setRmb(rmb);
						pab_credit_ChinaAccountType.setZdyf(zdyf);
						pabChinaAccountTypeRepository.save(pab_credit_ChinaAccountType);

						// 明细入参1
						String mxrc = zdjson21.getString("maskQryAccountNo").trim();
						System.out.println("明细入参1-----" + mxrc);
						// 明细入参2
						String mxrc2 = zdjson21.getString("qryAccountIndex").trim();
						System.out.println("明细入参2-----" + mxrc2);
						// 明细信息
						String loginurl4qm = "https://creditcard.pingan.com.cn/financing/newccuser/printHistoryBillPayoffDetail.do?0.765770673229819&accountNo="
								+ mxrc + "&accountSign=2&accountNoIndex=" + mxrc2 + "&queryMonth=" + zdyf
								+ "&acountType=0&currentPage=1&defaultBillDate=" + zdyf + "";
						WebRequest webRequestlogin4qm;
						webRequestlogin4qm = new WebRequest(new URL(loginurl4qm), HttpMethod.GET);
						Page pagelogin2qm = webClient.getPage(webRequestlogin4qm);
						String contentAsString2qm = pagelogin2qm.getWebResponse().getContentAsString();
						System.out.println("明细信息-----" + contentAsString2qm);

						JSONObject jsonmx = JSONObject.fromObject(contentAsString2qm);
						String payoffDetailDto = jsonmx.getString("payoffDetailDto").toString().trim();
						JSONObject jsonmx2 = JSONObject.fromObject(payoffDetailDto);
						String currentPayRecords = jsonmx2.getString("currentPayRecords").toString().trim();
						JSONArray arraymx = JSONArray.fromObject(currentPayRecords);
						for (int j = 0; j < arraymx.size(); j++) {
							String trim = arraymx.get(j).toString().trim();
							JSONObject jsonmx3 = JSONObject.fromObject(trim);
							// 交易时间
							String jysj = jsonmx3.getString("txnDate").trim();
							System.out.println("交易时间-----" + jysj);
							// 记账日期
							String jzrq = jsonmx3.getString("settleDate").trim();
							System.out.println("记账日期-----" + jzrq);
							// 卡号末四位
							String khmsw = jsonmx3.getString("cardNo").trim();
							System.out.println("卡号末四位-----" + khmsw);
							// 交易摘要
							String jyzy = jsonmx3.getString("txnDescTxt").trim();
							System.out.println("交易摘要-----" + jyzy);
							// 人民币金额
							String rmbje = jsonmx3.getString("txnAmount").trim();
							System.out.println("人民币金额-----" + rmbje);
							Pab_credit_ChinaTransFlow pab_credit_ChinaTransFlow = new Pab_credit_ChinaTransFlow();
							pab_credit_ChinaTransFlow.setTaskid(bankJsonBean.getTaskid().trim());
							pab_credit_ChinaTransFlow.setRmbje(rmbje);
							pab_credit_ChinaTransFlow.setJyzy(jyzy);
							pab_credit_ChinaTransFlow.setKhmsw(khmsw);
							pab_credit_ChinaTransFlow.setJzrq(jzrq);
							pab_credit_ChinaTransFlow.setJysj(jysj);
							pabChinaTransFlowRepository.save(pab_credit_ChinaTransFlow);
						}
					}
					taskBankStatusService.updateTaskBankTransflow(200,
							BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());

					// 卡片管理
					String loginurl4 = "https://creditcard.pingan.com.cn/financing/newccuser/listAccountforCardInfo.do?random=0.18131410963431765";
					WebRequest webRequestlogin4;
					webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.GET);
					Page pagelogin2 = webClient.getPage(webRequestlogin4);

					String contentAsString2 = pagelogin2.getWebResponse().getContentAsString();

					System.out.println("卡片管理：" + contentAsString2);
					JSONObject json = JSONObject.fromObject(contentAsString2);
					String accounts = json.getString("accounts").trim();
					JSONArray array1 = JSONArray.fromObject(accounts);
					String string = array1.get(0).toString();
					JSONObject json2 = JSONObject.fromObject(string);
					String cardSet = json2.getString("cardSet").trim();
					JSONArray array2 = JSONArray.fromObject(cardSet);
					for (int i = 0; i < array2.size(); i++) {

						Pab_credit_ChinaUserInfo pab_credit_ChinaUserInfo = new Pab_credit_ChinaUserInfo();
						pab_credit_ChinaUserInfo.setTaskid(bankJsonBean.getTaskid().trim());
						String string2 = array2.get(i).toString();
						JSONObject json3 = JSONObject.fromObject(string2);
						// 卡号
						String kh = json3.getString("cardNo").trim();
						System.out.println("卡号-----" + kh);
						pab_credit_ChinaUserInfo.setKh(kh);
						// 卡种
						String kz = json3.getString("description").trim();
						System.out.println("卡种-----" + kz);
						pab_credit_ChinaUserInfo.setKz(kz);
						// 卡面
						String km = json3.getString("cardFaceDescription").trim();
						System.out.println("卡面-----" + km);
						pab_credit_ChinaUserInfo.setKm(km);

						Thread.sleep(2000);

						// 获取姓名
						String loginurl3 = "https://creditcard.pingan.com.cn/financing/newccuser/getSetAccountControllerRest.do";
						WebRequest webRequestlogin3;
						webRequestlogin3 = new WebRequest(new URL(loginurl3), HttpMethod.GET);
						Page pagelogin = webClient.getPage(webRequestlogin3);
						String contentAsString = pagelogin.getWebResponse().getContentAsString();

						JSONObject json4 = JSONObject.fromObject(contentAsString);
						String CUSTOMER = json4.getString("CUSTOMER").trim();
						JSONObject json42 = JSONObject.fromObject(CUSTOMER);
						// 姓名
						String xm = json42.getString("userName").trim();
						System.out.println("姓名-----" + xm);
						pab_credit_ChinaUserInfo.setXm(xm);
						Thread.sleep(2000);

						// 账户信息
						String loginurl31 = "https://creditcard.pingan.com.cn/financing/newccuser/resetShowIframefirstView.do?0.9530269105697611";
						WebRequest webRequestlogin31;
						webRequestlogin31 = new WebRequest(new URL(loginurl31), HttpMethod.GET);
						Page pagelogin1 = webClient.getPage(webRequestlogin31);

						String contentAsString1 = pagelogin1.getWebResponse().getContentAsString();

						JSONObject json41 = JSONObject.fromObject(contentAsString1);
						String SERVICE_RESPONSE_RESULT = json41.getString("SERVICE_RESPONSE_RESULT").trim();
						JSONObject json5 = JSONObject.fromObject(SERVICE_RESPONSE_RESULT);
						// 每月账单日
						String zdr = json5.getString("settleDate").trim();
						System.out.println("每月账单日-----" + zdr);
						pab_credit_ChinaUserInfo.setZdr(zdr);
						// 信用额度
						String xyed = json5.getString("limit").trim();
						System.out.println("信用额度 -----" + xyed);
						pab_credit_ChinaUserInfo.setXyed(xyed);
						Thread.sleep(2000);

						// 个人信息管理
						String loginurl5 = "https://creditcard.pingan.com.cn/financing/newccuser/showConnectInfoRest.do?652488.0073194891&emailAddr=&newZoneNO=&newTelNO=&oldZoneNO=&oldTelNO=&oldTelNOZoneNO=&empPostCode=&oldEmpPostCode=";
						WebRequest webRequestlogin5;
						webRequestlogin5 = new WebRequest(new URL(loginurl5), HttpMethod.GET);
						Page pagelogin3 = webClient.getPage(webRequestlogin5);

						String contentAsString3 = pagelogin3.getWebResponse().getContentAsString();

						System.out.println("个人信息管理：" + contentAsString3);

						JSONObject json6 = JSONObject.fromObject(contentAsString3);
						String contactInfoDTO = json6.getString("contactInfoDTO").trim();
						JSONObject json7 = JSONObject.fromObject(contactInfoDTO);
						// 电子邮箱
						String dzyx = json7.getString("emailAddr").trim();
						System.out.println("电子邮箱-----" + dzyx);
						pab_credit_ChinaUserInfo.setDzyx(dzyx);
						// 住宅地址
						String zzdz = json7.getString("homeAddr").trim();
						System.out.println("住宅地址-----" + zzdz);
						pab_credit_ChinaUserInfo.setZzdz(zzdz);
						// 单位电话
						String dwdh = json7.getString("corpPhoneNo").trim();
						System.out.println("单位电话-----" + dwdh);
						pab_credit_ChinaUserInfo.setDwdh(dwdh);
						// 单位地址
						String dwdz = json7.getString("empAddr").trim();
						System.out.println("单位地址-----" + dwdz);
						pab_credit_ChinaUserInfo.setDwdz(dwdz);
						// 账单地址
						String zddz = json7.getString("billAddress1").trim();
						System.out.println("账单地址-----" + zddz);
						pab_credit_ChinaUserInfo.setZddz(zddz);

						String loginurl4q11 = "https://www.pingan.com.cn/pinganone/pa/newFace.do";
						WebRequest webRequestlogin4q11;
						webRequestlogin4q11 = new WebRequest(new URL(loginurl4q11), HttpMethod.GET);
						HtmlPage pagelogin2q11 = webClient.getPage(webRequestlogin4q11);

						DomElement elementById1 = pagelogin2q11.getElementById("birthDate");
						String csrq = elementById1.getAttribute("value").trim();
						System.out.println("出生日期---" + csrq);
						pab_credit_ChinaUserInfo.setCsrq(csrq);
						pabChinaUserInfoRepository.save(pab_credit_ChinaUserInfo);
					}
					taskBankStatusService.updateTaskBankUserinfo(200,
							BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				}
			} else {
				System.out.println("没有信用卡！需要获取个人信息！");
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				tracerLog.addTag("没有信用卡！需要获取个人信息！", taskBank.getCrawlerHost());

				String loginurl4q11 = "https://www.pingan.com.cn/pinganone/pa/newFace.do";
				WebRequest webRequestlogin4q11;
				webRequestlogin4q11 = new WebRequest(new URL(loginurl4q11), HttpMethod.GET);
				HtmlPage pagelogin2q11 = webClient.getPage(webRequestlogin4q11);
				DomElement elementById = pagelogin2q11.getElementById("name");
				String xm = elementById.getAttribute("value").trim();
				System.out.println("姓名---" + xm);

				DomElement elementById1 = pagelogin2q11.getElementById("birthDate");
				String csrq = elementById1.getAttribute("value").trim();
				System.out.println("出生日期---" + csrq);

				DomElement elementById2 = pagelogin2q11.getElementById("email");
				String dzyx = elementById2.getAttribute("value").trim();
				System.out.println("电子邮箱---" + dzyx);
				Pab_credit_ChinaUserInfo pab_credit_ChinaUserInfo = new Pab_credit_ChinaUserInfo();
				pab_credit_ChinaUserInfo.setTaskid(bankJsonBean.getTaskid().trim());
				pab_credit_ChinaUserInfo.setXm(xm);
				pab_credit_ChinaUserInfo.setCsrq(csrq);
				pab_credit_ChinaUserInfo.setDzyx(dzyx);
				pabChinaUserInfoRepository.save(pab_credit_ChinaUserInfo);
				taskBankStatusService.updateTaskBankUserinfo(200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
						bankJsonBean.getTaskid());
				taskBankStatusService.updateTaskBankTransflow(200,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());

			}
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
