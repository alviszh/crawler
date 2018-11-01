package test.bank;

import java.awt.Robot;
import java.awt.event.KeyEvent;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
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
import org.openqa.selenium.support.ui.ExpectedCondition;
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

public class PsbcChinaTest {

	static String driverPath = "D:\\WD\\s\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://pbank.psbc.com/pweb/prelogin.do?_locale=zh_CN&BankId=9999";

	public static String OCR_FILE_PATH = "D:/img";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		try {
			String cookieString = "[{\"domain\":\"pbank.psbc.com\",\"key\":\"JSESSIONID\",\"value\":\"MP1khsFKdmh87nMv152RhvTcQGhW0cPYvKqypsCxQLnSSsFh96Qg!189407613\"},{\"domain\":\"pbank.psbc.com\",\"key\":\"BIGipServerwangyin_443_pool\",\"value\":\"708618432.20480.0000\"}]";
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient = addcookie(cookieString);
			login();
//			 getUser(webClient);
//			 getlogin(webClient);
			// getliushui(webClient);
			// zhanghu();
			// chaxun();
			// liushui();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void liushui() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\liushui.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		Elements trValue = doc.getElementsByClass("trValue");

		for (Element element : trValue) {
			Elements tdValue = element.getElementsByTag("td");
			if (tdValue.size() > 4) {
				// 流水号
				String serial_number = tdValue.get(0).text();
				System.out.println("流水号----" + serial_number);
				// 交易日期
				String tran_date = tdValue.get(1).text();
				System.out.println("交易日期----" + tran_date);
				// 摘要
				String trans_decription = tdValue.get(2).text();
				System.out.println("摘要----" + trans_decription);
				// 交易金额
				String money = tdValue.get(3).text();
				System.out.println("交易金额----" + money);
				// 账户余额
				String balance = tdValue.get(4).text();
				System.out.println("账户余额----" + balance);
			}
			System.out.println("----------------------------------");
		}

	}

