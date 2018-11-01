package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingCallRecord;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingFlow;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingIncrement;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingMessage;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Chongqing {

	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
//			Cookie next = i.next();
//			if("flow_analysis_key".equals(next.getName()) && "cq.189.cn".equals(next.getDomain())){
//			}
//			else if("com.cqcis.ecsc.sso.client.filter.user".equals(next.getName()) && "cq.189.cn".equals(next.getDomain())){
//			}
//			else if("JSESSIONID".equals(next.getName()) && "cq.189.cn".equals(next.getDomain())){
//			}
//			else if("code_v".equals(next.getName()) && "www.189.cn".equals(next.getDomain())){
//			}
//			else if("loginCode".equals(next.getName()) && "cq.189.cn".equals(next.getDomain())){
//			}
//			else if("com.crunii.ecsc.webkey".equals(next.getName()) && "cq.189.cn".equals(next.getDomain())){
//			}
//			else if("insight_v".equals(next.getName()) && "cq.189.cn".equals(next.getDomain())){
//			}
//			else{
////				System.out.println(i.next());
//				webClient.getCookieManager().addCookie(i.next());
//			}
			webClient.getCookieManager().addCookie(i.next());
			
		}

		return webClient;
	}

	public static void main(String[] args) {
		String cookieT = "[{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"51b9e256f777161b53315ff48e22efd5\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"cq\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"1882306306\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"847\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"51A3FA0E784C2C8775233D52E36C5C4B-n5\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000004327060\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10004\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"CF599980-8A7E-4EFF-9CEB-E042E3E76AC6\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX18LyqlLkCAQNVFjN3y2Se%2Ba2XM7NEaAUm1UlGlcyVFTYafH6%2FuRxqxK%2BXTmqgUncL8KdiUC4yKbY5G2zouGa%2FhuLTX8wM8oNEM%3D\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"0C3821DB2404CF63-0655911438F6A798\"},{\"domain\":\"cq.189.cn\",\"key\":\"td_cookie\",\"value\":\"18446744072580062554\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"15320975586\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"812\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"bktpnkcHr4gqftOsUTpnDI43Vw8H71PrTNrRNLdzvp%2F5U35NbvVUFA%3D%3D\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"D423D61B441BAA11\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"Xtr93hWRPUJYNwJEiGrxMgLG4AIx9leQnwO9lD%2Fc%2FPPNNfKsrCJ%2BfIyhPILLsc87xT7aU3Yma%2BA%2F9XFy3jtjXGsUEdozhyVHisumqMVfm9NBbtdzBJYiJUodwzr%2BrcPChrU0h6tnleM%3D\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"240%2C90%2C605\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"319EF72DEBB3B239\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"8501EFCF4CB88A7B29DDD34014C85342\"}]";
		String cookie = "[{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"240%2C90%2C605\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"Xtr93hWRPUJYNwJEiGrxMgLG4AIx9leQnwO9lD%2Fc%2FPPNNfKsrCJ%2BfIyhPILLsc87xT7aU3Yma%2BA%2F9XFy3jtjXGsUEdozhyVHisumqMVfm9NBbtdzBJYiJUodwzr%2BrcPChrU0h6tnleM%3D\"},{\"domain\":\"cq.189.cn\",\"key\":\"com.crunii.ecsc.webkey\",\"value\":\"bba5389c3a534b7ab35035a10841670c\"},{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX18LyqlLkCAQNVFjN3y2Se%2Ba2XM7NEaAUm1UlGlcyVFTYafH6%2FuRxqxK%2BXTmqgUncL8KdiUC4yKbY5G2zouGa%2FhuLTX8wM8oNEM%3D\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10004\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"749C67C912A6C877\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000004327060\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"1882306306\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"8501EFCF4CB88A7B29DDD34014C85342\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"cq.189.cn\",\"key\":\"loginCode\",\"value\":\"15320975586\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"812\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"5178b98e24e5395fad51bf85bc705426\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"15320975586\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"847\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"bktpnkcHr4gqftOsUTpnDI43Vw8H71PrTNrRNLdzvp%2F5U35NbvVUFA%3D%3D\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"C1DED982-7F2A-4D03-B132-21D568DAC933\"},{\"domain\":\"cq.189.cn\",\"key\":\"flow_analysis_key\",\"value\":\"4e260cd989101109797ed74af22ed24d\"},{\"domain\":\"cq.189.cn\",\"key\":\"td_cookie\",\"value\":\"18446744072580062554\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"0FC4FFBB4148C261-372BAD1965082EA8\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"non-logined\"},{\"domain\":\"cq.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"cq\"},{\"domain\":\"cq.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"6DDB98AFC1260D6B314BC43342EE7DC2\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"3AEEE5382DBACDC9\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\"www.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"51A3FA0E784C2C8775233D52E36C5C4B-n5\"},{\"domain\":\"cq.189.cn\",\"key\":\"com.cqcis.ecsc.sso.client.filter.user\",\"value\":\"\\\"44BBA3D4DE538C51C960F4A5C04EC324:1505551579312\\\"\"},{\"domain\":\"189.cn\",\"key\":\"td_cookie\",\"value\":\"18446744072580530455\"}]";

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String username = "17723292676";
			String password = "440442";

			String usernameT = "15320975586";
			String passwordT = "550520";

			String sendcode = "5642";
			String idcardT = "044813";
			String tnameT = "振";

			String idcard = "200018";
			String tname = "壹";

			String username1 = "18172055939";
			String password1 = "741258";

			// webClient = login(webClient, username1, password1);

//			 webClient = login(webClient, username, password);

			webClient = login(webClient, usernameT, passwordT);

			// webClient = addcookie(cookieT);
			 webClient = cook(webClient);
			// String cook111 = cookieString(webClient);

			/**
			 * 发送验证码
			 */
//			webClient = sendcode(webClient);
			String cookie111 = CommonUnit.transcookieToJson(webClient);
			System.out.println("cookieStringsendcode---------------------->" + cookie111);
			
//			for (int i = 0; i < 2; i++) {
			for (int i = 0; i < 7; i++) {
//			for (int i = 6; i > -1; i--) {
				String yearmonth = getDateBefore("yyyy-MM", i);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(StrToDate(yearmonth));
				calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
				String beginTime = format.format(calendar.getTime());
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				String endTime = format.format(calendar.getTime());

				call(null, usernameT, tnameT, idcardT, "", yearmonth, beginTime, endTime, cookie111);
				msm(null, usernameT, tnameT, idcardT, "", yearmonth, beginTime, endTime, cookie111);
				flow(null, usernameT, tnameT, idcardT, "", yearmonth, beginTime, endTime, cookie111);
				zengzhi(null, usernameT, tnameT, idcardT, "", yearmonth, beginTime, endTime, cookie111);

//				call(webClient, username, tname, idcard, "", yearmonth, beginTime, endTime, null);
//				msm(webClient, username, tname, idcard, "", yearmonth, beginTime, endTime, null);
//				flow(webClient, username, tname, idcard, "", yearmonth, beginTime, endTime, null);
//				zengzhi(webClient, username, tname, idcard, "", yearmonth, beginTime, endTime, null);
				
			}


			/**
			 * 通话详单
			 */
			// call(webClient, usernameT, tnameT, idcardT, "");
			// call(webClient, username, tname, idcard, "");

			// msm(webClient, usernameT, tnameT, idcardT, "");

			// flow(webClient, usernameT, tnameT, idcardT, "");

			// zengzhi(webClient, usernameT, tnameT, idcardT, "");

			/**
			 * 验证姓名身份证号
			 */
			// verificationIdcard(webClient, idcard);
			// verificationTname(webClient, tname);

			/**
			 * 通话记录
			 */
			// call(webClient);

			/**
			 * 充值缴费
			 */
			// chongzhi(webClient);

			/**
			 * 在用业务----已订购的基础功能-----OVER
			 */
			// yewujichu(webClient);

			/**
			 * 在用业务----已订购的增值业务-----OVER
			 */
			// yewuzengzhi(webClient);

			/**
			 * 在用业务-----套餐信息-----OVER
			 */
			// yewutaocan(webClient);

			/**
			 * 套餐使用情况-----OVER
			 */
			// chanpin(webClient);

			/**
			 * 积分---------OVER
			 */
			// jifenjiexi(webClient);
			// jifen(webClient);

			/**
			 * 账单查询---------OVER
			 */
			// zhangdan(webClient);
			/**
			 * 余额信息---------OVER
			 */
			// ye(webClient);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private static String cookieString(WebClient webClient) throws Exception {

		try {

			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
			HtmlPage html3 = getHtmlPage(webClient, url3, null);
			String json3 = html3.getWebResponse().getContentAsString();
			System.out.println(json3);
			String cookieString = CommonUnit.transcookieToJson(html3.getWebClient());
			System.out.println("cookieString---------------------->" + cookieString);
			return cookieString;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static WebClient cook(WebClient webClient) throws Exception {

		try {

//			String url4 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
//			HtmlPage html4 = getHtmlPage(webClient, url4, null);
//			String json4 = html4.getWebResponse().getContentAsString();
//			
//			
//			String url2 = "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
//			HtmlPage html2 = getHtmlPage(webClient, url2, null);
//			String json2 = html2.getWebResponse().getContentAsString();
			
			
			
			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
			HtmlPage html3 = getHtmlPage(webClient, url3, null);
			String json3 = html3.getWebResponse().getContentAsString();
			System.out.println(json3);
			String cookieString = CommonUnit.transcookieToJson(html3.getWebClient());
			System.out.println("cookieString---------------------->" + cookieString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return webClient;
	}

	/**
	 * 增值详单
	 * 
	 * @param webClient
	 * @param accNbr
	 * @param tname
	 * @param idcard
	 * @param sendcode
	 * @return
	 * @throws Exception
	 */
	private static WebClient zengzhi(WebClient webClient, String accNbr, String tname, String idcard, String sendcode,
			String month, String beginTime, String endTime, String cookie) throws Exception {
		try {
			if (null != cookie) {
				webClient = null;
				webClient = addcookie(cookie);
			}
			// String url2 =
			// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
			// HtmlPage html2 = getHtmlPage(webClient, url2, null);
			// String json2 = html2.getWebResponse().getContentAsString();

//			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//			HtmlPage html3 = getHtmlPage(webClient, url3, null);
//			String json3 = html3.getWebResponse().getContentAsString();

			// String url5 =
			// "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
			// HtmlPage html5 = getHtmlPage(webClient, url5, null);

			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", accNbr));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", month));
			paramsList.add(new NameValuePair("callType", "00"));
			paramsList.add(new NameValuePair("listType", "300004"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", sendcode));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));

			Page html1 = getPage(webClient, url1, HttpMethod.POST, paramsList, true);
			String json1 = html1.getWebResponse().getContentAsString();
			System.out.println(month+"zengzhi---json---"+json1);

			// String url4 = "http://cq.189.cn/new-bill/bill_XDCX_Head";
			// Page html4 = getPage(webClient, url4, HttpMethod.POST, null,
			// false);
			// String json4 = html4.getWebResponse().getContentAsString();
			// System.out.println(json4);
			
			String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("page", "1"));
			paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
			// paramsList2.add(new NameValuePair("rows", "10"));

			Page html = getPage(webClient, url, HttpMethod.POST, paramsList2, false);

			String json = html.getWebResponse().getContentAsString();

			System.out.println(json);
			JSONObject jsonObj = JSONObject.fromObject(json);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String businessName = jsonObj1.getString("业务名称");
				String businessCode = jsonObj1.getString("业务代码/号码");
				String chargetype = jsonObj1.getString("计费类型");
				String useTime = jsonObj1.getString("使用时间");
				String callTimeCost = jsonObj1.getString("通话时长（秒）");
				String fee = jsonObj1.getString("费用（元）");
				TelecomChongqingIncrement telecomChongqingIncrementBusiness = new TelecomChongqingIncrement("",
						businessName, businessCode, chargetype, useTime, callTimeCost, fee);
				System.out.println(telecomChongqingIncrementBusiness.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------增值业务--------------------------------------------------");
		System.out.println(month + "--------" + beginTime + "------" + endTime);

		return webClient;
	}

	/**
	 * 流量
	 * 
	 * @param webClient
	 * @param accNbr
	 * @param tname
	 * @param idcard
	 * @param sendcode
	 * @return
	 * @throws Exception
	 */
	private static WebClient flow(WebClient webClient, String accNbr, String tname, String idcard, String sendcode,
			String month, String beginTime, String endTime, String cookie) throws Exception {

		try {
			if (null != cookie) {
				webClient = null;
				webClient = addcookie(cookie);
			}
			// String url2 =
			// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
			// HtmlPage html2 = getHtmlPage(webClient, url2, null);
			// String json2 = html2.getWebResponse().getContentAsString();

//			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//			HtmlPage html3 = getHtmlPage(webClient, url3, null);
//			String json3 = html3.getWebResponse().getContentAsString();

			// String url5 =
			// "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
			// HtmlPage html5 = getHtmlPage(webClient, url5, null);

			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", accNbr));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", month));
			paramsList.add(new NameValuePair("callType", "00"));
			paramsList.add(new NameValuePair("listType", "300003"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", sendcode));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));

			Page html1 = getPage(webClient, url1, HttpMethod.POST, paramsList, true);
			String json1 = html1.getWebResponse().getContentAsString();
			System.out.println(month+"flow---json"+json1);

			// String url4 = "http://cq.189.cn/new-bill/bill_XDCX_Head";
			// Page html4 = getPage(webClient, url4, HttpMethod.POST, null,
			// false);
			// String json4 = html4.getWebResponse().getContentAsString();
			// System.out.println(json4);
			
			String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("page", "1"));
			paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
			// paramsList2.add(new NameValuePair("rows", "10"));

			Page html = getPage(webClient, url, HttpMethod.POST, paramsList2, false);

			String json = html.getWebResponse().getContentAsString();

			System.out.println(json);
			JSONObject jsonObj = JSONObject.fromObject(json);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String netTime = jsonObj1.getString("开始时间");
				String netTimeCost = jsonObj1.getString("上网时长（秒）");
				String netFlow = jsonObj1.getString("流量（KB）");
				String netType = jsonObj1.getString("网络类型");
				String netArea = jsonObj1.getString("通信地点");
				String netBusiness = jsonObj1.getString("业务类型");
				String netFee = jsonObj1.getString("费用（元）");
				TelecomChongqingFlow telecomChongqingFlow = new TelecomChongqingFlow("", netTime, netTimeCost, netFlow,
						netType, netArea, netBusiness, netFee);
				System.out.println(telecomChongqingFlow.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------流量--------------------------------------------------");
		System.out.println(month + "--------" + beginTime + "------" + endTime);

		return webClient;
	}

	/**
	 * 短信
	 * 
	 * @param webClient
	 * @param accNbr
	 * @param tname
	 * @param idcard
	 * @param sendcode
	 * @return
	 * @throws Exception
	 */
	private static WebClient msm(WebClient webClient, String accNbr, String tname, String idcard, String sendcode,
			String month, String beginTime, String endTime, String cookie) throws Exception {

		try {
			if (null != cookie) {
				webClient = null;
				webClient = addcookie(cookie);
			}
			// String url2 =
			// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
			// HtmlPage html2 = getHtmlPage(webClient, url2, null);
			// String json2 = html2.getWebResponse().getContentAsString();

//			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//			HtmlPage html3 = getHtmlPage(webClient, url3, null);
//			String json3 = html3.getWebResponse().getContentAsString();

			// String url5 =
			// "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
			// HtmlPage html5 = getHtmlPage(webClient, url5, null);

			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", accNbr));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", month));
			paramsList.add(new NameValuePair("callType", "00"));
			paramsList.add(new NameValuePair("listType", "300002"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", sendcode));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));

			Page html1 = getPage(webClient, url1, HttpMethod.POST, paramsList, true);
			String json1 = html1.getWebResponse().getContentAsString();
			System.out.println(month+"msm--json----------------------------"+json1);

			// String url4 = "http://cq.189.cn/new-bill/bill_XDCX_Head";
			// Page html4 = getPage(webClient, url4, HttpMethod.POST, null,
			// false);
			// String json4 = html4.getWebResponse().getContentAsString();
			// System.out.println(json4);
			Thread.sleep(1000);
			String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("page", "1"));
			paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
			// paramsList2.add(new NameValuePair("rows", "10"));

			Page html = getPage(webClient, url, HttpMethod.POST, paramsList2, false);

			String json = html.getWebResponse().getContentAsString();

			System.out.println(json);
			JSONObject jsonObj = JSONObject.fromObject(json);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String smsType = jsonObj1.getString("业务类型");
				String smsMobile = jsonObj1.getString("对方号码");
				String smsTime = jsonObj1.getString("发送时间");
				String smsCost = jsonObj1.getString("费用（元）");
				String roam = jsonObj1.getString("漫游状态");
				String inpackage = jsonObj1.getString("是否在套餐内");
				TelecomChongqingMessage telecomChongqingMessage = new TelecomChongqingMessage("", smsType, smsMobile,
						smsTime, smsCost, roam, inpackage);
				System.out.println(telecomChongqingMessage.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------短信--------------------------------------------------");
		System.out.println(month + "--------" + beginTime + "------" + endTime);

		return webClient;
	}

	/**
	 * 通话详单
	 * 
	 * @param webClient
	 * @param accNbr
	 * @param tname
	 * @param idcard
	 * @param sendcode
	 * @return
	 * @throws Exception
	 */
	private static WebClient call(WebClient webClient, String accNbr, String tname, String idcard, String sendcode,
			String month, String beginTime, String endTime, String cookie) throws Exception {

		try {
			if (null != cookie) {
				webClient = null;
				webClient = addcookie(cookie);
//				webClient.getCache().setMaxSize(0);
			}

			// String url2 =
			// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
			// HtmlPage html2 = getHtmlPage(webClient, url2, null);
			// String json2 = html2.getWebResponse().getContentAsString();

//			String url5 = "http://cq.189.cn/new-bill/bill_XDCX?accNbr=15320975586&productId=208511296&billingModeId=2100";
//			Page html5 = getPage(webClient, url5, HttpMethod.POST, null, false);
//			String json5 = html5.getWebResponse().getContentAsString();

//			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//			HtmlPage html3 = getHtmlPage(webClient, url3, null);
//			String json3 = html3.getWebResponse().getContentAsString();
//			String cookieString = CommonUnit.transcookieToJson(webClient);
//			System.out.println("cookieString---------------------->" + cookieString);
//			System.out.println(month+"----------"+cookieString);
			
			// String url5 =
			// "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
			// HtmlPage html5 = getHtmlPage(webClient, url5, null);

			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", accNbr));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", month));
			paramsList.add(new NameValuePair("callType", "00"));
			paramsList.add(new NameValuePair("listType", "300001"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", sendcode));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));

			Page html1 = getPage(webClient, url1, HttpMethod.POST, paramsList, true);
			String json1 = html1.getWebResponse().getContentAsString();
			System.out.println(month+"call---json--------------------"+json1);

//			String url4 = "http://cq.189.cn/new-bill/bill_XDCX_Head";
//			Page html4 = getPage(webClient, url4, HttpMethod.POST, null, false);
//			String json4 = html4.getWebResponse().getContentAsString();
//			System.out.println(json4);

			String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("page", "1"));
			paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
			// paramsList2.add(new NameValuePair("rows", "10"));

			Page html = getPage(webClient, url, HttpMethod.POST, paramsList2, true);

			String json = html.getWebResponse().getContentAsString();

			System.out.println(json);
			JSONObject jsonObj = JSONObject.fromObject(json);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String callMobile = jsonObj1.getString("对方号码");
				String callType = jsonObj1.getString("呼叫类型");
				String callTime = jsonObj1.getString("起始时间");
				String callTimeCost = jsonObj1.getString("通话时长（秒）");
				String callFee = jsonObj1.getString("费用（元）");
				String callStyle = jsonObj1.getString("通话类型");
				String callArea = jsonObj1.getString("使用地点");
				String inpackage = jsonObj1.getString("是否在套餐内");

				TelecomChongqingCallRecord telecomChongqingCallRecord = new TelecomChongqingCallRecord("", callMobile,
						callType, callTime, callTimeCost, callFee, callStyle, callArea, inpackage);
				System.out.println(telecomChongqingCallRecord.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String cookie111 = CommonUnit.transcookieToJson(webClient);
//		System.out.println(cookie111);
		System.out.println("------------------通话--------------------------------------------------");
		System.out.println(month + "--------" + beginTime + "------" + endTime);
		return webClient;
	}

	private static WebClient sendcode(WebClient webClient) throws Exception {
		

		String url4 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
		HtmlPage html4 = getHtmlPage(webClient, url4, null);
		String json4 = html4.getWebResponse().getContentAsString();
		
		
		String url2 = "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();
		
		
//		String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//		Page html3 = getPage(webClient, url3, HttpMethod.POST, null, false);
//		String json3 = html3.getWebResponse().getContentAsString();
		
		String cookie111 = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieString02031273---------------------->" + cookie111);
		
		

		String url = "http://cq.189.cn/new-bill/bill_DXYZM";

		Page html = getPage(webClient, url, HttpMethod.POST, null, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		String errorDescription = "";
		JSONObject jsonObj = JSONObject.fromObject(json);
		if (jsonObj.has("errorCode")) {
			String errorCode = jsonObj.getString("errorCode");
			if ("0".equals(errorCode)) {
				System.out.println("发送成功");
			} else {
				if (jsonObj.has("errorDescription")) {
					errorDescription = jsonObj.getString("errorDescription");
					System.out.println("发送失败" + errorDescription);
				}
				System.out.println("发送失败");
			}
		}
		
		
		String cookieString = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieStringsendcode---------------------->" + cookieString);

		
		return webClient;
	}

	/**
	 * 验证tname
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient verificationTname(WebClient webClient, String tname) throws Exception {

		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();

		// String url1 =
		// "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq";
		// HtmlPage html1 = getHtmlPage(webClient, url1, null);
		// HtmlTextInput username = (HtmlTextInput)
		// html1.getFirstByXPath("//input[@id='tname']");
		// HtmlTextInput id_card = (HtmlTextInput)
		// html1.getFirstByXPath("//input[@id='id_card']");
		// username.setText(tname);
		// id_card.reset();
		// HtmlSpan sqan = (HtmlSpan)
		// html1.getFirstByXPath("//span[@id='xingm']");
		// String text = sqan.asText();
		// System.out.println(text);
		// String json1 = html1.getWebResponse().getContentAsString();

		// String url = "http://cq.189.cn/new-bill/bill_SMZ?tname=%E6%8C%AF";
		String url = "http://cq.189.cn/new-bill/bill_SMZ";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		// tname = URLEncoder.encode(tname, "UTF-8");
		tname = tname.substring(1);
		paramsList.add(new NameValuePair("tname", tname));
		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, true);

		String json = html.getWebResponse().getContentAsString();

		JSONObject jsonObj = JSONObject.fromObject(json);

		String sm = "0";
		String xm = "0";

		if (jsonObj.has("sm")) {
			sm = jsonObj.getString("sm");
		}

		if (jsonObj.has("xm")) {
			xm = jsonObj.getString("xm");
		}
		if (sm.equals("1") && xm.equals("1")) {
			System.out.println("验证tname失败：客户姓名   信息不正确，请仔细核对后再输入");
		} else {
			System.out.println("验证tname成功");
		}
		System.out.println("sm----------" + sm);
		System.out.println("xm----------" + xm);

		return webClient;
	}

	/**
	 * 验证idcard inputPassword.reset();
	 * 
	 * @param webClient
	 * @param idcard
	 * @return
	 * @throws Exception
	 */
	private static WebClient verificationIdcard(WebClient webClient, String idcard) throws Exception {

		String sm = "0";
		String sfz = "0";
		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://cq.189.cn/new-bill/bill_SMZ";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		idcard = idcard.substring(idcard.length() - 6);
		paramsList.add(new NameValuePair("idcard", idcard));

		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		JSONObject jsonObj = JSONObject.fromObject(json);
		if (jsonObj.has("sm")) {
			sm = jsonObj.getString("sm");
		}

		if (jsonObj.has("sfz")) {
			sfz = jsonObj.getString("sfz");
		}

		if (sm.equals("2") && sfz.equals("2")) {
			System.out.println("验证idcard失败：证件号码   信息不正确，请仔细核对后再输入");
		} else {
			System.out.println("验证idcard成功");
		}

		System.out.println("sm----------" + sm);
		System.out.println("sfz----------" + sfz);
		return webClient;
	}

	/**
	 * 重庆在用业务-----套餐信息
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient yewutaocan(WebClient webClient) throws Exception {

		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://cq.189.cn/new-bill/getAccept";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNbr", "17723292676"));
		paramsList.add(new NameValuePair("productId", "208511296"));

		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		JSONArray jsonarray = JSONArray.fromObject(json);

		if (null != jsonarray && jsonarray.size() > 0) {
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String offerCompName = jsonObj1.getString("offerCompName");
				JSONArray ywxq = JSONArray.fromObject(jsonObj1.getString("ywxq"));
				for (Object object2 : ywxq) {
					String favourName = JSONObject.fromObject(object2).getString("favourName");
					String favourEffTime = JSONObject.fromObject(object2).getString("favourEffTime");

					System.out.println(offerCompName + "---------" + favourName + "-------" + favourEffTime);
				}

			}

		}

		return webClient;
	}

	/**
	 * 重庆在用业务----已订购的基础功能
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient yewujichu(WebClient webClient) throws Exception {

		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://cq.189.cn/new-bill/getZZYW";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNbr", "17723292676"));
		paramsList.add(new NameValuePair("productId", "208511296"));
		paramsList.add(new NameValuePair("type", "zzyw"));
		paramsList.add(new NameValuePair("page", "1"));
		paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		JSONObject jsonObj = JSONObject.fromObject(json);
		String rows = jsonObj.getString("rows");
		JSONArray jsonarray = JSONArray.fromObject(rows);
		for (Object object : jsonarray) {
			JSONObject jsonObj1 = JSONObject.fromObject(object);
			String serv_product_name = jsonObj1.getString("serv_product_name");
			System.out.println(serv_product_name);
		}

		return webClient;
	}

	/**
	 * 重庆在用业务----已订购的增值业务
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient yewuzengzhi(WebClient webClient) throws Exception {

		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://cq.189.cn/new-bill/getKXB";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNbr", "17723292676"));
		paramsList.add(new NameValuePair("productId", "208511296"));
		paramsList.add(new NameValuePair("type", "kxb"));
		paramsList.add(new NameValuePair("page", "1"));
		paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		JSONObject jsonObj = JSONObject.fromObject(json);
		String rows = jsonObj.getString("rows");
		JSONArray jsonarray = JSONArray.fromObject(rows);
		for (Object object : jsonarray) {
			JSONObject jsonObj1 = JSONObject.fromObject(object);
			String serv_product_name = jsonObj1.getString("serv_product_name");
			System.out.println(serv_product_name);
		}

		return webClient;
	}

	private static WebClient jifenjiexi(WebClient webClient) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\qqq111.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		Elements link1 = doc.getElementsByTag("table");

		if (link1.size() > 0) {
			Elements link2 = link1.get(0).getElementsByTag("tr");
			if (link2.size() > 1) {
				String type = link2.get(0).getElementsByTag("th").get(0).text();
				String integral = link2.get(1).getElementsByTag("td").get(0).text();
				System.out.println(type + "--------" + integral);
				String type1 = link2.get(0).getElementsByTag("th").get(1).text();
				String integral1 = link2.get(1).getElementsByTag("td").get(1).text();
				System.out.println(type1 + "--------" + integral1);
				String type2 = link2.get(0).getElementsByTag("th").get(2).text();
				String integral2 = link2.get(1).getElementsByTag("td").get(2).text();
				System.out.println(type2 + "--------" + integral2);
			}
		}

		return webClient;
	}

	/**
	 * 积分
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient jifen(WebClient webClient) throws Exception {

		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02061287";
		HtmlPage html = getHtmlPage(webClient, url, null);
		String json = html.getWebResponse().getContentAsString();

		String url1 = "http://cq.189.cn/new-integral/index.htm?fastcode=02061287&cityCode=cq";
		HtmlPage html1 = getHtmlPage(webClient, url1, null);
		String json1 = html1.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(json1);
		Elements link1 = doc.getElementsByTag("table");

		if (link1.size() > 0) {
			Elements link2 = link1.get(0).getElementsByTag("tr");
			if (link2.size() > 1) {
				String type = link2.get(0).getElementsByTag("th").get(0).text();
				String integral = link2.get(1).getElementsByTag("td").get(0).text();
				System.out.println(type + "--------" + integral);
				String type1 = link2.get(0).getElementsByTag("th").get(1).text();
				String integral1 = link2.get(1).getElementsByTag("td").get(1).text();
				System.out.println(type1 + "--------" + integral1);
				String type2 = link2.get(0).getElementsByTag("th").get(2).text();
				String integral2 = link2.get(1).getElementsByTag("td").get(2).text();
				System.out.println(type2 + "--------" + integral2);
			}
		}

		return webClient;
	}

	/**
	 * 充值缴费
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient chongzhi(WebClient webClient) throws Exception {

		String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02051285";
		HtmlPage html1 = getHtmlPage(webClient, url1, null);
		String json1 = html1.getWebResponse().getContentAsString();

		try {
			String url = "http://cq.189.cn/new-pay/allPayQueryPage";
			System.out.println(url);
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sTime", "2017-03-01"));
			paramsList.add(new NameValuePair("eTime", "2017-09-11"));
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
			Page html = getPage(webClient, url, HttpMethod.GET, paramsList, false);
			String json = html.getWebResponse().getContentAsString();
			JSONObject jsonObj = JSONObject.fromObject(json);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String time = jsonObj1.getString("time");
				System.out.print(time);
				System.out.print("---------");
				String order = jsonObj1.getString("order");
				System.out.print(order);
				System.out.print("---------");
				String state = jsonObj1.getString("state");
				System.out.print(state);
				System.out.print("---------");
				String money = jsonObj1.getString("money");
				System.out.print(money);
				System.out.print("---------");
				String from = jsonObj1.getString("from");
				System.out.print(from);
				System.out.print("---------");
				String type = jsonObj1.getString("type");
				System.out.println(type);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return webClient;
	}

	/**
	 * 账单
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient zhangdan(WebClient webClient) throws Exception {

		String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031272";
		HtmlPage html1 = getHtmlPage(webClient, url1, null);
		String json1 = html1.getWebResponse().getContentAsString();

		String url2 = "http://cq.189.cn/new-bill/bill_ZZDCX";
		List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
		paramsList2.add(new NameValuePair("accNbr", "17723292676"));
		paramsList2.add(new NameValuePair("productId", "208511296"));
		paramsList2.add(new NameValuePair("month", "2017-05"));
		paramsList2.add(new NameValuePair("queryType", "2"));
		Page html2 = getPage(webClient, url2, HttpMethod.POST, paramsList2, false);
		String json2 = html2.getWebResponse().getContentAsString();

		try {
			String url = "http://cq.189.cn/new-bill/bill_ZDCX";
			System.out.println(url);
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", "99999"));
			Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);
			String json = html.getWebResponse().getContentAsString();
			JSONObject jsonObj = JSONObject.fromObject(json);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String billItem = jsonObj1.getString("billItem");
				String billAmount = jsonObj1.getString("billAmount");
				System.out.println(billItem);
				System.out.println(billAmount);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return webClient;
	}

	/**
	 * 余额信息
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient ye(WebClient webClient) throws Exception {

		// String url1 =
		// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203&cityCode=cq";
		String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";

		HtmlPage html1 = getHtmlPage(webClient, url1, null);
		String json1 = html1.getWebResponse().getContentAsString();

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNbr", "17723292676"));
		paramsList.add(new NameValuePair("productId", "208511296"));
		try {
			String url = "http://cq.189.cn/new-bill/getYe";
			System.out.println(url);
			Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);
			String json = html.getWebResponse().getContentAsString();
			JSONArray jsonarray = JSONArray.fromObject(json);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String time = jsonObj1.getString("time");
				String money = jsonObj1.getString("money");
				String type = jsonObj1.getString("type");
				System.out.println(time);
				System.out.println(money);
				System.out.println(type);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			String url = "http://cq.189.cn/new-bill/getSSHF";
			System.out.println(url);
			Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);
			String json = html.getWebResponse().getContentAsString();
			JSONArray jsonarray = JSONArray.fromObject(json);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String time = jsonObj1.getString("time");
				String money = jsonObj1.getString("money");
				String type = jsonObj1.getString("type");
				System.out.println(time);
				System.out.println(money);
				System.out.println(type);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return webClient;
	}

	/**
	 * 重庆用户套餐使用情况
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	private static WebClient chanpin(WebClient webClient) throws Exception {

		// String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do";
		// HtmlPage html3 = getHtmlPage(webClient, url3, null);
		// String json3 = html3.getWebResponse().getContentAsString();

		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();

		// String url5 =
		// "http://cq.189.cn/new-account/index?fastcode=0202&cityCode=cq";
		// HtmlPage html5 = getHtmlPage(webClient, url5, null);
		// String json5 = html5.getWebResponse().getContentAsString();

		String url = "http://cq.189.cn/new-bill/getSYTC";

		// String url =
		// "http://cq.189.cn/new-bill/getSYTC?accNbr=17723292676&productId=208511296&queryMonth=2017-09-01+00%3A00%3A00";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNbr", "17723292676"));
		paramsList.add(new NameValuePair("productId", "208511296"));
		paramsList.add(new NameValuePair("queryMonth", "2017-09-01 00:00:00"));

		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		JSONArray jsonarray = JSONArray.fromObject(json);

		// JSONObject jsonObj = JSONObject.fromObject(json);
		// getJSONObject(\"listPaymentHistory").
		// jsonObj.getString("listPaymentHistory");
		// JSONArray jsonarray = jsonObj.getJSONArray("proList");
		if (null != jsonarray && jsonarray.size() > 0) {
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String totalDesc = jsonObj1.getString("totalDesc");
				String disctDealedAmount = jsonObj1.getString("disctDealedAmount");
				String disctUndealedAmount = jsonObj1.getString("disctUndealedAmount");
				String disctAmount = jsonObj1.getString("disctAmount");
				String disctUnit = jsonObj1.getString("disctUnit");
				String expDate = jsonObj1.getString("expDate");
				String carryoverAmount = "";
				String month = "";
				String sm = "";
				try {
					if (jsonObj1.has("carryoverAmount")) {
						carryoverAmount = jsonObj1.getString("carryoverAmount");
						month = jsonObj1.getString("month");
						sm = jsonObj1.getString("sm");
					}
				} catch (Exception e) {

				}
				System.out.print(totalDesc);
				System.out.print("-----");
				System.out.print(disctDealedAmount);
				System.out.print("-----");
				System.out.print(disctUndealedAmount);
				System.out.print("-----");
				System.out.print(disctAmount);
				System.out.print("-----");
				System.out.print(disctUnit);
				System.out.print("-----");
				System.out.print(expDate);
				System.out.print("-----");
				System.out.print(month);
				System.out.print("-----");
				System.out.print(carryoverAmount);
				System.out.print("-----");
				System.out.println(sm);
			}

		}

		return webClient;
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
				System.out.println("login---cookieString---------------------->" + cookieString);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return webClient;

	}

	private static void parserUserInfo() {

		File file = new File("C:\\Users\\Administrator\\Desktop\\SS\\dianxin\\shanxi\\user.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);

		String name = getNextLabelByKeyword(doc, "客户名称");
		System.out.println(name);

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

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList,
			Boolean code) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		// webRequest.setAdditionalHeader("Accept", "*/*");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "15");
		// webRequest.setAdditionalHeader("Content-Type",
		// "application/x-www-form-urlencoded; charset=UTF-8");
		// webRequest.setAdditionalHeader("Host", "cq.189.cn");
		// webRequest.setAdditionalHeader("Origin", "http://cq.189.cn");
		// webRequest.setAdditionalHeader("Referer",
		// "http://cq.189.cn/new-bill/bill_xd?fastcode=02031273&cityCode=cq");
		// webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT
		// 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
		// Chrome/60.0.3112.113 Safari/537.36");
		// webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		// webRequest.setAdditionalHeader("Cookie",
		// "JSESSIONID=1F42394CC52F3FF08121833664C904A5; insight_v=20151104;
		// lvid=a543b2792889e9c8239477370fcb1791; nvid=1;
		// _gscu_1708861450=03281633futekl98; i_vnum=1; svid=3E66F4E7AA7F62E6;
		// ijg=1503282281843;
		// Hm_lvt_9c25be731676bc425f242983796b341c=1503371257;
		// cn_1260051947_dplus=%7B%22distinct_id%22%3A%20%2215e0db515b231d-06e99ce3e532fd-464d0128-15f900-15e0db515b32a6%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201503546325%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201503546325%7D%7D;
		// UM_distinctid=15e0db515b231d-06e99ce3e532fd-464d0128-15f900-15e0db515b32a6;
		// dqmhIpCityInfos=%E5%8C%97%E4%BA%AC%E5%B8%82+%E8%81%94%E9%80%9A%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83;
		// flag=2; com.crunii.ecsc.webkey=c88d299041a54a1fb9876136167832a6;
		// CNZZDATA4834284=cnzz_eid%3D1621584804-1504851440-http%253A%252F%252Fcq.189.cn%252F%26ntime%3D1504851440;
		// Hm_lvt_cd4b0404a9fec314e381e78f93149c3c=1504859261,1505112407,1505117523,1505182273;
		// flow_analysis_key=cfb480b21c8de9f6721615f6207ee83e;
		// loginCode=17723292676; trkHmPageName=%2Fcq%2F; trkHmCoords=0;
		// trkHmCity=0; trkHmLinks=0;
		// CNZZDATA2202448=cnzz_eid%3D1826527078-1504683071-http%253A%252F%252Fcq.189.cn%252F%26ntime%3D1505271869;
		// ecsc_session_uuid=\"b94fa3c70a654df5a9f57003c5309bf2:1505272111907\";
		// login_tab_idx=1; _ecsc_area_key=1088;
		// userId=201%7C20170100000004327060;
		// .ybtj.189.cn=8501EFCF4CB88A7B29DDD34014C85342; SHOPID_COOKIEID=10004;
		// com.cqcis.ecsc.sso.client.filter.user=\"44BBA3D4DE538C51C960F4A5C04EC324:1505298898534\";
		// s_sq=%5B%5BB%5D%5D; s_cc=true;
		// s_fid=0749E6A704C1A714-21520211E5191A34; cityCode=cq;
		// loginStatus=non-logined; trkId=5301ACE3-31DB-4A37-9A8A-0EE0BDAB4FB2;
		// trkHmClickCoords=277%2C363%2C877");

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}

		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();

		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("th:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	/**
	 * 账单 获取六个月内的年月字符串 比如本月九月返回8，7，6，5，4，3
	 * 
	 * @return
	 */
	public static List<String> getDecade() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 6; i++) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
			list.add(dateFormat.format(calendar.getTime()));
		}
		return list;
	}

	/**
	 * 套餐使用情况 获取六个月内的年月字符串 比如本月九月返回9，8，7，6，5，4
	 * 
	 * @return
	 */
	public static List<String> getDecade2() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 6; i++) {
			list.add(dateFormat.format(calendar.getTime()));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		}
		return list;
	}

	/**
	 * 通话记录 获取month个月内的年月字符串 比如本月九月返回9，8，7，6，5，4，3
	 * 
	 * @return
	 */
	public static List<String> getDecade3(int month) {
		List<String> decade = getDecade3(7);
		for (String string : decade) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(StrToDate(string));
			calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
			String beginTime = format.format(calendar.getTime());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			String endTime = format.format(calendar.getTime());

		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= month; i++) {
			list.add(dateFormat.format(calendar.getTime()));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		}
		return list;
	}

	public static void getDecade3() {
		List<String> decade = getDecade3(7);
		for (String string : decade) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(StrToDate(string));
			calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
			String beginTime = format.format(calendar.getTime());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			String endTime = format.format(calendar.getTime());
		}
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
}
