package app.service;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaDebitcardUserinfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.ccbchina.CcbChinaDebitCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.ccbchina.CcbChinaDebitcardUserinfoRepository;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.RequestParam;
import app.commontracerlog.TracerLog;
import app.parser.CcbChinaCrawlerParser;
import app.parser.CcbChinaLoginParser;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.ccbchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.ccbchina"})
public class CcbChinaService implements ICrawlerLogin, ISms{
	
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private CcbChinaLoginParser ccbChinaLoginParser; 
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CcbChinaDebitcardUserinfoRepository ccbChinaDebitcardUserinfoRepository;
	@Autowired
	private CcbChinaCrawlerParser ccbChinaCrawlerParser;
	@Autowired
	private WebDriverChromeService webDriverChromeService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private CcbChinaDebitCardTransFlowRepository ccbChinaDebitCardTransFlowRepository;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private AgentService agentService;
	@Autowired
	private CcbChinaDebitcardLoginService ccbChinaDebitcardLoginService;
	
	private WebDriver driver;
	private String loginUrl = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_00?SERVLET_NAME=B2CMainPlat_00&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2";


	/**
	 * @Des 建行登录
	 * @param bankJsonBean
	 * @author zz
	 * @throws Exception 
	 * @throws NativeException 
	 * @throws IllegalAccessException 
	 */
	
