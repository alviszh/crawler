/**
 * 
 */
package app.service;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
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
import app.service.common.CmbcChinaHelperService;

/**
 * @author sln
 * @date 2018年8月9日下午6:28:03
 * @Description: 由于民生银行储蓄卡和信用卡登陆页面相同，故只写一个公共的登陆Service
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.cmbcchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.cmbcchina" })
public class CmbcChinaLoginService extends AbstractChaoJiYingHandler implements ICrawlerLogin{
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
			String baseUrl = "https://nper.cmbc.com.cn/pweb/static/login.html";  //登录链接
			driver.get(baseUrl);  //跳转到登录页面
			
			Thread.sleep(2000L);  //留取足够时间加载登录页面
			driver.findElement(By.id("writeUserId")).sendKeys(bankJsonBean.getLoginName().trim());  //输入登录名或卡号
			VK.Tab();
			Thread.sleep(1000);
			VK.KeyPress(bankJsonBean.getPassword().trim());// 输入密码
			Thread.sleep(1000);
			WebElement loginButton = driver.findElement(By.id("loginButton"));
			Actions action=new Actions(driver);  
			action.click(loginButton).perform();
			//=====================================================================
			try {
				WebDriverWait wait = new WebDriverWait(driver, 5);   
				Boolean isLogon=wait.until(ExpectedConditions.urlToBe("https://nper.cmbc.com.cn/pweb/static/main.html"));
				if(isLogon==true){
					 Thread.sleep(1000);   //留取足够的时间将登录成功后的页面加载完成，便于截图
					 //截图 
					 String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					 tracer.addTag("登录成功","截图:" +path);
					 tracer.addTag("登录成功，欢迎来到首页面","当前url:" + driver.getCurrentUrl());
			    	 Set<Cookie> cookies =  driver.manage().getCookies();
			 		 WebClient webClient = WebCrawler.getInstance().getWebClient();//  
			 		 for(Cookie cookie:cookies){
			 			 webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("nper.cmbc.com.cn",cookie.getName(),cookie.getValue()));
			 		 }
			 		 String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
			 		 taskBank.setCookies(cookieString);
				     taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase());
					 taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
					 taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription());
					 taskBank.setTesthtml(bankJsonBean.getPassword().trim()); //存储登录密码
					 taskBankRepository.save(taskBank);
				}
			} catch (Exception e) {
				if(driver.getCurrentUrl().equals("https://nper.cmbc.com.cn/pweb/static/firstloginmain.html")){
					//登陆成功了，但是首次登陆网银，需要更改密码
					taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
					taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
					taskBank.setDescription("欢迎您第首次登录网上银行，请先去官网设置您的个性登录名和登录密码~");
					taskBankRepository.save(taskBank);
				}else{
					//截图 
					String path;
					try {
						path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracer.addTag("判断是否登录成功，超出等待时间后，没有到达首页面，登陆出现错误，此时截图为：",path);
					} catch (Exception e1) {
						tracer.addTag("判断是否登录成功，超出等待时间后，没有到达首页面，登陆出现错误，采取截图方式,但是截图过程中出现异常:", ExUtils.getEDetail(e1));
					}
					tracer.addTag("action.login.waitfirstpage===>e",ExUtils.getEDetail(e));
					String errorfinfo = driver.findElement(By.id("jsonError")).getText(); 
					if(errorfinfo.length()>0){
						if(errorfinfo.contains("登录密码错误次数过多，已被冻结，请前往我行网点办理登录密码重置")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("登录密码错误次数过多，已被冻结，请前往我行网点办理登录密码重置");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("用户不存在或该账号未追加网银，请核实后重新输入")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("用户不存在或该账号未追加网银，请核实后重新输入");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("您未签约网银，请前往柜台或在线注册网银后重新登陆")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("您未签约网银，请前往柜台或在线注册网银后重新登陆");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("您输入的登录名/账号不存在，请输入正确的登录名/账号后重新尝试登录")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("您输入的登录名/账号不存在，请输入正确的登录名/账号后重新尝试登录");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("登录密码错误或与登录名不符，请核实后重新输入")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("登录密码错误或与登录名不符，请核实后重新输入");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("请录入登录名或已追加网银的账号")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("请录入登录名或已追加网银的账号");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("登录密码已锁定，请次日再尝试。若急需使用，请前往我行网点办理登录密码重置")){				
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("登录密码已锁定，请次日再尝试。若急需使用，请前往我行网点办理登录密码重置");
							taskBankRepository.save(taskBank);
						}else if(errorfinfo.contains("登录过于频繁，请稍后再试")){
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription("登录过于频繁，请稍后再试");
							taskBankRepository.save(taskBank);
						}else{  //未知原因，调研过程中未出现的情况
							taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
							taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
							taskBank.setDescription(errorfinfo);
							taskBankRepository.save(taskBank);
						}
					}else{
						taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
						taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
						taskBank.setDescription("系统登陆异常，未提示具体原因，请联系管理员~");
						taskBankRepository.save(taskBank);
					}
				}
			} 
		} catch (Exception e) {
			//截图 
			String path;
			try {
				path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracer.addTag("登陆过程===》获取登陆所需元素过程中出现异常，异常截图:", path);
			} catch (Exception e1) {
				tracer.addTag("登陆过程===》获取登陆所需元素过程中出现异常,采取截图方式,但是截图过程中出现异常:", ExUtils.getEDetail(e1));
			}
			tracer.addTag("action.login.taskid===>e",ExUtils.getEDetail(e));
			taskBank.setPhase(BankStatusCode.BANK_LOGIN_ERROR.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus());
			taskBank.setDescription("民生银行官网登录页面暂时存在异常，无法响应，请稍后再试~");
			taskBankRepository.save(taskBank);
		}
		try {
			tracer.addTag("释放instance ip ，quit webdriver",taskBank.getCrawlerHost());
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);   //此处有可能出现异常
			CmbcChinaHelperService.killProcess();  
			tracer.addTag(taskBank.getTaskid()+"执行了释放实例的代码，释放的ip是：", taskBank.getCrawlerHost());
		} catch (Exception e) {
			CmbcChinaHelperService.killProcess();  
			tracer.addTag("执行 agentService.releaseInstance方法时出现异常,调用了杀死ie进程的方法", e.getMessage());
		}
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
