package test1;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class WuLuMuQiLoginTest {
	public static void main(String[] args) throws Exception { 
		String loginUrl="http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
		WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		if(null!=hPage){
			String asXml = hPage.asXml();
			Document doc = Jsoup.parse(asXml);
			String modulus = doc.getElementById("modulus").val();
			String exponent = doc.getElementById("exponent").val();
			String pwd="801013";
			RSATest rsaTest = new RSATest();
			String str = rsaTest.encrypt(pwd,modulus,exponent);  
			System.out.println("加密的密码是："+str);
			//身份证号登录
			HtmlImage image = hPage.getFirstByXPath("//img[@id='captcha_img']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			//如下验证登录信息
			String requestBody="grloginDxyz=0&username=13565860057&password="+str+"&force=&captcha="+code.trim()+"&sliderCaptcha=";
			webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			webRequest.setRequestBody(requestBody);
			Page logonPage=webClient.getPage(webRequest);
//			Thread.sleep(2000);
			if(null!=logonPage){
				String html=logonPage.getWebResponse().getContentAsString();
				System.out.println("模拟点击登陆后获取的页面是："+html);
			}
		}
	}
}
