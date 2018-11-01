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

public class Testlogin {

	public static void main(String[] args) throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		String loginUrl = "http://www.hnylbx.com:33002/siq/indexsz.jsp?zoneCode=419900";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(loginUrl);		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='perUserCode']");
		id_card.reset();
		id_card.setText("hujuhua2017");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='perPassword']");
		id_account.reset();
		id_account.setText("hujuhua2017");
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='perVaidImg']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='perVaidImgText']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='perLoginBtn']");
		Page page2 = firstByXPath.click();
		String url="http://www.hnylbx.com:33002/siq/web/szpersonHome_seeInsuper.action?insuperType=3";
		String url1="http://www.hnylbx.com:33002/siq/web/szpersonHome_personHome.action";
		
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());

	}
	
	
}
