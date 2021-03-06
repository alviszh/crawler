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
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
	     HtmlPage page=	getHtml("http://www.gzszfgjj.com:8888/wscx/zfbzgl/wscx/login_hidden.jsp?password=111111&sfzh=360702199008240062&cxyd=0&dbname=GJJ&dlfs=01&yzm=01",webClient);
		System.out.println(page.asXml());

		
		   String loginUrl4 = "http://www.bssgjj.com/wscx/zfbzgl/zfbzsq/main_menu.jsp";
		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			String body="zgzh=0195573&sfzh=220122198901270026&zgxm=%C1%F8%BA%E7%D3%EE&dwbm=0101272&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&dbname=gjjmx12&pass1=111111";
//			paramsList.add(new NameValuePair("zgzh","0195573"));
//			paramsList.add(new NameValuePair("sfzh", "220122198901270026"));	
//			paramsList.add(new NameValuePair("zgxm", URLEncoder.encode("柳虹宇", "GBK")));
//			paramsList.add(new NameValuePair("dwbm", "0101272"));
//			paramsList.add(new NameValuePair("cxyd", URLEncoder.encode("当前年度", "GBK")));			
//			paramsList.add(new NameValuePair("dbname", "gjjmx12"));		
//			paramsList.add(new NameValuePair("pass1", "111111"));	
	
	    	webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "www.bssgjj.com");
			webRequest4.setAdditionalHeader("Referer", "http://www.bssgjj.com/wscx/zfbzgl/zfbzsq/login_hidden.jsp?password=111111&sfzh=220122198901270026&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&dbname=gjjmx12");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(body);
			HtmlPage page4 = webClient.getPage(webRequest4);
			System.out.println(page4.asXml());
			
		
			
		    String loginUrl5 = "http://www.bssgjj.com/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";			
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.POST);
			String requestBody5="sfzh=220122198901270026&zgxm=%C1%F8%BA%E7%D3%EE&zgzh=0195573&dwbm=0101272&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8";
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "www.bssgjj.com");
			webRequest5.setAdditionalHeader("Referer", "http://www.bssgjj.com/wscx/zfbzgl/zfbzsq/main_menu.jsp?zgzh=0195573&sfzh=220122198901270026&zgxm=%C1%F8%BA%E7%D3%EE&dwbm=0101272&cxyd=%B5%B1%C7%B0%C4%EA%B6%C8&dbname=gjjmx12&pass1=111111");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest5.setRequestBody(requestBody5);
			HtmlPage page5 = webClient.getPage(webRequest5);
			 System.out.println(page5.asXml());
			 
			  
			
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
