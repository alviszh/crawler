package app.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component                    
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.handan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.handan"})
public class HousingFundHandanService extends HousingBasicService implements ICrawlerLogin {
	
	@Autowired
	private TracerLog tracer;	
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingFundHandanCrawlerServie housingFundHandanCrawlerServie;
	
	@Value("${webdriver.chrome.driver.path}")
	String driverPathChrome;
	private WebDriver driver;
	private String _csrf;
	WebClient webClient = WebCrawler.getInstance().getNewWebClient();

	/**
	 * 登录
	 * @param taskHousing
	 * @param messageLoginForHousing
	 */
	@Async
	public void crawler(TaskHousing taskHousing, MessageLoginForHousing messageLoginForHousing) {
		
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		tracer.addTag("parser.crawler.auth", messageLoginForHousing.getNum());
		
		//解决验证码不正确的问题。循环3次。如果3次后验证码还不正确则抛出异常
		for(int i=0;i<3;i++){			
			try {
				taskHousing = login(messageLoginForHousing);
				if(null != driver && driver.getPageSource().contains("验证码错误")){
					driver.quit();
					continue;
				}
				if(null != driver && driver.getPageSource().contains("验证码已经过期，请重新生成")){
					driver.quit();
					continue;
				}
				if(null == driver){
					driver.quit();
					continue;
				}			
				break;
			} catch (Exception e) {
				tracer.addTag("登录异常", "error");
				tracer.addTag("login.error", e.getMessage());
				driver.quit();
				continue;
			}
		}
		
		String html = driver.getPageSource();
		if(html.contains("身份证号或密码不正确")){
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhasestatus());
			taskHousing.setDescription("身份证号或密码不正确");
			taskHousingRepository.save(taskHousing);
			driver.quit();
		}else if(html.contains("个人基本信息查询")){
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhasestatus());
			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getDescription());
			taskHousing = taskHousingRepository.save(taskHousing);
			
			Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
			for(org.openqa.selenium.Cookie cookie : cookiesDriver){
				Cookie cookieWebClient = new Cookie("www.hdgjj.cn", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			
			driver.quit();
			
			getAllData(messageLoginForHousing);
			housingFundHandanCrawlerServie.crawlerUserinfo(taskHousing,webClient,_csrf);
			
		}else{
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("系统繁忙，请稍后再试");
			taskHousingRepository.save(taskHousing);
			driver.quit();
		}
	
	}

	/**
	 * seleum登录
	 * @param messageLoginForHousing
	 * @return
	 * @throws Exception
	 */
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String loginUrl = "http://www.hdgjj.cn/olbh/index";
		//页面有时候打开会出现404的情况，刷新页面4次。
		for(int i = 0 ; i<4;i++){
			try {
				driver = getPageByChrome(loginUrl);
				break;
			} catch (Exception e) {
				driver.quit();
				continue;
			}
			
		}
		
		//radio选择框
		WebElement radio = driver.findElement(By.id("perRadio"));
		//点击个人账户登录（默认是人脸识别）
		radio.click();
				
		WebElement element = driver.findElement(By.name("_csrf"));
		_csrf = element.getAttribute("value");			
		tracer.addTag("_csrf", _csrf);
		//身份证号输入框
		WebElement idnum = driver.findElement(By.id("idNumber"));
		//密码输入框
		WebElement password = driver.findElement(By.id("password"));
		//点击登录按钮
		WebElement button = driver.findElement(By.xpath("//button[@class='btn btn-default btn_login']"));		
		//图片验证码输入框
		WebElement sms = driver.findElement(By.id("verifyCode"));
				
		String path = null;
		try {
			path = WebDriverUnit.saveImg(driver,By.id("person_pic"));
		} catch (Exception e) {
			tracer.addTag("图片保存出错", "error");
			tracer.addTag("path", e.getMessage());
			return null;
		}
		if(StringUtils.isBlank(path)){
			return null;
		}
		String code = chaoJiYingOcrService.callChaoJiYingService(path, "1902");
		tracer.addTag("code ====>>",code); 
				
		idnum.sendKeys(messageLoginForHousing.getNum());
		password.sendKeys(messageLoginForHousing.getPassword());
		sms.sendKeys(code);
		//点击登录按钮
		button.click();		
		tracer.addTag("点击登录后页面源码", "<xmp>"+driver.getPageSource()+"</xmp>");
		
		return taskHousing;
	}
	
	
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
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundHandanCrawlerServie.crawlerUserinfo(taskHousing,webClient,_csrf);
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
