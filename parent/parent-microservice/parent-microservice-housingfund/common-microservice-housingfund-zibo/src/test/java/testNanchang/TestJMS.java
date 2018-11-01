package testNanchang;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import sun.misc.BASE64Encoder;

public class TestJMS {
	public static void main(String[] args) throws Exception{
//		login();
		loginTest();
//		encode64Test("666124");
	}
	
	public static String encode64Test(String s) throws Exception{
        System.out.println("转换前：" + s);  
          
        String encode = base64Encode(s.getBytes());  
        System.out.println("转换后：" + encode);  
        return encode;
//        System.out.println("解码后：" + new String(base64Decode(encode)));
	}
	
	/** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    public static String base64Encode(byte[] bytes){  
        return new BASE64Encoder().encode(bytes);  
    }  
      
	public static void loginTest(){
		try {
			String smsUrl = "http://www.wfgjj.gov.cn/personal/sendVercodeByIdNo.jspx?idNo=410326199210036712";
			String url = "http://www.wfgjj.gov.cn/personal/login.jspx";
			String loginUrl = "http://www.wfgjj.gov.cn/personal/personLogin_new.jspx";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			webRequest.setAdditionalHeader("Host", "www.zzzfgjj.com");
//			webRequest.setAdditionalHeader("Referer", "http://www.zzzfgjj.com/wt-web/login");
//			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//			webRequest.setRequestBody("sjhm=17600325986&bs=1");
			/*HtmlPage page = webClient.getPage(webRequest);
			System.out.println("*-*->"+page.getWebResponse().getContentAsString());*/
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
//			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String input = scanner.next();
//			
//			password.setText("675725");
//			captcha.setText(input);
//			Page page3 = button.click();
//			
//			System.out.println("--->"+page3.getWebResponse().getContentAsString());
			
			webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			webRequest.setRequestBody("key=&idno=370782199101061847&pwd=NjY2MTI0&captcha="+input);
			Page page3 = webClient.getPage(webRequest);
			System.out.println("--->"+page3.getWebResponse().getContentAsString());
			System.out.println("--12->"+page3.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void login(){
		String loginUrl = "http://www.jmsgjj.org.cn:8080/wt-web/login";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			HtmlPage loginPage1 = webClient.getPage(webRequest);
			HtmlImage vc1 = loginPage1.getFirstByXPath("//img[@style='cursor: pointer;']");
			if(null != vc1){
				HtmlPage loginPage = (HtmlPage) vc1.click();
				if(loginPage.asXml().contains("立即登录")){
					HtmlImage vc = loginPage.getFirstByXPath("//img[@style='cursor: pointer;']");
					HtmlButton loginbtn = loginPage.getFirstByXPath("//button[@class='btn btn-primary']");
					HtmlTextInput username = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='username']");
					HtmlPasswordInput password = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='password']");
					HtmlTextInput captcha = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='captcha']");
					
					File file = new File("E:\\Codeimg\\jms.jpg");
					vc.saveAs(file);
					
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(System.in);
					String input = scanner.next();
					
					username.setText("230811198707261127");
					password.setText("718623");
					captcha.setText(input);
					HtmlPage loginedPage = (HtmlPage) loginbtn.click();
					Thread.sleep(1500);
					DomElement element = loginedPage.getElementById("error");
					if(null != element){
						System.out.println("---->"+element.asText());
					}else{
						System.out.println("success");
					}
					
					/*String userUrl = "http://www.jmsgjj.org.cn:8080/wt-web/person/jbxx";
					webRequest = new WebRequest(new URL(userUrl), HttpMethod.POST);
					Page page = webClient.getPage(webRequest);
					System.out.println("userinfo-->"+page.getWebResponse().getContentAsString());
					
					String transUrl = "http://www.jmsgjj.org.cn:8080/wt-web/personal/jcmxlist?beginDate=2014-12-11&endDate=2017-12-11&UserId=1&pageNum=1&pageSize=500";
					webRequest = new WebRequest(new URL(transUrl), HttpMethod.GET);
					Page page2 = webClient.getPage(webRequest);
					
					String resPath = "E:\\crawler\\housingfund\\nanchang\\jms3.txt";
					savefile(resPath,page2.getWebResponse().getContentAsString());*/
				}
				
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
