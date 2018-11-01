package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamInsurance;
import app.unit.CommonUnit;

/**
 * 宿迁 连云港登录界面相同，所以创建公共service 项目名称：common-microservice-insurance-service
 * 类名称：InsuranceSuQianAndLianYunGangCommonService 类描述： 创建人：hyx 创建时间：2018年3月1日
 * 上午11:07:37
 * 
 * @version
 */
public class InsuranceSuQianAndLianYunGangCommonService extends InsuranceService {

	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	public WebDriver intiChrome() throws Exception {
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

	public WebParamInsurance loginChromeCommon(String url, InsuranceRequestParameters insuranceRequestParameters)
			throws Exception {

		WebParamInsurance webParamInsurance = new WebParamInsurance();
		WebDriver driver = intiChrome();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.get(url);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.name("username"));
				}
			});
		} catch (Exception e) {
			webParamInsurance.setErrormessage("网络连接超时");
			driver.close();
			return webParamInsurance;
		}

		driver.findElement(By.name("username")).sendKeys(insuranceRequestParameters.getUsername().trim());

		driver.findElement(By.name("password")).sendKeys(insuranceRequestParameters.getPassword());

		String code = CommonUnit.getVerfiycodeBy(By.id("captchaImage"), driver, this.getClass());

		driver.findElement(By.name("captchaWord")).sendKeys(code);

		driver.findElement(By.xpath("//*[@id='loginForm']/div[8]/button")).click();

		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			WebElement loginsucess = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.className("modal-header"));
				}
			});
		} catch (Exception e) {

			Document doc = Jsoup.parse(driver.getPageSource());
			if(doc.select("div.alert-danger").text().indexOf("验证码")!=-1){
				webParamInsurance.setErrormessage("网络超时，请重试");
			}else{
				webParamInsurance.setErrormessage(doc.select("div.alert-danger").text());
			}
			driver.close();
			return webParamInsurance;
		}

		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Map<String, String> cookiemap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			cookiemap.put(cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		driver.close();
		webParamInsurance.setWebClient(webClient);
		return webParamInsurance;
		//
	}

	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

}
