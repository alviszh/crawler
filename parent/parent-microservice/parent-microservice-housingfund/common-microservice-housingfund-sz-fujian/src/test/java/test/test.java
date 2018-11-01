package test;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String url = "http://www.fjszgjj.com/gjj/businessHallLogin.do";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		HtmlElement botton1 = searchPage.getFirstByXPath("//*[@id='type']/option[1]");
		HtmlPage htmlPage1 = botton1.click();
		HtmlTextInput name1 = htmlPage1.getFirstByXPath("//*[@id='username']");
		HtmlPasswordInput password1 = htmlPage1.getFirstByXPath("//*[@id='password']");
		name1.setText("350125198810234749");
		password1.setText("234749");
		
		HtmlElement botton = htmlPage1.getFirstByXPath("//*[@id='personalLoginbtn']");
		
		Page searchPage1 = botton.click();
		String url1 ="http://www.fjszgjj.com/gjj/accountInfo/queryInfogrxx";
		WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);

		HtmlPage searchPage2 = webClient.getPage(webRequest1);
		System.out.println(searchPage2.getWebResponse().getContentAsString());
	}

}
