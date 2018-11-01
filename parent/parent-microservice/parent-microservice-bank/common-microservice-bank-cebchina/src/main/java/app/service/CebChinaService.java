package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaDebitCardDeadline;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaDebitcardDeadlineRepository;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaDebitcardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.cebchina.CebChinaDebitcardUserinfoRepository;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.VirtualKeyBoard;

import app.commontracerlog.TracerLog;


@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.bank.cebchina"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.bank.cebchina"})
public class CebChinaService {

	@Autowired
	private WebDriverIEService webDriverIEService;

	@Autowired
	private AgentService agentService;

	private WebDriver driver;

	@Autowired
	private JNativeService jNativeService;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private CebChainParser cebChainParser;

	@Autowired 
	private TracerLog tracerLog;

	@Autowired
	private CebChinaDebitcardUserinfoRepository cebChinaDebitcardUserinfoRepository;

	@Autowired
	private CebChinaDebitcardTransFlowRepository cebChinaDebitcardTransFlowRepository;

	@Autowired
	private CebChinaDebitcardDeadlineRepository cebChinaDebitcardDeadlineRepository;


	static String LoginPage = "https://www.cebbank.com/per/prePerlogin.do?_locale=zh_CN";//登录页
	static String GenIndex ="https://www.cebbank.com/per/perlogin1.do";//大众版首页
	static String DxinIndex ="https://www.cebbank.com/per/perlogin3.do";//短信
	static String ZyeIndex ="https://www.cebbank.com/per/perlogin4.do";//专业版首页

