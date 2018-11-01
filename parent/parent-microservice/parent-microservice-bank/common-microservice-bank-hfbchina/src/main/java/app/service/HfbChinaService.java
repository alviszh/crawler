package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
import com.crawler.mobile.json.StatusCodeLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.hfbchina.HfbChinaDebitCardHtml;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.hfbchina.HfbChinaDebitCardHtmlRepository;
import com.module.ddxoft.VK;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.hfbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.hfbchina" })
public class HfbChinaService extends WebDriverIEService implements ICrawlerLogin{

	@Autowired
	private HfbChinaDebitCardHtmlRepository hfbChinaDebitCardHtmlRepository;

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

	static String GenIndex = "https://my.hfbank.com.cn/perbank/html/main.htm";

	static String LoginPage = "https://my.hfbank.com.cn/perbank/login.htm";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	/**
	 * @Des 打开恒丰登录页面
	 * @param 无
	 */
	public WebDriver openloginCmbChina() {
		driver = webDriverIEService.getNewWebDriver();
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "开始登陆恒丰银行登陆页");
		try {
			driver = getPage(driver, LoginPage);
		} catch (NoSuchWindowException e) {
			tracerLog.addTag("打开恒丰登录页面报错，尝试重新初始化游览器", e.getMessage());
			driver = getPage(driver, LoginPage);
		}
		tracerLog.addTag("WebDriverIEService loginCmbChina Msg", "恒丰银行登陆页加载已完成,当前页面句柄" + driver.getWindowHandle());
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
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
						.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				// 个人信息
				String pageSource = driver.getPageSource();