	@Async
	public void loginByAccountNum(BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception{
		
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.output("crawler.bank.login.account.start", taskBank.getTaskid());		
		driver = getHtmlByAccount(bankJsonBean);
		try{
			Alert alert = driver.switchTo().alert();
			String message = alert.getText();
			tracerLog.output("alert的内容", message);
			alert.accept();
			
			//需要发送短信验证码
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(), 
					BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), 
					message, 
					BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false,bankJsonBean.getTaskid());
		}catch(NoAlertPresentException e){
			tracerLog.output("获取alert失败", e.getMessage());
			
			//截图
			String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("登录后页面截图：", path);
			
			String html = driver.getPageSource();
			tracerLog.output("账户名登录后页面：", "<xmp>"+html+"</xmp>");
			if(html.contains("您输入的登录密码不正确")){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}else if(html.contains("您输入的信息有误")){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}else if(html.contains("该客户只能以用户名登录")){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
						"该客户只能以用户名登录。", 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}else if(html.contains("请输入姓名并点击确认进入设置登录密码页面")){//您尚未设置网上银行登录密码，为了您的账户安全，请输入姓名并点击确认进入设置登录密码页面
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
						"您尚未设置网上银行登录密码，为了您的账户安全，请您到官网修改查询密码。", 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}else if(html.contains("您的网银登录密码强度较低或长时间未更换")){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
						"您的网银登录密码强度较低或长时间未更换，为了您的账户资金安全，请您到官网修改查询密码。", 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}else if(driver.getCurrentUrl().equals("https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1")){
				//登录成功
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(),false,bankJsonBean.getTaskid());
				
				
				//登录成功
				RequestParam params = ccbChinaLoginParser.getParam(html);
				taskBankStatusService.transforCookie(driver, "ibsbjstar.ccb.com.cn", taskBank, params);
				tracerLog.output("crawler.bank.login.account.success", taskBank.getTaskid());
				
				CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = ccbChinaLoginParser.getUserInfo(html);
				ccbChinaDebitcardUserinfo.setTaskid(taskBank.getTaskid());
				ccbChinaDebitcardUserinfoRepository.save(ccbChinaDebitcardUserinfo);
				taskBank.setUserinfoStatus(200);
				taskBankRepository.save(taskBank);
				tracerLog.output("crawler.bank.userinfo", "success");
				
//				crawler(taskBank,bankJsonBean);							
			}else if(html.contains("为让您更安全地使用网上银行，现对您当前使用的设备进行安全认证")){//发送短信
				//需要发送短信验证码
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false,bankJsonBean.getTaskid());		
			}else{
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
						"网络波动，请稍后再试。", 
						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}
			
		}
		
	}

	/**
	 * @Des 爬取
	 * @param taskBank
	 * @param bankJsonBean
	 */

	public void crawler(TaskBank taskBank, BankJsonBean bankJsonBean) {
		
		try {
			List<CcbChinaDebitCardTransFlow> transFlows = ccbChinaCrawlerParser.getBankStatement(taskBank,bankJsonBean);
			tracerLog.output("爬取后的总条数：", transFlows.size()+"");
			if(null != transFlows){
				ccbChinaDebitCardTransFlowRepository.saveAll(transFlows);
				
				taskBankStatusService.updateTaskBankTransflow(200,BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription(),
						bankJsonBean.getTaskid());
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getError_code(),true, 
						bankJsonBean.getTaskid());
				
				tracerLog.output("crawler.bank.crawler", "success");
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}
		} catch (IOException e) {			
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, 
					bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
	
	}

	/**
	 * @Des 通过卡号登录
	 * @param bankJsonBean
	 */
	@Async
	public void loginByCardNum(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.output("crawler.bank.login.card.start", taskBank.getTaskid());		
		try {
			driver = ccbChinaDebitcardLoginService.retryLogin(bankJsonBean);
			if(null != driver){
				//截图
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("登录后页面截图：", path);			
			}
			
			String html = driver.getPageSource();
//			tracerLog.output("卡号登录后页面：", "<xmp>"+html+"</xmp>");
			String url = driver.getCurrentUrl();
			if(!url.contains("main_savings")){
				if(html.contains("请输入正确的附加码")){
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							"系统繁忙，请稍后再试！", 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
					//释放机器，关闭driver进程
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}else if(html.contains("我行对储蓄账户查询服务进行了优化升级，原有查询密码将无法继续使用")){
					// 为了更好地保障您的账户安全，我行对储蓄账户查询服务进行了优化升级，原有查询密码将无法继续使用，请您点击登录页面上的“开通储蓄账户查询”链接，进行查询密码开通后查询。		 				
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							"当前查询密码无法继续使用，请您到建行官网重新设置查询密码后查询。", 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
					//释放机器，关闭driver进程
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);	
				}else{
					WebElement error = driver.findElement(By.cssSelector("p.fs_16 + p"));
					String errorMsg = error.getText();
					taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
							errorMsg, 
							BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
					//释放机器，关闭driver进程
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);				
				}
			}else{
				tracerLog.output("登陆成功，存入cookie", "success");
				taskBankStatusService.transforCookie(driver, "accounts.ccb.com", taskBank, null);
				tracerLog.output("crawler.bank.login.card.success", taskBank.getTaskid());
				
				CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = ccbChinaLoginParser.getUserInfoByCard(html);
				ccbChinaDebitcardUserinfo.setTaskid(taskBank.getTaskid());
				ccbChinaDebitcardUserinfoRepository.save(ccbChinaDebitcardUserinfo);
				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
				taskBank.setUserinfoStatus(200);
				taskBankRepository.save(taskBank);
				tracerLog.output("crawler.bank.userinfo", "success");
				
//				crawler(bankJsonBean);
			}
