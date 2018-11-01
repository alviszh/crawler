package Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		String url="http://www.yn12333.gov.cn/Index.aspx?cid=54";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		HtmlPage page = webClient.getPage(url);		
		
	    HtmlSelect h = (HtmlSelect) page.getElementById("ddl_originlist");
	    HtmlPage optionByText = h.getOptionByText("玉溪").click(); 
//		HtmlPage click = h.getOptionByValue("5304").click();
		System.out.println(optionByText.asXml());
	}
}
