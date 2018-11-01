package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		String url="http://www.mdjzfgjj.cn/mdjweb/grcxdl.jhtml";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlSelect select = (HtmlSelect) page.getElementById("selet");
        HtmlOption option = select.getOptionByText("身份证号");
        System.out.println(option+"----------------------------------===========================");
        option.click();
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='certinum']");
		id_card.reset();
		id_card.setText("231084199003134023");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		searchpwd.reset();
		searchpwd.setText("111111");
		
		HtmlImage img = page.getFirstByXPath("//img[@id='image']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='verify']");
		yzm.reset();
		yzm.setText(inputValue);
		
		HtmlElement button = page.getFirstByXPath("//input[@id='submit_button']");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		Thread.sleep(1000);
		WebClient webClient2 = page2.getWebClient();
		
		String urlInfo ="http://www.mdjzfgjj.cn/mdjweb/website/trans/gjjquery.do?className=TRC311014&accnum=113028310071&time="+System.currentTimeMillis();
		Page page4 = webClient2.getPage(urlInfo);
		//System.out.println(page4.getWebResponse().getContentAsString());
	    String url2="http://www.mdjzfgjj.cn/mdjweb/website/trans/gjjquery.do?time=1512611773840&className=TRC311015&accnum=113028310071"; 
	    Page page32 = webClient2.getPage(url2);
	    
		String url1 ="http://www.mdjzfgjj.cn/mdjweb/website/trans/gjjquery.do?className=TRC311015&time="+System.currentTimeMillis()+"&accnum=113028310071&password=111111&mark=1&txt=1&verify="+inputValue+"&CI_Pagenum=1";
		Page page3 = webClient2.getPage(url1);
		Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
		System.out.println(doc.text());
	}
	
	
}
