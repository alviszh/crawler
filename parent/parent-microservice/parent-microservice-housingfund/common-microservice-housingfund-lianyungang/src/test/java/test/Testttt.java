package test;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class Testttt {

//	@Autowired
//	private static ChaoJiYingOcrService chaoJiYingOcrService;
	
	public static void main(String[] args) throws Exception {
		login();
	}
	
	public static void login() throws Exception{
		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		
		String loginUrl = "https://12329.lygzfgjj.com.cn/BSS_GR/Gjjmxcx/Gjjmxcx?MenuID=1006";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
//		System.out.println(loginPage.asXml());
		HtmlTextInput usercode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='usercode']");
		HtmlPasswordInput password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='password']");
		HtmlTextInput checkCode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='checkCode']");
		HtmlImage imgVerifyCode = (HtmlImage)loginPage.getFirstByXPath("//img[@id='imgVerifyCode']");
		HtmlButton btnminlogin = (HtmlButton)loginPage.getFirstByXPath("//button[@id='btnminlogin']");
		
		File file = new File("E:\\Codeimg\\lianyungang.jpg");
		imgVerifyCode.saveAs(file);
		
		String verifycode = chaoJiYingOcrService.getVerifycode(imgVerifyCode, "6001");
		System.out.println("验证码是==》"+verifycode);
		
		usercode.setText("320723198911014467");
		password.setText("123");
		checkCode.setText(verifycode);
		HtmlPage loginedPage = btnminlogin.click();
		
		System.out.println("登陆后的页面--》"+loginedPage.asXml());
		HtmlElement error = loginedPage.getFirstByXPath("//*[@id='longinerror']");
		if(null != error){
			if(error.asText().equals(" ") && loginedPage.asXml().contains("身份证号后四位")){
				System.out.println("登录成功，可以抓取数据！");
				login2(loginedPage);
			}else{
				System.out.println("登陆失败，失败原因--》"+error.asText());
			}
		}else{
			System.out.println("登陆失败，失败原因-->网站页面不正确。");
		}
		
		
//		System.out.println("验证码是--》"+verifycode);
	}
	
	public static void login2(HtmlPage loginedPage) throws Exception{
		System.out.println("开始点击第二年份---》");
		Thread.sleep(5000);
		
		HtmlAnchor second = (HtmlAnchor)loginedPage.getFirstByXPath("//*[@id='ios']/table/tbody/tr[1]/td[2]/div/div/ul/li[2]/a");
		HtmlPage page2 = second.click();
		System.out.println("登陆后的页面2--》"+page2.asXml());
		if(page2.asXml().contains("身份证号后四位")){
			HtmlAnchor third = (HtmlAnchor)page2.getFirstByXPath("//*[@id='ios']/table/tbody/tr[1]/td[2]/div/div/ul/li[3]/a");
			HtmlPage page3 = third.click();
			System.out.println("登陆后的页面3--》"+page3.asXml());
		}
		
	}
	
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}

}
