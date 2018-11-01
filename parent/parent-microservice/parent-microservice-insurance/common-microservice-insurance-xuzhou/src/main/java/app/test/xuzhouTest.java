package app.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.module.htmlunit.WebCrawler;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.HttpMethod;
public class xuzhouTest {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		long startWC = System.currentTimeMillis(); 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		long endWC = System.currentTimeMillis(); 
		System.out.println("初始化WC用时:" + (endWC-startWC)); 
		
		String url = "https://www.jsxzhrss.gov.cn//personal/login.jsp?model=ylloading";
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest); 
		
		
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("username", "1002661217"));
		webRequest.getRequestParameters().add(new NameValuePair("password", "320323198811160625"));
		webRequest.getRequestParameters().add(new NameValuePair("passwordstrength", ""));
		
		
		

		webRequest.setAdditionalHeader("Host", "www.jsxz.lss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.jsxz.lss.gov.cn/ylzh.jsp");
		webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate, sdch");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setCharset(Charset.forName("UTF-8"));
		
		HtmlPage page = (HtmlPage)webClient.getPage(webRequest); 
		
		String html = page.getWebResponse().getContentAsString();
		
		System.out.println("html--------"+html);
		
	}
}
