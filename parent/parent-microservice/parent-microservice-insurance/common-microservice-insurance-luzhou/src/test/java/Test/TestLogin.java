package Test;

import java.io.File;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

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

				
		String url="http://www.sclzsi.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='username']");
		id_card.reset();
		id_card.setText("18281128587");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText("xx123456");
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='randImg']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='codes']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='loginBody']/div/div[2]/div[3]/div[5]/input");
		Page page2 = firstByXPath.click();
		Page page3 = webClient.getPage("http://202.101.185.218/inner/bsdt/bsdt_sbxxcx.html?1520321171798637556358");
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("欢迎进入泸州社保网上服务大厅"))
		{
			Page page5 = webClient.getPage("http://www.sclzsi.cn/user/center.do");
			System.out.println(page5.getWebResponse().getContentAsString());
		}
		
//		Page page4 = webClient.getPage("http://www.sclzsi.cn/user/center.do");
//		System.out.println(page4.getWebResponse().getContentAsString());
//		Page page5 = webClient.getPage("http://www.sclzsi.cn/query/inquire.do");
//		System.out.println(page5.getWebResponse().getContentAsString());
//		Page page6 = webClient.getPage("http://www.sclzsi.cn/query/ml/lnjfqk.do");
//		System.out.println(page6.getWebResponse().getContentAsString());

	}
}
