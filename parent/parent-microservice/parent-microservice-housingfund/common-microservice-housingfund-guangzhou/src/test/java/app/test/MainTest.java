package app.test;

import java.io.File;
import java.net.URL;
import java.util.Set;

import javax.swing.JOptionPane;


import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class MainTest {

	public static void main(String[] args) {
		try{
			String loginurl = "https://gzgjj.gov.cn/wsywgr/";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
			webclientlogin.getOptions().setThrowExceptionOnScriptError(false);
			webclientlogin.getOptions().setJavaScriptEnabled(false);
			HtmlImage image = pagelogin.getFirstByXPath("//img[@id='safecode']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	
			
			// 获取对应的输入框
			HtmlHiddenInput usernamehidden = (HtmlHiddenInput) pagelogin.getFirstByXPath("//input[@id='certno']");
			HtmlHiddenInput passwordInputhidden = (HtmlHiddenInput) pagelogin.getFirstByXPath("//input[@id='password']");
			DomElement username = pagelogin.getElementById("seccertno");
			DomElement passwordInput = pagelogin.getElementById("secpassword");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("/html/body/form/table[2]/tbody/tr[2]/td/table/tbody/tr/td[1]/table[2]/tbody/tr/td[2]/table/tbody/tr[7]/td[2]/table/tbody/tr/td[1]/img");
			HtmlTextInput validateCode = (HtmlTextInput)pagelogin.getFirstByXPath("//input[@id='captcha']"); 
			username.setTextContent("36220219910102442200");
			passwordInput.setTextContent("699192");
			usernamehidden.setTextContent("36220219910102442200");
			passwordInputhidden.setTextContent("699192");
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			validateCode.setText(inputValue);
			HtmlPage htmlpage = button.click();			 	
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("点击登录后的页面是：------------" + asXml);
			Set<Cookie> cookies0 = webclientlogin.getCookieManager().getCookies();
			for(Cookie cookie : cookies0){
				System.out.println("登录成功获取到的cookie是："+cookie.toString());
			}
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				String url2="https://gzgjj.gov.cn/wsywgr/TQAction!getDetailInfo.parser";
				WebRequest webRequestwdzl2 = new WebRequest(new URL(url2), HttpMethod.GET);
				webRequestwdzl2.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
				webRequestwdzl2.setAdditionalHeader("Referer", "https://gzgjj.gov.cn/wsywgr/index.jsp");
				webRequestwdzl2.setAdditionalHeader("Accept-Language", "zh-CN");
				webRequestwdzl2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				webRequestwdzl2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequestwdzl2.setAdditionalHeader("Host", "gzgjj.gov.cn");
				webRequestwdzl2.setAdditionalHeader("Connection", "Keep-Alive");
				webRequestwdzl2.setAdditionalHeader("Cache-Control", "no-cache");
				webRequestwdzl2.setAdditionalHeader("Cookie", "EV7_P6cNQkC4jYOQE5Aq_wRPsVhUwHJJtVatWkSnqbSoEmbNw4ot!403644457");
				Page wdzl2 = webclientlogin.getPage(webRequestwdzl2);
				System.out.println("最终需要的页面是："+wdzl2.getWebResponse().getContentAsString());
			}
		}catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}

}
