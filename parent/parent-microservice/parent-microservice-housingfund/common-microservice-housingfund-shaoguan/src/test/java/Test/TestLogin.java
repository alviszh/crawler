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
		String url="http://www.sggjj.com:8080/gjjnet/jsp/login.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td[2]/input");
		searchpwd.reset();
		searchpwd.setText("440202199310060620");
		
		HtmlPasswordInput searchpwd1 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[3]/td[2]/input");
		searchpwd1.reset();
		searchpwd1.setText("lsy8881287");
		
        HtmlImage img = page.getFirstByXPath("//*[@id='ValidImage']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[4]/td[2]/input");
		identifying.reset();
		identifying.setText(inputValue);
		HtmlElement button = page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[5]/td/img[1]");
		HtmlPage page2 = button.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		
		String url1="http://www.sggjj.com:8080/gjjnet/jsp/person/gjj_grjbqkcx.jsp";
		
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		Page page4 = webClient.getPage(webRequest);
		String url2="http://www.sggjj.com:8080/gjjnet/dwr/call/plaincall/PageViewAjax.rollPageDWR.dwr";
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest1);
		
		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
