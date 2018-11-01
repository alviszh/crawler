package org.common.microservice.insurance.sz.hunan;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Test2 {

	public static void main(String[] args) throws Exception{
		String url = "http://www.hn12333.com:81/comm_front/query/bsjylbxgrzh_query.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		HtmlTextInput sfzhm = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzhm']");
		HtmlTextInput xm = (HtmlTextInput)page.getFirstByXPath("//input[@id='xm']");
		HtmlPasswordInput mm = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='mm']");
		HtmlImage image = page.getFirstByXPath("//img[@id='pics']");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@name='yzm']");
		HtmlButtonInput btnLogin = (HtmlButtonInput)page.getFirstByXPath("//input[@name='search']");
		
		File file = new File("E:\\Codeimg\\hunan2.jpg");
		image.saveAs(file);
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		
		sfzhm.setText("430321198911102302");
		xm.setText("陈艳");
		mm.setText("102302");
		yzm.setText(input);
		
		HtmlPage loginedPage = btnLogin.click();
		System.out.println("登陆后的页面"+loginedPage.asXml());
		
		String ylUrl = "http://www.hn12333.com:81/comm_front/query/jfxx_zz_list.jsp?flag=next&page1=2";
		webRequest = new WebRequest(new URL(ylUrl), HttpMethod.GET);
		HtmlPage userPage = webClient.getPage(webRequest);
		if(null != userPage.getElementById("count")){
			System.out.println("yanglao----->"+userPage.asXml());
		}
		
		/*String insuranceUrl = "http://sbk.hn12333.com:7002/PubQuery/commbiz/commonInfoAction!queryPersonPayInfo.action?yer=2017";
		WebRequest webRequest3 = new WebRequest(new URL(insuranceUrl), HttpMethod.GET);
		HtmlPage insurancePage = webClient.getPage(webRequest3);
		
		String insurancePath = "E:\\crawler\\changsha\\insruanceInfo.txt";
		savefile(insurancePath,insurancePage.asXml());*/
	}
	
	
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
}
