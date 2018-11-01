package Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

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
		String url="https://puser.zjzwfw.gov.cn/sso/usp.do?action=ssoLogin&servicecode=njdh";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='sfzh']");
		id_card.reset();
		id_card.setText("511602199408281123");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pswd']");
		searchpwd.reset();
		searchpwd.setText("035872");
		
		
        HtmlImage img = page.getFirstByXPath("//*[@id='yzm']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@name='code']");
		identifying.reset();
		identifying.setText(inputValue);
		
		
		
		
		HtmlElement button = page.getFirstByXPath("//input[@id='add_submit']");
		HtmlPage page2 = button.click();
		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("欢迎访问台州市住房公积金网站"))
		{
			
		}
		
//		String url1="http://www.thgjj.com/nethousing/topPage.action";
//		WebClient webClient2 = page2.getWebClient();
//		HtmlPage page3 = webClient2.getPage(url1);
//		System.out.println(page4.getWebResponse().getContentAsString());
	}
}
