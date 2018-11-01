package app.service;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.sichuan.InsuranceSZSichuanInformation;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.sichuan.InsuranceSZSichuanInformationRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.parser.SZSichuanInsuranceParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.sichuan"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.sichuan"})
public class AsyncSZSichuanInsuranceService implements InsuranceLogin{
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private SZSichuanInsuranceParser sZSichuanInsuranceParser;
	@Autowired
	private InsuranceSZSichuanInformationRepository insuranceSZSichuanInformationRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	@Value("${webdriver.chrome.driver.path}")
	String driverPathChrome;
	private WebDriver driver;
	WebClient webClient = WebCrawler.getInstance().getNewWebClient();

	/**
	 * 登录+爬取数据
	 * @param insuranceRequestParameters
	 * @param taskInsurance
	 */

	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("taskid", insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());		
		driver = getLoginHtmlPage(insuranceRequestParameters,taskInsurance);
		tracer.addTag("点击过后页面源码：", "<xmp>"+driver.getPageSource()+"</xmp>");
		
		webClient.getOptions().setTimeout(50000);		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for(org.openqa.selenium.Cookie cookie : cookiesDriver){	
			Cookie cookieWebClient = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		driver.quit();
		tracer.addTag("登陆成功后，cookie赋给webClient.", "chrome浏览器关闭。");
		
//		String messageUrl = "http://119.6.84.89:7001/scwssb/g40Action.do?___businessId=01391304";
//		try {
//			String html = getHtmlByWebClient(messageUrl);
//			tracer.addTag("参保信息 ： ","<xmp>"+html+"</xmp>");
//			
//			List<InsuranceSZSichuanInformation> list = sZSichuanInsuranceParser.parserMessage(html,taskInsurance);
//			insuranceSZSichuanInformationRepository.saveAll(list);
//			tracer.addTag("参保信息入库。", "success:"+list.size());
//			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
//			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
//			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getError_code());
//			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
//			taskInsurance.setFinished(true);
//			taskInsuranceRepository.save(taskInsurance);
//		} catch (Exception e) {
//			tracer.addTag("获取参保信息失败！", e.getMessage());
//			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
//			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
//			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getError_code());
//			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
//			taskInsurance.setError_message("参保信息采集失败！");
//			taskInsurance.setFinished(true);
//			taskInsuranceRepository.save(taskInsurance);
//			
//		}
		return taskInsurance;
	}

	private String getHtmlByWebClient(String messageUrl) throws Exception{
		
		WebRequest webRequest = new WebRequest(new URL(messageUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "119.6.84.89:7001");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
//		webRequest.setAdditionalHeader("Referer", "http://119.6.84.89:7001/scwssb/welcome2.jsp");
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}

	//登录
	private WebDriver getLoginHtmlPage(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		String url = "http://119.6.84.89:7001/scwssb/login.jsp";
		try {
			getPageByChrome(url);
		} catch (Exception e) {
			tracer.addTag("selenium打开登录页面失败。", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getError_code());
			taskInsurance.setDescription("网络超时，请稍后再试");
			taskInsurance.setError_message("网络超时，请稍后再试");
			taskInsuranceRepository.save(taskInsurance);
			driver.quit();
		}
		
		//用户名输入框
		WebElement username = driver.findElement(By.className("inputFocusBorder"));
		tracer.addTag("用户名输入框：", "<xmp>"+username.toString()+"</xmp>");
		//密码输入框
		WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
		tracer.addTag("密码输入框：", "<xmp>"+password.toString()+"</xmp>");
		//登录按钮
		WebElement button = driver.findElement(By.xpath("//a[@class='STYLE1']"));
		tracer.addTag("登录按钮：", "<xmp>"+button.toString()+"</xmp>");
		//图片验证码输入框
		WebElement img = driver.findElement(By.id("checkCode"));
		tracer.addTag("图片验证码输入框：", "<xmp>"+img.toString()+"</xmp>");
		//等待图片出现
		WebDriverWait wait = new WebDriverWait(driver, 30); 
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("codeimg")));
			String file = WebDriverUnit.saveImg(driver, By.id("codeimg"));
			String imgCode = chaoJiYingOcrService.callChaoJiYingService(file, "1902");
			tracer.addTag("超级鹰识别后的图片验证码：", imgCode);
			username.sendKeys(insuranceRequestParameters.getName());
			password.sendKeys(insuranceRequestParameters.getPassword());
			img.sendKeys(imgCode);
			
