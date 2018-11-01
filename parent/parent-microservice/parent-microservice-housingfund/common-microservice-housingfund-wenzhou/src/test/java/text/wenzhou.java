package text;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;

public class wenzhou {

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.wzgjj.gov.cn:8088/";
			HtmlPage page = (HtmlPage)getHtml(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);
			
			HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='card_no']");//身份证
			//yzm_get
			HtmlAnchor get = (HtmlAnchor)page.getFirstByXPath("//a[@id='yzm_get']");
			
			HtmlTextInput login = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzm_login']");//短信框
			
			cardno.setText("14");
			Page page2 = get.click();
			
			String html2 = page2.getWebResponse().getContentAsString();
			System.out.println(html2);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
