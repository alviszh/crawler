package com.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;

public class qinghaitest {
	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\tel2.xls";

	public static void main(String[] args) {
		try {
			WebClient webClient = TelecomLogin.login("13312530250", "211314");
			// WebClient webClientadd =
			// WebCrawler.getInstance().getNewWebClient();
			// webClientadd = POIUnit.addCookie(webClientadd, cookiefile,
			// "yn.189.cn");
			if (webClient != null) {
				String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01941226";
				Page page = TelecomLogin.getHtml2(url, webClient);

				Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
				for (Cookie cookie4 : cookies4) {
					System.out.println("发送请求的cookie：" + cookie4.toString());
				}

				getphonecode(webClient);
				String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

				webClient = setphonecode(webClient, valicodeStr);
				tonghuatest(webClient);
				// webClient = page.getEnclosingWindow().getWebClient();
				//tonghuatest(webClient);
			} else {
				System.out.println("登录失败");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// http://yn.189.cn/service/jt/bill/actionjtv2/ifr_balance.jsp
	public static void yueertest(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/service/jt/bill/actionjtv2/ifr_balance.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("ACCT_ID", "419437674"));
		paramsList.add(new NameValuePair("PRODTYPE", "4"));
		paramsList.add(new NameValuePair("PRODNO", "4217"));
		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
		System.out.println(page.getWebResponse().getContentAsString());
	}

	public static void zhangdantest(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_hislist_em.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("USER_ID", "710112046828"));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201708"));
		paramsList.add(new NameValuePair("TEMPLATE_ID", "1010"));
		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
		System.out.println(page.getWebResponse().getContentAsString());
	}

	// 测试失败
	public static void tonghuatest(WebClient webClient2) throws Exception {
		
		WebClient webClient =  WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies4 = webClient2.getCookieManager().getCookies();
		for (Cookie cookie4 : cookies4) {
			if(cookie4.getName().indexOf("JSESSIONID")!=-1){
				webClient.getCookieManager().addCookie(cookie4);
			}
		}
		
		String url2 = "http://yn.189.cn/public/query_realnameInfo.jsp?NUM=13312530250&AREA_CODE=&accNbrType=9";
		Page page2 = TelecomLogin.getHtml(url2, webClient);
		// page = TelecomLogin.getHtml(url, webClient);
		System.out.println(page2.getWebResponse().getContentAsString());
		
		
		url2 = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_new.jsp?NUM=13312530250&AREA_CODE=0871&PROD_NO=4217";
		HtmlPage htmlPage = (HtmlPage) TelecomLogin.getHtml(url2, webClient);
		// page = TelecomLogin.getHtml(url, webClient);
	/*	System.out.println("asdfs="+htmlPage.getWebResponse().getContentAsString());
		
		
		HtmlElement loginButton = (HtmlElement) htmlPage.getFirstByXPath("//*[@id='query_model']/input");
		htmlPage = loginButton.click();
		System.out.println("==============================");
		System.out.println("======="+htmlPage.asXml());
		System.out.println("==============================");*/
		//*[@id="query_model"]/input
			
		/*String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp?NUM=13312530250&AREA_CODE=0871&CYCLE_BEGIN_DATE=&CYCLE_END_DATE=&BILLING_CYCLE=201709&QUERY_TYPE=10";

		Page page = TelecomLogin.getHtml(url, webClient);
		// page = TelecomLogin.getHtml(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());*/
		
		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp";
		List<NameValuePair>  paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
		paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
		paramsList.add(new NameValuePair("QUERY_TYPE", "10"));
	
		
		Page page = TelecomLogin.gethtmlPost4(webClient, paramsList, url);
		System.out.println(page.getWebResponse().getContentAsString());
	
		
		Set<Cookie> cookies5 = webClient.getCookieManager().getCookies();
		for (Cookie cookie4 : cookies5) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}
		/*
		 * Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
		 * Set<Cookie> cookiesadd =
		 * webClientadd.getCookieManager().getCookies(); for (Cookie cookie4 :
		 * cookies4) { System.out.println("登录cookie："+cookie4.toString());
		 * for(Cookie cookie4add : cookiesadd){
		 * if(cookie4.getName().equals(cookie4add.getName())){
		 * System.out.println(cookie4.toString()+"name相等=="+cookie4add.toString(
		 * )); } } //System.out.println("发送请求的cookie：" + cookie4.toString()); }
		 */
	}

