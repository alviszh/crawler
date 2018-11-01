package Test;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception{
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 

				
		String url="http://escsi.eszrsj.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='userName']");
		id_card.reset();
		id_card.setText("李华");
		
		HtmlTextInput pwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='userCard']");
		pwd.reset();
		pwd.setText("422822198912120012");
		
		
	
		
		HtmlImage img = page.getFirstByXPath("//*[@id='Im_Code']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='PageCodes']");
		h.setText(inputValue);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='QueryLogin']");
		HtmlPage page2 = firstByXPath.click();
		
//		WebRequest webRequest = new WebRequest(new URL(""), HttpMethod.GET);
//		Page page3 = webClient.getPage(webRequest);
		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("参保信息"))
		{
			//个人信息
//			String url2="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_General.aspx?CMD=A5FD5B2061264CF720E9345A652B8ED4";
//			WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
//			HtmlPage page3 = webClient.getPage(webRequest);
//			System.out.println(page2.getWebResponse().getContentAsString());

			
			//医疗
			String u=   "http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_YbAccCon.aspx?CMD=E11A90A1FA2A2C12";
//			System.out.println(url3);
			WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
			HtmlPage page33 = webClient.getPage(webRequest1);
			System.out.println(page33.getWebResponse().getContentAsString());
			
			
			HtmlHiddenInput firstByXPath44 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATE']");
			String attribute44 = firstByXPath44.getAttribute("value");
			String encode44 = URLEncoder.encode(attribute44, "UTF-8");
			
			HtmlHiddenInput firstByXPath55 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
			String attribute55 = firstByXPath55.getAttribute("value");
			String encode55 = URLEncoder.encode(attribute55, "UTF-8");
			
			HtmlHiddenInput firstByXPath66 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
			String attribute66 = firstByXPath66.getAttribute("value");
			String encode66 = URLEncoder.encode(attribute66, "UTF-8");
			
			String encode77 = URLEncoder.encode("2017", "UTF-8");
			String url4="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_YbAccCon.aspx?CMD=E11A90A1FA2A2C12";
			String a="&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode44+"&__VIEWSTATEGENERATOR="+encode66+"&__EVENTVALIDATION="+encode55+"&ctl00%24ContentPlaceHolder1%24QueryYears=2017&ctl00%24ContentPlaceHolder1%24QueryResultBtn=%E6%9F%A5%E8%AF%A2";
			
			WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
			requestSettings.setRequestBody(a);
			Page page4 = webClient.getPage(requestSettings);
			System.out.println(page4.getWebResponse().getContentAsString());
		}
	}
}
