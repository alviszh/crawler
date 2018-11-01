package app.service.common;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
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
	
	public static Page getHtml(String url, WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		return page;
	}
	
	public static Page gethtmlPost(WebClient webClient,List<NameValuePair> paramsList,String url){
		try{
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);
		webRequest.setCharset(Charset.forName("gbk"));
		if(paramsList != null){
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		
		if(searchPage == null){
			return null;
		}
		return searchPage;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
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
