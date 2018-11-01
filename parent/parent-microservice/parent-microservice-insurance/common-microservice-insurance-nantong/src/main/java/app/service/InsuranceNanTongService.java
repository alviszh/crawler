package app.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 济宁社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.nantong" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.nantong" })
public class InsuranceNanTongService  implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	public static String driverPath = "D:\\IEDriverServer_Win32\\chromedriver.exe";
	@Autowired
	private InsuranceService insuranceService;
	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		String baseUrl = "http://www.jsnt.lss.gov.cn:1002/query/";
		driver.get(baseUrl);
		// 用户名
		driver.findElement(By.id("account")).sendKeys("JSC050899974");
		// 密码
		Actions action = new Actions(driver);
		WebElement psddly = driver.findElement(By.xpath("//*[@id=\"psddly\"]"));
		action.moveToElement(psddly).click().sendKeys("1234").perform();

		WebElement source = driver.findElement(By.xpath("//*[@id=\"drag\"]/div[3]"));
		action.moveToElement(source).clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// 释放
		action.moveToElement(source).release();
		// 组织完这些一系列的步骤，然后开始真实执行操作
		try {
			Action actions = action.build();
			actions.perform();
		} catch (Exception e) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			String html = driver.getPageSource();
			System.out.println(html);
			if (html.contains("id=\"mainFrame\"")) {
				System.out.println("登录成功！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

				Set<Cookie> cookies = driver.manage().getCookies();

				WebClient webClient = WebCrawler.getInstance().getWebClient();//

				for (Cookie cookie : cookies) {
					System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
							"www.jsnt.lss.gov.cn", cookie.getName(), cookie.getValue()));
				}

				String cookies2 = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setCookies(cookies2);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("登录失败！请重新登录！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
			driver.quit();
		}
		return taskInsurance;
	}

	/**
	 * 爬取指定账号的济宁社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsuranceNanTongService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);

		// 爬取解析养老保险
		crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance);
		// 爬取解析医疗保险
		crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance);
		// 爬取解析失业保险()
		crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance);
		// 爬取解析生育保险()
		crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance);
		// 爬取解析工伤保险()
		crawlerBaseInfoService.crawlerGongshangInsurance(parameter, taskInsurance);

		// 更新最终的状态
		taskInsurance = insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());
		System.out.println("数据采集完成之后的-----" + taskInsurance.toString());

		return taskInsurance;
	}

	// 通过taskid将登录界面的cookie存进数据库
	public void saveCookie(InsuranceRequestParameters parameter, String cookies) {
		taskInsuranceRepository.updateCookiesByTaskid(cookies, parameter.getTaskId());
	}
	/**
	 * 获取TaskInsurance
	 * 
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getTaskInsurance(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
