package Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 

				
		String url="http://58.222.185.50:9009/personwork.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='account']");
		id_card.reset();
		id_card.setText("321281199011237499");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText("123456");
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='f_svl']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='captcha']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='logininfo']/div[2]/div[2]/ul/li[2]/input");
		Page page2 = firstByXPath.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		String url3="http://58.222.185.50:9009/person/yiliaozhanghu_result.html?&inputvalue=2017-02-01&inputvalue_end=2017-12-01";
		String url4="http://58.222.185.50:9009/person/monthAccountYLao.html";
		Page page3 = webClient.getPage(url3);
		System.out.println("==============================-----------------------------"+page3.getWebResponse().getContentAsString());
	}
}
