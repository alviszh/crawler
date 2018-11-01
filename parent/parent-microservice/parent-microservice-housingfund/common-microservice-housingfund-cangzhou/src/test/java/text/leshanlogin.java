package text;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class leshanlogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//511123198409122263  ls66021520LS
		
		String url = "http://wangt.lszfgjj.gov.cn:7009/netface/login.do";
		HtmlPage page = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		HtmlButton obtainBtn = (HtmlButton) page.getElementById("obtainBtn");
		String uname = "511123198409122263";
		String pass = "ls66021520LS";
		username.setText(uname);
		password.setText(pass);
	//	obtainBtn.click();
		//
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Host", "wangt.lszfgjj.gov.cn:7009");
		webClient.addRequestHeader("Origin", "http://wangt.lszfgjj.gov.cn:7009");
		webClient.addRequestHeader("Referer", "http://wangt.lszfgjj.gov.cn:7009/netface/login.do");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		String url2 = "http://wangt.lszfgjj.gov.cn:7009/netface/reg!getDynCode.do?"
				+ "dto%5B%27idCard%27%5D="+uname
				+ "&dto%5B%27aType%27%5D=4";
		Page gethtmlPost = gethtmlPost(webClient, null, url2);
		String contentAsString = gethtmlPost.getWebResponse().getContentAsString();
		String trim = JSONObject.fromObject(contentAsString).getString("msg").trim();
		System.out.println(trim);
		
		
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
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
