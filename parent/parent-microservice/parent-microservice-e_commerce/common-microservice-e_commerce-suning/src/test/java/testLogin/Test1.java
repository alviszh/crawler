package testLogin;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Sleeper;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;
/**
*　　　　　　　　┏┓　　　┏┓+ +
*　　　　　　　┏┛┻━━━┛┻┓ + +
*　　　　　　　┃　　　　　　　┃ 　
*　　　　　　　┃　　　━　　　┃ ++ + + +
*　　　　　　 ████━████ ┃+
*　　　　　　　┃　　　　　　　┃ +
*　　　　　　　┃　　　┻　　　┃
*　　　　　　　┃　　　　　　　┃ + +
*　　　　　　　┗━┓　　　┏━┛
*　　　　　　　　　┃　　　┃　　　　　　　　　　　
*　　　　　　　　　┃　　　┃ + + + +
*　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting　　　　　　　
*　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug　　
*　　　　　　　　　┃　　　┃
*　　　　　　　　　┃　　　┃　　+　　　　　　　　　
*　　　　　　　　　┃　 　　┗━━━┓ + +
*　　　　　　　　　┃ 　　　　　　　┣┓
*　　　　　　　　　┃ 　　　　　　　┏┛
*　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
*　　　　　　　　　　┃┫┫　┃┫┫
*　　　　　　　　　　┗┻┛　┗┻┛+ + + +
*/
public class Test1 {

	static String driverPath = "D:\\ChromeDriver\\chromedriver.exe";

	static Boolean headless = false;
	
	public static void main(String[] args) throws Exception{
		loginByDriver();
//		sendSms();
	}
	
