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

public class TestS {

	public static void main(String[] args) throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		
		String url="http://www.sqgjj.com/Convenient/Index/1 ";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='PCard']");
		id_card.reset();
		id_card.setText("411422198602066693");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='PPwd']");
		id_account.reset();
		id_account.setText("111111");
		
		HtmlImage img = page.getFirstByXPath("//*[@id='PimgCode']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@id='PCode']");
		identifying.reset();
		identifying.setText(inputValue);
		
		
		HtmlElement button = page.getFirstByXPath("//*[@id='PSearch']");
		HtmlPage page2 = button.click();
		String string = page2.getWebResponse().getContentAsString();
//		System.out.println(string);
		
		String url2="http://www.sqgjj.com/Person/Index?PCard=411422198602066693&PType=1&PCard=411422198602066693&PType=1";
		Page page3 = webClient.getPage(url2);
//		System.out.println(page3.getWebResponse().getContentAsString());
		
		String url3="http://www.sqgjj.com/Person/GetJCQKInfo?pcard=411422198602066693&ptype=1";
		Page page4 = webClient.getPage(url3);
		System.out.println(page4.getWebResponse().getContentAsString());
	}
}
