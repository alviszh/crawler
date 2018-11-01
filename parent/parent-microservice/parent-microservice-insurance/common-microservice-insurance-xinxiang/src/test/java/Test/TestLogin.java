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

				
		String url="https://www.haxx.lss.gov.cn/xxsshbz/userLogin/toLoginPage";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='userName']");
		id_card.reset();
		id_card.setText("410711198306271031");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText("111111");
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='random']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='randnum']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("/html/body/div[2]/div/div[2]/form/div/div[5]/button");
		Page page2 = firstByXPath.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
		String url2="https://www.haxx.lss.gov.cn/xxsshbz/userLogin/index";
		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
//		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("个人中心"))
		{
			String url1="https://www.haxx.lss.gov.cn/xxsshbz/endowment/queryEndowmentPayInfo?pageNum=1&pageSize=1000";
			WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.POST);
			Page page1 = webClient.getPage(webRequest1);
			System.out.println(page1.getWebResponse().getContentAsString());
		}
		
	}
}
