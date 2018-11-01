package app.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocom.BocomDebitcardMsg;
import com.microservice.dao.entity.crawler.bank.bocom.BocomDebitcardTransFlow;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.bocom.BocomDebitcardMsgRepository;
import com.microservice.dao.repository.crawler.bank.bocom.BocomDebitcardTransFlowRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.VirtualKeyBoard;

import app.commontracerlog.TracerLog;
import app.parser.BocomParser;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.bocom"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.bocom"})
public class BocomService implements ICrawlerLogin, ISms {

	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private BocomParser bocomParser;
//	@Autowired
//	private ChaoJiYingOcrService chaoJiYingOcrService;
//	@Autowired
//	private JNativeService jNativeService;
	@Autowired
	private AgentService agentService;
//	@Autowired
//	private WebDriverIEService webDriverIEService;
	@Autowired
	private BocomDebitcardMsgRepository bocomDebitcardMsgRepository;
	@Autowired
	private BocomDebitcardTransFlowRepository bocomDebitcardTransFlowRepository;
	@Value("${spring.application.name}")
	String appName;
	@Autowired
	private BocomLoginService bocomLoginService;
	
	private WebDriver driver;
	private WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	String loginUrl = "https://pbank.95559.com.cn/personbank/logon.jsp";
	
	/**
	 * @Des 登录
	 * @param bankJsonBean
	 */
	public TaskBank login(BankJsonBean bankJsonBean) {
		
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.output("crawler.bank.login.account.start", taskBank.getTaskid());	
		//登录
		try{
			driver = bocomLoginService.retryLogin(bankJsonBean);
			
		}catch(RuntimeException e){
			tracerLog.output("BocomService.login.getHtml", "error");
			tracerLog.output("login.getHtml.error", e.getMessage());
			taskBank = 	taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(), 
					BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false,bankJsonBean.getTaskid());
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
			
		try {		
			//截图
			String loginPath = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
			tracerLog.output("登录后页面截图：", loginPath);
//			tracerLog.output("crawler.bank.login.page", driver.getPageSource());	
			//判断是否需要发送短信验证码
			//等待10秒(每2秒轮训检查一遍)，如果还没有登录成功的标识btnConf1 则表示登录失败
//			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//			try {
//				eleHomePage = wait.until(new Function<WebDriver, WebElement>() {  
//					public WebElement apply(WebDriver driver) {   
//						return driver.findElement(By.id("btnConf1"));  
//					}  
//				});  
//			} catch (Exception e) {		
//				tracerLog.output("未找到温馨提示页面", "有可能需要发送短信");
//			}
			
			if(driver.getPageSource().contains("您未注册或登录密码输入错误")){
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus(),
						"您未注册或登录密码输入错误，登录密码输错6次将无法登录。", 
						BankStatusCode.BANK_LOGIN_PWD_ERROR.getError_code(),false, bankJsonBean.getTaskid());
//			}else if(driver.findElement(By.id("captchaErrMsg")).getAttribute("style").equals("display: none;")){
//				//释放机器，关闭driver进程
//				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//				
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
//						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(),
//						"官网网络波动，请稍后重试。", 
//						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false, bankJsonBean.getTaskid());
			}else if(driver.getPageSource().contains("动态密码有效时间为5分钟，请尽快完成相关操作")){
				
				//点击发送短信按钮
				WebElement sms = driver.findElement(By.id("authSMSSendBtn"));
				sms.click();
				
				//需要发送短信验证码
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false,bankJsonBean.getTaskid());
				
			}else if(driver.getPageSource().contains("您还未添加银行卡")){
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getPhasestatus(),
						"您还未添加银行卡，暂时无法使用网银，请先登录手机银行点击“我的”完成添加银行卡。", 
						BankStatusCode.BANK_LOGIN_CAPTCHA_ERROR.getError_code(),false, bankJsonBean.getTaskid());
			}else if(driver.getPageSource().contains("确定")){
				WebElement eleHomePage = driver.findElement(By.id("btnConf1"));
				//不需要短信验证码
				tracerLog.output("crawler.bank.login.elehomepage", eleHomePage.toString());			
				eleHomePage.click();
				tracerLog.output("crawler.bank.login.clickPage", "<xmp>"+driver.getPageSource()+"</xmp>");
				
				//截图
				String noSMSPath = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("不需要短信验证码的登录的页面：：", noSMSPath);
				
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(),false,taskBank.getTaskid());
				
				
				//直接接着点击到查询页面
