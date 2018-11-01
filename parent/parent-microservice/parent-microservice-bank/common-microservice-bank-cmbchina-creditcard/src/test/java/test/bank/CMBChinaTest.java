package test.bank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import ch.qos.logback.core.encoder.Encoder;
import net.sf.json.JSONObject;

public class CMBChinaTest {

	static String driverPath = "D:\\WD\\s\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";

	public static void main(String[] args) {
		try {
			// login();
//			 getUser();
//			getZD();
			// getmingxi("201711");
			// getmingxi1();

			// for (int i = 0; i < 12; i++) {
			// String yearmonth = getDateBefore("yyyyMM", i);
			// System.out.println(yearmonth);
			// }
			// getJF();
			// getjifen();
//			String CardsInfoForType = "{A:true,H:false,B:false,C:true,D:true}";
			String CardsInfoForType = "{A:true}";
			JSONObject jsonObj = JSONObject.fromObject(CardsInfoForType);
			String type = jsonObj.getString("C");
			if (jsonObj.has("C") && "true".equals(type)) {
				System.out.println("11111");
			}
			
			 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getjifen() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\jifenyemian.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		String BtnQuery = doc.getElementById("BtnQuery").val();
		System.out.println("BtnQuery-----------" + BtnQuery);

	}

	public static void getJF() {
		// File file = new File("C:\\Users\\Administrator\\Desktop\\jifen.txt");
		File file = new File("C:\\Users\\Administrator\\Desktop\\LSjifen.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);

		Element elementById = doc.getElementById("UpdatePanel2");
		Elements elementsByTag = elementById.getElementsByTag("tr");
		if (elementsByTag.size() > 1) {
			Elements elementsByTag2 = elementsByTag.get(1).getElementsByTag("td");
			if (elementsByTag2.size() == 10) {
				// 积分名称
				String name = elementsByTag2.get(0).text();
				System.out.println(name + "----");
				// 积分管理模式
				String model = elementsByTag2.get(1).text();
				System.out.println(model + "----");
				// 当期刷卡积分
				String payCardIntegral = elementsByTag2.get(2).text();
				System.out.println(payCardIntegral + "----");
				// 当期调整积分
				String adjustedIntegral = elementsByTag2.get(3).text();
				System.out.println(adjustedIntegral);
				// 当期奖励积分
				String rewardIntegral = elementsByTag2.get(4).text();
				System.out.println(rewardIntegral);
				// 当期新增积分
				String addIntegral = elementsByTag2.get(5).text();
				System.out.println(addIntegral);
				// 当期兑换积分
				String exchangeIntegral = elementsByTag2.get(6).text();
				System.out.println(exchangeIntegral);
				// 当前可用积分
				String useIntegral = elementsByTag2.get(7).text();
				System.out.println(useIntegral);
				// 最近即将失效积分
				String InvalidIntegral = elementsByTag2.get(8).text();
				System.out.println(InvalidIntegral);
				// 失效日期
				String invalidDate = elementsByTag2.get(9).text();
				System.out.println(invalidDate);
			}
		}

	}

	/**
	 * 账单明细
	 */
	public static void getmingxi1() {
		// File file = new
		// File("C:\\Users\\Administrator\\Desktop\\month11Q.txt");
		File file = new File("C:\\Users\\Administrator\\Desktop\\mingxi.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		String CreditAccNo = doc.getElementsByAttributeValue("selected", "selected").val();

		// String CreditAccNo = doc.getElementsByAttributeValue("name",
		// "CreditAccNo").get(0).val();
		System.out.println("CreditAccNo-----------" + CreditAccNo);

	}

	/**
	 * 账户查询明细
	 */
	public static String getJF(WebClient webClient, String __EVENTTARGET, String __EVENTARGUMENT, String __VIEWSTATE,
			String __VIEWSTATEGENERATOR, String __EVENTVALIDATION, String ddlYearMonthList, String BtnQuery,
			String ClientNo, String FunctionName) throws Exception {

		String url1 = "https://pbsz.ebank.cmbchina.com/CmbBank_CreditCard_Customer/UI/CreditCard/Points/pm_QueryHistPoints.aspx";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("__EVENTTARGET", __EVENTTARGET));
		paramsList1.add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
		paramsList1.add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
		paramsList1.add(new NameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));
		paramsList1.add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
		paramsList1.add(new NameValuePair("ddlYearMonthList", ddlYearMonthList));
		paramsList1.add(new NameValuePair("BtnQuery", BtnQuery));
		paramsList1.add(new NameValuePair("ClientNo", ClientNo));
		paramsList1.add(new NameValuePair("FunctionName", FunctionName));

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept", "text/html, application/xhtml+xml, */*");
		// map.put("Referer",
		// "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx");
		// map.put("Accept-Language", "zh-CN");
		// map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1;
		// WOW64;Trident/7.0; rv:11.0) like Gecko");
		// map.put("Content-Type", "application/x-www-form-urlencoded");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Host", "pbsz.ebank.cmbchina.com");
		// map.put("Connection", "Keep-Alive");
		// map.put("Cache-Control", "no-cache");

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getMX*****************************" + html);
		return html;
	}

	/**
	 * 账户查询明细
	 */
	public static String getMX(WebClient webClient, String ClientNo, String CreditAccNo) throws Exception {
		String url1 = "https://pbsz.ebank.cmbchina.com/CmbBank_CreditCard_Loan/UI/CreditCard/Loan/am_QueryReckoningListNew.aspx";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("ClientNo", ClientNo));
		paramsList1.add(new NameValuePair("O_STMT_FLAG", "Y"));
		paramsList1.add(new NameValuePair("IN_YYYYMM", "201711"));
		paramsList1.add(new NameValuePair("CreditAccNo", CreditAccNo));

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept", "text/html, application/xhtml+xml, */*");
		// map.put("Referer",
		// "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx");
		// map.put("Accept-Language", "zh-CN");
		// map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1;
		// WOW64;Trident/7.0; rv:11.0) like Gecko");
		// map.put("Content-Type", "application/x-www-form-urlencoded");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Host", "pbsz.ebank.cmbchina.com");
		// map.put("Connection", "Keep-Alive");
		// map.put("Cache-Control", "no-cache");

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getMX*****************************" + html);
		return html;
	}

	/**
	 * 账单明细
	 */
	public static void getmingxi(String date) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\month11TWO.txt");
		// File file = new
		// File("C:\\Users\\Administrator\\Desktop\\month201612QW.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		int i = 0;
		// 2017年
		String loopBand = "loopBand2";
		if (date.contains("2016")) {
			// 2016年
			loopBand = "loopBand1";
		}

		Element loopBand2 = doc.getElementById(loopBand);
		Element table = loopBand2.getElementsByTag("table").get(0);
		Elements elementsBytable = table.getElementsByTag("span");
		for (Element element : elementsBytable) {
			// 2017年
			String fixBand = "fixBand15";
			if (date.contains("2016")) {
				// 2016年
				fixBand = "fixBand28";
			}
			Element elementById = element.getElementById(fixBand);
			if (null != elementById) {
				Elements elementsByTag = elementById.getElementsByTag("div");
				// Elements elementsByTag =
				// elementById.getElementsByTag("table").get(0).getElementsByTag("table").get(0)
				// .getElementsByTag("tr").get(0).getElementsByTag("div");
				if (elementsByTag.size() == 7) {
					i++;
					// 交易日
					String tradingDay = elementsByTag.get(0).text();
					// 记账日
					String billingDay = elementsByTag.get(1).text();
					// 交易摘要
					String summary = elementsByTag.get(2).text();
					// 人民币金额
					String rmbAmount = elementsByTag.get(3).text();
					// 卡号末四位
					String endfourNum = elementsByTag.get(4).text();
					// 交易地点
					String tradingPlace = elementsByTag.get(5).text();
					// 交易地金额
					String amountTransaction = elementsByTag.get(6).text();
					System.out.print(tradingDay + "----");
					System.out.print(billingDay + "----");
					System.out.print(summary + "----");
					System.out.print(rmbAmount + "----");
					System.out.print(endfourNum + "----");
					System.out.print(tradingPlace + "----");
					System.out.println(amountTransaction);
				}

			}
		}
		System.out.println(i);

	}

	public static void getZD() {
		// File file = new File("C:\\Users\\Administrator\\Desktop\\yczd.txt");
		File file = new File("C:\\Users\\Administrator\\Desktop\\ZD.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);

		Element elementById = doc.getElementById("dgReckoningInfo1");
		Elements elementsByTag = elementById.getElementsByTag("tr");
		for (Element element : elementsByTag) {
			Elements elementsByTag2 = element.getElementsByTag("td");
			if (elementsByTag2.size() > 4) {
				// 账单月份
				String billMonth = elementsByTag2.get(0).text();
				System.out.print(billMonth + "----");
				// 人民币应还总额
				String repaymentSumRMB = elementsByTag2.get(1).text();
				System.out.print(repaymentSumRMB + "----");
				// 人民币最低还款额
				String repaymentMinRMB = elementsByTag2.get(2).text();
				System.out.print(repaymentMinRMB + "----");
				// 美元应还总额
				String repaymentSumDollar = elementsByTag2.get(3).text();
				System.out.print(repaymentSumDollar + "----");
				// 美元最低还款额
				String repaymentMinDollar = elementsByTag2.get(4).text();
				System.out.println(repaymentMinDollar);
			}
		}

	}

	/**
	 * 账户查询
	 */
	public static String getZHCX(WebClient webClient, String ClientNo) throws Exception {
		String url1 = "https://pbsz.ebank.cmbchina.com/CmbBank_CreditCard_Account/UI/CreditCard/Account/am_QueryAccount.aspx";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("ClientNo", ClientNo));
		paramsList1.add(new NameValuePair("index", "0"));

		Map<String, String> map = new HashMap<String, String>();
		map.put("Accept", "text/html, application/xhtml+xml, */*");
		map.put("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx");
		map.put("Accept-Language", "zh-CN");
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64;Trident/7.0; rv:11.0) like Gecko");
		map.put("Content-Type", "application/x-www-form-urlencoded");
		map.put("Accept-Encoding", "gzip, deflate");
		map.put("Host", "pbsz.ebank.cmbchina.com");
		map.put("Connection", "Keep-Alive");
		map.put("Cache-Control", "no-cache");

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, paramsList1, null, null, map);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("*****************************" + html);
		return html;
	}

	public static void getZHCXInfo() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zhanghuxinxi11.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);

		// 本期账单金额---人民币
		String LiterRMBZDJE = doc.getElementById("LiterRMBZDJE").text();
		System.out.println("本期账单金额--人民币----" + LiterRMBZDJE);
		// 本期账单金额---美元
		String LiterUSBZDJE = doc.getElementById("LiterUSBZDJE").text();
		System.out.println("本期账单金额---美元---" + LiterUSBZDJE);

		// 本期剩余应还金额---人民币
		String LiterRMBBQJE = doc.getElementById("LiterRMBBQJE").text();
		System.out.println("本期剩余应还金额---人民币------" + LiterRMBBQJE);
		// 本期剩余应还金额---美元
		String LiterUSBQJE = doc.getElementById("LiterUSBQJE").text();
		System.out.println("本期剩余应还金额---美元------" + LiterUSBQJE);

		// 本期剩余最低还款金额---人民币
		String rmbzdje = doc.getElementById("RMBZDJE").text();
		System.out.println("本期剩余最低还款金额---人民币------" + rmbzdje);
		// 本期剩余最低还款金额---美元
		String uszdje = doc.getElementById("USZDJE").text();
		System.out.println("本期剩余最低还款金额---美元------" + uszdje);

	}

	public static void getUser() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\user.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		Elements trxyed = doc.getElementById("ucCmQueryCustomInfo0_trxyed").getElementsByTag("td");
		// 信用额度--------人民币
		String trxyedRMB = trxyed.get(1).text();
		// 信用额度--------美元
		String trxyedDollar = trxyed.get(2).text();
		System.out.println(trxyedRMB + "--人民币-----信用额度----美元--" + trxyedDollar);

		Elements trkyed = doc.getElementById("ucCmQueryCustomInfo0_trkyed").getElementsByTag("td");
		// 可用额度--------人民币
		String trkyedRMB = trkyed.get(1).text();
		// 可用额度--------美元
		String trkyedDollar = trkyed.get(2).text();
		System.out.println(trkyedRMB + "--人民币-----可用额度----美元--" + trkyedDollar);

		Elements tryjxjed = doc.getElementById("ucCmQueryCustomInfo0_tryjxjed").getElementsByTag("td");
		// 预借现金可用额度--------人民币
		String tryjxjedRMB = tryjxjed.get(1).text();
		// 预借现金可用额度--------美元
		String tryjxjedDollar = tryjxjed.get(2).text();
		System.out.println(tryjxjedRMB + "--人民币-----预借现金可用额度----美元--" + tryjxjedDollar);
		// 每月账单日
		String lmyzd = doc.getElementById("ucCmQueryCustomInfo0_LMYZD").text();
		System.out.println("每月账单日------" + lmyzd);
		// 本期到期还款日
		String lbqdqhk = doc.getElementById("ucCmQueryCustomInfo0_LBQDQHK").text();
		System.out.println("本期到期还款日------" + lbqdqhk);

		Elements elementsByTag = doc.getElementById("ucCmQueryCustomInfo0_dgReckoningSet").getElementsByTag("tr");
