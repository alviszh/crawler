package org.common.microservice.eureka.china.unicom;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

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
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

/**
 * @author zhenZ
 * @createtime 2017年6月8日 上午10:02:20 北京移动通话记录爬取（测试类）
 */
public class dianxintest {

	private static Set<Cookie> cookies;

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

		url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage html3 = getHtml(url, webClient);

		url = "http://bj.189.cn/service/account/lastLoginTime.parser";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		System.out.println("====================" + (System.currentTimeMillis() / 1000));
		paramsList.add(new NameValuePair("time", (System.currentTimeMillis() / 1000) + ""));
		try {
			Page page = gethtmlPost(webClient, paramsList, url);

			System.out.println("===================================================================");

			System.out.println(page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}

		url = "http://bj.189.cn/iframe/feequery/billQueryIndex.parser?fastcode=01390637";

		try {
			HtmlPage html4 = getHtml(url, webClient);
			System.out.println("==================================================");
			System.out.println(html4.asXml());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(html3.asXml());

		/*
		 * url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		 * 
		 * HtmlPage html4 = getHtml(url, webClient);
		 */
		// System.out.println(html4.asXml());

		//callthrem(webClient);
		//pay(webClient);
		//integra(webClient);
		String code = getSMSCode(webClient);
		setSMSCoide( webClient,"code");
		calltherm(webClient);
		/*
		 * url = "http://bj.189.cn/iframe/feequery/billDetailQuery.action"; //
		 * WebClient webClient = WebCrawler.getInstance().getNewWebClient(); //
		 * webClient = addcookie(webClient, taskMobile);
		 * 
		 * paramsList = new ArrayList<NameValuePair>(); paramsList.add(new
		 * NameValuePair("requestFlag", "synchronization")); paramsList.add(new
		 * NameValuePair("billDetailType", "1")); paramsList.add(new
		 * NameValuePair("qryMonth", "2017年08月")); paramsList.add(new
		 * NameValuePair("startTime", "1")); paramsList.add(new
		 * NameValuePair("endTime", "10")); paramsList.add(new
		 * NameValuePair("accNum", "13366777357")); paramsList.add(new
		 * NameValuePair("billPage", "1"));
		 * 
		 * try { Page page = gethtmlPost(webClient,paramsList,url);
		 * System.out.println(page.getWebResponse().getContentAsString()); }
		 * catch (Exception e) {
		 * 
		 * e.printStackTrace(); }
		 */
		/*
		 * url =
		 * "http://bj.189.cn/iframe/custservice/modifyUserInfo.action?indexPage=INDEX3";
		 * 
		 * Page page = getHtml(url, html3.getWebClient());
		 * 
		 * System.out.println("====="+page.getWebResponse().getContentAsString()
		 * );
		 */

	}
	
	public static void integra(WebClient webClient) {

		String url = "http://bj.189.cn/iframe/custquery/qryPointHistory.parser?indexPage=INDEX3&fastcode=01410646&cityCode=bj";
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtml(url, webClient);
			System.out.println(page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void pay(WebClient webClient) {

		String url = "http://bj.189.cn/iframe/local/queryPaymentRecord.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNum", "13366777357"));
		paramsList.add(new NameValuePair("requestFlag", "synchronization"));
		paramsList.add(new NameValuePair("paymentHistoryQueryIn.startDate", "2017-03-01"));
		paramsList.add(new NameValuePair("paymentHistoryQueryIn.endDate", "2017-03-31"));


		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = gethtmlPost(webClient, paramsList, url);
			System.out.println(page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void calltherm(WebClient webClient) {


		String url = "http://bj.189.cn/iframe/feequery/billDetailQuery.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNum", "13366777357"));
		paramsList.add(new NameValuePair("requestFlag", "synchronization"));
		paramsList.add(new NameValuePair("billDetailType", "1"));
		paramsList.add(new NameValuePair("qryMonth", "2017年07月"));
		paramsList.add(new NameValuePair("startTime", "1"));
		paramsList.add(new NameValuePair("endTime", "31"));
		paramsList.add(new NameValuePair("billPage", "1"));



		/*requestFlag:synchronization
		billDetailType:1
		qryMonth:2017年08月
		startTime:1
		accNum:13366777357
		endTime:14*/
		
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = gethtmlPost(webClient, paramsList, url);
			System.out.println(page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
		/*
		 * accNum:13366777357 requestFlag:synchronization billCycle:201706
		 */
	}

	public static String getSMSCode(WebClient webClient){
		//http://bj.189.cn/iframe/feequery/smsRandCodeSend.action
		
		String url = "http://bj.189.cn/iframe/feequery/smsRandCodeSend.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNum", "13366777357"));

		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = gethtmlPost(webClient, paramsList, url);
			
			System.out.println("===========getSMSCode=============="+page.getWebResponse().getContentAsString());
		
		    Gson gs = new Gson();

		    SMSCodejson jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(), SMSCodejson.class);
		    
		    return jsonObject.getSRandomCode();
		
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void setSMSCoide(WebClient webClient,String code){

		String url = "http://bj.189.cn/iframe/feequery/detailValidCode.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("accNum", "13366777357"));
		paramsList.add(new NameValuePair("requestFlag", "asynchronism"));
		paramsList.add(new NameValuePair("randCode", code));

		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = gethtmlPost(webClient, paramsList, url);
			System.out.println("===========setSMSCoide=============="+page.getWebResponse().getContentAsString());
		} catch (Exception e) {

			e.printStackTrace();
		}
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

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	@SuppressWarnings("unused")
	private static void addCookies(WebClient webClient, Set<Cookie> cookies) {

		for (Cookie cookie : cookies) {
			if (StringUtils.isEmpty(cookie.getName())) {
				System.out.println("详情：" + cookie.getName() + ":" + cookie.getValue());
				continue;
			}
			webClient.getCookieManager().addCookie(cookie);
		}
	}

}
