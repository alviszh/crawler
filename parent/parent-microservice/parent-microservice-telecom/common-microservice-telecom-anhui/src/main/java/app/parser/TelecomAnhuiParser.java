package app.parser;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBusiness;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiCall;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiMessage;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiPay;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiScore;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class TelecomAnhuiParser {

	private static final String HtmlTextInput = null;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomAnhuiRetry telecomAnhuiRetry;

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	// 当前时间
	public String getTime(String fmt) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	//获取第一天
	public String getFirstDay(String fmt,int i) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -i);  
        c.set(Calendar.DAY_OF_MONTH, 1);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	//获取最后天
		public String getLastDay(String fmt,int i) throws Exception{
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -i+1);   
	        c.set(Calendar.DAY_OF_MONTH, 0);
			Date m = c.getTime();
			String mon = format.format(m);
			return mon;
		}
	
	
	// 通过URL获得HTMLpage
	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public WebClient addcookie(TaskMobile taskMobile) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	
	
	//net用Nexturl存cookie
	public WebClient addcookieNet(TaskMobile taskMobile) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getNexturl());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskMobile taskMobile, String url, HttpMethod type) throws Exception {
		tracer.addTag("TelecomGuangxiParser.getPage---url:", url + "taskId:" + taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomGuangxiParser.getPage.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomGuangxiParser.getPage---taskid:",
					taskMobile.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}

	// //登陆不发短信码的点击
	// public HtmlPage login(MessageLogin messageLogin, TaskMobile taskMobile)
	// throws Exception{
	// WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	//
	// //解析验证码
	// String
	// url="http://ah.189.cn/sso/VImage.servlet?random=0.8871965497595795";
	// Set<Cookie> cookies = webClient.getCookieManager().getCookies();
	// String code = chaoJiYingOcrService.getVerifycode(url,cookies, "1004");
	//
	// //验证验证码
	// String codeurl="http://ah.189.cn/sso/ValidateRandom?validCode="+code;
	// Page page = webClient.getPage(codeurl);
	// String url1="http://ah.189.cn/sso/login";
	//
	// HtmlPage pageLogin = webClient.getPage(url1);
	// HtmlDivision firstByXPath =
	// pageLogin.getFirstByXPath("//div[@class='bd_box']");
	// HtmlDivision submit =
	// pageLogin.getFirstByXPath("//div[@class='submit']");
	//
	// //HtmlDivision bd = firstByXPath.getFirstByXPath("//div[@class='bd']");
	// HtmlDivision bd = firstByXPath.getFirstByXPath("//div[@style='display:
	// block;']");
	// HtmlTextInput serviceNbr = (HtmlTextInput)
	// bd.getFirstByXPath("//input[@name='serviceNbr']");
	// HtmlTextInput passWord = (HtmlTextInput)
	// bd.getFirstByXPath("//input[@name='showPwd']");
	//
	// HtmlImage image = pageLogin.getFirstByXPath("//img[@id='vImg']");
	//
	// DomElement loginbtn = submit.getFirstElementChild();
	//
	// File file = new File("D:\\img\\anhui.jpg");
	// image.saveAs(file);
	//
	// HtmlTextInput validCode =
	// firstByXPath.getFirstByXPath("//input[@id='validCode']");
	//
	// serviceNbr.setText("17718194181");
	// passWord.setText("119110");
	// String inputValue = JOptionPane.showInputDialog("请输入验证码……");
	// validCode.setText(inputValue);
	// HtmlPage logined = loginbtn.click();
	//
	// System.out.println(logined.getBaseURI());
	// return null;
	// }

	// 登陆 不发短信码的post！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！（之前用的）
	public WebParam loginNet(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// //验证码
		// String url="http://ah.189.cn/sso/VImage.servlet?random=0.8871965497595795";
		// Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		// String code = chaoJiYingOcrService.getVerifycode(url,cookies,"1004");
		// //验证验证码
		// String codeurl="http://ah.189.cn/sso/ValidateRandom?validCode="+code;
		// Page page = webClient.getPage(codeurl);

		// 登陆页面
		String url = "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.action";
		
		String urlNew ="http://ah.189.cn/sso/login";
		WebRequest requestSettings1 = new WebRequest(new URL(urlNew), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(requestSettings1);
		// System.out.println(loginPage.asXml());
		WebParam webParam = new WebParam();
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
		}

		//图形验证码
		HtmlImage image = (HtmlImage) loginPage.querySelector("#vImg");
		String code = chaoJiYingOcrService.getVerifycode(image, "1902");

		// 服务密码加密
		String encryptedPhone = encryptedPhone(messageLogin.getPassword());

		//post请求
		WebRequest requestSettings = new WebRequest(new URL("http://ah.189.cn/sso/LoginServlet"), HttpMethod.POST);
		// String url1 ="http://ah.189.cn/sso/LoginServlet?loginType=4&accountType=9&loginName="+messageLogin.getName()+"&latnId=551&passType=1&passWord="+encryptedPhone+"&validCode="+code+"&csrftoken=&ssoAuth=0&returnUrl=/&sysId=1003";
		// WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings.setAdditionalHeader("Referer","http://ah.189.cn/sso/login");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");

		if(taskMobile.getCity().equals("合肥"))
		{
			taskMobile.setTrianNum(551);
		}
		else if(taskMobile.getCity().equals("蚌埠"))
		{
			taskMobile.setTrianNum(552);
		}
		else if(taskMobile.getCity().equals("芜湖"))
		{
			taskMobile.setTrianNum(553);
		}
		else if(taskMobile.getCity().equals("淮南"))
		{
			taskMobile.setTrianNum(554);
		}
		else if(taskMobile.getCity().equals("马鞍山"))
		{
			taskMobile.setTrianNum(555);
		}
		else if(taskMobile.getCity().equals("安庆"))
		{
			taskMobile.setTrianNum(556);
		}
		else if(taskMobile.getCity().equals("宿州"))
		{
			taskMobile.setTrianNum(557);
		}
		else if(taskMobile.getCity().equals("阜阳"))
		{
			taskMobile.setTrianNum(558);
		}
		else if(taskMobile.getCity().equals("黄山"))
		{
			taskMobile.setTrianNum(559);
		}
		else if(taskMobile.getCity().equals("滁州"))
		{
			taskMobile.setTrianNum(550);
		}
		else if(taskMobile.getCity().equals("淮北"))
		{
			taskMobile.setTrianNum(561);
		}
		else if(taskMobile.getCity().equals("铜陵"))
		{
			taskMobile.setTrianNum(562);
		}
		else if(taskMobile.getCity().equals("宣城"))
		{
			taskMobile.setTrianNum(563);
		}
		else if(taskMobile.getCity().equals("六安"))
		{
			taskMobile.setTrianNum(564);
		}
		else if(taskMobile.getCity().equals("池州"))
		{
			taskMobile.setTrianNum(566);
		}
		else if(taskMobile.getCity().equals("亳州"))
		{
			taskMobile.setTrianNum(560);
		}
		String trianNum = taskMobile.getTrianNum()+"";
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("loginType", "4"));
		requestSettings.getRequestParameters().add(new NameValuePair("result", "true"));//!!!!重点  页面没有
		requestSettings.getRequestParameters().add(new NameValuePair("accountType", "9"));
		requestSettings.getRequestParameters().add(new NameValuePair("loginName", messageLogin.getName()));
		requestSettings.getRequestParameters().add(new NameValuePair("serviceNbr", messageLogin.getName()));//!!!
		requestSettings.getRequestParameters().add(new NameValuePair("latnId", trianNum));
		requestSettings.getRequestParameters().add(new NameValuePair("passType", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("passWord", encryptedPhone));
		requestSettings.getRequestParameters().add(new NameValuePair("validCode", code));
		requestSettings.getRequestParameters().add(new NameValuePair("csrftoken", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("ssoAuth", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("returnUrl", "%2Fservice%2Faccount%2Finit.action"));
		requestSettings.getRequestParameters().add(new NameValuePair("sysId", "1003"));

		requestSettings.setCharset(Charset.forName("UTF-8"));
		Page pagezhu = webClient.getPage(requestSettings);
		System.out.println(pagezhu.getWebResponse().getContentAsString());
		                                                      
//		WebRequest requestSettings11 = new WebRequest(new URL("http://ah.189.cn/"), HttpMethod.GET);
		WebRequest requestSettings11 = new WebRequest(new URL("http://ah.189.cn/service/account/init.action"), HttpMethod.POST);
		// String url1 ="http://ah.189.cn/sso/LoginServlet?loginType=4&accountType=9&loginName="+messageLogin.getName()+"&latnId=551&passType=1&passWord="+encryptedPhone+"&validCode="+code+"&csrftoken=&ssoAuth=0&returnUrl=/&sysId=1003";
		// WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.POST);
		requestSettings11.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings11.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings11.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings11.setAdditionalHeader("Referer", "http://ah.189.cn/service/account/");
		requestSettings11.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings11.setCharset(Charset.forName("UTF-8")); 

		
		Page page = webClient.getPage(requestSettings11);
		Thread.sleep(5000);
		System.out.println(page.getWebResponse().getContentAsString());
		
		Set<Cookie> cookies1 = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies1) {
			System.out.println("登录后获取到的cookie是：" + cookie.toString());
		}
		
		//进行判断   企业号码 和  个人 号码 进行 请求 得到的页面 不一样 企业号 是登陆进去 白色界面  个人的请求不到
		if(page.getWebResponse().getContentAsString().contains("尊敬的"))
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(urlNew);
			webParam.setWebClient(webClient);
			return webParam;
		}
		else{
			WebRequest requestSettings5 = new WebRequest(new URL(" http://ah.189.cn/service/account/init.action"),HttpMethod.GET);
			requestSettings5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings5.setAdditionalHeader("Host", "ah.189.cn");
			requestSettings5.setAdditionalHeader("Origin", "http://ah.189.cn");
			requestSettings5.setAdditionalHeader("Referer", "http://ah.189.cn/service/account/");
			requestSettings5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings5.setCharset(Charset.forName("UTF-8"));
			List<NameValuePair> params5 = new ArrayList<NameValuePair>();
			params5.add(new NameValuePair("serviceNum", messageLogin.getName()));
			Page zcPage = webClient.getPage(requestSettings5);
			System.out.println(zcPage.getWebResponse().getContentAsString());
			webParam.setHtml(zcPage.getWebResponse().getContentAsString());
			webParam.setUrl(urlNew);
			webParam.setWebClient(webClient);
			return webParam;
		}
	}

	// //登陆 模拟点击、、post
	// public HtmlPage login(MessageLogin messageLogin, TaskMobile taskMobile)
	// throws Exception {
	// // 正常页面登陆
	// WebParam webParam = new WebParam();
	// String url1 ="http://ah.189.cn/sso/login";
	// //WebClient webClient = addcookie(taskMobile);
	// WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	// HtmlPage adminPage = webClient.getPage(url1);
	//
	//// HtmlImage image = page.getFirstByXPath("//img[@id='vImg']");
	//// String code1 = chaoJiYingOcrService.getVerifycode(image, "1004");
	//// HtmlTextInput inputuserjym =
	// page.getFirstByXPath("//input[@id='validCode']");
	//// inputuserjym.setText(code1);
	//// String inputuserjym = taskMobile.getNexturl().substring(0,4);
	//
	//
	// String
	// url="http://ah.189.cn/sso/VImage.servlet?random=0.8871965497595795";
	// Set<Cookie> cookies = webClient.getCookieManager().getCookies();
	// String inputuserjym = chaoJiYingOcrService.getVerifycode(url,cookies,
	// "1004");
	// String
	// codeurl="http://ah.189.cn/sso/ValidateRandom?validCode="+inputuserjym;
	// Page newpage = webClient.getPage(codeurl);
	// System.out.println("*************************VALIDATE");
	// System.out.println(newpage.getWebResponse().getContentAsString());
	//
	//
	// String urllogin =
	// "http://ah.189.cn/sso/LoginServlet?loginType=4&accountType=9&loginName="+messageLogin.getName()+"&latnId=551&passType=1&passWord="+taskMobile.getNexturl().substring(5)+"&validCode="+inputuserjym+"&csrftoken=&ssoAuth=0&returnUrl=/&sysId=1003";
	// WebRequest requestSettings = new WebRequest(new URL(urllogin),
	// HttpMethod.GET);
	// requestSettings.setAdditionalHeader("Accept",
	// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	// requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	// requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	// requestSettings.setAdditionalHeader("Connection", "keep-alive");
	// requestSettings.setAdditionalHeader("Content-Type",
	// "application/x-www-form-urlencoded");
	// requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
	// requestSettings.setAdditionalHeader("Host", "ah.189.cn");
	// requestSettings.setAdditionalHeader("Referer",
	// "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.action");
	// requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
	// requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows
	// NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78
	// Safari/537.36");
	// HtmlPage page = webClient.getPage(requestSettings);
	// System.out.println("*************************POST请求");
	// System.out.println(page.getWebResponse().getContentAsString());
	//// HtmlDivision firstByXPath = page.getFirstByXPath("//div[@id='lc']");
	//// HtmlTextInput username1 =(HtmlTextInput)
	// firstByXPath.getFirstByXPath("//input[@name='serviceNbr']");
	//// username1.reset();
	//// username1.setText(messageLogin.getName());
	////
	//// HtmlPasswordInput code2 =
	// firstByXPath.getFirstByXPath("//input[@name='showPwd']");
	//// code2.reset();
	//// code2.setText(messageLogin.getSms_code());
	////
	//// HtmlDivision firstByXPath3 =
	// firstByXPath.getFirstByXPath("//div[@class='submit']");
	//// DomElement submit = firstByXPath3.getFirstElementChild();
	//// HtmlPage page2 = submit.click();
	// WebClient client = page.getWebClient();
	// String url3="http://ah.189.cn/service/account/init.action";
	// Page page2 = client.getPage(url3);
	// System.out.println(page2.getWebResponse().getContentAsString());
	// if(null != page)
	// {
	// int cod = page.getWebResponse().getStatusCode();
	// if(cod==200)
	// {
	// System.out.println("-------------------------------------"+page.getWebResponse().getContentAsString());
	// HtmlAnchor name = page.getFirstByXPath("//a[@id='already_loginName']");
	//
	// if(name != null)
	// {
	// webParam.setCode(cod);
	// webParam.setHtml(page.getWebResponse().getContentAsString());
	// webParam.setPage(page);
	// webParam.setUrl(url);
	// return page;
	// }
	// }
	// }
	// return null;
	// }

	// //登陆 模拟点击
	// public HtmlPage login(MessageLogin messageLogin, TaskMobile taskMobile)
	// throws Exception {
	// // 正常页面登陆
	// WebParam webParam = new WebParam();
	// String url1 ="http://ah.189.cn/sso/login";
	// //WebClient webClient = addcookie(taskMobile);
	// WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	// HtmlPage page = webClient.getPage(url1);
	// HtmlHeading2 elementById = (HtmlHeading2) page.getElementById("nopw");
	// elementById.click();
	// HtmlImage image = page.getFirstByXPath("//img[@id='vImg']");
	// String code1 = chaoJiYingOcrService.getVerifycode(image, "1004");
	// HtmlTextInput inputuserjym =
	// page.getFirstByXPath("//input[@id='validCode']");
	//// inputuserjym.setText(code1);
	//// String inputuserjym = taskMobile.getNexturl().substring(0,4);
	//// String url =
	// "http://ah.189.cn/sso/LoginServlet?loginType=4&accountType=9&loginName="+messageLogin.getName()+"&latnId=551&passType=1&passWord="+taskMobile.getNexturl().substring(5)+"&validCode="+inputuserjym+"&csrftoken=&ssoAuth=0&returnUrl=/&sysId=1003";
	//// WebRequest requestSettings = new WebRequest(new URL(url),
	// HttpMethod.POST);
	//// requestSettings.setAdditionalHeader("Accept",
	// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	//// requestSettings.setAdditionalHeader("Accept-Encoding", "gzip,
	// deflate");
	//// requestSettings.setAdditionalHeader("Accept-Language",
	// "zh-CN,zh;q=0.8");
	//// requestSettings.setAdditionalHeader("Connection", "keep-alive");
	//// requestSettings.setAdditionalHeader("Content-Type",
	// "application/x-www-form-urlencoded");
	//// requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
	//// requestSettings.setAdditionalHeader("Host", "ah.189.cn");
	//// requestSettings.setAdditionalHeader("Referer",
	// "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.action");
	//// requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
	//// requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows
	// NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78
	// Safari/537.36");
	//// HtmlPage page = webClient.getPage(requestSettings);
	// HtmlDivision firstByXPath = page.getFirstByXPath("//div[@id='lc']");
	// HtmlTextInput username1 =(HtmlTextInput)
	// firstByXPath.getFirstByXPath("//input[@name='serviceNbr']");
	// username1.reset();
	// username1.setText(messageLogin.getName());
	//
	// HtmlTextInput code2 =(HtmlTextInput)
	// firstByXPath.getFirstByXPath("//input[@name='showPwd']");
	// code2.reset();
	// code2.setText(messageLogin.getSms_code());
	//
	// HtmlDivision firstByXPath3 =
	// firstByXPath.getFirstByXPath("//div[@class='submit']");
	// DomElement submit = firstByXPath3.getFirstElementChild();
	// HtmlPage page2 = submit.click();
	// WebClient client = page2.getWebClient();
	// String url3="http://ah.189.cn/service/account/init.action";
	// Page page3 = client.getPage(url3);
	// System.out.println(page3.getWebResponse().getContentAsString());
	// if(null != page)
	// {
	// int cod = page.getWebResponse().getStatusCode();
	// if(cod==200)
	// {
	// System.out.println("-------------------------------------"+page.getWebResponse().getContentAsString());
	// HtmlAnchor name = page.getFirstByXPath("//a[@id='already_loginName']");
	//
	// if(name != null)
	// {
	// webParam.setCode(cod);
	// webParam.setHtml(page.getWebResponse().getContentAsString());
	// webParam.setPage(page);
	// webParam.setUrl(url1);
	// return page;
	// }
	// }
	// }
	// return null;
	// }

	// 个人信息
	public WebParam<TelecomAnhuiUserInfo> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile)
			throws Exception {
		String url = "http://ah.189.cn/service/manage/showCustInfo.action";
		WebClient webClient = addcookieNet(taskMobile);
		HtmlPage page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiUserInfo> webParam = new WebParam<TelecomAnhuiUserInfo>();
		if (null != page) {
			int code = page.getWebResponse().getStatusCode();
			if (code == 200) {
				TelecomAnhuiUserInfo telecomAnhuiUserInfo = new TelecomAnhuiUserInfo();
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				Elements elements = doc.select(".clearfix");
				telecomAnhuiUserInfo.setName(elements.get(0).text());
				telecomAnhuiUserInfo.setCity(elements.get(1).text());
				telecomAnhuiUserInfo.setNetTime(elements.get(2).text());
				telecomAnhuiUserInfo.setBirthday(elements.get(3).text());
				telecomAnhuiUserInfo.setHobby(elements.get(4).text());
				telecomAnhuiUserInfo.setEducation(elements.get(5).text());
				telecomAnhuiUserInfo.setJob(elements.get(6).text());
				telecomAnhuiUserInfo.setSex(elements.get(7).text());
				telecomAnhuiUserInfo.setPeople(elements.get(8).text());
				telecomAnhuiUserInfo.setPhone(elements.get(9).text());
				telecomAnhuiUserInfo.setPhone(elements.get(10).text());
				telecomAnhuiUserInfo.setEmail(elements.get(11).text());
				telecomAnhuiUserInfo.setTaskid(messageLogin.getTask_id());
				webParam.setCode(code);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setPage(page);
				webParam.setUrl(url);
				webParam.setTelecomAnhuiUserInfo(telecomAnhuiUserInfo);
				return webParam;
			}
		}
		return null;
	}

	// 缴费
	public WebParam<TelecomAnhuiPay> getPay(MessageLogin messageLogin, TaskMobile taskMobile, int a) throws Exception {
		String dateBefore = getDateBefore("yyyy-MM-dd", a);
		String url = "http://ah.189.cn/service/pay/payrecordlist.action?currentPage=1&pageSize=10&recordReq.effDate="+ dateBefore + "&recordReq.expDate=" + getTime("yyyy-MM-dd") + "&recordReq.serviceNbr="+ messageLogin.getName() + "&recordReq.objType=0";
		WebClient webClient = addcookieNet(taskMobile);
		HtmlPage page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiPay> webParam = new WebParam<TelecomAnhuiPay>();
		TelecomAnhuiPay telecomAnhuiPay = null;
		List<TelecomAnhuiPay> list = new ArrayList<TelecomAnhuiPay>();
		if (null != page) {
			int code = page.getWebResponse().getStatusCode();
			if (code == 200) {
				if(page.getWebResponse().getContentAsString().contains("没有符合条件的记录"))
				{
					webParam.setUrl(url);
					webParam.setCode(203);
					webParam.setHtml(page.getWebResponse().getContentAsString());
					return webParam;
				}
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				Elements elements = doc.getElementsByTag("tr");
				if (elements != null) {
					for (int i = 0; i < elements.size(); i++) {
						Elements elements2 = elements.get(i).getElementsByTag("td");
						telecomAnhuiPay = new TelecomAnhuiPay();
						for (int j = 0; j < elements2.size(); j = j + 6) {
							telecomAnhuiPay.setStatus(elements2.get(j).text());
							telecomAnhuiPay.setPayTime(elements2.get(j + 1).text());
							telecomAnhuiPay.setOutMoney(elements2.get(j + 2).text());
							telecomAnhuiPay.setGetMoney(elements2.get(j + 3).text());
							telecomAnhuiPay.setBanlance(elements2.get(j + 4).text());
							telecomAnhuiPay.setTypePay(elements2.get(j + 5).text());
							telecomAnhuiPay.setTaskid(messageLogin.getTask_id());
							list.add(telecomAnhuiPay);
						}
					}
					webParam.setCode(code);
					webParam.setHtml(page.getWebResponse().getContentAsString());
					webParam.setList(list);
					webParam.setPage(page);
					webParam.setUrl(url);
					return webParam;
				}
			}
		}
		return null;
	}

	// 账单
	public WebParam<TelecomAnhuiBill> getBill(MessageLogin messageLogin, TaskMobile taskMobile, int a)
			throws Exception {
		String dateBefore = getDateBefore("yyyyMM", a);
		String string = encryptedPhone("prodServiceNbr="+messageLogin.getName());
		String url = "http://ah.189.cn/service/bill/queryBillDetail.action?uuid="+string+"&currentMonth="+ dateBefore + "&currentLatnId="+taskMobile.getTrianNum();
		WebClient webClient = addcookieNet(taskMobile);
		
		WebParam<TelecomAnhuiBill> webParam = new WebParam<TelecomAnhuiBill>();
		Page page = webClient.getPage(url);
		TelecomAnhuiBill telecomAnhuiBill= null;
		System.out.println(page.getWebResponse().getContentAsString()+"---------------=============");
		List<TelecomAnhuiBill> list = new ArrayList<TelecomAnhuiBill>();
		if (null != page) {
			int code = page.getWebResponse().getStatusCode();
			if (code == 200) {
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				Elements element2 = doc.getElementsByClass("titbill");
				if(element2.size()>1)
				{
					boolean equals = element2.toString().contains("dl");
					if(equals==true)
					{
						//话费等
						Elements elementsByTag = doc.getElementsByClass("titbill").get(1).getElementsByTag("dl");
						for (Element element : elementsByTag) {
							String text = element.text();
							String[] split3 = text.split(" ");
							telecomAnhuiBill = new TelecomAnhuiBill();
							telecomAnhuiBill.setMoney(split3[0]);
							telecomAnhuiBill.setBillName(split3[1]);
							telecomAnhuiBill.setTaskid(taskMobile.getTaskid());
						}
						Element element3 = doc.getElementsByClass("cinfo").get(0).getElementsByClass("vtop").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(0);
						String substring = element3.text().substring(0,7);	
						telecomAnhuiBill.setMonth(substring);
						list.add(telecomAnhuiBill);
						//套餐月账单
						Elements elementsByTag2 = doc.getElementsByClass("titbill").get(1).getElementsByTag("li");
						System.out.println(elementsByTag2);
						if(elementsByTag2.contains("套餐月基本费"))
						{
							for (Element element : elementsByTag2) {
								String text = element.text();
								String[] split3 = text.split(" ");
								telecomAnhuiBill = new TelecomAnhuiBill();
								telecomAnhuiBill.setMoney(split3[0]);
								telecomAnhuiBill.setBillName(split3[1]);
								telecomAnhuiBill.setTaskid(taskMobile.getTaskid());
								if(split3[1].equals("套餐月基本费"))
								{
									//System.out.println(telecomAnhuiBill);
									break;
								}
							}
							Element element4 = doc.getElementsByClass("cinfo").get(0).getElementsByClass("vtop").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(0);
							String substring1 = element4.text().substring(0,7);	
							telecomAnhuiBill.setMonth(substring1);
							list.add(telecomAnhuiBill);
						}
						
						//合计
						Elements elementsByTag3 = doc.getElementsByClass("titbill").get(1).getElementsByTag("h5");
						System.out.println(elementsByTag3);
						    for (Element element : elementsByTag3) {
							if(element.text().contains("."))
							{
								String text = element.text();
								String[] split3 = text.split(" ");
								telecomAnhuiBill = new TelecomAnhuiBill();
								telecomAnhuiBill.setMoney(split3[0]+"");
								telecomAnhuiBill.setBillName(split3[1]+"");
								telecomAnhuiBill.setTaskid(taskMobile.getTaskid());
							}
							else{
								continue;
							}
							
							Element element5 = doc.getElementsByClass("cinfo").get(0).getElementsByClass("vtop").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(0);
							String substring2 = element5.text().substring(0,7);	
							telecomAnhuiBill.setMonth(substring2);							
							
							list.add(telecomAnhuiBill);
							webParam.setCode(code);
							webParam.setHtml(page.getWebResponse().getContentAsString());
							webParam.setList(list);
							//webParam.setPage(page);
							webParam.setUrl(url);
							return webParam;
						}
					}
				}
				else if(element2.size()==1){
					webParam.setHtml(page.getWebResponse().getContentAsString());
					return webParam;
				}
			}
		}
		return null;
	}

	// 积分
	public WebParam<TelecomAnhuiScore> getScore(MessageLogin messageLogin, TaskMobile taskMobile, int i)
			throws Exception {
		String dateBefore = getDateBefore("yyyyMMdd", i);
		String url = "http://ah.189.cn/service/point/point_detail.action?pointTab=pointDetail&dBeginCycle=" + dateBefore+ "&dEndCycle=" + getTime("yyyyMMdd") + "&csrftoken=";
		WebClient webClient = addcookieNet(taskMobile);
		WebParam<TelecomAnhuiScore> webParam = new WebParam<TelecomAnhuiScore>();
		HtmlPage page = webClient.getPage(url);
		TelecomAnhuiScore telecomAnhuiScore = null;
		List<TelecomAnhuiScore> list = new ArrayList<TelecomAnhuiScore>();
		if (null != page) {
			int code = page.getWebResponse().getStatusCode();
			System.out.println(page.getWebResponse().getContentAsString());
			if (code == 200) {
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				if(doc.text().contains("暂无查到相关记录"))
				{
					webParam.setHtml(page.getWebResponse().getContentAsString());
					return webParam;
				}
				else 
				{
					Element element = doc.getElementsByTag("tbody").get(1);
					Elements elements = element.getElementsByTag("td");
					for (int j = 0; j < elements.size(); j = j + 3) {
						for (int j2 = 0; j2 < elements.size(); j2++) {
							telecomAnhuiScore = new TelecomAnhuiScore();
							telecomAnhuiScore.setMonth(elements.get(j).text());
							telecomAnhuiScore.setStatus(elements.get(j + 1).text());
							telecomAnhuiScore.setNewScore(elements.get(j + 2).text());
							telecomAnhuiScore.setTaskid(messageLogin.getTask_id());
						}
						list.add(telecomAnhuiScore);
					}
					webParam.setCode(code);
					webParam.setHtml(page.getWebResponse().getContentAsString());
					webParam.setList(list);
					webParam.setPage(page);
					webParam.setUrl(url);
					return webParam;
				}
				
			}
		}
		return null;
	}

	// 业务
	public WebParam<TelecomAnhuiBusiness> getBusiness(MessageLogin messageLogin, TaskMobile taskMobile)
			throws Exception {
		String url = "http://ah.189.cn/service/manage/getHandelProdList.action";
		WebClient webClient = addcookieNet(taskMobile);
		WebParam<TelecomAnhuiBusiness> webParam = new WebParam<TelecomAnhuiBusiness>();
		Page page = webClient.getPage(url);
		TelecomAnhuiBusiness telecomAnhuiBusiness = null;
		List<TelecomAnhuiBusiness> list = new ArrayList<TelecomAnhuiBusiness>();
		if (null != page) {
			if (page.getWebResponse().getContentAsString().contains("mainMealList")) {
				
				JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
				String string = fromObject.getString("mainMealList");
				JSONArray fromObject2 = JSONArray.fromObject(string);
				for (Object object : fromObject2) {
					JSONObject fromObject3 = JSONObject.fromObject(object);
					telecomAnhuiBusiness = new TelecomAnhuiBusiness();
					telecomAnhuiBusiness.setName(fromObject3.getString("name"));
					telecomAnhuiBusiness.setStatus(fromObject3.getString("handStatus"));
					telecomAnhuiBusiness.setDoTime(fromObject3.getString("createDate"));
					telecomAnhuiBusiness.setDescr(fromObject3.getString("descr"));
					telecomAnhuiBusiness.setStartTime(fromObject3.getString("effDate"));
					telecomAnhuiBusiness.setEndTime(fromObject3.getString("expDate"));
					telecomAnhuiBusiness.setTaskid(messageLogin.getTask_id());
					list.add(telecomAnhuiBusiness);
				}
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
				return webParam;
			}
		}
		return null;
	}

	// 通讯详单
