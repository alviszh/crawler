import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import app.bean.ValidationLoginDataObject;
import app.bean.ValidationLoginRoot;
import app.crawler.telecom.htmlparse.TelecomParseCommon;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：jdlogin 类描述： 创建人：hyx
 * 创建时间：2017年12月8日 下午6:04:36
 * 
 * @version
 */
public class Telecomloginhubei2 {

//	static String driverPath = "d:\\ChromeServer\\chromedriver.exe";
	
	static String driverPath = "c:\\chromedriver.exe";

	static Boolean headless = false;

	protected static Gson gs = new Gson();

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

	public static String loginChrome() throws Exception {
		WebDriver driver = intiChrome();
		long startTime=System.currentTimeMillis();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "http://hb.189.cn/pages/selfservice/custinfo/userinfo/userInfo.action?trackPath=SYDH";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("txtAccount"));
			}
		});

		loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("txtAccount"));
			}
		});
		loginname.sendKeys("17786472180");
		driver.findElement(By.id("txtShowPwd")).click();
		driver.findElement(By.id("txtPassword")).sendKeys("73307533");
		String code = JOptionPane.showInputDialog("请输入验证码：");
		driver.findElement(By.id("txtCaptcha")).sendKeys(code.trim());

		try {
			driver.findElement(By.id("loginbtn")).click();
		} catch (Exception e) {			
			e.printStackTrace();			
		}
		Robot robot = new Robot();
		robot.setAutoDelay(1000);	
		 //获取屏幕分辨率
       // Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		robot.delay(500);
	    robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.delay(500);
//		 //刷新
        //driver.navigate().refresh();        
		try{
			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("showTable01"));
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	   WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	   Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		Map<String, String> cookiemap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			cookiemap.put(cookie.getName(), cookie.getValue());
			System.out.println(
					cookieWebClient.getDomain() + ":" + cookieWebClient.getName() + ":" + cookieWebClient.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		long emdTime=System.currentTimeMillis();
		System.out.println("用时"+(emdTime-startTime));
		 //getYzm(webClient);
		
		return null;
		//
	}
    public static void pressKey(Robot robot, int key){
        
            robot.keyPress(key);
            robot.keyRelease(key);
            robot.delay(500);
       
    }
	private static ValidationLoginDataObject ValidationLogin(WebClient webClient) {

		try {
			String url = "http://www.189.cn/login/index.do";
			Page page = getHtml(url, webClient);

			System.out.println("*************************************** index.do");
			System.out.println(page.getWebResponse().getContentAsString());

			ValidationLoginRoot jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(),
					ValidationLoginRoot.class);

			return jsonObject.getDataObject();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "www.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/html/login/right.html");
		webRequest.setAdditionalHeader("Origin", "http://www.czgjj.com");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	// 得到验证码方法
	public static void getYzm2(WebClient webClient) {

		try {
			String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000776";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			webClient = wdzl.getWebClient();
			int statusCode = wdzl.getWebResponse().getStatusCode();
			if (statusCode == 200) {

				String packurl7 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend7 = "<buffalo-call><method>getCustOfBillByCustomerCode</method><string>2951121277270000__giveup</string></buffalo-call>";
				WebRequest webRequestpack7 = new WebRequest(new URL(packurl7), HttpMethod.POST);
				webRequestpack7.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack7.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack7.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack7.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack7.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack7.setRequestBody(requestPayloadSend7);
				webClient.getPage(webRequestpack7);

				String packurl8 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend8 = "<buffalo-call><method>getFeeNumByHT</method><string>10732369</string><string>201708</string></buffalo-call>";
				WebRequest webRequestpack8 = new WebRequest(new URL(packurl8), HttpMethod.POST);
				webRequestpack8.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack8.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack8.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack8.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack8.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack8.setRequestBody(requestPayloadSend8);
				webClient.getPage(webRequestpack8);

				String packurl9 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend9 = "<buffalo-call><method>getSelectedFeeProdNum</method><string>undefined</string><string>"
						+ "18995154123" + "</string><string>2</string></buffalo-call>";
				WebRequest webRequestpack9 = new WebRequest(new URL(packurl9), HttpMethod.POST);
				webRequestpack9.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack9.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack9.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack9.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack9.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack9.setRequestBody(requestPayloadSend9);
				Page page2 = webClient.getPage(webRequestpack9);
				System.out.println("发送验证码接口" + page2.getWebResponse().getContentAsString());
				// 发送验证码接口
				String sendurl = "http://nx.189.cn/bfapp/buffalo/CtSubmitService";
				String requestPayloadSend = "<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
				WebRequest webRequestSend = new WebRequest(new URL(sendurl), HttpMethod.POST);
				webRequestSend.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestSend.setAdditionalHeader("Host", "nx.189.cn");
				webRequestSend.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestSend.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestSend.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestSend.setRequestBody(requestPayloadSend);
				Page pageSend = webClient.getPage(webRequestSend);

				String send = pageSend.getWebResponse().getContentAsString();
				System.out.println("发送验证码接口" + send);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getYzm(WebClient webClient) {
		try {
//			
//			String preUrl = "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp";
//			Page page2 = webClient.getPage(preUrl);		
			String smsUrl="http://hb.189.cn/feesquery_PhoneIsDX.action?productNumber=17786472180&cityCode=0127&sentType=C&ip=0";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("productNumber", "17786472180"));		
			paramsList.add(new NameValuePair("cityCode", "0127"));	
			paramsList.add(new NameValuePair("sentType", "C"));	
			paramsList.add(new NameValuePair("ip", "0"));		
			WebRequest webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);				
			webRequest.setAdditionalHeader("Accept", "text/plain, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "hb.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
			webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//			webRequest.setRequestParameters(paramsList);
			Page page = webClient.getPage(webRequest);				
			Thread.sleep(1000);
			String html=page.getWebResponse().getContentAsString();
			System.out.println("发送验证码接口" + html);
			Set<Cookie> cookies = webClient.getCookieManager().getCookies();
			for (Cookie cookie : cookies) {
				System.out.println(cookie.getName() + ":" + cookie.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// https://order.jd.com/center/list.action
	public static void main(String[] args) {
		try {
			loginChrome();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}