				// 账户查询
				JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
				Thread.sleep(1500L);
				//执行查看关联我的银行卡
				driver_js.executeScript("queryMyRelatedCard()");
				Thread.sleep(1000L);
				driver.switchTo().frame("openIframe");
				String openIframe ="";
				String openIframe2 = "";
				//账户明细按钮显示出来再获取页面
				WebElement accountInfoTable = null;
				try {
					accountInfoTable = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.className("accountInfoTable"));
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (accountInfoTable != null) {
					openIframe = driver.getPageSource();
					tracerLog.addTag("点击账户明细按钮", "");
					// 账户明细
					driver.findElement(By.className("accountInfoTable")).findElements(By.tagName("button")).get(0).click();
					
					//活期账户显示出来再获取页面
//					WebElement account = null;
//					try {
//						account = wait.until(new Function<WebDriver, WebElement>() {
//							public WebElement apply(WebDriver driver) {
//								return driver.findElement(By.id("account"));
//							}
//						});
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					if (account != null) {
						Thread.sleep(3000L);
						openIframe2 = driver.getPageSource();
						tracerLog.addTag("点击账户明细按钮", openIframe2);
//					}
				}
				
				parserService.userInfoParser(pageSource, openIframe, openIframe2, bankJsonBean.getTaskid());
				saveHtml(bankJsonBean.getTaskid(), "hfbchina_debitcard_userinfo---hfbchina_debitcard_deposit", "1");

				//交易流水
				driver.findElement(By.className("op-close")).click();
				driver.switchTo().defaultContent();
				//点击我的总资产详情
				WebElement showMyTotalAssetsInfo = null;
				try {
					showMyTotalAssetsInfo = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("showMyTotalAssetsInfo"));
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (showMyTotalAssetsInfo != null) {
					driver.findElement(By.id("showMyTotalAssetsInfo")).click();
					//银行卡明细
					driver_js.executeScript("letidlecashrollDetail(0)");

					driver.switchTo().frame("openIframe");
					WebElement detailQuery = null;
					try {
						detailQuery = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(By.id("detailQuery"));
							}
						});

					} catch (Exception e) {
						e.printStackTrace();
					}
					if (detailQuery != null) {
						WebElement transType_hide1 = null;
						try {
							transType_hide1 = wait.until(new Function<WebDriver, WebElement>() {
								public WebElement apply(WebDriver driver) {
									return driver.findElement(By.id("transType_hide1"));
								}
							});

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (transType_hide1 != null) {
							//每次只能查询180天，总共可查询一年数据，此代码查询360天
							String yearmonth = getDateBefore("yyyy-MM-dd", 179);
							String endDate = getDateBefore("yyyy-MM-dd", 180);
							String beginDate = getDateBefore("yyyy-MM-dd", 359);
							tracerLog.addTag("yearmonth-----",yearmonth);
							tracerLog.addTag("endDate-----",endDate);
							tracerLog.addTag("beginDate-----",beginDate);
							//点击自定义查询时间按钮，并不是此处要双击而是它一次click有时候不灵
							driver.findElement(By.id("transType_hide1")).findElements(By.tagName("input")).get(3).click();
							driver.findElement(By.id("transType_hide1")).findElements(By.tagName("input")).get(3).click();
							driver.findElement(By.id("input_beginDate")).click();
							driver.findElement(By.id("input_beginDate")).clear();
							driver.findElement(By.id("input_beginDate")).sendKeys(yearmonth);
							Thread.sleep(500L);
							driver.findElement(By.id("input_endDate")).click();
//							driver.findElement(By.id("input_beginDate")).sendKeys(Keys.ENTER);
							//点击查询按钮，并不是此处要双击而是它一次click有时候不灵
//							driver.findElement(By.id("transType_hide1")).findElements(By.tagName("input")).get(3).click();
							driver.findElement(By.id("detailQuery")).click();
							driver.findElement(By.id("detailQuery")).click();
							Thread.sleep(3000L);
							String pageSource1 = driver.getPageSource();
							parserService.billDetailsParser(pageSource1, bankJsonBean.getTaskid());
							saveHtml(bankJsonBean.getTaskid(), "hfbchina_debitcard_billdetails", beginDate+"到"+endDate+"页数：1");
							
							WebElement lui_pagenav_pagetotal = null;
							try {
								lui_pagenav_pagetotal = wait.until(new Function<WebDriver, WebElement>() {
									public WebElement apply(WebDriver driver) {
										return driver.findElement(By.className("lui_pagenav_pagetotal"));
									}
								});

							} catch (Exception e) {
								e.printStackTrace();
							}
							if (lui_pagenav_pagetotal != null) {
								int count = 0;
								String text = driver.findElement(By.className("lui_pagenav_pagetotal")).getText();
								tracerLog.addTag("页数：",text);
								try {
									count = Integer.valueOf(text);
								} catch (Exception e) {
									e.printStackTrace();
								}
								for (int i = 1; i < count; i++) {
									tracerLog.addTag("点击第几页：",i+"");
									driver.findElement(By.id("selfDetialList")).findElement(By.className("lui_pagenav_next")).click();
									Thread.sleep(1000L);
									String pageSource2 = driver.getPageSource();
									parserService.billDetailsParser(pageSource2, bankJsonBean.getTaskid());
									saveHtml(bankJsonBean.getTaskid(), "hfbchina_debitcard_billdetails", beginDate+"到"+endDate+"页数："+(i+1));
									
								}
							}
							
							tracerLog.addTag("查询上半年",bankJsonBean.getTaskid());
							driver.findElement(By.id("input_beginDate")).click();
							driver.findElement(By.id("input_beginDate")).clear();
							driver.findElement(By.id("input_beginDate")).sendKeys(beginDate);
							Thread.sleep(500L);
//							driver.findElement(By.id("input_beginDate")).sendKeys(Keys.ENTER);

							driver.findElement(By.id("input_endDate")).click();
							driver.findElement(By.id("input_endDate")).clear();
							driver.findElement(By.id("input_endDate")).sendKeys(endDate);
							Thread.sleep(500L);
//							driver.findElement(By.id("input_endDate")).sendKeys(Keys.ENTER);
							//点击查询按钮，并不是此处要双击而是它一次click有时候不灵
//							driver.findElement(By.id("transType_hide1")).findElements(By.tagName("input")).get(3).click();
							driver.findElement(By.id("detailQuery")).click();
							driver.findElement(By.id("detailQuery")).click();
							Thread.sleep(3000L);
							String pageSource11 = driver.getPageSource();
							parserService.billDetailsParser(pageSource11, bankJsonBean.getTaskid());
							saveHtml(bankJsonBean.getTaskid(), "hfbchina_debitcard_billdetails", beginDate+"到"+endDate+"页数：1");
							
							WebElement lui_pagenav_pagetotal1 = null;
							try {
								lui_pagenav_pagetotal1 = wait.until(new Function<WebDriver, WebElement>() {
									public WebElement apply(WebDriver driver) {
										return driver.findElement(By.className("lui_pagenav_pagetotal"));
									}
								});

							} catch (Exception e) {
								e.printStackTrace();
							}
							if (lui_pagenav_pagetotal1 != null) {
								int count = 0;
								String text = driver.findElement(By.className("lui_pagenav_pagetotal")).getText();
								tracerLog.addTag("查询上半年页数：",text);
								try {
									count = Integer.valueOf(text);
								} catch (Exception e) {
									e.printStackTrace();
								}
								for (int i = 1; i < count; i++) {
									tracerLog.addTag("查询上半年点击第几页：",i+"");
									driver.findElement(By.id("selfDetialList")).findElement(By.className("lui_pagenav_next")).click();
									Thread.sleep(1000L);
									String pageSource2 = driver.getPageSource();
									parserService.billDetailsParser(pageSource2, bankJsonBean.getTaskid());
									saveHtml(bankJsonBean.getTaskid(), "hfbchina_debitcard_billdetails", beginDate+"到"+endDate+"页数："+(i+1));
								}
							}
						}
					}
				}
				
				taskBank = taskBankStatusService.updateTaskBankTransflow(200,
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
			}
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			return taskBank;
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.getDateCombo---ERROR:", e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(404, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(),
					bankJsonBean.getTaskid());
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
		}
		return null;
	}

	/**
	 * @Des 恒丰银行通过银行卡号登录
	 * @param bankJsonBean
	 */
	public TaskBank loginCombo(BankJsonBean bankJsonBean, int count) throws Exception {
		tracerLog.addTag("loginCombo", "开始登陆恒丰银行" + bankJsonBean.getLoginName());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			// 打开IE游览器，访问恒丰行的登录页面
			WebDriver webDriver = openloginCmbChina();
			String windowHandle = webDriver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracerLog.addTag("登录恒丰银行，打开网页获取网页handler", windowHandle);

			if (StatusCodeLogin.NICK_NAME.equals(bankJsonBean.getLoginType())) {
				webDriver.findElement(By.id("nick_name")).sendKeys(Keys.DOWN);
			} else {
				webDriver.findElement(By.id("loginTab")).findElement(By.className("clearfix"))
						.findElements(By.tagName("li")).get(1).click();
				webDriver.findElement(By.id("id_card")).sendKeys(Keys.DOWN);
			}

			tracerLog.addTag("开始输入卡号", bankJsonBean.getLoginName());
			// 键盘输入卡号
			Thread.sleep(500L);
			
			driver.findElement(By.id("id_card")).sendKeys(bankJsonBean.getLoginName());
			
			// 键盘输入Tab，让游览器焦点切换到密码框
			Thread.sleep(500L);
			driver.findElement(By.id("_password")).sendKeys(Keys.DOWN);
			tracerLog.addTag("开始输入密码", bankJsonBean.getPassword());
			// 键盘输入查询密码
			Thread.sleep(500L);
			VK.KeyPress(bankJsonBean.getPassword());

			// 验证码
			String pathcode = WebDriverUnit.saveImg(webDriver, By.id("verifyImg"));
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					pathcode);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			tracerLog.addTag("验证码code ====>>", code);
			driver.findElement(By.id("verify_code")).sendKeys(code);

			tracerLog.addTag("开始点击登陆按钮", "#LoginBtn");
			// 点击游览器的登录按钮
			Thread.sleep(500L);
			webDriver.findElement(By.id("submitBtn")).click();

			// 等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识form#HomePage(无须短信验证) 则表示登录失败
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement bgmoneyClick = null;
			// 截图
			String path = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
			try {
				bgmoneyClick = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("bgmoneyClick")); // 这是我的账户隐藏金额，以此来判定是否登录进去
					}
				});
			} catch (Exception e) {
				tracerLog.addTag("#bgmoneyClick 元素等待10秒未出现,登录登录失败,异常:" + e.toString(), "截图:" + path);
			}

			String currentPageURL = webDriver.getCurrentUrl();
			tracerLog.addTag("当前页面URL", currentPageURL);
			if (bgmoneyClick == null) {
				tracerLog.addTag("登录失败", currentPageURL);
				// 如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
				if (LoginPage.equals(currentPageURL)) {
					tracerLog.addTag("登录失败.当前页面仍是登录页面", currentPageURL);
					String errorfinfo = driver.findElement(By.id("msg")).getText();

					if (errorfinfo != null && errorfinfo.length() > 0) {
						tracerLog.addTag("登录失败,错误信息", errorfinfo);
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

						WebElement dialog = null;
						try {
							dialog = wait.until(new Function<WebDriver, WebElement>() {
								public WebElement apply(WebDriver driver) {
									return driver.findElement(By.className("ui-dialog-content"));
								}
							});

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (dialog != null) {
							String errorinfodialog = driver.findElement(By.className("ui-dialog-content")).getText();
							if (errorinfodialog != null && errorinfodialog.length() > 0) {
								tracerLog.addTag("登录失败,错误信息", errorinfodialog);
								//验证码错误，请重新输入！
								if (errorinfodialog.contains("验证码错误") && count < 3) {
									driver.quit();
									loginCombo(bankJsonBean, ++count);
									return null;
								} else{
									if(errorinfodialog.contains("验证码错误")){
										errorinfodialog = "系统超时,请稍后再试";
									}
									taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
											BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
											BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), errorinfodialog,
											BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
											windowHandle);
									// 登录错误，释放资源
									// 释放instance ip ，quit webdriver
									tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
									agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
									return taskBank;
								}
								
							}
						}
						// 截图
						String pathE = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
						tracerLog.addTag("登录失败，且没有登录失败错误信息", "截图:" + pathE + " 当前页面URL:" + webDriver.getCurrentUrl());
						taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
								BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
								windowHandle);
						// 释放instance ip ，quit webdriver
						tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					}
				}
			} else {
				tracerLog.addTag("登录成功", currentPageURL);
				tracerLog.addTag("登录成功 ，登录恒丰银行，登录成功网页handler", webDriver.getWindowHandle());
				// 截图
				String pathS = WebDriverUnit.saveScreenshotByPath(webDriver, this.getClass());
				
				if (currentPageURL.equals(GenIndex)) {
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false, bankJsonBean.getTaskid(),
							windowHandle);

					tracerLog.addTag("登录成功", "登录成功:" + currentPageURL);
					tracerLog.addTag("登录成功", "登录成功截图:" + pathS);

				} else {
					// TODO 设个地方需要用seleunim 截图并保持url和html，可能是未处理的情况（账号登录成功后，例如
					// 还未绑定银行卡、还未设置安全提问、还未认证等等）
					tracerLog.addTag("登录遇到了未知的链接", "未知的链接" + currentPageURL);
					tracerLog.addTag("登录遇到了未知的链接图片截图", pathS);

					// 释放instance ip ，quit webdriver
					tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(
					BankStatusCode.BANK_LOGIN_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), "登录失败，请稍后再试！",
					BankStatusCode.BANK_LOGIN_ERROR.getError_code(), true, bankJsonBean.getTaskid(),
					"");
			tracerLog.addTag("登录遇到了未知报错", "MSG:" + e.getMessage());
			// 释放instance ip ，quit webdriver
			tracerLog.addTag("释放instance ip ，quit webdriver", taskBank.getCrawlerHost());
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

	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
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
			HfbChinaDebitCardHtml cmbChinaCreditCardHtml = new HfbChinaDebitCardHtml(taskid, type, pageCount,
					driver.getCurrentUrl(), driver.getPageSource());
			hfbChinaDebitCardHtmlRepository.save(cmbChinaCreditCardHtml);
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

	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		try {
			loginCombo(bankJsonBean,0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("CmbChinaService.login---ERROR:", e.toString());
		}
		return null;
	}

}