	public static void loginByDriver() throws Exception{
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String baseUrl = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		driver.get(baseUrl);
		Thread.sleep(2500);
//		((JavascriptExecutor)driver).executeScript("document.getElementById(\"dt_notice\").style ='width: 100%;';");
		
		/*WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));*/
		WebElement smsLogin = driver.findElement(By.name("WAP_login_password_meslog"));
		
		smsLogin.click();
		WebElement phoneNum = driver.findElement(By.id("phoneNum"));
		WebElement phoneCode = driver.findElement(By.id("phoneCode"));
//		WebElement sendSms = driver.findElement(By.name("WAP_login_message_getmes"));
		Thread.sleep(300);
		phoneNum.sendKeys("17600325986");
		Thread.sleep(1000);
		phoneCode.sendKeys("688829");
		Thread.sleep(1000);
		
		/*WebElement slide = driver.findElement(By.id("WAP_login_message_slide"));
		String attribute = slide.getAttribute("style");
		System.out.println("属性是="+attribute);
		if(attribute.contains("display: none;")){
			System.out.println("98754没有滑动验证码");
		}else{
			String url = "https://dt.suning.com/detect/dt/dragDetect.json?ffs=91c617d9dacbaf8c2c5e443cad40f42dYTRhMWFBVTlBXTlBSTlBRTlBWTlVVTlVXTlVxI05VcSNOVU1OVU1OVVNOVVNOVVZOV1dOV1ROcSNXTnEjUU5xI1ZOTVdPVFBNTlRVUE5UVVFOVFdQTlRXU05UcSNXTlRxI1JOVHEjVk5UTXEjTlRNVk5UUk1OVFFQTlRRcSNOVFFRTlRRVE5UU1VOVFNXTlRTTU5UU1NOVFRQT1RNU1VUVVdXVFRXV1dOVFVOUU1OTVZOcSNXTlVSTlRWVk5UVFdOVFFSTlRxI1VOVFVSTlNWVE5TVFVOU1FSTlNxI1dOUVZxI05RTVZOUlZWTlJRUk5NTVZPVk5RUVVOUVBNTlNXTU5SUlVOUVRXTlRQVU5TUlFOU1FNTlRXUE5RVFFOUVBNTlRXcSNOU1NNTlRWUE5UV1NOUFJOVVBOVFNNTlFXcOxNTU5xI1dOVE1XTlNSTU5RVFVOUVVTTlJSUE5NVFJOTVdQTnEjTVNOV1RWTldNVU5VVlZOVVJTTlVQVk5QV1FOVFZUVU5UVk1RTlRWVVFOVFRUVk5UVFFXTlRUTVBOVFRXUE5UVFBxI05UU1ZVTlRTU1FOVFNNVU5UU1BUTlRRVFBOVFFRVU5UUU1UTlRRcSNWTlRRcSNxI05UUVdNTlRRVU1OVFFQU05UUlZSTlRSU01OVFJSVk5UUlJVT1RUVU5UVFBOVFFWTlRRcSNOVFFQTlRRUE5UUVBOVFJRTlRScSNOVFJVTlRSVU5UUlVOVFJVTlRSVU5UUlVOVFJVTlRSVU5UUlVOVFJVTlRSVU5UUlVOVFJVTlRSVU5UUlVOVFJVTlRSVU5UUlVOVFJVTlRSVU5UUlVOVFJVTlRSTU5UUk1OVFJSTlRSUk5UUlNOVFJUTlRRV05UUXEjTlRRUk9UTVNVVFVXV1RTUlRTTnEjUk5VU05QVU5UVE1OVFFRTlRNVE5UcSNxI05UVVNOVFBQTlNUTU5TUVNOU1JQTlNxI01OU1VUTlNQVU5RVE1OUVFTTlFSUE5RcSNxI05RVVFOUVBQTlJUTU5SUVROUlJVTlJxI01OUlVTTlJQUE5NVE1OTVFTTk1SUE5NcSNNTk1VTU5xI1ZWTnEjVFdOcSNRUU5xI3EjcSNOcSNVU05xI1BQTldUTU9WTlRVVU5NVlFXTk1NVFFOUlNQVU5RTU1xI05RV1NTTlJRUlNOUlZxI1dOUlNQcSNOUXEjU01OU1VTUk5TUldUTlNxI1NNTlFWVlZOUlVVU05TcSNSV05TVk1QTlRXcSNNTlRNVVVOVE1VVU5UUVdNTlRTTVZOVFZxI1FOV1ZxI05VVVNOU1ZNUE5UUFJUTlRXTVZOVFRUVU5XcSNNTk1QUU5RVlZOcSNWUk5NVVVOUk1NTlFxI01OVFFRcSNOVVVSTk1UTXDsVE1NVk5UTXEjVE5UTXEjVU9UVFVOVFRNTlRUUk9UTVNVVFVXV1RRUVdXTlRXTlFRT1RWVldOcSNXVE5SUlNw7FRxI1ZWT1BWVnDsVHEjVlZPVXEjVnDsVE1VUU9UU1ZWcOxTVU9QVU9UTVNXT1JTT1JTcOxWUtIcGlGSUpRV3V4OHZ0alhuMzYxc2tNbDdlU0FCOW9mbXFDVUdaZ2RENVBUd1ZFY3JZNGEyMGh6T3lOYlJMNg%3D%3D&key=243614269536019952960416617922&v=20180523v&_callback=jsonp_45681528187713747";
			driver.get(url);
			Thread.sleep(1000);
//			driver.get(url);
//			Thread.sleep(1000);
//			driver.navigate().back();
//			Thread.sleep(1000);
			driver.navigate().back();
			Thread.sleep(1000);
		}
		
		WebElement loginBtn = driver.findElement(By.name("WAP_login_password_logsubmit"));
		loginBtn.click();
		Thread.sleep(6000);*/
		Actions action = new Actions(driver);
		// 获取滑动滑块的标签元素
//		WebElement source = driver.findElement(By.xpath("//div[@class='dt_child_content_knob']"));
//		source.click();
		Thread.sleep(500);
		
		
//		WebElement k = driver.findElement(By.xpath("//*[@id=\"siller1_dt_child_content_containor\"]/div[3]"));
//		
//		WebElement x = driver.findElement(By.xpath("//*[@id=\"siller1_dt_child_content_containor\"]"));
		WebElement moveSlider = driver.findElement(By.xpath("//*[@id=\"siller1_dt_child_content_containor\"]/div[3]"));
		
		Point location = moveSlider.getLocation();
		Robot robot = new Robot();
		robot.mouseMove(location.getX()+5, location.getY()+115);
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		Thread.sleep(1500);
		for (int i = 0; i < 10; i++) {
			location = moveSlider.getLocation();
			robot.mouseMove((int)(Math.random() * 10)+150+location.getX(), location.getY()+115+(int)(Math.random() * 3));
			Thread.sleep(300);
		}
		Thread.sleep(1500);
//		robot.mousePress(KeyEvent.BUTTON3_MASK);
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
//		action.dragAndDrop(moveSlider,x);
		// 确保每次拖动的像素不同，故而使用随机数
//		int width = x.getSize().getWidth();
//		int width2 = k.getSize().getWidth();
//		System.out.println("11111111111111111111");
//		System.out.println(width);
//		System.out.println(width2);
//		action.clickAndHold(moveSlider).moveByOffset(x.getLocation().getX()+30, 0);
		action.clickAndHold(moveSlider).moveByOffset(200, 0).perform();;
		Thread.sleep(400);
//		action.clickAndHold(moveSlider).moveByOffset((int) (Math.random() * 10) + 200, 0);
//		Thread.sleep(2000);
		action.clickAndHold(moveSlider).moveByOffset(262, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(-245, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(138, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(-155, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(165, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(206, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(229, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(184, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(216, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(285, 0).perform();
		Thread.sleep(400);
		action.clickAndHold(moveSlider).moveByOffset(26, 0).perform();
		Thread.sleep(400);
		// 拖动完释放鼠标
//		action.moveToElement(moveSlider);
		
		// 组织完这些一系列的步骤，然后开始真实执行操作
//		Action actions = action.build();
//		actions.perform();
//		Thread.sleep(2500);
		action.release().perform();	
		
		Actions action2 = new Actions(driver);
		WebElement moveSlider2 = driver.findElement(By.xpath("//*[@id=\"siller1_dt_child_content_containor\"]/div[3]"));
		action.clickAndHold(moveSlider2);
//		action2.moveByOffset(485, 16);
//		action2.moveToElement(moveSlider2);
		Action build = action.build();
		build.perform();
		Thread.sleep(800);
		
		Actions action22 = new Actions(driver);
//		WebElement moveSlider2 = driver.findElement(By.xpath("//div[@class=\"dt_parent dt_child_content_knob_move_back\"]"));
		action.moveByOffset(247, 13);
//		action2.moveToElement(moveSlider2);
		Action build22 = action.build();
		build22.perform();
		Thread.sleep(1320);
		
		Actions action3 = new Actions(driver);
		WebElement moveSlider3 = driver.findElement(By.xpath("//div[@class=\"dt_parent dt_child_content_knob_move\"]"));
		action.clickAndHold(moveSlider3).moveByOffset(247, 12);
//		action3.moveToElement(moveSlider3);
		Action build3 = action.build();
		build3.perform();
		Thread.sleep(1334);
		
		Actions action4 = new Actions(driver);
		action.moveByOffset(264, 23);
		Action build4 = action.build();
		build4.perform();
		Thread.sleep(1350);
		
		Actions action5 = new Actions(driver);
		action.moveByOffset(230, -59);
		Action build5 = action.build();
		build5.perform();
		Thread.sleep(1615);
		
		Actions action6 = new Actions(driver);
		action.moveByOffset(-247, 30);
		Action build6 = action.build();
		build6.perform();
		Thread.sleep(1355);
		
		Actions action7 = new Actions(driver);
		action.moveByOffset(254, -15);
		Action build7 = action.build();
		build7.perform();
		Thread.sleep(1516);
		
		Actions action8 = new Actions(driver);
		action.moveByOffset(240, 56);
		Action build8 = action.build();
		build8.perform();
		Thread.sleep(1600);
		
		Actions action9 = new Actions(driver);
		action.moveByOffset(250, -22);
		Action build9 = action.build();
		build9.perform();
		Thread.sleep(1100);
		
		Thread.sleep(1100);
		
//		Actions actionEnd = new Actions(driver);
		action.release();
		Action buildEnd = action.build();
		buildEnd.perform();
//		loginBtn.click();
//		sendSms.click();
		
		//zhaochunxiang555@126.com 100094wsl
		/*WebElement numLogin = driver.findElement(By.name("WAP_login_message_paslog"));
		numLogin.click();
		System.out.println("开始输入=====");
		username.sendKeys("13520800817");
		Thread.sleep(1000);
		password.sendKeys("md87315450");
		Thread.sleep(1000);
		loginBtn.click();*/
		
		Thread.sleep(1000);
//		System.out.println("/*-?>"+driver.getPageSource());
		
		/*Document doc = Jsoup.parse(driver.getPageSource());
		Elements eles = doc.select(".alert-msg");
		Elements wbox = doc.select(".search-content.wbox");
		System.out.println("123456"+wbox);
		String text = eles.text();
		System.out.println(text);
		if(null != text && !text.equals("")){
			System.out.println("提示信心："+text);
		}else if(null != wbox && wbox.size() > 0){
			System.out.println("true");
		}else{
			System.out.println("未知的错误啊啊啊啊啊啊啊啊啊啊啊");
		}*/
//		driver.quit();
//		String indexUrl = "https://my.suning.com/";
//		driver.get(indexUrl);
//		String pay = "https://pay.suning.com/epp-epw/useraccount/compatible-login!login.action";
//		driver.get(pay);
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("my.suning.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		/*//订单详情请求--order.suning.com
		String order = "https://order.suning.com/wap/order/queryOrderList.do?status=all&page=1&pageSize=500";
		WebRequest webRequest = new WebRequest(new URL(order), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		System.out.println("order-->"+page.getWebResponse().getContentAsString());*/
		
//		String payUrl = "https://pay.suning.com/epp-epw/useraccount/compatible-login!login.action";
		/*String payUrl = "https://pay.suning.com/epp-epw/useraccount/user-account!initUserAccount.action?idsTrustFrom=suning";
		WebRequest webRequest = new WebRequest(new URL(payUrl), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		System.out.println("my易付宝-->"+page2.getWebResponse().getContentAsString());*/
		Thread.sleep(1000);
		
		/*String idInfo = "https://pay.suning.com/epp-epw/epp/epp-user-account!initAccountInfo.action";
		WebRequest webRequest1 = new WebRequest(new URL(idInfo), HttpMethod.GET);
		Page page = webClient.getPage(webRequest1);
//		System.out.println("idinfo-->"+page.getWebResponse().getContentAsString());
		parserAccount(page.getWebResponse().getContentAsString());
		
		String bankcard = "https://pay.suning.com/epps-pppw/show/showQuickList.htm";
		WebRequest webRequest2 = new WebRequest(new URL(bankcard), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest2);
//		System.out.println("bankcard-->"+page3.getWebResponse().getContentAsString());
		parserBankCard(page3.getWebResponse().getContentAsString());*/
		
		/*String order = "https://order.suning.com/wap/order/queryOrderList.do?status=all&page=1&pageSize=15";
		WebRequest webRequest3 = new WebRequest(new URL(order), HttpMethod.GET);
		webRequest3.setAdditionalHeader("Host", "order.suning.com");
		Page page4 = webClient.getPage(webRequest3);
		System.out.println("order-->"+page4.getWebResponse().getContentAsString());
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(page4.getWebResponse().getContentAsString());
		JsonArray jsonArray = object.get("orderList").getAsJsonArray();
		List<String> orderIdList = new ArrayList<>();
		for (JsonElement jsonElement : jsonArray) {
			String orderId = jsonElement.getAsJsonObject().get("orderId").getAsString();
			orderIdList.add(orderId);
		}
		parserOrderlist(orderIdList, webClient);*/
//		address(webClient);
//		userinfo(webClient);
		
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
	
	public static void sendSms() throws Exception{
		String wapLogin = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		String smsUrl = "https://reg.suning.com/smsLogin/sendSms.do?phoneNumber=18600914623&rememberMe=true&type=1&sceneId=logonImg&targetUrl=http%3A%2F%2Fm.suning.com%2F&_=1516612502639&callback=smsLoginSendSms";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(wapLogin), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		
		webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		System.out.println("--*->"+page2.getWebResponse().getContentAsString());
		
	}
	
	public static void userinfo(WebClient webClient) throws Exception{
		String persondetail = "https://my.suning.com/memberInfo.do";
		String person = "http://my.suning.com/person.do";
		WebRequest webRequest = new WebRequest(new URL(persondetail), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		System.out.println("memberInfo/*->"+page.getWebResponse().getContentAsString());
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
		String nickName = object.get("nickName").getAsString();
		String custLevelNum = object.get("custLevelNum").getAsString();
		
		webRequest = new WebRequest(new URL(person), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		System.out.println("newPerson-->"+page2.getWebResponse().getContentAsString());
		System.out.println("end-=-=-=");
		Document document = Jsoup.parse(page2.getWebResponse().getContentAsString());
		String name = document.getElementById("name").val();
		String nickName2 = document.getElementById("nickName").val();
		String gender = document.getElementsByAttributeValue("checked", "checked").first().nextElementSibling().text();
		String phoneNum = document.getElementsContainingOwnText("手机：").first().nextElementSibling().child(0).text();
		String email = document.getElementsContainingOwnText("邮箱：").first().nextElementSibling().child(0).text();
		String birthdate = document.getElementById("birthdate").val();
		String constellation = document.getElementById("constellation").val();
		String hid_state = document.getElementsByClass("hid_state").first().val();
		String hid_city = document.getElementsByClass("hid_city").first().val();
		String hid_town = document.getElementsByClass("hid_town").first().val();
		String hid_street = document.getElementsByClass("hid_street").first().val();
		String address = document.getElementsByClass("adress-detail").first().child(0).val();
		
		System.out.println("nickName-->"+nickName);
		System.out.println("custLevelNum-->"+custLevelNum);
		System.out.println("name-->"+name);
		System.out.println("nickName2-->"+nickName2);
		System.out.println("gender-->"+gender);
		System.out.println("phoneNum-->"+phoneNum);
		System.out.println("email-->"+email);
		System.out.println("birthdate-->"+birthdate);
		System.out.println("constellation-->"+constellation);
		System.out.println("address-->"+hid_state+hid_city+hid_town+hid_street+address);
	}
	
	public static void address(WebClient webClient) throws Exception{
		/*String address = "https://my.suning.com/address.do";
		WebRequest webRequest = new WebRequest(new URL(address), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		int i = html.indexOf("uuid");
		int j = html.indexOf(">", i);
		String uuid = html.substring(i+6, j-1);
		System.out.println("uuid+="+uuid);
		//https://my.suning.com/wap/address0.do
		String addUrl = "http://my.suning.com/address.do?uuid="+uuid+"&callback=myCall&tabIndex=0";
		webRequest = new WebRequest(new URL(addUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "my.suning.com");
		webRequest.setAdditionalHeader("Referer", "http://my.suning.com/address.do");
		Page page2 = webClient.getPage(webRequest);
		System.out.println("地址页面："+page2.getWebResponse().getContentAsString());
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(page2.getWebResponse().getContentAsString());
		String addhtml = object.get("htmlDom").getAsString();
		System.out.println("addhtml"+addhtml);
		Document document = Jsoup.parse(addhtml);
		Element element = document.select(".addr-matter-con.clearfix").first();
		Elements elements = element.select(".addr-wap-del");
		for (Element ele : elements) {
			System.out.println("/*-/*-/*-/*-/*-/*-/*-/*-/*-/*-/*-");
			Element ele2 = ele.select(".addr-info-sure").first();
			Element ne = ele2.select(".addr-ne").first();
			Element hd = ele2.select(".addr-hd").first();
			Element de = ele2.select(".addr-deail").first();
			String[] split = ne.text().split(" ");
			System.out.println("ne0-->"+split[0]);
			System.out.println("ne1-->"+split[1]);
			System.out.println("hd-->"+hd.text());//与下面的 de 合并为总地址
			System.out.println("de-->"+de.text());
		}*/
		
		String address = "https://my.suning.com/wap/address0.do";
		WebRequest webRequest = new WebRequest(new URL(address), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		System.out.println("addressInfo=-=-=-"+html);
		Document document = Jsoup.parse(html);
		Elements lists = document.select(".address-list");
		if(null != lists && lists.size() > 0){
			Elements addressList = lists.first().select("li");
			for (Element add : addressList) {
				System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
				String name = add.select(".ad-name").first().text();
				String tel = add.select(".ad-tel").first().text();
				String addres = add.select(".address-all").first().text();
				System.out.println("name--"+name);
				System.out.println("tel--"+tel);
				System.out.println("address--"+addres);
			}
		}
	}
	
	public static void parserOrderlist(List<String> orderIdList, WebClient webClient) throws Exception {
		for (String orderId : orderIdList) {
			String orderUrl = "https://order.suning.com/wap/order/queryOrderDetail.do?orderId="+orderId+"&vendorCode=";
			WebRequest webRequest = new WebRequest(new URL(orderUrl), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			System.out.println("-------------订单分割线-------------");
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
			String submitTime = object.get("submitTime").getAsString();
			String transStatus = object.get("transStatus").getAsString();
			String vendorName = object.get("vendorName").getAsString();
			
			JsonArray itemList = object.get("itemList").getAsJsonArray();
			JsonObject receiveInfo = object.get("receiveInfo").getAsJsonObject();
			
			String receiverName = receiveInfo.get("receiverName").getAsString();
			String receiverTel = receiveInfo.get("receiverTel").getAsString();
			String receiverAddr = receiveInfo.get("receiverAddr").getAsString();
			String shipModeContent = receiveInfo.get("shipModeContent").getAsString();
			System.out.println("submitTime-->"+submitTime);
			System.out.println("transStatus-->"+transStatus);
			System.out.println("vendorName-->"+vendorName);
			System.out.println("receiverName-->"+receiverName);
			System.out.println("receiverTel-->"+receiverTel);
			System.out.println("receiverAddr-->"+receiverAddr);
			System.out.println("shipModeContent-->"+shipModeContent);
			
			for (JsonElement item : itemList) {
				JsonObject order = item.getAsJsonObject();
				String productName = order.get("productName").getAsString();
				String qty = order.get("qty").getAsString();
				String price = order.get("price").getAsString();
				System.out.println("*****商品分割线*****");
				System.out.println("productName-->"+productName);
				System.out.println("qty-->"+qty);
				System.out.println("price-->"+price);
			}
		}
	}
	
	public static void parserAccount(String html) throws Exception{
		Document document = Jsoup.parse(html);
		String name = getNextLabelByKeyword(document, "span", "真实姓名：");
		String username = getNextLabelByKeyword(document, "span", "帐  户 名 ：");
		String idNum = getNextLabelByKeyword(document, "span", "证件号码：");
		String phone = getNextLabelByKeyword(document, "td", "手机：");
		String email = getNextLabelByKeyword(document, "td", "联系邮箱：");
		String occupation = getNextLabelByKeyword(document, "td", "职业：");
		String address = getNextLabelByKeyword(document, "td", "联系地址：");
		
		if(null != occupation && occupation.contains("修改职业")){
			int i = occupation.indexOf("修改职业");
			occupation = occupation.substring(0, i);
		}
		if(null != address && address.contains("修改联系地址")){
			int i = address.indexOf("修改联系地址");
			address = address.substring(0, i);
		}
		
		System.out.println("name-->"+name);
		System.out.println("username-->"+username);
		System.out.println("idNum-->"+idNum);
		System.out.println("phone-->"+phone);
		System.out.println("email-->"+email);
		System.out.println("occupation-->"+occupation);
		System.out.println("address-->"+address);
	}
	
	public static void parserBankCard(String html) throws Exception{
		Document document = Jsoup.parse(html);
		Elements cards = document.select(".middle");
		for (Element card : cards) {
			Element bankico = card.select(".bank-ico").first();
			String ico = bankico.text();
			String bindTime = getNextLabelByKeyword(card, "span", "开通时间：");
			String status = getNextLabelByKeyword(card, "span", "当前状态：");
			String lastNum = card.select(".view-money").first().text();
			
			lastNum = lastNum.split(" ")[0];
			System.out.println("---------银行卡分割线---------");
			System.out.println("ico-->"+ico);
			System.out.println("bindTime-->"+bindTime);
			System.out.println("status-->"+status);
			System.out.println("lastNum-->"+lastNum);
		}
	}
	
	public static void loginHtmlUnit() throws Exception{
		String wapLogin = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(wapLogin), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
//		System.out.println("-/->"+loginPage.getWebResponse().getContentAsString());
		
		HtmlTextInput username = loginPage.getFirstByXPath("//input[@id='username']");
		HtmlPasswordInput password = loginPage.getFirstByXPath("//input[@id='password']");
		HtmlElement loginBtn = loginPage.getFirstByXPath("//a[@name='WAP_login_password_logsubmit']");
		
		username.setText("17600325986");
		password.setText("wang1992");
		Page loginedPage = loginBtn.click();
		
//		System.out.println("-0->"+loginedPage.getWebResponse().getContentAsString());
		
		String url = "https://www.suning.com/";
		webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		System.out.println("-0->"+page.getWebResponse().getContentAsString());
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
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