//				try {
//					taskBank = getAllData(bankJsonBean);
//				} catch (Exception e) {
//					//释放机器，关闭driver进程
//					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//					
//					tracerLog.output("crawler.bank.error.findsearchpage", e.getMessage());
//					e.printStackTrace();
//					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//							"网络出现波动，获取数据失败，请重新再试！", 
//							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),false,taskBank.getTaskid());
//				}
//				
			}else if(driver.getCurrentUrl().equals("https://pbank.95559.com.cn/personbank/system/syLogin.do")){ //直接进入登录后页面
//				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getError_code(),false,taskBank.getTaskid());
//				
				
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(),false,taskBank.getTaskid());
				//直接接着点击到查询页面
//				try {
//					taskBank = getAllData(bankJsonBean);
//				} catch (Exception e) {
//					//释放机器，关闭driver进程
//					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//					
//					tracerLog.output("crawler.bank.error.findsearchpage", e.getMessage());
//					e.printStackTrace();
//					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//							"网络出现波动，获取数据失败，请重新再试！", 
//							BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),false,taskBank.getTaskid());
//				}
			}else {
				//截图
				try {
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					//没有跳到正确的选择页面
					tracerLog.output("crawler.bank.login.error", "未知错误："+path);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				//释放机器，关闭driver进程
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR2.getPhase(), 
						BankStatusCode.BANK_LOGIN_ERROR2.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_ERROR2.getDescription(), 
						BankStatusCode.BANK_LOGIN_ERROR2.getError_code(),false,bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass()); 
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
				tracerLog.output("crawler.bank.login.cardnum.exception.error", e.getMessage());
			} catch (Exception e1) {
				
			}
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
		}
		return taskBank;
		
		
	}
	
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		try {
			WebElement frameMain = wait.until(new Function<WebDriver, WebElement>() {  
				public WebElement apply(WebDriver driver) {   
					return driver.findElement(By.id("btnConf1"));  
				}  
			});  
		} catch (Exception e) {		
			System.out.println("未找到温馨提示页面"+ "frameMain");
		}
		//点击账户查询
		driver.switchTo().frame("frameMain");
//		tracerLog.output("crawler.bank.login.frameMain", "<xmp>"+driver.getPageSource()+"</xmp>");
		driver.switchTo().frame("tranArea");
//		tracerLog.output("crawler.bank.login.tranArea", "<xmp>"+driver.getPageSource()+"</xmp>");
		WebElement a = driver.findElement(By.xpath("//a[@title='账户查询']"));	
		a.click();
