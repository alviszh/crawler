package app.test;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class MainIntegra {

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
			username.setText("18970922391");
			passwordInput.setText("830818");
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
				
				String wdzlurl0 = "http://www.189.cn/dqmh/ssoLink.do?method=skip&platNo=10015&toStUrl=http://jx.189.cn/SsoAgent?returnPage=yecx";
				
				webclientlogin.getOptions().setJavaScriptEnabled(false);
				WebRequest webRequestwdzl0 = new WebRequest(new URL(wdzlurl0), HttpMethod.GET);
				HtmlPage wdzl0 = webclientlogin.getPage(webRequestwdzl0);
				
				Set<Cookie> cookies1 = webclientlogin.getCookieManager().getCookies();
				for(Cookie cookie : cookies1){
					System.out.println("第一个中间中间请求获取到的cookie是："+cookie);
				}
				webclientlogin = wdzl0.getWebClient();
				System.out.println("第一个中间中间请求的页面是："+wdzl0.asXml());
				
				String url2="http://jx.189.cn/jf/getUserBonus.jsp?LAN_CODE=0791&ACC_MONTH=201708&ACCOUNT_TYPE=80000045&ACCOUNT_NO=18970922391";
				WebRequest webRequestwdzl2 = new WebRequest(new URL(url2), HttpMethod.GET);
				Page wdzl2 = webclientlogin.getPage(webRequestwdzl2);
				System.out.println("最终需要的页面是："+wdzl2.getWebResponse().getContentAsString());
				String html=wdzl2.getWebResponse().getContentAsString();
				
				
				Document doc = Jsoup.parse(html);
				System.out.println("当月积分类型是："+doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text());
				System.out.println("当月积分总额是："+doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text());
				
				
			}
		}catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}

}
