package Test;

import java.io.File;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;


public class TestLogin {

	public static void main(String[] args) throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		String url="http://218.2.15.138:8090/socialSecurity/loginsi.jsp";
//		http://218.2.15.138:8090/socialSecurity/LoginAction.do?method=loginsi
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
//		System.out.println(page.getWebResponse().getContentAsString());
		
		HtmlTextInput id_card = (HtmlTextInput)page.getElementById("iCard");
		id_card.reset();
		id_card.setText("230904199410131328");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText("131328");
		
		HtmlImage img = page.getFirstByXPath("//*[@id='kaptchaImage1']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='kaptcha']");
		h.setText(inputValue);
		HtmlElement button = page.getFirstByXPath("//*[@id='loginForm']/div[6]/a");
		Page page2 = button.click();
		Thread.sleep(1000);
//		System.out.println(page2.getWebResponse().getContentAsString());
		String url2="http://218.2.15.138:8090/socialSecurity/index.jsp";
		Page page3 = webClient.getPage(url2);
		System.out.println(page3.getWebResponse().getContentAsString());
		
		String url3="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryJbxx&_="+System.currentTimeMillis();
		String url4="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&sign=init&_="+System.currentTimeMillis();
		String url5="http://218.2.15.138:8090/socialSecurity/GrcxAction.do?method=queryYjsjxx&xz=31&jsq=2018";
		Page page4 = webClient.getPage(url5);
		System.out.println(page4.getWebResponse().getContentAsString());
		

	}
}
