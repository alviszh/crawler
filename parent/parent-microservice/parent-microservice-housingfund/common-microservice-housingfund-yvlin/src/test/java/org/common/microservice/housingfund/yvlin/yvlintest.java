package org.common.microservice.housingfund.yvlin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import app.crawler.htmlparse.HousingYvLinParse;

/**
 * 
 * 项目名称：common-microservice-housingfund-yvlin 类名称：yvlintest 类描述： 创建人：hyx
 * 创建时间：2018年1月4日 上午10:44:24
 * 
 * @version
 */
public class yvlintest {

	static String driverPath = "C:\\chromedriver.exe";

	static Boolean headless = false;

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
		// WebDriver driver = new RemoteWebDriver(new
		// URL("http://10.167.202.218:32768//wd/hub/"),
		// DesiredCapabilities.chrome());
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://www.ylzfgjj.cn/ylwsyyt/index.jsp?flag=1&loginflag=1";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("current"));
			}
		});

		driver.findElement(By.name("certinum")).sendKeys("61270119910603442X");

		driver.findElement(By.name("perpwd")).sendKeys("275945");

		String code = JOptionPane.showInputDialog("请输入验证码：");

		driver.findElement(By.name("vericode")).sendKeys(code);

		driver.findElement(By.xpath("//*[@id='tabs-1']/form/div[4]/div/button")).click();
		

		driver.getPageSource();
		Thread.sleep(5000);

		// System.out.println("===="+htmlsource3);

		getPayResult(driver);
		return null;
		//
	}


	public static void getUserBasic(WebDriver driver) throws Exception {
		String url = "http://www.ylzfgjj.cn/ylwsyyt/init.summer?_PROCID=70000001";
		driver.get(url);
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();

		Map<String, String> cookiesmap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}

		Map<String, String> datas = HousingYvLinParse.UserBasicParameterParse(driver.getPageSource());

		datas.put("accnum1", datas.get("_ACCNUM"));
		
		datas.put("accname1",  datas.get("_ACCNAME"));
		datas.put("certinum3", datas.get("_DEPUTYIDCARDNUM"));
		datas.put("certitype3", "");
		datas.put("calintyear", "");

		for (Map.Entry<String, String> entry : datas.entrySet()) {
			// Map.entry<Integer,String> 映射项（键-值对） 有几个方法：用上面的名字entry
			// entry.getKey() ;entry.getValue(); entry.setValue();
			// map.entrySet() 返回此映射中包含的映射关系的 Set视图。
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		}

		url = "http://www.ylzfgjj.cn/ylwsyyt/command.summer?uuid=" + (new Date().getTime());
		Connection con2 = Jsoup.connect(url);
		con2.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		con2.header("Accept", "application/json, text/javascript, */*; q=0.01");
		con2.header("Accept-Encoding", "gzip, deflate");
		con2.header("Accept-Language", "zh-CN,zh;q=0.9");
		con2.header("Connection", "keep-alive");
		con2.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		con2.header("Host", "www.ylzfgjj.cn");
		con2.header("Origin", "www.ylzfgjj.cn");
		con2.header("Referer", "http://www.ylzfgjj.cn/ylwsyyt/init.summer?_PROCID=70000001");
		con2.header("X-Requested-With", "XMLHttpRequest");
		// 设置cookie和post上面的map数据
		Response login = con2.ignoreContentType(true).method(Method.POST).data(datas).cookies(cookiesmap).execute();
		// 打印，登陆成功后的信息
		System.out.println(login.body());
		HousingYvLinParse.basicusereParse(login.body(), null);
		/*
		 * //登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可 Map<String, String>
		 * map=login.cookies(); for(String s:map.keySet()){
		 * System.out.println(s+"      "+map.get(s)); }
		 */

	}
	
	
	public static void getPayResult(WebDriver driver) throws Exception {
		String url = "http://www.ylzfgjj.cn/ylwsyyt/init.summer?_PROCID=70000002";
		driver.get(url);
		
		Thread.sleep(3000);
		driver.findElement(By.id("b_query")).click();
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();

		Map<String, String> cookiesmap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}
		Map<String, String> datas2 = HousingYvLinParse.UserBasicParameterParse(driver.getPageSource());
		Map<String, String> datas = HousingYvLinParse.UserPayParameterParse(driver.getPageSource());
		
		
		
		url = "http://www.ylzfgjj.cn/ylwsyyt/command.summer?uuid=" + (new Date().getTime());
		datas2.put("begdate", "2017-01-01");
		datas2.put("enddate", "2017-12-31");
		datas2.put("year", "2017");
		datas2.put("accnum", "612000275945");
		
		Connection con1 = Jsoup.connect(url);
		con1.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		con1.header("Accept", "application/json, text/javascript, */*; q=0.01");
		con1.header("Accept-Encoding", "gzip, deflate");
		con1.header("Accept-Language", "zh-CN,zh;q=0.9");
		con1.header("Connection", "keep-alive");
		con1.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		con1.header("Host", "www.ylzfgjj.cn");
		con1.header("Origin", "http://www.ylzfgjj.cn");
		con1.header("Referer", "http://www.ylzfgjj.cn/ylwsyyt/init.summer?_PROCID=70000002");
		con1.header("X-Requested-With", "XMLHttpRequest");
		// 设置cookie和post上面的map数据
		Response login2 = con1.ignoreContentType(true).method(Method.POST).data(datas2).cookies(cookiesmap).execute();
		// 打印，登陆成功后的信息
		System.out.println(login2.body());
		
		
		
		datas.put("accnum", datas2.get("_ACCNUM").trim());
		
		datas.put("_APPLY", "0");
		
		datas.put("_CHANNEL","1");
		datas.put("_PROCID","70000002");
		datas.put("dynamicTable_id","datalist2");
		datas.put("dynamicTable_currentPage","0");
		datas.put("dynamicTable_pageSize","1000");
		datas.put("dynamicTable_nextPage","1");
		datas.put("dynamicTable_page","/ydpx/70000002/700002_01.ydpx");
		datas.put("dynamicTable_nextPage","1");
		datas.put("dynamicTable_paging","true");
		datas.put("dynamicTable_configSqlCheck","0");
		datas.put("errorFilter","1=1");
		
		/*datas.put("begdate","2016-01-01");
		datas.put("enddate","2016-12-31");
		datas.put("year","2016");
*/
		for (Map.Entry<String, String> entry : datas.entrySet()) {
			// Map.entry<Integer,String> 映射项（键-值对） 有几个方法：用上面的名字entry
			// entry.getKey() ;entry.getValue(); entry.setValue();
			// map.entrySet() 返回此映射中包含的映射关系的 Set视图。
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		}

		url = "http://www.ylzfgjj.cn/ylwsyyt/dynamictable?uuid=" + (new Date().getTime());
		Connection con2 = Jsoup.connect(url);
		con2.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		con2.header("Accept", "application/json, text/javascript, */*; q=0.01");
		con2.header("Accept-Encoding", "gzip, deflate");
		con2.header("Accept-Language", "zh-CN,zh;q=0.9");
		con2.header("Connection", "keep-alive");
		con2.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		con2.header("Host", "www.ylzfgjj.cn");
		con2.header("Origin", "http://www.ylzfgjj.cn");
		con2.header("Referer", "http://www.ylzfgjj.cn/ylwsyyt/init.summer?_PROCID=70000002");
		con2.header("X-Requested-With", "XMLHttpRequest");
		// 设置cookie和post上面的map数据
		Response login = con2.ignoreContentType(true).method(Method.POST).data(datas).cookies(cookiesmap).execute();
		// 打印，登陆成功后的信息
		System.out.println(login.body());
		
		HousingYvLinParse.basicpayParse(login.body(), null);

		//HousingYvLinParse.basicusereParse(login.body(), null);
		/*
		 * //登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可 Map<String, String>
		 * map=login.cookies(); for(String s:map.keySet()){
		 * System.out.println(s+"      "+map.get(s)); }
		 */

	}
	

	public static void main(String[] args) {
		try {
			loginChrome();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
