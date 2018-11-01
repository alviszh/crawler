package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";	
		HtmlPage  searchPage=getHtml(url,webClient);
		HtmlTextInput username = (HtmlTextInput) searchPage.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) searchPage.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) searchPage.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("637965");
    	passwordInput.setText("132488");
		HtmlPage htmlpage = button.click();
	
		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00320354";
		HtmlPage htmlpage2 = getHtml(url2, webClient);
		System.out.println(htmlpage2.asXml());
		
		
		String url3="http://service.gz.189.cn/web/query.php?parser=call";
		HtmlPage htmlpage3 = getHtml(url3, webClient);
		
		getphonecode(webClient);
//		HtmlElement getbtn = (HtmlElement) htmlpage3.getFirstByXPath("//input[@id='getcheckcode']");
//		
//	System.out.println(getbtn.asXml());
//	HtmlPage page= getbtn.click();
//	System.out.println(page.asXml());
	
	System.out.println("1111111111111");
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
	//发送验证码
		public static String getphonecode(WebClient webClient) throws Exception {
	    	String	url = "http://service.gz.189.cn/web/query.php?parser=postsms";
//			List<NameValuePair> paramsList = new ArrayList<>();
//			paramsList.add(new NameValuePair("parser", "postsms"));
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);				
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://service.gz.189.cn");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web/query.php?parser=call&fastcode=00320353&cityCode=gz");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			//webRequest.setRequestParameters(paramsList);
			Page page = webClient.getPage(webRequest);	
			String html=page.getWebResponse().getContentAsString();
			System.out.println("短信验证吗结果为："+html);
     		return html;
	
		}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			url="http://service.gz.189.cn/web/query.php?parser=postsms";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
			
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("parser", "postsms"));
			//设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Host", "service.gz.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://service.gz.189.cn");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Referer", "http://service.gz.189.cn/web/query.php?parser=call&fastcode=00320353&cityCode=gz");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			System.out.println("=============================");
			System.out.println(searchPage.getWebResponse().getContentAsString());
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	
	public static Page gettmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;

	}
}
