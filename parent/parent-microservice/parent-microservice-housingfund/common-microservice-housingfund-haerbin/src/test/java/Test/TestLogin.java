package Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.module.htmlunit.WebCrawler;

public class TestLogin {
	
	public static void main(String[] args) throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		
		String url="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/";
//		http://bh.hrbgjj.org.cn:47598/hrbwsyyt/getvercode.jsp?task=getvercode
//		http://bh.hrbgjj.org.cn:47598/hrbwsyyt/platform/workflow/sendMessage.jsp?uuid=1524636184154&task=send&trancode=430303&type=socket&message=%3Ccertinum%3E23100419870611142X%3C%2F%3E
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='certinum']");
		id_card.reset();
		id_card.setText("23100419870611142X");
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='perpwd']");
		id_account.reset();
		id_account.setText("121212");
		
		HtmlImage img = page.getFirstByXPath("//*[@id='tabs-1']/form/div[3]/div/span/img");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@id='vericode']");
		identifying.reset();
		identifying.setText(inputValue);
		
		
		
		
		HtmlTextInput identifying1 = (HtmlTextInput)page.getFirstByXPath("//input[@id='vcode']");
		identifying1.reset();
		identifying1.setText("");
		
		
		HtmlElement button = page.getFirstByXPath("//*[@id='tabs-1']/form/div[6]/div/button");
		HtmlPage page2 = button.click();
		String string = page2.getWebResponse().getContentAsString();
		System.out.println(string);
		Document doc = Jsoup.parse(string);
		//Elements elementById = doc.getElementById("table33").getElementById("table36").getElementsByTag("strong");
		Elements elementById1 = doc.getElementById("table33").getElementById("table36").getElementsByTag("font");

	
		System.out.println("================"+page2.getWebResponse().getContentAsString());
//		WebClient webClient2 = page2.getWebClient();
//		String url1="http://www.sygjj.com/cxxt/personalDetail/personalDetailLogin.action?idCard=210105199112073421&pafCard=6212253301000254673&identifying="+inputValue;
//		HtmlPage page3 = webClient2.getPage(url1);
//		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
