package Test;

import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TsetLogin {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		String url="http://202.100.251.116:8880/uaa/views/person/login.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
//		System.out.println(page.getWebResponse().getContentAsString());
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("//*[@id='idnumber']");
		id_account.reset();
		id_account.setText("460102199110021529");
		
		
		HtmlTextInput id_account1 = (HtmlTextInput)page.getFirstByXPath("//*[@id='mobilenumber']");
		id_account1.reset();
		id_account1.setText("13698922625");
		
		HtmlPasswordInput img = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='inputPassword']");
		img.reset();
		img.setText("123456");
		
		
		HtmlElement button = page.getFirstByXPath("//*[@id='loginForm']/div[3]/div[2]/div[10]/button");
		Page page2 = button.click();
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
//		String url2="http://218.2.15.138:8090/socialSecurity/index.jsp";
//		Page page3 = webClient.getPage(url2);
//		System.out.println(page3.getWebResponse().getContentAsString());

	}
}
