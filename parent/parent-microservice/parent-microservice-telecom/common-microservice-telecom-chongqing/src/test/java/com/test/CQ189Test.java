package com.test;

import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class CQ189Test {

	public static void main(String[] args) throws Exception {
		 
		//org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		webClient = login(webClient, "15320975586", "550520");
		
		String ssopage = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202"; 
		getHtmlPage(webClient,ssopage,HttpMethod.GET); 
		String cqpage = "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
		HtmlPage page = getHtmlPage(webClient,cqpage,HttpMethod.GET); 
		
		 
		System.out.println(page.asXml());
		
		
		
	}
	
	
	private static WebClient login(WebClient webClient, String name, String password) {

		try {
			String url = "http://login.189.cn/login";
			HtmlPage html = getHtmlPage(webClient, url, null);
			HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
			username.setText(name);
			passwordInput.setText(password);

			HtmlPage htmlpage = button.click();

			// String a = htmlpage.asXml();
			//
			// System.out.println(a);

			if (htmlpage.asXml().indexOf("登录失败") != -1) {
				System.out.println("登录失败");
			} else {
				System.out.println("登录成功");
				String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
				System.out.println("cookieString---------------------->" + cookieString);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return webClient;

	}
	
	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

}
