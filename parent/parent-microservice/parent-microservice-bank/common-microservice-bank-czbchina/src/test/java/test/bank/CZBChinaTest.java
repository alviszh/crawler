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
import java.util.Iterator;
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

public class CZBChinaTest {

	static String driverPath = "D:\\WD\\s\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://perbank.czbank.com/PERBANK/logon.jsp";
	static String ErrorPage = "https://perbank.czbank.com/PERBANK/EBank";

	public static String OCR_FILE_PATH = "D:/img";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		try {
//			6223091000010603753
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			String jsonT = "";
//			 login();
//			getUser(webClient, "GTIMIJGCDWFOGNEDGZIVEKCGELBIGLFMJCCCDLCS", "");
//			 getdse_sessionId();
//			 getding();
//			 user("6223091000010603753");
//			certNo();
//			mingxi(jsonT);
			
//			 getmingxi(webClient, "ECGLEDAEJOBFJPBZERDEEBGCFCELGDDBEMEAFHGK", "20161219", "20171219", "6223091000010603753", 1);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\mingxifirst.txt");
		String html = txt2String(file);
		int pages = 0;
		String count = "";
		try {
			String turnPage = "javascript:turnPage('";
			if(html.contains(turnPage)){
				html = html.substring(html.lastIndexOf(turnPage) + turnPage.length());
				System.out.println(html);
				if(html.contains("'")){
					html = html.substring(0, html.indexOf("'"));
					System.out.println(html);
					count = html;
				}
			}
			pages = Integer.valueOf(count) + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(pages);
		
	}
	
	
	public static void mingxi(String jsonT,WebClient webClient, String dse_sessionId,String startDate,String endDate,String accountNo,int nowPage) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\mingxi1.txt");
		String json = txt2String(file);
		
		Document doc = Jsoup.parse(jsonT);
		
		Element depositTR0 = doc.getElementById("tradeListTable");
		Elements elementsByClass = depositTR0.getElementsByClass("bg1");
		for (Element element : elementsByClass) {
			Elements td = element.getElementsByTag("td");
			if(td.size()==8){
				// 序号
				String num = td.get(0).text();
				System.out.print("序号----" + num);

				// 交易时间
				String tran_date = td.get(1).text();
				System.out.print("---交易时间----" + tran_date);

				// 发生额（元）
				String fee = td.get(2).text();
				System.out.print("---发生额（元）----" + fee);

				// 当前余额（元）
				String balance = td.get(3).text();
				System.out.println("---当前余额（元）----" + balance);

				// 摘要
				String trans_decription = td.get(4).text();
				System.out.print("---摘要----" + trans_decription);

				// 对方名称
				String opposite_name = td.get(5).text();
				System.out.print("---对方名称----" + opposite_name);
				
				// 对方账号
				String opposite_card_number = td.get(6).text();
				System.out.print("---对方账号----" + opposite_card_number);

				// 对方开户行
				String opposite_bank_name = td.get(7).text();
				System.out.println("---对方开户行----" + opposite_bank_name);
			}
		}
		
		Elements elementsByClass2 = doc.getElementsByClass("pageText");
		int pages = 0;
		String count = elementsByClass2.text();
		if (count.length() > 5) {
			count = count.substring(3, count.length() - 2);
		}
		System.out.println("count------------------"+count);
		try {
			pages = Integer.valueOf(count)/10+1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 2; i < pages+1; i++) {
			try {
				System.out.println(i);
//				getmingxi(webClient, dse_sessionId, startDate, endDate, accountNo, i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void certNo() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\certNo.txt");
		String json = txt2String(file);
		
		Document doc = Jsoup.parse(json);
		
		String certNo = doc.getElementsByAttributeValue("name", "certNo").val();

		System.out.println("certNo----" + certNo);

	}
	
	//开户/认购日
	public static void user(String jsonT,String card_number) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\user.txt");
		String json = txt2String(file);
		
		Document doc = Jsoup.parse(jsonT);
		
		Element depositTR0 = doc.getElementById(card_number+"depositTR0");
		Elements elementsByTag = depositTR0.getElementsByTag("td");
		if (elementsByTag.size() > 6) {
			//开户/认购日
			String deposit_time = elementsByTag.get(5).text();
			System.out.println("开户/认购日----" + deposit_time);
		}

	}
	
	public static void getding(String jsonT) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\userdingqi.txt");
		String json = txt2String(file);
		
		Document doc = Jsoup.parse(jsonT);

//		Element balanceTable = doc.getElementById("balanceTable");
		Elements bg1 = doc.getElementsByClass("bg1");
		if (bg1.size() > 0) {
			Elements td = bg1.get(0).getElementsByTag("td");

			// 卡号/账号
			String card_number = td.get(0).text();
			System.out.println("卡号/账号----" + card_number);

			// 账户类型
			String card_type = td.get(1).text();
			System.out.println("---账户类型----" + card_type);

			// 账户别名
			String aliases = td.get(2).text();
			System.out.println("---账户别名----" + aliases);

			// 币种
			String currency = td.get(3).text();
			System.out.println("---币种----" + currency);

			// 钞/汇
			String Banknote_remittance = td.get(4).text();
			System.out.println("---钞/汇----" + Banknote_remittance);

			// 当前余额
			String balance = td.get(5).text();
			System.out.println("---当前余额----" + balance);

			// 可用余额
			String available_balance = td.get(6).text();
			System.out.println("可用余额----" + available_balance);
		}
		System.out.println("-----------------------------------------");
		if (bg1.size() > 1) {
			for (int i = 1; i < bg1.size(); i++) {
				Elements td = bg1.get(i).getElementsByTag("td");

				// 子账户序号
				String num = td.get(0).text();
				System.out.print("子账户序号----" + num);

				// 账户类型
				String card_type = td.get(1).text();
				System.out.print("---账户类型----" + card_type);

				// 币种
				String currency = td.get(2).text();
				System.out.print("---币种----" + currency);
				
				// 钞/汇
				String banknote_remittance = td.get(3).text();
				System.out.print("---钞/汇----" + banknote_remittance);
				
				// 余额
				String balance = td.get(4).text();
				System.out.print("---余额----" + balance);
				
				// 年利率（%）
				String interest_rate = td.get(5).text();
				System.out.print("---年利率（%）----" + interest_rate);

				// 存期
				String storge_period = td.get(6).text();
				System.out.print("---存期----" + storge_period);
				
				// 开户日
				String deposit_time = td.get(7).text();
				System.out.print("---开户日----" + deposit_time);
				
				// 到期日
				String interest_dnddate = td.get(8).text();
				System.out.print("---到期日----" + interest_dnddate);
				
				// 状态
				String state = td.get(9).text();
				System.out.println("---状态----" + state);
			}
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

		String baseUrl = "https://perbank.czbank.com/PERBANK/logon.jsp";

		driver.get(baseUrl);

		// driver.findElement(By.id("custId")).sendKeys(Keys.DOWN);

		String accountNum = "6223091000010603753";
		 String password = "123123";

		Thread.sleep(1000L);
		
		driver.findElement(By.id("flag0")).click();

		driver.findElement(By.id("custId")).sendKeys(accountNum);

		// VirtualKeyBoard.KeyPressEx(accountNum, 60);// 输入密码

		InputTab();

		VirtualKeyBoard.KeyPressEx(password, 60);// 输入密码

		InputTab();

		// 验证码
		String path = saveImg(driver, By.id("dynamicPwdPer"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);

		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		// VirtualKeyBoard.KeyPressEx(code, 60);
		driver.findElement(By.id("dynamicPwdPer")).sendKeys(code);

		driver.findElement(By.id("logonBtId")).click();

		Thread.sleep(1000L);

		String errorfinfo = "";
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		try {
			Alert alert = wait.until(new ExpectedCondition<Alert>() {
				@Override
				public Alert apply(WebDriver driver) {
					try {
						return driver.switchTo().alert();
					} catch (NoAlertPresentException e) {
						return null;
					}
				}
			});
			errorfinfo = alert.getText();
			alert.accept();
		} catch (Exception e) {
			// errorfinfo = "登录超时,请重新登录!";
			System.out.println("errorfinfo---------------没有Alert");
		}
		if (errorfinfo != null && errorfinfo.length() > 0) {
			System.out.println("errorfinfo---------------" + errorfinfo);
		} else {
			String currentPageURL = driver.getCurrentUrl();

			System.out.println("currentPageURL---------------" + currentPageURL);

			if (ErrorPage.equals(currentPageURL)) {
				WebElement main = null;
				try {
					main = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("main")).findElements(By.tagName("li")).get(2);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (main != null) {
					errorfinfo = driver.findElement(By.id("main")).findElements(By.tagName("li")).get(2).getText();
					if (errorfinfo != null && errorfinfo.length() > 0) {
						System.out.println("errorfinfo---------------" + errorfinfo);
					} else {
						errorfinfo = "登录超时,请重新登录!";
						System.out.println("errorfinfo---------------" + errorfinfo);
					}
				} else {
					System.out.println("errorfinfo---------------无错误信息");
					WebElement pb_main = null;
					try {
						pb_main = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(By.id("pb_main"));
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (pb_main != null) {
						String dse_sessionId = "";
						String accountNo = "";
						System.out.println("---------------正确登录");
						JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
						driver.switchTo().frame("main");
						driver_js.executeScript(
						"javascript:doTranDispatch('PB020002-PB020200');showMenuPlace('PB250000','PB250200','PB020200');");
						Thread.sleep(1000L);
						driver.switchTo().frame("mainframe");
						String json = driver.getPageSource();
						System.out.println(json);
						Document dse_doc = Jsoup.parse(json);
						dse_sessionId = dse_doc.getElementsByAttributeValue("name", "dse_sessionId").get(0).text();
						String mingxiSearch = "javascript:detailInfo(";
						if (json.contains(mingxiSearch)) {
							json = json.substring(json.indexOf(mingxiSearch)+mingxiSearch.length());
							if (json.contains(");")) {
								json = json.substring(0, json.indexOf(");"));
								json = json.replace("'", "");
								String[] split = json.split(",");
								String accNo = split[0];
								String accBalance = split[1];
								String avlBalance = split[2];
								String accType = split[3];
								String subaccType = split[4];
								String state = split[5];
								String stateInfo = split[6];
								String openNode = split[7];
								String currency = split[8];
								System.out.println(accNo);
								System.out.println(accBalance);
								System.out.println(avlBalance);
								System.out.println(accType);
								System.out.println(subaccType);
								System.out.println(state);
								System.out.println(stateInfo);
								System.out.println(openNode);
								System.out.println(currency);
							}
						}
						
						
						
						
//						String json = driver.getPageSource();
////						System.out.println("pageSource-------" + json);
//						String dse_sessionId = "";
//						if(json.contains("dse_sessionId=")){
//							json = json.substring(json.lastIndexOf("dse_sessionId="), json.length());
//							System.out.println(json);
//							if(json.contains("&")){
//								json = json.substring(json.indexOf("dse_sessionId=")+14, json.lastIndexOf("&"));
//								System.out.println(json);
//								dse_sessionId = json;
//							}
//						}
//						JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
//						Thread.sleep(1000L);
//						driver.switchTo().frame("main");
//						driver_js.executeScript(
//								"javascript:tranSwitch();doTranDispatch('PB250000-PB250000');showMenuPlace('PB250000','','');");
//						Thread.sleep(1000L);
//						driver_js.executeScript(
//								"javascript:doTranDispatch('PB250200-PB250200');DoMenu('PB250200');changLastMenuClass();");
//						Thread.sleep(1000L);
//						driver_js.executeScript(
//								"javascript:doTranDispatch('PB020401-PB020401');changMenuClass('PB020401');");
//						Thread.sleep(1000L);
//						driver.switchTo().frame("mainframe");
//						driver_js.executeScript("javascript:check();");
//						Thread.sleep(1000L);
//						driver.switchTo().defaultContent();
//						
						
						
						WebClient webClient = WebCrawler.getInstance().getWebClient();

						Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

						for (org.openqa.selenium.Cookie cookie : cookies) {
							System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
							webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
									"perbank.czbank.com", cookie.getName(), cookie.getValue()));
						}
						String cookieString = CommonUnit.transcookieToJson(webClient); // 存储cookie
						
						
						 String endDate = getDateBefore("yyyyMMdd", 0);
						 System.out.println(endDate);
						 
						 String startDate = getDateBefore("yyyyMMdd", 12);
						 System.out.println(startDate);
						 
//						getmingxi(webClient, dse_sessionId, startDate, endDate, accountNum, 1);
						
						String html = getmingxi(cookieString, dse_sessionId, startDate, endDate, accountNum, 1);
						Document doc = Jsoup.parse(html);
						Elements elementsByClass2 = doc.getElementsByClass("pageText");
						int pages = 0;
						String count = elementsByClass2.text();
						if (count.length() > 5) {
							count = count.substring(3, count.length() - 2);
						}
						System.out.println("count---------"+count);
						pages = Integer.valueOf(count) / 10 + 1;
						for (int i = 2; i < pages + 1; i++) {
							String htmlPage = getmingxi(cookieString, dse_sessionId, startDate, endDate, accountNum, i);
							Document doc1 = Jsoup.parse(htmlPage);
							Elements elementsByClass21 = doc1.getElementsByClass("pageText");
							String count1 = elementsByClass21.text();
							System.out.println("count1-------"+count1);
						}
						
						
//						getUser(webClient,dse_sessionId , accountNum);
////	//					getcertNo(webClient, dse_sessionId);
//						getdeposit_time(webClient, dse_sessionId, "");

					} else {
						errorfinfo = "登录超时,请重新登录!";
						System.out.println("errorfinfo---------------" + errorfinfo);
					}

				}
			} else {
				errorfinfo = "登录超时,请重新登录!";
				System.out.println("errorfinfo---------------" + errorfinfo);
			}
		}
	}
	
	public static void getdse_sessionId() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\loginS.txt");
		String json = txt2String(file);
		
		if(json.contains("dse_sessionId=")){
			json = json.substring(json.lastIndexOf("dse_sessionId="), json.length());
			System.out.println(json);
			if(json.contains("&")){
				json = json.substring(json.indexOf("dse_sessionId=")+14, json.lastIndexOf("&"));
				System.out.println(json);
			}
		}
		

	}
	
	
	public static String getmingxi(String cookieString, String dse_sessionId,String startDate,String endDate,String accountNo,int nowPage) throws Exception {
		
		WebClient webClient = addcookie(cookieString);
		
		String url = "https://perbank.czbank.com/PERBANK/Trans?"
				+ "dse_sessionId="+dse_sessionId
				+ "&netType="
				+ "&dse_parentContextName="
				+ "&dse_operationName=tradeListQrySrvOp"
//				+ "&dse_pageId=33"
				+ "&dse_processorState="
				+ "&dse_processorId="
				+ "&vonType=0"
				+ "&curtype=01"
				+ "&cashRemit=1"
				+ "&startDate="+startDate
				+ "&endDate="+endDate
				+ "&menuCode=PB020401"
				+ "&accountNo="+accountNo
				+ "&accountType=901"
				+ "&currCode=01"
				+ "&opFlag=0"
				+ "&accNo=";
				if(nowPage>1){
					url = url + "&nowPage="+nowPage;
				}
		
		System.out.println("url--------------"+url);
		
//		Map<String, String> map = new HashMap<String, String>();
//		
//		map.put("Accept", "text/html, application/xhtml+xml, */*");
//		map.put("Accept-Language", "zh-CN");
//		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
//		map.put("Accept-Encoding", "gzip, deflate");
//		map.put("Host", "perbank.czbank.com");
//		map.put("Connection", "Keep-Alive");
		
//			map.put("Cookie",
//				"czbank_PB_logonType=0; td_cookie=18446744070553798910; TrsAccessMonitor=TrsAccessMonitor-1512114694000-1301372759; tma=85694411.50374414.1512114698161.1512114698161.1512114698161.1; tmd=155.85694411.50374414.1512114698161.; bfdid=87205254007bf95200001a6100036e4e5a210a33; bfd_g=87205254007bf95200001a6100036e4e5a210a33; bfd_s=85694411.59706183.1513663413328; tmc=32.85694411.31168525.1513663413329.1513669406192.1513669409326; JSESSIONID=0000tQlPNQqHlaNlnXhUFbnIxYl:1ajmhh9mv");

		Page page1 = getPage(webClient, null, url, HttpMethod.GET, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("nowPage~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + nowPage);
		System.out.println("getmingxi*****************************" + html);
		
		mingxi(html, webClient, dse_sessionId, startDate, endDate, accountNo, nowPage);
		
		return html;
	}
	
	public static String getdeposit_time(WebClient webClient, String dse_sessionId,String certNo) throws Exception {
//		String url1 = "https://perbank.czbank.com/PERBANK/Trans?dse_sessionId=GTIMIJGCDWFOGNEDGZIVEKCGELBIGLFMJCCCDLCS&netType=&dse_parentContextName=&dse_operationName=balanceQrySrvOp&dse_pageId=48&balanceReqIcoll.0.payAccNo=6223091000010603753&balanceReqIcoll.0.payAccType=901&balanceReqIcoll.0.tranOutCurr=01&receiveNum=1&qryFlag=0&subAccType=null&opDownload=0&isSubAcc=0";
		String url1 = "https://perbank.czbank.com/PERBANK/Trans?"
				+ "dse_sessionId="+dse_sessionId
				+ "&netType="
				+ "&dse_parentContextName="
				+ "&dse_operationName=myAssetQrySrvOp"
//				+ "&dse_pageId=48"
				+ "&dse_processorState="
				+ "&dse_processorId="
				+ "&certType=1"
				+ "&certNo="
				+ "&qryFlag=0";

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
//		System.out.println("getdeposit_time*****************************" + html);
		
		user(html, "6223091000010603753");
		
		return html;
	}
	
	
	public static String getcertNo(WebClient webClient, String dse_sessionId) throws Exception {
//		String url1 = "https://perbank.czbank.com/PERBANK/Trans?dse_sessionId=GTIMIJGCDWFOGNEDGZIVEKCGELBIGLFMJCCCDLCS&netType=&dse_parentContextName=&dse_operationName=balanceQrySrvOp&dse_pageId=48&balanceReqIcoll.0.payAccNo=6223091000010603753&balanceReqIcoll.0.payAccType=901&balanceReqIcoll.0.tranOutCurr=01&receiveNum=1&qryFlag=0&subAccType=null&opDownload=0&isSubAcc=0";
		String url1 = "https://perbank.czbank.com/PERBANK/Trans?"
				+ "dse_sessionId="+dse_sessionId
				+ "&netType="
				+ "&dse_parentContextName="
				+ "&dse_operationName=tranDispatchSrvOp"
//				+ "&dse_pageId=48"
				+ "&businessCode=PB020001"
				+ "&menuCode=PB020100"
				+ "&jumpAccNo=";

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getcertNo*****************************" + html);
		Document doc = Jsoup.parse(html);
		String certNo = doc.getElementsByAttributeValue("name", "certNo").val();
		System.out.println("certNo*****************************" + certNo);
		getdeposit_time(webClient, dse_sessionId, certNo);
		
		return html;
	}
	
	
	

	public static String getUser(WebClient webClient, String dse_sessionId, String payAccNo) throws Exception {
//		String url1 = "https://perbank.czbank.com/PERBANK/Trans?dse_sessionId=GTIMIJGCDWFOGNEDGZIVEKCGELBIGLFMJCCCDLCS&netType=&dse_parentContextName=&dse_operationName=balanceQrySrvOp&dse_pageId=48&balanceReqIcoll.0.payAccNo=6223091000010603753&balanceReqIcoll.0.payAccType=901&balanceReqIcoll.0.tranOutCurr=01&receiveNum=1&qryFlag=0&subAccType=null&opDownload=0&isSubAcc=0";
		String url1 = "https://perbank.czbank.com/PERBANK/Trans?"
				+ "dse_sessionId="+dse_sessionId
				+ "&netType="
				+ "&dse_parentContextName="
				+ "&dse_operationName=balanceQrySrvOp"
//				+ "&dse_pageId=48"
				+ "&balanceReqIcoll.0.payAccNo="+payAccNo
				+ "&balanceReqIcoll.0.payAccType=901"
				+ "&balanceReqIcoll.0.tranOutCurr=01&receiveNum=1&qryFlag=0&subAccType=null&opDownload=0&isSubAcc=0";

		// List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		// paramsList1.add(new NameValuePair("dse_sessionId", dse_sessionId));
		// paramsList1.add(new NameValuePair("netType", ""));
		// paramsList1.add(new NameValuePair("dse_parentContextName", ""));
		// paramsList1.add(new NameValuePair("dse_pageId", "37"));
		// paramsList1.add(new NameValuePair("balanceReqIcoll.0.payAccNo",
		// "6223091000010603753"));
		// paramsList1.add(new NameValuePair("balanceReqIcoll.0.payAccType",
		// "901"));
		// paramsList1.add(new NameValuePair("balanceReqIcoll.0.tranOutCurr",
		// "01"));
		// paramsList1.add(new NameValuePair("receiveNum", "1"));
		// paramsList1.add(new NameValuePair("qryFlag", "0"));
		// paramsList1.add(new NameValuePair("subAccType", null));
		// paramsList1.add(new NameValuePair("opDownload", "0"));
		// paramsList1.add(new NameValuePair("isSubAcc", "0"));

//		Map<String, String> map = new HashMap<String, String>();
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

//		map.put("Cookie",
//				"czbank_PB_logonType=0; td_cookie=18446744070553798910; TrsAccessMonitor=TrsAccessMonitor-1512114694000-1301372759; tma=85694411.50374414.1512114698161.1512114698161.1512114698161.1; tmd=90.85694411.50374414.1512114698161.; bfdid=87205254007bf95200001a6100036e4e5a210a33; bfd_g=87205254007bf95200001a6100036e4e5a210a33; bfd_s=85694411.21410090.1513237161281; tmc=7.85694411.3480013.1513237161283.1513239590900.1513239631805; request_temp_url=https%3A%2F%2Fxwcjsc.czbank.com%2FTrackEvent.do%3Fname%3Dclick_v_login%26category%3Dxxx%26action%3Dxxx%26label%3D1%26value%3Dxxx%26uid%3Dxxx%26pageflag%3D1513239590881%26sid%3D85694411.21410090.1513237161281%26fingerprint%3D0c62ad99437f5c1bdc84cbb296abe911%26fpduration%3D42%26cid%3DCzsbank%26d_s%3Dpc%26appkey%3Dc74c4d21e0cee11a6202d85ac9743cbe%26gid%3D87205254007bf95200001a6100036e4e5a210a33; JSESSIONID=0000sdoFOi4Xivy3TkJhOAeX1e7:1ajm2h3pg");

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
//		System.out.println("getUser*****************************" + html);
		
		getding(html);
		
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
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
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