			button.click();
			Thread.sleep(4000);
			tracer.addTag("点击登录后的页面源码：", "<xmp>"+driver.getPageSource()+"</xmp>");
		} catch (Exception e) {
			tracer.addTag("截屏获取图片验证码失败", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getError_code());
			taskInsurance.setDescription("网络超时，请稍后再试");
			taskInsurance.setError_message("网络超时，请稍后再试");
			taskInsuranceRepository.save(taskInsurance);
			driver.quit();
		}
		
		judgeIsLogined(taskInsurance);		
		return driver;
	}
	
	
	/**
	 * 判断是否登录成功
	 * @param taskInsurance
	 */
	private void judgeIsLogined(TaskInsurance taskInsurance) {
		
		//无论是用户名输错还是密码输错，都是显示验证失败，用户名密码不对。
		//页面有非空判断，前端做非空功能
		if(driver.getPageSource().contains("验证失败，用户名密码不对")){
			tracer.addTag("用户名密码错误！", "error");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getError_code());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getDescription());
			taskInsuranceRepository.save(taskInsurance);
			driver.quit();
		}else if(driver.getPageSource().contains("验证码输入错误")){
			tracer.addTag("验证码输入错误！", "error");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getError_code());
			taskInsurance.setDescription("网络出现波动，请稍后再试！");
			taskInsurance.setError_message("图片验证码有误！");
			taskInsuranceRepository.save(taskInsurance);
			driver.quit();
		}else if(driver.getCurrentUrl().contains("http://119.6.84.89:7001/scwssb/welcome2.jsp")){
			tracer.addTag("登录成功！", "success");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getError_code());
			taskInsurance.setDescription("登录成功,开始获取数据！");
			taskInsuranceRepository.save(taskInsurance);
		}else{
			tracer.addTag("未知原因！", "error");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getError_code());
			taskInsurance.setDescription("网络出现波动，请稍后再试！");
			taskInsuranceRepository.save(taskInsurance);
			driver.quit();
		}
		
	}

	/**
	 * 通过chrome打开登录页面
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public WebDriver getPageByChrome(String url) throws Exception{

		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPathChrome);

		if(driverPathChrome==null){
			tracer.addTag("WebDriverIEService initChrome RuntimeException", "webdriver.chrome.driver 初始化失败！");
			throw new RuntimeException("webdriver.chrome.driver 初始化失败！");
		}

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu");

		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.get(url);

		return driver;

	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		
		tracer.addTag("insurance.sz.sichuan.getalldata", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		
		String messageUrl = "http://119.6.84.89:7001/scwssb/g40Action.do?___businessId=01391304";
		try {
			String html = getHtmlByWebClient(messageUrl);
			tracer.addTag("参保信息 ： ","<xmp>"+html+"</xmp>");
			
			List<InsuranceSZSichuanInformation> list = sZSichuanInsuranceParser.parserMessage(html,taskInsurance);
			insuranceSZSichuanInformationRepository.saveAll(list);
			tracer.addTag("参保信息入库。", "success:"+list.size());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getError_code());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
			taskInsurance.setFinished(true);
			taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			tracer.addTag("获取参保信息失败！", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
			taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getError_code());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
			taskInsurance.setError_message("参保信息采集失败！");
			taskInsurance.setFinished(true);
			taskInsuranceRepository.save(taskInsurance);
			
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
