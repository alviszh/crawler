package app.service;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;

@Component
public class CibChinaserviceLogin {
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private AgentService agentService;
	private WebDriver driver;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	@Value("${webdriver.chrome.driver.path}")
	public String driverPathChrome;
	static String LoginPage = "https://personalbank.cib.com.cn/pers/main/login.do";
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebDriver login(BankJsonBean bankJsonBean)throws Exception{
		tracerLog.addTag("loginCombo","开始登陆兴业银行"+bankJsonBean.getLoginName());
		//打开谷歌游览器，访问兴业银行的登录页面
		final WebDriver webDriver = openloginCmbChina(bankJsonBean); 
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
		String chaoJiYingResult = null;
		String htmlsource1 = null;
		if (webDriver==null){
//			taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
//					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
//					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
//					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
//			String path = WebDriverUnit.saveScreenshotByPath(webDriver,this.getClass());
//			tracerLog.addTag("网络超时,截图路径：",path);
//			tracerLog.addTag("网络超时!",bankJsonBean.getTaskid());
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			return null;
		}else{
			String windowHandle = webDriver.getWindowHandle();
			
			try {
				bankJsonBean.setWebdriverHandle(windowHandle);
				tracerLog.addTag("登录兴业银行，打开网页获取网页handler",windowHandle); 
				Thread.sleep(1000);
				webDriver.findElement(By.cssSelector("label[for='logintype3']")).click();
				Thread.sleep(1000);
				webDriver.findElement(By.id("loginNameTemp")).sendKeys(bankJsonBean.getLoginName());//手机号
				String path1 = WebDriverUnit.saveImg(webDriver, By.cssSelector("#captcha0 img"));
				chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("4004", LEN_MIN, TIME_ADD, STR_DEBUG, path1); // 1005
				Gson gson = new GsonBuilder().create();
				String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
				tracerLog.addTag("PbccrcService.login","图片验证码url："+ chaoJiYingResult +"，识别结果："+ code);
//				String code =getVerfiycode(By.cssSelector("#captcha0 img"), webDriver);//验证码图片
				Thread.sleep(1000);
//				Scanner input = new Scanner(System.in);
//				System.out.print("请输入：");
//		        String val = input.next();
				webDriver.findElement(By.id("mobilecaptchafield")).sendKeys(code);//验证码
				Thread.sleep(1000);
				webDriver.findElement(By.id("btnSendSms")).click();
				Thread.sleep(2000);
				htmlsource1 = webDriver.getPageSource();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				//截图 
//				try {
//					String path = WebDriverUnit.saveScreenshotByPath(webDriver,this.getClass());
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//				}
				taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
						"系统繁忙，请稍后再试！", 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),true, bankJsonBean.getTaskid());
//				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
//				tracerLog.addTag("打开网页获取网页异常,截图路径：",path);
				tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				//webDriver.quit();
			}
			
			if(htmlsource1.contains("验证码不能为空")){
				tracerLog.addTag(chaoJiYingResult, "图片验证码错误，触发retry机制");
				webDriver.quit();
				throw new RuntimeException("请输入正确的附加码");
			}else {
				return driver;
			}
				
			
		}
		
		
//		return chaoJiYingResult;
		
	}
	
    public WebDriver openloginCmbChina(BankJsonBean bankJsonBean)throws Exception{ 
		
		//driver.manage().window().maximize();
		
			System.out.println("launching chrome browser");
			System.setProperty("webdriver.chrome.driver", driverPathChrome);
			try {
			
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("disable-gpu"); 

			driver = new ChromeDriver(chromeOptions);
			 
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			
			
				//driver.manage().window().maximize();
				driver.get(LoginPage);
				return driver;
			} catch (Exception e) {
				System.out.println("网络超时");
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
				tracerLog.addTag("登录兴业银行，打开网页超时",e.toString()); 
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.addTag("打开网页超时,截图路径：",path);
				return null;
			}
			
		
		
	} 
}
