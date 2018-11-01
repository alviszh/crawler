package com.test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
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
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.unit.TeleComCommonUnit;

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
