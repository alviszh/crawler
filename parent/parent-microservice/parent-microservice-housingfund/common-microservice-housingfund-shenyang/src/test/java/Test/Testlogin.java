package Test;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Testlogin {

	public static void main(String[] args) throws Exception {
		String url="http://personal.sygjj.com/login.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput idCard = (HtmlTextInput)page.getFirstByXPath("//input[@id='idCard']");
		idCard.reset();
		idCard.setText("210105199112073421");
		HtmlTextInput pafCard = (HtmlTextInput)page.getFirstByXPath("//input[@id='pafCard']");
		pafCard.reset();
		pafCard.setText("6212253301000254673");
		
		HtmlImage img = page.getFirstByXPath("//img[@id='code']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@id='identifying']");
		identifying.reset();
		identifying.setText(inputValue);
		HtmlElement button = page.getFirstByXPath("//input[@type='submit']");
		HtmlPage page2 = button.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		WebClient webClient2 = page2.getWebClient();
		String url1="http://www.sygjj.com/cxxt/personalDetail/personalDetailLogin.parser?idCard=210105199112073421&pafCard=6212253301000254673&identifying="+inputValue;
		HtmlPage page3 = webClient2.getPage(url1);
		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
