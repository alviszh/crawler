package app.paeser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
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

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.hubei.InsuranceSZHuBeiInformation;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hubei.InsuranceSZHuBeiInformationRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.hubei"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.hubei"})
public class InsuranceSZHuBeiParser implements InsuranceLogin,InsuranceCrawler{
	
	@Autowired
	private  InsuranceSZHuBeiInformationRepository insuranceSZHuBeiInformationRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	@Value("${datasource.driverPath}")
	public String driverPath;
	private WebDriver driver;
	WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 * @throws Exception 
	 */

	public  WebDriver intiChrome() throws Exception {
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--window-size=1080,868");
		driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();  //页面最大化
		return driver;
	}	
	
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String url = "http://59.175.218.202:7004/hbcms/member/login.jhtml";
		try {
			driver = intiChrome();
			driver.get(url);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id='username']")).sendKeys(insuranceRequestParameters.getUsername().trim());
	        Thread.sleep(1000);
	        driver.findElement(By.xpath("//*[@id='password']")).sendKeys(insuranceRequestParameters.getPassword().trim());
	        Thread.sleep(1000);
	        String path = WebDriverUnit.saveImg(driver, By.xpath("//*[@id='codeimg']")); 
	        String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", "0", "0", "a", path); 
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
//	        String code = getVerfiycode(By.xpath("//*[@id='codeimg']"), driver);
	        System.out.println(code);
	        driver.findElement(By.xpath("//*[@id='verification-code']")).sendKeys(code);
	        Thread.sleep(1000);
	        driver.findElement(By.xpath("//*[@id='login_btn']")).click();
	        Thread.sleep(1000 * 1);
	        try{  
				 Alert alt = driver.switchTo().alert();
			     alt.accept();  
		           
		     }catch (Exception Ex)  {  
		            
		     }     
	        
	        String htmlsource2 = driver.getPageSource();
//	        System.out.println(htmlsource2);
	        tracer.addTag("登陆html", htmlsource2);
			
	        if (htmlsource2.contains("我的基本信息")) {
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				
	        }else if (htmlsource2.contains("账号暂未通过审核或被禁用")) {
	        	taskInsurance.setDescription("账号暂未通过审核或被禁用");
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance
						.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				driver.quit();
	        }else{
	        	taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance
						.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				driver.quit();
	        }
		}catch (Exception e) {
			// TODO: handle exception
			driver.quit();
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}
	
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			driver.findElement(By.xpath("//*[@class='right-part my-basicInfor']/h3/a")).click();
			String htmlsource3 = driver.getPageSource();
			InsuranceSZHuBeiInformation userInfo = user_person(htmlsource3);
			driver.quit();
			if (userInfo == null) {
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				userInfo.setTaskid(taskInsurance.getTaskid());
				insuranceSZHuBeiInformationRepository.save(userInfo);
				insuranceService.changeCrawlerStatus("【个人信息】已采集完成！",
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
				taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getError_code());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
				taskInsurance.setFinished(true);
				taskInsuranceRepository.save(taskInsurance);
			} 
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}
	
	
	public InsuranceSZHuBeiInformation user_person(String html){
		InsuranceSZHuBeiInformation infoUser = new InsuranceSZHuBeiInformation();
		Document doc = Jsoup.parse(html);
		Elements element = doc.select("div.infor-part > span >em");
		if (element.size()>0){
			String name = element.get(0).text().trim();                  //真实姓名
			String idCard = element.get(1).text().trim();                //身份证号
			String num = element.get(2).text().trim();                   //社保卡号
			String phone = element.get(3).text().trim();                 //手 机 号
			String username = element.get(4).text().trim();              //用户昵称
			String grade = element.get(5).text().trim();                 //用户等级
			infoUser.setName(name);
			infoUser.setIdCard(idCard);
			infoUser.setNum(num);
			infoUser.setPhone(phone);
			infoUser.setUsername(username);
			infoUser.setGrade(grade);
			return infoUser;
		}
		return null;
		
	}
	
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
