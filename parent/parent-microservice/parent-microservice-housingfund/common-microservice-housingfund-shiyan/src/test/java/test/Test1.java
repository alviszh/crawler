package test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Test1 {

	private final static String ENCODE = "UTF-8"; 
	
	public static void main(String[] args) throws Exception{
		String loginUrl = "https://grcx.sygjj.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		System.out.println("loginPage--->"+loginPage.asXml());
		HtmlImage img = (HtmlImage) loginPage.getElementById("authcode_img");
		
		File file = new File("E:\\Codeimg\\shiyan.jpg");
		img.saveAs(file);
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		
		/*HtmlTextInput cardno = loginPage.getFirstByXPath("//input[@id='cardno']");
		HtmlTextInput xm = loginPage.getFirstByXPath("//input[@id='xm']");
		HtmlTextInput authcode = loginPage.getFirstByXPath("//input[@id='authcode']");
		HtmlButton docx = loginPage.getFirstByXPath("//button[@id='docx']");
		
		cardno.setText("420323198309053414");
		xm.setText("张绍朋");
		authcode.setText(input);
		Page page = docx.click();*/
		
		String loginUrl2 = "https://grcx.sygjj.gov.cn/servlet/query.do";
		System.out.println("loginUrl2---->"+loginUrl2);
		webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.POST);
		webRequest.setAdditionalHeader(":authority", "grcx.sygjj.gov.cn");
		webRequest.setAdditionalHeader(":method", "POST");
		webRequest.setAdditionalHeader(":path", "/servlet/query.do");
		webRequest.setAdditionalHeader(":scheme", "https");
		webRequest.setAdditionalHeader("accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("accept-encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("accept-language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("origin", "https://grcx.sygjj.gov.cn");
		webRequest.setAdditionalHeader("referer", "https://grcx.sygjj.gov.cn/");
		webRequest.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		webRequest.setAdditionalHeader("x-requested-with", "XMLHttpRequest");
		webRequest.setRequestBody("cardno=420323198309053414&xm="+getURLEncoderString("张绍朋")+"&authcode="+input);
		Page page = webClient.getPage(webRequest);
		System.out.println("page----->"+page.getWebResponse().getContentAsString());
	}
	
	 public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
