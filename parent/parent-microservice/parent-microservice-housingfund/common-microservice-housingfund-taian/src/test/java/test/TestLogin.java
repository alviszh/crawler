package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
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

		
	     HtmlPage page=getHtml("http://tagjj.com:7001/wscx/zfbzgl/zfbzsq/login_hidden.jsp?password=111111&sfzh=370902199112251848&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&dbname=gjjmx9&dlfs=0",webClient);
		 System.out.println(page.asXml());

		   String loginUrl4 = "http://tagjj.com:7001/wscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp";
		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			String body="sfzh=370902199112251848&zgxm=%C0%EE%F0%A9&zgzh=020212000095&dwbm=020212&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&zgzt=%D5%FD%B3%A3";

	
	    	webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "tagjj.com:7001");
			webRequest4.setAdditionalHeader("Origin", "http://tagjj.com:7001");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(body);
			HtmlPage page4 = webClient.getPage(webRequest4);
			System.out.println(page4.asXml());
			
					
		    String loginUrl5 = "http://tagjj.com:7001/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";			
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.POST);
			String requestBody5="sfzh=370902199112251848&zgxm=%C0%EE%F0%A9&zgzh=020212000095&dwbm=020212&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&zgzt=%D5%FD%B3%A3";
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "tagjj.com:7001");
			webRequest5.setAdditionalHeader("Origin", "http://tagjj.com:7001");
			webRequest5.setAdditionalHeader("Referer", "http://tagjj.com:7001/wscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest5.setRequestBody(requestBody5);
			HtmlPage page5 = webClient.getPage(webRequest5);
			System.out.println(page5.asXml());
			 
		    String loginUrl6 = "http://tagjj.com:7001/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";			
			WebRequest webRequest6 = new WebRequest(new URL(loginUrl6), HttpMethod.POST);
			String requestBody6="cxydone=2016-2017&cxydtwo=2016-2017&yss=2&totalpages=2&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&zgzh=020212000095&sfzh=370902199112251848&zgxm=%C0%EE%F0%A9&dwbm=020212";
			webRequest6.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest6.setAdditionalHeader("Connection", "keep-alive");
			webRequest6.setAdditionalHeader("Host", "tagjj.com:7001");
			webRequest6.setAdditionalHeader("Origin", "http://tagjj.com:7001");
			webRequest6.setAdditionalHeader("Referer", "http://tagjj.com:7001/wscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp");
			webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest6.setRequestBody(requestBody6);
			HtmlPage page6 = webClient.getPage(webRequest5);
			 System.out.println(page6.asXml());
		  
			
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
	
	
}
