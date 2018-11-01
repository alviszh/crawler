package com.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class RSATest {

	public static void main(String[] args) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://gd.189.cn/J/J10041.j";
		List<NameValuePair> paramsList = new ArrayList<>();
		paramsList.add(new NameValuePair("a.c", "0"));
		paramsList.add(new NameValuePair("a.u", "user"));
		paramsList.add(new NameValuePair("a.p", "pass"));
		paramsList.add(new NameValuePair("a.s", "ECSS"));
		paramsList.add(new NameValuePair("c.n", "套餐使用查询"));
		paramsList.add(new NameValuePair("c.t", "02"));
		paramsList.add(new NameValuePair("c.i", "02-006"));

		//webClient.addRequestHeader("Accept-Ranges", "bytes");
		//webClient.addRequestHeader("Content-Length", "2202");
		//webClient.addRequestHeader("Content-Type", "text/json;charset=gbk");
		//webClient.addRequestHeader("Date", "Wed, 20 Sep 2017 09:14:33 GMT");
		//webClient.addRequestHeader("Set-Cookie", "ecss_identity=68019397354633597548; domain=gd.189.cn; expires=Saturday, 21-Oct-2017 09:14:34 GMT; path=/");
		//webClient.addRequestHeader("Set-Cookie", "TS9d76e8=6f3616b692ff9b483e6e698a7ea4adf6917d55c991e0339059c230ff; Path=/");
		//webClient.addRequestHeader("X-WA-Info", "[V2.S10203.A1.P5947.N5993.RN5998.U0].[OT/json.OG/pages]");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		//webClient.addRequestHeader("Accept-Encoding", "gzip, deflate");
		//webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		//webClient.addRequestHeader("Connection", "keep-alive");
		//webClient.addRequestHeader("Content-Length", "74");
		//webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		webClient.addRequestHeader("Host", "gd.189.cn");
		webClient.addRequestHeader("Origin", "http://gd.189.cn");
		webClient.addRequestHeader("Referer", "http://gd.189.cn/consumeInfo/myHisConsume/myHisConsume.html");
		//webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(".ybtj.189.cn", "44E437FE89DFB46A640B3AEB0DDD03FB");
		map.put("APPCOOKIES", "%3BloginOldUri%3D%252Fservice%252Fhome%252Fquery%252Fjifen.html%253Fssid%253Djtsb-khzy-wdsy-wdjj-");
		map.put("BIGipServerTongYiSouSuo", "3060705472.39455.0000");
		map.put("CNZZDATA1261476274", "535275455-1505886786-%7C1505886786");
		map.put("JSESSIONID", "JLr2ZDvTwdv5sp97pSCBmkcl0D52YskQSmtmcCj0rslKLK6FcgKQ!-1134347362");
		map.put("LATN_CODE_COOKIE", "20");
		map.put("SESSIONID", "97e6e6b0-0ab5-4fc1-bb2d-cc1572777a38");
		map.put("SHOPID_COOKIEID", "10020");
		map.put("TS9d76e8", "05bb89ea65a60a5a451380879e5a9df93765ed198ca45a5e59c364c6");
		map.put("TS6a02c3", "216d18fa193d15fe0624a39acebffa1fb40de4721547970359c36327");
		map.put("UM_distinctid", "15e9e188438da0-0a676d75c6a729-3a3e5f04-15f900-15e9e188439abb");
		map.put("_visit_custid", "1505821441987-607151");
		map.put("choose_v", "TS");
		map.put("cityCode", "gd");
		map.put("create_time", "Tue Sep 19 2017 19:44:01 GMT+0800 (%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)");
		map.put("d_appid", "null");
		map.put("d_channel", "0");
		map.put("d_cmpid", "Null");
		map.put("d_openid", "null");
		map.put("d_source", "other");
		map.put("dqmhIpCityInfos", "%E5%8C%97%E4%BA%AC%E5%B8%82+%E8%81%94%E9%80%9A%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83");
		map.put("ecss_identity", "12660700000000000000");
		map.put("gdLogin", "yes");
		map.put("gdUserInfo", "custId%3D2981672449%3BLATN_CODE_COOKIE%3D020%3BlatnCode%3D020%3Bwt_userid%3D2020336946360000%3BuserId%3D1812672");
		map.put("i_PV", "gd.189.cn%2FconsumeInfo%2FmyHisConsume%2FmyHisConsume.html");
		map.put("i_cc", "TRUE");
		map.put("i_invisit", "1");
		map.put("i_ppv", "45");
		map.put("i_sess", "%20in_mpid%3Dkhzy-zcdh-fycx-wdxf-tcsycx%3B%20ssid%3Dgdds-syleft-wdxx-grxx-wdzl%3B");
		map.put("i_sq", "eship-gdt-touch%3D%2526pid%253Dgd.189.cn%25252FTS%25252Findex.htm%2526pidt%253D1%2526oid%253Dhttp%25253A%25252F%25252Fgd.189.cn%25252F%25253Fin_cmpid%25253Dncpb-sy-dbdh-wt%252526SESSIONID%25253Df02f57a6-9341-4269-96ec-1ada1ad92819%2526ot%253DA");
		map.put("i_url", "%5B%5BB%5D%5D");
		map.put("i_vnum", "8");
		map.put("ijg", "1505980000000");
		map.put("ijg_s", "Less%20than%201%20day");
		map.put("isLogin", "logined");
		map.put("loginStatus", "logined");
		map.put("lvid", "9b5dbb5e27dce77a2e64249132f2c9c0");
		map.put("nvid", "1");
		map.put("s_cc", "TRUE");
		map.put("s_fid", "18A82A9067A66D43-16923CB71CC8122B");
		map.put("s_link", "%5B%5BB%5D%5D");
		map.put("svid", "35C69241C007DB1");
		map.put("trkId", "72AA32C6-0E4D-483E-A5D0-9DCC6CD7F281");
		map.put("userId", "201%7C18126726741");
		map.put("userKey", "18126726741#ECSS#2915058858289966914");
		map.put("wt_acc_nbr", "18126726741");
		map.put("wt_serv_type", "CDMA");
		map.put("wt_sessionid", "2915058858289960000");
		map.put("wt_userid", "2020336946360000");
		map.put("wt_usertype", "2");
		map.put("TS9d76e8", "6f3616b692ff9b483e6e698a7ea4adf6917d55c991e0339059c230ff");
		map.put("ecss_identity", "68019397354633500000");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			webClient.getCookieManager().addCookie(new Cookie("gd.189.cn", entry.getKey(), entry.getValue()));
		}	

		

		Page page = gethtmlPost(webClient, paramsList, url);
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("gbk"));
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