//	public WebParam<TelecomAnhuiCall> getAllCall(MessageLogin messageLogin, TaskMobile taskMobile, int a)
//			throws Exception {
//		
//		
//		String purl = "http://ah.189.cn/service/bill/phoneAndInternetDetail.action?rnd="+Math.random();
//		WebClient webClient = addcookie(taskMobile);
//		HtmlPage page2 = webClient.getPage(purl);
//		Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
//		Element elementById = doc1.getElementById("macCode");
//		System.out.println(elementById.val());
//		
//		
//		webClient = addcookie(taskMobile);
//		String params = "currentPage=&pageSize=10&effDate="+getFirstDay("yyyy-MM-dd",a)+"&expDate="+getLastDay("yyyy-MM-dd", a)+"&serviceNbr="+messageLogin.getName()+"&operListID=2&isPrepay=0&pOffrType=481&random="+messageLogin.getSms_code()+"&macCode="+elementById.val();
//		//String params = "currentPage=&pageSize=10&effDate=2017-09-01&expDate=2017-09-31&serviceNbr="+messageLogin.getName()+"&operListID=2&isPrepay=0&pOffrType=481&random="+messageLogin.getSms_code()+"&macCode=="+elementById.val();
//
//		String string = encryptedPhone(params);
//		String url = "http://ah.189.cn/service/bill/feeDetailrecordList.action?_v="+string;
//		//WebClient webClient = addcookie(taskMobile);
//		WebParam<TelecomAnhuiCall> webParam = new WebParam<TelecomAnhuiCall>();
//		//HtmlPage page = webClient.getPage(url);
//		
//		WebRequest requestSettings = new WebRequest(new URL("http://ah.189.cn/service/bill/feeDetailrecordList.action?_v="+string), HttpMethod.GET);
//		requestSettings.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		requestSettings.setAdditionalHeader("Connection", "keep-alive");
//		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
//		requestSettings.setAdditionalHeader("Host", "ah.189.cn");
//		requestSettings.setAdditionalHeader("Referer","http://ah.189.cn/service/bill/fee.action?type=phoneAndInternetDetail");
//		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
//		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
////		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
////		requestSettings.getRequestParameters().add(new NameValuePair("_v", string));
//
//		Page page = webClient.getPage(requestSettings);
//		System.out.println("+==============+"+page.getWebResponse().getContentAsString());
//		List<TelecomAnhuiCall> list = new ArrayList<TelecomAnhuiCall>();
//		if (null != page) {
//			int code = page.getWebResponse().getStatusCode();
//			if (code == 200) {
//				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
//				System.out.println(doc.text());
//				if(doc.text().contains("没有符合条件的记录"))
//				{
//					webParam.setHtml(doc.text());
//					return webParam;
//				}
//				else if(doc.text().contains("输入的随机码错误"))
//				{
//					webParam.setHtml(doc.text());
//					return null;
//				}
//				else if(doc.text().contains("您输入随机码次数过多"))
//				{
//					return null;
//				}
//				else if(doc.text()==null)
//				{
//					return null;
//				}
//				else
//				{
//					Elements elements2 = doc.getElementsByClass("tabsty").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
//					if (elements2 != null) {
//						TelecomAnhuiCall t = null;
//						for (int i = 1; i < elements2.size(); i = i + 7) {
//							for (int j = 0; j < elements2.size() - 1; j++) {
//								t = new TelecomAnhuiCall();
//								// System.out.println(elements2.get(j).getElementsByTag("nobr").get(i).text());
//								t.setCallType(elements2.get(j).getElementsByTag("nobr").get(i).text());
//								t.setTalkType(elements2.get(j).getElementsByTag("nobr").get(i + 1).text());
//								t.setCallPlace(elements2.get(j).getElementsByTag("nobr").get(i + 2).text());
//								t.setHisNum(elements2.get(j).getElementsByTag("nobr").get(i + 3).text());
//								t.setHisPlace(elements2.get(j).getElementsByTag("nobr").get(i + 4).text());
//								t.setStartTime(elements2.get(j).getElementsByTag("nobr").get(i + 5).text());
//								t.setCallTime(elements2.get(j).getElementsByTag("nobr").get(i + 6).text());
//								t.setMoney(elements2.get(j).getElementsByTag("nobr").get(i + 7).text());
//								list.add(t);
//							}
//						}
//						webParam.setCode(code);
//						webParam.setHtml(page.getWebResponse().getContentAsString());
//						webParam.setList(list);
//						webParam.setUrl(url);
//						return webParam;
//					}
//				}
//				
//			}
//		}
//		return null;
//	}

