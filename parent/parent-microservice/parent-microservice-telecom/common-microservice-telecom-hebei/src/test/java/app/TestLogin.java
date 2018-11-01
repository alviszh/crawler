package app;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.unit.TeleComCommonUnit;

public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception {
		
		//登录
		login();
		
		//获取个人信息所需的cookie
		String userUrlNeed = "http://www.189.cn/login/index.do";
		getHtml(userUrlNeed,webClient2,null);
		
		String index = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage page = (HtmlPage) getHtml(index,webClient2,null);
		
//		String url = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10006&toStUrl=http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1";
//		TeleComCommonUnit.getHtml(url, webClient2);
//		
//		HtmlElement a = (HtmlElement) page.getFirstByXPath("//a[@tree_id='2188']");
//		
//		Page city = a.click();
//		System.out.println(city.getWebResponse().getContentAsString());
		
		String smsUrl = "http://he.189.cn/service/transaction/postValidCode.jsp?"
				+ "reDo=Tue+Feb+27+2018+14%3A39%3A53+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)"
				+ "&OPER_TYPE=CR1&RAND_TYPE=006&PRODTYPE=2020966&MOBILE_NAME=18131003055&MOBILE_NAME_PWD=&NUM=18131003055&AREA_CODE=186&LOGIN_TYPE=21";
		getHtml(smsUrl,webClient2,null);
		
		//获取个人信息
//		String userUrl ="http://he.189.cn/service/manage/index_iframe.jsp";
////		getHtml(userUrl);
//		getHtml(userUrl,webClient2,null);
		
		//套餐
//		String taocanUrl = "http://he.189.cn/service/manage/prod_baseinfo_iframe.jsp";
//		Page taocanPage = getHtml(taocanUrl,webClient2,null);
//		parser(taocanPage);
/*		//缴费记录
		String payUrl = "http://he.189.cn/service/pay/userPayfeeHisList_iframe.jsp?START_ASK_DATE=2017-08-01&END_ASK_DATE=2017-09-06";
		getHtml(payUrl,webClient2,null);*/
		
/*		//账单
		String accountUrl = "http://he.189.cn/service/bill/action/bill_month_list_detail_iframe.jsp?ACC_NBR=13398649929&SERVICE_KIND=8&feeDate=201703&retCode=0000";
		Page page = getHtml(accountUrl,webClient2,null);
		parser(page);*/
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String sjmput = scanner.nextLine();
		
		
		//获取通话记录
		String callUrl = "http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp?ACC_NBR=18131003055&CITY_CODE=186&BEGIN_DATE=2017-11-01+00%3A00%3A00&END_DATE=2018-01-31+23%3A59%3A59&FEE_DATE=201801&SERVICE_KIND=8&retCode=0000&QUERY_FLAG=1&QUERY_TYPE_NAME=%E7%A7%BB%E5%8A%A8%E8%AF%AD%E9%9F%B3%E8%AF%A6%E5%8D%95&QUERY_TYPE=1&radioQryType=on&QRY_FLAG=1&ACCT_DATE=201801&ACCT_DATE_1=201802"
				+ "&sjmput="+sjmput;
		getHtml(callUrl,webClient2,null);
		
		//获取短信记录
		/*String msgUrl = "http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new_iframe.jsp?ACC_NBR=13398649929&CITY_CODE=187&BEGIN_DATE=2017-08-01+00%3A00%3A00&END_DATE=2017-08-31+23%3A59%3A59&FEE_DATE=201708&SERVICE_KIND=8&retCode=0000&QUERY_FLAG=1&QUERY_TYPE_NAME=%E7%A7%BB%E5%8A%A8%E7%9F%AD%E4%BF%A1%E8%AF%A6%E5%8D%95&QUERY_TYPE=2&radioQryType=on&QRY_FLAG=1&ACCT_DATE=201708&ACCT_DATE_1=201709";
		Page msgPage = getHtml(msgUrl,webClient2,null);
		parserMsg(msgPage);*/
		
		
		
		
	}
	

	private static void parserMsg(Page msgPage) {
		Document doc = Jsoup.parse(msgPage.getWebResponse().getContentAsString());
		Elements tbodys = doc.getElementsByTag("tbody");
		if(null != tbodys && tbodys.size()>0){
			Elements trs = tbodys.first().select("tr");	
			if(null != trs && trs.size()>0){
				Element tr = trs.first();
				System.out.println(tr.child(0).text());
				System.out.println(tr.child(1).text());
				System.out.println(tr.child(2).text());
				System.out.println(tr.child(3).text());
				System.out.println(tr.child(4).text());
				System.out.println(tr.child(5).text());
				System.out.println(tr.child(6).text());
				System.out.println(tr.child(7).text());
				System.out.println(tr.child(8).text());
				System.out.println(tr.child(9).text());
			}
		}
	}

	private static void parser(Page page) {
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
//		Element table = doc.getElementsByClass("tableLeft").first();
//		System.out.println("套餐月基本费 :"+parserField(table,"td:contains(套餐月基本费)",1,"parent_1_5_4"));
//		System.out.println("本项小计 :"+parserField(table,"td:contains(本项小计)",0,"parent_1_5_4"));
		
		Element table = doc.getElementById("tab_prodinfo_15383349697");
		
		String productNum = parserField(table,"td:contains(产品号)");
		String productName = parserField(table,"td:contains(产品名称)");
		String packageName = parserField(table,"td:contains(产品套餐)");
		String status = parserField(table,"td:contains(状态)");
		String netTime = parserField(table,"td:contains(入网时间)");
		String packageDetail = parserField(table,"td:contains(套餐详细介绍)");
		
		
		System.out.println(productNum);
		System.out.println(productName);
		System.out.println(packageName);
		System.out.println(status);
		System.out.println(netTime);
		System.out.println(packageDetail);
	}
	
	public static String parserField(Element doc, String rule){
		
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
		
	}
	
	public static String parserField(Element table, String rule, int i, String id){
		
		Elements es = table.select(rule);
		if(null != es && es.size()>0){
			Element element = es.get(i).nextElementSibling();
			element = element.getElementById(id);
			
			if(null != element){
				return element.text();
			}
		}
		return null;
		
	}

	public static WebClient login() throws Exception {

		String url2 = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url2, webClient2, null);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlElement htmlElement = (HtmlTextInput)html.getFirstByXPath("//input[@id='txtShowPwd']");
		htmlElement.click();
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		HtmlTextInput input = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtCaptcha']");
		
		
		
		username.setText("18131003055");
		passwordInput.setText("900628");
		HtmlImage image = html.getFirstByXPath("//img[@id='imgCaptcha']");	
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1006", LEN_MIN, TIME_ADD, STR_DEBUG, file.getAbsolutePath()); 
		
		input.setText(chaoJiYingResult);
		
		HtmlPage htmlpage2 = button.click();
		String url = htmlpage2.getUrl().toString();
		
		System.out.println(" url  =======>>"+url);
		
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		} else {
			System.out.println("=======成功==============");
		}

		System.out.println("************************************************点击后源码");
		System.out.println(htmlpage2.getWebResponse().getContentAsString());
		return webClient2;
	}
	
	
	public static Page getHtml(String url, WebClient webClient, String fromData) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			
		if(null != fromData){
			webRequest.setRequestBody(fromData);
		}
		webClient.setJavaScriptTimeout(20000);

		webClient.getOptions().setTimeout(20000); // 15->60

		Page searchPage = webClient.getPage(webRequest);
		System.out.println(url+"   =========================================>>源码   ");
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie : cookies){
			System.out.println("cookie  ====== "+cookie.getName()+" : "+cookie.getValue());
		}
		return searchPage;

	}
	
	
	public static Page getHtml(String url) throws Exception{
		
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "he.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00420429");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		
//		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn",".ybtj.189.cn", "CC9C4FB4E9F59098B7082978E54A91AD"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","BIGipServerHB-WTpool", "1695911560.16671.0000"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","JSESSIONID", "5mhVQy1s-hEv_4003ziBF0TM_R108uUs4LMvxgzLCJeSuOfD2FoH!-950495259"));
//		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","SHOPID_COOKIEID", "10006"));
//		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","aactgsh111220", "13398649929"));
//		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","cityCode", "he"));
/*		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","LATN_CODE_COOKIE", "0000"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","WSSNETID", "201786-738562"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","isLogin", "logined"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","loginStatus", "logined"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","lvid", "59dbf7978f2472414ad55add32674e99"));*/
//		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","userId", "201%7C20170000000001985387"));
//		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","trkId", "E52EF5CC-2FB0-451C-B6FB-770C7D164D88"));
/*		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","nvid", "1"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","s_cc", "true"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","s_fid", "30BA22764A7717CE-394DE5A473ABD905"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","s_sq", "%5B%5BB%5D%5D"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","trkHmCity", "0"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","trkHmClickCoords", "0"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","trkHmCoords", "0"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","trkHmLinks", "0"));
		webClient2.getCookieManager().addCookie(new Cookie("he.189.cn","trkHmPageName", "%2Fhe%2F"));*/
		
		Page page = webClient2.getPage(webRequest);
		System.out.println("*****************************************************************个人信息  ");
		System.out.println(page.getWebResponse().getContentAsString());
		
		return page;
		
	}

}
