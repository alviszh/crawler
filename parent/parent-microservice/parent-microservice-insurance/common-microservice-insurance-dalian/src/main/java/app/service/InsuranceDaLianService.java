package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianMedical;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianPension;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianUnemployment;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLianUserInfo;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDaLiangInjury;
import com.microservice.dao.entity.crawler.insurance.dalian.InsuranceDalianMaternity;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.dalian.InsuranceDaLianMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.dalian.InsuranceDaLianPensionRepository;
import com.microservice.dao.repository.crawler.insurance.dalian.InsuranceDaLianUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.dalian.InsuranceDaLianUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.dalian.InsuranceDaLiangInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.dalian.InsuranceDalianMaternityRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.paeser.InsuranceDaLianParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.dalian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.dalian"})
public class InsuranceDaLianService implements InsuranceLogin,InsuranceCrawler{
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public InsuranceService insuranceService;
	@Autowired
	public TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	public InsuranceDaLianParser insuranceDaLianParser;
	@Autowired
	public InsuranceDaLianUserInfoRepository insuranceDaLianUserInfoRepository;
	@Autowired
	public InsuranceDaLianUnemploymentRepository insuranceDaLianUnemploymentRepository;
	@Autowired
	public InsuranceDaLianPensionRepository insuranceDaLianPensionRepository;
	@Autowired
	public InsuranceDaLianMedicalRepository insuranceDaLianMedicalRepository;
	@Autowired
	public InsuranceDaLiangInjuryRepository insuranceDaLiangInjuryRepository;
	@Autowired
	public InsuranceDalianMaternityRepository insuranceDalianMaternityRepository;
	@Autowired
	private AgentService agentService;
	@Value("${datasource.driverPath}")
	public String driverPath;
	

