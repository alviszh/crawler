package testParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.crawler.domain.WebParam;
import app.service.InsuranceService;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class Test3 {

	
	public static void main(String[] args) throws Exception{
		String url = "http://gzlss.hrssgz.gov.cn/cas/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		if(200 == status){
			HtmlImage image = page.getFirstByXPath("//img[@id='validCodeimg']");
			HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginName']");
			HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='loginPassword']");
			HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='validateCode']");
			HtmlElement usertype2 = (HtmlElement)page.getFirstByXPath("//input[@id='usertype2']");
			HtmlElement submitbt = (HtmlElement)page.getFirstByXPath("//input[@id='submitbt']");
			
//			String imageName = UUID.randomUUID() + ".jpg";
			File file = new File("E:\\Codeimg\\guangzhou.jpg");
			image.saveAs(file);
			
			loginName.setText("440181199011210625");
			loginPassword.setText("0f2efc2d");
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String input = scanner.next();
			validateCode.setText(input);
			
			usertype2.click();
			HtmlPage page2 = submitbt.click();
			System.out.println("page2---"+page2.asXml());
			
			/*webClient = page2.getWebClient();
			
			String smsValidateUrl = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/tomain/main.xhtml?buscode=LOGIN_BUSSINESS_CODE&code=123123";
			webRequest = new WebRequest(new URL(smsValidateUrl), HttpMethod.GET);
			HtmlPage smsValidatePage = webClient.getPage(webRequest);
			System.out.println("smsValidatePage---"+smsValidatePage.asXml());*/
			/*HtmlTextInput validateCode2 = smsValidatePage.getFirstByXPath("//input[@id='validateCode']");
			HtmlButtonInput checkCode = smsValidatePage.getFirstByXPath("//input[@id='checkCode']");
			
//			String smsCode = scanner.next();
			validateCode2.setText("834448");
			HtmlPage mainPage = checkCode.click();
			
			System.out.println("zhuyemian+++"+mainPage.asXml());*/
			
			/* http://gzlss.hrssgz.gov.cn/gzlss_web/business/anon/LetterVistsSMSCode/letterVists.xhtml?buscode=LOGIN_BUSSINESS_CODE
			 * 
			 * WebRequest webRequest2 = new WebRequest(new URL("http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/realGetPersonInfoSearch.xhtml?querylog=true&businessocde=291QB-GRJCXX&visitterminal=PC&csrftoken=1jt6Zftc22fwGlpzDDYg7v6QLQvN73KRbpLwY91v2zQ22p3VswLx!1218314479!"+System.currentTimeMillis()+"&type=1"), HttpMethod.GET);
			HtmlPage page3 = webClient.getPage(webRequest2);
			System.out.println("maybe personalInfo---"+page3.asXml());*/
//			getData(webClient);
		}
		
//		long currentTimeMillis = System.currentTimeMillis();
//		System.out.println(currentTimeMillis);
	}
	
	public static void getData(WebClient webClient) throws Exception{
		String url  = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getHealthcarePersonPayHistorySumup.xhtml?query=1&querylog=true&businessocde=291QB_YBGRJFLSCX&visitterminal=PC&aac001=3000566209&startStr=201601&endStr=201804";
		String url1 = "http://gzlss.hrssgz.gov.cn/gzlss_web/business/front/foundationcentre/getHealthcarePersonPayHistorySumup.xhtml?querylog=true&businessocde=291QB_YBGRJFLSCX&visitterminal=PC-MENU";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		System.out.println("shuju==="+page.asXml());
	}
	
}
