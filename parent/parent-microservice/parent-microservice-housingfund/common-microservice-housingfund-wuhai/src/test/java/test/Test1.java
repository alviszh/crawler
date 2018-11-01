package test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Test1 {

	public static void main(String[] args) throws Exception{
		login();
	}
	
	public static void login() throws Exception{
		String loginUrl = "http://www.whgjj.net/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		System.out.println("-->"+loginPage.asXml());
		
		HtmlTextInput nameInput =(HtmlTextInput) loginPage.getFirstByXPath("//input[@name='xm']");
		HtmlTextInput idInput =(HtmlTextInput) loginPage.getFirstByXPath("//input[@name='sfzid']");
		HtmlSubmitInput submit =(HtmlSubmitInput) loginPage.getFirstByXPath("//input[@type='submit']");
		
		nameInput.setText("陈俊兰");
		idInput.setText("150302197109150525");
		HtmlPage click = submit.click();
		
		System.out.println("*-*->"+click.asXml());
	}
}
