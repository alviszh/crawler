package test;



import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

	    String loginUrl4 = "http://www.jsyz.si.gov.cn/shebao/front/web";
	
		String requestBody="method=anotherlogin&userName=8300180115&psd=123&B3=";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("method","anotherlogin"));
//		paramsList.add(new NameValuePair("userName", "8300180115"));
//		paramsList.add(new NameValuePair("psd", "psd"));
//		paramsList.add(new NameValuePair("B3", ""));
		
		WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);
		webRequest4.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest4.setAdditionalHeader("Connection", "keep-alive");
		webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest4.setAdditionalHeader("Host", "www.jsyz.si.gov.cn");
		webRequest4.setAdditionalHeader("Origin", "http://www.jsyz.si.gov.cn");
		webRequest4.setAdditionalHeader("Referer", "http://www.jsyz.si.gov.cn/shebao/index.jsp");
		webRequest4.setRequestBody(requestBody);
		//webRequest4.setRequestParameters(paramsList);//没有注册信息  //密码错误
	    Page page4 = webClient.getPage(webRequest4);
	    String alertMsg=WebCrawler.getAlertMsg();
	    System.out.println(alertMsg);
		System.out.println(page4.getWebResponse().getContentAsString());

		HtmlPage pageInfo=getHtml("http://www.jsyz.si.gov.cn/shebao/front/web?method=identity&userCode=8300180115",webClient);
		System.out.println(pageInfo.asXml());
		
		HtmlPage pageInfo2=getHtml("http://www.jsyz.si.gov.cn/shebao/front/web?method=useraccount&userCode=8300180115",webClient);
		System.out.println(pageInfo2.asXml());
			
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
