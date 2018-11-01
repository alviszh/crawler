package test;

import java.net.URL;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		    String url = "http://121.30.239.174:90/netface/login.do?r=0.11954986423652114";		                     
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
			//username=01010001356&password=8205&loginType=3&vertcode=5768&bsr=chrome%2F64.0.3282.119&vertype=1
			String requestBody="username=140202198205113035&password=820511&loginType=4&vertcode=&bsr=chrome%2F64.0.3282.119&vertype=1";
			webRequest.setAdditionalHeader("Accept", "*/*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest.setAdditionalHeader("Host", "121.30.239.174:90");
			webRequest.setAdditionalHeader("Origin", "http://121.30.239.174:90");
			webRequest.setAdditionalHeader("Referer", "http://121.30.239.174:90/netface/login.do");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			System.out.println(page.getWebResponse().getContentAsString());
			//{"success":true, "url":"/index.do"}
			//{"success": false, "msg":"无效的身份证号码", "validVertCode":false}
			//{"success": false, "msg":"用户名/密码错误", "validVertCode":false}
			//{"success": false, "msg":"用户名或密码错误", "validVertCode":true}
			
			String urlIndex="http://121.30.239.174:90/netface/index.do";
			 HtmlPage  page3= getHtml(urlIndex,  webClient);
			 System.out.println(page3.getWebResponse().getContentAsString());
			String url2="http://121.30.239.174:90/netface/per/queryPerInfo.do";
			webClient.getOptions().setJavaScriptEnabled(true);  
			webClient.waitForBackgroundJavaScript(20000);
		    WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
			webRequest2.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest2.setAdditionalHeader("Connection", "keep-alive");
			webRequest2.setAdditionalHeader("Host", "121.30.239.174:90");
			webRequest2.setAdditionalHeader("Referer", "http://121.30.239.174:90/netface/index.do");
		
			Page page2 = webClient.getPage(webRequest2);
			System.out.println(page2.getWebResponse().getContentAsString());
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
