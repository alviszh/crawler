package com.test;

import java.net.URL;
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

public class FanWenTest {

	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\tel2.xls";

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		webClient = POIUnit.addCookie(webClient, cookiefile, "yn.189.cn");

		System.out.println("================================");
		WebClient webCilent2 = WebCrawler.getInstance().getNewWebClient();
		webCilent2 = login(webCilent2);
		//getphoneCode.getphoneCodeQingHai(webCilent2);
		String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01941226";
		//webClient.getCookieManager().addCookie(new Cookie("yn.189.cn", "JSESSIONID", "0001DgHEo_qbVEE5yb5RPtHUh8A:1K9NU4RN2C") );

		Page page3 = TelecomLogin2.getHtml(url3, webCilent2);
		if (webCilent2 != null) {

			Set<Cookie> cookies3 =webCilent2.getCookieManager().getCookies();
			
			for (Cookie cookie3 : cookies3) {
				
			
				
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

		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp?NUM=13312530250&AREA_CODE=0871&CYCLE_BEGIN_DATE=&CYCLE_END_DATE=&BILLING_CYCLE=201709&QUERY_TYPE=10";
		/*List<NameValuePair>  paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
		paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
		paramsList.add(new NameValuePair("QUERY_TYPE", "10"));
	
		
		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);*/
		Page page = TelecomLogin2.getHtml(url, webClient);
		System.out.println("===========" + page.getWebResponse().getContentAsString());
		Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
		for (Cookie cookie4 : cookies4) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}

	}

	public static WebClient login(WebClient webClient2) throws Exception {

		webClient2 = WebCrawler.getInstance().getNewWebClient();
		String url2 = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url2, webClient2);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("13389757201");
		passwordInput.setText("270822");

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
			return null;
		} else {
			System.out.println("=======成功==============");
		}

		url2 = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage html3 = (HtmlPage) getHtml(url2, webClient2);
		
		Set<Cookie> cookies4 = html3.getWebClient().getCookieManager().getCookies();
		for (Cookie cookie4 : cookies4) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}
		return webClient2;
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
