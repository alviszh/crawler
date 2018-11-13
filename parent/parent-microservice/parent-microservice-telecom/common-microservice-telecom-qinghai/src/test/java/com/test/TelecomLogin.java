package com.test;

import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TelecomLogin {
	
	public static WebClient login(String phonenum,String password) throws Exception {

		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		String url2 = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url2, webClient2);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText(phonenum);
		passwordInput.setText(password);

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
			return null;
		} else {
			System.out.println("=======成功==============");
		}

		url2 = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage html3 = (HtmlPage) getHtml2(url2, webClient2);
		
 		System.out.println(html3.asXml());
 		
 		/*url2 = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_new.jsp";
 		
 		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("PROD_NO", "4217"));
		Page page = TelecomLogin.gethtmlPost(webClient2, paramsList, url2);
	    html3 = (HtmlPage) gethtmlPost2(webClient2, paramsList, url2);*/
		
 		//System.out.println(html3.asXml());
 		
		return webClient2;
	}
	
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			webClient.setJavaScriptTimeout(20000); 
			webClient.getOptions().setTimeout(20000); // 15->60 
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Referer", "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static Page gethtmlPost2(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			webClient.setJavaScriptTimeout(20000); 
			webClient.getOptions().setTimeout(20000); // 15->60 
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static Page gethtmlPost3(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			webClient.setJavaScriptTimeout(20000); 
			webClient.getOptions().setTimeout(20000); // 15->60 
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			
			webRequest.setAdditionalHeader("Accept", "application/xml, text/xml, */*; q=0.01");
			webRequest.setAdditionalHeader("Referer", "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  
			webRequest.setAdditionalHeader("Host", "yn.189.cn");  
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}		
	
	public static Page gethtmlPost4(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			webClient.setJavaScriptTimeout(20000); 
			webClient.getOptions().setTimeout(20000); // 15->60 
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Referer", "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  
			webRequest.setAdditionalHeader("Pragma", "no-cache");  
			webRequest.setAdditionalHeader("Host", "yn.189.cn");  
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 

			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static String getAjax(WebClient webClient)  throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://www.189.cn/login/index.do"), HttpMethod.GET);
		//webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		/*webRequest.setAdditionalHeader("Referer", "http://www.189.cn/html/login/right.html");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  */
		Page searchPage = webClient.getPage(webRequest); 
		return searchPage.getWebResponse().getContentAsString(); 
		
		
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception { 
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		//webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Referer", "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.3"); 
		webRequest.setAdditionalHeader("Host", "yn.189.cn");  
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
		webClient.setJavaScriptTimeout(20000); 
		webClient.getOptions().setTimeout(20000); // 15->60 
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage; 
	}
	
	public static Page getHtml2(String url, WebClient webClient) throws Exception { 
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(20000); 
		webClient.getOptions().setTimeout(20000); // 15->60 
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage; 
	}
	
	public static Page getHtm3(String url, WebClient webClient) throws Exception { 
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "application/xml, text/xml, */*; q=0.01");
		webRequest.setAdditionalHeader("Referer", "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");  
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.3"); 
		webRequest.setAdditionalHeader("Host", "yn.189.cn");  
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn"); 
		webClient.setJavaScriptTimeout(20000); 
		webClient.getOptions().setTimeout(20000); // 15->60 
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage; 
	}

}