//	// 短信详单
//	public WebParam<TelecomAnhuiMessage> getSMS(MessageLogin messageLogin, TaskMobile taskMobile, int a)
//			throws Exception {
//		
//		
//		String purl = "http://ah.189.cn/service/bill/phoneAndInternetDetail.action?rnd="+Math.random();
//		WebClient webClient = addcookie(taskMobile);
//		HtmlPage page2 = webClient.getPage(purl);
//		Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
//		Element elementById = doc1.getElementById("macCode");
//		System.out.println(elementById.val());
//		
//		String params = "currentPage=&pageSize=10&effDate="+getFirstDay("yyyy-MM-dd",a)+"&expDate="+getLastDay("yyyy-MM-dd", a)+"&serviceNbr="+messageLogin.getName()+"&operListID=4&isPrepay=0&pOffrType=481&random="+messageLogin.getSms_code()+"&macCode="+elementById.val();
//		//String params = "currentPage=&pageSize=10&effDate=2017-09-01&expDate=2017-09-31&serviceNbr="+messageLogin.getName()+"&operListID=2&isPrepay=0&pOffrType=481&random="+messageLogin.getSms_code()+"&macCode=83e9fee4cb8271307820aa064ab2b5f5";
//
//		String string = encryptedPhone(params);
//		String url = "http://ah.189.cn/service/bill/feeDetailrecordList.action?_v="+string;
//		//WebClient webClient = addcookie(taskMobile);
//		TelecomAnhuiMessage telecomAnhuiMessage = null;
//		WebParam<TelecomAnhuiMessage> webParam = new WebParam<TelecomAnhuiMessage>();
//		Page page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
//		List<TelecomAnhuiMessage> list = new ArrayList<TelecomAnhuiMessage>();
//		if (null != page) {
//			int code = page.getWebResponse().getStatusCode();
//			if (code == 200) {
//				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
//				System.out.println(doc.text());
//				if(doc.text().contains("没有符合条件的记录"))
//				{
//					webParam.setHtml(doc.text());
//					return webParam;
//				}
//				else if(doc.text().contains("输入的随机码有误"))
//				{
//					return null;
//				}
//				else if(doc.text().contains("您输入随机码次数过多"))
//				{
//					return null;
//				}
//				else
//				{
//					Elements elements2 = doc.getElementsByClass("tabsty").get(0).getElementsByTag("tbody").get(0).getElementsByTag("nobr");
//					if (elements2 != null) {
//						for (int i2 = 1; i2 < elements2.size(); i2 = i2 + 11) {
//
//							for (int j = 0; j < elements2.size(); j++) {
//								telecomAnhuiMessage = new TelecomAnhuiMessage();
//								telecomAnhuiMessage.setSmsType(elements2.get(i2).text());
//								telecomAnhuiMessage.setGetType(elements2.get(i2 + 1).text());
//								telecomAnhuiMessage.setHisNum(elements2.get(i2 + 2).text());
//								telecomAnhuiMessage.setSendTime(elements2.get(i2 + 3).text());
//								telecomAnhuiMessage.setMoney(elements2.get(i2 + 4).text());
//							}
//							list.add(telecomAnhuiMessage);
//						}
//						webParam.setCode(code);
//						webParam.setHtml(page.getWebResponse().getContentAsString());
//						webParam.setList(list);
//						webParam.setUrl(url);
//						return webParam;
//					}
//				}
//				
//
//			}
//		}
//		return null;
//	}

	// 第一次发送验证码
	public WebParam getPhoneCodeFirst(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		// 正常页面登陆
		String url6 = "http://ah.189.cn/sso/login";
		HtmlPage html = getHtml(url6, webClient);
		HtmlDivision firstByXPath = html.getFirstByXPath("//div[@class='login-con']");

		// HtmlTextInput username1 =(HtmlTextInput)
		// firstByXPath.getFirstByXPath("//input[@class='login-text']");
		// username1.reset();
		// username1.setText(messageLogin.getName());
		// HtmlImage image = firstByXPath.getFirstByXPath("//img[@id='vImg']");
		// String code = chaoJiYingOcrService.getVerifycode(image, "4004");
		// HtmlTextInput inputuserjym =
		// firstByXPath.getFirstByXPath("//input[@id='validCode']");

		String url = "http://ah.189.cn/sso/VImage.servlet?random=0.8871965497595795";
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		String code = chaoJiYingOcrService.getVerifycode(url, cookies, "1004");
		// inputuserjym.setText(code);
		String codeurl = "http://ah.189.cn/sso/ValidateRandom?validCode=" + code;
		Page page = webClient.getPage(codeurl);

		// 用于登陆时的参数password 发送手机的验证码
		// TestJs test = new TestJs();
		String encryptedPhone = encryptedPhone(messageLogin.getSms_code());
		// password 拼在一起 放入大字段
		taskMobile.setNexturl(encryptedPhone);// post
		// taskMobile.setNexturl(code+encryptedPhone);//模拟点击
		taskMobileRepository.save(taskMobile);

		// 发送验证码需要的加密参数
		String mobilNbr = messageLogin.getName();
		String _key = mobilNbr.substring(1, 2) + mobilNbr.substring(3, 4) + mobilNbr.substring(6, 7)+ mobilNbr.substring(8, 10);
		String params = "validCode=" + code + "&phone=" + mobilNbr + "&key=" + _key;

		String str = encryptedPhone(params);
		String urlurl = "http://ah.189.cn/sso/SendSms?_v=" + str;

		WebRequest requestSettings = new WebRequest(new URL(urlurl), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings.setAdditionalHeader("Referer", "http://ah.189.cn/sso/login?returnUrl=%2Fservice%3Ftype");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page pagedl = webClient.getPage(requestSettings);
		webParam.setWebClient(webClient);
		webParam.setHtml(pagedl.getWebResponse().getContentAsString());
		// System.out.println(pagedl);
		// HtmlTextInput firstByXPath2 =
		// firstByXPath.getFirstByXPath("//input[@class='login-text-yzm']");
		// firstByXPath2.reset();
		// firstByXPath2.setText(text);

		// HtmlAnchor abutton = html.getFirstByXPath("//a[@class='loginBtn']");
		// HtmlPage page2 = abutton.click();
		// System.out.println(page2.getWebResponse().getContentAsString());
		return webParam;
	}

	
	//加密js2
	public String encryptedPhoneTwo(String phonenum,String nodeValue) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecom2.js", Charsets.UTF_8);
		// System.out.println(path);
		// FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum,nodeValue);
		return data.toString();
	}
	
	//加密js
		public String encryptedPhone(String phonenum) throws Exception {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			String path = this.readResource("telecom.js", Charsets.UTF_8);
			// System.out.println(path);
			// FileReader reader1 = new FileReader(path); // 执行指定脚本
			engine.eval(path);
			final Invocable invocable = (Invocable) engine;
			Object data = invocable.invokeFunction("encryptedString", phonenum);
			return data.toString();
		}

	public String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	// 第二次发送验证码
