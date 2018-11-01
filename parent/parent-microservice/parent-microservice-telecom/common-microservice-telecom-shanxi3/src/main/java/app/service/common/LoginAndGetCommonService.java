package app.service.common;

import java.lang.reflect.Type;
import java.net.URL;
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
			tracer.addTag("telecomShanXi3 ", e.getMessage());
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
			tracer.addTag("telecomShanXi3", e.getMessage());
			return null;
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
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomShanXi3", e.getMessage());
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
	//存储中间请求的cookie，将其更新到taskmobile表中
/*	public void getInitMy189homeWebClient(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
		webClient.getOptions().setTimeout(60000);
		//根据如下链接获取爬取各种信息需要用到的cookie（首先根据登录的cookie获取让中间链接成功获取）
		String url="http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000196&cityCode=sn";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		if(200==statusCode){
			//中间请求通过，获取其cookie
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskMobile.setCookies(cookies);
			taskMobileRepository.save(taskMobile);   //将中间请求的cookie更新到taskmobile表中
			tracer.addTag("action.crawler.getInitMy189homeWebClient.cookies",cookies+"======中间请求的cookie已经更新到表中");
		}
	}*/
	
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
}
