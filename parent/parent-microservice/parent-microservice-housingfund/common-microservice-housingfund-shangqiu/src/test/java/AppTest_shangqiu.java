

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class AppTest_shangqiu {
	public static void main(String[] args) {
		
		try {
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.sqgjj.com/Convenient/PersonSearch";
			String codeurl = "http://www.sqgjj.com/Convenient"; 
			WebRequest webRequest = new WebRequest(new URL(codeurl), HttpMethod.GET); 
//			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//			webRequest.setAdditionalHeader("Connection", "keep-alive");
//			webRequest.setAdditionalHeader("Host", "www.sqgjj.com");
//			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Mobile Safari/537.36");
			HtmlPage page = webClient.getPage(webRequest); 
			String html11 = page.getWebResponse().getContentAsString();
			System.out.println(html11);
		} catch (Exception e) {
		}
	}
}
