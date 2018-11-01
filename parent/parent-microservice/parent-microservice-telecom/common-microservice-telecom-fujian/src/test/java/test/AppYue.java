package test;

import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class AppYue {

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
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

				String wdzlurl = "http://fj.189.cn/BillAjaxServlet.do?method=realtime&PRODNO=18060845645&PRODTYPE=50";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
				webclientlogin.getPage(webRequestwdzl);
				Page wdzl = webclientlogin.getPage(webRequestwdzl);
				String base = wdzl.getWebResponse().getContentAsString();
				System.out.println(base);

				JSONObject json = JSONObject.fromObject(base);
				String commonremaining = json.getString("OHERMONEY");
				System.out.println(commonremaining);
			}
		} catch (Exception e) {

		}
	}

}
