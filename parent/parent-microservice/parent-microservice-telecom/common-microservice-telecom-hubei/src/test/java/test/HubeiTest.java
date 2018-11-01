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

public class HubeiTest {

	public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		URL url  = new URL("http://waphb.189.cn/billQuery/custBillDetail.htm");

		WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("queryType", "14"));
		requestSettings.getRequestParameters().add(new NameValuePair("billCycle", "201808"));
		requestSettings.getRequestParameters().add(new NameValuePair("servId", "19262371823"));
		requestSettings.getRequestParameters().add(new NameValuePair("latnId", "1001"));  
		requestSettings.getRequestParameters().add(new NameValuePair("paymentMode", "1"));  
		 
		webClient.getCookieManager().addCookie(new Cookie(".waphb.189.cn","Hm_lvt_26a8253f10ab23013839977df4d98aee","1535097789"));
		webClient.getCookieManager().addCookie(new Cookie(".waphb.189.cn","Hm_lpvt_26a8253f10ab23013839977df4d98aee","1535097789"));
		webClient.getCookieManager().addCookie(new Cookie("waphb.189.cn","J_SESSIONID_MMALL","_2Vq9VKtEK1n8BhaAX9ArEtUQCiapCAnKBJR7PKAhQT8FhK4OIQTh7bnZuC6YQdKoNMrHEY1VegkEzp3xFRSxLDUAa2nIAU_FqEuGEaX1dOSLM3hcLzYieSP7zXhov1s!1964876995"));
		webClient.getCookieManager().addCookie(new Cookie("waphb.189.cn","tipflag","1"));
		webClient.getCookieManager().addCookie(new Cookie("waphb.189.cn","usert","hGP1Q0JQHWCuRCm45RbTBkElxi/9jsq5RMS9yy2yJg1RyS5C2XimP2Y3CyY8ajIdLpND+OXcIFRu0RcsM9nQsWszFEn2LXzCgCXvzmx1oTZ2nbi2YCZkJkCvUtMhNQAcQTfzMguNQcE="));
		webClient.getCookieManager().addCookie(new Cookie(".hm.baidu.com","HMACCOUNT","88D423509D47516F"));
		 
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 	
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9"); 
		requestSettings.setAdditionalHeader("Connection", "keep-alive"); 
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
		requestSettings.setAdditionalHeader("Host", "waphb.189.cn");
		requestSettings.setAdditionalHeader("Origin", "http://waphb.189.cn");
		requestSettings.setAdditionalHeader("Referer", "http://waphb.189.cn/service/custBillDetail.jsp");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");				
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		  
		Page page = webClient.getPage(requestSettings); 		
		System.out.println(page.getWebResponse().getContentAsString());
		
	
	}

}
