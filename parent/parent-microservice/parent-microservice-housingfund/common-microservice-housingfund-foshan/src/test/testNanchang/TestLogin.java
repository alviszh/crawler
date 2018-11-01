package testNanchang;

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
		String loginUrl = "http://www.ncgjj.com.cn:8081/wt-web/login";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			HtmlPage loginPage = webClient.getPage(webRequest);
			if(loginPage.asXml().contains("立即登录")){
				HtmlImage cert = loginPage.getFirstByXPath("//img[@style='cursor: pointer;']");
				HtmlTextInput username = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='username']");
				HtmlPasswordInput in_password = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='in_password']");
//					HtmlPasswordInput passwordInput = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='txtPassword']");
//					HtmlElement button = (HtmlElement) loginPage.getFirstByXPath("//a[@id='loginbtn']");
				HtmlTextInput validate = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='captcha']");
				
				HtmlElement button = (HtmlElement) loginPage.getFirstByXPath("//button[@id='gr_login']");
				
				File file = new File("E:\\Codeimg\\nanchang.jpg");
				cert.saveAs(file);
				
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
				String input = scanner.next();
				
				username.setText("hujing1119");
				in_password.setText("hujing1119");
				validate.setText(input);
				HtmlPage loginedPage = button.click();
				Thread.sleep(3000);
				/*String resPath = "E:\\crawler\\housingfund\\nanchang\\loginederr.txt";
				savefile(resPath,loginedPage.asXml());*/
				System.out.println(loginedPage.getBaseURL());
//				String userinfoUrl = "http://www.fsgjj.gov.cn/webapply/person/personQuery!personInfo.do?aId=&areaCode=";
//				String detailsUrl = "http://www.ncgjj.com.cn:8081/wt-web/home?logintype=1";
//				WebRequest webRequest1 = new WebRequest(new URL(detailsUrl), HttpMethod.GET);
//				webRequest1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//				webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//				webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//				webRequest1.setAdditionalHeader("Connection", "keep-alive");
//				webRequest1.setAdditionalHeader("Host", "www.ncgjj.com.cn:8081");
//				webRequest1.setAdditionalHeader("Referer", "http://www.ncgjj.com.cn:8081/wt-web/home?logintype=1");
//				webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//				webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
//				webClient.getPage(webRequest1);
				
				String url = "http://www.ncgjj.com.cn:8081/wt-web/person/jczqmxqc?lsnd=2016-2017&pageNum=1&pageSize=50";
				WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.GET);
				webRequest2.setAdditionalHeader("Accept", "*/*");
				webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest2.setAdditionalHeader("Connection", "keep-alive");
				webRequest2.setAdditionalHeader("Host", "www.ncgjj.com.cn:8081");
				webRequest2.setAdditionalHeader("Origin", "http://www.ncgjj.com.cn:8081");
				webRequest2.setAdditionalHeader("Referer", "http://www.ncgjj.com.cn:8081/wt-web/home?logintype=1");
//				webRequest2.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
				webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
				Page userinfoPage = webClient.getPage(webRequest2);
				
				String resPath = "E:\\crawler\\housingfund\\nanchang\\detailsinfo.jpg";
				savefile(resPath,userinfoPage.getWebResponse().getContentAsString());
			}
		} catch (Exception e) {
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