//	public WebParam getphonecodeTwo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
//		String mobilNbr = messageLogin.getName();
//		String _key = mobilNbr.substring(1, 2) + mobilNbr.substring(3, 4) + mobilNbr.substring(6, 7)+ mobilNbr.substring(8, 10);
//		String params = "mobileNum=" + mobilNbr + "&key=" + _key;
//		String str = encryptedPhone(params);
//		String url = "http://ah.189.cn/service/bill/sendValidReq.action?_v=" + str;
//		WebClient webClient = addcookie(taskMobile);  
//		
//		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
//		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		requestSettings.setAdditionalHeader("Connection", "keep-alive");
//		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
//		requestSettings.setAdditionalHeader("Host", "ah.189.cn");
//		requestSettings.setAdditionalHeader("Referer", "http://ah.189.cn/service/bill/fee.action?type=phoneAndInternetDetail");
//		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36");
//		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		Page page = webClient.getPage(requestSettings);
//		System.out.println("------"+page.getWebResponse().getContentAsString());
//		WebParam webParam = new WebParam();
//		if(null != page)
//		{
//			int code = page.getWebResponse().getStatusCode();
//			if(code ==200)
//			{
//				webParam.setCode(code);
//				webParam.setHtml(page.getWebResponse().getContentAsString());
//				webParam.setUrl(url);
//				webParam.setWebClient(webClient);
//				return webParam;
//			}
//		}
//		return null;
//	}
	

	//验证验证码
