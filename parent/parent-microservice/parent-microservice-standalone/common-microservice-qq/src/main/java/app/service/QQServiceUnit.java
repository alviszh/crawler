package app.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.CookieJson;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.qq.json.QQStatusCode;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.qq.QQFriend;
import com.microservice.dao.entity.crawler.qq.QQMessage;
import com.microservice.dao.entity.crawler.qq.QQqun;
import com.microservice.dao.entity.crawler.qq.TaskQQ;
import com.microservice.dao.repository.crawler.qq.QQFriendRepository;
import com.microservice.dao.repository.crawler.qq.QQMessageRepository;
import com.microservice.dao.repository.crawler.qq.QQqunRepository;
import com.microservice.dao.repository.crawler.qq.TaskQQRepository;

import app.commontracerlog.TracerLog;
import app.parser.QQParser;
import app.unit.QQunit;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.qq"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.qq"})
public class QQServiceUnit extends QQunit{
	
	private WebDriver driver = null;
//	@Value("${webdriver.ie.driver.path}")
	static String driverPath="C:\\IEDriverServer_Win32\\chromedriver.exe";
	static Boolean headless = false;
	static String skey = "";
	@Autowired
	private TaskQQRepository taskQQRepository;
	@Autowired
	private QQParser qqParser;
	@Autowired
	private QQqunRepository qqunRepository;
	@Autowired
	private QQFriendRepository qqFriendRepository;
	@Autowired
	private QQMessageRepository qqmessageRepository;
	@Autowired
	private AgentService agentService;
	@Autowired
	private TracerLog tracer;
//	@Autowired
//	private QQunit qQunit;
	public TaskQQ login(PbccrcJsonBean pbccrcJsonBean, TaskQQ taskqq) throws Exception {
		driver = intiChrome();
		taskqq.setQqnum(pbccrcJsonBean.getUsername());
		taskqq.setPassword(pbccrcJsonBean.getPassword());
		String url = "https://xui.ptlogin2.qq.com/cgi-bin/xlogin?"
				+ "proxy_url=https%3A//qzs.qq.com/qzone/v6/portal/proxy.html"
				+ "&daid=5"
				+ "&&hide_title_bar=1"
				+ "&low_login=0"
				+ "&qlogin_auto_login=1"
				+ "&no_verifyimg=1"
				+ "&link_target=blank"
				+ "&appid=549000912"
				+ "&style=22"
				+ "&target=self"
				+ "&s_url=https%3A%2F%2Fqzs.qzone.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara%3Dizone"
				+ "&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4"
				+ "&pt_qr_link=http%3A//z.qzone.com/download.html"
				+ "&self_regurl=https%3A//qzs.qq.com/qzone/v6/reg/index.html"
				+ "&pt_qr_help_link=http%3A//z.qzone.com/download.html&pt_no_auth=0";
		driver.get(url);
		tracer.addTag("登录页", driver.getPageSource());
		Thread.sleep(5000);
		driver.findElement(By.id("switcher_plogin")).click();
		driver.findElement(By.id("u")).sendKeys(pbccrcJsonBean.getUsername());
		driver.findElement(By.id("p")).sendKeys(pbccrcJsonBean.getPassword());
		WebElement element = driver.findElement(By.id("login_button"));
		element.click();
		tracer.addTag("点击登录按钮", driver.getPageSource());
		String error = "";
		try {
			error = driver.findElement(By.id("err_m")).getText();
		} catch (Exception e) {
			String pageSource = driver.getPageSource();
			System.out.println(pageSource);
			if(pageSource.indexOf("的QQ空间")!=-1){
				System.out.println("登录成功");
				taskqq.setDescription("登录成功");
			}else{
				error = "超时请重新登录";
				System.out.println("登录失败");
				taskqq.setDescription(error);
			}
		}
		
		if(error.length()<1){
			System.out.println("登录成功");
		}else{
			System.out.println("登录失败："+error);
			tracer.addTag("登录失败", error);
			taskqq.setDescription(error);
			taskqq.setPhase(QQStatusCode.QQ_LOGIN_ERROR.getPhase());
			taskqq.setPhasestatus(QQStatusCode.QQ_LOGIN_ERROR.getPhasestatus());
			taskQQRepository.save(taskqq);
			driver.quit();
			return taskqq;
		}
		
		Thread.sleep(2000);
		String ur = "https://user.qzone.qq.com/"+pbccrcJsonBean.getUsername();
		driver.get(ur);
		
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		String transcookieToJson2 = transcookieToJson2(cookies);
		taskqq.setCookies(transcookieToJson2);
		taskqq.setDescription(QQStatusCode.QQ_LOGIN_SUCCESS.getDescription());
		taskqq.setPhase(QQStatusCode.QQ_LOGIN_SUCCESS.getPhase());
		taskqq.setPhasestatus(QQStatusCode.QQ_LOGIN_SUCCESS.getPhasestatus());
		taskQQRepository.save(taskqq);
		return taskqq;
	}
	public TaskQQ getqqqun(PbccrcJsonBean pbccrcJsonBean, TaskQQ taskqq, String g_tk) {
		
		String qunurl = "https://user.qzone.qq.com/proxy/domain/r.qzone.qq.com/cgi-bin/tfriend/qqgroupfriend_extend.cgi?"
				+ "uin="+taskqq.getQqnum()
				+ "&g_tk="+g_tk;
		driver.get(qunurl);
		String htmls = driver.getPageSource();
		tracer.addTag("qq群信息", htmls);
		List<QQqun> getqqqun = qqParser.getqqqun(htmls,taskqq.getTaskid());
		if(getqqqun!=null){
			qqunRepository.saveAll(getqqqun);
		}else{
			updateQunMsgStatus(pbccrcJsonBean.getMapping_id(), 404, 
					QQStatusCode.QQ_CRAWLER_QUN_MSG_SUCCESS.getDescription());
		}
		updateQunMsgStatus(pbccrcJsonBean.getMapping_id(), 200, 
				QQStatusCode.QQ_CRAWLER_QUN_MSG_SUCCESS.getDescription());
		//return new AsyncResult<String>("200");
		TaskQQ taskqq2 = taskQQRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		return taskqq2;
	}
	public TaskQQ getfriend(PbccrcJsonBean pbccrcJsonBean, TaskQQ taskqq, String g_tk) {
		String findurl = "https://user.qzone.qq.com/proxy/domain/r.qzone.qq.com/cgi-bin/tfriend/friend_mngfrd_get.cgi?"
				+ "uin="+taskqq.getQqnum()
				+ "&g_tk="+g_tk;
		driver.get(findurl);
		String html2 = driver.getPageSource();
		System.out.println(html2);
		tracer.addTag("qq好友", html2);
		List<QQFriend> friend=qqParser.getfriend(taskqq.getTaskid(),html2);
		if(friend!=null){
			qqFriendRepository.saveAll(friend);
		}else{
			updateFriendStatus(pbccrcJsonBean.getMapping_id(), 404, 
					QQStatusCode.QQ_CRAWLER_FRIEND_MSG_SUCCESS.getDescription());
		}
	//	return new AsyncResult<String>("200");
		updateFriendStatus(pbccrcJsonBean.getMapping_id(), 200, 
				QQStatusCode.QQ_CRAWLER_FRIEND_MSG_SUCCESS.getDescription());
		TaskQQ taskqq2 = taskQQRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		return taskqq2;
	}
	public TaskQQ getuser(PbccrcJsonBean pbccrcJsonBean, TaskQQ taskqq, String g_tk) {
		String url = "https://h5.qzone.qq.com/proxy/domain/base.qzone.qq.com/cgi-bin/user/cgi_userinfo_get_all?"
				+ "uin="+taskqq.getQqnum()
				+ "&vuin="+taskqq.getQqnum()
				+ "&fupdate=1"
				+ "&g_tk="+g_tk;
		driver.get(url);
		String html2 = driver.getPageSource();
		System.out.println(html2);
		tracer.addTag("qq个人信息", html2);
		QQMessage qquser = qqParser.getuser(taskqq.getTaskid(),html2);
		if(qquser!=null){
			qqmessageRepository.save(qquser);
		}else{
			updateMessageStatus(pbccrcJsonBean.getMapping_id(), 404, 
					QQStatusCode.QQ_CRAWLER_USER_MSG_SUCCESS.getDescription());
		}
		driver.quit();
	//	return new AsyncResult<String>("200");
		TaskQQ taskqq2 = taskQQRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		updateMessageStatus(pbccrcJsonBean.getMapping_id(), 200, 
				QQStatusCode.QQ_CRAWLER_USER_MSG_SUCCESS.getDescription());
		return taskqq2;
	}
	public static WebDriver intiChrome() throws Exception {
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
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static String transcookieToJson2(Set<org.openqa.selenium.Cookie> cookies){
		//Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 

		Set<CookieJson> cookiesSet= new HashSet<>();
		
		for(org.openqa.selenium.Cookie cookie:cookies){ 
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson); 
		} 

		String cookieJson = new Gson().toJson(cookiesSet);
		return cookieJson;

	}
	public static Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

	}
	public static String encryptedPhone(String jsname,String Navi,String writid, String runEval) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource(jsname, Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction(Navi,writid,runEval);
		return data.toString(); 
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	/**
     * @Des 系统退出，释放资源
     * @param pbccrcJsonBean
     */
    public void quit(PbccrcJsonBean pbccrcJsonBean){
        //调用公用释放资源方法
    	 agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
    }
	
}
