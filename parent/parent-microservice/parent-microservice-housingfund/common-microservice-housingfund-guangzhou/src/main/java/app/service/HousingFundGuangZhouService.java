package app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.service.common.AgentService;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import app.service.common.aop.ICrawlerLogin;


/**
 * @description: 广州公积金信息service(登录成功调用爬取接口)
 * @author: sln 
 * @date: 2017年9月29日 上午9:52:28 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.guangzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.guangzhou"})
public class HousingFundGuangZhouService extends AbstractChaoJiYingHandler implements ICrawlerLogin{
	public static final Logger log = LoggerFactory.getLogger(HousingFundGuangZhouService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingBasicService housingBasicService;
	@Autowired
	private HousingFundGuangZhouCrawlerService crawlerAllService;
	@Autowired
	private AgentService agentService;
	@Value("${spring.application.name}")
	String appName;	
	@Value("${webdriver.ie.driver.path}")
	public String driverPath;
	
	private static int imageErrorCount;
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id().trim());
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		try {
			String baseUrl = "https://gzgjj.gov.cn/wsywgr/";  //登录链接
			driver.get(baseUrl);  //跳转到登录页面
			Thread.sleep(2000L);  //留取足够时间加载登录页面
			//根据登陆类型选择对应的输入框
			String logintype = messageLoginForHousing.getLogintype();
			if("CO_BRANDED_CARD".equals(logintype)){  //CO_BRANDED_CARD 联名卡号登录
//				driver.findElement(By.cssSelector("input[onclick='changeCert(1)']")).click();//把焦点定为到"公积金账号"
				/*Unable to find element with css selector == input[onclick='changeCert(1)']
						 (WARNING: The server did not provide any stacktrace informatio*/
				//上述定位方式报异常，故决定不再采用
				List<WebElement> radios = driver.findElements(By.name("radiobutton"));
				radios.get(0).click();
				VK.Tab();
				String num = messageLoginForHousing.getNum();
 			    VK.KeyPress(num);//   输入公积金账号 
			}else if("IDNUM".equals(logintype)){   //此方式还需要用户姓名作为参数
//				driver.findElement(By.cssSelector("input[onclick='changeCert(2)']")).click();//把焦点定为到"证件号"
				List<WebElement> radios = driver.findElements(By.name("radiobutton"));
				radios.get(1).click();
//				   切换到证件号选项的时候，证件号输入框就已经获取焦点了，故此处不需要再InputTab()
				VK.KeyPress(messageLoginForHousing.getNum());//   输入证件号
				Thread.sleep(1000);
				VK.Tab();
				WebElement findElement = driver.findElement(By.id("name"));
				findElement.sendKeys(messageLoginForHousing.getUsername().trim());  //   输入姓名
			}
			Thread.sleep(1000);
			VK.Tab();  
			VK.KeyPress(messageLoginForHousing.getPassword());// 输入密码
			Thread.sleep(1000);
			VK.Tab(); 
			String path = WebDriverUnit.saveImg(driver, By.id("safecode")); 
			tracer.addTag("保存图片验证码的路径是：", path);
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
			tracer.addTag("chaoJiYingResult", chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
			Thread.sleep(1000);
			tracer.addTag("超级鹰识别出来的图片验证码是：", code);
			driver.findElement(By.id("captcha")).sendKeys(code);     //输入图片验证码
			Thread.sleep(1000L);
			//==================================================
			//如下方式后来也不好用了，也说找不到
//			WebElement loginButton = driver.findElement(By.cssSelector("img[onclick='doLogin()']"));
			//故采用如下方式(登陆按钮也是img)
			WebElement loginButton = driver.findElements(By.tagName("img")).get(1);
			Actions action=new Actions(driver);  
			action.doubleClick(loginButton).perform();
			//=====================================================================
			try {
				WebDriverWait wait = new WebDriverWait(driver, 15);   
				Boolean isLogon=wait.until(ExpectedConditions.urlToBe("https://gzgjj.gov.cn/wsywgr/index.jsp"));
				if(isLogon==true){
			    	 Set<Cookie> cookies =  driver.manage().getCookies();
			 		 WebClient webClient = WebCrawler.getInstance().getWebClient();//  
			 		 for(Cookie cookie:cookies){
			 			 webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("gzgjj.gov.cn",cookie.getName(),cookie.getValue()));
			 		 }
			 		 housingBasicService.changeLoginStatusSuccess(taskHousing, webClient);
					 Thread.sleep(2000);
					 //截图保存
					 String ScreenshotPath= WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
					 tracer.addTag("登陆成功的页面截图路径", ScreenshotPath);
				}
			} catch (Exception e) {
				//截图保存
				String ScreenshotPath= WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracer.addTag("判断是否登录成功，超出等待时间后，直接报超时异常，故异常信息在catch中解析,此时的截图保存路径是：", ScreenshotPath);
				String loginClickHtml = driver.getPageSource(); //获取点击登录按钮后的html页面
				Document doc = Jsoup.parse(loginClickHtml);
				Element elementErrorMsg= doc.getElementById("actionmessage");   //获取页面中可能出现的错误信息
				String errorMsg = "";
				if(null!=elementErrorMsg){
					errorMsg = elementErrorMsg.text();
				}
				tracer.addTag("登陆失败，页面提示的错误详情是：", errorMsg);
				if(errorMsg.contains("验证码错误")){
					 imageErrorCount++;
					 //图片验证码识别错误，重试三次登录
					 if(imageErrorCount>3){
						 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
						 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
						 taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getDescription());
						 taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
						 housingBasicService.save(taskHousing);
						 imageErrorCount=0;
					 }else{
						 login(messageLoginForHousing);
						 tracer.addTag("action.login.auth.imageErrorCount", "这是第"+imageErrorCount+"次因图片验证码识别错误重新调用登录方法");
					 }
				}else if(errorMsg.contains("公积金账号或证件号不存在")){
					 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					 taskHousing.setDescription("公积金账号或证件号不存在");
					 taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					 taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					 housingBasicService.save(taskHousing);
					 
				}else if(errorMsg.contains("您输入的密码有误")){
//					   您输入的密码有误,您今日还有2次机会！
					 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					 taskHousing.setDescription("您输入的密码有误,密码第三次输入错误时，账号将被锁定");
					 taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					 taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					 housingBasicService.save(taskHousing);
					 
				}else if(errorMsg.contains("请使用正确登录信息")){
					 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					 taskHousing.setDescription("请使用正确登录信息");
					 taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					 taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					 housingBasicService.save(taskHousing);
					 
				}else{
			    	 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					 taskHousing.setDescription("登录失败，相关信息输入有误，请重试！");
					 taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					 taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					 housingBasicService.save(taskHousing);
				} 
			}
		} catch (Exception e) {
			tracer.addTag("登陆过程出现异常，异常信息是:", e.toString());
			//截图保存
			try {
				String ScreenshotPath= WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracer.addTag("登陆出现异常，截图路径:", ScreenshotPath);
			} catch (Exception e2) {
				tracer.addTag("在登陆出现异常时进行截图，调用截图方法出现了异常", e2.toString());
			}
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，公积金网站系统繁忙，请稍后再试！");
			housingBasicService.save(taskHousing);
		}
		try {
			 //执行完相关操作后释放资源
			agentService.releaseInstance(taskHousing.getCrawlerHost().trim(),driver);
		} catch (Exception e) {
			tracer.addTag("执行完相关操作后释放资源，该操作出现异常", e.toString());
		}
		//执行最后，调用下杀死进程的方法，因为有时候quit不好用
		HousingFundHelperService.killProcess();
		return taskHousing;
	}
	//获取所有的数据
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id().trim());
		Map<String, Future<String>> listfuture = new HashMap<String, Future<String>>();   //判断异步爬取是否完成
		try {
			Future<String> future=crawlerAllService.getUserInfo(taskHousing);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		try {
			Future<String> future=crawlerAllService.getDetailAccount(taskHousing);
			listfuture.put("getDetailAccount", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getDetailAccount.e", e.toString());
			taskHousingRepository.updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );		}
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskHousing.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskHousing.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			housingBasicService.updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			tracer.addTag("listfuture--ERROR", e.toString());
			housingBasicService.updateTaskHousing(taskHousing.getTaskid());
		}
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
