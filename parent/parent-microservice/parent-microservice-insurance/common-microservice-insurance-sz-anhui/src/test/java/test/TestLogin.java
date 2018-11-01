package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url1 = "https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=d4a4dfffe8974f309b7f1f6b25b35ecd&service=http%3A%2F%2F61.190.31.166%3A9091%2Fahrsgrwt%2Findex.do";
		// 调用下面的getHtml方法
		WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest1);
		String html = page.asXml();
		Document doc = Jsoup.parse(html);
		Element elementById = doc.getElementById("legpsdFm");
		String lt = elementById.getElementsByAttributeValue("name", "lt").val();
		System.out.println(lt);
		String execution = elementById.getElementsByAttributeValue("name", "execution").val();
		System.out.println(execution);
		String _eventId = elementById.getElementsByAttributeValue("name", "_eventId").val();
		System.out.println(_eventId);
		String platform = elementById.getElementsByAttributeValue("name", "platform").val();
		System.out.println(platform);
		String loginType = elementById.getElementsByAttributeValue("name", "loginType").val();
		System.out.println(loginType);
		
//		String url22="https://sso.ahzwfw.gov.cn/uccp-server/randomCodeValidate";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("userName", "mengshanshan007"));
//		paramsList.add(new NameValuePair("userType", "0"));
//		paramsList.add(new NameValuePair("loginType", "INTERNET"));
//		WebRequest webRequest22 = new WebRequest(new URL(url22), HttpMethod.POST);
//		webRequest22.setAdditionalHeader("Accept", "*/*");
//		webRequest22.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest22.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		
//		webRequest22.setAdditionalHeader("Connection", "keep-alive");
//		webRequest22.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//		webRequest22.setAdditionalHeader("Host", "sso.ahzwfw.gov.cn");
//		webRequest22.setAdditionalHeader("Origin", "https://sso.ahzwfw.gov.cn");
//		webRequest22.setRequestBody("userName=mengshanshan007&userType=0&loginType=INTERNET");
//		Page page22 = webClient.getPage(webRequest22);
//		String html22=page22.getWebResponse().getContentAsString();
//		System.out.println(html22);
		// 调用下面的getHtml方法
		String url="https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=4fc73e5bcd794b08889f39ad2b89acde&service=https://uc.ewoho.com/provinceCas/login?whService=aHR0cDovL3d3dy5ld29oby5jb20vcGVyc29uYWxjZW50ZXIvbG9hZGhvdXNlZnVuZC5kbw";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("lt", lt));
		webRequest.getRequestParameters().add(new NameValuePair("execution", execution));
		webRequest.getRequestParameters().add(new NameValuePair("_eventId", _eventId));
		webRequest.getRequestParameters().add(new NameValuePair("platform", platform));
		webRequest.getRequestParameters().add(new NameValuePair("loginType", loginType));
		webRequest.getRequestParameters().add(new NameValuePair("credentialType", "PASSWORD"));
		webRequest.getRequestParameters().add(new NameValuePair("userType", "0"));
		webRequest.getRequestParameters().add(new NameValuePair("username", "mengshanshan007"));
		webRequest.getRequestParameters().add(new NameValuePair("password", "mss007"));
		webRequest.getRequestParameters().add(new NameValuePair("random", ""));
		Page loginPage = webClient.getPage(webRequest);
		String loginHtml=loginPage.getWebResponse().getContentAsString();
		System.out.println(loginHtml);
		
		String cookieswebClient = CommonUnit.transcookieToJson(webClient);
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieswebClient);
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
		}
		HtmlPage indexPage=getHtml("http://61.190.31.166:9091/ahrsgrwt/index.do",webClient2);
		System.out.println(indexPage.asXml());
		System.out.println(indexPage.getWebResponse().getContentAsString());
		
		String url2="http://61.190.31.166:9091/ahrsgrwt/grbs/sbkgl/cd030101.do";
		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		webRequest2.setAdditionalHeader("Host", "61.190.31.166:9091");
		Page page2 = webClient2.getPage(webRequest2);
		String html2=page2.getWebResponse().getContentAsString();
		System.out.println(html2);
		//{"resultCode":"1001","resultMsg":"","resultBody":{"aac004":"2","aab001":"CODEhs7-11009","aac003":"顾雨芹","aac006":"19931028","aac005":"01","aac008":"1","aae159":"","aac009":"","aaz500":"B32441847","aae007":"","aae006":"平湖秋月16-3-402","aae005":"13955303302","ace005":"","aae042":"","gj":"CHN","aab301":"340200","zjlx":"0","aab004":"徽商银行芜湖环城路支行","aac001":"11650982","aac010":"平湖秋月16-3-402","aac002":"34020219931028142X"}}

		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
