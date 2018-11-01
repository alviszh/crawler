package test.bank;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.hfbchina.HfbChinaDebitCardBillDetails;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import net.sf.json.JSONObject;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

public class HFBChinaTest {

	static String driverPath = "D:\\WD\\s\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://my.hfbank.com.cn/perbank/login.htm";

	public static String OCR_FILE_PATH = "D:/img";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		try {

//			WebClient webClient = WebCrawler.getInstance().getWebClient();
//			login();
			// login(webClient);
			// getUserInfo();
			// getcardInfo();
//			 getsignIn(webClient);
//			 getZH(webClient);
			// getcard("");
			// getbillDetails("");
			
			
			String yearmonth = getDateBeforeDate("yyyy-MM-dd", 179);
			String endDate = getDateBeforeDate("yyyy-MM-dd", 180);
			String beginDate = getDateBeforeDate("yyyy-MM-dd", 359);
			System.out.println("yearmonth-----"+yearmonth);
			System.out.println("endDate-----"+endDate);
			System.out.println("beginDate-----"+beginDate);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore1(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	

	public static void login() throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		String baseUrl = "https://my.hfbank.com.cn/perbank/login.htm";

		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
		// System.out.println("currentUrl--11--"+currentUrl);
		String htmlsource = driver.getPageSource();
		// System.out.println("htmlsource--11--"+htmlsource);

		if (/* StatusCodeLogin.NICK_NAME */"".equals("NICK_NAM")) {
			driver.findElement(By.id("nick_name")).sendKeys(Keys.DOWN);
		} else {
			driver.findElement(By.id("loginTab")).findElement(By.className("clearfix")).findElements(By.tagName("li"))
					.get(1).click();
			driver.findElement(By.id("id_card")).sendKeys(Keys.DOWN);
		}

		String accountNum = "340321198602178635";
		String password = "xhhj2017";

		Thread.sleep(1000L);

		VirtualKeyBoard.KeyPressEx(accountNum, 60);// 输入密码

		InputTab();

		VirtualKeyBoard.KeyPressEx(password, 60);// 输入密码

		InputTab();

		// 验证码
		String path = saveImg(driver, By.id("verifyImg"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);

		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		VirtualKeyBoard.KeyPressEx(code, 60);

		// String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");
		// VirtualKeyBoard.KeyPressEx(inputuserjymtemp, 60);

		driver.findElement(By.id("submitBtn")).click();

		Thread.sleep(3000L);

		String currentPageURL = driver.getCurrentUrl();

		// 如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
		if (LoginPage.equals(currentPageURL)) {
			String errorfinfo = driver.findElement(By.id("msg")).getText();
			if (errorfinfo != null && errorfinfo.length() > 0) {
				System.out.println("errorfinfo---------------" + errorfinfo);
			} else {
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
						.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
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
						System.out.println("errorinfodialog---------------" + errorinfodialog);
					}
				}
			}

		} else {

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

			WebElement m_userName = null;
			try {
				m_userName = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("bgmoneyClick"));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (m_userName != null) {
				String pageSource = driver.getPageSource();
				// System.out.println("pageSource-------" + pageSource);
				getUserInfo(pageSource);
			}

			// driver.findElement(By.id("showMyTotalAssetsInfo")).click();
			// Thread.sleep(1000L);
			// driver.findElement(By.id("myBankMoneyNav")).click();
			// Thread.sleep(1000L);
			// driver.findElement(By.id("letidlecashrollDetail")).click();

			JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
			Thread.sleep(1500L);
			driver_js.executeScript("queryMyRelatedCard()");
			Thread.sleep(1000L);
			driver.switchTo().frame("openIframe");

			String openIframe = driver.getPageSource();
			// System.out.println("openIframe-------" + openIframe);

			getcardInfo(openIframe);
			// Thread.sleep(1500L);

			driver.findElement(By.className("accountInfoTable")).findElements(By.tagName("button")).get(0).click();

			// driver_js.executeScript("accountDetails(this,0,738)");
			Thread.sleep(1000L);
			// driver.switchTo().frame("openIframe");
			String openIframe2 = driver.getPageSource();
//			System.out.println("openIframe2-------" + openIframe2);

			getcard(openIframe2);

			//流水
			
			driver.findElement(By.className("op-close")).click();

			driver.switchTo().defaultContent();

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
				showMyTotalAssetsInfo.click();
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
						
						String yearmonth = getDateBefore1("yyyy-MM-dd", 179);
						String endDate = getDateBefore1("yyyy-MM-dd", 180);
						String beginDate = getDateBefore1("yyyy-MM-dd", 359);
						
						driver.findElement(By.id("transType_hide1")).findElements(By.tagName("input")).get(3).click();
						driver.findElement(By.id("transType_hide1")).findElements(By.tagName("input")).get(3).click();
						driver.findElement(By.id("input_beginDate")).click();
						driver.findElement(By.id("input_beginDate")).clear();
						driver.findElement(By.id("input_beginDate")).sendKeys(yearmonth);
						driver.findElement(By.id("input_beginDate")).sendKeys(Keys.ENTER);
						detailQuery.click();
						detailQuery.click();
						Thread.sleep(4000L);
						System.out.println("开始解析");
						String pageSource1 = driver.getPageSource();
						getbillDetails(pageSource1);
						
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
							System.out.println("页数："+text);
							try {
								count = Integer.valueOf(text);
							} catch (Exception e) {
								e.printStackTrace();
							}
							for (int i = 1; i < count; i++) {
								System.out.println("点击第几页："+i);
								driver.findElement(By.id("selfDetialList")).findElement(By.className("lui_pagenav_next")).click();
								Thread.sleep(2000L);
								String pageSource2 = driver.getPageSource();
								getbillDetails(pageSource2);
							}
						}
						
						System.out.println("查询上半年");
						driver.findElement(By.id("input_beginDate")).click();
						driver.findElement(By.id("input_beginDate")).clear();
						driver.findElement(By.id("input_beginDate")).sendKeys(beginDate);
						driver.findElement(By.id("input_beginDate")).sendKeys(Keys.ENTER);

						driver.findElement(By.id("input_endDate")).click();
						driver.findElement(By.id("input_endDate")).clear();
						driver.findElement(By.id("input_endDate")).sendKeys(endDate);
						driver.findElement(By.id("input_endDate")).sendKeys(Keys.ENTER);
						detailQuery.click();
						detailQuery.click();
						Thread.sleep(4000L);
						String pageSource11 = driver.getPageSource();
						getbillDetails(pageSource11);
						
						
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
							System.out.println("页数："+text);
							try {
								count = Integer.valueOf(text);
							} catch (Exception e) {
								e.printStackTrace();
							}
							for (int i = 1; i < count; i++) {
								System.out.println("点击第几页："+i);
								driver.findElement(By.id("selfDetialList")).findElement(By.className("lui_pagenav_next")).click();
								Thread.sleep(2000L);
								String pageSource2 = driver.getPageSource();
								getbillDetails(pageSource2);
							}
						}
						
						
						

