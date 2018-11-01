package testParser;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Test1 {

	public static void main(String[] args) throws Exception{
		String url = "http://www.ccshbx.org.cn/member/login.jhtml";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		System.out.println("page---"+page.asXml());
		HtmlImage image = page.getFirstByXPath("//img[@id='captcaImg']");
		HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='checkCode']");
		HtmlElement submitbt = (HtmlElement)page.getFirstByXPath("//a[@id='login_btn']");
		HtmlElement zh = (HtmlElement)page.getFirstByXPath("//*[@id='zh']");
		HtmlElement firstChild = (HtmlElement)zh.getFirstChild();
		
		File file = new File("E:\\Codeimg\\changchunshebao.jpg");
		image.saveAs(file);
		firstChild.click();
		loginName.setText("220102198908196169");
		loginPassword.setText("196169");
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		validateCode.setText(input);
		
		HtmlPage page2 = submitbt.click();
		System.out.println("page2url---"+page2.getUrl());
		System.out.println("page2---"+page2.asXml());
		
		
	}
}
