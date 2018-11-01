package test;

import java.awt.Robot;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaAccountType;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestCGB_base {
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) {

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();

		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://ebanks.cgbchina.com.cn/perbank";
		driver.get(baseUrl);
		driver.manage().window().maximize();

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("loginId"));
			}
		});

		username.click();
		username.clear();
		username.sendKeys("6214623721002119229");
		try {
			/*
			 * DD模拟输入（安装版）
			 */
			VK.Tab();
			VK.KeyPress("12qwaszx");
			/*
			 * winio模拟输入
			 */
//			InputTab();
//
//			String password = "12qwaszx";
//			VirtualKeyBoard.KeyPressEx(password, 50);

			String path = WebDriverUnit.saveImg(driver, By.id("verifyImg"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("captcha")).sendKeys(code);

			driver.findElement(By.id("loginButton")).click();

			Thread.sleep(10000);

			String pageSource2 = driver.getPageSource();

			System.out.println("登录成功");

			String[] split = pageSource2.split("_emp_sid");
			String[] split2 = split[1].split("cipURL");
			String[] split3 = split2[0].split("'");
			// 请求的入参
			String empSid = split3[1].trim();
			System.out.println(empSid);

			WebClient webClient = WebCrawler.getInstance().getWebClient();
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
						"ebanks.cgbchina.com.cn", cookie.getName(), cookie.getValue()));
			}
			// 账户信息请求参数请求
			String loginurl3155 = "https://ebanks.cgbchina.com.cn/perbank/scripts/forpage/debitAndCreditMainPageShared.htm.js?JSVERSION=20161201";
			WebRequest webRequestlogin3155;
			webRequestlogin3155 = new WebRequest(new URL(loginurl3155), HttpMethod.GET);
			Page pagelogin3155 = webClient.getPage(webRequestlogin3155);
			
			String contentAsString3155 = pagelogin3155.getWebResponse().getContentAsString();
			System.out.println(contentAsString3155);
			
			//处理账户信息入参
			String[] split4 = contentAsString3155.split("cardNo:b.accountNo,sasbDepositNo:b.sasbDepositNo,");
			String[] split5 = split4[1].split("}");
			String json = "{"+split5[0]+"}";
			JSONObject object = JSONObject.fromObject(json);
			//99
			String currencyType = object.getString("currencyType");
			System.out.println(currencyType);
			//999
			String productName = object.getString("productName");
			System.out.println(productName);
			//A
			String cardState = object.getString("cardState");
			System.out.println(cardState);
			//FT
			String pageQueryFlag = object.getString("pageQueryFlag");
			System.out.println(pageQueryFlag);
			//1
			String turnPageBeginPos = object.getString("turnPageBeginPos");
			System.out.println(turnPageBeginPos);
			// 请求参数请求
			String loginurl31 = "https://ebanks.cgbchina.com.cn/perbank/AC0002.do?cardNo=6214623721002119229&sasbDepositNo=&currencyType="+currencyType+"&productName="+productName+"&cardState="+cardState+"&pageQueryFlag="+pageQueryFlag+"&isAutoDump=&turnPageBeginPos="+turnPageBeginPos+"&EMP_SID="
					+ empSid + "&submitTimestamp=20171130112823739&trxCode=b010101";
			WebRequest webRequestlogin31;
			webRequestlogin31 = new WebRequest(new URL(loginurl31), HttpMethod.GET);
			Page pagelogin31 = webClient.getPage(webRequestlogin31);

			String contentAsString31 = pagelogin31.getWebResponse().getContentAsString();
			System.out.println(contentAsString31);

			JSONObject object3 = JSONObject.fromObject(contentAsString31);
			String cd = object3.getString("cd");
			JSONObject object2 = JSONObject.fromObject(cd);
			String iSubHomeAccountList = object2.getString("iSubHomeAccountList");
			JSONArray array = JSONArray.fromObject(iSubHomeAccountList);
			for (int i = 0; i < array.size(); i++) {
				String string = array.get(0).toString();
				JSONObject stringobject2 = JSONObject.fromObject(string);
				// 账户总余额
				String balance = stringobject2.getString("balance");
				// 余额
				String balance2 = stringobject2.getString("balance");
				// 活期余额
				String canUseAmt = stringobject2.getString("canUseAmt");
				// 冻结余额
				String frozenAmount = stringobject2.getString("frozenAmount");
				// 定期余额
				String useVol = stringobject2.getString("useVol");
				// 账号
				String sequeceNo = stringobject2.getString("sequeceNo");
				// 类型
				String subAccountType = stringobject2.getString("subAccountType");
				// 币种
				String currencyType3 = stringobject2.getString("currencyType");

				CgbChinaAccountType cgbChinaAccountType = new CgbChinaAccountType();
				cgbChinaAccountType.setTaskid("");
				cgbChinaAccountType.setNumber("");
				cgbChinaAccountType.setBalance(balance);
				cgbChinaAccountType.setBalance2(balance2);
				cgbChinaAccountType.setCanUseAmt(canUseAmt);
				cgbChinaAccountType.setFrozenAmount(frozenAmount);
				cgbChinaAccountType.setUseVol(useVol);
				cgbChinaAccountType.setSequeceNo(sequeceNo);
				cgbChinaAccountType.setSubAccountType(subAccountType);
				cgbChinaAccountType.setCurrencyType(currencyType3);
			}
		} catch (Exception e) {
			// TODO: handle exception
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

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void keyPress(Robot r, int key) {
		r.keyPress(key);
		r.keyRelease(key);
		r.delay(100);
	}

	public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
		}
	}

}
