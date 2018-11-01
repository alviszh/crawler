package test;


import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		
		    String loginUrl4 = "http://cx2.ybxww.cn:6655/login.asp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sfzh", "511525199207015389"));
			paramsList.add(new NameValuePair("username", URLEncoder.encode("刘倩", "UTF-8")));
			paramsList.add(new NameValuePair("pass", "480094"));
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);
			String body="sfzh=511525199207015389&username=%E5%88%98%E5%80%A9&pass=480094";
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "cx2.ybxww.cn:6655");
			webRequest4.setAdditionalHeader("Origin", "http://cx2.ybxww.cn:6655");
			webRequest4.setAdditionalHeader("Referer", "http://cx2.ybxww.cn:6655/login.asp");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			//webRequest4.setRequestParameters(paramsList);
			webRequest4.setRequestBody(body);
			HtmlPage page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
	
			HtmlPage pageInfo=getHtml("http://cx2.ybxww.cn:6655/hrss.asp?id=a1",webClient);
			System.out.println(pageInfo.asXml());
			
			HtmlPage pageInfo2=getHtml("http://cx2.ybxww.cn:6655/hrss.asp?id=b2",webClient);
			System.out.println(pageInfo2.asXml());
			
			HtmlPage pageInfo3=getHtml("http://cx2.ybxww.cn:6655/hrss.asp?id=c2",webClient);
			System.out.println(pageInfo3.asXml());
			
			//https://www.sdwfhrss.gov.cn/rsjwz/self/cblb?time=2017
		    String loginUrl5 = "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls";
		
			WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.GET);
			webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
			webRequest5.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls");
			webRequest5.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			HtmlPage page5 = webClient.getPage(webRequest5);
			System.out.println(page5.asXml());
			
			
			
			System.out.println(getHtml("https://www.sdwfhrss.gov.cn/rsjwz/self/cblb?time=2016",webClient).asXml());
			
			String medUrl="https://www.sdwfhrss.gov.cn/rsjwz/self/zgyl?time=2017";
			HtmlPage medPage= getHtml(medUrl,webClient);
			System.out.println(medPage.asXml());
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
