package test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import org.jsoup.Jsoup;
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

	static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";

	static String LoginPage = "https://my.hfbank.com.cn/perbank/login.htm";
	
	public static String OCR_FILE_PATH="D:/img";
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		try {

			WebClient webClient = WebCrawler.getInstance().getWebClient();
			login();
			// login(webClient);
			
//			getUserInfo();
//			getcardInfo();
			// getsignIn(webClient);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String path =saveImg(driver, By.id("verifyImg"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,path); 
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		VirtualKeyBoard.KeyPressEx(code, 60);
		
//		String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");
//		VirtualKeyBoard.KeyPressEx(inputuserjymtemp, 60);

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
//				System.out.println("pageSource-------" + pageSource);
				getUserInfo(pageSource);
			}
			
//			driver.findElement(By.id("showMyTotalAssetsInfo")).click();
//			Thread.sleep(1000L);
//			driver.findElement(By.id("myBankMoneyNav")).click();
//			Thread.sleep(1000L);
//			driver.findElement(By.id("letidlecashrollDetail")).click();
			
			JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
			Thread.sleep(1500L);
			driver_js.executeScript("queryMyRelatedCard()");
			Thread.sleep(1000L);
			driver.switchTo().frame("openIframe");
			 
			String openIframe = driver.getPageSource();
//			System.out.println("openIframe-------" + openIframe);
			 
			getcardInfo(openIframe);
//			Thread.sleep(1500L);
			
			driver.findElement(By.className("accountInfoTable")).findElements(By.tagName("button")).get(0).click();
			
//			driver_js.executeScript("accountDetails(this,0,738)");
			Thread.sleep(1000L);
//			driver.switchTo().frame("openIframe");
			String openIframe2 = driver.getPageSource();
			System.out.println("openIframe2-------" + openIframe2);
			
			getcard(openIframe2);
			
			
			// driver_js.executeScript("transferDetails(this,0,738);");

//			WebElement btnVerifyCode = null;
//			try {
//				btnVerifyCode = wait.until(new Function<WebDriver, WebElement>() {
//					public WebElement apply(WebDriver driver) {
//						return driver.findElement(By.id("detailQuery"));
//					}
//				});
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			if (btnVerifyCode != null) {
//				System.out.println("--------------id(detailQuery)).click();");
//				driver.findElement(By.id("detailQuery")).click();
//			}

//			String pageSource = driver.getPageSource();
//			System.out.println("pageSource-------" + pageSource);

			WebClient webClient = WebCrawler.getInstance().getWebClient();

			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				// System.out.println(cookie.getName() +
				// "-------cookies--------" + cookie.getValue());

				// Yb6GOJPkvJXKn8ctAd9osA%3D%3D!%40!1
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("my.hfbank.com.cn",
						cookie.getName(), cookie.getValue()));
			}
			// driver.quit();
//			getsignIn(webClient);
//			 存储cookie
			// String cookieString = CommonUnit.transcookieToJson(webClient);

		}

	}
	
	private static void getcard(String json) throws Exception {

		Document doc = Jsoup.parse(json);
		
		String account = doc.getElementById("account").text();
		System.out.println("活期账户--------------" + account);
		
		String amount = doc.getElementById("amount").text();
		System.out.println("活期余额--------------" + amount);
		
		String currencyType = doc.getElementById("currencyType").text();
		System.out.println("币种--------------" + currencyType);
		
		String accountState = doc.getElementById("accountState").text();
		System.out.println("账户状态--------------" + accountState);

	}
	
	
	private static void getcardInfo(String json) throws Exception {

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
	
	private static void getcardInfo() throws Exception {

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
	
	
	
	private static void getUserInfo(String json) throws Exception {

		Document doc = Jsoup.parse(json);
		// 卡片持有人
		String m_userName = doc.getElementById("m_userName").text();
		System.out.println("卡片持有人--------------" + m_userName);
		if(m_userName.contains("！")){
			m_userName = m_userName.replaceAll("！", "");
		}
		System.out.println("卡片持有人--------------" + m_userName);
		
		String customerMobile = doc.getElementsByClass("customerMobile").text();
		System.out.println("手机号码--------------" + customerMobile);

	}
	
	private static void getUserInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\loginS.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		// 卡片持有人
		String m_userName = doc.getElementById("m_userName").text();
		System.out.println("卡片持有人--------------" + m_userName);
		if(m_userName.contains("！")){
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
	public static String getZH(WebClient webClient, String appKey, String customerEnc) throws Exception {
		String url1 = "https://my.hfbank.com.cn/perbank/accountQueryOverView.do?appKey=EDFODKFXFTENBHJKEIHRGZEQDUDEHYIFBBGLHYHU&customerEnc=MjAwMDg2MTY5NXw%3D&format=JSON&channel=WEB&Math=0.26504237161745814";
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		// paramsList1.add(new NameValuePair("ClientNo", ClientNo));

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, null);
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

		Page page1 = getPage(webClient, null, url1, HttpMethod.POST, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("getUser*****************************" + html);
		return html;
	}

	public static String getsignIn(WebClient webClient) throws Exception {
		try {
			String url1 = "https://my.hfbank.com.cn/perbank/signIn.do";
			// String inputuserjymtemp =
			// JOptionPane.showInputDialog("请输入验证码……");

			List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
			paramsList1.add(new NameValuePair("logonPassword", "iP2GcP3FHncolgHb4izx8g%3D%3D!%40!1"));
			paramsList1.add(new NameValuePair("netWorkInfo", "HSdiw6zQyIEeXQcB9fU8%2FNf0BPqrG9Q2iSZ2mOC1jKM%3D"));
			// paramsList1.add(new NameValuePair("checkCode",
			// inputuserjymtemp));
			paramsList1.add(new NameValuePair("logonType", "1"));
			paramsList1.add(new NameValuePair("logonId", "340321198602178635"));
			paramsList1.add(new NameValuePair("format", "JSON"));
			paramsList1.add(new NameValuePair("channel", "WEB"));
			// paramsList1.add(new NameValuePair("Math", "0.1655655335803895"));
			Map<String, String> map = new HashMap<String, String>();
			// map.put("Cookie",
			// "JSESSIONID=Wjcff780sPTD6_SrW4MjzzWKlglnc5uaP2gyyhw4iRKE0RPuNaw0!-883867200");

			Page page1 = getPage(webClient, null, url1, HttpMethod.POST, paramsList1, null, null, map);
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
	public static String getNextLabelByKeyword(Document document, String keyword){
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}

}
