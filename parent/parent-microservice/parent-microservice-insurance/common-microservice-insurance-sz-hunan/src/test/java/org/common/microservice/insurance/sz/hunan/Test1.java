package org.common.microservice.insurance.sz.hunan;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Test1 {

	public static void main(String[] args){
		try {
			String url = "http://sbk.hn12333.com:7002/PubQuery/person/";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			HtmlTextInput UserName = (HtmlTextInput)page.getFirstByXPath("//input[@id='UserName']");
			HtmlTextInput LoginUserName = (HtmlTextInput)page.getFirstByXPath("//input[@id='LoginUserName']");
			HtmlPasswordInput UserPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='UserPassword']");
			HtmlImage image = page.getFirstByXPath("//img[@id='VerifyImage']");
			HtmlTextInput VerifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='VerifyCode']");
			HtmlButton btnLogin = (HtmlButton)page.getFirstByXPath("//button[@id='btnLogin']");
			
			File file = new File("E:\\Codeimg\\hunan.jpg");
			image.saveAs(file);
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String input = scanner.next();
			
			UserName.setText("430181197607179146");
			LoginUserName.setText("徐向华");
			UserPassword.setText("666666");
			VerifyCode.setText(input);
			
			//webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage loginedPage = null;
			loginedPage = btnLogin.click();
			
		
			System.out.println("loginedPage--->"+loginedPage.asXml());
			System.out.println(loginedPage.getBaseURI());
			
			HtmlElement message = (HtmlElement)loginedPage.getFirstByXPath("//span[@id='messageInfo']");
			if(null != message){
				String asText = message.asText();
				System.out.println(asText);
			}
//			String loginedPath = "E:\\crawler\\changsha\\hunanlogined.txt";
//			savefile(loginedPath,loginedPage.asXml());
			
			String personUrl = "http://sbk.hn12333.com:7002/PubQuery/commbiz/commonInfoAction!queryPersonBaseInfo.parser";
			WebRequest webRequest2 = new WebRequest(new URL(personUrl), HttpMethod.GET);
			HtmlPage userPage = webClient.getPage(webRequest2);
			System.out.println("asd");
			String userinfoPath = "E:\\crawler\\changsha\\hunanUserinfo.txt";
			savefile(userinfoPath,userPage.asXml());
			
			/*String insuranceUrl = "http://sbk.hn12333.com:7002/PubQuery/commbiz/commonInfoAction!queryPersonPayInfo.action?yer=2017";
			WebRequest webRequest3 = new WebRequest(new URL(insuranceUrl), HttpMethod.GET);
			HtmlPage insurancePage = webClient.getPage(webRequest3);
			
			String insurancePath = "E:\\crawler\\changsha\\insruanceInfo.txt";
			savefile(insurancePath,insurancePage.asXml());*/
		} catch (ScriptException e) {
			System.out.println("zhengchang,bubaocuo");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("有问题  报错");
			e.getMessage();
			e.printStackTrace();
		}
		
	}
	
	
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
}
