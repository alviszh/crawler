package test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class xiangyanglogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url = "http://www.xf12333.cn/xywzweb/html/fwdt/xxcx/shbxjfcx/index.shtml";
//		HtmlPage page = (HtmlPage) getHtml(url, webClient);
//		HtmlTextInput username = page.getElementByName("sfzh");
//		HtmlPasswordInput pass = page.getElementByName("scbh");
//		HtmlSubmitInput sub = page.getElementByName("cmdok");

		String name = "42062119850121746X";
		String password = "4206995506122";
//		username.setText("42062119850121746X");//42062119850121746X
//		pass.setText("4206995506122");//4206995506122
//		Page page2 = sub.click();

//		String html = page2.getWebResponse().getContentAsString();

//		System.out.println(html);

		String url2 = "http://www.xf12333.cn/hbwz/qtpage/fwdt/shbxjfcx_result.jsp?"
				+ "scbh="+password
				+ "&sfzh="+name
				+ "&dt=118";
		Page page3 = getHtml(url2, webClient);
		String html2 = page3.getWebResponse().getContentAsString();

		System.out.println(html2);
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
