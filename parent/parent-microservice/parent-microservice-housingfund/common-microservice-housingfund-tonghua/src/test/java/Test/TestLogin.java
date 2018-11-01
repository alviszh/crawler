package Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception{
		String url="http://www.thgjj.com/";
//		http://www.thgjj.com/
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='userName']");
		id_card.reset();
		id_card.setText("220519196903110565");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='passWord']");
		searchpwd.reset();
		searchpwd.setText("123456");
		
		HtmlElement button = page.getFirstByXPath("//input[@name='loginSubmit']");
		HtmlPage page2 = button.click();
		Thread.sleep(5000);
		//System.out.println(page2.getWebResponse().getContentAsString());
		
		String url1="http://www.thgjj.com/nethousing/topPage.action";
		WebClient webClient2 = page2.getWebClient();
		HtmlPage page3 = webClient2.getPage(url1);
		//System.out.println(page3.getWebResponse().getContentAsString());
		String url2="http://www.thgjj.com/nethousing/personalInformation_list.action";
		Page page4 = webClient2.getPage(url2);
		System.out.println(page4.getWebResponse().getContentAsString());
		
	}
}