	public static String khjgdh = null;
	@Async
	public TaskBank loginCombo(BankJsonBean bankJsonBean) throws Exception{ 
		tracerLog.output("loginCombo","开始登陆光大银行"+bankJsonBean.getLoginName());
		TaskBank taskBank = null;
		try {
			//打开IE游览器，访问登录页面
			driver = openloginCebChina();

			String windowHandle = driver.getWindowHandle();
			bankJsonBean.setWebdriverHandle(windowHandle);
			tracerLog.output("登录光大银行，打开网页获取网页handler",windowHandle); 

			tracerLog.output("开始输入卡号",bankJsonBean.getLoginName()); 
			//键盘输入卡号
			Thread.sleep(2000L);//skey
			driver.findElement(By.id("skey")).sendKeys(bankJsonBean.getLoginName());
			//	VirtualKeyBoard.KeyPressEx(,50);//  sleep 不易设置果断，否则可能造成没有输入上的情况（11位数只输入了10位）
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("键盘输入卡号", path);

			tracerLog.output("开始输入Tab","Tab"); 
			//键盘输入Tab，让游览器焦点切换到密码框
			Thread.sleep(2000L);
			jNativeService.InputTab();

			tracerLog.output("开始输入密码",bankJsonBean.getPassword()); 
			//键盘输入查询密码15322868959
			//Thread.sleep(500L);
			VirtualKeyBoard.KeyPressEx(bankJsonBean.getPassword(),50);//   
			String path2 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("键盘输入密码", path2);

			tracerLog.output("开始点击登陆按钮","#LoginBtn"); 
			System.out.println("开始点击登陆按钮"+"#LoginBtn");
			//点击游览器的登录按钮
			Thread.sleep(2000L); 
			driver.findElement(By.xpath("//p[@class = 'loginbtn']")).click(); 
			String path3 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("键盘点击登录", path3);
			Thread.sleep(5000L); 
			//选择发送短信（2选一）
			//			List<WebElement> list = driver.findElements(By.xpath("//img[@class='shouzhi']"));
			//			list.get(1).click();
			String html = driver.getPageSource();
			if(html.indexOf("网银登录密码修改")!=-1){
				taskBankStatusService.changeStatus(BankStatusCode.BANK_RESETPASSWORD_ERROR.getPhase(), 
						BankStatusCode.BANK_RESETPASSWORD_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_RESETPASSWORD_ERROR.getDescription(), 
						BankStatusCode.BANK_RESETPASSWORD_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				return null;
			};
			String path4 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("点击登录按钮后", path4);
			String currentPageURL = driver.getCurrentUrl();
			System.out.println("当前页面URL"+currentPageURL);
			String htmlindex = driver.getPageSource();
			if(htmlindex.indexOf("忘记用户名")!=-1){
				//如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
				String errorfinfo = driver.findElement(By.id("exceptionDiv")).getText(); 
				if(errorfinfo!=null&&errorfinfo.length()>0) {
					tracerLog.output("登录失败,错误信息", errorfinfo);
					String[] split = errorfinfo.split("，有问题请点击在线客服");
					errorfinfo = split[0];
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), 
							errorfinfo, 
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(),true,bankJsonBean.getTaskid(),windowHandle); 

					String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("登录失败", "未知的错误:" + currentPageURL);
					tracerLog.output("登录失败", pat);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				} else{
					//截图 
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(),
							"登录失败，未知的错误",
							BankStatusCode.BANK_LOGIN_ERROR.getError_code(),true,bankJsonBean.getTaskid(),windowHandle);
					String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("登录失败，且没有登录失败错误信息", "未知的错误:" + currentPageURL);
					tracerLog.output("登录失败，且没有登录失败错误信息", pat);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}
			}else if(htmlindex.indexOf("为便于您记忆和使用，除用卡号登录之外，您也可以自行设置网银登录名，以后可使用登录名或卡号两种方式登录网上银行。")!=-1){
				tracerLog.output("logincmbchina", "未知的链接" + currentPageURL);
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
						"请前往光大银行官网登录个人网银，将网银登录名的设置成功后再进行操作！", 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
				String path54 = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("未知的链接", "未知的错误:" + driver.getCurrentUrl());
				tracerLog.output("未知的链接", path54);
				Thread.sleep(2000L);
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				
			}else if(htmlindex.indexOf("您的网银登录密码已超过6个月，为了您的账户安全，请及时更换网银登录密码！")!=-1
					&&htmlindex.indexOf("为便于您记忆和使用，除用卡号登录之外，您也可以自行设置网银登录名，以后可使用登录名或卡号两种方式登录网上银行。")==-1){
				driver.findElement(By.xpath("//span[@class = 'btn_r']")).click();
				String htmlin = driver.getPageSource();
				//https://www.cebbank.com/per/modLogInfo.do
				String currentUrl = driver.getCurrentUrl();
				if(currentUrl.equals("https://www.cebbank.com/per/modLogInfo.do")){
					JavascriptExecutor driver_js = ((JavascriptExecutor) driver);
					driver_js.executeScript("document.form1.submit();");
//					driver.findElement(By.xpath("//a[@onclick='document.form1.submit();']")).click();
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
					Thread.sleep(2000L);
					//帐户信息
					CebChinaDebitCardUserInfo cebChinaDebitCardUserInfo = getUserInfo(bankJsonBean);
					if(cebChinaDebitCardUserInfo!=null){
						cebChinaDebitCardUserInfo.setTaskid(bankJsonBean.getTaskid());
						cebChinaDebitcardUserinfoRepository.save(cebChinaDebitCardUserInfo);
						taskBankStatusService.updateTaskBankUserinfo(
								200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankUserinfo(
								201, BankStatusCode.BANK_USERINFO_ERROR.getDescription(), bankJsonBean.getTaskid());
						tracerLog.output("账户信息爬取失败，且没有登录失败错误信息", "未知的错误:" + driver.getCurrentUrl());
					}
					//流水
					List<CebChinaDebitCardTransFlow> transFlowlist = getTransFlow(bankJsonBean);
					if(transFlowlist.size()>0&&transFlowlist!=null){
						for (CebChinaDebitCardTransFlow resultset : transFlowlist) {
							resultset.setTaskid(bankJsonBean.getTaskid());
							cebChinaDebitcardTransFlowRepository.save(resultset);
						}
						taskBankStatusService.updateTaskBankTransflow(
								200, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankTransflow(
								201, BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), bankJsonBean.getTaskid());
						tracerLog.output("流水信息爬取失败，且没有失败错误信息", "未知的错误:" + driver.getCurrentUrl());
					}
					/**
					 * 期限
					 */
					List<CebChinaDebitCardDeadline> list2 = getDeadline(bankJsonBean);
					if(list2.size()>0&&list2!=null){
						cebChinaDebitcardDeadlineRepository.saveAll(list2);
					}
					else{
						tracerLog.output("getDeadline", "期限网页出问题");
					}
					taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}else{
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getPhasestatus(), 
							BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getDescription(), 
							BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
					tracerLog.output("logincmbchina", "未知的链接" + currentPageURL);
					tracerLog.output("未知的链接", "未知的错误:" + driver.getCurrentUrl());
					String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("未知的链接", pat);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}

			}else if(htmlindex.indexOf("网银操作记录")!=-1){
				/***
				 * 开通多个版本（不确定是否存在不选择版本页）
				 */
				//				if(htmlindex.indexOf("您已开通多个网银版本,请选择进入...")!=-1){
				//					tracerLog.output("loginCombo", currentPageURL); 
				//					tracerLog.output("登录成功", currentPageURL); 
				if (currentPageURL.equals(GenIndex)) {
					tracerLog.output("logincmbchina", "GenIndex 无需进行短信，直接开始获取数据");
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
					//选择不需要短信登录（大众版）
					//						List<WebElement> list = driver.findElements(By.xpath("//img[@class='shouzhi']"));
					//						list.get(0).click();
					Thread.sleep(2000L);
					//帐户信息
					CebChinaDebitCardUserInfo cebChinaDebitCardUserInfo = getUserInfo(bankJsonBean);
					if(cebChinaDebitCardUserInfo!=null){
						cebChinaDebitCardUserInfo.setTaskid(bankJsonBean.getTaskid());
						cebChinaDebitcardUserinfoRepository.save(cebChinaDebitCardUserInfo);
						taskBankStatusService.updateTaskBankUserinfo(
								200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankUserinfo(
								201, BankStatusCode.BANK_USERINFO_ERROR.getDescription(), bankJsonBean.getTaskid());
						tracerLog.output("账户信息爬取失败，且没有登录失败错误信息", "未知的错误:" + driver.getCurrentUrl());
					}
					//流水
					List<CebChinaDebitCardTransFlow> transFlowlist = getTransFlow(bankJsonBean);
					if(transFlowlist.size()>0&&transFlowlist!=null){
						for (CebChinaDebitCardTransFlow resultset : transFlowlist) {
							resultset.setTaskid(bankJsonBean.getTaskid());
							cebChinaDebitcardTransFlowRepository.save(resultset);
						}
						taskBankStatusService.updateTaskBankTransflow(
								200, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankTransflow(
								201, BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), bankJsonBean.getTaskid());
						tracerLog.output("流水信息爬取失败，且没有失败错误信息", "未知的错误:" + driver.getCurrentUrl());
					}
					/**
					 * 期限
					 */
					List<CebChinaDebitCardDeadline> list2 = getDeadline(bankJsonBean);
					if(list2.size()>0&&list2!=null){
						cebChinaDebitcardDeadlineRepository.saveAll(list2);
					}
					else{
						tracerLog.output("getDeadline", "期限网页出问题");
					}
					taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}else{
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.SYSTEM_QUIT.getPhase(), 
							BankStatusCode.SYSTEM_QUIT.getPhasestatus(), 
							BankStatusCode.SYSTEM_QUIT.getDescription(), 
							BankStatusCode.SYSTEM_QUIT.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
					tracerLog.output("logincmbchina", "未知的链接" + currentPageURL);
					tracerLog.output("未知的链接", "未知的错误:" + driver.getCurrentUrl());
					String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("未知的链接", pat);
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				}

				//				}
				//发短信情况1（URL无变化）
				if(htmlindex.indexOf("手机动态密码:")!=-1){
					tracerLog.output("logincmbchina", "GenLoginVerifyM2 需进行短信，开始发送短信验证码");
					taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(), 
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
					String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("需要短信", "未知的错误:" + driver.getCurrentUrl());
					tracerLog.output("需要短信", pat);
				}

			}
			//发短信情况2（URL变化）
			else if (currentPageURL.equals(DxinIndex)) {
				tracerLog.output("logincmbchina", "GenLoginVerifyM2 需进行短信，开始发送短信验证码");
				taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(), 
						BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
				String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("需要短信", "未知的错误:" + driver.getCurrentUrl());
				tracerLog.output("需要短信", pat);
			}else{
				tracerLog.output("logincmbchina", "未知的链接" + currentPageURL);
				String pat = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("未知的链接", "未知的错误:" + driver.getCurrentUrl());
				tracerLog.output("未知的链接", pat);
				taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getPhasestatus(), 
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getDescription(), 
						BankStatusCode.BANK_LOGIN_NOT_SET_SAFEISSUE_PWD_ERROR.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
				agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			}
			return taskBank; 
		} catch (Exception e) {
			String windowHandle = driver.getWindowHandle();
			tracerLog.output("login", "未知的" + e.toString());
			taskBank = taskBankStatusService.changeStatusbyWebdriverHandle(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid(),windowHandle);
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("信用卡信息错误信息", path);
			return taskBank;
		}

	}

