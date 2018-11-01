package test;

import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class YanzhengmaTest {

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String loginurl = "http://login.189.cn/login";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);

			// 获取对应的输入框
			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin
					.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			username.setText("17388977090");
			passwordInput.setText("216832");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("------------" + asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				
				String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
				WebRequest webRequestwdzl1;
				webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
				HtmlPage wdzl1 = webclientlogin.getPage(webRequestwdzl1);
				webclientlogin = wdzl1.getWebClient();

				String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBill.parser?_z=1&cityCode=hn&fastcode=10000280";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				// 获取对应的输入框
				HtmlTextInput cardId = (HtmlTextInput) wdzl.getFirstByXPath("//input[@id='cardId']");
				HtmlTextInput userName = (HtmlTextInput) wdzl.getFirstByXPath("//input[@id='userName']");
				HtmlElement wdzlbutton = (HtmlElement) wdzl.getFirstByXPath("//a[@class='btn_big']");
				cardId.setText("230833199411240338");	
				userName.setText("齐斌");	
				//发送验证码的界面
				HtmlPage wdzlpage = wdzlbutton.click();
				//图片验证码输入框
				HtmlTextInput test = (HtmlTextInput) wdzlpage.getFirstByXPath("//input[@id='randQuery']");
				if(null==test){
					System.out.println("实名认证失败！");
				}else{
					System.out.println("实名认证成功！");
					//图片验证码输入框
					HtmlTextInput randQuery = (HtmlTextInput) wdzlpage.getFirstByXPath("//input[@id='randQuery']");
					//图片验证码
					HtmlImage validationCode4 = (HtmlImage) wdzlpage.getFirstByXPath("//img[@id='validationCode4']");
					//点击获取短信验证码input
					HtmlTextInput btnSendCode = (HtmlTextInput) wdzlpage.getFirstByXPath("//input[@id='btnSendCode']");
					
//					
//					String asXml2 = wdzlpage.asXml();
//					System.out.println(asXml2);
					
				}
				
						
			}         
		} catch (Exception e) {

		}
	}


}
