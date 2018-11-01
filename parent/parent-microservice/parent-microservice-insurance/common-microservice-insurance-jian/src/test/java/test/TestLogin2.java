package test;


import java.net.URL;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String loginUrl4 = "http://jasi12333.xicp.net:58699/jxquery/person/logincl";
		String requestBody="name=362421199301140428&password=930205&errors=";
		WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);
		webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest4.setAdditionalHeader("Connection", "keep-alive");
		webRequest4.setAdditionalHeader("Host", "jasi12333.xicp.net:58699");
		webRequest4.setRequestBody(requestBody);
		HtmlPage page4 = webClient.getPage(webRequest4);
		 System.out.println(page4.getWebResponse().getContentAsString());

		 
		 HtmlPage  personhome= getHtml("http://jasi12333.xicp.net:58699/jxquery/person/person_jb.jsp",webClient);
		 System.out.println(personhome.getWebResponse().getContentAsString());
	  
		 HtmlPage accounthome= getHtml("http://jasi12333.xicp.net:58699/jxquery/person/account_mx.jsp",webClient);
		 System.out.println(accounthome.getWebResponse().getContentAsString());
		  
	
		 
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
