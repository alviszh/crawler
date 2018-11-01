package com.test;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TelecomLogin {

	public static void main(String[] args) throws Exception {
		String phonenum = "17763819087";
		String password = "211314";

		login(phonenum, password);
	}

	public static WebClient login(String phonenum, String password) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		//
		username.setText(phonenum);
		passwordInput.setText(password);

		// System.out.println(html.asXml());

		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		html = button.click();

		HtmlTextInput valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("//*[@id='txtCaptcha']");
		

		System.out.println("===================================");
		System.out.println(html.asXml());
		System.out.println("===================================");

		HtmlImage valiCodeImg = html.getFirstByXPath("//*[@id='imgCaptcha']");
		ImageReader imageReader = valiCodeImg.getImageReader();
		BufferedImage bufferedImage = imageReader.read(0);

		JFrame f2 = new JFrame();
		JLabel l = new JLabel();
		l.setIcon(new ImageIcon(bufferedImage));
		f2.getContentPane().add(l);
		f2.setSize(200, 200);
		f2.setTitle("验证码");
		f2.setVisible(true);

		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
		 username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		 passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		 valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("//*[@id='txtCaptcha']");

		button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");

		username.setText(phonenum.trim());
		passwordInput.setText(password.trim());
		valicodeStrinput.setText(valicodeStr.toLowerCase().trim());

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {

			System.out.println(htmlpage2.asXml());

			System.out.println("=======失败==============");

			return null;
		} else {
			System.out.println("=======成功==============");
		}
		url = "http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
		Page html3 = getHtml2(url, webClient);

		System.out.println(html3.getWebResponse().getContentAsString());

		/*
		 * url2 =
		 * "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_new.jsp";
		 * 
		 * List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		 * paramsList = new ArrayList<NameValuePair>(); paramsList.add(new
		 * NameValuePair("NUM", "13312530250")); paramsList.add(new
		 * NameValuePair("AREA_CODE", "0871")); paramsList.add(new
		 * NameValuePair("PROD_NO", "4217")); Page page =
		 * TelecomLogin.gethtmlPost(webClient2, paramsList, url2); html3 =
		 * (HtmlPage) gethtmlPost2(webClient2, paramsList, url2);
		 */

		// System.out.println(html3.asXml());

		return webClient;
	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			webClient.setJavaScriptTimeout(20000);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Referer",
					"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setAdditionalHeader("Host", "yn.189.cn");
			webRequest.setAdditionalHeader("Pragma", "no-cache");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");

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

	public static Page gethtmlPost2(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			webClient.setJavaScriptTimeout(20000);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(10000);
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

	public static Page gethtmlPost3(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			webClient.setJavaScriptTimeout(20000);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

			webRequest.setAdditionalHeader("Accept", "application/xml, text/xml, */*; q=0.01");
			webRequest.setAdditionalHeader("Referer",
					"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setAdditionalHeader("Host", "yn.189.cn");
			webRequest.setAdditionalHeader("Pragma", "no-cache");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
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

	public static Page gethtmlPost4(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			webClient.setJavaScriptTimeout(20000);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Referer",
					"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setAdditionalHeader("Pragma", "no-cache");
			webRequest.setAdditionalHeader("Host", "yn.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
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

	public static String getAjax(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://www.189.cn/login/index.do"), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept", "application/json,
		// text/javascript, */*; q=0.01");
		/*
		 * webRequest.setAdditionalHeader("Referer",
		 * "http://www.189.cn/html/login/right.html");
		 * webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		 */
		Page searchPage = webClient.getPage(webRequest);
		return searchPage.getWebResponse().getContentAsString();

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Referer",
				"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.3");
		webRequest.setAdditionalHeader("Host", "yn.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static Page getHtml2(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static Page getHtm3(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Referer",
				"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Host", "yn.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");

		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static Page getHtm4(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("Accept", "application/xml, text/xml,
		// */*; q=0.01");
		webRequest.setAdditionalHeader("Host", "yn.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(10000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static WebClient addcookieForSMSAndCall(WebClient webClient) throws Exception {
		Map<String, String> cookieMaps = new HashMap<>();
		cookieMaps.put("Hm_lvt_79fae2027f43ca31186e567c6c8fe33e", "15,054,702,261,505,600,000");
		cookieMaps.put("Hm_lvt_99c2beeae1f4239c63480835d2892bf4", "1501581156");
		cookieMaps.put("Hm_lvt_ad041cdaa630664faeaf7ca3a6a45b89", "15,051,808,891,505,200,000");
		cookieMaps.put("UM_distinctid", "15d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557");
		cookieMaps.put("__utma", "218110873.589372428.1501581156.1501581156.1501581156.1");
		cookieMaps.put("__utmz", "218110873.1501581156.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
		cookieMaps.put("_gscu_1758414200", "015599983snitv16");
		cookieMaps.put("cn_1260051947_dplus",
				"%7B%22distinct_id%22%3A%20%2215d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557%22%2C%22sp%22%3A%20%7B%22%24recent_outside_referrer%22%3A%20%22sso.telefen.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201505462008%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201505462008%7D%2C%22initial_view_time%22%3A%20%221501493821%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DC5gnI1MOIDBWo3OrGrMxnEjAAnhMBNOAHWIls2ffKuq-_zaY4GpCvvgm2_QRPn6J%26wd%3D%26eqid%3Dbf7d80a2000275f300000002597efec8%22%2C%22initial_referrer_domain%22%3A%20%22www.baidu.com%22%7D");
		cookieMaps.put("fenxiId", "8c07f0a0-3c66-46ec-b4b4-1ed04013c6a9");
		cookieMaps.put("i_vnum", "9");
		cookieMaps.put("ijg", "1.50518E+12");
		cookieMaps.put("pgv_pvi", "6499893248");
		cookieMaps.put("s_sq", "%5B%5BB%5D%5D");
		cookieMaps.put("trkHmCity", "0");
		cookieMaps.put("trkHmClickCoords", "0");
		cookieMaps.put("trkHmCoords", "0");
		cookieMaps.put("trkHmLinks", "0");
		cookieMaps.put("trkHmPageName", "%2Fyn%2F");

		Set<Cookie> cookiesWebClient = webClient.getCookieManager().getCookies();
		System.out.println("第三种：通过Map.entrySet遍历key和value");
		for (Map.Entry<String, String> entry : cookieMaps.entrySet()) {
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
			boolean istrue = false;
			for (Cookie cookie : cookiesWebClient) {
				if (entry.getKey().indexOf(cookie.getName()) != -1) {
					System.out.println("===" + cookie.toString());
					istrue = true;
					break;
				}
			}
			if (istrue) {
				continue;
			}
			webClient.getCookieManager().addCookie(new Cookie("yn.189.cn", entry.getKey(), entry.getValue()));
		}

		return webClient;

	}
}