//		tracerLog.output("crawler.bank.login.searchpage", "<xmp>"+driver.getPageSource()+"</xmp>");
		
		List<BocomDebitcardMsg> msgs = bocomParser.parserCardMsg(driver.getPageSource(),taskBank.getTaskid());
		if(null != msgs){
			bocomDebitcardMsgRepository.saveAll(msgs);
			taskBankStatusService.updateTaskBankUserinfo(200,"正在爬取中。。用户信息爬取完毕",taskBank.getTaskid());
		}
		//点击明细按钮
		List<WebElement> mingxis = driver.findElements(By.xpath("//TD[a='明细']/a[1]"));
		if(null != mingxis && mingxis.size()>0){
			//获取参数
			List<String> params = new ArrayList<String>();
			WebElement pessionIdE = driver.findElement(By.name("PSessionId"));
			String pessionId = pessionIdE.getAttribute("value");
			tracerLog.output("crawler.bank.findsearchpage.pessionid", pessionId);
			List<BocomDebitcardTransFlow> bocomDebitcardTransFlows = new ArrayList<BocomDebitcardTransFlow>();
			for(WebElement mingxi : mingxis){
				String onclick = mingxi.getAttribute("onclick");
				
				String[] strs = onclick.split(",");
				String cardNo = strs[0].substring(strs[0].indexOf("'")+1, strs[0].length()-1);
				String selectCardNo = strs[1].replaceAll("'", "").replace(")", "");
				
				params.add(selectCardNo);
				params.add(pessionId);
				params.add(cardNo);
				
				Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
				for(org.openqa.selenium.Cookie cookie : cookiesDriver){
					Cookie cookieWebClient = new Cookie("pbank.95559.com.cn", cookie.getName(), cookie.getValue());
					webClient.getCookieManager().addCookie(cookieWebClient);
				}
				//得到第一页的数据，因为每页的数据参数不同。
				//==================================请求第一页数据开始==================================//
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -2);
				cal.add(Calendar.DAY_OF_MONTH, +2);
				
				String url = "https://pbank.95559.com.cn/personbank/account/acTranRecordQuery.do?"
						+ "PSessionId="+pessionId+"&x-channel=0&menuCode=P002000"
						+ "&step=conf&cardNo="+cardNo+"&selectCardNo="+selectCardNo+"&startDate="+sdf.format(cal.getTime())
						+ "&endDate="+sdf.format(new Date())+"&acoAcRecord=&queryType=&serialNo=&page=1";
				
				tracerLog.output("第一页请求的url", url);
				WebRequest webRequest = null;
				try {
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Page page = null;
				try {
					page = webClient.getPage(webRequest);
				} catch (FailingHttpStatusCodeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				//第一页数据
				List<BocomDebitcardTransFlow> pageOne = null;
				try {
					pageOne = bocomParser.parserDate(page,taskBank,selectCardNo);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				if(null != pageOne){
					tracerLog.output("第一页数据size：", pageOne.size()+"");
					bocomDebitcardTransFlows.addAll(pageOne);					
				}else{
					continue;
				}
				
				Elements inputs = doc.select("[name^=pageUpList]");
				//表示没有数据
				if(null == inputs || inputs.size()<=0){
					continue;
				}
				List<String> names = new ArrayList<>();
				List<String> values = new ArrayList<>();
				
				for(Element input : inputs){
					names.add(input.attr("name"));
					values.add(input.attr("value"));
				}			
				//翻页所需参数
				String str = "&"+URLEncoder.encode(names.get(0))+"="+values.get(0)
				+ "&"+URLEncoder.encode(names.get(1))+"="+values.get(1)
				+ "&"+URLEncoder.encode(names.get(2))+"="+values.get(2)
				+ "&"+URLEncoder.encode(names.get(3))+"="+values.get(3)
				+ "&"+URLEncoder.encode(names.get(4))+"="+values.get(4);
				String lastStr = str;
				//==================================请求第一页数据结束,获取全部分页数据==================================//
				for(int i=2;i<50;i++){

					String param = "PSessionId="+pessionId
							+ "&x-channel=0"
							+ "&menuCode=P002000"
							+ "&step=conf"
							+ "&cardNo="+cardNo
							+ "&selectCardNo="+selectCardNo
							+ "&startDate="+sdf.format(cal.getTime())
							+ "&endDate="+sdf.format(new Date())
							+ "&acoAcRecord="
							+ "&queryType="
							+ "&serialNo="
							+ lastStr
							+ "&page="+String.valueOf(i)
							+ "&begTme="
							+ "&endTme="
							+ "&txnKnd=";
					String url1 = "https://pbank.95559.com.cn/personbank/account/acTranRecordQuery.do?"+param;
					WebRequest webRequest1 = null;
					try {
						webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Page page1 = null;
					try {
						page1 = webClient.getPage(webRequest1);
					} catch (FailingHttpStatusCodeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					writer(page1.getWebResponse().getContentAsString(),"C:/home/明细"+i+" ,"+aa+".txt");
					
					String change = "pageUpList%5B"+(i-1)+"%5D";
					String var = str.replaceAll("pageUpList%5B\\d%5D", change);
					
					lastStr+=var;
					System.out.println("当前"+i+"的str="+lastStr);
					if(page1.getWebResponse().getContentAsString().contains("未知系统异常")){
						break;
					}
					List<BocomDebitcardTransFlow> data = null;
					try {
						data = bocomParser.parserDate(page1,taskBank,selectCardNo);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(null == data){
						break;
					}else{
						tracerLog.output("第 "+i+"页数据size：", data.size()+"");
						bocomDebitcardTransFlows.addAll(data);
					}
					
				}
			}
			
			bocomDebitcardTransFlowRepository.saveAll(bocomDebitcardTransFlows);
			tracerLog.output("crawler.bank.bocom.crawler", "数据已入库");
			tracerLog.output("入库条数：", bocomDebitcardTransFlows.size()+"");
			//==================================获取全部分页数据完成==================================//
			taskBankStatusService.updateTaskBankTransflow(200,"",taskBank.getTaskid());
			taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(), 
					BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription(), 
					200, true, taskBank.getTaskid());

		}else{
			//释放机器，关闭driver进程
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			//没有获取到查询做需要的参数
			//截图
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				//没有跳到正确的选择页面
				tracerLog.output("crawler.bank.login.error", "未知错误："+path);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
					"当前用户未绑定银行卡！", 
					BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),false,taskBank.getTaskid());
		}
		
		//释放机器，关闭driver进程
		agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		return taskBank;
	
	}

//	public void login(BankJsonBean bankJsonBean) throws Exception {
//		
//		Properties props=System.getProperties(); 
//		String osName = props.getProperty("os.name"); //操作系统名称
//		String osArch = props.getProperty("os.arch"); //操作系统构架
//		String osVersion = props.getProperty("os.version"); //操作系统版本
//		
//		System.out.println("当前操作系统名称："+osName);
//		System.out.println("当前操作系统构架："+osArch);
//		System.out.println("当前操作系统版本："+osVersion);
//		
//		driver = webDriverIEService.getNewWebDriver();
//		
//		//ie浏览器打开银行登录页
//		driver = webDriverIEService.getPage(driver,loginUrl);
//		//获取登录框所在的位置
//		WebElement ele = driver.findElement(By.id("bannerLogin"));
//		Point point = ele.getLocation();
//		// Crop the entire page screenshot to get only element screenshot
//		System.out.println("point.getX()-------" + point.getX());
//		System.out.println("point.getY()-------" + point.getY());
//		int pX = point.getX();
//		int pY = point.getY();
//
////		tracerLog.output("crawler.bank.bocom.login.page", "<xmp>"+page+"</xmp>");
//		driver.switchTo().frame("bannerLogin");
//		String pageSource = driver.getPageSource();
////		tracerLog.output("crawler.bank.bocom.login.page", "<xmp>"+page+"</xmp>");
//		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
//		WebElement usernameInput = null;
//		try {
//			//用户名输入框
//			usernameInput = wait.until(new Function<WebDriver, WebElement>() {  
//				public WebElement apply(WebDriver driver) {   
//					return driver.findElement(By.id("alias"));  
//				}  
//			});  
//		} catch (Exception e) {		
//			tracerLog.output("未找到用户名输入框", e.getMessage());
//		}
//		
//		tracerLog.output("用户名输入框：", usernameInput.toString());
//		//登录点击按钮
//		WebElement button = driver.findElement(By.id("login"));
//		
//		//判断是否需要图片验证码，为true则需要。
//		if(pageSource.contains("var captchaEnable = 'true'")){
//			//截图，并保存图片验证码到本地
//			String path = WebDriverUnit.saveImg(driver,By.className("captchas-img-bg"),pX,pY);
//			//图片验证码输入框
//			WebElement imageInput = driver.findElement(By.id("input_captcha"));
//			
//			String code = chaoJiYingOcrService.callChaoJiYingService(path, "1902");
//			tracerLog.output("crawler.bank.bocom.login.code", code);
//			
//			usernameInput.click();
//			VirtualKeyBoard.KeyPressEx(bankJsonBean.getLoginName().trim());
//			
//			jNativeService.InputTab();
//			Thread.sleep(1000);
//			VirtualKeyBoard.KeyPressEx(bankJsonBean.getPassword().trim());
//			jNativeService.InputTab();
//			Thread.sleep(1000);
//			imageInput.sendKeys(code);
//		}else{
//			//不需要图片验证码的情况
//			usernameInput.click();
//			VirtualKeyBoard.KeyPressEx(bankJsonBean.getLoginName().trim(), 500);
//			
//			jNativeService.InputTab();
//			Thread.sleep(1000);
//			VirtualKeyBoard.KeyPressEx(bankJsonBean.getPassword().trim(), 500);
//		}
//		
//		button.click();
//		button.click();
//		Thread.sleep(5000);		
//		
//	}

	/**
	 * @Des 发送短信
	 * @param bankJsonBean
	 */
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		//短信输入框
		WebElement smsInput = driver.findElement(By.id("mobileCode"));
		//下一步按钮
		WebElement button2 = driver.findElement(By.id("btnConf2"));
		//输入短信验证码
		smsInput.sendKeys(bankJsonBean.getVerification());
		//点击下一步按钮
		button2.click();
		
		taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(), 
				BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code(),false,taskBank.getTaskid());
		
		String pageStr = driver.getPageSource();
		tracerLog.output("crawler.bank.bocom.sendsms", "<xmp>"+pageStr+"</xmp>");
		if(pageStr.contains("是否设为常用电脑")){
			//点击否
//			WebElement radio = driver.findElement(By.cssSelector("span.contact-radio:last-child"));
			WebElement radio = driver.findElements(By.className("contact-radio")).get(1);
//			WebElement radio = driver.findElement(By.xpath("//span[@class='contact-radio'][2]"));
			tracerLog.output("是否设为常用电脑", radio.getText());
			radio.click();
			//获取登录按钮
			WebElement button3 = driver.findElement(By.id("next"));
			//点击登录按钮
			button3.click();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(driver.getPageSource().contains("用这台电脑取代原先的常用电脑")){
				//点击暂不设置
				WebElement unband = driver.findElement(By.cssSelector("input[value=unband]"));
				tracerLog.output("暂不设置", unband.getText());
				//点击确认按钮
				WebElement submit = driver.findElement(By.id("next"));
				submit.click();				
			}
			
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(), 
					BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(), 
					BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(), 
					BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false,taskBank.getTaskid());
			
