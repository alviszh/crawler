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

	public static void main(String[] args) throws Exception{
		String url="http://202.101.185.218/inner/bsdt/nologin.html";
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 

				
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='loginname']");
		id_card.reset();
		id_card.setText("33062119900727690X");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='loginpwd']");
		id_account.reset();
		id_account.setText("王映映");
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='yzmsrc']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='loginyzm']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='registerBtn']");
		Page page2 = firstByXPath.click();
		Page page3 = webClient.getPage("http://202.101.185.218/inner/bsdt/bsdt_sbxxcx.html?1520321171798637556358");
		String url1="http://202.101.185.218/web/pages/sxrs/Win_ZgJfxx.jsp";
		String url2="http://202.101.185.218/web/sxrs/ZgJfxx.action?_pageLines=53&xzdm=05";
		Page page4 = webClient.getPage(url2);
		System.out.println(page4.getWebResponse().getContentAsString());
//		if(page3.getWebResponse().getContentAsString().contains("欢迎进入泸州社保网上服务大厅"))
//		{
//			Page page5 = webClient.getPage("http://www.sclzsi.cn/user/center.do");
//			System.out.println(page5.getWebResponse().getContentAsString());
//		}
	}
}
