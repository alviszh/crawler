package test;

import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class AppJifen {

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
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
			username.setText("18060845645");
			passwordInput.setText("054246");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				
				String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
				WebRequest webRequestwdzl1;
				webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
				HtmlPage wdzl1 = webclientlogin.getPage(webRequestwdzl1);
				webclientlogin = wdzl1.getWebClient();

				//三个积分的请求
				String wdzlurl = "http://fj.189.cn/service/club/vipinfo.jsp";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				String base = wdzl.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(base);
			    
				//姓名和用户状态的请求
				String wdzlurl2 = "http://fj.189.cn/service/club/queryIntegral.jsp";
				WebRequest webRequestwdzl2;
				webRequestwdzl2 = new WebRequest(new URL(wdzlurl2), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl2);
				HtmlPage wdzl2 = webclientlogin.getPage(webRequestwdzl2);
				String base2= wdzl2.getWebResponse().getContentAsString();
				Document doc2 = Jsoup.parse(base2);
				Element element = doc2.getElementsByClass("points_box").get(0);
				
				String name1 = element.select("p").get(0).text();
				String[] split = name1.split("当前状态");
				//姓名
				String name = split[0];
				//账户状态
				String accountstatus = element.select("span").get(0).html();
			    System.out.println(accountstatus);
			    
			}         
		} catch (Exception e) {

		}
	}

}
