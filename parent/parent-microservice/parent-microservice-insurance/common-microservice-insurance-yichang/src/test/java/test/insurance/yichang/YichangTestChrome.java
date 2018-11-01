package test.insurance.yichang;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class YichangTestChrome {
	
	public static String OCR_FILE_PATH = "D:/img";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = "[{\"domain\":\"61.136.223.44\",\"key\":\"targetUrl\",\"value\":\"{%22type%22:%221%22%2C%22url%22:%22ehome.html%22}\"},{\"domain\":\"61.136.223.44\",\"key\":\"ASP.NET_SessionId\",\"value\":\"dd4idjwxpbuj2qd5o5mpb0ax\"},{\"domain\":\"61.136.223.44\",\"key\":\"CheckCode\",\"value\":\"82257\"},{\"domain\":\"61.136.223.44\",\"key\":\"key\",\"value\":\"{%22sfzhm%22:%22Zmd2bndwam1raGRyZWxnbWlo%22%2C%22token%22:%22B468F15EEE64A59804C4C69622F4F5DC%22%2C%22logintime%22:%22ZGV3cXJnbWFkZF9zbG9jb3Jpdg\u003d\u003d%22%2C%22name%22:%22wo7Cqn5xwotrwpXCqWloaHXCkMKqaW9xbA\u003d\u003d%22%2C%22tel%22:%22Y2p3cHZvcGxqaWo\u003d%22%2C%22type%22:%221%22%2C%22email%22:%22Ug\u003d\u003d%22%2C%22note%22:%22420501%22}\"}]";
			String username = "420529198610174513";
			String password = "861017";
			String cookie = login(username, password);
			WebClient webclient = addcookie(cookie);
			cookies = URLDecoder.decode(cookies, "UTF-8");
			System.out.println(cookies);
			cookies = cookies.replace("\"{", "{");
			cookies = cookies.replace("}\"", "}");
			JSONArray object = JSONArray.fromObject(cookies);
			for (Object object2 : object) {
				JSONObject fromObject = JSONObject.fromObject(object2);
				if (fromObject.getString("key").equals("key")) {
					String value = fromObject.getString("value");
					System.out.println(value);
					JSONObject valuejson = JSONObject.fromObject(value);
					String sfzhm = valuejson.getString("sfzhm");
					String token = valuejson.getString("token");
					String type = valuejson.getString("type");
					System.out.println(sfzhm);
					System.out.println(token);
					System.out.println(type);
					TestService.getloginInfo(webclient, token, sfzhm, type);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

//		driver.manage().window().maximize();

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "http://61.136.223.44/web2/src/index/index.html";
		
		String loginUrl = "http://61.136.223.44/web2/src/index/login.html";
		
		
		driver.get(loginUrl);
		
//		Thread.sleep(10000);
//		
//		
//		WebElement menudl = null;
//		try {
//			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
//					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//			menudl = wait.until(new Function<WebDriver, WebElement>() {
//				public WebElement apply(WebDriver driver) {
//					return driver.findElement(By.id("menu-dl"));
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if(menudl != null){
//			
//			 Actions action = new Actions(driver);// 鼠标左键在当前停留的位置做单击操作
//			 action.click(menudl);// 鼠标左键点击指定的元素 
			 
//			Actions action = new Actions(driver);
//			action.click();
//			action.moveToElement(menudl).release();
//			Action actions = action.build();
//			actions.perform();
			
//			menudl.click();
//		}
		
		Thread.sleep(1000);
//		Thread.sleep(10000);
//		driver.switchTo().defaultContent();

//		driver.switchTo().frame("mainFrame");
		
		
		driver.findElement(By.id("sfz")).sendKeys(username);
		Thread.sleep(2000);
		driver.findElement(By.id("password")).sendKeys(password);
		Thread.sleep(2000);
		
		// 验证码
		String path = saveImg(driver, By.cssSelector("#loginTable > tbody > tr > td > table > tbody > tr:nth-child(5) > td:nth-child(3) > img"));
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);

		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		driver.findElement(By.id("captcha")).sendKeys(code);

		Thread.sleep(3000);
		driver.findElement(By.id("personalLoginBtn")).click();
		
		String cookieJson = "";
		Thread.sleep(10000);
//		String text = driver.findElement(By.id("error")).getText();
//		System.out.println("text---------" + text);
//		if (text == null || "".equals(text)) {
//			System.out.println("#########登录成功");
			// Thread.sleep(5000);
//			try {
//				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
//						.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//				WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
//					public WebElement apply(WebDriver driver) {
//						return driver.findElement(By.id("259333"));
//					}
//				});
//			} catch (Exception e) {
//				e.printStackTrace();
//				text = driver.findElement(By.id("error")).getText();
//			}

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
//			 cookieJson = CommonUnit.transcookieToJson(webClient);

//		driver.get(baseUrl);
		
		
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
//		} else {
//			System.out.println("#########登录失败");
//		}
//		System.out.println("text---------ERROR:" + text);
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

}
