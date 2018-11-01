package testCrawler;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) {
		login(0);
	}
	
	
	public static void login(int i){
		String loginUrl = "http://www.fsgjj.gov.cn/webapply/login.do?fromFlag=wwgr";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			HtmlPage loginPage1 = webClient.getPage(webRequest);
			if(loginPage1.asXml().contains("登录")){
				HtmlImage cert1 = loginPage1.getFirstByXPath("//img[@id='cert']");
				HtmlPage loginPage = (HtmlPage) cert1.click();
				
				HtmlImage cert = loginPage.getFirstByXPath("//img[@id='cert']");
				HtmlTextInput idCard = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='idcard']");
				HtmlTextInput account = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='account']");
//					HtmlPasswordInput passwordInput = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='txtPassword']");
//					HtmlElement button = (HtmlElement) loginPage.getFirstByXPath("//a[@id='loginbtn']");
				HtmlTextInput validate = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='_cert_parameter']");
				
				HtmlImage loginImg = loginPage.getFirstByXPath("//img[@id='login_r17_c4']");
				
				File file = new File("E:\\Codeimg\\foshan.jpg");
				cert.saveAs(file);
				
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
				String input = scanner.next();
				
				idCard.setText("440603199203044527");
				account.setText("96931010");
				validate.setText(input);
				HtmlPage loginedPage = (HtmlPage)loginImg.click();
				
				String resPath = "E:\\crawler\\housingfund\\foshan\\logined.txt";
				savefile(resPath,loginedPage.asXml());
				
//				String userinfoUrl = "http://www.fsgjj.gov.cn/webapply/person/personQuery!personInfo.do?aId=&areaCode=";
				String detailsUrl = "http://www.fsgjj.gov.cn/webapply/person/personQuery!moneyseq.do?aId=&areaCode=";
				WebRequest webRequest1 = new WebRequest(new URL(detailsUrl), HttpMethod.GET);
				webRequest1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
				webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest1.setAdditionalHeader("Connection", "keep-alive");
				webRequest1.setAdditionalHeader("Host", "www.fsgjj.gov.cn");
				webRequest1.setAdditionalHeader("Referer", "http://www.fsgjj.gov.cn/webapply/personapply/index.do");
				webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
				webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
				
				HtmlPage userinfoPage = webClient.getPage(webRequest1);
				
				/*String resPath = "E:\\crawler\\housingfund\\foshan\\detailsinfo.txt";
				savefile(resPath,userinfoPage.asXml());*/
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
