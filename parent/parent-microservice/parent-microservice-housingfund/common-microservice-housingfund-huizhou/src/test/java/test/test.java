package test;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url1 = "http://oa.hzgjj.cn:8083/vgsGjj-port/psnquery/psnqueryPsninfoAction.shtml;jsessionid=3BA02DA0F9DE1656EB8BCE497F4CF44C";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "oa.hzgjj.cn:8083");
		webRequest.setAdditionalHeader("Origin", "http://oa.hzgjj.cn:8083");
		webRequest.setAdditionalHeader("Referer", "http://oa.hzgjj.cn:8083/vgsGjj-port/psnquery/psnqueryPsninfoAction.shtml");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "method=login&perName=%E9%BB%84%E9%9B%AF%E9%9B%AF&perAccount=0306740&perNo=441322199408281722&perMobile=15916412775";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
	    System.out.println(searchPage.getWebResponse().getContentAsString());
	}

}
