package app.crawler.domain;

import java.util.HashSet;
import java.util.Set;

import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.module.htmlunit.WebCrawler;


public class Cookies {
	private Page htmlpage;
	private WebClient webClient;
	

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public Page getHtmlpage() {
		return htmlpage;
	}

	public void setHtmlpage(Page htmlpage) {
		this.htmlpage = htmlpage;
	}

	/**
	 * @Des 将json转为Set<Cookie>
	 * @param json
	 * @return
	 */
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
	
	public static WebClient getWebClient(Set<Cookie> cookies) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for(Cookie cookie:cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		return webClient;
	}
	/**
	 * @Des 将cookie转为string
	 * @param webClient
	 * @return
	 */
	public static String transcookieToJson(WebClient webClient){
		Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 

		Set<CookieJson> cookiesSet= new HashSet<>();
		
		for(Cookie cookie:cookies){ 
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson); 
		}
		
		String cookieJson = new Gson().toJson(cookiesSet);
		return cookieJson;
	}
}