//		if (elementsByTag.size() > 1) {
			for (Element element : elementsByTag) {
				Elements elementsByTag2 = element.getElementsByTag("td");
				if (elementsByTag2.size() > 4) {
					// 卡号
					String idNum = elementsByTag2.get(0).text();
					if(!"卡号".equals(idNum)){
						// 主卡/附属卡
						String cardType = elementsByTag2.get(1).text();
						// 联名卡别
						String cardStyle = elementsByTag2.get(2).text();
						// 持卡人姓名
						String name = elementsByTag2.get(3).text();
						// 开卡标志
						String cardState = elementsByTag2.get(4).text();
						System.out.println(idNum);
						System.out.println(cardType);
						System.out.println(cardStyle);
						System.out.println(name);
						System.out.println(cardState);
					}
				}
			}
//		}

	}

	/**
	 * 发送验证码
	 */
	public static String sendMsg(WebClient webClient, String ClientNo) throws Exception {

		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("PRID", "SendMSGCode"));
		paramsList.add(new NameValuePair("ClientNo", ClientNo));

		Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);

		System.out.println("~~~~~~~~~~~~~~~" + page.getWebResponse().getContentAsString());
		/**
		 * 验证验证码
		 */
		String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");
		String url1 = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		paramsList1.add(new NameValuePair("PRID", "VerifyMSGCode"));
		paramsList1.add(new NameValuePair("ClientNo", ClientNo));
		paramsList1.add(new NameValuePair("SendCode", inputuserjymtemp));

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, paramsList1, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("*****************************" + html);
		return html;
	}

	public static void login() throws Exception {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		// RemoteWebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.120.30:4444/wd/hub/"),
		// DesiredCapabilities.internetExplorer());

		driver = new InternetExplorerDriver(ieCapabilities);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";
		// String baseUrl =
		// "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
		// System.out.println("currentUrl--11--"+currentUrl);
		String htmlsource = driver.getPageSource();
		// System.out.println("htmlsource--11--"+htmlsource);

		String[] accountNum = { "6", "2", "2", "5", "7", "6", "8", "7", "8", "9", "8", "8", "5", "6", "0", "3" };

		String[] password = { "1", "4", "7", "8", "5", "2" };

		Thread.sleep(1000L);

		Input(accountNum);

		InputTab();

		Input(password);

		driver.findElement(By.id("LoginBtn")).click();

		Thread.sleep(3000L);

		String currentPageURL = driver.getCurrentUrl();

		// 如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
		if (LoginPage.equals(currentPageURL)) {
			String errorfinfo = driver.findElement(By.className("page-form-item-controls")).getText();
			if (errorfinfo != null && errorfinfo.length() > 0) {
				throw new RuntimeException(errorfinfo);
			}
			System.out.println("errorfinfo---------------" + errorfinfo);
		} else {

			// Wait<WebDriver> wait = new
			// FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
			// .pollingEvery(2,
			// TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			// WebElement ele1 = wait.until(new Function<WebDriver,
			// WebElement>() {
			// public WebElement apply(WebDriver driver) {
			// return driver.findElement(By.id("favoritehref1"));
			// }
			// });

			// driver.findElement(By.id("favoritehref1")).click();
			// Thread.sleep(3000L);
			// driver.findElement(By.className("lkPanelNoSel")).click();

			JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
			Thread.sleep(1500L);

			// 客户管理--------->客户综合查询
			// driver_js.executeScript(
			// "CallFuncEx2('C','CBANK_CREDITCARD_CUSTOMER','Customer/cm_QueryCustomInfo.aspx','FORM',null);");
			// 账户管理--------->账户查询
			// driver_js.executeScript(
			// "CallFuncEx2('C','CBANK_CREDITCARD_ACCOUNT',
			// 'Account/am_QueryAccount.aspx','FORM', null);");
			// 积分管理--------->积分查询
			driver_js.executeScript(
					"CallFuncEx2('C','CBANK_CREDITCARD_CUSTOMER','Points/psm_QueryCurPoints.aspx','FORM',null);");
			driver.switchTo().frame("mainWorkArea");

			Thread.sleep(1000L);

			driver_js.executeScript("changeSub(1)");

			driver.switchTo().defaultContent();

			driver.switchTo().frame("mainWorkArea");

			// driver.findElement(By.id("UcTabControl1_panelTd1")).findElement(By.tagName("a")).click();

			String pageSource = driver.getPageSource();
			System.out.println("pageSource-------" + pageSource);

			// String ClientNo =
			// driver.findElement(By.id("ClientNo")).getAttribute("value");
			// System.out.println("ClientNo-----------" + ClientNo);

			// String attribute =
			// driver.findElement(By.cssSelector("selected=selected")).getAttribute("value");
			// System.out.println("CreditAccNo-----------" + attribute);

			Document doc = Jsoup.parse(pageSource);

			String __EVENTTARGET = doc.getElementById("__EVENTTARGET").val();
			System.out.println("__EVENTTARGET-----------" + __EVENTTARGET);

			String __EVENTARGUMENT = doc.getElementById("__EVENTARGUMENT").val();
			System.out.println("__EVENTARGUMENT-----------" + __EVENTARGUMENT);

			String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
			System.out.println("__VIEWSTATE-----------" + __VIEWSTATE);

			String __VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").val();
			System.out.println("__VIEWSTATEGENERATOR-----------" + __VIEWSTATEGENERATOR);

			String __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").val();
			System.out.println("__EVENTVALIDATION-----------" + __EVENTVALIDATION);

			String ddlYearMonthList = "201711";

			String BtnQuery = doc.getElementById("BtnQuery").val();
			System.out.println("BtnQuery-----------" + BtnQuery);

			String ClientNo = doc.getElementById("ClientNo").val();
			System.out.println("ClientNo-----------" + ClientNo);

			String FunctionName = doc.getElementById("FunctionName").val();
			System.out.println("FunctionName-----------" + FunctionName);

			// String __ASYNCPOS = doc.getElementById("__ASYNCPOS").val();
			// System.out.println("__ASYNCPOS-----------" + __ASYNCPOS);

			// String CreditAccNo2 = doc.getElementsByAttributeValue("selected",
			// "selected").val();
			// System.out.println("CreditAccNo2-----------" + CreditAccNo2);

			WebClient webClient = WebCrawler.getInstance().getWebClient();

			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				// System.out.println(cookie.getName() +
				// "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
						"pbsz.ebank.cmbchina.com", cookie.getName(), cookie.getValue()));
			}
			// driver.quit();
			getJF(webClient, __EVENTTARGET, __EVENTARGUMENT, __VIEWSTATE, __VIEWSTATEGENERATOR, __EVENTVALIDATION,
					ddlYearMonthList, BtnQuery, ClientNo, FunctionName);
			String cookieString = CommonUnit.transcookieToJson(webClient); // 存储cookie

		}

	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(500L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
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
	public static Page getPage(WebClient webClient, TaskHousing taskHousing, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		// tracer.addTag("HousingTaiyuanParse.getPage---url:", url +
		// "---taskId:" + taskHousing.getTaskid());

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
		// tracer.addTag("HousingTaiyuanParse.getPage.statusCode:" + statusCode,
		// "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			// tracer.addTag("HousingTaiyuanParse.getPage---taskid:",
			// taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html +
			// "</xmp>");
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

	public static void decode() {
		// String par = "å¾ä¿¡æ”¯ä»˜-æ°‘ç”Ÿé“¶è¡ŒåŽ¦é—¨åˆ†è¡Œ ";
		String par = "ä¸»å¡";
		try {
			String outStr = new String(par.getBytes("iso-8859-1"), "UTF-8");
			System.out.println(outStr);
			String output = URLDecoder.decode(par, "iso-8859-1");
			System.out.println(output);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
}
