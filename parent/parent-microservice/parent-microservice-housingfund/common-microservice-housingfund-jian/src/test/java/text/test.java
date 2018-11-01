package text;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url1 = "http://218.64.84.34:8089/gongjijin/dkxxlist.action";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
//		HtmlPage htmlPage1 = webClient.getPage(webRequest);
//		HtmlTextInput username = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='per_name']");
//		HtmlTextInput num = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='per_idcard']");
//		HtmlTextInput password = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='cust_code']");
//		HtmlTextInput countNumber = (HtmlTextInput)htmlPage1.getFirstByXPath("//*[@id='per_code']");
//		username.setText("肖志丹");
//		num.setText("362421199301140428");
//		password.setText("20138");
//		countNumber.setText("450");
//		HtmlElement button = (HtmlElement)htmlPage1.getFirstByXPath("//*[@id='form_0']");
//		HtmlPage htmlPage = button.click();
//		webRequest.setRequestBody(requestBody);
//		Page searchPage = webClient.getPage(webRequest);
//		System.out.println(htmlPage.getWebResponse().getContentAsString());
//		String url = "http://218.64.84.34:8089/gongjijin/indexlist.action";
//		String urlInfo = "http://218.64.84.34:8089/gongjijin/jbxx.action?id=18053055";
////		String urlpay = "http://218.64.84.34:8089/gongjijin/jcmx.action";
//		WebRequest webRequest1 = new WebRequest(new URL(urlInfo), HttpMethod.GET);
//		webClient.setJavaScriptTimeout(50000); 
//		webClient.getOptions().setTimeout(50000); // 15->60 
//		Page searchPage1 = webClient.getPage(webRequest1);
//		System.out.println(searchPage1.getWebResponse().getContentAsString());
//		ChromeDriver driver = intiChrome();
//		//driver.manage().window().maximize();
//		String url = "http://218.64.84.34:8089/gongjijin/dkxxlist.action";
//		//driver.manage().window().maximize();
//
//		// 设置超时时间界面加载和js加载
//		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
//		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
//		driver.get(url);
//		driver.findElement(By.id("per_name")).sendKeys(num);
//		Thread.sleep(1000);
//		driver.findElement(By.id("t_pass")).sendKeys(password);
//		Thread.sleep(1000);
//		String code = getVerfiycode(By.id("img_CheckCode_Layout"), driver);
//		driver.findElement(By.id("Tb_CheckCode_Layout")).sendKeys(code);
//		Thread.sleep(1000);
//		driver.findElement(By.id("btn_Login")).click();
//		Thread.sleep(1000*5);
//		String htmlsource2 = driver.getPageSource();
//		String cookieJson = "";
//		// 获取cookies
//		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
//		Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
//		for (org.openqa.selenium.Cookie cookie : cookies) {
//			CookieJson cookiejson = new CookieJson();
//			cookiejson.setDomain(cookie.getDomain());
//			cookiejson.setKey(cookie.getName());
//			cookiejson.setValue(cookie.getValue());
//			cookiesSet.add(cookiejson);
//		}
//		cookieJson = new Gson().toJson(cookiesSet);
		String x = "Sep 19, 2017 ";
		SimpleDateFormat sdf1 = new SimpleDateFormat ("MMM dd, yyyy", Locale.UK);
		Date date=sdf1.parse(x);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sDate=sdf.format(date);
		System.out.println(sDate);
		
	}
	
	public static  ChromeDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", "D:\\zhaohui\\chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

}