						// driver.findElement(By.id("downF")).click();

					}
				}
			}

			// String pageSource = driver.getPageSource();
			// System.out.println("pageSource-------" + pageSource);

		}

	}

	private static void getbillDetails(String jsonT) throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\zhanghu.txt");
		String json = txt2String(file);

		Document table = Jsoup.parse(jsonT);

		Element timeDepositTable = table.getElementById("selfDetialTable");
		Elements tobody = timeDepositTable.getElementsByTag("tbody");
		if (tobody.size() > 0) {
			Elements elementsByTag = tobody.get(0).getElementsByTag("tr");
			for (Element element2 : elementsByTag) {
				Elements elementsByTag2 = element2.getElementsByTag("td");
				if (elementsByTag2.size() > 7) {
					// 交易时间
					String tran_date = elementsByTag2.get(0).text();
					System.out.print("---交易时间-----------" + tran_date);
					// 交易渠道
					String tran_channel = elementsByTag2.get(1).text();
					System.out.print("---交易渠道-----------" + tran_channel);
					// 对方账号
					String opposite_card_number = elementsByTag2.get(2).text();
					System.out.print("---对方账号-----------" + opposite_card_number);
					// 对方户名
					String opposite_name = elementsByTag2.get(3).text();
					System.out.print("---对方户名-----------" + opposite_name);
					// 对方行名
					String opposite_bank_name = elementsByTag2.get(4).text();
					System.out.print("---对方行名-----------" + opposite_bank_name);
					// 收付
					String currentaccount_type = elementsByTag2.get(5).text();
					System.out.print("---收付-----------" + currentaccount_type);
					// 金额
					String money = elementsByTag2.get(6).text();
					System.out.print("---金额-----------" + money);
					// 余额
					String balance = elementsByTag2.get(7).text();
					System.out.print("---余额-----------" + balance);
					// 用途
					String purpose = elementsByTag2.get(8).text();
					System.out.println("用途-----------" + purpose);
				}
			}

		}

	}

	private static void getcard(String jsonT) throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\getcard2.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(jsonT);

		String account = doc.getElementById("account").text();
		System.out.println("活期账户--------------" + account);

		String balance = doc.getElementById("amount").text();
		System.out.println("活期余额--------------" + balance);

		String currencyType = doc.getElementById("currencyType").text();
		System.out.println("币种--------------" + currencyType);

		String accountState = doc.getElementById("accountState").text();
		System.out.println("账户状态--------------" + accountState);
		try {
			Element timeDepositTable = doc.getElementById("timeDepositTable");
			Elements tobody = timeDepositTable.getElementsByTag("tbody");
			if (tobody.size() > 0) {
				Elements elementsByTag = tobody.get(0).getElementsByTag("tr");
				for (Element element : elementsByTag) {
					Elements elementsByTag2 = element.getElementsByTag("td");
					// 子账号
					String subaccount = elementsByTag2.get(0).text();
					System.out.println("子账号-----------" + subaccount);
					// 存款本金
					String deposit = elementsByTag2.get(1).text();
					System.out.println("存款本金-----------" + deposit);
					// 币种
					String currency = elementsByTag2.get(2).text();
					System.out.println("币种-----------" + currency);
					// 存期
					String storge_period = elementsByTag2.get(3).text();
					System.out.println("存期-----------" + storge_period);
					// 存入日
					String deposit_date = elementsByTag2.get(4).text();
					System.out.println("存入日-----------" + deposit_date);
					// 到期日
					String due_date = elementsByTag2.get(5).text();
					System.out.println("到期日-----------" + due_date);
					// 存款利率
					String interest_rate = elementsByTag2.get(6).text();
					System.out.println("存款利率-----------" + interest_rate);
					// 账户状态
					String account_state = elementsByTag2.get(7).text();
					System.out.println("账户状态-----------" + account_state);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Element installmentDepositTable = doc.getElementById("installmentDepositTable");
			Elements tobody = installmentDepositTable.getElementsByTag("tbody");
			if (tobody.size() > 0) {
				Elements elementsByTag = tobody.get(0).getElementsByTag("tr");
				for (Element element : elementsByTag) {
					Elements elementsByTag2 = element.getElementsByTag("td");
					// 子账号
					String subaccount = elementsByTag2.get(0).text();
					System.out.println("子账号-----------" + subaccount);
					// 账户金额
					String account_amount = elementsByTag2.get(1).text();
					System.out.println("账户金额-----------" + account_amount);
					// 币种
					String currency = elementsByTag2.get(2).text();
					System.out.println("币种-----------" + currency);
					// 每次存取金额
					String each_access_amount = elementsByTag2.get(3).text();
					System.out.println("每次存取金额-----------" + each_access_amount);
					// 本期应存金额
					String amount_deposit = elementsByTag2.get(4).text();
					System.out.println("本期应存金额-----------" + amount_deposit);
					// 存期
					String storge_period = elementsByTag2.get(5).text();
					System.out.println("存期-----------" + storge_period);
					// 存入日
					String deposit_date = elementsByTag2.get(6).text();
					System.out.println("存入日-----------" + deposit_date);
					// 到期日
					String due_date = elementsByTag2.get(7).text();
					System.out.println("到期日-----------" + due_date);
					// 存款利率
					String interest_rate = elementsByTag2.get(8).text();
					System.out.println("存款利率-----------" + interest_rate);
					// 是否违约
					String contract = elementsByTag2.get(9).text();
					System.out.println("是否违约-----------" + contract);
					// 账户状态
					String account_state = elementsByTag2.get(10).text();
					System.out.println("账户状态-----------" + account_state);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void getcardInfo(String jsonT) throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\cardInfo.txt");
		String json = txt2String(file);

		Document table = Jsoup.parse(json);

		Elements accountInfoTable = table.getElementsByClass("accountInfoTable");
		Document doc = Jsoup.parse(accountInfoTable.toString());

		String aliases = getNextLabelByKeyword(doc, "账户别名");
		System.out.println("账户别名--------------" + aliases);

		String signingMode = getNextLabelByKeyword(doc, "签约方式");
		System.out.println("签约方式--------------" + signingMode);

		String credentialState = getNextLabelByKeyword(doc, "凭证状态");
		System.out.println("凭证状态--------------" + credentialState);

		String deposit_bank = getNextLabelByKeyword(doc, "开户行");
		System.out.println("开户行--------------" + deposit_bank);

	}

	private static void getUserInfo(String jsonT) throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\loginS.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		// 卡片持有人
		String m_userName = doc.getElementById("m_userName").text();
		System.out.println("卡片持有人--------------" + m_userName);
		if (m_userName.contains("！")) {
			m_userName = m_userName.replaceAll("！", "");
		}
		System.out.println("卡片持有人--------------" + m_userName);

		String customerMobile = doc.getElementsByClass("customerMobile").text();
		System.out.println("手机号码--------------" + customerMobile);

	}

	public static String login(WebClient webClient) throws Exception {
		try {
			String url1 = "https://my.hfbank.com.cn/perbank/custLogonInfoQuery.do?appKey=IDBPBUDNCAELDKFFBFBTFQAYHPJTGFFUEWAXAIAL&customerEnc=MjAwMDg2MTY5NXw%3D&format=JSON&channel=WEB&Math=0.04140471403521828";

			// List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
			// paramsList1.add(new NameValuePair("logonPassword",
			// "iP2GcP3FHncolgHb4izx8g%3D%3D!%40!1"));
			// paramsList1.add(new NameValuePair("netWorkInfo",
			// "HSdiw6zQyIEeXQcB9fU8%2FNf0BPqrG9Q2iSZ2mOC1jKM%3D"));
			// paramsList1.add(new NameValuePair("checkCode", "q3b4"));
			// paramsList1.add(new NameValuePair("logonType", "1"));
			// paramsList1.add(new NameValuePair("logonId",
			// "340321198602178635"));
			// paramsList1.add(new NameValuePair("format", "JSON"));
			// paramsList1.add(new NameValuePair("channel", "WEB"));
			// paramsList1.add(new NameValuePair("Math", "0.1655655335803895"));

			Map<String, String> map = new HashMap<String, String>();
			map.put("Cookie", "JSESSIONID=uPMgaUX7-HnKFYiqq25YSC5leEOGZ514nQihS9hCIjW3A9gYELUZ!1530647079");

			Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, map);
			String html = page1.getWebResponse().getContentAsString();
			System.out.println("getsignIn*****************************" + html);
			JSONObject rr = JSONObject.fromObject(html);
			JSONObject jsonObject = rr.getJSONObject("cd");
			String appKey = jsonObject.getString("appKey");
			String customerEnc = jsonObject.getString("customerEnc");
			System.out.println("appKey------" + appKey + "---------customerEnc--------" + customerEnc);
			getUser(webClient, appKey, customerEnc);
		} catch (Exception e) {
			// 2000861695|
			e.printStackTrace();
		}
		return null;
	}

	// 账户信息
	public static String getZH(WebClient webClient) throws Exception {
					   //https://my.hfbank.com.cn/perbank/subAccountQuery.do?accountNo=6230YFSZZZZZVEX5788&accountType=738&currencyType=01&appKey=EUHAFHBFHFCKGOIUGFFWDBELEJBRCHFEAXDTIIHY&customerEnc=MjAwMDg2MTY5NXw%3D&format=JSON&channel=WEB&Math=0.3608365366418168
		String url1 = "https://my.hfbank.com.cn/perbank/subAccountQuery.do?accountNo=6230YFSZZZZZVEX5788&accountType=738&currencyType=01&appKey=EUHAFHBFHFCKGOIUGFFWDBELEJBRCHFEAXDTIIHY&customerEnc=MjAwMDg2MTY5NXw%3D&format=JSON&channel=WEB&Math=0.3608365366418161";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		// paramsList1.add(new NameValuePair("ClientNo", ClientNo));
		Map<String, String> map = new HashMap<String, String>();
		
//		map.put("X-Requested-With", "XMLHttpRequest");
//		map.put("Accept", "application/json, text/javascript, */*; q=0.01");
//		map.put("Referer", "https://my.hfbank.com.cn/perbank/html/01_account/010100_accountList.htm");
//		map.put("Accept-Language", "zh-CN");
//		map.put("Accept-Encoding", "gzip, deflate");
//		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
//		map.put("Host", "my.hfbank.com.cn");
//		map.put("Connection", "Keep-Alive");
//		map.put("Cache-Control", "no-cache");
		map.put("Cookie", "JSESSIONID=0WZDcwH0WjRt25JEZK7s01LsFidTVmmizpUCVknkVikyNOPisgcW!-160895928");
		
		
		Page page1 = getPage(webClient, null, url1, HttpMethod.GET, null, null, null, map);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getUser*****************************" + html);
		return html;
	}

	public static String getUser(WebClient webClient, String appKey, String customerEnc) throws Exception {
		String url1 = "https://my.hfbank.com.cn/perbank/custLogonInfoQuery.do?appKey=" + appKey + "&customerEnc="
				+ customerEnc + "&format=JSON&channel=WEB&Math=0.6481733572023423";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		// paramsList1.add(new NameValuePair("ClientNo", ClientNo));
		// paramsList1.add(new NameValuePair("O_STMT_FLAG", "Y"));
		// paramsList1.add(new NameValuePair("IN_YYYYMM", "201711"));
		// paramsList1.add(new NameValuePair("CreditAccNo", CreditAccNo));

		Map<String, String> map = new HashMap<String, String>();
		map.put("Accept", "text/html, application/xhtml+xml, */*");
		map.put("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx");
		map.put("Accept-Language", "zh-CN");
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1;WOW64;Trident/7.0; rv:11.0) like Gecko");
		map.put("Content-Type", "application/x-www-form-urlencoded");
		map.put("Accept-Encoding", "gzip, deflate");
		map.put("Host", "pbsz.ebank.cmbchina.com");
		map.put("Connection", "Keep-Alive");
		map.put("Cache-Control", "no-cache");

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, map);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getUser*****************************" + html);
		return html;
	}

	public static String getsignIn(WebClient webClient) throws Exception {
		try {
			String url1 = "https://my.hfbank.com.cn/perbank/signIn.do?"
					+ "logonPassword=UsL%2FFHEWyOhH%2FRmGZmt06w%3D%3D!%40!1&netWorkInfo="
					+ "&checkCode=xk9q&logonType=1&logonId=340321198602178635&format=JSON&channel=WEB";
			// String inputuserjymtemp =
			// JOptionPane.showInputDialog("请输入验证码……");

			List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
//			paramsList1.add(new NameValuePair("logonPassword", "iP2GcP3FHncolgHb4izx8g%3D%3D!%40!1"));
//			paramsList1.add(new NameValuePair("netWorkInfo", "HSdiw6zQyIEeXQcB9fU8%2FNf0BPqrG9Q2iSZ2mOC1jKM%3D"));
////			paramsList1.add(new NameValuePair("checkCode", inputuserjymtemp));
//			paramsList1.add(new NameValuePair("logonType", "1"));
//			paramsList1.add(new NameValuePair("logonId", "340321198602178635"));
//			paramsList1.add(new NameValuePair("format", "JSON"));
//			paramsList1.add(new NameValuePair("channel", "WEB"));
			// paramsList1.add(new NameValuePair("Math", "0.1655655335803895"));
			Map<String, String> map = new HashMap<String, String>();
//			map.put("Accept", "text/html, application/xhtml+xml, */*");
//			map.put("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx");
//			map.put("Accept-Language", "zh-CN");
//			map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1;WOW64;Trident/7.0; rv:11.0) like Gecko");
//			map.put("Content-Type", "application/x-www-form-urlencoded");
//			map.put("Accept-Encoding", "gzip, deflate");
//			map.put("Host", "pbsz.ebank.cmbchina.com");
//			map.put("Connection", "Keep-Alive");
//			map.put("Cache-Control", "no-cache");
			
			map.put("Cookie", "JSESSIONID=kF1ELDtPp2uTOumxT4c302Rcdguez_yxDgq5539jPs-blztjFyJ6!558304631");
			Page page1 = getPage(webClient, null, url1, HttpMethod.GET, null, null, null, map);
			String html = page1.getWebResponse().getContentAsString();
			System.out.println("getsignIn*****************************" + html);
			JSONObject rr = JSONObject.fromObject(html);
			JSONObject jsonObject = rr.getJSONObject("cd");
			String appKey = jsonObject.getString("appKey");
			String customerEnc = jsonObject.getString("customerEnc");
			System.out.println("appKey------" + appKey + "---------customerEnc--------" + customerEnc);
//			getUser(webClient, appKey, customerEnc);
		} catch (Exception e) {
			// 2000861695|
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getImg() {
		String url = "https://my.hfbank.com.cn/perbank/VerifyImage";
		
		Connection con = Jsoup.connect(url).header("Accept","image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5")
					.header("Accept-Encoding", "gzip, deflate")
					.header("Accept-Language", "zh-CN")
					.header("Cache-Control", "no-cache")
					.header("Connection", "Keep-Alive")
					.header("Host", "my.hfbank.com.cn")
					.header("Referer", "https://my.hfbank.com.cn/perbank/login.htm")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		
		try {
			Response response = con.ignoreContentType(true).execute();
			File file = new File("D:\\img\\111..jpg");
			String imgagePath = file.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
			
			out.write(response.bodyAsBytes()); 
			out.close();
			return imgagePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
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
	public static Page getPage(WebClient webClient, String taskid, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		// tracerLog.output("CmbChinaService.getPage---url:", url + "---taskId:"
		// + taskid);
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
		// tracerLog.output("CmbChinaService.getPage.statusCode:" + statusCode,
		// url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
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
	
	
	public static String getDateBeforeDate(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type, String body) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	// 根据dom节点截取图片
	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		WebElement ele = driver.findElement(selector);
		Point point = ele.getLocation();
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	public static File getImageLocalPath() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

}
