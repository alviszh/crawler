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

public class CQTest {

	public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		URL url  = new URL("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");

		WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("code", "015"));
		requestSettings.getRequestParameters().add(new NameValuePair("year", "2015"));
		requestSettings.getRequestParameters().add(new NameValuePair("currentPage", "2"));
		requestSettings.getRequestParameters().add(new NameValuePair("goPage", "2"));  
		 
		webClient.getCookieManager().addCookie(new Cookie("ggfw.cqhrss.gov.cn","Hm_lvt_c9e4dff6b2fa46bedb23549177e34ee9","1500368460"));
		webClient.getCookieManager().addCookie(new Cookie("ggfw.cqhrss.gov.cn","JSESSIONID","HSoC6cwyZrHSlTDF95sD_pP32LcuYzEhpg6TOFAxZwtLSEdlt_Cw!1786026659"));
		webClient.getCookieManager().addCookie(new Cookie("ggfw.cqhrss.gov.cn","SERVERID","s1"));
		webClient.getCookieManager().addCookie(new Cookie("ggfw.cqhrss.gov.cn","_gscu_322517208","00368459trissc14"));
		webClient.getCookieManager().addCookie(new Cookie("ggfw.cqhrss.gov.cn","cqtoken","cb55448708452d0495436c3fc59c2d62"));
		webClient.getCookieManager().addCookie(new Cookie("ggfw.cqhrss.gov.cn","random","94294771"));
		 
		requestSettings.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.cqhrss.gov.cn");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=015");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		  
		Page page = webClient.getPage(requestSettings); 		
		System.out.println(page.getWebResponse().getContentAsString());
		
	
	}

}
