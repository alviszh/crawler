package app.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBillNow;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import app.bean.CodeMsgBean;
import app.parser.BocomCreditcardParse;
import app.unit.AbstractTesseserHandlerByTess4j;
import app.unit.CommonUnitForBocomCreditcard;
import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class XYBankTest6 extends AbstractChaoJiYingHandler {

	public static final Set<Integer> EXCLUDED_COLOR_RGBS = new HashSet<Integer>();

	static {
		EXCLUDED_COLOR_RGBS.add(new Color(147, 196, 236).getRGB());
		EXCLUDED_COLOR_RGBS.add(new Color(110, 110, 110).getRGB());
	}
	static Boolean headless = false;

	public static final HashMap<String, String> map = new HashMap<String, String>();

	static {
		map.put("q", "10");
		map.put("w", "11");
		map.put("e", "12");
		map.put("r", "13");
		map.put("t", "14");
		map.put("y", "15");
		map.put("u", "16");
		map.put("i", "17");
		map.put("o", "18");
		map.put("p", "19");
		map.put("a", "20");
		map.put("s", "21");
		map.put("d", "22");
		map.put("f", "23");
		map.put("g", "24");
		map.put("h", "25");
		map.put("j", "26");
		map.put("k", "27");
		map.put("l", "28");
		map.put("z", "29");
		map.put("x", "30");
		map.put("c", "31");
		map.put("v", "32");
		map.put("b", "33");
		map.put("n", "34");
		map.put("m", "35");
		map.put("Q", "36");
		map.put("W", "37");
		map.put("E", "38");
		map.put("R", "39");
		map.put("T", "40");
		map.put("Y", "41");
		map.put("U", "42");
		map.put("I", "43");
		map.put("O", "44");
		map.put("P", "45");
		map.put("A", "46");
		map.put("S", "47");
		map.put("D", "48");
		map.put("F", "49");
		map.put("G", "50");
		map.put("H", "51");
		map.put("J", "52");
		map.put("K", "53");
		map.put("L", "54");
		map.put("Z", "55");
		map.put("X", "56");
		map.put("C", "57");
		map.put("V", "58");
		map.put("B", "59");
		map.put("N", "60");
		map.put("M", "61");
		map.put("\"", "62");
		map.put(";", "63");
		map.put("~", "64");
		map.put("'", "66");
		map.put(".", "67");
		map.put("@", "68");
		map.put("+", "69");
		map.put("\\", "70");
		map.put("$", "71");
		map.put(",", "72");
		map.put("&", "73");
		map.put("/", "74");
		map.put(">", "75");
		map.put("{", "76");
		map.put("^", "77");
		map.put("*", "78");
		map.put("(", "79");
		map.put("!", "80");
		map.put("`", "81");
		map.put("-", "82");
		map.put("_", "83");
		map.put("}", "84");
		map.put("?", "85");
		map.put("%", "86");
		map.put("|", "87");
		map.put("[", "88");
		map.put(":", "89");
		map.put(" ", "90");
		map.put("=", "91");
		map.put("#", "92");
		map.put("<", "93");
		map.put(")", "94");
	}

	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	static String driverPath = "C:\\chromedriver.exe";

	private static final String OCR_FILE_PATH = "D:/home/img/tr";

	public static void main(String[] args) throws Exception {
		login();
//		for(int i=0;i<10;i++){
//			test();
//
//		}
	}

	public static void test() throws Exception {

		String url = "http://zxgk.court.gov.cn/shixin/captcha.do?captchaId=94a77eec5d7a4fdb91406ef2f777e3d4&random=0.20323006239254293";

		Connection con = Jsoup.connect(url).header("Content-Type", "image/jpeg");

		String imgagePath = null;
		try {
			Response response = con.ignoreContentType(true).execute();
			File codeImageFile = getImageLocalPath();

			imgagePath = codeImageFile.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));

			out.write(response.bodyAsBytes());
			out.close();
			// saveImageStream(imgagePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(imgagePath);
//		ImageHandler.imagePreHandle(new File(imgagePath), null);

		String code = AbstractTesseserHandlerByTess4j.getVerifycodeByTess4jFoTest(new File(imgagePath)).trim();

		System.out.println(code);
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-bocom-creditcard 所属包名：app.test 类描述： 创建人：hyx
	 * 创建时间：2018年5月28日
	 * 
	 * @version 1 返回值 void
	 * @return
	 */
	public static Object login() throws Exception {

		WebDriver driver = intiChrome();
		// driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://creditcardapp.bankcomm.com/idm/sso/login.html?service=https://creditcardapp.bankcomm.com/member/shiro-cas";
		driver.get(baseUrl);

		Thread.sleep(1000L);
		driver.findElement(By.id("cardNo")).clear();
		driver.findElement(By.id("cardNo")).sendKeys("5218995914789795");

		Thread.sleep(1000L);
		WebElement eleCardpassword = driver.findElement(By.id("cardpassword"));
		eleCardpassword.click(); // 点击密码输入框，为了让安全键盘弹出，以便截图
		Thread.sleep(1000L);

		String path = CommonUnitForBocomCreditcard.saveImgXY(driver, By.cssSelector("div.key-con li:nth-child(1)"),
				By.cssSelector("div.key-con li:nth-child(10)"));
		System.out.println("path---------------" + path);

		Thread.sleep(1000L);

		String code = AbstractTesseserHandlerByTess4j.getVerifycodeByTess4jFoTest(new File(path)).trim();

		System.out.println("code------------" + code);
		System.out.println("code------length------" + code.length());

		String pot = CommonUnitForBocomCreditcard.getPot(code, "124587", 0);

		System.out.println("pot-----------" + pot);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('cardpassword').setAttribute('data-val', '" + pot + "')");

		WebElement eleDetermine_btn_fuceng = driver.findElement(By.className("close_overlay"));
		eleDetermine_btn_fuceng.click();// 关闭页面浮层,防止浮层影响点击

		WebElement eleDetermine_btn = driver.findElement(By.className("determine_btn"));

		eleDetermine_btn.click(); // 点击安全键盘上的确认键,关闭安全键盘
		Thread.sleep(1000L);

		driver.findElement(By.id("cardLogin")).click();

		new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		try {
			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(By.className("userinf"));
				}
			});
		} catch (Exception e) {

			if (driver.getPageSource().indexOf("尊敬的用户，由于您长期未登录我行信用卡官方网站，为了您账户安全，请您完成验证，验证成功后即可完成登录，感谢配合") != -1) {
				System.out.println("================需要短信验证码验证====================");
			} else {
				Document doc = Jsoup.parse(driver.getPageSource());

				String error_msg = doc.select("div.errormsg").first().text();

				System.out.println("=======" + error_msg);

				return null;
			}

		}

		// System.out.println(BocomCreditcardParse.getCardnum(driver.getPageSource()));

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		String cardNo = getCardNo(webClient);
		getBillNow(webClient, cardNo);
		// getBillNow2(webClient, cardNo);

		// Document doc = Jsoup.parse(driver.getPageSource());
		//
		// String username = doc.select("input#username").attr("value");
		//
		// System.out.println("=======username==========" + username);
		//
		// getCode(username, webClient);
		//
		// String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		// setcode(username, valicodeStr, webClient);

		// Set<org.openqa.selenium.Cookie> cookies_driver =
		// driver.manage().getCookies();
		driver.close();

		return null;

		/*
		 * WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		 * Set<org.openqa.selenium.Cookie> cookiesDriver =
		 * driver.manage().getCookies();
		 * 
		 * 
		 * for (org.openqa.selenium.Cookie cookie : cookiesDriver) { Cookie
		 * cookieWebClient = new Cookie("creditcardapp.bankcomm.com",
		 * cookie.getName(), cookie.getValue());
		 * 
		 * webClient.getCookieManager().addCookie(cookieWebClient); }
		 * 
		 * BocomCreditcardParse.balance_parse(getBill(
		 * webClient).getWebResponse().getContentAsString());
		 * 
		 * String url_urerinfo =
		 * "https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html";
		 * driver.get(url_urerinfo); Thread.sleep(5000);
		 * 
		 * cookiesDriver = driver.manage().getCookies();
		 * 
		 * webClient = WebCrawler.getInstance().getNewWebClient(); for
		 * (org.openqa.selenium.Cookie cookie : cookiesDriver) { Cookie
		 * cookieWebClient = new Cookie("creditcardapp.bankcomm.com",
		 * cookie.getName(), cookie.getValue());
		 * 
		 * webClient.getCookieManager().addCookie(cookieWebClient); }
		 * System.out.println("=====url_urerinfo==========" +
		 * driver.getPageSource()); driver.quit();
		 * 
		 * BocomCreditcardParse.userinfo_parse(getUserInfo(webClient).
		 * getWebResponse().getContentAsString());
		 */
	}

	public static WebClient getCode(String username, WebClient webClient) throws Exception {

		String getcode_url = "https://creditcardapp.bankcomm.com/idm/sso/sendDynamicCode.json?v=0.5344162919169624";

		WebRequest webRequest = new WebRequest(new URL(getcode_url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webRequest.setCharset(Charset.forName("UTF-8"));

		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "creditcardapp.bankcomm.com");
		webRequest.setAdditionalHeader("Origin", "https://creditcardapp.bankcomm.com");
		webRequest.setAdditionalHeader("Referer", "https://creditcardapp.bankcomm.com/idm/sso/auth.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", username));
		paramsList.add(new NameValuePair("mobile", "18610007920"));

		webRequest.setRequestParameters(paramsList);

		Page getcode_page = webClient.getPage(webRequest);

		CodeMsgBean codeMsgBean = BocomCreditcardParse
				.codeMsg_parse(getcode_page.getWebResponse().getContentAsString());

		System.out.println("==========发送验证码=======codeMsgBean==" + codeMsgBean.getResult());
		System.out.println("==========发送验证码=======codeMsgBean==" + codeMsgBean.getError());

		System.out.println("==========发送验证码=======codeMsgBean==" + codeMsgBean.getMsg());

		return webClient;
	}

	public static void setcode(String username, String code, WebClient webClient) throws Exception {

		String setcode_url = "https://creditcardapp.bankcomm.com/idm/sso/verifyDynamicCode.json";

		WebRequest webRequest = new WebRequest(new URL(setcode_url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webRequest.setCharset(Charset.forName("UTF-8"));

		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "creditcardapp.bankcomm.com");
		webRequest.setAdditionalHeader("Origin", "https://creditcardapp.bankcomm.com");
		webRequest.setAdditionalHeader("Referer", "https://creditcardapp.bankcomm.com/idm/sso/auth.html");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", username));
		paramsList.add(new NameValuePair("mobile", "18610007920"));
		paramsList.add(new NameValuePair("pwd", code));

		webRequest.setRequestParameters(paramsList);

		Page setcode_Page = webClient.getPage(webRequest);

		CodeMsgBean codeMsgBean = BocomCreditcardParse
				.codeMsg_parse(setcode_Page.getWebResponse().getContentAsString());

		System.out.println("==========验证验证码=======codeMsgBean==" + codeMsgBean.getResult());
		System.out.println("==========验证验证码=======codeMsgBean==" + codeMsgBean.getError());

		System.out.println("==========验证验证码=======codeMsgBean==" + codeMsgBean.getMsg());

	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
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

	// 根据dom节点截取图片
	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
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

	public static Page getBill(WebClient webClient) throws Exception, IOException {
		String url = "https://creditcardapp.bankcomm.com/member/member/service/billing/finished.html" + "?cardNo="
				+ "5218%20****%20****%208975" + "&billDate=" + "20171105";

		System.out.println("=================getBill================");
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		// webClient.addRequestHeader("Accept", "*/*");
		// webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		// webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"
		 * ); webClient.addRequestHeader("Referer",
		 * "https://ebsnew.boc.cn/boc15/welcome_ele.html?v=20170907091405353&locale=zh&login=card&segment=1"
		 * ); // webClient.addRequestHeader("X-Requested-With",
		 * "XMLHttpRequest");
		 */
		// webRequest.setRequestBody(
		// "{\"header\":{\"local\":\"zh_CN\",\"agent\":\"WEB15\",\"bfw-ctrl\":\"json\",\"version\":\"\",\"device\":\"\",\"platform\":\"\",\"plugins\":\"\",\"page\":\"\",\"ext\":\"\"},\"request\":[{\"id\":6,\"method\":\"PsnAccBocnetQryLoginInfo\",\"conversationId\":null,\"params\":null}]}");
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		return searchPage;
	}

	public static Page getUserInfo(WebClient webClient) throws Exception, IOException {

		String url = "https://creditcardapp.bankcomm.com/sac/user/account/index/baseinfo.json";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		for (Cookie cookie : cookies) {
			System.out.println(cookie.toString());
		}
		return searchPage;
	}

	public static String getCardNo(WebClient webClient) throws Exception, Exception {

		String url = "https://creditcardapp.bankcomm.com/member/member/service/billing/index.html";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		Page searchPage = webClient.getPage(webRequest);

		Document doc = Jsoup.parse(searchPage.getWebResponse().getContentAsString());

		String cardNo = doc.select("div.slider-content").attr("val");

		return cardNo;
	}

	public static void getBillNow(WebClient webClient, String cardNo) throws Exception, Exception {

		String url = "https://creditcardapp.bankcomm.com/member/member/limit/info.json";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("cardNo", cardNo));

		webRequest.setRequestParameters(paramsList);
		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());

		BocomCreditcardBillNow bocomCreditcardBillNow = BocomCreditcardParse
				.billnow_parse(searchPage.getWebResponse().getContentAsString());

		bocomCreditcardBillNow = getBillNow2(webClient, cardNo, bocomCreditcardBillNow);
	}

	public static BocomCreditcardBillNow getBillNow2(WebClient webClient, String cardNo,
			BocomCreditcardBillNow bocomCreditcardBillNow) throws Exception, Exception {

		String url = "https://creditcardapp.bankcomm.com/member/member/service/billing/balanceQry.html?" + "cardNo="
				+ (cardNo.trim().replaceAll(" ", "%20")) + "&_=" + System.currentTimeMillis();

		// String url =
		// "https://creditcardapp.bankcomm.com/member/member/service/billing/balanceQry.html?"
		// + "cardNo=5218%20****%20****%209795"
		// + "&_=1528253743915";

		System.out.println(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60

		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		webClient.addRequestHeader("Host", "creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Origin", "https://creditcardapp.bankcomm.com");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer",
				"https://creditcardapp.bankcomm.com/sac/user/account/index.html?fromUrl=https://creditcardapp.bankcomm.com/member/member/home/index.html");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		/*
		 * webClient.addRequestHeader("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
		 * );
		 */
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("cardNo", cardNo));

		webRequest.setRequestParameters(paramsList);
		Page searchPage = webClient.getPage(webRequest);

		System.out.println(searchPage.getWebResponse().getContentAsString());
		bocomCreditcardBillNow = BocomCreditcardParse.billnow_parse2(searchPage.getWebResponse().getContentAsString(),
				bocomCreditcardBillNow);

		System.out.println(bocomCreditcardBillNow.toString());

		return bocomCreditcardBillNow;

	}

}
