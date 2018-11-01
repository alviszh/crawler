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
		//webClient.getOptions().setJavaScriptEnabled(true);
		String loginUrl = "http://www.ybzfgjj.com/admin/zhxx_userlogin.do";		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("SFZHM", "22012219920610504X"));
		paramsList.add(new NameValuePair("MIMA", "916927"));
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.ybzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_loginout.do");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setRequestParameters(paramsList);
		HtmlPage page = webClient.getPage(webRequest);
		System.out.println("登录页   =====》》"+page.asXml());
		Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());		
		}
//		String loginUrl2 = "http://www.ybzfgjj.com/admin/zhxx_grzhxxchax.do?_dc=1510728092121";	
//		WebRequest webRequest8 = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
//		webRequest8.setAdditionalHeader("Accept", "t*/*;");
//		webRequest8.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest8.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
//		webRequest8.setAdditionalHeader("Connection", "keep-alive");
//		webRequest8.setAdditionalHeader("Host", "www.ybzfgjj.com");
//		webRequest8.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
//		webRequest8.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//		webRequest8.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//	
//		Page page8= webClient.getPage(webRequest8);
//	
//		System.out.println("信息1"+page8.getWebResponse().getContentAsString());
		
		String url22="http://www.ybzfgjj.com/admin/zhxx_pangdingGrzh.do?GRZH=094810000972";
		WebRequest webRequest22 = new WebRequest(new URL(url22), HttpMethod.POST);
		webRequest22.setAdditionalHeader("Accept", "*/*");
		webRequest22.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest22.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest22.setAdditionalHeader("Connection", "keep-alive");
		webRequest22.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest22.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
		webRequest22.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest22.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		Page page22 = webClient.getPage(webRequest22);
		Thread.sleep(1000);
		String url = "http://www.ybzfgjj.com/admin/zhxx_Grzhmx.do";	
		             // http://www.ybzfgjj.com/admin/zhxx_Grzhmx.do
	
		WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest2.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		Page page2 = webClient.getPage(webRequest2);
		Thread.sleep(2000);
		System.out.println("信息2："+page2.getWebResponse().getContentAsString());
		
		String url3 = "http://www.ybzfgjj.com/admin/zhxx_Grjjgjjmxb.do";			
		WebRequest webRequest3= new WebRequest(new URL(url3), HttpMethod.POST);
		List<NameValuePair> paramsList3 = new ArrayList<NameValuePair>();
		paramsList3.add(new NameValuePair("start", "0"));
		paramsList3.add(new NameValuePair("limit", "300"));
		webRequest3.setAdditionalHeader("Accept", "*/*");
		webRequest3.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest3.setAdditionalHeader("Connection", "keep-alive");
		webRequest3.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest3.setAdditionalHeader("Host", "www.ybzfgjj.com");
		webRequest3.setAdditionalHeader("Origin", "http://www.ybzfgjj.com");
		webRequest3.setAdditionalHeader("Referer", "http://www.ybzfgjj.com/admin/zhxx_userlogin.do");
		webRequest3.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest3.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest3.setRequestParameters(paramsList3);
		Page page3 = page.getWebClient().getPage(webRequest3);
		
		System.out.println("信息3："+page3.getWebResponse().getContentAsString());
		
//			String loginUrl8 = "http://www.cqgjj.cn/Member/UserInfo/MyInformation.aspx";	
//			WebRequest webRequest8 = new WebRequest(new URL(loginUrl8), HttpMethod.GET);
//			webRequest8.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest8.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest8.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			webRequest8.setAdditionalHeader("Connection", "keep-alive");
//			webRequest8.setAdditionalHeader("Host", "www.cqgjj.cn");
//			webRequest8.setAdditionalHeader("Referer", "http://www.cqgjj.cn/Member/UserInfo/MyInformation.aspx");
//			webRequest8.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//			HtmlPage page8= loggedPage.getWebClient().getPage(webRequest8);
//			if (page8.getWebResponse().getContentAsString().contains("listinfo")) {
//                System.out.println(page8.getWebResponse().getContentAsString());
//			}else{
//				page8= loggedPage.getWebClient().getPage(webRequest8);
//                System.out.println(page8.getWebResponse().getContentAsString());
//			}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
