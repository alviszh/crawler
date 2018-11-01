package app.service;

import java.util.List;
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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouHtml;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouLostwork;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouMedical;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.huizhou.InsuranceHuiZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.huizhou.InsuranceHuiZhouLostworkRepository;
import com.microservice.dao.repository.crawler.insurance.huizhou.InsuranceHuiZhouMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.huizhou.InsuranceHuiZhouUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceHuiZhouParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huizhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huizhou"})
public class InsuranceHuiZhouService implements InsuranceLogin{

	@Autowired
	private InsuranceHuiZhouLostworkRepository  insuranceHuiZhouLostworkRepository;
	@Autowired
	private InsuranceHuiZhouMedicalRepository  insuranceHuiZhouMedicalRepository;
	@Autowired
	private InsuranceHuiZhouUserInfoRespository  insuranceHuiZhouUserInfoRespository;
	@Autowired
	private InsuranceHuiZhouHtmlRepository insuranceHuiZhouHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuiZhouParser insuranceHuiZhouParser;
	@Autowired
	private InsuranceService insuranceService;
	@Value("${datasource.driverPath}")
	public String driverPath;
	@Autowired
	private AgentService agentService;
	private WebDriver driver;
	@Autowired
	private TracerLog tracer;
	
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.service.login.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance  taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {			
			WebDriver driver = intiChrome();
			WebParam webParam = insuranceHuiZhouParser.login(insuranceRequestParameters, driver);
			driver = webParam.getDriver();		
			InsuranceHuiZhouHtml html = new InsuranceHuiZhouHtml();
			html.setUrl(driver.getCurrentUrl().toString());
			html.setType("logined");
			html.setTaskid(taskInsurance.getTaskid());
			html.setHtml(driver.getPageSource());
			insuranceHuiZhouHtmlRepository.save(html);
			
			if(null != webParam.getHtml()){
				insuranceService.changeLoginStatus("LOGIN", "ERROR", webParam.getHtml(), taskInsurance);
				tracer.addTag("crawler.service.login.fail", "登陆失败："+webParam.getHtml());
				driver.close();
			}else{
				insuranceService.changeLoginStatus("LOGIN", "SUCCESS", "登录成功！", taskInsurance);
				tracer.addTag("crawler.service.login.success", "登陆成功");
				//登陆成功，开始采集数据。
//				taskInsurance.setYanglaoStatus(201);
//				taskInsurance.setGongshangStatus(201);
//				taskInsurance.setShengyuStatus(201);
				taskInsuranceRepository.save(taskInsurance);
//				taskInsurance = getAllData(insuranceRequestParameters);
			}
		} catch (Exception e) {
			insuranceService.changeLoginStatus("LOGIN", "ERROR", "连接超时！", taskInsurance);
			tracer.addTag("crawler.service.login.Exception", e.toString());
			e.printStackTrace();
			driver.close();
		}
//		taskInsurance = insuranceService.findTaskInsurance(taskInsurance.getTaskid());
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
//		chromeOptions.addArguments("--start-maximized");
//		chromeOptions.addArguments("--window-size=1600,900");
		driver = new ChromeDriver(chromeOptions);
//		Dimension browserSize = new Dimension(1100,868);
//		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
//		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
//		driver = new InternetExplorerDriver(ieCapabilities);
//		driver.manage().window().setSize(browserSize);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
//		driver.manage().window().maximize();  //页面最大化 
		return driver;
	}		

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.service.crawler.taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance  taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			//获取用户信息
			String infoUrl = "http://113.106.216.244:8003/web/ajax.do?r=0.3344419586609646&_isModel=true&params={'oper':'wscx_czzgbx_GrjbxxcxAction.getRyjbxx','params':{},'datas':{'ncm_gt_个人基本信息':{'params':{}}}}";
			driver.get(infoUrl);
			tracer.addTag("crawler.service.crawler.userinfo.page", driver.getPageSource());
			InsuranceHuiZhouHtml insuranceHuiZhouHtml = new InsuranceHuiZhouHtml();
			insuranceHuiZhouHtml.setUrl(infoUrl);
			insuranceHuiZhouHtml.setType("userInfo");
			insuranceHuiZhouHtml.setTaskid(taskInsurance.getTaskid());
			insuranceHuiZhouHtml.setHtml(driver.getPageSource());
			insuranceHuiZhouHtmlRepository.save(insuranceHuiZhouHtml);
			InsuranceHuiZhouUserInfo userInfo = insuranceHuiZhouParser.getUserInfo(driver.getPageSource(), taskInsurance.getTaskid());
			if(null != userInfo){
				insuranceHuiZhouUserInfoRespository.save(userInfo);
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
				tracer.addTag("crawler.service.crawler.userinfo.success", "【个人信息】已采集成功");
			}else{
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 				
				tracer.addTag("crawler.service.crawler.userinfo.fail", "【个人信息】已采集完成");
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
			insuranceService.changeCrawlerStatus("数据采集中，【个人信息】已采集完成", "CRAWLER_USER_MSG", 404, taskInsurance);
			tracer.addTag("crawler.service.crawler.userinfo.Exception", e.toString());
		}
		
		//获取医保缴纳信息
			try {
				String medicalUrl = "http://113.106.216.244:8003/web/ajax.do?r=0.0406122718591484&_isModel=true&params={'oper':'wscx_czzgbx_YlibxcblscxAction.getYlibxcbls','params':{},'datas':{'ncm_glt_医疗保险参保历史':{'heads':[],'params':{'pageSize':100,'maxPageSize':50,'curPageNum':1,'rowsCount':0,'Total_showMsg':null,'Total_showMsgCell':null,'Total_Cols':[]},'heads_change':[],'dataset':[]}}}";
				tracer.addTag("crawler.service.crawler.medical.url", medicalUrl);
				driver.get(medicalUrl);
				tracer.addTag("crawler.service.crawler.medical.page", driver.getPageSource());
				InsuranceHuiZhouHtml insuranceHuiZhouHtml = new InsuranceHuiZhouHtml();
				insuranceHuiZhouHtml.setUrl(medicalUrl);
				insuranceHuiZhouHtml.setType("medical");
				insuranceHuiZhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceHuiZhouHtml.setHtml(driver.getPageSource());
				insuranceHuiZhouHtmlRepository.save(insuranceHuiZhouHtml);
				List<InsuranceHuiZhouMedical>   medicalList= insuranceHuiZhouParser.getMedicalList(driver.getPageSource(), taskInsurance.getTaskid());
			if (null != medicalList && !medicalList.isEmpty()) {
				insuranceHuiZhouMedicalRepository.saveAll(medicalList);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				tracer.addTag("crawler.service.crawler.medical.success", "【医疗保险信息】已采集成功");
			}else{
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					tracer.addTag("crawler.service.crawler.medical.fail", "【医疗保险信息】已采集成功");
				}
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 404, taskInsurance); 
				tracer.addTag("crawler.service.crawler.medical.Exception", e.toString());
			}
						
			//获取失业缴纳信息
			try {
				String lostworkUrl = "http://113.106.216.244:8003/web/ajax.do?r=0.2839117585140434?&_isModel=true&params={'oper':'wscx_czzgbx_ShybxcblscxAction.getShybxcbls','params':{},'datas':{'ncm_glt_失业保险参保历史':{'heads':[],'params':{'pageSize':100,'maxPageSize':50,'curPageNum':1,'rowsCount':0,'Total_showMsg':null,'Total_showMsgCell':null,'Total_Cols':[]},'heads_change':[],'dataset':[]}}}";
				tracer.addTag("crawler.service.crawler.lostwork.url", lostworkUrl);
				driver.get(lostworkUrl);
				tracer.addTag("crawler.service.crawler.lostwork.page", driver.getPageSource());
				InsuranceHuiZhouHtml insuranceHuiZhouHtml = new InsuranceHuiZhouHtml();
				insuranceHuiZhouHtml.setUrl(lostworkUrl);
				insuranceHuiZhouHtml.setType("lostwork");
				insuranceHuiZhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceHuiZhouHtml.setHtml(driver.getPageSource());
				insuranceHuiZhouHtmlRepository.save(insuranceHuiZhouHtml);
				List<InsuranceHuiZhouLostwork>   lostworkList= insuranceHuiZhouParser.getLostworkList(driver.getPageSource(), taskInsurance.getTaskid());
				if(null != lostworkList && !lostworkList.isEmpty()){
					insuranceHuiZhouLostworkRepository.saveAll(lostworkList);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance); 					
					tracer.addTag("crawler.service.crawler.lostwork.success", "【失业保险信息】已采集成功");
				}else{
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					tracer.addTag("crawler.service.crawler.lostwork.fail", "【失业保险信息】已采集成功");
				}
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 404, taskInsurance); 
				tracer.addTag("crawler.service.crawler.lostwork.Exception", e.toString());
			}	
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		taskInsurance = insuranceService.findTaskInsurance(taskInsurance.getTaskid());
		driver.close();
		return taskInsurance;
	}
	public TaskInsurance quitDriver(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("quit", insuranceRequestParameters.toString()); 
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskInsurance taskInsurance = insuranceService.systemClose(true, insuranceRequestParameters.getTaskId());  
		//调用公用释放资源方法
		if(taskInsurance != null){
			agentService.releaseInstance(taskInsurance.getCrawlerHost(), driver);
		} else{
			tracer.addTag("quit taskInsurance is null",""); 
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