	@Autowired
	private TracerLog tracerLog;
	private WebDriver driver;
	
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
	
	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		System.out.println(ele);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		String path = WebDriverUnit.getPathBySystem();
		// String path = "/home/seluser" + "/snapshot/";
		System.out.println("截图保存路径： " + path);
		File file = WebDriverUnit.getImageCustomPath(path);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX()+230, point.getY()+170, eleWidth+5, eleHeight+5);
		ImageIO.write(eleScreenshot, "png", screenshot);
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}
	
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url = "http://bsdt.dl12333.gov.cn/personal.jsp";
			WebDriver driver = intiChrome();
			driver.get(url);
			Thread.sleep(1000);
			//driver.manage().window().setSize(new Dimension(800, 1000));
			driver.findElement(By.xpath("//*[@id='login_status']/span[1]/a")).click();
			Thread.sleep(1000);
			driver.switchTo().frame("layui-layer-iframe1");
			String mainWorkArea = driver.getPageSource();
			if (mainWorkArea.contains("请输入您注册时设定的手机号码")) {
				
				driver.findElement(By.xpath("//input[@placeholder='请输入您注册时设定的手机号码']"))
						.sendKeys(insuranceRequestParameters.getName().trim());
				Thread.sleep(1000);
				driver.findElement(By.xpath("//input[@placeholder='请输入8位个人编号']"))
						.sendKeys(insuranceRequestParameters.getUsername().trim());
				Thread.sleep(1000);
				driver.findElement(By.xpath("//input[@placeholder='请输入办事大厅登录密码']"))
						.sendKeys(insuranceRequestParameters.getPassword().trim());
				Thread.sleep(1000);
				
				String path = saveImg(driver, By.xpath("//img[@id='codeimage']")); 
				tracerLog.addTag("parser.login.codeimg.path", path);
				String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", "0", "0", "a", path); 
				System.out.println("图片验证码url："+chaoJiYingResult);
				tracerLog.addTag("parser.login.codeimg.chaoJiYingResult", chaoJiYingResult);
				Gson gson = new GsonBuilder().create();
				String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
				tracerLog.addTag("parser.login.codeimg.code", code);
				
//				String code = getVerfiycode(By.xpath("//input[@type='submit']"), driver);
				driver.findElement(By.xpath("//input[@placeholder='请输入验证码']")).sendKeys(code);
				Thread.sleep(1000);
				driver.findElement(By.xpath("//input[@type='submit']")).click();
				Thread.sleep(1000 * 5);
				String htmlsource2 = driver.getPageSource();
				tracerLog.addTag("登陆html", htmlsource2);
				//System.out.println(htmlsource2);
				if (htmlsource2.contains("欢迎您")) {
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
					
					//insuranceDaLianServicePaer.getResult(taskInsurance);
				} else {
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskInsurance
							.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}
			} else {
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} 
			driver.quit();
			return taskInsurance;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tracerLog.addTag("登陆异常",  e.toString());
			driver.quit();
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
			String url1 = "http://bsdt.dl12333.gov.cn/grqy.jsp";
			driver.get(url1);
			Thread.sleep(1000 * 2);
			String currentUrl3 = driver.getCurrentUrl();
			//System.out.println("url"+currentUrl3);
			String s = currentUrl3.substring(0, currentUrl3.indexOf("grqyw/"));
			String urlUserInfo = s + "grqyw/D01.do?method=editAccess";
			String urlPension = s + "grqyw/D03.do?method=doQueryByQY&type=1"; //养老
			String urlInjury = s + "grqyw/D03.do?method=doQueryByQY&type=8";//工伤
			String urlMaternity = s + "grqyw/D03.do?method=doQueryByQY&type=12";//生育
			String urlMedical = s + "grqyw/D03.do?method=doQueryByQY&type=13";//医疗
			String urlUnemployment = s + "grqyw/D03.do?method=doQueryByQY&type=15";//失业
			//基本信息
			driver.get(urlUserInfo);
			String urlUserInfohtml = driver.getPageSource();
			tracerLog.addTag("基本信息html",  urlUserInfo);
			Thread.sleep(1000 * 5);
			InsuranceDaLianUserInfo userInfo = insuranceDaLianParser.userInfo(urlUserInfohtml);
			if (userInfo == null || userInfo.equals("")) {
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				userInfo.setTaskid(taskInsurance.getTaskid());
				insuranceDaLianUserInfoRepository.save(userInfo);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
						InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code(), taskInsurance);
			}
			//养老
			driver.get(urlPension);
			String urlPensionhtml = driver.getPageSource();
			List<String> list = getHtml(urlPensionhtml);
			//System.out.println(list.size());
			List<InsuranceDaLianPension> pension = null;
			for (String html : list) {
				tracerLog.addTag("养老信息html",  html);
				System.out.println("html");
				pension = insuranceDaLianParser.parser(html, taskInsurance.getTaskid());
				if (pension == null || pension.equals("")) {
					System.out.println("html");
					insuranceService.changeCrawlerStatus(
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getError_code(), taskInsurance);
				} else {

					insuranceDaLianPensionRepository.saveAll(pension);
					insuranceService.changeCrawlerStatus(
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
				}
			}
			
			Thread.sleep(1000 * 5);
			//工伤
			driver.get(urlInjury);
			String urlInjuryhtml = driver.getPageSource();
			List<String> list1 = getHtml(urlInjuryhtml);
			List<InsuranceDaLiangInjury> injury = null;
			for (String html : list1) {
				tracerLog.addTag("工伤信息html",  html);
				injury = insuranceDaLianParser.injury(html, taskInsurance.getTaskid());
				if (injury == null || injury.equals("")) {
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getError_code(), taskInsurance);
				} else {

					insuranceDaLiangInjuryRepository.saveAll(injury);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
				}
			}
			
			Thread.sleep(1000 * 5);
			//生育
			driver.get(urlMaternity);
			String urlMaternityhtml = driver.getPageSource();
			List<String> list2 = getHtml(urlMaternityhtml);
			List<InsuranceDalianMaternity> maternity = null;
			for (String html : list2) {
				tracerLog.addTag("生育信息html",  html);
				maternity = insuranceDaLianParser.maternity(html, taskInsurance.getTaskid());
				if (maternity == null || maternity.equals("")) {
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getError_code(), taskInsurance);
				} else {

					insuranceDalianMaternityRepository.saveAll(maternity);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
				}
			}
			
			Thread.sleep(1000 * 5);
			//医疗
			driver.get(urlMedical);
			String urlMedicalhtml = driver.getPageSource();
			List<String> list3 = getHtml(urlMedicalhtml);
			List<InsuranceDaLianMedical> medical = null;
			for (String html : list3) {
				tracerLog.addTag("医疗信息html",  html);
				medical = insuranceDaLianParser.medical(html, taskInsurance.getTaskid());
				if (medical == null || medical.equals("")) {
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getError_code(), taskInsurance);
				} else {

					insuranceDaLianMedicalRepository.saveAll(medical);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
				}
			}
			
			Thread.sleep(1000 * 5);
			//失业
			driver.get(urlUnemployment);
			String urlUnemploymenthtml = driver.getPageSource();
			List<String> list4 = getHtml(urlUnemploymenthtml);
			List<InsuranceDaLianUnemployment> unemployment = null;
			for (String html : list4) {
				unemployment = insuranceDaLianParser.unemployment(html, taskInsurance.getTaskid());
				if (unemployment == null || unemployment.equals("")) {
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getError_code(), taskInsurance);
				} else {

					insuranceDaLianUnemploymentRepository.saveAll(unemployment);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tracerLog.addTag("登陆异常",  e.toString());
			driver.quit();
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String>  getHtml(String urlPensionhtml) throws Exception{
		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(urlPensionhtml);
		Elements element = doc.select("td.statusTool");
		if (element.size()>0){
			//System.out.println("sssss");
			String pa = element.text().trim();
			pa = pa.substring(1,pa.indexOf("条"));
			int i = Integer.parseInt(pa);
			//System.out.println("i:"+i);
			int page = 1;
			if (i%10==0){
				page = i/10;
			}else{
				page = i/10+1;
			}
			//System.out.println("page:"+page);
			for(int j = 0;j<page;j++){
				//System.out.println("list:");
				String html = null;
				if (j==0){
					html = driver.getPageSource();
					//System.out.println("j:"+j);
				}else{
					driver.findElement(By.cssSelector("class.nextPageD")).click();
					html = driver.getPageSource();
					Thread.sleep(1000);
				}
				list.add(html);
				
			}
			
		}
		return list;
	}
	
	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean 
	 */ 
//	public TaskBank quit(InsuranceRequestParameters insuranceRequestParameters){ 
//		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
//		TaskBank taskBank = taskBankStatusService.systemClose(true,insuranceRequestParameters.getTaskId());  
//		//调用公用释放资源方法
//		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		return taskBank;
//	} 
}
