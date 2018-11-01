

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;


public class GJJTest {

	private static String baseUrl = "https://sso.ahzwfw.gov.cn/uccp-server/login";
	
	private static WebClient webClient = null;

	public static void main(String[] args) throws Exception {
		webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage htmlPage = (HtmlPage) getHtml(baseUrl, webClient);
//		System.out.println("=================================================");
//		System.out.println(htmlPage.asXml());
		
		Document doc = Jsoup.parse(htmlPage.asXml());
		
		String lt = doc.select("[name=lt]").attr("value");
		String execution = doc.select("[name=execution]").attr("value");
		String _eventId = doc.select("[name=_eventId]").attr("value");
		String loginType = doc.select("[name=loginType]").attr("value");
		String credentialType = doc.select("[name=credentialType]").attr("value");
		
		String platform = doc.select("[name=platform]").attr("value");
//		String ukeyType = doc.select("[name=ukeyType]").attr("value");
//		String ahcaukey = doc.select("[name=ahcaukey]").attr("value");
//		String ahcasign = doc.select("[name=ahcasign]").attr("value");
		String userType = doc.select("[name=userType]").attr("value");
		
		
		String url = "https://sso.ahzwfw.gov.cn/uccp-server/login";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("lt", lt));
		paramsList.add(new NameValuePair("execution", execution));
		paramsList.add(new NameValuePair("_eventId", _eventId));
		paramsList.add(new NameValuePair("platform", platform));
		paramsList.add(new NameValuePair("loginType", loginType));
		paramsList.add(new NameValuePair("credentialType", credentialType));
		paramsList.add(new NameValuePair("userType", userType));
		paramsList.add(new NameValuePair("username", "15856785648"));
		paramsList.add(new NameValuePair("password", "y15856785648"));
//		paramsList.add(new NameValuePair("username", "zhaojuan0623"));
//		paramsList.add(new NameValuePair("password", "y15856785647"));
		paramsList.add(new NameValuePair("random", ""));
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		System.out.println("============");
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		if(searchPage.getWebResponse().getContentAsString().indexOf("用户名或密码不正确")!=-1){
			System.out.println("=========false=============");
		}else{
			System.out.println("=========true=============");
		}

//		HtmlSpan form = htmlPage.getFirstByXPath("//*[@id='person']");
//
//		HtmlInput nameInput = form.getFirstByXPath("//*[@id='perName']");
//		HtmlInput passInput = form.getFirstByXPath("//*[@id='perPsd']");
//
//		nameInput.setValueAttribute("15856785648");
//		passInput.setValueAttribute("y15856785648");
//	
//
//		HtmlElement loginButton = (HtmlElement) form.getFirstByXPath("//*[@id='psdBtn']");
//		htmlPage = loginButton.click();
//		System.out.println("=================================================");
//		System.out.println(htmlPage.asXml());	
	}


	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

}
