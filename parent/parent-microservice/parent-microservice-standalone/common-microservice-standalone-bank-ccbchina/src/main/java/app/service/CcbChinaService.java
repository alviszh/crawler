package app.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.exceptions.NativeException;

import com.microservice.dao.entity.crawler.standalone.bank.ccbchina.CcbChinaDebitCardBillDetails;
import com.microservice.dao.repository.crawler.standalone.bank.ccbchina.CcbChinaDebitCardBillDetailsRepository;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.RequestParam;
import app.parser.CcbChinaCrawlerParser;
import app.parser.CcbChinaLoginParser;
import app.util.ExUtils;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.standalone.bank.ccbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.standalone.bank.ccbchina" })
public class CcbChinaService {
	
	public static final Logger log = LoggerFactory.getLogger(CcbChinaService.class);

	@Autowired
	private CcbChinaLoginParser ccbChinaLoginParser;
	@Autowired
	private CcbChinaCrawlerParser ccbChinaCrawlerParser;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private CcbChinaDebitCardBillDetailsRepository ccbChinaDebitCardBillDetailsRepository;

	private WebDriver driver;
	
	private String loginUrl = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_00?SERVLET_NAME=B2CMainPlat_00&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2";

	@Value("${webdriver.ie.driver.path}")
	String driverPathIE;
	
	/**
	 * @Des 打开登录页面
	 * @param 无
	 */
	public WebDriver openloginIE() {
		driver = getNewWebDriver();
		try {
			driver = getPage(driver, loginUrl);
		} catch (NoSuchWindowException e) {
			driver = getPage(driver, loginUrl);
		}
		return driver;
	}

	public WebDriver getPage(WebDriver driver,String url) throws NoSuchWindowException{
		driver.get(url);
		return driver;
	}
	
	public WebDriver getNewWebDriver(){ 
		 
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        
		System.setProperty("webdriver.ie.driver", driverPathIE );  
		if(driverPathIE==null){
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
	 
		return driver;
	}
	
	
	/**
	 * 建行登录
	 * 
	 * @param loginName
	 * @param password
	 * @throws Exception
	 */
	@Async
	public void loginByAccountNum(String loginName, String password) throws Exception {
		log.info("开始登录建行");
		driver = getHtmlByAccount(loginName, password);

		if (null != driver) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			
			WebElement main = null;
			try {
				main = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.id("kh_dlcs"));
					}
				});
			} catch (Exception e) {
				log.info("登录失败---main：" + ExUtils.getEDetail(e));
			}
			if (main != null) {
				log.info("登录成功---MSG：" + main.getText());
				String html = driver.getPageSource();
				RequestParam params = ccbChinaLoginParser.getParam(html, driver);
				crawler(params);
			}else{
				String html = driver.getPageSource();
				log.info("登录失败---MSG：" + html);
				driver.quit();
			}
		}
		
	}

	/**
	 * @Des 登录
	 * @param bankJsonBean
	 * @return
	 * @throws IllegalAccessException
	 * @throws NativeException
	 * @throws Exception
	 */
	public WebDriver getHtmlByAccount(String loginName, String password) throws Exception {

		// 打开建行登录页面
		log.info("打开建行登录页面");
		
		try {
			
			driver = openloginIE();

			WebElement usernameInput = driver.findElement(By.id("USERID"));
			WebElement passwordInput = driver.findElement(By.id("LOGPASS"));
			
			usernameInput.click();

			usernameInput.sendKeys(loginName);
			passwordInput.sendKeys(password);
			
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			
			WebElement imgInput = null;
			try {
				imgInput = wait.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						return driver.findElement(By.name("PT_CONFIRM_PWD"));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (imgInput != null) {
				WebElement fujiama = null;
				try {
					fujiama = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("fujiama"));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fujiama != null) {
					String path = WebDriverUnit.saveImg(driver, By.id("fujiama"));
					String code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
					imgInput.sendKeys(code);
				}
			}

			// 模拟点击登录按钮
			driver.findElement(By.id("loginButton")).click();
		} catch (Exception e) {
			log.error("登录建行有误"+ExUtils.getEDetail(e));
		}

		return driver;
	}
	
	/**
	 * 爬取
	 * 
	 * @param loginName
	 * @param password
	 * @param params
	 */

	public void crawler(RequestParam params) {
		
		log.info("开始爬取明细:crawler");

		try {
			List<CcbChinaDebitCardBillDetails> transFlows = ccbChinaCrawlerParser.getBankStatement(params);
			if (null != transFlows) {
				log.info("建行明细增量数据条数---" + transFlows.size());
				ccbChinaDebitCardBillDetailsRepository.saveAll(transFlows);
				
				
				driver.quit();
			}
		} catch (Exception e) {
			driver.quit();
			log.error("保存有误"+ExUtils.getEDetail(e));
			e.printStackTrace();
		}

	}

}