	public static void jiaofeitest(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_userchargeList.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("USER_FLAG", ""));
		paramsList.add(new NameValuePair("NAME", ""));
		paramsList.add(new NameValuePair("NUM", ""));
		paramsList.add(new NameValuePair("CITYNO", "0871"));
		paramsList.add(new NameValuePair("FLAG", "1"));
		paramsList.add(new NameValuePair("S_AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("SERV_NAME", ""));
		paramsList.add(new NameValuePair("USER_FLAG", ""));
		paramsList.add(new NameValuePair("SERV_TYPE", ""));
		paramsList.add(new NameValuePair("SERV_NO", ""));
		paramsList.add(new NameValuePair("SERV_KIND", ""));
		paramsList.add(new NameValuePair("DESC_OPEN_TYPE", ""));
		paramsList.add(new NameValuePair("TB_SHOW_NUM", "13312530250"));
		paramsList.add(new NameValuePair("START_ASK_DATE", " 2017-08-29  "));
		paramsList.add(new NameValuePair("END_ASK_DATE", "2017-09-12"));

		/*
		 * paramsList.add(new NameValuePair("TB_SHOW_NUM", "13312530250"));
		 * paramsList.add(new NameValuePair("START_ASK_DATE", "2017-07-29"));
		 * paramsList.add(new NameValuePair("END_ASK_DATE", " 2017-09-12"));
		 * paramsList.add(new NameValuePair("FLAG", "0871")); paramsList.add(new
		 * NameValuePair("S_AREA_CODE", "0871"));
		 */
		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
		// Page page = TelecomLogin.getHtml(url, webClient);

		System.out.println(page.getWebResponse().getContentAsString());
	}

	public static void yonghuxinxitest(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/service/manage/my_selfinfo.jsp";

		Page page = TelecomLogin.getHtml(url, webClient);

		System.out.println(page.getWebResponse().getContentAsString());
	}

	public static void taocantest(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/service/manage/myprod_sm.jsp";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("BUREAUCODE", "0871"));
		paramsList.add(new NameValuePair("USER_PASSWD", ""));
		paramsList.add(new NameValuePair("ACCNBR", "13312530250"));
		paramsList.add(new NameValuePair("AuthFlag", "0"));
		paramsList.add(new NameValuePair("PROD_TYPE", "4"));

		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
		// Page page = TelecomLogin.getHtml(url, webClient);

		System.out.println(page.getWebResponse().getContentAsString());
	}

	public static void jifentest(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/jf/ifr_query_scores.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("PROD_NO", "4217"));
		paramsList.add(new NameValuePair("USER_NAME", ""));
		paramsList.add(new NameValuePair("ACCT_ID", "410004763"));

		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);

		// Page page = TelecomLogin.getHtml(url, webClient);

		System.out.println(page.getWebResponse().getContentAsString());
	}

	public static void jifentestchange(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/service/jf/parser/getBonusDetail_list.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("USER_ID", "710112046828"));
		paramsList.add(new NameValuePair("StartDate", "2017-01-01"));
		paramsList.add(new NameValuePair("EndDate", "2017-09-12"));

		Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);

		// Page page = TelecomLogin.getHtml(url, webClient);

		System.out.println(page.getWebResponse().getContentAsString());
	}
	
	public static void getphonecode(WebClient webClient) throws Exception {
				
		String url = "http://yn.189.cn/public/postValidCode.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("LOGIN_TYPE", "21"));
		paramsList.add(new NameValuePair("OPER_TYPE", "CR0"));
		paramsList.add(new NameValuePair("RAND_TYPE", "004"));
		Page page = TelecomLogin.gethtmlPost3(webClient, paramsList, url);
		System.out.println("========="+page.getWebResponse().getContentAsString());
	}
	
	public static WebClient setphonecode(WebClient webClient,String code) throws Exception {
		
		String url = "http://yn.189.cn/public/custValid.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("_FUNC_ID_", "WB_PAGE_PRODPASSWDQRY"));
		paramsList.add(new NameValuePair("NAME", "韩译兴"));
		paramsList.add(new NameValuePair("CUSTCARDNO", "130984199210023312"));
		paramsList.add(new NameValuePair("PROD_PASS", "211314"));
		paramsList.add(new NameValuePair("MOBILE_CODE", code.trim()));
		paramsList.add(new NameValuePair("NAME", "韩译兴"));
		paramsList.add(new NameValuePair("CUSTCARDNO", "130984199210023312"));
		Page page = TelecomLogin.gethtmlPost3(webClient, paramsList, url);
		System.out.println("========="+page.getWebResponse().getContentAsString());
		
		url = "http://yn.189.cn/public/pwValid.jsp";
	    paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("_FUNC_ID_", "WB_PAGE_PRODPASSWDQRY"));
		paramsList.add(new NameValuePair("NAME", "韩译兴"));
		paramsList.add(new NameValuePair("CUSTCARDNO", "130984199210023312"));
		paramsList.add(new NameValuePair("PROD_PASS", "211314"));
		paramsList.add(new NameValuePair("MOBILE_CODE", code.trim()));
		paramsList.add(new NameValuePair("ACC_NBR", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("LOGIN_TYPE", "21"));
		paramsList.add(new NameValuePair("PASSWORD", "211314"));
		paramsList.add(new NameValuePair("MOBILE_FLAG", "1"));
		paramsList.add(new NameValuePair("MOBILE_LOGON_NAME", "13312530250"));
		paramsList.add(new NameValuePair("MOBILE_CODE", code.trim()));
		paramsList.add(new NameValuePair("PROD_NO", "421"));
		 page = TelecomLogin.gethtmlPost3(webClient, paramsList, url);
		System.out.println("========="+page.getWebResponse().getContentAsString());
		
		return webClient;
	}
	
	
	
}
