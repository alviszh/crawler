package app.service;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Account;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Html;
import com.microservice.dao.repository.crawler.housing.fuzhou2.HousingFuzhou2AccountRepository;
import com.microservice.dao.repository.crawler.housing.fuzhou2.HousingFuzhou2BasicRepository;
import com.microservice.dao.repository.crawler.housing.fuzhou2.HousingFuzhou2HtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundFuzhou2Parser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.fuzhou2")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.fuzhou2")
public class HousingFundFuzhou2CommonService extends HousingBasicService{

	private static WebDriver driver;
	
	@Autowired
	private HousingFundFuzhou2Parser housingFundFuzhou2Parser;
	@Autowired
	private HousingFuzhou2AccountRepository accountRepository;
	@Autowired
	private HousingFuzhou2BasicRepository basicRepository;
	@Autowired
	private HousingFuzhou2HtmlRepository htmlRepository;
	
	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;


	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			driver = intiChrome();
			WebParam webParam = housingFundFuzhou2Parser.login(messageLoginForHousing,taskHousing, driver);
			driver = webParam.getDriver();
			HousingFuzhou2Html html = new HousingFuzhou2Html();
			html.setUrl(driver.getCurrentUrl().toString());
			html.setType("logined");
			html.setTaskid(messageLoginForHousing.getTask_id());
			html.setHtml(driver.getPageSource());
			if(null != webParam.getHtml()){
				tracer.addTag("parser.login.ERROR.NUMORPASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(102);
				tracer.addTag("crawler.service.login.fail", "登陆失败："+webParam.getHtml());
				save(taskHousing);
				driver.close();
				
			}else{
				taskHousing.setPaymentStatus(200);
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				tracer.addTag("crawler.service.login.success", "登陆成功");
				save(taskHousing); 
				getFuzhou2Basic(messageLoginForHousing,taskHousing);
				getFuzhou2Account(messageLoginForHousing,taskHousing);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void getFuzhou2Account(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			String url = "http://www.fjszgjj.com/gjj/accountDetial/view?page=1";
			driver.get(url);
			tracer.addTag("crawler.service.crawler.account.page", driver.getPageSource());
			HousingFuzhou2Html html = new HousingFuzhou2Html();
			html.setUrl(url);
			html.setType("accountInfo");
			html.setTaskid(taskHousing.getTaskid());
			html.setHtml(driver.getPageSource());
			htmlRepository.save(html);
			List<HousingFuzhou2Account> list = new  ArrayList<HousingFuzhou2Account>();
			list =housingFundFuzhou2Parser.getFuzhou2Account(driver.getPageSource(), taskHousing.getTaskid(),list);
			if(null==list){
				updatePayStatusByTaskid("【公积金对账单明细】无数据！", 201, taskHousing.getTaskid());
				tracer.addTag("crawler.service.crawler.accountinfo.fail", "【公积金对账单明细】无数据！");
			}else{
				accountRepository.saveAll(list);
				updatePayStatusByTaskid("【公积金对账单明细】数据采集完成！", 200, taskHousing.getTaskid());
				tracer.addTag("crawler.service.crawler.accountinfo.success", "【公积金对账单明细】数据采集完成！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void getFuzhou2Basic(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			String url = "http://www.fjszgjj.com/gjj/accountInfo/queryInfogrxx";
			driver.get(url);
			tracer.addTag("crawler.service.crawler.basic.page", driver.getPageSource());
			HousingFuzhou2Html html = new HousingFuzhou2Html();
			html.setUrl(url);
			html.setType("basicInfo");
			html.setTaskid(taskHousing.getTaskid());
			html.setHtml(driver.getPageSource());
			htmlRepository.save(html);					 
			WebParam webParam = housingFundFuzhou2Parser.getBasicInfo(driver.getPageSource(), taskHousing.getTaskid());
			if(null==webParam.getFuzhou2Basic()){
				updateUserInfoStatusByTaskid("【公积金基本信息】无数据！", 201, taskHousing.getTaskid());
				tracer.addTag("crawler.service.crawler.basicinfo.fail", "【个人信息】已采集完成 无数据");
			}else{
				basicRepository.save(webParam.getFuzhou2Basic());
				updateUserInfoStatusByTaskid("【公积金基本信息】采集完成！", 200, taskHousing.getTaskid());
				tracer.addTag("crawler.service.crawler.basicinfo.success", "【个人信息】已采集完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
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


	

	
	

}