//			if(html.contains("您输入的密码不正确")){
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
//						BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
//						BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription(), 
//						BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
//				//释放机器，关闭driver进程
//				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//			}else if(html.contains("您输入的账户未设置查询密码")){
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase(), 
//						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus(), 
//						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getDescription(), 
//						BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//				//释放机器，关闭driver进程
//				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//			}else if(html.contains("请输入正确的附加码")){
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
//						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
//						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
//						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
//				//释放机器，关闭driver进程
//				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//			}else{
//				tracerLog.output("登陆成功，存入cookie", "success");
//				taskBankStatusService.transforCookie(driver, "accounts.ccb.com", taskBank, null);
//				tracerLog.output("crawler.bank.login.card.success", taskBank.getTaskid());
//				
//				CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = ccbChinaLoginParser.getUserInfoByCard(html);
//				ccbChinaDebitcardUserinfo.setTaskid(taskBank.getTaskid());
//				ccbChinaDebitcardUserinfoRepository.save(ccbChinaDebitcardUserinfo);
//				taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//				taskBank.setUserinfoStatus(200);
//				taskBankRepository.save(taskBank);
//				tracerLog.output("crawler.bank.userinfo", "success");
//				
//				crawler(bankJsonBean);
//			}
		} catch (Exception e) {
			tracerLog.output("getHtmlByCard", "error");
			tracerLog.output("loginByCardNum.getHtmlByCard", e.getMessage());
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
	}

	/**
	 * @Des 通过卡号爬取数据
	 * @param taskBank
	 * @param bankJsonBean
	 */
	@Async
	public void crawlerByCard(TaskBank taskBank, BankJsonBean bankJsonBean, int page) throws Exception  {
		tracerLog.output("crawler.ccbchina.cardnum", bankJsonBean.getTaskid());
		WebClient webClient = taskBank.getClient(taskBank.getCookies());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = "http://accounts.ccb.com/accounts/list_savings_ope.gsp?query=true&q_from_date=2000-07-26&q_to_date="+sdf.format(new Date())+"&to_page="+page;
		WebRequest request = new WebRequest(new URL(url),HttpMethod.GET);
		HtmlPage html = webClient.getPage(request);
		tracerLog.output("crawler.bank.ccbchina.getcrawler.onepage.page."+page, "<xmp>"+html.asXml()+"</xmp>");
		
		List<CcbChinaDebitCardTransFlow> transFlows = ccbChinaCrawlerParser.parserDataByCard(taskBank,html.asXml(),bankJsonBean);
		ccbChinaDebitCardTransFlowRepository.saveAll(transFlows);
		taskBank.setTransflowStatus(200);
		taskBankRepository.save(taskBank);
		tracerLog.output("crawler.bank.crawler.alldata", "success");
	}

	/**
	 * @Des 爬取当前第一页数据。并返回总页数
	 * @param taskBank
	 * @param bankJsonBean
	 * @return
	 * @throws Exception 
	 */
	public Integer getCrawlerOnePage(TaskBank taskBank, BankJsonBean bankJsonBean) {
		tracerLog.output("crawler.bank.ccbchina.getcrawler.onepage", bankJsonBean.getTaskid());
		WebClient webClient = taskBank.getClient(taskBank.getCookies());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = "http://accounts.ccb.com/accounts/list_savings_ope.gsp?query=true&q_from_date=2000-07-26&q_to_date="+sdf.format(new Date())+"&to_page=1";
		WebRequest request;
		HtmlPage page = null;
		try {
			request = new WebRequest(new URL(url),HttpMethod.GET);
			page = webClient.getPage(request);
		} catch (Exception e) {
			tracerLog.output("crawler.bank.ccbchina.getcrawleronepage", e.getMessage());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
					null, false, bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
		tracerLog.output("crawler.bank.ccbchina.getcrawler.onepage.page", "<xmp>"+page.asXml()+"</xmp>");
		
		List<CcbChinaDebitCardTransFlow> transFlows = ccbChinaCrawlerParser.parserDataByCard(taskBank,page.asXml(),bankJsonBean);
		Integer pageCount = ccbChinaCrawlerParser.getPageSize(page.asXml());
		
		ccbChinaDebitCardTransFlowRepository.saveAll(transFlows);
		taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS_ONEPAGE.getPhase(), 
				BankStatusCode.BANK_CRAWLER_SUCCESS_ONEPAGE.getPhasestatus(), 
				BankStatusCode.BANK_CRAWLER_SUCCESS_ONEPAGE.getDescription(), 
				BankStatusCode.BANK_CRAWLER_SUCCESS_ONEPAGE.getError_code(),false, 
				bankJsonBean.getTaskid());
		
		tracerLog.output("crawler.bank.crawler.onepage", "success");
		
		return pageCount;
	}
	
	
	public void crawler(BankJsonBean bankJsonBean){
		tracerLog.output("crawler.bank.crawler", bankJsonBean.getTaskid());
		
//		boolean isDoing = taskBankStatusService.isDoing(bankJsonBean.getTaskid());
		TaskBank taskBank = null;
//		if(isDoing){
//			tracerLog.output("正在进行上次未完成的爬取。。。。", bankJsonBean.getTaskid());
//		}else{
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
//					BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
//					BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
//					null, false, bankJsonBean.getTaskid());
		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.CARD_NUM)){
				
			Integer page = null;
			try {
				page = getCrawlerOnePage(taskBank,bankJsonBean);
			} catch (Exception e) {
				tracerLog.output("crawler.bank.ccbchina.getcrawleronepage", e.getMessage());
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
						null, false, bankJsonBean.getTaskid());
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}
			if(null != page && page>2){
				for(int i = 2; i <= page; i++){
					try {
						crawlerByCard(taskBank,bankJsonBean,i);
					} catch (Exception e) {
						tracerLog.output("crawler.bank.ccbchina.crawlerbycard.error", e.getMessage());
						taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
								BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
								BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
								null, false, bankJsonBean.getTaskid());
						//释放机器，关闭driver进程
						agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
					}						
				}
					
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription(), 
						BankStatusCode.BANK_CRAWLER_SUCCESS.getError_code(),true, 
						bankJsonBean.getTaskid());
				tracerLog.output("关闭页面driver", "success");
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);

			}
		}
	}
	
	
	/**
	 * @Des 登录
	 * @param bankJsonBean
	 * @return
	 * @throws IllegalAccessException
	 * @throws NativeException
	 * @throws Exception
	 */
	public WebDriver getHtmlByAccount(BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception {
		
		//打开建行登录页面
		driver = webDriverChromeService.getPageByChrome(loginUrl);
		
		WebElement usernameInput = driver.findElement(By.id("USERID"));
		WebElement passwordInput = driver.findElement(By.id("LOGPASS"));
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		usernameInput.click();
		
		usernameInput.sendKeys(bankJsonBean.getLoginName());
		passwordInput.sendKeys(bankJsonBean.getPassword());
		
		String path = WebDriverUnit.saveImg(driver,By.id("fujiama"));
		String code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
		tracerLog.output("crawler.bank.login.account.code", code);
		
		imgInput.sendKeys(code);
		
		//模拟点击登录按钮
		webDriverChromeService.clickButtonByDomId(driver, "loginButton");
		
		Thread.sleep(2000L);
		
		return driver;
	}
	
	/**
	 * @Des 通过卡号登录
	 * @param bankJsonBean
	 * @return
	 * @throws Exception 
	 */
	public WebDriver getHtmlByCard(BankJsonBean bankJsonBean) throws Exception {
		String cardUrl = "http://accounts.ccb.com/accounts/login_savings_ope.gsp";
		//打开建行登录页面
		driver = webDriverChromeService.getPageByChrome(cardUrl);
		
//		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//		try {
//			BufferedImage  fullImg = ImageIO.read(screenshot);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// Get the location of element on the page 
		WebElement ele = driver.findElement(By.tagName("iframe")); 
		Point point = ele.getLocation(); 
		
		driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
		//用户名输入框
		WebElement usernameInput = driver.findElement(By.id("ACC_NO_temp"));
		//密码输入框
		WebElement passwordInput = driver.findElement(By.name("LOGPASS"));			
		//图片验证码输入框
		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		//点击登录按钮
		WebElement button = driver.findElement(By.className("btn"));
		//使用键盘输入的按钮
		WebElement keyButton = driver.findElement(By.id("useKey"));
		
		//有数字键盘，改成用本地键盘录入。
		passwordInput.click();
		keyButton.click();
		
		//输入用户名密码
		usernameInput.sendKeys(bankJsonBean.getLoginName());
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		tracerLog.output("用户密码----》", bankJsonBean.getPassword());
		passwordInput.sendKeys(bankJsonBean.getPassword().trim());
		
		String code = null;
		try {
			String path = WebDriverUnit.saveImg(driver,By.id("fujiama"),point.getX(),point.getY());
			code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
			
			imgInput.sendKeys(code);
			button.click();
			
			//判断是否需要发送短信验证码
			//等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识btnConf1 则表示登录失败
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement eleHomePage = wait.until(new Function<WebDriver, WebElement>() {  
				public WebElement apply(WebDriver driver) {   
				return driver.findElement(By.id("error_main"));  
				}  
			});  
		} catch (Exception e) {
			return driver;
		}
		return driver;
	}

	public TaskBank findTaskBank(BankJsonBean bankJsonBean) {
		return taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
	}

	public TaskBank saveTask(TaskBank taskBank) {
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}

	/**
	 * 发送及接受短信
	 * @param bankJsonBean
	 */
//	public void sendSms(BankJsonBean bankJsonBean) {
//		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//		try{
//			//切换到发短信的页面
//			driver.switchTo().frame("mainfrm1");
//			//获取短信输入框
//			WebElement sms = driver.findElement(By.id("TRANSMSCODE"));
//			//获取确认按钮
//			WebElement btn = driver.findElement(By.id("btnNext"));
//			//输入短信
//			tracerLog.output("短信验证码：", bankJsonBean.getVerification());
//			sms.sendKeys(bankJsonBean.getVerification().trim());
//			//点击确认按钮
//			btn.click();
//			
//			try {
//				Thread.sleep(3000L);
//			} catch (InterruptedException e) {
//				
//			}
//			
//			CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = ccbChinaLoginParser.getUserInfoByCard(driver.getPageSource());
//			
//			ccbChinaDebitcardUserinfo.setTaskid(taskBank.getTaskid());
//			ccbChinaDebitcardUserinfoRepository.save(ccbChinaDebitcardUserinfo);
//			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
//			taskBank.setUserinfoStatus(200);
//			taskBankRepository.save(taskBank);
//			tracerLog.output("crawler.bank.userinfo", "success");
//			
//			crawler(bankJsonBean);
//			
//		}catch(Exception e){
//			tracerLog.output("输入短信页面alert窗口为空", e.getMessage());
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
//					null, true, bankJsonBean.getTaskid());
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		}
////		}else{
////			tracerLog.output("输入短信页面alert窗口为空", "null");
////			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
////					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
////					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
////					null, true, bankJsonBean.getTaskid());
////			//释放机器，关闭driver进程
////			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
////		}
//		
//	}

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = findTaskBank(bankJsonBean);
		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.CARD_NUM)){
			crawler(bankJsonBean);
		}
		
		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.ACCOUNT_NUM)){
			crawler(taskBank,bankJsonBean);	
		}
		return null;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		TaskBank taskBank = findTaskBank(bankJsonBean);
		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.CARD_NUM)){
			try{
				loginByCardNum(bankJsonBean);
			}catch(Exception e){
				tracerLog.output("crawler.bank.login.cardnum.exception", e.getMessage());
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			}
					
		}
		if(bankJsonBean.getLoginType().equals(StatusCodeLogin.ACCOUNT_NUM)){
			try{
				loginByAccountNum(bankJsonBean);			
			}catch(Exception e){
				tracerLog.output("crawler.bank.login.accountnum.exception", e.getMessage());
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			}		
		}
		return taskBank;
	}

	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try{
			//切换到发短信的页面
			driver.switchTo().frame("mainfrm1");
			//获取短信输入框
			WebElement sms = driver.findElement(By.id("TRANSMSCODE"));
			//获取确认按钮
			WebElement btn = driver.findElement(By.id("btnNext"));
			//输入短信
			tracerLog.output("短信验证码：", bankJsonBean.getVerification());
			sms.sendKeys(bankJsonBean.getVerification().trim());
			//点击确认按钮
			btn.click();
			
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				
			}
			
			taskBankStatusService.changeStatus("LOGIN", "SMS_SUCCESS", "发送及验证短信成功！", 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			
			CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = ccbChinaLoginParser.getUserInfoByCard(driver.getPageSource());
			
			ccbChinaDebitcardUserinfo.setTaskid(taskBank.getTaskid());
			ccbChinaDebitcardUserinfoRepository.save(ccbChinaDebitcardUserinfo);
			taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
			taskBank.setUserinfoStatus(200);
			taskBankRepository.save(taskBank);
			tracerLog.output("crawler.bank.userinfo", "success");
//			
//			crawler(bankJsonBean);
			
		}catch(Exception e){
			tracerLog.output("输入短信页面alert窗口为空", e.getMessage());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
					null, true, bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
//		}else{
//			tracerLog.output("输入短信页面alert窗口为空", "null");
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getDescription(), 
//					null, true, bankJsonBean.getTaskid());
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//		}
		return taskBank;
	}

	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}


}
