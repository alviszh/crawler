package test;

import java.net.URL;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class Test1 {

	public static void main(String[] args) throws Exception{
//		loginTest();
		testSubString();
	}
	
	public static void loginTest(){
		try {
			String url = "http://www.nxzfgjj.gov.cn/ycgjj/list_gjjcx.jsp?urltype=tree.TreeTempUrl&wbtreeid=1044";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			webRequest.setAdditionalHeader("Host", "www.zzzfgjj.com");
//			webRequest.setAdditionalHeader("Referer", "http://www.zzzfgjj.com/wt-web/login");
//			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//			webRequest.setRequestBody("sjhm=17600325986&bs=1");
			Page page = webClient.getPage(webRequest);
			String html = page.getWebResponse().getContentAsString();
			System.out.println("*-*->"+html);
			
			int i = html.indexOf("var webOwner1 = ");
			System.out.println("是否有？--》"+i);
			int j = html.indexOf(";", i);
			String webOwner = html.substring(i+17, j-1);
			System.out.println("webowner="+webOwner);
			String smsUrl = "http://www.nxzfgjj.gov.cn/system/resource/dxSend1.jsp?sfzh=642224198911060020&sjhm=13619598031&webOwner="+webOwner;
			webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);
			Page page2 = webClient.getPage(webRequest);
			System.out.println("发送短信-->"+page2.getWebResponse().getContentAsString());
			/*HtmlTextInput idno = page.getFirstByXPath("//input[@id='idno']");
			idno.setText("410326199210036712");
			HtmlButtonInput sendSms = page.getFirstByXPath("//input[@id='mobilepwd']");
			HtmlPage page2 = sendSms.click();
			System.out.println("-123->"+page2.asXml());*/
			/*webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);
			Page page2 = webClient.getPage(webRequest);
			System.out.println("*-*-*->"+page2.getWebResponse().getContentAsString());*/
//			HtmlImage vimg = page.getFirstByXPath("//img[@style='cursor: pointer;']");
//			HtmlPage page2 = (HtmlPage) vimg.click();
//			System.out.println("2--->"+page.getWebResponse().getContentAsString());
//			HtmlPasswordInput password = page.getFirstByXPath("//input[@id='password']");
//			HtmlTextInput captcha = page.getFirstByXPath("//input[@id='captcha']");
//			HtmlImage veryImg = page.getFirstByXPath("//img[@style='cursor: pointer;']");
//			HtmlButton button = page.getFirstByXPath("//button[@class='btn btn-primary']");
			
//			
//			File file = new File("E:\\Codeimg\\zaozhuang.jpg");
//			veryImg.saveAs(file);
			System.out.println("输入短信验证码：");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String input = scanner.next();
			
			String valUrl = "http://www.nxzfgjj.gov.cn/system/resource/dxValidate.jsp?dxyzm="+input;
			webRequest = new WebRequest(new URL(valUrl), HttpMethod.GET);
			Page valpage = webClient.getPage(webRequest);
			System.out.println("*--*"+valpage.getWebResponse().getContentAsString());
			
			String loginUrl = "http://www.nxzfgjj.gov.cn/ycgjj/gjjcx_result.jsp?urltype=tree.TreeTempUrl&wbtreeid=1052";
//			password.setText("675725");
//			captcha.setText(input);
//			Page page3 = button.click();
//			
//			System.out.println("--->"+page3.getWebResponse().getContentAsString());
			
			webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			webRequest.setRequestBody("webOwner="+webOwner+"&sfzh=642224198911060020&sjhm=13619598031&dxyzm="+input);
			Page page3 = webClient.getPage(webRequest);
			System.out.println("--->"+page3.getWebResponse().getContentAsString());
			System.out.println("--12->"+page3.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testSubString() throws Exception{
		String resp = "Y,N,410326199210036712,,10101010,身份证号与手机号码不匹配，请重新输入！,A";
		String[] strings = resp.split(",");
		for (String string : strings) {
			System.out.println("-->"+string);
		}
	}
}
