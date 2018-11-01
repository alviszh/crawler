package org.common.microservice.eureka.china.unicomt.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

public class dianxintestbyclieck {

	private static Gson gs = new Gson();

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("13366777357");
		passwordInput.setText("591414");

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		}
		
		callthremByClieck(webClient);

	}
	
	public static void callthremByClieck(WebClient webClient) throws Exception{
		String url = "http://bj.189.cn/iframe/feequery/detailBillIndex.parser";
		HtmlPage html3 = getHtml(url, webClient);
		
		System.out.println("========================================================");
		System.out.println(html3.asXml());
		
	/*	HtmlElement callclieck = html3.getFirstByXPath("//*[@id='01390638']");

		System.out.println(callclieck.asXml());
		HtmlPage html4  = callclieck.click();
		Thread.sleep(10000);*/
	
		
	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
