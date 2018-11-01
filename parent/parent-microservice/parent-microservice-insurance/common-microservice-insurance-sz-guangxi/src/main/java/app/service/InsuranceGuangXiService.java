package app.service;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXiHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.guangxi.InsuranceSZGuangXiHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceSZGuangXiParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.guangxi"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.guangxi"})
public class InsuranceGuangXiService implements InsuranceLogin{

	@Autowired
	private InsuranceSZGuangXiHtmlRepository insuranceSZGuangXiHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZGuangXiParser insuranceSZGuangXiParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGuangXiAllDateService  insuranceGuangXiAllDateService;
	@Value("${datasource.driverPath}")
	public String driverPath;
	private WebDriver driver;
	@Autowired
	private TracerLog tracer;
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.service.login.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {		
			WebDriver driver = intiChrome();
			WebParam webParam = insuranceSZGuangXiParser.login(insuranceRequestParameters, driver);
			driver = webParam.getDriver();		
			InsuranceSZGuangXiHtml html = new InsuranceSZGuangXiHtml();
			html.setUrl("https://gx12333.net/index/loginInit.html");
			html.setType("login");
			html.setPagenumber(1);
			html.setTaskid(taskInsurance.getTaskid());
			html.setHtml(webParam.getHtml());
			insuranceSZGuangXiHtmlRepository.save(html);
			if(1== webParam.getCode()){
				insuranceService.changeLoginStatus("LOGIN", "SUCCESS", "登录成功！", taskInsurance);		
				tracer.addTag("crawler.service.login.success", "登陆成功");
				String cookies = webParam.getCookies();
				taskInsurance = changeLoginStatusSuccess(taskInsurance, cookies);	
				System.out.println("登陆成功");
			}else{
				taskInsurance=insuranceService.changeLoginStatusPwdError(taskInsurance);		
				tracer.addTag("crawler.service.login.fail", "登陆失败："+webParam.getHtml());
				driver.close();
			}
		} catch (Exception e) {
			insuranceService.changeLoginStatus("LOGIN", "ERROR", "连接超时！", taskInsurance);
			tracer.addTag("crawler.service.login.Exception", e.toString());
			e.printStackTrace();
			driver.close();
		}
		taskInsurance = insuranceService.findTaskInsurance(taskInsurance.getTaskid());
		return taskInsurance;
	}
	
	public  WebDriver intiChrome() throws Exception {
		//String driverPath = "/opt/selenium/chromedriver-2.31";
		//System.setProperty("webdriver.chrome.driver", driverPath);
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		//chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		//chromeOptions.addArguments("--start-maximized");
		chromeOptions.addArguments("--window-size=1080,868");
		driver = new ChromeDriver(chromeOptions);
//		Dimension browserSize = new Dimension(1100,868);
//		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
//		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
//		driver = new InternetExplorerDriver(ieCapabilities);
//		driver.manage().window().setSize(browserSize);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();  //页面最大化
		return driver;
	}		
	public static WebClient addcookie(String cookiesIn) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookiesIn);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
  private TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance, String cookie) {
        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

        taskInsurance.setCookies(cookie);

        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		insuranceGuangXiAllDateService.getAllData(insuranceRequestParameters);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		
		return null;
	}

	


}
