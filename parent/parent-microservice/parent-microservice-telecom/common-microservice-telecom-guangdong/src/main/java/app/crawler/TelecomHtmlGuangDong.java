package app.crawler;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class TelecomHtmlGuangDong {
	@Autowired
	private TracerLog tracer;

	public String getPayMent(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String string, String string2, int i, String month1) {
		try {	
			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/consumeInfo/payRecharge/payRecharge.html?in_cmpid=khzy-zcdh-czjf-czjfjlcx-czjfjlcx");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			webClient = addcookie(webClient, taskMobile);
			String url = "http://gd.189.cn/J/J10165.j?"
					+ "a.c=0"
					+ "&a.u=user"
					+ "&a.p=pass"
					+ "&a.s=ECSS"
					+ "&d.d01="+messageLogin.getName()
					+ "&d.d02="+month1;


			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = gethtmlPost(webClient, null, url);


			String html = page.getWebResponse().getContentAsString();
			if(html.indexOf("r")!=-1){
				return html;
			}
			return null;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}


	}
	public String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String kai, String end, int i, String month1) {
		try {
			webClient = addcookie(webClient, taskMobile);
			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			String url3 = "http://gd.189.cn/J/J10037.j";
			//a.c=0&a.u=user&a.p=pass&a.s=ECSS&c.n=客户状态查询&c.t=04&c.i=04-013&
			List<NameValuePair> paramsList3 = new ArrayList<>();
			paramsList3.add(new NameValuePair("a.c", "0"));
			paramsList3.add(new NameValuePair("a.u", "user"));
			paramsList3.add(new NameValuePair("a.p", "pass"));
			paramsList3.add(new NameValuePair("a.s", "ECSS"));
			paramsList3.add(new NameValuePair("c.n", "客户状态查询"));
			paramsList3.add(new NameValuePair("c.t", "04"));
			paramsList3.add(new NameValuePair("c.i", "04-013&"));
			Page page3 = gethtmlPost(webClient, paramsList3, url3);
			String html3 = page3.getWebResponse().getContentAsString();
			if(html3.indexOf("r")!=-1){
				JSONObject obj = JSONObject.fromObject(html3);
				String r = obj.getString("r");
				JSONObject obj2 = JSONObject.fromObject(r);
				String r01 = obj2.getString("r01");
				JSONArray obj3 = JSONArray.fromObject(r01);
				JSONObject obj4 = obj3.getJSONObject(0);
				String date = obj4.getString("r0104");
				String date2 = date.substring(0, 8);//入网时间
				String date3 = date.substring(0, 6);
				webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webClient.addRequestHeader("Host", "gd.189.cn");
				webClient.addRequestHeader("Origin", "http://gd.189.cn");
				webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
				webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

				String url2 = "http://gd.189.cn/J/J10008.j";
				List<NameValuePair> paramsList = new ArrayList<>();
				paramsList.add(new NameValuePair("a.c", "0"));
				paramsList.add(new NameValuePair("a.u", "user"));
				paramsList.add(new NameValuePair("a.p", "pass"));
				paramsList.add(new NameValuePair("a.s", "ECSS"));
				paramsList.add(new NameValuePair("d.d01", "call"));
				paramsList.add(new NameValuePair("d.d02", month1));
				paramsList.add(new NameValuePair("d.d03", kai));
				paramsList.add(new NameValuePair("d.d04", end));
				paramsList.add(new NameValuePair("d.d05", "20"));
				paramsList.add(new NameValuePair("d.d06", "1"));
				paramsList.add(new NameValuePair("&d.d08", "1"));
				Page page2 = gethtmlPost(webClient, paramsList, url2);
				String html2 = page2.getWebResponse().getContentAsString();
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				String str = messageLogin.getSms_code();

				webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webClient.addRequestHeader("Host", "gd.189.cn");
				webClient.addRequestHeader("Origin", "http://gd.189.cn");
				webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
				webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

				String url = "http://gd.189.cn/J/J10009.j";
				List<NameValuePair> paramsList2 = new ArrayList<>();
				paramsList2.add(new NameValuePair("a.c", "0"));
				paramsList2.add(new NameValuePair("a.u", "user"));
				paramsList2.add(new NameValuePair("a.p", "pass"));
				paramsList2.add(new NameValuePair("a.s", "ECSS"));
				paramsList2.add(new NameValuePair("c.n", "语音清单"));
				paramsList2.add(new NameValuePair("c.t", "02"));
				paramsList2.add(new NameValuePair("c.i", "02-005-04"));
				paramsList2.add(new NameValuePair("d.d01", "call"));
				paramsList2.add(new NameValuePair("d.d02", month1));
				if(month1.equals(date3)==true){
					paramsList2.add(new NameValuePair("d.d03", date2));
				}else{
					paramsList2.add(new NameValuePair("d.d03", kai));
				}
				paramsList2.add(new NameValuePair("d.d04", end));
				paramsList2.add(new NameValuePair("d.d05", "800"));
				paramsList2.add(new NameValuePair("d.d06", "1"));
				paramsList2.add(new NameValuePair("d.d07", str));
				paramsList2.add(new NameValuePair("d.d08", "1"));




				webClient.setJavaScriptTimeout(20000);

				webClient.getOptions().setTimeout(20000); // 15->60

				Page page = gethtmlPost(webClient, paramsList2, url);

				String html = page.getWebResponse().getContentAsString();

				System.out.println(html);
				return html;
			}
			return null;
		} catch (Exception e) {
			tracer.addTag("中国电信抓取广东用户通话详单", messageLogin.getTask_id());
			e.printStackTrace();
		}
		return null;

	}


	public String getSMSThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String kai, String end, int i, String month1) {
		try {


			webClient = addcookie(webClient, taskMobile);

			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			String url3 = "http://gd.189.cn/J/J10037.j";
			//a.c=0&a.u=user&a.p=pass&a.s=ECSS&c.n=客户状态查询&c.t=04&c.i=04-013&
			List<NameValuePair> paramsList3 = new ArrayList<>();
			paramsList3.add(new NameValuePair("a.c", "0"));
			paramsList3.add(new NameValuePair("a.u", "user"));
			paramsList3.add(new NameValuePair("a.p", "pass"));
			paramsList3.add(new NameValuePair("a.s", "ECSS"));
			paramsList3.add(new NameValuePair("c.n", "客户状态查询"));
			paramsList3.add(new NameValuePair("c.t", "04"));
			paramsList3.add(new NameValuePair("c.i", "04-013&"));
			Page page3 = gethtmlPost(webClient, paramsList3, url3);
			String html3 = page3.getWebResponse().getContentAsString();
			System.out.println(html3);
			if(html3.indexOf("r")!=-1){
				JSONObject obj = JSONObject.fromObject(html3);
				String r = obj.getString("r");
				JSONObject obj2 = JSONObject.fromObject(r);
				String r01 = obj2.getString("r01");
				JSONArray obj3 = JSONArray.fromObject(r01);
				JSONObject obj4 = obj3.getJSONObject(0);
				String date = obj4.getString("r0104");
				String date2 = date.substring(0, 8);//入网时间
				String date3 = date.substring(0, 6);


				webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webClient.addRequestHeader("Host", "gd.189.cn");
				webClient.addRequestHeader("Origin", "http://gd.189.cn");
				webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
				webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

				String url2 = "http://gd.189.cn/J/J10008.j";
				List<NameValuePair> paramsList = new ArrayList<>();
				paramsList.add(new NameValuePair("a.c", "0"));
				paramsList.add(new NameValuePair("a.u", "user"));
				paramsList.add(new NameValuePair("a.p", "pass"));
				paramsList.add(new NameValuePair("a.s", "ECSS"));
				paramsList.add(new NameValuePair("d.d01", "call"));
				paramsList.add(new NameValuePair("d.d02", month1));
				paramsList.add(new NameValuePair("d.d03", kai));
				paramsList.add(new NameValuePair("d.d04", end));
				paramsList.add(new NameValuePair("d.d05", "20"));
				paramsList.add(new NameValuePair("d.d06", "1"));
				paramsList.add(new NameValuePair("&d.d08", "1"));
				Page page2 = gethtmlPost(webClient, paramsList, url2);
				String html2 = page2.getWebResponse().getContentAsString();
				String str = messageLogin.getSms_code();
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				System.out.println(html2);
				webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				webClient.addRequestHeader("Host", "gd.189.cn");
				webClient.addRequestHeader("Origin", "http://gd.189.cn");
				webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
				webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

				String url = "http://gd.189.cn/J/J10009.j";
				List<NameValuePair> paramsList2 = new ArrayList<>();
				paramsList2.add(new NameValuePair("a.c", "0"));
				paramsList2.add(new NameValuePair("a.u", "user"));
				paramsList2.add(new NameValuePair("a.p", "pass"));
				paramsList2.add(new NameValuePair("a.s", "ECSS"));
				paramsList2.add(new NameValuePair("c.n", "短信清单"));
				paramsList2.add(new NameValuePair("c.t", "02"));
				paramsList2.add(new NameValuePair("c.i", "02-005-03"));
				paramsList2.add(new NameValuePair("d.d01", "note"));
				paramsList2.add(new NameValuePair("d.d02", month1));
				if(month1.equals(date3)==true){
					paramsList2.add(new NameValuePair("d.d03", date2));
				}else{
					paramsList2.add(new NameValuePair("d.d03", kai));
				}
				paramsList2.add(new NameValuePair("d.d04", end));
				paramsList2.add(new NameValuePair("d.d05", "20"));
				paramsList2.add(new NameValuePair("d.d06", "1"));
				paramsList2.add(new NameValuePair("d.d07", str));
				paramsList2.add(new NameValuePair("d.d08", "1"));




				webClient.setJavaScriptTimeout(20000);

				webClient.getOptions().setTimeout(20000); // 15->60

				Page page = gethtmlPost(webClient, paramsList2, url);

				String html = page.getWebResponse().getContentAsString();

				System.out.println(html);
				return html;
			}
			return null;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}
	public WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
	public Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}}
	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

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
