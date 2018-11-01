package app.service.common;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

@Component
public class LoginAndGetCommonService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	//================================================================
	public WebClient getWebClient(Set<Cookie> cookies) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for(Cookie cookie:cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		return webClient;
	}
	//================================================================
	public  Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomTianjin ", e.getMessage());
			return null;
		}
	}
	//================================================================
	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if(searchPage == null){
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//================================================================
	public  HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomTianjin", e.getMessage());
			return null;
		}
	}
	
	//================================================================
	@SuppressWarnings("unused")
	public  WebClient addcookie(WebClient webClient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
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
	//===================================================================================
	//存储中间请求的cookie，将其更新到taskmobile表中，每次用的时候取出，以此方式跳过验证码
	public void getInitMy189homeWebClient(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("parser.crawler.getInitMy189homeWebClient",taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		webClient.getOptions().setTimeout(20000);
		//根据如下链接获取爬取各种信息需要用到的cookie（首先根据登录的cookie获取让中间链接成功获取）
		String url="http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02241349&cityCode=tj";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		if(200==statusCode){
			//尝试添加通话记录爬取之前所用的cookie
			url="http://tj.189.cn/tj/service/bill/detailBillQuery2.action";
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			statusCode = page.getWebResponse().getStatusCode();
			if(200==statusCode){
				//中间请求通过，获取其cookie
				String cookies = CommonUnit.transcookieToJson(page.getWebClient());
				taskMobile.setCookies(cookies);
				taskMobileRepository.save(taskMobile);   //将中间请求的cookie更新到taskmobile表中
			}
		}
	}
	//================================================================
	/**
	 * 在爬取我的现状那些信息的时候，发现需要返回Page，而不是HtmlPage,如果调用上边的返回getHtml()方法，不会报错类型转换错误
	 * 会返回null，故封装如下方法
	 * @param url
	 * @param webClient
	 * @return
	 */
	public Page getPage(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomTianjin", e.getMessage());
			return null;
		}
	}
//	=============================================================================
	public Page getPage(WebClient webClient, TaskMobile taskMobile, String url, HttpMethod type) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	public HtmlPage getHtml(String url, WebClient webClient, TaskMobile taskMobile) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}
	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if (null == taskMobile) {
			return true;
		}
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}
	
	public void isCrawlerSuccess(TaskMobile taskMobile) throws Exception{
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("isCrawlerSuccess", taskMobile.getTaskid());
		
		if((taskMobile.getUserMsgStatus()==200 || taskMobile.getUserMsgStatus()==201)
				&& (200==taskMobile.getAccountMsgStatus()||  201==taskMobile.getAccountMsgStatus())
				&& (200==taskMobile.getPayMsgStatus() ||  201==taskMobile.getPayMsgStatus())
				&& (200==taskMobile.getCallRecordStatus() ||  201==taskMobile.getCallRecordStatus())
				&& (200==taskMobile.getSmsRecordStatus() ||  201==taskMobile.getSmsRecordStatus() )
				&& (200==taskMobile.getIntegralMsgStatus() ||  201==taskMobile.getIntegralMsgStatus())
				&& (200==taskMobile.getBusinessMsgStatus() ||  201==taskMobile.getBusinessMsgStatus()))
		{
			taskMobile.setDescription("数据采集成功！");
			taskMobile.setPhase("CRAWLER");
			taskMobile.setPhase_status("SUCCESS");
			taskMobile.setFinished(true);
			taskMobileRepository.save(taskMobile);
			tracer.addTag("changeCrawlerStatusSuccess success", taskMobile.getTaskid());
		}
	}
	
	
	//===================================================================================
	//在后期测试的测试过程中，发现之前可以采集到的语音和短信详单无法采集到，之前爬取所有的数据之前，
	//用的cookie是本类中的getInitMy189homeWebClient
	//方法返回的，后期发现对爬取语音和短息详单已经不起作用了,故用此方法，以查询通话和短信详单的前提页面的cookie为爬取所有数据的前提cookie
	//但是此方法貌似不管用，故将详情查询页的请求代码放在了循环中（貌似受到访问频率的影响）
	public void getDetailListWebClient(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("parser.crawler.getDetailListWebClient",taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		webClient.getOptions().setTimeout(20000);
		//根据如下链接获取爬取各种信息需要用到的cookie（首先根据登录的cookie获取让中间链接成功获取）
		String url="http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02241349&cityCode=tj";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		if(200==statusCode){
			url="http://tj.189.cn/tj/service/bill/detailBillQuery2.action";
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setCharset(Charset.forName("UTF-8"));
			page = webClient.getPage(webRequest);
			if(null!=page){
				//中间请求通过，获取其cookie
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskMobile.setCookies(cookies);
				taskMobileRepository.save(taskMobile);   //将中间请求的cookie更新到taskmobile表中
			}
		}
	}
	//===================================================================================

}