//			try {
//				taskBank = getAllData(bankJsonBean);
//			} catch (Exception e) {
//				//释放机器，关闭driver进程
//				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//				
//				tracerLog.output("crawler.bank.error.findsearchpage", e.getMessage());
//				e.printStackTrace();
//				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(), 
//						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), 
//						"网络出现波动，获取数据失败，请重新再试！", 
//						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getError_code(),false,taskBank.getTaskid());
//			}
//		}else{
//			//释放机器，关闭driver进程
//			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
//			
//			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getPhase(), 
//					BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getPhasestatus(), 
//					"短信验证失败，请重新登录", 
//					BankStatusCode.BANK_VALIDATE_CODE_FIRST_ERROR.getError_code(),false,bankJsonBean.getTaskid());
		}
		return taskBank;
		
	}

	/**
	 * @Des 爬取数据
	 * @param taskBank
	 * @param cardNo 
	 * @param pessionId 
	 * @param selectCardNo 
	 */
	public void crawler(TaskBank taskBank, String selectCardNo, String pessionId, String cardNo) throws Exception{
			
		tracerLog.output("crawler.bank.crawler", taskBank.getTaskid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		cal.add(Calendar.DAY_OF_MONTH, +2);

		taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
				BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
				BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
				null, false, taskBank.getTaskid());	
		for(int i=1;i<50;i++){
			String url = "https://pbank.95559.com.cn/personbank/account/acTranRecordQuery.do?"
					+ "PSessionId="+pessionId+"&x-channel=0&menuCode=P002000"
					+ "&step=conf&cardNo="+cardNo+"&selectCardNo="+selectCardNo+"&startDate="+sdf.format(cal.getTime())
					+ "&endDate="+sdf.format(new Date())+"&acoAcRecord=&queryType=&serialNo=&page="+String.valueOf(i);	
				
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			tracerLog.output("crawler.bank.bocom.transpage."+i, "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
			List<BocomDebitcardTransFlow> bocomDebitcardTransFlows = bocomParser.parserDate(page,taskBank,selectCardNo);
			if(null != bocomDebitcardTransFlows){
				bocomDebitcardTransFlowRepository.saveAll(bocomDebitcardTransFlows);
				tracerLog.output("crawler.bank.bocom.crawler."+i, "数据已入库");
			}else{
				System.out.println("数据已全部显示，或没有数据   。。"+i);
				break;
			}
		}	
	}

	/**
	 * @Des 保存host和port
	 * @param ip
	 * @param port
	 * @param bankJsonBean
	 */
	public void saveHost(String ip, Integer port, BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid().trim());
		taskBank.setCrawlerHost(ip);
		taskBank.setCrawlerPort(port+"");
		taskBankRepository.save(taskBank);
	}

	public TaskBank save(TaskBank taskBank) {
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}


	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
