package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.lianyungang.InsuranceLianYunGangPay;
import com.microservice.dao.entity.crawler.insurance.lianyungang.InsuranceLianYunGangUser;
import com.microservice.dao.repository.crawler.insurance.lianyungang.InsuranceLianYunGangPayRepository;
import com.microservice.dao.repository.crawler.insurance.lianyungang.InsuranceLianYunGangUserRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamInsurance;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.InsuranceLianYunGangParseService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.insurance.lianyungang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.insurance.lianyungang")
public class InsuranceLianYunGangFutureService extends InsuranceService {

	public static final Logger log = LoggerFactory.getLogger(InsuranceLianYunGangFutureService.class);

	@Autowired
	private LoginAngGetService loginAngGetService;

	@Autowired
	private InsuranceLianYunGangUserRepository insuranceLianYunGangUserRepository;

	@Autowired
	private InsuranceLianYunGangPayRepository insuranceLianYunGangPayRepository;

	@Autowired
	private InsuranceLianYunGangParseService insuranceLianYunGangParse;
	
	@Autowired
	private TracerLog tracerLog;

	public WebParamInsurance<?> login(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {
			return loginAngGetService.loginChrome(insuranceRequestParameters, taskInsurance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public TaskInsurance getPayResult(WebDriver driver, TaskInsurance taskInsurance) {

		LocalDate nowdate = LocalDate.now();

		String url = "http://218.92.102.28:8010/ehrss/si/person/ui/?code=5FEnkI#/rights/payment/payinfo";

		driver.get(url);
		List<InsuranceLianYunGangPay> resultlist = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			String year = nowdate.plusYears(-i).getYear() + "";
			try {
				resultlist.addAll(getPayNeedByDriver(driver, year, taskInsurance.getTaskid()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (resultlist.size() > 0) {

			tracerLog.output("getPayResult", "缴费信息共" + resultlist.size() + "条");
			// 存储resultlist
			insuranceLianYunGangPayRepository.saveAll(resultlist);
			//
			taskInsurance = changeCrawlerStatusSuccess(taskInsurance);
		} else {
			tracerLog.output("getPayResult", "没有缴费信息");
			taskInsurance = changeCrawlerStatusError(taskInsurance);
		}

		return taskInsurance;
	}

	private List<InsuranceLianYunGangPay> getPayNeedByDriver(WebDriver driver, String year, String taskid)
			throws Exception {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		WebElement yearButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("btn-default"));
			}
		});
		yearButton.click();

		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("//*[text()=" + year + "]"));
			}
		});
		loginByUserButton.click();
		driver.findElement(By.xpath("//*[@id='ng-view']/div/div/div[2]/form/div/div[3]/p/input")).click();

		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("ui-grid-canvas"));
			}
		});

		tracerLog.addTag("缴费信息" + year + "年", "");

		List<InsuranceLianYunGangPay> list = new ArrayList<>();

		driver = loginAngGetService.getPayByDriver(driver, year, "养老");
		list.addAll(insuranceLianYunGangParse.payParse(driver.getPageSource(), taskid));

		driver = loginAngGetService.getPayByDriver(driver, year, "医疗");
		list.addAll(insuranceLianYunGangParse.payParse(driver.getPageSource(), taskid));

		driver = loginAngGetService.getPayByDriver(driver, year, "工伤");
		list.addAll(insuranceLianYunGangParse.payParse(driver.getPageSource(), taskid));

		driver = loginAngGetService.getPayByDriver(driver, year, "失业");
		list.addAll(insuranceLianYunGangParse.payParse(driver.getPageSource(), taskid));

		driver = loginAngGetService.getPayByDriver(driver, year, "生育");
		list.addAll(insuranceLianYunGangParse.payParse(driver.getPageSource(), taskid));
		// insuranceLianYunGangParse.payParse(driver.getPageSource());

		return list;

	}

	
	public TaskInsurance getUserResult(WebDriver driver, TaskInsurance taskInsurance){
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Map<String, String> cookiemap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			cookiemap.put(cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		try {
			Page searchPage = loginAngGetService.getUser(webClient);
			
			InsuranceLianYunGangUser result = insuranceLianYunGangParse.userParse(searchPage.getWebResponse().getContentAsString());
	
			result.setTaskid(taskInsurance.getTaskid());
			insuranceLianYunGangUserRepository.save(result);
			taskInsurance = changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 404,taskInsurance);

		}
		
		return taskInsurance;
		
	}
	
}