package org.common.microservice.bank.cebchina;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaDebitCardUserInfo;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangShiyeInfoqqq;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.service.ChaoJiYingOcrService;

public class logintest {
	// static String driverPath = "D:\\IEDriverServer_x64\\IEDriverServer.exe";
	//https://whgjj.hkbchina.com/portal/sendMsgRfdsLogin.do    发送验证码地址
	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;

	public static void main(String[] args) {
		try {
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			System.setProperty("webdriver.ie.driver", driverPath);
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
//			String baseUrl = "https://www.cebbank.com/per/prePerlogin.do?_locale=zh_CN";
			String url = "http://www.czgjj.net/";
			driver.get(url);
			String html = driver.getPageSource();
			System.out.println(html);
//			Thread.sleep(2000L);
//			VirtualKeyBoard.KeyPressEx("6214920208365971", 50);//帐号
//			//driver.findElement(By.id("skey")).sendKeys("6214920208365971");//帐号
//			Thread.sleep(2000L);
//			robot = new Robot();
//
//			robot.keyPress(KeyEvent.VK_TAB);
//			//huangwei1987924
//			String password = "liu299481122";//密码
//			VirtualKeyBoard.KeyPressEx(password, 50);
//
//
//			WebElement element = driver.findElement(By.xpath("//img[@class = 'shouzhi']"));//登录
//			Thread.sleep(2000L);
//			element.click();
//
//			String html = driver.getPageSource();
//
//			if(html.indexOf("网银登录密码修改")!=-1){
//				System.out.println("首次登录网银修改初始密码，请前往光大银行官网修改密码");
//			};
//
//			List<WebElement> list = driver.findElements(By.xpath("//img[@class='shouzhi']"));
//			WebElement webElement = list.get(0);
//			webElement.click();
//			
////			for (WebElement webElement : list) {
////				webElement.click();
////				break;
////			}
//			Thread.sleep(2000L);
////			String duanxin = JOptionPane.showInputDialog("请输入短信验证码……");
////			VirtualKeyBoard.KeyPressEx(duanxin,20);
////			
////			driver.findElement(By.className("btn_r")).click();
//			
//			
//			String url = "https://www.cebbank.com/per/FP020207.do?"
//					+ "_viewReferer=query/queryAsyError"
//					+ "&kind=br"
//					+ "&AcNo=6214920208365971"
//					+ "&AcType=1"
//					+ "&AcOrganId=3536";
//			driver.get(url);
//			String html2 = driver.getPageSource();
//			String html3 = html2.replaceAll("&gt;", ">");
//			String html4 = html3.replaceAll("&lt;", "<");
//			String html5 = html4.replaceAll("&amp;nbsp;", " ");
//			Document doc = Jsoup.parse(html5);
//			System.out.println(doc.toString());
//			Elements elementsByClass = doc.getElementsByClass("td2");
//			for (int i=0;i<elementsByClass.size();i++) {
//				Element element2 = elementsByClass.get(i);
//				Elements tag = element2.getElementsByTag("td");
//					String leixing = tag.get(0).text();//类型
//					String bizhong = tag.get(1).text();//币种
//					String qianleixing = tag.get(2).text();//钞汇标志
//					String qixiang = tag.get(3).text();//期限
//					String kaihu = tag.get(4).text();//开户日期
//					String quhu = tag.get(5).text();//到期日期
//					String text = tag.get(6).text();
//					String text2 = tag.get(7).text();
//					String ztai = tag.get(12).text();//状态
//					System.out.println("11");
//			}
//			if(eles!=null&&eles.size()>0){
//				for(int i=1;i<eles.size();i++){//表单中由于第一行是表头，数据从第二航开始，因此i=1
//					Element ele = eles.get(i); 
//					String cardNo = ele.select("td").eq(1).text();//交易时间
//					String username = ele.select("td").eq(2).text();//支出
//					String branch = ele.select("td").eq(3).text();//存入
//				}
//			}
//			String url = "https://www.cebbank.com/per/FP020217.do?"
//					+ "queryflag=1"
//					+ "&CHFlag=0"
//					+ "&Currency=01"
//					+ "&AcCur=01"
//					+ "&savekind=00"
//					+ "&BeginDate=2017-10-5"
//					+ "&EndDate=2017-11-21"
//					+ "&SavingKind=00"
//					+ "&AcNo=6214920208365971";
//
//			driver.get(url);
//			List<WebElement> elements4 = driver.findElements(By.xpath("//a[@class='txt03 txt_line']"));
//
//			if(elements4.get(0).getText().equals("[下一页]")){
//				driver.findElements(By.xpath("//a[@class='txt03 txt_line']")).get(0).click();
//			}
//			if(elements4.size()>2){
//				if(elements4.get(2).getText().equals("[下一页]")){
//					driver.findElements(By.xpath("//a[@class='txt03 txt_line']")).get(2).click();
//				}
//			}
			



			//			driver.get("https://www.cebbank.com/per/FP020217.do?queryflag=1&CHFlag=0&Currency=01&AcCur=01&savekind=00&BeginDate=2017-10-15&EndDate=2017-11-17&SavingKind=00&AcNo=6214920208365971");
			//			String html2 = driver.getPageSource();
			//			
			//			System.out.println(html2);


			//			WebClient webClient = WebCrawler.getInstance().getWebClient();
			//			Set<Cookie> cookies2 = driver.manage().getCookies();
			//			for (Cookie cookie2 : cookies2) {
			//				System.out.println(cookie2.getName() + "-------cookies--------" + cookie2.getValue());
			//				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("whgjj.hkbchina.com",
			//						cookie2.getName(), cookie2.getValue()));
			//			}
			//			String cookieString = CommonUnit.transcookieToJson(webClient);
			//			Set<com.gargoylesoftware.htmlunit.util.Cookie> set = CommonUnit.transferJsonToSet(cookieString);
			//			Iterator<com.gargoylesoftware.htmlunit.util.Cookie> j = set.iterator();
			//			while(j.hasNext()){
			//				webClient.getCookieManager().addCookie(j.next());
			//			}

			//			String url = "https://www.cebbank.com/per/FP020217.do?"
			//					+ "queryflag=1"
			//					+ "&CHFlag=0"
			//					+ "&Currency=01"
			//					+ "&AcCur=01"
			//					+ "&savekind=00"
			//					+ "&BeginDate=2017-10-15"
			//					+ "&EndDate=2017-11-17"
			//					+ "&SavingKind=00&AcNo=6214920208365971";
			//			String url = "https://www.cebbank.com/per/FP020207.do?_viewReferer=query/queryAsyError&kind=br&AcNo=6214920208365971&AcType=1&AcOrganId=3536";
			//			Page page = gethtmlPost(webClient, null, url);
			//			String html1 = page.getWebResponse().getContentAsString();
			//			System.out.println(html1);
			//Thread.sleep(2000L);
			//			driver.get("https://www.cebbank.com/per/FP020207.do?_viewReferer=query/queryAsyError&kind=br&AcNo=6214920208365971&AcType=1&AcOrganId=3536");
			//			String html3 = driver.getPageSource();
			//			System.out.println(html3);
			//			
			//			
			//			WebElement webElement = driver.findElements(By.xpath("//tr[@class='td2']")).get(0);
			//			String text = webElement.getText();
			//			System.out.println(text);


			/***
			 * 余额网址
			 */
			//https://www.cebbank.com/per/FP020207.do?_viewReferer=query/queryAsyError&kind=br&AcNo=6214920208365971&AcType=1&AcOrganId=3536


			/**
			 * 流水网址
			 */
			//https://www.cebbank.com/per/FP020217.do?queryflag=1&CHFlag=0&Currency=01&AcCur=01&savekind=00&BeginDate=2017-10-15&EndDate=2017-11-15&SavingKind=00&AcNo=6214920208365971			
			/**
			 * 个人信息网址
			 */
			//https://www.cebbank.com/per/perset.do

			// https://www.cebbank.com/per/FP020201.do?enterPath=2 

			//		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
			//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			//		WebElement login_person = wait.until(new Function<WebDriver, WebElement>() {
			//			public WebElement apply(WebDriver driver) {
			//				return driver.findElement(By.id("image3"));
			//			}
			//		});


			//		System.out.println("获取到login_person---->" + login_person.getText());

			//		Thread.sleep(2000L);
			// 这里需要休息2秒，不然点击事件可能无法弹出登录框
			//		login_person.click();
			//		Thread.sleep(1000L);
			//		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
			//				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			//身份证 帐号
			//		Thread.sleep(2000L);
			//			
			//			String LoginId = "420107198709061535";
			//			VirtualKeyBoard.KeyPressEx(LoginId, 50);


			Thread.sleep(2000L);

			//		
			//			Set<Cookie> cookies2 = driver.manage().getCookies();
			//			WebClient webClient = WebCrawler.getInstance().getWebClient();//
			//			
			//			for (Cookie cookie2 : cookies2) {
			//				System.out.println(cookie2.getName() + "-------cookies--------" + cookie2.getValue());
			//				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("https://whgjj.hkbchina.com/portal/pc/login.html",
			//						cookie2.getName(), cookie2.getValue()));
			//			}
			//			//验证码
			//			String url2="https://whgjj.hkbchina.com/portal/pc/htmls/LoginContent/loginContent.html";
			//			//WebDriver driver2 = new InternetExplorerDriver();
			//			HtmlPage page1 = getHtml(url2, webClient);
			//			String html1 = page1.getWebResponse().getContentAsString();
			//			HtmlImage image = (HtmlImage) page1.getFirstByXPath("//img[@id='_tokenImg']");
			//			String code = chaoJiYingOcrService.getVerifycode(image, "1902");
			//			
			//			System.out.println("11");
			//			//验证码
			//			/*String path = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
			//			System.out.println("path---------------" + path);
			//			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("3005", LEN_MIN, TIME_ADD, STR_DEBUG,
			//					path); // 1005
			//			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			//			Gson gson = new GsonBuilder().create();
			//			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			//			System.out.println("code ====>>" + code);*/
			//			
			//			InputTab();
			//			VirtualKeyBoard.KeyPressEx(code, 50);
			//			
			//			Thread.sleep(8000L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static Page getHtml1(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
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

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception{

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		return page;

	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		//		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		//		webRequest.setAdditionalHeader("Host", "whgjj.hkbchina.com");
		//		webRequest.setAdditionalHeader("Referer", "https://whgjj.hkbchina.com/portal/pc/main.html");
		//		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		//		webRequest.setAdditionalHeader("$Referer", "/");
		//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		//		webRequest.setAdditionalHeader("DNT", "1");
		//		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		//		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		//		webRequest.setAdditionalHeader("Cookie","");
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}

}
