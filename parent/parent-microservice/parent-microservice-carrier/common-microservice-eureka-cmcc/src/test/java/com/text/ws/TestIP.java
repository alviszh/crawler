package com.text.ws;

import java.net.URL;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class TestIP {
	
	public static void main(String[] args) {
		
		try {
			test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test() throws Exception{
		String url = "https://www.toutiao.com/ch/news_entertainment/"; 
		WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
		//放入代理ip
		request.setProxyHost("117.69.175.161");
		request.setProxyPort(4548);
		WebClient webClient = WebCrawler.getInstance().getWebClient();

		long startTime = System.currentTimeMillis();
		HtmlPage page = webClient.getPage(request);
		long endTime = System.currentTimeMillis();
		String html = page.getWebResponse().getContentAsString();
		System.out.println("今日头条----"+html);
		System.out.println("此IP打开页面总耗时："+(endTime-startTime)+"毫秒");
		
	}

}
