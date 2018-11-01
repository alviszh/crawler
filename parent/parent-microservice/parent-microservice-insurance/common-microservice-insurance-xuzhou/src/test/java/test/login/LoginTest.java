package test.login;

import java.net.URL;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class LoginTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		//login();
		
		Yanglao();
	}
	
	public static void Yanglao() throws Exception {
		String yl = "http://www.jsxz.lss.gov.cn/ylzh.jsp?grbh=1002661217&sfzhm=320323198811160625";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setTimeout(30000);
		WebRequest webRequest = new WebRequest(new URL(yl), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
//		System.out.println(page.getWebResponse().getStatusCode());
		System.out.println(page.asXml());
		
	}

	public static WebClient login() throws Exception {
		String url = "https://www.jsxzhrss.gov.cn//personal/login.jsp?model=ylloading";

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setTimeout(30000);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		if (status == 200) {
			HtmlTextInput username = (HtmlTextInput) page.getFirstByXPath("//input[@id='grbh']");
			HtmlTextInput password = (HtmlTextInput) page.getFirstByXPath("//input[@id='sfzhm']");

			username.setText("1002661217");
			password.setText("320323198811160625");

			HtmlElement button = (HtmlElement) page.getFirstByXPath("//img[@width='78']");

			HtmlPage loadPage = button.click();

			System.out.println(loadPage.asXml());

			// System.out.println(loadPage.getWebResponse().);

		}

		return webClient;
	}

}