	public static void chaxun() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\chaxun.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		// count
		String count = doc.getElementById("count").val();
		System.out.println("count----" + count);
		// AcNo
		String AcNo = doc.getElementById("AcNo").val();
		System.out.println("AcNo----" + AcNo);
		// BankAcType
		String BankAcType = doc.getElementById("BankAcType").val();
		System.out.println("BankAcType----" + BankAcType);
		// SubAcSeq
		String SubAcSeq = doc.getElementById("SubAcSeq").val();
		System.out.println("SubAcSeq----" + SubAcSeq);
		// SeqNoKey
		String SeqNoKey = doc.getElementById("SeqNoKey").val();
		System.out.println("SeqNoKey----" + SeqNoKey);
		// Currency
		String Currency = doc.getElementById("Currency").val();
		System.out.println("Currency----" + Currency);
		// CRFlag
		String CRFlag = doc.getElementById("CRFlag").val();
		System.out.println("CRFlag----" + CRFlag);
		// DeptId
		String DeptId = doc.getElementById("DeptId").val();
		System.out.println("DeptId----" + DeptId);
		// AcType
		String AcType = doc.getElementById("AcType").val();
		System.out.println("AcType----" + AcType);
		// PrePage
		String PrePage = doc.getElementById("PrePage").val();
		System.out.println("PrePage----" + PrePage);
		// Download
		String Download = doc.getElementById("Download").val();
		System.out.println("Download----" + Download);

		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("_viewReferer", "query/ActTrsQry/ActTrsQryPre"));
		paramsList1.add(new NameValuePair("qryType", "1"));
		paramsList1.add(new NameValuePair("count", count));
		paramsList1.add(new NameValuePair("AcNo", AcNo));
		paramsList1.add(new NameValuePair("BankAcType", BankAcType));
		paramsList1.add(new NameValuePair("SubAcSeq", SubAcSeq));
		paramsList1.add(new NameValuePair("SeqNoKey", SeqNoKey));
		paramsList1.add(new NameValuePair("Currency", Currency));
		paramsList1.add(new NameValuePair("CRFlag", CRFlag));
		paramsList1.add(new NameValuePair("DeptId", DeptId));
		paramsList1.add(new NameValuePair("AcType", AcType));
		paramsList1.add(new NameValuePair("PrePage", PrePage));
		paramsList1.add(new NameValuePair("Download", Download));
		paramsList1.add(new NameValuePair("ChannelId", ""));
		paramsList1.add(new NameValuePair("BeginDate", "2017-09-17"));
		paramsList1.add(new NameValuePair("EndDate", "2018-03-17"));
		paramsList1.add(new NameValuePair("currentIndex", "0"));
		paramsList1.add(new NameValuePair("recordNumber", ""));
		paramsList1.add(new NameValuePair("pageSize", "9999999"));

	}

	public static void zhanghu() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zhanghu.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		Elements tdValue = doc.getElementsByClass("tdValue");
		String attr = tdValue.get(7).getElementsByTag("a").get(0).attr("onClick");
		System.out.println("账户状态----" + attr);
		attr = attr.substring(attr.indexOf("(") + 1, attr.indexOf(")"));
		System.out.println("账户状态----" + attr);
		attr = attr.replace("'", "");
		System.out.println("账户状态----" + attr);
		String[] split = attr.split(",");
		String acnoV = split[0];
		String actypeV = split[1];
		String deptIdV = split[2];
		String url = "https://pbank.psbc.com/pweb/ActTrsQryPre.do?DeptId=" + deptIdV + "&AcNo=" + acnoV + "&AcType="
				+ actypeV + "&PrePage=type";
		System.out.println("url--------------" + url);
		if (tdValue.size() > 10) {
			// 卡号/账号
			String card_number = tdValue.get(1).text();
			System.out.println("卡号/账号----" + card_number);
			// 别名
			String aliases = tdValue.get(2).text();
			System.out.println("别名----" + aliases);
			// 账户类型
			String card_type = tdValue.get(3).text();
			System.out.println("账户类型----" + card_type);
			// 币种
			String currency = tdValue.get(4).text();
			System.out.println("币种----" + currency);
			// 账户余额
			String balance = tdValue.get(5).text();
			System.out.println("账户余额----" + balance);
			// 可用余额
			String available_balance = tdValue.get(6).text();
			System.out.println("可用余额----" + available_balance);
			// 账户状态
			String account_state = tdValue.get(8).text();
			System.out.println("账户状态----" + account_state);
			// 签约标志
			String sign_sign = tdValue.get(9).text();
			System.out.println("签约标志----" + sign_sign);
			// 开户机构
			String opening_institution = tdValue.get(10).text();
			System.out.println("开户机构----" + opening_institution);
		}

	}

	public static void login() throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.get(LoginPage);
//		String windowHandle = driver.getWindowHandle();
		
//		driver.manage().window().maximize();
		
//		Actions action = new Actions(driver);
//		WebElement source = driver.findElement(By.xpath("/html/body/form/table[2]/tbody/tr/td"));
//		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
//		Thread.sleep(2000);
//		action.moveToElement(source).release();
//		Action actions = action.build();
//		actions.perform();
		
//		Actions action = new Actions(driver);
//		action.contextClick(driver.findElement(By.id("textfield")));// 鼠标右键点击指定的元素
//		action.click(driver.findElement(By.id("textfield")));// 鼠标左键点击指定的元素
//		action.perform();
//		action.release();

		
		String accountNum = "2305231456454564";

		String password = "12qwaszx";
		// Thread.sleep(1000L);

//		driver = driver.switchTo().window(windowHandle);
//		 driver.findElement(By.id("textfield")).click();
//		driver.findElement(By.id("textfield")).sendKeys(Keys.DOWN);

//		driver.switchTo().activeElement().sendKeys(accountNum);
		driver.findElement(By.id("textfield")).sendKeys(accountNum);
		
		// 键盘输入Tab，让游览器焦点切换到密码框
		/**
		 * 
		 */
		// Actions action = new Actions(driver);
		// action.sendKeys(Keys.TAB);
		// action.sendKeys(Keys.TAB);
		// Action actions = action.build();
		// actions.perform();
		// action.release();

		/**
		 * 
		 */
		// Actions action = new Actions(driver);
		// action.sendKeys(Keys.TAB).perform();
		// action.release();
		// Thread.sleep((long) (Math.random()*1500));
		// Actions action1 = new Actions(driver);
		// action1.sendKeys(Keys.TAB).perform();
		// action1.release();
		/**
		 * 
		 */
		// Actions action = new Actions(driver);
		// action.sendKeys(Keys.TAB).perform();
		// Actions action1 = new Actions(driver);
		// action1.sendKeys(Keys.TAB).perform();
		/**
		 * 
		 */
		// Actions action = new Actions(driver);
		// action.sendKeys(Keys.TAB);
		// action.sendKeys(Keys.TAB);
		// action.perform();
		/**
		 * 
		 */
		// Robot robot = new Robot();
		// robot.keyPress(java.awt.event.KeyEvent.VK_TAB);
		// robot.keyPress(java.awt.event.KeyEvent.VK_TAB);
		
		/**
		 * 
		 */
