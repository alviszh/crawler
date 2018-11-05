package com.test;

import java.util.Set;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class FanWenTest2 {
	static String cookiefile1 = "C:\\Users\\Administrator\\Desktop\\tel.xls";
	static String cookiefile2 = "C:\\Users\\Administrator\\Desktop\\tel2.xls";

	public static void main(String[] args) throws Exception {

		WebClient webClient1 = WebCrawler.getInstance().getNewWebClient();

		webClient1 = POIUnit.addCookie(webClient1, cookiefile1, "yn.189.cn");

		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();

		webClient2 = POIUnit.addCookie(webClient2, cookiefile2, "yn.189.cn");
		Set<Cookie> cookies1 = webClient1.getCookieManager().getCookies();
		Set<Cookie> cookies2 = webClient2.getCookieManager().getCookies();

		for (Cookie cookie1 : cookies1) {
			for (Cookie cookie2 : cookies2) {
				if (cookie1.getName().equals(cookie2.getName())) {
					if (cookie1.getValue().indexOf(cookie2.getValue()) == -1) {
						System.out.println(cookie1.toString() + "不等============" + cookie2.toString());
					}

				}
			}
		}

	}

}
