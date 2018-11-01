package test;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
       	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
	    String loginUrl = "http://www.xxzfgjj.com/GJJCX/DoL2o2g2i2n2";	
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("LoginType", "1"));//2
		paramsList.add(new NameValuePair("UserAccount", "41070219881110051x"));//5008202000152
		paramsList.add(new NameValuePair("UserPassword", "111111"));
		paramsList.add(new NameValuePair("UserName", URLEncoder.encode("王超", "UTF-8")));
		paramsList.add(new NameValuePair("CXType", "1"));
		paramsList.add(new NameValuePair("X-Requested-With", "XMLHttpRequest"));
		
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.xxzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.xxzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.xxzfgjj.com/gjjcx/");
		webRequest.setRequestBody("LoginType=1&UserAccount=41070219881110051x&UserPassword=111111&UserName=%E7%8E%8B%E8%B6%85&CXType=1&X-Requested-With=XMLHttpRequest");
		//{"Result_Type":2,"Result_Content":null,"Result_ObjectContent":null,"Result_Message":"","Redirect_Url":"/gjjcx/deposit","Redirect_Type":null}
		//webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
	    System.out.println(page.getWebResponse().getContentAsString());
		  
		    String loginUrl5 = "http://www.xxzfgjj.com/gjjcx/deposit";			
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.POST);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "www.xxzfgjj.com");
			webRequest5.setAdditionalHeader("Referer", "http://www.xxzfgjj.com/gjjcx/");				
			Page page5 = webClient.getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
		
			
			String loginUrl6 = "http://www.xxzfgjj.com/gjjcx/deposit/241238.html";			
			WebRequest webRequest6 = new WebRequest(new URL(loginUrl6), HttpMethod.POST);
			webRequest6.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest6.setAdditionalHeader("Connection", "keep-alive");
			webRequest6.setAdditionalHeader("Host", "www.xxzfgjj.com");
			webRequest6.setAdditionalHeader("Referer", "http://www.xxzfgjj.com/gjjcx/deposit/241238.html");				
			Page page6 = webClient.getPage(webRequest6);
			System.out.println(page6.getWebResponse().getContentAsString());
			
			
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