//	public boolean setphonecode(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
//		String url ="http://wapah.189.cn/detailSingleQuery.shtml?startDate="++"&endDate="++"&serviceNbr="++"&type=1&sendcheck="+messageLogin.getSms_code();
//		WebClient webClient = addcookie(taskMobile);
//		Page page = webClient.getPage(url);
//		if(page.getWebResponse().getContentAsString().contains("输入的随机码错误"))
//		{
//			return false;fmList
//		}
//		else
//		{
//			return true;
//		}
//	}

	
	/**
	 * 2017-10-11 yl 通话详单
	 * @param messageLogin
	 * @param taskMobile
	 * @param j
	 * @return  
	 * @throws Exception 
	 */
	public WebParam<TelecomAnhuiCall> getAllCall(MessageLogin messageLogin, TaskMobile taskMobile, int j) throws Exception {
//		    String url1="http://wapah.189.cn/detailSingleQuery.shtml?startDate="+getFirstDay("yyyy-MM-dd",j)+"&endDate="+getLastDay("yyyy-MM-dd",j)+"&serviceNbr="+messageLogin.getName()+"&type=2&sendcheck="+messageLogin.getSms_code();      
		String url1="http://wapah.189.cn/detailQueryAfter.shtml?startDate="+getFirstDay("yyyy-MM-dd",j)+"&endDate="+getLastDay("yyyy-MM-dd",j)+"&serviceNbr="+messageLogin.getName()+"&type=2&sendcheck="+messageLogin.getSms_code()+"&pageNum=10";
			WebClient webClient = addcookieNet(taskMobile); 
			Page page2 = webClient.getPage(url1);//先请求首页
			Thread.sleep(1000);
 			if(page2.getWebResponse().getContentAsString().contains("fmList"))
			{
				JSONObject fromObjec = JSONObject.fromObject(page2.getWebResponse().getContentAsString());
				String strin = fromObjec.getString("flowUseList");
//				System.out.println(strin);
				if(strin.contains("pay"))
				{
					JSONObject fromObject22 = JSONObject.fromObject(strin);
					String string2 = fromObject22.getString("totalLines");
//					System.out.println(string2);//找到本月的总数
					String url = "http://wapah.189.cn/detailQueryAfter.shtml?startDate="+getFirstDay("yyyy-MM-dd",j)+"&endDate="+getLastDay("yyyy-MM-dd",j)+"&serviceNbr="+messageLogin.getName()+"&type=2&sendcheck="+messageLogin.getSms_code()+"&pageNum="+string2;
//					Page page = telecomAnhuiRetry.getCallRetry(taskMobile, messageLogin, url, webClient);
					Page page = webClient.getPage(url);
					Thread.sleep(2000);
//					System.out.println(url+page.getWebResponse().getContentAsString());
					TelecomAnhuiCall telecomAnhuiCall = null;
					List<TelecomAnhuiCall> list = new ArrayList<TelecomAnhuiCall>();
					WebParam<TelecomAnhuiCall> webParam = new WebParam<TelecomAnhuiCall>();
					if(null != page)
					{
						int code = page.getWebResponse().getStatusCode();
						if(code==200)
						{
							if(page.getWebResponse().getContentAsString().contains("fmList"))
							{
								JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
//								for (int i = 0; i < 2; i++) {
//									
//								}
								String string = fromObject.getString("flowUseList");
//								System.out.println(string);
								if(string.contains("pre"))
								{
									JSONObject fromObject4 = JSONObject.fromObject(string);
									String string3 = fromObject4.getString("fmList");
									if(string3.contains("null"))
									{
										webParam.setHtml(string3);
										return webParam;
									}
									else
									{
										JSONArray fromObject2 = JSONArray.fromObject(string3);
										for (Object object : fromObject2) {
											JSONObject fromObject3 = JSONObject.fromObject(object);
											telecomAnhuiCall = new TelecomAnhuiCall();
											telecomAnhuiCall.setStartTime(fromObject3.getString("qryEighthLine"));
											if(fromObject3.getString("qryThirdLine").equals("主叫"))
											{
												telecomAnhuiCall.setHisNum(fromObject3.getString("qrySixthLine"));
											}
											else if(fromObject3.getString("qryThirdLine").equals("被叫"))
											{
												telecomAnhuiCall.setHisNum(fromObject3.getString("qryFifthLine"));
											}
											telecomAnhuiCall.setCallType(fromObject3.getString("qryThirdLine"));
											telecomAnhuiCall.setCallTime(fromObject3.getString("qryNinthLine"));
											telecomAnhuiCall.setMoney(fromObject3.getString("qryTenthLine"));
											telecomAnhuiCall.setCallPlace(fromObject3.getString("qrySeventhLine"));
											telecomAnhuiCall.setTalkType(fromObject3.getString("qryEleventhLine"));
											telecomAnhuiCall.setTaskid(messageLogin.getTask_id());
											list.add(telecomAnhuiCall);
										}
									}
								}
								else if(string.contains("pay"))
								{
									JSONObject fromObject4 = JSONObject.fromObject(string);
									String string3 = fromObject4.getString("fmList");
									if(string3.contains("null"))
									{
										webParam.setHtml(string3);
										return webParam;
									}
									else
									{
										JSONArray fromObject2 = JSONArray.fromObject(string3);
										for (Object object : fromObject2) {
											JSONObject fromObject3 = JSONObject.fromObject(object);
											telecomAnhuiCall = new TelecomAnhuiCall();
											telecomAnhuiCall.setStartTime(fromObject3.getString("qryEighthLine"));
											telecomAnhuiCall.setHisNum(fromObject3.getString("qryFifthLine"));
											telecomAnhuiCall.setCallType(fromObject3.getString("qrySecondLine"));
											telecomAnhuiCall.setCallTime(fromObject3.getString("qryTenthLine"));
											telecomAnhuiCall.setMoney(fromObject3.getString("qryEleventhLine"));
											
											telecomAnhuiCall.setTalkType(fromObject3.getString("qryThirdLine"));
											telecomAnhuiCall.setCallPlace(fromObject3.getString("qryForthLine"));
											telecomAnhuiCall.setHisPlace(fromObject3.getString("qrySixthLine"));
											telecomAnhuiCall.setTaskid(messageLogin.getTask_id());
											list.add(telecomAnhuiCall);
										}
									}
								}
								webParam.setCode(code);
								webParam.setHtml(page.getWebResponse().getContentAsString());
								webParam.setList(list);
								webParam.setUrl(url);
								return webParam;
							}
							
						}
					}
				}
			}
		return null;
	}
	
	/**
	 * 2017-10-11 yl 发送验证码
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception 
	 */
	public WebParam getphonecodeTwo(MessageLogin messageLogin, TaskMobile taskMobile) throws  Exception {
		String url ="http://wapah.189.cn/ua/sendSms.shtml?smsPhone="+messageLogin.getName();
		WebClient webClient = addcookie(taskMobile); 
		Page page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(null != page)
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	
	/**
	 * 2017-10-12 yl 短信详单
	 * @param messageLogin
	 * @param taskMobile
	 * @param j
	 * @return
	 * @throws Exception 
	 */
	public WebParam<TelecomAnhuiMessage> getSMS(MessageLogin messageLogin, TaskMobile taskMobile, int j) throws Exception {
//		    String u   = "http://wapah.189.cn/detailSingleQuery.shtml?startDate="+getFirstDay("yyyy-MM-dd",j)+"&endDate="+getLastDay("yyyy-MM-dd",j)+"&serviceNbr="+messageLogin.getName()+"&type=1&sendcheck="+messageLogin.getSms_code();         
		    String u="http://wapah.189.cn/detailQueryAfter.shtml?startDate="+getFirstDay("yyyy-MM-dd",j)+"&endDate="+getLastDay("yyyy-MM-dd",j)+"&serviceNbr="+messageLogin.getName()+"&type=1&sendcheck="+messageLogin.getSms_code()+"&pageNum=10";
			WebClient webClient = addcookieNet(taskMobile); 
			Page page2 = webClient.getPage(u);
			Thread.sleep(1000);
//			System.out.println(page2.getWebResponse().getContentAsString());
			if(page2.getWebResponse().getContentAsString().contains("fmList"))
			{
				JSONObject fromObjec = JSONObject.fromObject(page2.getWebResponse().getContentAsString());
				String strin = fromObjec.getString("flowUseList");
//				System.out.println(strin);
				if(strin.contains("pay"))
				{
					JSONObject fromObject22 = JSONObject.fromObject(strin);
					String string2 = fromObject22.getString("totalLines");
//					System.out.println(string2);//找到本月的总数
					String url = "http://wapah.189.cn/detailQueryAfter.shtml?startDate="+getFirstDay("yyyy-MM-dd",j)+"&endDate="+getLastDay("yyyy-MM-dd",j)+"&serviceNbr="+messageLogin.getName()+"&type=1&sendcheck="+messageLogin.getSms_code()+"&pageNum="+string2;
//					Page page = telecomAnhuiRetry.getMessageRetry(taskMobile, messageLogin, url, webClient);
					Page page = webClient.getPage(url);
					Thread.sleep(2000);
//					System.out.println(url+page.getWebResponse().getContentAsString());
					TelecomAnhuiMessage telecomAnhuiMessage = null;
					List<TelecomAnhuiMessage> list = new ArrayList<TelecomAnhuiMessage>();
					WebParam<TelecomAnhuiMessage> webParam = new WebParam<TelecomAnhuiMessage>();
					if(null != page)
					{
						webParam.setHtml(page.getWebResponse().getContentAsString());
						int code = page.getWebResponse().getStatusCode();
						if(code==200)
						{
							webParam.setCode(code);
							if(page.getWebResponse().getContentAsString().contains("fmList"))
							{
								JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
								String string = fromObject.getString("flowUseList");
//								System.out.println(string);
								if(string.contains("pay"))
								{
									JSONObject fromObject4 = JSONObject.fromObject(string);
									String string3 = fromObject4.getString("fmList");
									if(string3.equals("null"))
									{
										webParam.setHtml(string3);
										return webParam;
									}
									else
									{
										JSONArray fromObject2 = JSONArray.fromObject(string3);
										for (Object object : fromObject2) {
											JSONObject fromObject3 = JSONObject.fromObject(object);
											telecomAnhuiMessage = new TelecomAnhuiMessage();
											telecomAnhuiMessage.setSmsType(fromObject3.getString("qrySecondLine"));
											telecomAnhuiMessage.setGetType(fromObject3.getString("qryThirdLine"));
											telecomAnhuiMessage.setHisNum(fromObject3.getString("qryFifthLine"));
											telecomAnhuiMessage.setSendTime(fromObject3.getString("qrySixthLine"));
											telecomAnhuiMessage.setMoney(fromObject3.getString("qrySeventhLine"));
											telecomAnhuiMessage.setTaskid(messageLogin.getTask_id());
											list.add(telecomAnhuiMessage);
										}
									}
									
								}
								else if(string.contains("pre"))
								{
									JSONObject fromObject4 = JSONObject.fromObject(string);
									String string3 = fromObject4.getString("fmList");
									if(string3.equals("null"))
									{
										webParam.setHtml(string3);
										return webParam;
									}
									else
									{
										JSONArray fromObject2 = JSONArray.fromObject(string3);
										for (Object object : fromObject2) {
											JSONObject fromObject3 = JSONObject.fromObject(object);
											telecomAnhuiMessage = new TelecomAnhuiMessage();
											telecomAnhuiMessage.setSmsType(fromObject3.getString("qryThirdLine"));
											//telecomAnhuiMessage.setGetType(fromObject3.getString("qrySecondLine"));
											telecomAnhuiMessage.setHisNum(fromObject3.getString("qryFifthLine"));
											
											telecomAnhuiMessage.setSendTime(fromObject3.getString("qrySixthLine"));
											
											telecomAnhuiMessage.setMoney(fromObject3.getString("qrySeventhLine"));
											telecomAnhuiMessage.setTaskid(messageLogin.getTask_id());
											list.add(telecomAnhuiMessage);
										}
									}
									
								}
								webParam.setList(list);
								webParam.setUrl(url);
								return webParam;
							}
							else
							{
								webParam.setHtml("验证失败网络有误，请重新尝试");
								return webParam;
							}
							
						}
				}
				}
			}
			
		return null;
	}

	
	/**
	 * @throws Exception 
	 * wap 登陆
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws  
	 */
	public WebParam login(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		//登陆界面
		String url = "http://wapah.189.cn/ua/toLogin.shtml";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);
		
		
		//加密的动态val
		//HtmlElement module = page.getFirstByXPath("//input[@id='module']");
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element elementById = doc.getElementById("module");
		String nodeValue = elementById.val();
		
//		HtmlTextInput name = page.getFirstByXPath("//input[@id='userphone']");
//		name.reset();
//		name.setText(messageLogin.getName());
//		HtmlPasswordInput pwd = page.getFirstByXPath("//input[@type='password']");
//		pwd.reset();
//		pwd.setText(messageLogin.getPassword());
//		
		HtmlImage image = page.getFirstByXPath("//img[@id='gcode']");
		String code = chaoJiYingOcrService.getVerifycode(image, "1007");
//		
//		HtmlTextInput random = page.getFirstByXPath("//input[@id='randomCode']");
//		random.setText(code);
//		
//		HtmlElement button = page.getFirstByXPath("//input[@class='btn_b_orange']");
//		HtmlPage page2 = button.click();
		
		
		//加密参数
		String userphone = encryptedPhoneTwo(messageLogin.getName(),nodeValue);
		String khmm =encryptedPhoneTwo(messageLogin.getPassword(),nodeValue);
		String data ="{\"userphone\":\""+userphone+"\",\"check_userphone\":\""+messageLogin.getName()+"\",\"logintype\":\"1\",\"khmm\":\""+khmm+"\",\"sjmm\":\"\",\"randomCode\":\""+code+"\"}";
        String encode = URLEncoder.encode(data, "GBK");
		//String url1 ="http://wapah.189.cn/ua/login.shtml?tt=Thu%20Oct%2012%202017%2010:05:46%20GMT+0800%20(%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)?formData="+data;
		
		//时间参数
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT+0800' ", Locale.US); 
		String string = sdf.format(date);
		String string2 = string.replaceAll(" ", "%20");
//		System.out.println(string2);
		
		                                                                                                       //中国标准时间↓
		WebRequest requestSettings = new WebRequest(new URL("http://wapah.189.cn/ua/login.shtml?tt="+string2+"(%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)"), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("Origin", "http://wapah.189.cn");
		requestSettings.setAdditionalHeader("Host", "wapah.189.cn");
		requestSettings.setAdditionalHeader("Referer","http://wapah.189.cn/ua/toLogin.shtml");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		requestSettings.setRequestBody("formData="+encode);
		
		Page page2 = webClient.getPage(requestSettings);
		
		Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
		Elements byClass = doc1.getElementsByClass("layermcont");
//		System.out.println(byClass);
		
		WebParam  webParam = new WebParam();
		if(null != page2)
		{
			if(page2.getWebResponse().getContentAsString().contains("登录成功"))
			{
//				System.out.println(page2.getWebResponse().getContentAsString());
				webParam.setHtml(page2.getWebResponse().getContentAsString());
				webParam.setWebClient(webClient);
			}
			else
			{
//				System.out.println(page2.getWebResponse().getContentAsString());
				webParam.setHtml(page2.getWebResponse().getContentAsString());
			}
		}
		return webParam;
	}

	
	//net端第3次获取验证码  用于爬取详单的
	public WebParam getphonecodeThird(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		String mobilNbr = messageLogin.getName();
		String _key = mobilNbr.substring(1, 2) + mobilNbr.substring(3, 4) + mobilNbr.substring(6, 7)+ mobilNbr.substring(8, 10);
		String params = "mobileNum=" + mobilNbr + "&key=" + _key;
		String str = encryptedPhone(params);
		String url = "http://ah.189.cn/service/bill/sendValidReq.parser?_v=" + str;
		WebClient webClient = addcookie(taskMobile);  
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings.setAdditionalHeader("Referer", "http://ah.189.cn/service/bill/fee.parser?type=phoneAndInternetDetail");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(requestSettings);
		//System.out.println("------"+page.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(null != page)
		{
			int code = page.getWebResponse().getStatusCode();
			if(code ==200)
			{
				webParam.setCode(code);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setUrl(url);
				webParam.setWebClient(webClient);
				return webParam;
			}
		}
		return null;
	}

	//个人信息
	public WebParam<TelecomAnhuiUserInfo> getWapUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		String url="http://wapah.189.cn/custCenter/toBasicIfm.shtml";
		WebClient webClient = addcookie(taskMobile);
		HtmlPage page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiUserInfo> webParam = new WebParam<TelecomAnhuiUserInfo>();
		
        Elements elementsByClass = Jsoup.parse(page.getWebResponse().getContentAsString()).getElementsByClass("resultc").get(0).getElementsByTag("span");
//		System.out.println(elementsByClass.get(2).text());
		if (null != page) {
			int code = page.getWebResponse().getStatusCode();
			if (code == 200) {
				TelecomAnhuiUserInfo telecomAnhuiUserInfo = new TelecomAnhuiUserInfo();
//				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
//				Elements elements = doc.select(".clearfix");
//				telecomAnhuiUserInfo.setName(elements.get(0).text());
//				telecomAnhuiUserInfo.setCity(elements.get(1).text());
//				telecomAnhuiUserInfo.setNetTime(elements.get(2).text());
//				telecomAnhuiUserInfo.setBirthday(elements.get(3).text());
//				telecomAnhuiUserInfo.setHobby(elements.get(4).text());
//				telecomAnhuiUserInfo.setEducation(elements.get(5).text());
//				telecomAnhuiUserInfo.setJob(elements.get(6).text());
//				telecomAnhuiUserInfo.setSex(elements.get(7).text());
//				telecomAnhuiUserInfo.setPeople(elements.get(8).text());
//				telecomAnhuiUserInfo.setPhone(elements.get(9).text());
//				telecomAnhuiUserInfo.setPhone(elements.get(10).text());
//				telecomAnhuiUserInfo.setEmail(elements.get(11).text());
				
				telecomAnhuiUserInfo.setName(elementsByClass.get(0).text());
				telecomAnhuiUserInfo.setCardType(elementsByClass.get(1).text());
				telecomAnhuiUserInfo.setPhone(elementsByClass.get(2).text());
				telecomAnhuiUserInfo.setTaskid(messageLogin.getTask_id());
				webParam.setCode(code);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setPage(page);
				webParam.setUrl(url);
				webParam.setTelecomAnhuiUserInfo(telecomAnhuiUserInfo);
				return webParam;
			}
		}
		return null;
	}

	
	//缴费
	public WebParam<TelecomAnhuiPay> getWapPay(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		String url="http://wapah.189.cn/bill/detailAjax.shtml?month="+getDateBefore("yyyyMM",i);
		WebClient webClient = addcookie(taskMobile);
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiPay> webParam = new WebParam<TelecomAnhuiPay>();
		
		
		JSONObject jsonObj = JSONObject.fromObject(page.getWebResponse().getContentAsString()); 
		JSONObject ResultMsg = JSONObject.fromObject(jsonObj.getString("ResultMsg")); 
		if(page.getWebResponse().getContentAsString().contains("payLog"))
		{
			String payLog = ResultMsg.getString("payLog"); 
			Object obj = new JSONTokener(payLog).nextValue();
			TelecomAnhuiPay t = null;
			List<TelecomAnhuiPay> list = new ArrayList<TelecomAnhuiPay>();
			if (obj instanceof JSONObject) 
			{ 
				t = new TelecomAnhuiPay();
				JSONObject jsonObject = (JSONObject) obj; 
				String payDate = jsonObject.getString("payDate"); 
				String codeValue = jsonObject.getString("codeValue"); 
				String operationTypeName = jsonObject.getString("operationTypeName"); 
				String balanceChange = jsonObject.getString("balanceChange");
				
				t.setPayTime(payDate);
				t.setOutMoney(codeValue);
				t.setTypePay(operationTypeName);
				t.setBanlance(balanceChange);
				t.setTaskid(taskMobile.getTaskid());
				System.out.println("payDate-----" + payDate); 
				System.out.println("codeValue-----" + codeValue); 
				System.out.println("operationTypeName-----" + operationTypeName); 
				System.out.println("balanceChange-----" + balanceChange); 
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setTelecomAnhuiPay(t);
				webParam.setUrl(url);
				return webParam;
			} 
			else if (obj instanceof JSONArray) 
			{ 
			    JSONArray jsonArray = (JSONArray) obj; 
				for (Object object : jsonArray)
				{ 
					t = new TelecomAnhuiPay();
					JSONObject jsonObject = JSONObject.fromObject(object); 
					String payDate = jsonObject.getString("payDate"); 
					String codeValue = jsonObject.getString("codeValue"); 
					String operationTypeName = jsonObject.getString("operationTypeName"); 
					String balanceChange = jsonObject.getString("balanceChange");
			
					System.out.println("payDate-----" + payDate); 
					System.out.println("codeValue-----" + codeValue); 
					System.out.println("operationTypeName-----" + operationTypeName); 
					System.out.println("balanceChange-----" + balanceChange); 
					
					t.setPayTime(payDate);
					t.setOutMoney(codeValue);
					t.setTypePay(operationTypeName);
					t.setBanlance(balanceChange);
					t.setTaskid(taskMobile.getTaskid());
					list.add(t);
				} 
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
				return webParam;
			}
		}
		else if(ResultMsg.getString("resDesc").contains("OK"))
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
		return null;
	}

	
	//账单
	public WebParam<TelecomAnhuiBill> getWapBill(MessageLogin messageLogin, TaskMobile taskMobile, int time) throws Exception {
		String url="http://wapah.189.cn/bill/easyAjax.shtml?month="+getDateBefore("yyyyMM", time);
		
		WebClient webClient = addcookie(taskMobile);
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiBill> webParam = new WebParam<TelecomAnhuiBill>();
		
		JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("SOO"))
		{
			System.out.println(fromObject);//resultMessage
			String string = fromObject.getString("resultMessage");
			System.out.println(string);//ContractRoot
			JSONObject fromObject2 = JSONObject.fromObject(string);
			System.out.println(fromObject2);
			String string2 = fromObject2.getString("ContractRoot");
			System.out.println(string2);
			JSONObject fromObject3 = JSONObject.fromObject(string2);
			String string3 = fromObject3.getString("SvcCont");
			System.out.println(string3);
			JSONObject fromObject4 = JSONObject.fromObject(string3);
			String string4 = fromObject4.getString("SOO");
			System.out.println(string4);
			JSONArray fromObject5 = JSONArray.fromObject(string4);
		    Object object = fromObject5.get(1);
			System.out.println(object);
			JSONObject fromObject6 = JSONObject.fromObject(object);
			String string5 = fromObject6.getString("INVOICE_ITEM_INFO");
			
			System.out.println(string5);
			if(string5.startsWith("["))
			{
				JSONArray fromObject7 = JSONArray.fromObject(string5);
				TelecomAnhuiBill t = null;
				List<TelecomAnhuiBill> list = new ArrayList<TelecomAnhuiBill>();
				for (int i = 0; i < fromObject7.size(); i++) {
					
					t = new TelecomAnhuiBill(); 
					JSONObject fromObject8 = JSONObject.fromObject(fromObject7.get(i));
					String INVOICE_NAME = fromObject8.getString("INVOICE_NAME");
					String CHARGE_S = fromObject8.getString("CHARGE_S");
					String FORMAT_ITEM_STR = fromObject8.getString("FORMAT_ITEM_STR");
					t.setBillName(INVOICE_NAME);
					t.setMoney(CHARGE_S);
					t.setDesce(FORMAT_ITEM_STR);
					t.setMonth(getDateBefore("yyyyMM", time));
					t.setTaskid(taskMobile.getTaskid());
					list.add(t);
				}
				System.out.println(list);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
				return webParam;
			}
			else
			{
				TelecomAnhuiBill t =new TelecomAnhuiBill();
				JSONObject fromObject8 = JSONObject.fromObject(string5);
				String INVOICE_NAME = fromObject8.getString("INVOICE_NAME");
				String CHARGE_S = fromObject8.getString("CHARGE_S");
				String FORMAT_ITEM_STR = fromObject8.getString("FORMAT_ITEM_STR");
				t.setBillName(INVOICE_NAME);
				t.setMoney(CHARGE_S);
				t.setDesce(FORMAT_ITEM_STR);
				t.setMonth(getDateBefore("yyyyMM", time));
				t.setTaskid(taskMobile.getTaskid());
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setUrl(url);
				webParam.setTelecomAnhuiBill(t);
				return webParam;
			}
			
		}
		return null;
	}

	//积分
	public WebParam<TelecomAnhuiScore> getWapScore(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		//时间参数
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT+0800' ", Locale.US); 
		String string = sdf.format(date);
		String decode = URLDecoder.decode(string, "UTF-8");
		string = java.net.URLEncoder.encode(string,"UTF-8");
		
		String url="http://wapah.189.cn/jfQueryAuth/jfRecord.shtml?month="+getDateBefore("yyyyMM", i);
//		String url1="http://wapah.189.cn/jfQueryAuth/jfRecord.shtml?tt="+string+"(%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)&formData=%7B%22month%22%3A%22"+getDateBefore("yyyyMM", i)+"%22%7D";
		String url1="http://wapah.189.cn/jfQueryAuth/jfRecord.shtml?tt="+string+"(%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)&formData=%7B%22month%22%3A%22"+getDateBefore("yyyyMM", i)+"%22%7D";

		System.out.println(url1);
		String url2="http://wapah.189.cn/jfQueryAuth/jfRecordIndex.shtml";
		WebClient webClient = addcookie(taskMobile);
		
		
		WebRequest  requestSettings = new WebRequest(new URL(url1), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Host", "wapah.189.cn");
//		requestSettings.setAdditionalHeader("Cookie", "svid=D3CFD2C0CE4A263D; lvid=5d1fdd6fdc8b1aa87249bcc59812aa62; nvid=1; Hm_lvt_333c7327dca1d300fd7235c159b7da04=1515139421; userId=201%7C20160000000045218814; _gscu_1758414200=15139094d0g74j49; loginStatus=non-logined; trkId=5C466F91-713B-49C4-BF15-55D24526F025; cityCode=ah; SHOPID_COOKIEID=10013; index_tip=true; highRemind=true; JSESSIONID_WAP=yNb-2KgScHWJzutQOM5HCOp_cUPl5fddeueKECLCVOLQgsItvKcc!2014822496; Hm_lvt_65559b0ac8f481b555de15854fe31fad=1515139600,1516064260; Hm_lpvt_65559b0ac8f481b555de15854fe31fad=1516105161; s_cc=true; s_fid=48622E5D4A6F15EA-041889EC1F04B815; Hm_lvt_df6de7f7ba112632e78abd781408bca4=1515139579,1516064180; Hm_lpvt_df6de7f7ba112632e78abd781408bca4=1516105162");
		requestSettings.setAdditionalHeader("Origin", "http://www.zmdgjj.com");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		requestSettings.setAdditionalHeader("Referer", "http://wapah.189.cn/jfQueryAuth/jfRecordIndex.shtml");
		Page page = webClient.getPage(requestSettings);
		
//		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//		for (Cookie cookie : cookies) {
//			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
//		}
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiScore> webParam = new WebParam<TelecomAnhuiScore>();
		if(page.getWebResponse().getContentAsString().contains("客户未评上星级,禁查禁兑"))
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
		TelecomAnhuiScore t = new TelecomAnhuiScore();
		if(page.getWebResponse().getContentAsString().contains("成功"))
		{
			JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
			String string1 = fromObject.getString("svcContArray");
//			System.out.println(string1);
			JSONArray fromObject2 = JSONArray.fromObject(string1);
//			System.out.println(fromObject2);
			if(fromObject2.toString().contains("PointTypeName"))
			{
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(0));
				String month = fromObject3.getString("Month");
				String PointTypeName = fromObject3.getString("PointTypeName");
				String PointValue = fromObject3.getString("PointValue");
				t.setMonth(month);
				t.setStatus(PointTypeName);
				t.setNewScore(PointValue);
				t.setTaskid(taskMobile.getTaskid());
//				System.out.println(t);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setUrl(url1);
				webParam.setTelecomAnhuiScore(t);
				return webParam;
			}
			
		}
		return null;
	}

	public WebParam<TelecomAnhuiBusiness> getWapBusiness(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		String url="http://wapah.189.cn/busQueryAuth/busPackageDetail.shtml?type=kxb";
		WebClient webClient = addcookie(taskMobile);
		Page page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiBusiness> webParam = new WebParam<TelecomAnhuiBusiness>();
		
		JSONObject jsonObj = JSONObject.fromObject(page.getWebResponse().getContentAsString());
		String payLog = jsonObj.getString("resultMessage");
		Object obj = new JSONTokener(payLog).nextValue();
		TelecomAnhuiBusiness t = null;
		List<TelecomAnhuiBusiness> list = new ArrayList<TelecomAnhuiBusiness>();
		if (obj instanceof JSONObject) 
		{
			t = new TelecomAnhuiBusiness();
			JSONObject jsonObject = (JSONObject) obj;
			String INNER_GROUP_NAME = jsonObject.getString("INNER_GROUP_NAME");
			String EFF_DATE = jsonObject.getString("EFF_DATE");//生效
			String PROTOCOL_EFF_DATE = jsonObject.getString("PROTOCOL_EFF_DATE");//办理
			String EXP_DATE = jsonObject.getString("EXP_DATE");//失效
			String PRICING_DESC = jsonObject.getString("PRICING_DESC");//描述
			String STATUS_NAME = jsonObject.getString("STATUS_NAME");//状态
			
			t.setDescr(PRICING_DESC);
			t.setDoTime(PROTOCOL_EFF_DATE);
			t.setEndTime(EXP_DATE);
			t.setName(INNER_GROUP_NAME);
			t.setStartTime(EFF_DATE);
			t.setStatus(STATUS_NAME);
			t.setTaskid(taskMobile.getTaskid());
			
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setTelecomAnhuiBusiness(t);
			return webParam;
		} 
		else if (obj instanceof JSONArray) 
		{
			JSONArray jsonArray = (JSONArray) obj;
			
			for (Object object : jsonArray) {
				t = new TelecomAnhuiBusiness();
				JSONObject jsonObject = JSONObject.fromObject(object);
				String INNER_GROUP_NAME = jsonObject.getString("INNER_GROUP_NAME");
				String EFF_DATE = jsonObject.getString("EFF_DATE");//生效
				String PROTOCOL_EFF_DATE = jsonObject.getString("PROTOCOL_EFF_DATE");//办理
				String EXP_DATE = jsonObject.getString("EXP_DATE");//失效
				String PRICING_DESC = jsonObject.getString("PRICING_DESC");//描述
				String STATUS_NAME = jsonObject.getString("STATUS_NAME");//状态
				
				t.setDescr(PRICING_DESC);
				t.setDoTime(PROTOCOL_EFF_DATE);
				t.setEndTime(EXP_DATE);
				t.setName(INNER_GROUP_NAME);
				t.setStartTime(EFF_DATE);
				t.setStatus(STATUS_NAME);
				t.setTaskid(taskMobile.getTaskid());
				list.add(t);
			}
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	public WebParam<TelecomAnhuiCall> getDetailCall(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		String url="http://wapah.189.cn/detailSingleQuery.shtml?startDate="+getFirstDay("yyyy-MM-dd",1)+"&endDate="+getLastDay("yyyy-MM-dd",1)+"&serviceNbr="+messageLogin.getName()+"&type=2&sendcheck="+messageLogin.getSms_code();
		WebClient webClient = addcookieNet(taskMobile); 
//		Page page2 = webClient.getPage(url1);
		Page page = webClient.getPage(url);
		Thread.sleep(2000);
//		System.out.println(url+page.getWebResponse().getContentAsString());
		WebParam<TelecomAnhuiCall> webParam = new WebParam<TelecomAnhuiCall>();
		if(null != page)
		{
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setUrl(url);
		}
		return webParam;

	}

}
