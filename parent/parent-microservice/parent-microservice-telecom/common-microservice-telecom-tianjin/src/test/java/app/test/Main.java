package app.test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Main {

	public static void main(String[] args) {
		try{
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
			username.setText("18102027078");
			passwordInput.setText("488959");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("------------" + asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				String wdzlurl0 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02241349&cityCode=tj";
				WebRequest webRequestwdzl0;
				webRequestwdzl0 = new WebRequest(new URL(wdzlurl0), HttpMethod.GET);
				HtmlPage wdzl0 = webclientlogin.getPage(webRequestwdzl0);
				webclientlogin = wdzl0.getWebClient();
				
				//用户
//				String wdzlurl = "http://tj.189.cn/tj/service/manage/modifyUserInfo.action?fastcode=02241349&amp;cityCode=tj";
				//通话
				String wdzlurl = "http://tj.189.cn/tj/service/bill/billDetailQuery.parser?billDetailValidate=true&flag_is1k2x=false&billDetailType=1&sRandomCode=111111&randInputValue=1111&pageNo=1&rows=2000&startTime=2017-09-01&endTime=2017-09-14&exFormat=1";
				//短信
//				String wdzlurl = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=2&sRandomCode=&randInputValue=%E8%AF%B7%E7%82%B9%E5%87%BB&startTime=2017-06-01&endTime=2017-06-30&exFormat=1";
				
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				String asXml2 = wdzl.asXml();
				System.out.println(asXml2);
			}
		}catch (Exception e) {
			
		}
	}

}
