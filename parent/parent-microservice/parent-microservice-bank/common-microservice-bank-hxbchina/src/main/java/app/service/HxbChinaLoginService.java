/**
 * 
 */
package app.service;

import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import app.service.aop.ICrawlerLogin;
import app.service.common.HxbChinaHelperService;

/**
 * @author sln
 * @Description: 由于华夏银行储蓄卡和信用卡登陆页面相同，故只写一个公共的登陆Service
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.hxbchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.hxbchina" })
public class HxbChinaLoginService extends AbstractChaoJiYingHandler implements ICrawlerLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private WebDriverIEService webDriverIEService;
	@Autowired
	private AgentService agentService;
	@Value("${webdriver.ie.driver.path}")
	String driverPath;
	@Value("${spring.application.name}") 
	String appName;
	WebDriver driver;
	//登录
	@Async
	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		driver = webDriverIEService.getNewWebDriver();  //获取初始化实例
		try {
			String baseUrl = "https://sbank.hxb.com.cn/easybanking/jsp/indexComm.jsp";
			driver.get(baseUrl);  
			driver.manage().window().maximize();  //页面最大化
			Thread.sleep(2000L); 
			
			String loginType = bankJsonBean.getLoginType();
			if(loginType.equals("ACCOUNT_NUM")){   //用户名方式登录（个人账户号）
				driver.findElement(By.id("divli1")).click();//把焦点定为到"用户名登录"
				Thread.sleep(1000); 
				VK.Tab();  //定位到用户名输入框
				driver.findElement(By.id("alias")).sendKeys(bankJsonBean.getLoginName().trim());  //此方式可以输入中文
				Thread.sleep(1000);
//				VK.Tab();   
				driver.findElement(By.id("passwd")).sendKeys(Keys.ENTER);	
				VK.KeyPress(bankJsonBean.getPassword().trim());// 输入登录密码
				Thread.sleep(1000);
			}else{    //证件号方式登录
				driver.findElement(By.id("divli2")).click();//把焦点定为到"证件号登录"
				Thread.sleep(1000); 
				VK.Tab();  //先定位到证件类型
				driver.findElement(By.id("idNo")).sendKeys(bankJsonBean.getLoginName().trim());
				Thread.sleep(1000);
//				VK.Tab();   //改成如下方式聚焦到密码输入框，就不会少输入密码位数了
				driver.findElement(By.id("passwd")).sendKeys(Keys.ENTER);	
				VK.KeyPress(bankJsonBean.getPassword().trim());// 输入登录密码
				Thread.sleep(1000);
			}
			WebElement loginButton = driver.findElement(By.name("qy_sut"));
			Actions action=new Actions(driver);  
			action.click(loginButton).perform();
			try {
				WebDriverWait wait = new WebDriverWait(driver, 5);   
				Boolean isLogon=wait.until(ExpectedConditions.urlToBe("https://sbank.hxb.com.cn/easybanking/PAccountWelcomePage/FormParedirectAction.do?actionType=entry"));
				if(isLogon==true){
					Thread.sleep(1000);   //留取足够的时间将登录成功后的页面加载完成，便于截图
					 //截图 
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracer.addTag("登录成功", "截图:" +path);
					tracer.addTag("登录成功，欢迎来到首页面", taskBank.getTaskid()+"当前url:" + driver.getCurrentUrl());
			    	Set<Cookie> cookies =  driver.manage().getCookies();
			 		WebClient webClient = WebCrawler.getInstance().getWebClient();//  
			 		for(Cookie cookie:cookies){
			 			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("sbank.hxb.com.cn",cookie.getName(),cookie.getValue()));
			 		}
			 		String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
			 		taskBank.setCookies(cookieString);
			 		taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase());
					taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
					taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription());
					taskBank.setTesthtml(bankJsonBean.getPassword().trim()); //存储登录密码
					taskBankRepository.save(taskBank);
				}	
			}catch (Exception e) {
				//截图 
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("判断是否登录成功，超出等待时间后，看登录后的页面是不是需要改密码的页面", "异常:"+e.toString());
				tracer.addTag("异常截图:", path);
				
				String currentUrl = driver.getCurrentUrl();
				if(currentUrl.equals("https://sbank.hxb.com.cn/easybanking/login.do?")){   //登录后进入的是修改密码的网页
					String pageSource = driver.getPageSource();
					
					if(pageSource.contains("MsgTable")){
						Document doc = Jsoup.parse(pageSource);
						String errMsg = doc.getElementById("mess").text();
						taskBank.setPhase(BankStatusCode.BANK_LOGIN_ERROR.getPhase());
						taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus());
						taskBank.setDescription(errMsg);   //登录过程中的错误信息
						taskBankRepository.save(taskBank);
					}else{
						if(pageSource.contains("由于您的登录密码过于简单")){  //需要修改登录密码
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus());
							taskBank.setDescription(BankStatusCode.BANK_RESETPASSWORD_TIP.getDescription());
							taskBankRepository.save(taskBank);
						}
					}
				}else{
					tracer.addTag("输入相关用户名密码等登陆信息后，校验一定时间后没有进入首页面", "故提示：登录异常，华夏银行系统繁忙，请稍后再试！");
					taskBank.setPhase(BankStatusCode.BANK_LOGIN_ERROR.getPhase());
					taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus());
					taskBank.setDescription("登录异常，华夏银行系统繁忙，请稍后再试！");  
					taskBankRepository.save(taskBank);
				}
			}
		} catch (Exception e) {
			//截图 
			String path;
			try {
				path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("登陆过程===》获取登陆所需元素过程中出现异常，异常截图:", path);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			tracer.addTag("action.login.taskid===>e",ExUtils.getEDetail(e));
			taskBank.setPhase(BankStatusCode.BANK_LOGIN_ERROR.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus());
			taskBank.setDescription("登录异常，华夏银行系统繁忙，请稍后再试！");  
			taskBankRepository.save(taskBank);
		}
		try {
			tracer.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);   //此处有可能出现异常
			//出现问题时候，杀掉进程
			HxbChinaHelperService.killProcess();  
			tracer.addTag(taskBank.getTaskid()+"执行了释放实例的代码，释放的ip是：", taskBank.getCrawlerHost());
		} catch (Exception e) {
			//出现问题时候，杀掉进程
			HxbChinaHelperService.killProcess();  
			tracer.addTag("执行 agentService.releaseInstance方法时出现异常,调用了杀死ie进程的方法", e.getMessage());
		}
		//退出(貌似如下方法并没有起作用)
//		quit(bankJsonBean);
		return taskBank;
	}
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	public TaskBank quit(BankJsonBean bankJsonBean) {
		tracer.addTag("quit", bankJsonBean.toString());
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());  
		//调用公用释放资源方法
		if(taskBank!=null){
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else{
			tracer.addTag("quit taskBank is null",""); 
		}
		return taskBank;
	}
}
