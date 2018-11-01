package Test;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.unit.Base64Util;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {
		String url="http://www.e12345.gov.cn/portal/siportal/query/person_login.jsp";
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 

				
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='gr_loginname']");
		id_card.reset();
		id_card.setText("440202199310060620");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='gr_password']");
		id_account.reset();
		id_account.setText("lsy8881287");
		
		HtmlImage img = page.getFirstByXPath("//*[@id='gr_login_image']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='gr_login_code']");
		h.setText(inputValue);
		
		HtmlElement firstByXPath = page.getFirstByXPath("/html/body/table/tbody/tr[3]/td/table/tbody/tr[3]/td/table/tbody/tr[5]/td/div/input");
		HtmlPage page2 = firstByXPath.click();
		WebClient webClient2 = page2.getWebClient();
		String a="440202199310060620";
		String b="lsy8881287";
		String encode = Base64Util.encode(a.getBytes());
		String encode1 = Base64Util.encode(b.getBytes());
		System.out.println(encode+encode1);
//		System.out.println(page2.getWebResponse().getContentAsString());
		
		String url2="http://www.e12345.gov.cn/portal/siportal/query/person_jbxx.jsp";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page3 = webClient2.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());

	}
}
