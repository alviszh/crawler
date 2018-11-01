package app.service;

import java.net.URL;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.bean.WebParamInsurance;
import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.unit.CommonUnit;


/**   
*    
* 项目名称：common-microservice-housingfund-yvlin   
* 类名称：LoginAngGetService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月12日 上午10:30:04   
* @version        
*/

@Component
@Service
public class LoginAngGetService {
	
	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;
	
	@Autowired
	protected TracerLog tracerLog;

	
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
	
	
	/**   
	  *    
	  * 项目名称：common-microservice-housingfund-yvlin  
	  * 所属包名：app.service
	  * 类描述：   榆林公积金登录
	  * 创建人：hyx 
	  * 创建时间：2018年1月12日 
	  * @version 1  
	  * 返回值    WebDriver
	 * @return 
	  */
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public  WebParamInsurance<?> loginChrome(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String baseUrl = "http://218.92.102.28:8010/uaa/personlogin#/personL";
		WebParamInsurance<?> webParamInsurance = new WebParamInsurance<>();
		WebDriver driver = intiChrome();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.get(baseUrl);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait.until(new Function<WebDriver, WebElement>() {
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

		driver.findElement(By.name("password")).sendKeys(insuranceRequestParameters.getPassword().trim());

		String code = CommonUnit.getVerfiycodeBy(By.id("captchaImage"), driver, this.getClass());

		driver.findElement(By.name("captchaWord")).sendKeys(code);

		driver.findElement(By.xpath("//*[@id='loginForm']/div[8]/button")).click();

		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.className("leftFunctionMenus"));
				}
			});
		} catch (Exception e) {

			Document doc = Jsoup.parse(driver.getPageSource());
			if(doc.select("div.alert-danger").text().indexOf("验证码")!=-1){
				driver.close();
				throw new ErrorException("系统正忙，请稍后再试");
			}else{
				webParamInsurance.setErrormessage(doc.select("div.alert-danger").text());
			}
			
			driver.close();
			return webParamInsurance;
		}
		
		webParamInsurance.setDriver(driver);
		return webParamInsurance;
		
	}
	
	public  Page getUser(WebClient webClient) throws Exception {
		String url = "http://218.92.102.28:8010/api/security/user";
		Page page = getHtml(url, webClient); //

		return page;
	}
	
	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	public  WebDriver getPayByDriver(WebDriver driver,String year,String type) throws Exception {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("ui-grid-canvas"));
			}
		});

		WebElement typeButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.linkText(type));
			}
		});
		typeButton.click();
		
		 wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("ui-grid-canvas"));
			}
		});
		return driver;
		
	}

}