	/***
	 * 个人信息
	 * @throws Exception 
	 */
	public CebChinaDebitCardUserInfo getUserInfo(BankJsonBean bankJsonBean) throws Exception{

		CebChinaDebitCardUserInfo cebChinaDebitCardUserInfo =null;
		try {
			driver.get("https://www.cebbank.com/per/perset.do");
			String LoginName = driver.findElement(By.name("F03001[0].KEHHAO")).getAttribute("value");//登录帐号
			String Name = driver.findElement(By.name("F03001[0].GERZWM")).getAttribute("value");//姓名
			//	String brithday = driver.findElement(By.name("F03001[0].CHUSRQ")).getAttribute("value");//出生日期
			String Idcare = driver.findElement(By.name("F03001[0].ZHJHAO")).getAttribute("value");//身份证
			String arrive = driver.findElement(By.name("F03001[0].ZJDJDZ")).getAttribute("value");//地址
			String lxdz = driver.findElement(By.name("F03001[0].ZZAIDZ")).getAttribute("value");//联系地址
			String youbian = driver.findElement(By.name("F03001[0].DSYHYB")).getAttribute("value");//邮政编码
			String lxphone = driver.findElement(By.name("F03001[0].LXDIAH")).getAttribute("value");//联系电话
			String khrq = driver.findElement(By.name("F03001[0].KAIHRQ")).getAttribute("value");//开户日期
			String phone = driver.findElement(By.name("F03001[0].YDDIAH")).getAttribute("value");//手机号码

			String sex = driver.findElement(By.name("UserSex")).getAttribute("value");//性别
			if(sex.equals("1")){
				sex="男";
			}else{
				sex="女";
			}
			khjgdh = driver.findElement(By.name("F03001[0].KHJGDH")).getAttribute("value");

			String url2 = "https://www.cebbank.com/per/FP020201.do";
			driver.get(url2);
			String html = driver.getPageSource();
			Document doc = Jsoup.parse(html);
			Element element = doc.getElementsByClass("td2").get(0);
			String kahao = element.getElementsByTag("td").get(2).text();
			String kaihuhang = element.getElementsByTag("td").get(4).text();

			cebChinaDebitCardUserInfo = new CebChinaDebitCardUserInfo(
					bankJsonBean.getTaskid(),LoginName,Name,"",Idcare,arrive,lxdz,youbian,lxphone,khrq,phone,sex,kahao,kaihuhang);
		} catch (Exception e) {
			tracerLog.output("getUserInfo", bankJsonBean.getTaskid());
			String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
			tracerLog.output("账户信息爬取失败，且没有失败错误信息", "未知的错误:" + driver.getCurrentUrl());
			tracerLog.output("未知的链接", path);
		}
		tracerLog.output("账户信息数据", cebChinaDebitCardUserInfo.toString());
		return cebChinaDebitCardUserInfo;
	}
	/***
	 * 流水
	 */
	public List<CebChinaDebitCardTransFlow> getTransFlow(BankJsonBean bankJsonBean){
		List<CebChinaDebitCardTransFlow> transFlowlist = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -6);
			Date m = c.getTime();
			String mon = format.format(m);//前6个月(开始时间)

