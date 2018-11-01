package test;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
	                     
		String url="http://cx.czzfgjj.cn/pfsystem/login";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
		//
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", "43100219840812004x"));
		paramsList.add(new NameValuePair("mobile", "17752758721"));
		paramsList.add(new NameValuePair("password", "666666"));


		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");

		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Origin", "http://cx.czzfgjj.cn");
		webRequest.setAdditionalHeader("Referer", "http://cx.czzfgjj.cn/pfsystem/login");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
        String html=page.getWebResponse().getContentAsString();
		System.out.println(html);//{"code":200,"msg":"/pfsystem/admin","data":null}
		//{"code":-1,"msg":"用户信息错误，登录失败！","data":null}
		
		 String url2 = "http://cx.czzfgjj.cn/pfsystem/pf/jiaocun/my";		                     
			WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);	
			webRequest2.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest2.setAdditionalHeader("Connection", "keep-alive");
			webRequest2.setAdditionalHeader("Host", "cx.czzfgjj.cn");
			webRequest2.setAdditionalHeader("Origin", "http://cx.czzfgjj.cn/pfsystem/admin");

			HtmlPage page22 = webClient.getPage(webRequest2);
			System.out.println(page22.asXml());
			
			
		     String url3 = "http://cx.czzfgjj.cn/pfsystem/pf/jiaocun/detail/query";		                     
			WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.POST);	
			List<NameValuePair> paramsList3 = new ArrayList<NameValuePair>();
			
			paramsList.add(new NameValuePair("spcode", ""));
			paramsList.add(new NameValuePair("startdate", "2010-01-01"));
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", "300"));
			webRequest3.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest3.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		
			webRequest3.setAdditionalHeader("Connection", "keep-alive");
			webRequest3.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest3.setAdditionalHeader("Host", "cx.czzfgjj.cn");
			webRequest3.setAdditionalHeader("Origin", "http://cx.czzfgjj.cn");
			webRequest3.setAdditionalHeader("Referer", "http://cx.czzfgjj.cn/pfsystem/pf/jiaocun/detail");
			Page page3 = webClient.getPage(webRequest3);
			System.out.println(page3.getWebResponse().getContentAsString());



	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
