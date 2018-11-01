package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		//org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//webClient.getOptions().setJavaScriptEnabled(true);黑河
		String loginUrl = "http://www.hhgjj.org.cn/PersonLoginServlet";		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		String requestBody="sid=231102199303163230&gcode=111111";
//		paramsList.add(new NameValuePair("gcode", "231102199303163230"));
//		paramsList.add(new NameValuePair("gcode", "111111"));
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.hhgjj.org.cn");
		webRequest.setAdditionalHeader("Origin", "http://www.hhgjj.org.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.hhgjj.org.cn/personal_searchnew.jsp");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestBody(requestBody);
		//webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		System.out.println("登录页   =====》》"+page.asXml());
		Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());		
		}
		String loginUrl2 = "http://www.hhgjj.org.cn/PersonViewServlet?pageNumber=2";	
		WebRequest webRequest8 = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
		webRequest8.setAdditionalHeader("Accept", "t*/*;");
		webRequest8.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest8.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest8.setAdditionalHeader("Connection", "keep-alive");
		webRequest8.setAdditionalHeader("Host", "www.hhgjj.org.cn");
		webRequest8.setAdditionalHeader("Referer", "http://www.hhgjj.org.cn/PersonViewServlet?pageNumber=2");
		webRequest8.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest8.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
	
		Page page8= webClient.getPage(webRequest8);	
		System.out.println("信息1"+page8.getWebResponse().getContentAsString());
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