			Calendar c2 = Calendar.getInstance();
			c2.setTime(new Date());
			Date n = c2.getTime();
			String mon2 = format.format(n);//当前月(结束时间)
			Thread.sleep(2000L);
			String url = "https://www.cebbank.com/per/FP020217.do?"
					+ "queryflag=1"
					+ "&CHFlag=0"
					+ "&Currency=01"
					+ "&AcCur=01"
					+ "&savekind=00"
					+ "&BeginDate="+mon
					+ "&EndDate="+mon2
					+ "&SavingKind=00"
					+ "&AcNo="+bankJsonBean.getLoginName();

			driver.get(url);

			String html2 = driver.getPageSource();
			Document doc = Jsoup.parse(html2);
			String xinxi = doc.select("div.txt_zt").text();
			String[] split2 = xinxi.split("：");
			String hao = split2[2].substring(0, 19).trim();
			String[] split3 = split2[3].split("钞");
			String chao = split3[0].trim();

			Elements elements2 = doc.select("div.xyy");
			Element element = elements2.get(0);
			String text = element.text();
			String[] split = text.split("]");
			String s = split[1];
			String page = s.substring(3, s.length()-5);
			transFlowlist = new ArrayList<CebChinaDebitCardTransFlow>();
			int i = Integer.parseInt(page);
			for(int a=1;a<=i;a++){
				String html3 = driver.getPageSource();
				System.out.println("33"+html3);
				Document doc2 = Jsoup.parse(html3);
				Elements elements = doc2.select("tr.td2");
				for (Element element2 : elements) {
					CebChinaDebitCardTransFlow cebChinaDebitCardTransFlow = new CebChinaDebitCardTransFlow();
					cebChinaDebitCardTransFlow.setCurrency(chao);
					cebChinaDebitCardTransFlow.setNum(hao);
					cebChinaDebitCardTransFlow.setJydate(element2.getElementsByTag("td").get(0).text().trim());
					cebChinaDebitCardTransFlow.setDebit(element2.getElementsByTag("td").get(1).text().trim());
					cebChinaDebitCardTransFlow.setAmount(element2.getElementsByTag("td").get(2).text().trim());
					cebChinaDebitCardTransFlow.setBalance(element2.getElementsByTag("td").get(3).text().trim());
					cebChinaDebitCardTransFlow.setReciprocalAccount(element2.getElementsByTag("td").get(4).text().trim());
					cebChinaDebitCardTransFlow.setReciprocalname(element2.getElementsByTag("td").get(5).text().trim());
					cebChinaDebitCardTransFlow.setAbstracts(element2.getElementsByTag("td").get(6).text().trim());
					System.out.println(cebChinaDebitCardTransFlow.toString());
					transFlowlist.add(cebChinaDebitCardTransFlow);
				}
				List<WebElement> elements4 = driver.findElements(By.xpath("//a[@class='txt03 txt_line']"));
				Thread.sleep(1000L);
				if(elements4.get(0).getText().equals("[下一页]")){
					WebElement webElement = driver.findElements(By.xpath("//a[@class='txt03 txt_line']")).get(0);
					webElement.click();
					String html4 = driver.getPageSource();
					System.out.println("44"+html4);
					Thread.sleep(1000L);
				}
				if(elements4.size()>2&&elements4.get(2).getText().equals("[下一页]")){
					if(elements4.get(2).getText().equals("[下一页]")){
						driver.findElements(By.xpath("//a[@class='txt03 txt_line']")).get(2).click();
						Thread.sleep(1000L);
					}
				}
			}
			tracerLog.output("流水信息数据", transFlowlist.toString());
			return transFlowlist;
		} catch (Exception e) {
			tracerLog.output("getTransFlow", bankJsonBean.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(
					404, BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), bankJsonBean.getTaskid());
			tracerLog.output("流水信息爬取失败，且没有失败错误信息", "未知的错误:" + driver.getCurrentUrl());
			return transFlowlist;
		}


	}
	/***
	 * 活、定期状态
	 * @param bankJsonBean
	 * @return
	 * @throws Exception
	 */
	public List<CebChinaDebitCardDeadline> getDeadline(BankJsonBean bankJsonBean){
		List<CebChinaDebitCardDeadline> list = null;
		try {
			if(khjgdh!=null){
				String url = "https://www.cebbank.com/per/FP020207.do?"
						+ "_viewReferer=query/queryAsyError"
						+ "&kind=br"
						+ "&AcNo="+bankJsonBean.getLoginName()
						+ "&AcType=1"
						+ "&AcOrganId="+khjgdh;
				driver.get(url);
				String html2 = driver.getPageSource();

				String html3 = html2.replaceAll("&gt;", ">");
				String html4 = html3.replaceAll("&lt;", "<");
				String html5 = html4.replaceAll("&amp;nbsp;", " ");
				list = cebChainParser.getDeadline(html5,bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			tracerLog.output("getDeadline", bankJsonBean.getTaskid());
		}
		tracerLog.output("定、活期信息", list.toString());
		return list;
	}

	public WebDriver openloginCebChina(){ 
		driver = webDriverIEService.getNewWebDriver();
		System.out.println("WebDriverIEService loginCmbChina Msg 开始登陆光大银行登陆页");
		tracerLog.output("WebDriverIEService loginCmbChina Msg", "开始登陆光大银行登陆页"); 
		try{
			driver = getPage(driver,LoginPage);
		}catch(NoSuchWindowException e){ 
			System.out.println("打开光大登录页面报错，尝试重新初始化游览器"+e.getMessage());
			tracerLog.output("打开光大登录页面报错，尝试重新初始化游览器", e.getMessage()); 
			driver = getPage(driver,LoginPage);
		} 
		tracerLog.output("WebDriverIEService loginCmbChina Msg", "光大银行登陆页加载已完成,当前页面句柄"+driver.getWindowHandle());
		return driver;
	} 
	public WebDriver getPage(WebDriver driver,String url) throws NoSuchWindowException{
		System.out.println("getPage:"+url);
		//	tracer.addTag("getPage", url); 
		//driver = getWebDriver(); 
		driver.get(url);
		return driver;
	}

	public String getCurrentUrl(WebDriver driver){
		return  driver.getCurrentUrl();
	}

	public void setSMSCode(BankJsonBean bankJsonBean) throws Exception {
		TaskBank taskBank = null;
		try {
			Thread.sleep(5000L);
			//输入短信
			VirtualKeyBoard.KeyPressEx(bankJsonBean.getVerification(),20);

			//确认按钮
			driver.findElement(By.className("btn_r")).click();
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement eleHomePage = null;
			try {
				eleHomePage = wait.until(new Function<WebDriver, WebElement>() {  
					public WebElement apply(WebDriver driver) {   
						return driver.findElement(By.id("n_2")); 
					}  
				});  
			} catch (NoSuchElementException e) {
				System.out.println("异常 NoSuchElementException .txt05 size01 txt_b 元素等待10秒未出现"); 
			} 
			String currentPageURL = driver.getCurrentUrl();
			System.out.println("当前页面URL"+currentPageURL);

			if(eleHomePage==null){
				tracerLog.output("登录失败", currentPageURL);  
				//如果当前页面还是登陆页面，说明登陆不成功，从页面中获取登陆不成功的原因（密码错误、密码简单、。。。等等）
				if(DxinIndex.equals(currentPageURL)) {
					String errorfinfo = driver.findElement(By.className("errbox mar01")).getText(); 
					if(errorfinfo!=null&&errorfinfo.length()>0) {
						tracerLog.output("登录失败,错误信息", errorfinfo); 
						taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_ERROR.getPhase(), 
								BankStatusCode.BANK_LOGIN_ERROR.getPhasestatus(), 
								errorfinfo, 
								BankStatusCode.BANK_LOGIN_ERROR.getError_code(),false,bankJsonBean.getTaskid()); 
					} else{
						//截图 
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("登录失败，且没有登录失败错误信息", "未知的错误:" + currentPageURL);
						tracerLog.output("登录失败，且没有登录失败错误信息", path);
						throw new RuntimeException("登录失败，且没有登录失败错误信息（未知的错误）");
					}
				}
				tracerLog.output("loginCombo", currentPageURL); 
			}else{
				tracerLog.output("登录成功", currentPageURL); 
				if (currentPageURL.equals(ZyeIndex)) {
					tracerLog.output("logincmbchina", "ZyeIndex,专业版开始获取数据");
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(), 
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(), 
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(), 
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false,bankJsonBean.getTaskid());

					Thread.sleep(2000L);
					//帐户信息
					CebChinaDebitCardUserInfo cebChinaDebitCardUserInfo = getUserInfo(bankJsonBean);
					if(cebChinaDebitCardUserInfo!=null){
						cebChinaDebitCardUserInfo.setTaskid(bankJsonBean.getTaskid());
						cebChinaDebitcardUserinfoRepository.save(cebChinaDebitCardUserInfo);
						taskBankStatusService.updateTaskBankUserinfo(
								200, BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankUserinfo(
								201, BankStatusCode.BANK_USERINFO_ERROR.getDescription(), bankJsonBean.getTaskid());
						tracerLog.output("账户信息爬取失败，且没有登录失败错误信息", "未知的错误:" + driver.getCurrentUrl());
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("未知的链接", "账户信息爬取失败:" + driver.getCurrentUrl());
						tracerLog.output("账户信息爬取失败", path);
					}
					/**
					 * 期限
					 */
					List<CebChinaDebitCardDeadline> list2 = getDeadline(bankJsonBean);
					if(list2.size()>0&&list2!=null){
						cebChinaDebitcardDeadlineRepository.saveAll(list2);
					}else{
						tracerLog.output("getDeadline", "期限网页出问题");
						String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
						tracerLog.output("期限网页出问题", "未知的错误:" + driver.getCurrentUrl());
						tracerLog.output("期限网页出问题", path);
					}
					//流水
					List<CebChinaDebitCardTransFlow> transFlowlist = getTransFlow(bankJsonBean);
					if(transFlowlist.size()>0&&transFlowlist!=null){
						for (CebChinaDebitCardTransFlow resultset : transFlowlist) {
							resultset.setTaskid(bankJsonBean.getTaskid());
							cebChinaDebitcardTransFlowRepository.save(resultset);
						}
						taskBankStatusService.updateTaskBankTransflow(
								200, BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), bankJsonBean.getTaskid());
					}else{
						taskBankStatusService.updateTaskBankTransflow(
								201, BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), bankJsonBean.getTaskid());
						tracerLog.output("流水信息爬取失败，且没有失败错误信息", "未知的错误:" + driver.getCurrentUrl());
					}
					taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
					agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
				} else {
					tracerLog.output("logincmbchina", "未知的链接" + currentPageURL);
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), 
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),false,bankJsonBean.getTaskid());
					String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
					tracerLog.output("未知的链接", "未知的错误:" + driver.getCurrentUrl());
					tracerLog.output("未知的链接", path);
				}  
			} 
		} catch (Exception e) {
			tracerLog.output("crawler.bank.setSMSCode.Exception", "短信验证失败"+e.toString());
			tracerLog.output("crawler.bank.setSMSCode.Exception.page", "<xmp>"+driver.getPageSource()+"</xmp>");
			String html = driver.getPageSource();
			if(html.indexOf("手机动态密码:")!=-1){
				Document doc = Jsoup.parse(html);
				String errorfinfo = doc.select("span.txt_red").get(2).text();
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(), 
						errorfinfo+"，请重新登录!", 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(),false,bankJsonBean.getTaskid());
				String path = WebDriverUnit.saveScreenshotByPath(driver,this.getClass());
				tracerLog.output("短信错误", "短信失败:" + driver.getCurrentUrl());
				tracerLog.output("短信错误", path);
			}
			//退出
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		}
	}

	/**
	 * @Des 系统退出，释放资源
	 * @param BankJsonBean 
	 */ 
	public TaskBank quit(BankJsonBean bankJsonBean){ 
		tracerLog.output("quit",bankJsonBean.toString()); 
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());  
		//调用公用释放资源方法
		if(taskBank!=null){
			agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
		} else{
			tracerLog.output("quit taskBank is null",""); 
		}
		return taskBank;
	} 

}
