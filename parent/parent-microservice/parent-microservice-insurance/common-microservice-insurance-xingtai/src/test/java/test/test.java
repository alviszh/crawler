package test;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url1 = "http://hext.lss.gov.cn/ecdomain/searchYLData";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "hext.lss.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://hext.lss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://hext.lss.gov.cn/ecdomain/framework/fwdt/eeidicegkihebbofiknhifkbkkeahofk.jsp");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		
		String requestBody = "pagenum=0&idCard=130521198901110528&personNumber=1305013142915";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
	   // System.err.println(searchPage.getWebResponse().getContentAsString());
		String url = "http://hext.lss.gov.cn/ecdomain/searchYBData?pagenum=0&idCard=130521198901110528&personNumber=130501314291";
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.POST);
		Page searchPage1 = webClient.getPage(webRequest1);
		System.err.println(searchPage1.getWebResponse().getContentAsString());
	}

}
