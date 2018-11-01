package test.housingfund.chengdu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.chengdu.HousingChengduPay;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class ChengduTestChrome {

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// String
			// cookies="[{\"domain\":\"www.cdzfgjj.gov.cn\",\"key\":\"loginType\",\"value\":\"4\"},{\"domain\":\"www.cdzfgjj.gov.cn\",\"key\":\"JSESSIONID1\",\"value\":\"F7cpHjMeP2sfZN7-ijNAo1284WlWHyu6vcP1xar5uyOXppdSKEAn!499450747\"},{\"domain\":\"www.cdzfgjj.gov.cn\",\"key\":\"userType\",\"value\":\"gr\"}]";
			String username = "5105251988052330222";
			String password = "233022";
			String cookie = login(username, password);
			
//			 String cookie1 = login1();
			// webClient = addcookie(cookie);
			// getUserInfo(webClient);
			// getData(webClient);

			// getUserInfo();
//			getPay();
			// String json="Base.setSelectInputDataWidthJson(\"year\",
			// [{\"id\":\"2017\",\"name\":\"2017年\"},{\"id\":\"2016\",\"name\":\"2016年\"},{\"id\":\"2015\",\"name\":\"2015年\"}]);";
			// String json = "var yearList =
			// $.parseJSON('[{\"id\":\"2017\",\"name\":\"2017年\"},{\"id\":\"2016\",\"name\":\"2016年\"},{\"id\":\"2015\",\"name\":\"2015年\"}]');";
			//
			// String idtype = "";
			// if (json.contains("yearList = $.parseJSON('")) {
			// String slectInput_idtype = json.substring(json.indexOf("yearList
			// = $.parseJSON('"));
			// System.out.println(slectInput_idtype);
			// String[] split = slectInput_idtype.split("'");
			// if (split.length > 1) {
			// idtype = split[1];
			// System.out.println(idtype);
			// }
			// }
//			List<String> list = new ArrayList<String>();
//			list.add("first");
//			list.add("second");
//			list.add("third");
//			list.add("four");
//			list.add("five");
//			list.forEach((String num) -> {
//				System.out.println(num);
//			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String login1() throws Exception {

		ChromeDriver driver = intiChrome();
		driver.quit();
		
		return null;

	}

	private static WebClient getData(WebClient webClient) throws Exception {

		String url = "https://www.cdzfgjj.gov.cn:9802/cdnt/per/depositRecordQueryAction.do?menuid=259597";
		Page html = getPage(webClient, url, null, null, false);
		String json = html.getWebResponse().getContentAsString();
		System.out.println("json---------------" + json);

		List<String> list = new ArrayList<String>();
		String idtype = "";

		if (json.contains("yearList = $.parseJSON('")) {
			String slectInput_idtype = json.substring(json.indexOf("yearList = $.parseJSON('"));
			System.out.println("yearList---------------" + slectInput_idtype);
			String[] split = slectInput_idtype.split("'");
			if (split.length > 1) {
				idtype = split[1];
				System.out.println(idtype);
				Object obj = new JSONTokener(idtype).nextValue();
				if (obj instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) obj;
					String data = jsonObject.getString("id");
					list.add(data);

				} else if (obj instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) obj;
					for (Object object : jsonArray) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						String data = jsonObject.getString("id");
						list.add(data);
					}
				}

			}
		}
		System.out.println("list------------" + list.toString());
		return webClient;
	}

	private static void getUserInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\sub.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		// 个人账号
		String percode = doc.getElementById("percode").val();
		System.out.println("个人账号" + percode);

		// 个人姓名
		String pername1 = doc.getElementById("pername1").val();
		System.out.println("个人姓名" + pername1);

		// 证件类型
		String idtype = "";
		if (json.contains("slectInput_idtype[0]")) {
			String slectInput_idtype = json.substring(json.indexOf("slectInput_idtype[0]"));
			String[] split = slectInput_idtype.split("\"");
			if (split.length > 1) {
				idtype = split[1];
				System.out.println("证件类型" + idtype);
			}
		}

		// 证件号码
		String idcard1 = doc.getElementById("idcard1").val();
		System.out.println("证件号码" + idcard1);

		// 出生日期
		String birthday = doc.getElementById("birthday").val();
		System.out.println("出生日期" + birthday);

		// 个人邮箱
		String mail = doc.getElementById("mail").val();
		System.out.println("个人邮箱" + mail);

		// 移动电话
		String phone1 = doc.getElementById("phone1").val();
		System.out.println("移动电话" + phone1);

		// 面签状态
		String signflag_desc = "";
		if (json.contains("slectInput_signflag[0]")) {
			String slectInput_idtype = json.substring(json.indexOf("slectInput_signflag[0]"));
			String[] split = slectInput_idtype.split("\"");
			if (split.length > 1) {
				signflag_desc = split[1];
				System.out.println("面签状态" + signflag_desc);
			}
		}

		// 发卡银行
		String banktype = "";
		if (json.contains("slectInput_banktype[0]")) {
			String slectInput_idtype = json.substring(json.indexOf("slectInput_banktype[0]"));
			String[] split = slectInput_idtype.split("\"");
			if (split.length > 1) {
				banktype = split[1];
				System.out.println("发卡银行" + banktype);
			}
		}

		// 联名卡卡号
		String bankcode = doc.getElementById("bankcode").val();
		System.out.println("联名卡卡号" + bankcode);

		// 缴存基数
		String basemny = "";
		if (json.contains("\"basemny\":\"")) {
			String slectInput_idtype = json.substring(json.indexOf("\"basemny\":\""));
			String[] split = slectInput_idtype.split("\"");
			if (split.length > 3) {
				basemny = split[3];
				System.out.println("联名卡卡号" + basemny);
			}
		}

	}

	private static void getUserInfoJ() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\bb.txt");
		String json = txt2String(file);
		List<HousingChengduPay> list = new ArrayList<HousingChengduPay>();
		HousingChengduPay housingChengduPay = null;

		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject listsJson = jsonObj.getJSONObject("lists");
		JSONObject accDataListJson = listsJson.getJSONObject("accDataList");
		String listJson = accDataListJson.getString("list");
		Object obj = new JSONTokener(listJson).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 账户状态
			String mperpaystate = jsonObject.getString("mperpaystate");
			// 单位名称
			String corpname = jsonObject.getString("corpname");
			// 姓名
			String pername = jsonObject.getString("pername");
			// 单位账号
			String corpcode = jsonObject.getString("corpcode");
			// 缴存基数
			String basemny = jsonObject.getString("basemny");
			// 个人缴存额(元)
			String perdepmny = jsonObject.getString("perdepmny");
			// 合计(元)
			String depmny = jsonObject.getString("depmny");
			// 业务类型 '1':'汇缴' '2':'补缴'
			String bustype = jsonObject.getString("bustype");

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);

			}
		}
		System.out.println(list.toString());

	}

	private static void getPay() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\aa.txt");
		String json = txt2String(file);
		List<HousingChengduPay> list = new ArrayList<HousingChengduPay>();
		HousingChengduPay housingChengduPay = null;

		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject listsJson = jsonObj.getJSONObject("lists");
		JSONObject dataListJson = listsJson.getJSONObject("dataList");
		String listJson = dataListJson.getString("list");
		Object obj = new JSONTokener(listJson).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 单位名称
			String corpname = jsonObject.getString("corpname");
			// 缴存年月起
			String paybmnh = jsonObject.getString("paybmnh");
			// 缴存年月止
			String payemnh = jsonObject.getString("payemnh");
			// 缴款时间
			String acctime = jsonObject.getString("acctime");
			// 单位缴存额(元)
			String corpdepmny = jsonObject.getString("corpdepmny");
			// 个人缴存额(元)
			String perdepmny = jsonObject.getString("perdepmny");
			// 合计(元)
			String depmny = jsonObject.getString("depmny");
			// 业务类型 '1':'汇缴' '2':'补缴'
			String bustype = jsonObject.getString("bustype");
			// 单位账号
			String corpcode = jsonObject.getString("corpcode");
			housingChengduPay = new HousingChengduPay("", corpname, paybmnh, payemnh, acctime, corpdepmny.trim(),
					perdepmny.trim(), depmny.trim(), bustype, corpcode);
			list.add(housingChengduPay);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 单位名称
				String corpname = jsonObject.getString("corpname");
				// 缴存年月起
				String paybmnh = jsonObject.getString("paybmnh");
				// 缴存年月止
				String payemnh = jsonObject.getString("payemnh");
				// 缴款时间
				String acctime = jsonObject.getString("acctime");
				// 单位缴存额(元)
				String corpdepmny = jsonObject.getString("corpdepmny");
				// 个人缴存额(元)
				String perdepmny = jsonObject.getString("perdepmny");
				// 合计(元)
				String depmny = jsonObject.getString("depmny");
				// 业务类型 '1':'汇缴' '2':'补缴'
				String bustype = jsonObject.getString("bustype");
				// 单位账号
				String corpcode = jsonObject.getString("corpcode");
				housingChengduPay = new HousingChengduPay("", corpname, paybmnh, payemnh, acctime, corpdepmny.trim(),
						perdepmny.trim(), depmny.trim(), bustype, corpcode);
				list.add(housingChengduPay);
			}
		}
		System.out.println(list.toString());

	}

	private static WebClient getUserInfo(WebClient webClient) throws Exception {

		String url = "https://www.cdzfgjj.gov.cn:9802/cdnt/per/querPerInfo.do?menuid=259333";
		Page html = getPage(webClient, url, null, null, false);
		String json = html.getWebResponse().getContentAsString();
		System.out.println("json---------------" + json);

		// idtype

		// String url2 =
		// "https://www.cdzfgjj.gov.cn:9802/cdnt/per/querPerInfo.do?menuid=259333";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();
		// System.out.println("json---------------"+json2);

		return webClient;
	}

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {

			return searchPage;
		}

		return null;
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
	public static Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList,
			Boolean code) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate,
		// br");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Cookie",
		// "userType=gr; loginType=4;
		// JSESSIONID1=F7cpHjMeP2sfZN7-ijNAo1284WlWHyu6vcP1xar5uyOXppdSKEAn!499450747");
		// webRequest.setAdditionalHeader("Host", "www.cdzfgjj.gov.cn:9802");
		// webRequest.setAdditionalHeader("Referer",
		// "https://www.cdzfgjj.gov.cn:9802/cdnt/indexAction.do?_r=0.5112816707954304");
		// webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		// webRequest.setAdditionalHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like
		// Gecko) Chrome/61.0.3163.100 Safari/537.36");
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}

		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();

		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	public static ChromeDriver intiChrome() throws Exception {
		String driverPath = "D:\\WD\\s\\chromedriver\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static String login(String username ,String password) throws Exception {

		ChromeDriver driver = intiChrome();

		driver.manage().window().maximize();

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "https://www.cdzfgjj.gov.cn:9802/cdnt/login.jsp#per";
		driver.get(baseUrl);

		WebElement adrOption = driver.findElement(By.cssSelector("#aType option[value='4']"));
		adrOption.click();
		Thread.sleep(2000);
		driver.findElement(By.id("j_username")).sendKeys(username);
		Thread.sleep(2000);
		driver.findElement(By.id("j_password")).sendKeys(password);
		Thread.sleep(2000);

		Actions action = new Actions(driver);
		// 获取滑动滑块的标签元素
		WebElement source = driver.findElement(By.xpath("//div[@class='Slider ui-draggable']"));
		// parser.clickAndHold(source).moveByOffset(313, 0);
		// 确保每次拖动的像素不同，故而使用随机数
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
		Thread.sleep(2000);
		action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
		Thread.sleep(2000);
		// 拖动完释放鼠标
		action.moveToElement(source).release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();

		// Actions parser = new Actions(driver);
		// WebElement source = driver.findElement(By.xpath("//div[@class='Slider
		// ui-draggable']"));
		// // 点击并按住
		// Actions clickAndHold = parser.clickAndHold(source);
		// // 移动至
		// Actions moveByOffset = clickAndHold.moveByOffset(35, 0);
		// Thread.sleep(2000);
		// Actions moveByOffset2 = clickAndHold.moveByOffset(36, 0);
		// Thread.sleep(2000);
		// Actions moveByOffset3 = clickAndHold.moveByOffset(49, 0);
		// Thread.sleep(2000);
		// Actions moveByOffset4 = clickAndHold.moveByOffset(78, 0);
		// Thread.sleep(2000);
		// Actions moveByOffset5 = clickAndHold.moveByOffset(56, 0);
		// Thread.sleep(2000);
		// Actions moveByOffset6 = clickAndHold.moveByOffset(165, 0);
		// Thread.sleep(2000);
		// // 释放
		// moveByOffset.release();
		// // 组织完这些一系列的步骤，然后开始真实执行操作
		// Action actions = parser.build();
		// actions.perform();

		Thread.sleep(3000);
		driver.findElement(By.id("btn-login")).click();
		String cookieJson = "";
		Thread.sleep(5000);
		String text = driver.findElement(By.id("error")).getText();
		System.out.println("text---------" + text);
		if (text == null || "".equals(text)) {
			System.out.println("#########登录成功");
			// Thread.sleep(5000);
			try {
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
						.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
				WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("259333"));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				text = driver.findElement(By.id("error")).getText();
			}

			// Set<org.openqa.selenium.Cookie> cookies =
			// driver.manage().getCookies();
			// WebClient webClient = WebCrawler.getInstance().getWebClient();
			//
			// for (org.openqa.selenium.Cookie cookie : cookies) {
			// System.out.println(cookie.getName() + "-------cookies--------" +
			// cookie.getValue());
			// webClient.getCookieManager().addCookie(new
			// com.gargoylesoftware.htmlunit.util.Cookie(cookie.getDomain(),
			// cookie.getName(), cookie.getValue()));
			// }
			//
			// cookieJson = CommonUnit.transcookieToJson(webClient);

			// 获取cookies
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
			for (org.openqa.selenium.Cookie cookie : cookies) {
				CookieJson cookiejson = new CookieJson();
				cookiejson.setDomain(cookie.getDomain());
				cookiejson.setKey(cookie.getName());
				cookiejson.setValue(cookie.getValue());
				cookiesSet.add(cookiejson);
			}
			cookieJson = new Gson().toJson(cookiesSet);
			System.out.println("cookieJson---------------------" + cookieJson);
		} else {
			System.out.println("#########登录失败");
		}
		System.out.println("text---------ERROR:" + text);
		driver.quit();
		return cookieJson;

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

}
