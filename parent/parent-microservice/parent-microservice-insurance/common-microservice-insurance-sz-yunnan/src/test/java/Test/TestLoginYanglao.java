package Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;

public class TestLoginYanglao {

	public static void main(String[] args) throws Exception {
		String url="http://www.yn12333.gov.cn/Index.aspx?cid=49";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("/html/body/div[1]/div[4]/div[2]/ul/li[1]/input");
		id_card.reset();
		id_card.setText("532323199107020544");
		
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("/html/body/div[1]/div[4]/div[2]/ul/li[2]/input");
		id_account.reset();
		id_account.setText("0101701483");
		
		HtmlSelect h = (HtmlSelect) page.getFirstByXPath("//*[@id='ddl_originlist']");
		h.getOptionByText("昆明").click(); 
		HtmlElement button = page.getFirstByXPath("//*[@id='check']");
		Page page2 = button.click();
		Thread.sleep(1000);
//		System.out.println(page2.getWebResponse().getContentAsString());
		String url2="http://www.yn12333.gov.cn/Insurance/ProvideCare/personInfo.aspx";
		Page page3 = webClient.getPage(url2);
		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
