package test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class Test {

	public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		URL url  = new URL("http://www.gxwzgjj.com/dc_zg_grye.asp");

		WebRequest  requestSettings = new WebRequest(url, HttpMethod.GET);
		
//		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//		requestSettings.getRequestParameters().add(new NameValuePair("code", "015"));
//		requestSettings.getRequestParameters().add(new NameValuePair("year", "2015"));
//		requestSettings.getRequestParameters().add(new NameValuePair("currentPage", "2"));
//		requestSettings.getRequestParameters().add(new NameValuePair("goPage", "2"));  
		 
		webClient.getCookieManager().addCookie(new Cookie("http://www.gxwzgjj.com","yunsuo_session_verify","1f4a1ed0ffad37ccaa7f829abdee8afc"));
		webClient.getCookieManager().addCookie(new Cookie("http://www.gxwzgjj.com","ASPSESSIONIDACCTRACR","JGBNJPCBCBKHIKGNFAMBBHAL"));
		webClient.getCookieManager().addCookie(new Cookie("http://www.gxwzgjj.com","td_cookie","18446744071092201705"));
		requestSettings.setAdditionalHeader("Host", "www.gxwzgjj.com");
		requestSettings.setAdditionalHeader("Origin", "http://www.gxwzgjj.com");
		requestSettings.setAdditionalHeader("Referer", "http://www.gxwzgjj.com/");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		  
		Page page = webClient.getPage(requestSettings); 
		
		System.out.println(page.getWebResponse().getContentAsString());
		
		

	

	}

}
