package app.service.common;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

public class TelecomCommonUnit {

	private static Gson gson = new Gson();
	
	public static String setcookieTojson(Set<Cookie> setcookie){
		return gson.toJson(setcookie);
	}
	
	public static Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			System.out.println("222222222222222222222222222222222222");
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setTimeout(30000);
			// webClient.get
			// webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {
		System.out.println("11111111111111111111111111111111111111111111");
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setTimeout(30000);
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		System.out.println("111111111111111111111111111111111111112");
		return searchPage;

	}
	public static WebClient addcookie(WebClient webClient, TaskMobile taskMobile){
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());;
		}
		
		return webClient;
	}
	
	public static Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest,String url){
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
