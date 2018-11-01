package test.guiyang;

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

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Base {
	static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	static WebDriver driver;
	public static void main(String[] args) {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "http://118.112.188.109/nethall/login.jsp";
		driver.get(baseUrl);
		driver.manage().window().maximize();

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement login_person = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("login_person"));
			}
		});

		System.out.println("获取到login_person---->" + login_person.getText());

		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} // 这里需要休息2秒，不然点击事件可能无法弹出登录框

		login_person.click();

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("c_username"));
			}
		});
		System.out.println("获取到username---->" + username.getText());

		username.click();
		username.clear();
		username.sendKeys("120103198304271124");

		try {
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			
			String password = "500600";
			VK.KeyPress(password);

			String path = WebDriverUnit.saveImg(driver, By.id("codeimgC"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1007", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("checkCodeC")).sendKeys(code);

			driver.findElement(By.id("loginBtnC")).click();

			Thread.sleep(8000L);

			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			WebClient webClient = WebCrawler.getInstance().getWebClient();//

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("118.112.188.109",
						cookie.getName(), cookie.getValue()));
			}
			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=10&dto%5B'aae140_desc'%5D=%E5%B1%85%E6%B0%91%E5%85%BB%E8%80%81%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D=2013&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();
			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("成功！");
				
				if(html.contains("没有查询到相关信息!")){
					System.out.println("没有查询到相关信息!");
				}
				
				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("单位编号----"+unitNo);
					System.out.println("公司交费----"+companyPay);
					System.out.println("交费基数----"+payCardinal);
					System.out.println("个人交费----"+personalPay);
					System.out.println("期号----"+yearMonth);
					System.out.println("险种----"+name);
					System.out.println("公司名称----"+unitName);
				}
			} else {
				System.out.println("失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
