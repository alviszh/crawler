package com.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class ListTest {

	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\tel2.xls";

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		webClient = POIUnit.addCookie(webClient, cookiefile, "yn.189.cn");

		System.out.println("================================");
		Set<Cookie> cookieswebclients = webClient.getCookieManager().getCookies();
		/*String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp";
		List<NameValuePair>  paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
		paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
		paramsList.add(new NameValuePair("QUERY_TYPE", "10"));
		//Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
		Page page = TelecomLogin.getHtml(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());*/
		/*Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
		for (Cookie cookie4 : cookies4) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}*/
		//WebClient webCilent2 = login();

		/*//if (webCilent2 != null) {

			Set<Cookie> cookies3 = login().getCookieManager().getCookies();

			for (Cookie cookie3 : cookies3) {
				System.out.println("登录cookie ：" + cookie3.toString());
				if (cookie3.getName().indexOf("COM.TYDIC.SSO_AUTH_TOKEN	") != -1) {
					continue;
				}
				for (Cookie cookieswebclient : cookieswebclients) {
					

					if (cookie3.getName().indexOf("JSESSIONID")!=-1) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}


					if (cookie3.getName().equals("COM.TYDIC.SSO_AUTH_TOKEN")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}

					if (cookie3.getName().equals("trkId")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}

					if (cookie3.getName().equals("trkHmClickCoords")) {
						System.out.println("=========" + cookie3.toString());
						webClient.getCookieManager().addCookie(cookie3);
					}

					// 变化会导致登录出问题 }

					if (cookie3.getName().equals("Hm_lpvt_024e4958b87ba93ed27e4571805fbb5a")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}

					if (cookie3.getName().equals("Hm_lvt_024e4958b87ba93ed27e4571805fbb5a")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}
					if (cookie3.getName().equals("WT_FPC")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}
					if (cookie3.getName().equals("WT_SS")) {
						webClient.getCookieManager().addCookie(cookie3);
					}

					if (cookie3.getName().equals(".ybtj.189.cn")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}

					if (cookie3.getName().equals("WT_si_n")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}

					if (cookie3.getName().equals("s_fid")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}
					if (cookie3.getName().equals("lvid")) {
						System.out.println("=========" + cookie3.toString());

						webClient.getCookieManager().addCookie(cookie3);
					}

				}
			}

			String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp";
			List<NameValuePair>  paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("NUM", "13312530250"));
			paramsList.add(new NameValuePair("AREA_CODE", "0871"));
			paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
			paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
			paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
			paramsList.add(new NameValuePair("QUERY_TYPE", "1010"));
			Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
			System.out.println(page.getWebResponse().getContentAsString());
			Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
			for (Cookie cookie4 : cookies4) {
				System.out.println("发送请求的cookie：" + cookie4.toString());
			}
		//}*/
		  
		    String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp";
			List<NameValuePair>  paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("NUM", "13312530250"));
			paramsList.add(new NameValuePair("AREA_CODE", "0871"));
			paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
			paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
			paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
			paramsList.add(new NameValuePair("QUERY_TYPE", "1010"));
			Page page = TelecomLogin2.gethtmlPost(webClient, paramsList, url);
			System.out.println(page.getWebResponse().getContentAsString());
			Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
			for (Cookie cookie4 : cookies4) {
				System.out.println("发送请求的cookie：" + cookie4.toString());
			}
	}

	public static WebClient login() throws Exception {

		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		String url2 = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url2, webClient2);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("13312530250");
		passwordInput.setText("211314");

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
			return null;
		} else {
			System.out.println("=======成功==============");
		}

		url2 = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage html3 = (HtmlPage) getHtml(url2, webClient2);

		url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00900906";
		html3 = (HtmlPage) getHtml(url2, webClient2);

		System.out.println(html3.asXml());

		/*
		 * url2 =
		 * "http://qh.189.cn/service/bill/resto.action?rnd=0.6692071573214755";
		 * 
		 * html3 = (HtmlPage) getHtml(url2, webClient2);
		 */

		Set<Cookie> cookies4 = html3.getWebClient().getCookieManager().getCookies();
		for (Cookie cookie4 : cookies4) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}
		return html3.getWebClient();
		// return null;
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(20000);

		webClient.getOptions().setTimeout(20000); // 15->60

		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
