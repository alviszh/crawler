package com.microservice.dao.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author zz
 *
 */

@MappedSuperclass
public class IdEntityAndCookie extends IdEntity{
	
	protected String cookies;

	@Column(columnDefinition="text")
	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	
	public WebClient getClient(String cookieStr){
		if(StringUtils.isBlank(cookieStr)){
			throw new RuntimeException("cookie is null !");
		}else{
			Set<Cookie> cookies = transferJsonToSet(cookieStr);
			WebClient webClient = null;
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.setRefreshHandler(new ThreadedRefreshHandler());
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(50000); // 5s
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setUseInsecureSSL(true); //
			
			for(Cookie cookie:cookies){
				webClient.getCookieManager().addCookie(cookie);
			}
			return webClient;
		}
		
	}
	
	/**
	 * @Des 将json转为Set<Cookie>
	 * @param json
	 * @return
	 */
	public Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

	}

	

}