//		 Thread.sleep(2000L);
//		 InputTab();
//		 Thread.sleep(2000L);
//		 InputTab();
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.TAB).perform();
		Thread.sleep((long) (Math.random() * 1500));
		Actions action1 = new Actions(driver);
		action1.sendKeys(Keys.TAB).perform();
		
//		driver.findElement(By.xpath("/html/body/form/table[3]/tbody/tr/td/table[1]/tbody/tr/td[2]/table/tbody/tr/td[2]/table[3]/tbody/tr/td/table[1]/tbody/tr/td/table/tbody/tr[5]/td")).click();

		Thread.sleep(1500L);
		VirtualKeyBoard.KeyPressEx(password, 1000,3000);// 输入密码
		
		
		Thread.sleep(1500L);
//		String s = password;
//		for (int l = 0; l < s.length(); l++) {
//			driver.findElement(By.xpath("/html/body/form/table[3]/tbody/tr/td/table[1]/tbody/tr/td[2]/table/tbody/tr/td[2]/table[3]/tbody/tr/td/table[1]/tbody/tr/td/table/tbody/tr[4]/td")).click();
////			 Thread.sleep(1000);
////			 InputTab();
//			 Thread.sleep(1000);
//			 InputTab();
////			Actions action = new Actions(driver);
////			action.sendKeys(Keys.TAB);
////			action.sendKeys(Keys.TAB);
////			action.perform();
//			Thread.sleep(1500L);
//			char chr = s.charAt(l);
//			Integer is = VKMapping.getMapValue("" + chr);
//			if (is != null) {
//				VirtualKeyBoard.KeyPress(VKMapping.toScanCode("" + s.charAt(l)));//
//			} else {
//				VirtualKeyBoard.KeyPressUppercase(VKMapping.toScanCodeUppercase("" + s.charAt(l)));//
//			}
//			Thread.sleep((long) (Math.random() * 1000)+1000);
//		}
//		Thread.sleep(1500L);
		
		
		// 验证码
		// String path = WebDriverUnit.saveImg(driver,
		// By.xpath("//*[@id=\"_tokenImg\"]/../../.."));
		String path = WebDriverUnit.saveImg(driver, By.xpath("//*[@id=\"_tokenImg\"]/../.."));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("6004", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);

		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		// VirtualKeyBoard.KeyPressEx(code, 60);
		driver.findElement(By.id("_vTokenName")).sendKeys(code);

		driver.findElement(By.id("button")).click();

		Thread.sleep(1000L);

		String errorfinfo = "";

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		WebElement main = null;
		try {
			main = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("EEE"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (main != null) {
			errorfinfo = main.getText();
		}
		if (errorfinfo != null && errorfinfo.length() > 0) {
			System.out.println("errorfinfo---------------" + errorfinfo);
		} else {
			System.out.println("errorfinfo---------------无错误信息");
			// String json = driver.getPageSource();
			// JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
			// driver.switchTo().frame("main");
			// driver_js.executeScript("javascript:check();");
			// driver.switchTo().defaultContent();
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("pbank.psbc.com",
						cookie.getName(), cookie.getValue()));
			}
			String cookieString = CommonUnit.transcookieToJson(webClient); // 存储cookie
			System.out.println("cookieString------------" + cookieString);
			getUser(webClient);

			// getliushui(webClient);

			// String endDate = getDateBefore("yyyyMMdd", 0);
			// System.out.println(endDate);
			//
			// String startDate = getDateBefore("yyyyMMdd", 12);
			// System.out.println(startDate);
		}
	}

	public static String getliushui(WebClient webClient) throws Exception {
		String url = "https://pbank.psbc.com/pweb/ActTrsInfoQry.do";

		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("_viewReferer", "query/ActTrsQry/ActTrsQryPre"));
		paramsList1.add(new NameValuePair("qryType", "1"));
		paramsList1.add(new NameValuePair("count", "0"));
		paramsList1.add(new NameValuePair("AcNo", "6221882600107620508"));
		paramsList1.add(new NameValuePair("BankAcType", ""));
		paramsList1.add(new NameValuePair("SubAcSeq", ""));
		paramsList1.add(new NameValuePair("SeqNoKey", ""));
		paramsList1.add(new NameValuePair("Currency", ""));
		paramsList1.add(new NameValuePair("CRFlag", ""));
		paramsList1.add(new NameValuePair("DeptId", "230523020"));
		paramsList1.add(new NameValuePair("AcType", "D"));
		paramsList1.add(new NameValuePair("PrePage", "type"));
		paramsList1.add(new NameValuePair("Download", ""));
		paramsList1.add(new NameValuePair("ChannelId", ""));
		paramsList1.add(new NameValuePair("BeginDate", "2017-09-17"));
		paramsList1.add(new NameValuePair("EndDate", "2018-03-17"));
		paramsList1.add(new NameValuePair("currentIndex", "0"));
		paramsList1.add(new NameValuePair("recordNumber", ""));
		paramsList1.add(new NameValuePair("pageSize", "9999999"));

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept", "text/html, application/xhtml+xml, */*");
		// map.put("Referer",
		// "https://perbank.czbank.com/PERBANK/account/balance_qry.jsp?dse_sessionId=GHBOGDBNHNGMEECHCRBTHOHFDRFVEWGVEGCDGRDZ&netType=&accType=901&accNo=6223091000010603753&currCode=01&isSubAcc=0&accTypeText=901&subAccType=null");
		// map.put("Accept-Language", "zh-CN");
		// map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64;
		// Trident/7.0; rv:11.0) like Gecko");
		// map.put("Content-Type", "application/x-www-form-urlencoded");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Host", "perbank.czbank.com");
		// map.put("Connection", "Keep-Alive");
		// map.put("Cache-Control", "no-cache");

		// map.put("Cookie",
		// "czbank_PB_logonType=0; td_cookie=18446744070553798910;
		// TrsAccessMonitor=TrsAccessMonitor-1512114694000-1301372759;
		// tma=85694411.50374414.1512114698161.1512114698161.1512114698161.1;
		// tmd=90.85694411.50374414.1512114698161.;
		// bfdid=87205254007bf95200001a6100036e4e5a210a33;
		// bfd_g=87205254007bf95200001a6100036e4e5a210a33;
		// bfd_s=85694411.21410090.1513237161281;
		// tmc=7.85694411.3480013.1513237161283.1513239590900.1513239631805;
		// request_temp_url=https%3A%2F%2Fxwcjsc.czbank.com%2FTrackEvent.do%3Fname%3Dclick_v_login%26category%3Dxxx%26action%3Dxxx%26label%3D1%26value%3Dxxx%26uid%3Dxxx%26pageflag%3D1513239590881%26sid%3D85694411.21410090.1513237161281%26fingerprint%3D0c62ad99437f5c1bdc84cbb296abe911%26fpduration%3D42%26cid%3DCzsbank%26d_s%3Dpc%26appkey%3Dc74c4d21e0cee11a6202d85ac9743cbe%26gid%3D87205254007bf95200001a6100036e4e5a210a33;
		// JSESSIONID=0000sdoFOi4Xivy3TkJhOAeX1e7:1ajm2h3pg");

		Page page1 = getPage(webClient, null, url, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getUser*****************************" + html);

		return html;
	}
	
	/**
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	public static String getlogin(WebClient webClient) throws Exception {
		String url = "https://pbank.psbc.com/pweb/login.do?BankId=9999";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("_viewReferer", "query/ActTrsQry/ActTrsQryPre"));
		paramsList1.add(new NameValuePair("qryType", "1"));
		
		Page page1 = getPage(webClient, null, url, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getUser*****************************" + html);
		return html;
	}

	// 账户信息
	public static String getUser(WebClient webClient) throws Exception {
		String url = "https://pbank.psbc.com/pweb/ActTypeQry.do";
		Page page1 = getPage(webClient, null, url, HttpMethod.GET, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getUser*****************************" + html);
		return html;
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
	public static String getDateBefore(String fmt, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
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

	public static WebClient addcookie(String cookieString) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
}
