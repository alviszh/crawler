package app.crawler.telecom.htmlunit;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;

public class LoginAndGet {

	public final Logger log = LoggerFactory.getLogger(LoginAndGet.class);

	public static WebParamTelecom<?> getphonecode(TaskMobile taskMobile) {

		WebParamTelecom<?> webParamTelecom = new WebParamTelecom<>();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = addcookie(webClient, taskMobile);

//			String errorurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000846";
//			HtmlPage htmlpage_error = getHtml(errorurl, webClient);
//			
//			if (htmlpage_error.getWebResponse().getContentAsString().indexOf("对不起，系统忙，请稍后再试") != -1) {
//				webParamTelecom.setHtml(htmlpage_error.getWebResponse().getContentAsString());
//				webParamTelecom.setErrormessage("电信黑龙江网站出现问题，请稍后重试");
//				return webParamTelecom;
//			}
//			webClient = htmlpage_error.getWebClient();
//			//
//			String url = "http://hl.189.cn/service/cqd/detailQueryCondition.do";
//			HtmlPage htmlpage = getHtml(url, webClient);
//			webClient = htmlpage.getWebClient();
			String url = "http://hl.189.cn/service/userCheck.do?method=sendMsg";

			taskMobile.setTrianNum(0);

			List<NameValuePair> paramsList = new ArrayList<>();
			paramsList.add(new NameValuePair("method", "sendMsg"));

			Page page = gethtmlPost(webClient, paramsList, url);
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);

			System.out.println(page.getWebResponse().getContentAsString());
			if (webParamTelecom.getHtml().indexOf("对不起，系统忙，请稍后再试") != -1) {
				webParamTelecom.setErrormessage("电信黑龙江网站出现问题，请稍后重试");
			}
			
			if(webParamTelecom.getHtml()!="1"){
				if(webParamTelecom.getHtml().trim().indexOf("2")!=-1&&webParamTelecom.getHtml().length()<10){
					webParamTelecom.setErrormessage("发送短信已达上限");
				}
				else if(webParamTelecom.getHtml().length()>10){
					webParamTelecom.setErrormessage("短信验证错误");
				}
			}
			return webParamTelecom;
		} catch (Exception e) {
			if (taskMobile.getTrianNum() > 2) {
				webParamTelecom.setErrormessage("网站超时，请稍后重试");
				return webParamTelecom;
			}
			webParamTelecom = getphonecode(taskMobile);

			return webParamTelecom;
		}

	}

	public static WebParamTelecom<?> setphonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebParamTelecom<?> webParamTelecom = new WebParamTelecom<>();
		
		
		String url = "http://hl.189.cn/service/zzfw.do";
		
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		webClient2 = addcookie(webClient2, taskMobile);
		
		Set<Cookie> cookies = webClient2.getCookieManager().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (Cookie cookie : cookies) {
			
			if(cookie.getName().indexOf("ybtj.189.cn")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			
			if(cookie.getName().equals("JSESSIONID")){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			
			if(cookie.getName().indexOf("SHOPID_COOKIEID")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("aactgsh111220")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("cityCode")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("isLogin")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("loginStatus")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("lvid")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("nvid")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("s_cc")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("s_fid")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("svid")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("trkHmClickCoords")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("trkId")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
			if(cookie.getName().indexOf("userId")!=-1){
				webClient.getCookieManager().addCookie(new Cookie("hl.189.cn",cookie.getName(), cookie.getValue()));
			}
		}
		
		 cookies = webClient.getCookieManager().getCookies();

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("method", "checkDX"));
		paramsList.add(new NameValuePair("yzm", messageLogin.getSms_code().trim()));

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			
			webRequest.setAdditionalHeader("Host", "hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Origin", "http://hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Referer",
					"http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");// 设置请求报文头里的refer字段
			
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");// 设置请求报文头里的refer字段
			
			Page page = gethtmlPostByWebRequest(webClient, webRequest, url);
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			return webParamTelecom;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			webParamTelecom.setHtml(e.getMessage());
			webParamTelecom.setErrormessage(e.getMessage());
			return webParamTelecom;
		}

	}

	public static String getCallThemHtml(MessageLogin messageLogin, TaskMobile taskMobile, String phonenum, String date,
			int seledType,int selectype, int page) {
//		String url = "http://hl.189.cn/service/cqd/queryDetailList.do?isMobile=0" + "&seledType=" + selectype
//				+ "&queryType=&pageSize=1000&" + "pageNo=" + page + "&flag=&pflag=" + "&accountNum=" + phonenum.trim()
//				+ "%3A2000004&callType=3&selectType="+selectype
//				+ "&detailType=" + selectype + "&selectedDate=" + date
//				+ "&method=queryCQDMain";
		
		
		
		String url = "http://hl.189.cn/service/cqd/queryDetailList.do?"
				+ "isMobile=0"
				+ "&seledType="+seledType
				+ "&queryType="
				+ "&pageSize=1000"
				+ "&pageNo="+page
				+ "&flag="
				+ "&pflag="
				+ "&accountNum="+ phonenum.trim()
				+ "%3A2000004"
				+ "&callType=3"
				+ "&selectType=1"
				+ "&detailType="+seledType
				+ "&selectedDate="+ date
				+ "&method=queryCQDMain";
		
		
		
				
//		http://hl.189.cn/service/cqd/queryDetailList.do?isMobile=0&seledType=9&queryType=&pageSize=10&pageNo=1&flag=&pflag=&accountNum=13351102145%3A2000004&callType=3&selectType=1&detailType=9&selectedDate=20189&method=queryCQDMain
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		try {
			Page htmlPage = getHtml(url, webClient);
			return htmlPage.getWebResponse().getContentAsString();
		} catch (Exception e) {

			return null;
		}

	}

	public static String getPayMsgStatusThem(MessageLogin messageLogin, TaskMobile taskMobile, String stardate,
			String enddate) {
		
		String url = "http://hl.189.cn/service/payQuery.do?opFlag=query";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("startDate", stardate));
		paramsList.add(new NameValuePair("endDate", enddate));
		HtmlPage htmlPage = (HtmlPage) gethtmlPost(webClient, paramsList, url);
		return htmlPage.asXml();
	}

	public static String getCustomThem(MessageLogin messageLogin, TaskMobile taskMobile, String date) {
		String url = "http://hl.189.cn/service/billDateChoiceNew.do?method=doSearch&selectDate=" + date;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("method", "doSearch"));
		paramsList.add(new NameValuePair("selectDate", date));
		HtmlPage htmlPage = (HtmlPage) gethtmlPost(webClient, paramsList, url);
		return htmlPage.asXml();
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
			return null;
		}

	}

	public static Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url)
			throws Exception, IOException {
		webClient.getOptions().setJavaScriptEnabled(false);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			return null;
		}

	}

	private static WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
}
