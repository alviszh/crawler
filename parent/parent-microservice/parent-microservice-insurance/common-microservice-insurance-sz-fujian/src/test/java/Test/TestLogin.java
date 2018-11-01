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

				
		String url="http://www.fj12333.gov.cn:268/fwpt/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='aac003']");
		id_card.reset();
		id_card.setText("游晓芬");
		
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("//*[@id='aac002']");
		id_account.reset();
		id_account.setText("350600198611021521");
		
		HtmlPasswordInput id_account2 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='ysc002']");
		id_account2.reset();
		id_account2.setText("528671lhy");
		
//		http://www.fj12333.gov.cn:268/fwpt/Num.jsp?rnd=0.10192656990569104
		System.out.println(page.getWebResponse().getContentAsString());
		HtmlImage img = page.getFirstByXPath("//*[@id='checkImg']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='randCode']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='loginimg']/img");
		Page page2 = firstByXPath.click();
		System.out.println(page2.getWebResponse().getContentAsString());
	}
}
