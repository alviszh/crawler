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

public class TestLogin {
public static void main(String[] args) throws Exception {
	LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
			"org.apache.commons.logging.impl.NoOpLog"); 
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
			java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 

			
	String url="http://218.22.88.62:8000/wsbsbb/person/personSignUp/login.html";
	WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	HtmlPage page = webClient.getPage(url);		
	
	HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='aac003']");
	id_card.reset();
	id_card.setText("王晨晨");
	
	HtmlTextInput pwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='aac002']");
	pwd.reset();
	pwd.setText("340304198801030661");
	
	
	HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='aae099']");
	id_account.reset();
	id_account.setText("030661");
	
	HtmlImage img = page.getFirstByXPath("//*[@id='image']");
	String imageName = "111.jpg";
	File file = new File("D:\\img\\" + imageName);
	img.saveAs(file); 
	String inputValue = JOptionPane.showInputDialog("请输入验证码……");
	
	
	
	HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='verify']");
	h.setText(inputValue);
	
	
	HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='loginimg']/img");
	Page page2 = firstByXPath.click();
	
	String url2="http://218.22.88.62:8000/wsbsbb/person/main.html";
	
	String url3="http://218.22.88.62:8000/wsbsbb/person/myAccount.action";
	WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.GET);
	Page page3 = webClient.getPage(webRequest);
	System.out.println(page3.getWebResponse().getContentAsString());
	
	
	if(page3.getWebResponse().getContentAsString().contains("姓名"))
	{
		System.out.println("-----------");
	}
		
	
}
}
