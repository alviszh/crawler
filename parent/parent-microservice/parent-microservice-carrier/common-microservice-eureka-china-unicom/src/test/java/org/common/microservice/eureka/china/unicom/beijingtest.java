package org.common.microservice.eureka.china.unicom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
public class beijingtest {

	public static void main(String[] args) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(20000); // 5s
		try{
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		}catch(Exception e){
			System.out.println("setAjaxController exception:"+e.toString());
		}
		
		webClient.getOptions().setUseInsecureSSL(true); //
		webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理//		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
		String url = "http://grwsyw.bjgjj.gov.cn/ish/";
		System.setProperty("https.protocols", "TLS 1.2,SSLv3");
		try {
			
			webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
			webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
			webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/");
			webClient.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
			webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webClient.addRequestHeader("Cache-Control", "max-age=0");
			webClient.addRequestHeader("Connection", "keep-alive");
			webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

			
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("mm", "096571".trim()));
			paramsList.add(new NameValuePair("logintype", "card"));			
			paramsList.add(new NameValuePair("yzfs", "1"));
			paramsList.add(new NameValuePair("hm", "130984199210023312".trim()));
			paramsList.add(new NameValuePair("xyjmdzd", "xyjmdzd:mmMD5".trim()));
			Page page = gethtmlPost(webClient, paramsList, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());

			Set<Cookie> cookies = webClient.getCookieManager().getCookies();

			System.out.println(cookies);
//			Map<String, String> map = new HashMap<>();
//			for (Cookie cookie : cookies) {
//				System.out.println(cookie.toString());
//				/*if (cookie.getName().indexOf("gjjaccnum") != -1) {
//					map.put(cookie.getName(), cookie.getValue());
//				}
//				if (cookie.getName().indexOf("gjjcertinum") != -1) {
//					map.put(cookie.getName(), cookie.getValue());
//				}*/
//			}

			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
