package com.test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBusinessResult;
import com.module.htmlunit.WebCrawler;
import app.bean.TelecomYunNanCanShuAccidBean;
import app.bean.TelecomYunNanCanUserIdShuBean;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseYunNan;
import app.unit.TeleComCommonUnit;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.Page;
public class yunnantest {
	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\tel2.xls";
	public static void main(String[] args) throws Exception {
		/*
		 * try { getAccid(null); } catch (Exception e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		/*WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		webClient2 = POIUnit.addCookie(webClient2, cookiefile, "yn.189.cn");*/
		try {
			WebClient webClient = TelecomLogin.login("13312530250", "211314");
			// WebClient webClientadd =
			// WebCrawler.getInstance().getNewWebClient();
			// webClientadd = POIUnit.addCookie(webClientadd, cookiefile, "yn.189.cn");
			if (webClient != null) {
				String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01941226";
				Page page = TelecomLogin.getHtml2(url, webClient);
				//taocantest(webClient);
				//Set<Cookie> cookies4 = webClient2.getCookieManager().getCookies();
				/*for (Cookie cookie4 : cookies4) {
					if(cookie4.getName().indexOf("JSESSIONID")!=-1){
						continue;
					}
					webClient.getCookieManager().addCookie(cookie4);
					System.out.println("发送请求的cookie：" + cookie4.toString());
				}*/
				//zhangdantest(webClient);
				// getAccid(webClient);
				webClient = getphonecode(webClient);
				
				if(webClient !=null){
					String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
					//webClient = TelecomLogin.addcookieForSMSAndCall(webClient);
					webClient = setphonecode(webClient, valicodeStr);
					// tonghuatest(webClient);
					// webClient = page.getEnclosingWindow().getWebClient();
					// tonghuatest(webClient);
					//taocantest(webClient);
				}
				
			} else {
				System.out.println("登录失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static TelecomYunNanCanUserIdShuBean getUserIdunit(WebClient webClient) throws Exception {
		File input = new File("C:\\Users\\Administrator\\Desktop\\html.txt");
		Document doc = Jsoup.parse(input, "UTF-8");
		/*
		 * //String url =
		 * "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=9A001&fastcode=01941226&cityCode=yn";
		 * 
		 * Page page = TeleComCommonUnit.getHtml(url, webClient);
		 * 
		 * Document doc =
		 * Jsoup.parse(page.getWebResponse().getContentAsString());
		 */
		System.out.println(doc);
		Elements screles = doc.select("script");
		for (Element screle : screles) {
			System.out.println("=========" + screle.text());
			if (screle.toString().indexOf("13312530250") != -1) {
				System.out.println("===============" + screle);
				String areacode = screle.toString().split(",")[1];
				String weizhi = screle.toString().split(",")[2];
				String weizhi2 = screle.toString().split(",")[4];
				String userid = screle.toString().split(",")[5];
				TelecomYunNanCanUserIdShuBean telecomYunNanCanShuBean = new TelecomYunNanCanUserIdShuBean();
				telecomYunNanCanShuBean.setAreacode(areacode);
				telecomYunNanCanShuBean.setWeizhi(weizhi);
				telecomYunNanCanShuBean.setWeizhi2(weizhi2);
				telecomYunNanCanShuBean.setUserid(userid);
				System.out.println("===afas=====" + telecomYunNanCanShuBean.toString());
				return telecomYunNanCanShuBean;
			}
		}
		return null;
	}
	public static TelecomYunNanCanUserIdShuBean getAccid(WebClient webClient) throws Exception {
		/*
		 * File input = new File("C:\\Users\\Administrator\\Desktop\\html.txt");
		 * Document doc = Jsoup.parse(input, "UTF-8");
		 */
		String url = "http://yn.189.cn/service/jt/bill/qry_mainjt2.jsp?fastcode=01941227&cityCode=yn";
		Page page = TeleComCommonUnit.getHtml(url, webClient);
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		System.out.println(doc);
		Element screle = doc.select("select#qryNum").select("option").first();
		System.out.println("==========" + screle);
		TelecomYunNanCanShuAccidBean telecomYunNanCanShuAccidBean = new TelecomYunNanCanShuAccidBean();
		telecomYunNanCanShuAccidBean.setProdid(screle.attr("prodid"));
		telecomYunNanCanShuAccidBean.setAccid(screle.attr("accid"));
		telecomYunNanCanShuAccidBean.setAeracode(screle.attr("aeracode"));
		telecomYunNanCanShuAccidBean.setProdtype(screle.attr("prodtype"));
		System.out.println(telecomYunNanCanShuAccidBean.toString());
		return null;
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
		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_hislist_em.jsp?"
				+ "TEMPLATE_ID=1000&BILLING_CYCLE=201708&USER_ID=710112046828&NUM=13312530250&AREA_CODE=0871";
/*		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("USER_ID", "710112046828"));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201708"));
		paramsList.add(new NameValuePair("TEMPLATE_ID", "1010"));*/
		//Page page = TelecomLogin.gethtmlPost(webClient, paramsList, url);
		Page page = TelecomLogin.getHtml2(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());
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
		WebParamTelecom<TelecomYunNanBusinessResult> webParamTelecom = TelecomParseYunNan.business_parse(page.getWebResponse().getContentAsString());
	
		List<TelecomYunNanBusinessResult> list = webParamTelecom.getList();
		for(TelecomYunNanBusinessResult result : list){
			System.out.println(result.toString());
		}
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
	public static WebClient getphonecode(WebClient webClient) throws Exception {
		String url = "http://yn.189.cn/public/postValidCode.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("LOGIN_TYPE", "21"));
		paramsList.add(new NameValuePair("OPER_TYPE", "CR0"));
		paramsList.add(new NameValuePair("RAND_TYPE", "004"));
		Page page = TelecomLogin.gethtmlPost3(webClient, paramsList, url);
		System.out.println("=========" + page.getWebResponse().getContentAsString());
		Set<Cookie> cookies4 = webClient.getCookieManager().getCookies();
		for (Cookie cookie4 : cookies4) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}
		if (page.getWebResponse().getContentAsString().indexOf("0") == -1) {
			System.out.println("短信发送失败,电信接口波动,请稍后重试");
			return null;
		}
		if (page.getWebResponse().getContentAsString().indexOf("您今天内获取取随机密码已超出限制") != -1) {
			System.out.println("-196910 对不起，您今天内获取取随机密码已超出限制，您的号码已被锁定！请明天再试！ (错误日志序号：112023657)");
			return null;
		}
		return webClient;
	}
	public static void getphonecode2(WebClient webClient) throws Exception {
		String url2 = "	http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01941229";
		HtmlPage page2 = (HtmlPage) TelecomLogin.getHtm4(url2, webClient);
		System.out.println("=========" + page2.getWebResponse().getContentAsString());
		String url = "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn";
		HtmlPage page = (HtmlPage) TelecomLogin.getHtm4(url, webClient);
		System.out.println("=========" + page.getWebResponse().getContentAsString());
		HtmlTextInput username = (HtmlTextInput) page.getFirstByXPath("//*[@id='NAME']");
		HtmlTextInput cardno = (HtmlTextInput) page.getFirstByXPath("//*[@id='CUSTCARDNO']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) page.getFirstByXPath("//*[@id='PROD_PASS']");
		HtmlElement button = (HtmlElement) page.getFirstByXPath("//*[@id='MotoText']/a");
		username.setText("韩译兴");
		cardno.setText("130984199210023312");
		passwordInput.setText("211314");
		page = button.click();
		System.out.println(page.asXml());
	}
	public static WebClient setphonecode(WebClient webClient, String code) throws Exception {
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		/*String url = "http://yn.189.cn/public/custValid.jsp?_FUNC_ID_=WB_PAGE_PRODPASSWDQRY"
				+ "&NAME=%E9%9F%A9%E8%AF%91%E5%85%B4&CUSTCARDNO=130984199210023312&PROD_PASS=211314&MOBILE_CODE="
						+ code.trim()
						+ "&NAME=%E9%9F%A9%E8%AF%91%E5%85%B4&CUSTCARDNO=130984199210023312";*/
		//http://yn.189.cn/public/custValid.jsp?_FUNC_ID_=WB_PAGE_PRODPASSWDQRY&NAME=%E9%9F%A9%E8%AF%91%E5%85%B4&CUSTCARDNO=130984199210023312&PROD_PASS=211314&MOBILE_CODE=106167&NAME=%E9%9F%A9%E8%AF%91%E5%85%B4&CUSTCARDNO=130984199210023312

		String url = "http://yn.189.cn/public/custValid.jsp?_FUNC_ID_=WB_PAGE_PRODPASSWDQRY" + "&NAME="
				+ URLEncoder.encode("韩译兴") + "&CUSTCARDNO=" +"130984199210023312"
				+ "&PROD_PASS=" +"211314" + "&MOBILE_CODE="
				+ "235508" + "&NAME=" +  URLEncoder.encode("韩译兴")
				+ "&CUSTCARDNO=" + "130984199210023312";
		Page page = TelecomLogin.getHtml(url, webClient);
		System.out.println("=========" + page.getWebResponse().getContentAsString());
		url = "http://yn.189.cn/public/pwValid.jsp";
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
		
		
		
		//System.out.println("=========" + page.getWebResponse().getContentAsString());
		url = "http://yn.189.cn/public/query_realnameInfo.jsp?NUM=13312530250&AREA_CODE=&accNbrType=9";
		 page = TelecomLogin.getHtml2(url, webClient);
		System.out.println("=========" + page.getWebResponse().getContentAsString());
		url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_new.jsp";
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("PROD_NO", "4217"));
		page = TelecomLogin.gethtmlPost3(webClient, paramsList, url);
		
		url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp?NUM=13312530250&AREA_CODE=0871&CYCLE_BEGIN_DATE=&CYCLE_END_DATE=&BILLING_CYCLE=201709&QUERY_TYPE=30";
		/*paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
		paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
		paramsList.add(new NameValuePair("QUERY_TYPE", "30"));
		page = TelecomLogin.gethtmlPost(webClient, paramsList, url);*/
		page = TelecomLogin.getHtm3(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());
		Set<Cookie> cookies5 = webClient.getCookieManager().getCookies();
		
		url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp?NUM=13312530250&AREA_CODE=0871&CYCLE_BEGIN_DATE=&CYCLE_END_DATE=&BILLING_CYCLE=201709&QUERY_TYPE=10";
		/*paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", "13312530250"));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
		paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
		paramsList.add(new NameValuePair("BILLING_CYCLE", "201709"));
		paramsList.add(new NameValuePair("QUERY_TYPE", "30"));
		page = TelecomLogin.gethtmlPost(webClient, paramsList, url);*/
		page = TelecomLogin.getHtm3(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());
		for (Cookie cookie4 : cookies5) {
			System.out.println("发送请求的cookie：" + cookie4.toString());
		}
		TelecomParseYunNan.SMSThrem_parse(page.getWebResponse().getContentAsString());
		return webClient;
	}
}