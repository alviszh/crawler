package test;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class AppTest_xuancheng {
	public static void main(String[] args) throws Exception {
try {
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url1 = "https://sso.ahzwfw.gov.cn/uccp-server/login?service=http%3a%2f%2fwww.xcgjj.cn%3a8081%2fSzCasLogin%2ftSingle.aspx&appCode=8bda7b91ebd640f38f8a3bb86b26a36c";
			// 调用下面的getHtml方法
			WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
			HtmlPage html1 = webClient.getPage(webRequest1);
			String asXml = html1.asXml();
			Document doc = Jsoup.parse(asXml);
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
			String url = "https://sso.ahzwfw.gov.cn/uccp-server/login?service=http%3a%2f%2fwww.xcgjj.cn%3a8081%2fSzCasLogin%2ftSingle.aspx&appCode=8bda7b91ebd640f38f8a3bb86b26a36c";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("lt", lt));
			webRequest.getRequestParameters().add(new NameValuePair("execution", execution));
			webRequest.getRequestParameters().add(new NameValuePair("_eventId", _eventId));
			webRequest.getRequestParameters().add(new NameValuePair("platform", platform));
			webRequest.getRequestParameters().add(new NameValuePair("loginType", loginType));
			webRequest.getRequestParameters().add(new NameValuePair("credentialType", "PASSWORD"));
			webRequest.getRequestParameters().add(new NameValuePair("userType", "0"));
			webRequest.getRequestParameters().add(new NameValuePair("username", "342524198712140824"));
			webRequest.getRequestParameters().add(new NameValuePair("password", "huanghuan1987"));
			webRequest.getRequestParameters().add(new NameValuePair("random", ""));
			Page html = webClient.getPage(webRequest);
			String contentAsString2 = html.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			
			String[] split = contentAsString2.split("&sfzhm=");
			String[] split2 = split[1].split("\"");
			System.out.println(split2[0]);
			if (contentAsString2.contains("images/main_b01")) {
				System.out.println("登陆成功！");
				String urltemp = "http://www.xcgjj.cn:8081/gjjcx/cx_fromsfzhm_sz.aspx?cx=20180319&sfzhm="+split2[0]+""; 
				WebRequest requestSettings = new WebRequest(new URL(urltemp), HttpMethod.GET); 
				Page page = webClient.getPage(requestSettings); 
				String contentAsString = page.getWebResponse().getContentAsString(); 
				System.out.println("前提页面响应的内容是：" + contentAsString);
				
			} else {
				System.out.println("登陆失败！异常错误！");
			}
		} catch (Exception e) {
			System.out.println("登陆失败！异常错误！");
			e.printStackTrace();
		}
	}
}