package Test;

import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuUserInfo;
import com.module.htmlunit.WebCrawler;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="http://cx.bbzfgjj.com/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		HtmlRadioButtonInput firstByXPath3 = page.getFirstByXPath("//*[@id='RadioButtonList1_1']");
		HtmlPage page5 = firstByXPath3.click();
		
		
		HtmlTextInput firstByXPath7 = (HtmlTextInput)page5.getFirstByXPath("//*[@id='TextBox_sfzh']");
		firstByXPath7.setText("34032219910304561X");
		HtmlElement firstByXPath8 = (HtmlElement)page5.getFirstByXPath("//*[@id='ImageButton1']");
		HtmlPage page6 = firstByXPath8.click();
		System.out.println(page6.getWebResponse().getContentAsString());
		
		HtmlHiddenInput firstByXPath4 = (HtmlHiddenInput)page6.getFirstByXPath("//*[@id='__VIEWSTATE']");
		String attribute = firstByXPath4.getAttribute("value");
		System.out.println(firstByXPath4);
		System.out.println(attribute);
		String encode = URLEncoder.encode(attribute, "UTF-8");
		
		HtmlHiddenInput firstByXPath5 = (HtmlHiddenInput)page6.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
		String attribute5 = firstByXPath5.getAttribute("value");
		String encode5 = URLEncoder.encode(attribute5, "UTF-8");
		
		HtmlHiddenInput firstByXPath6 = (HtmlHiddenInput)page6.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
		String attribute6 = firstByXPath6.getAttribute("value");
		String encode6 = URLEncoder.encode(attribute6, "UTF-8");
		
		String a="__LASTFOCUS=&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode+"&__VIEWSTATEGENERATOR="+encode6+"&__EVENTVALIDATION="+encode5+"&RadioButtonList1=2&TextBox_sfzh=34032219910304561X&ImageButton1.x=29&ImageButton1.y=10";
//		String url13="http://cx.bbzfgjj.com/inputmima.aspx?dwzh=1003748+&grzh=00161";
//		String b ="__LASTFOCUS=&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUKMTQwMzQ0MjA5NQ9kFgICAw9kFggCBQ8QZGQWAQIBZAIHDw8WBB4EVGV4dGUeB0VuYWJsZWRoZGQCCQ8PFgIfAWdkZAILDw8WBB8AZR8BaGRkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQxJbWFnZUJ1dHRvbjEFDEltYWdlQnV0dG9uMrnIxdC9%2FnD%2BQRWJrBC3kDlFRpl0VNzmL0K5QxKdYPre&__VIEWSTATEGENERATOR=BBBC20B8&__EVENTVALIDATION=%2FwEdAAlq6Sfkxdgd8gV99dAKw6kp9WzmaW2Ka%2BCcTPIdwbaB%2B1RoR9lNgEd7SOq%2B8Ujsn2eWECZPQRLurjTFVUPTWENWT%2BzJdFl5zbwKWWeR1lWyG6qiF7kcjlfAtoRtHl7DFeqeCN%2FhKhURU8usu6CibpYL6ZACrx5RZnllKSerU%2BIuKmLNV%2B2mZgnOAlNG5DVTg1u17GK1D1b8BsIUq5lxh%2B4RqF9YUJGSZb3ePpSCGzQOdQ%3D%3D&RadioButtonList1=2&TextBox_sfzh=34032219910304561X&ImageButton1.x=36&ImageButton1.y=13";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestBody(a);

		
		HtmlPage page2 = webClient.getPage(webRequest);
		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("用户密码"))
		{
			
			HtmlPasswordInput firstByXPath = page2.getFirstByXPath("//*[@id='txtPass']");
			firstByXPath.setText("04561X");
			HtmlElement firstByXPath2 = page2.getFirstByXPath("//*[@id='ImageButton_gjj']");
			HtmlPage page3 = firstByXPath2.click();
			WebClient webClient2 = page3.getWebClient();
			
			DomElement elementById = page2.getElementById("form2");
			String asXml = elementById.asXml();
			String[] split = asXml.split("dwzh");
			String[] split2 = split[1].split("form2");
			String substring = split2[0].substring(1, 7);
			System.out.println(substring);
			String substring2 = split2[0].substring(19, 24);
			System.out.println(substring2);
			
			String url2="http://cx.bbzfgjj.com/cx_jieguo.aspx?dwzh="+substring+"&grzh="+substring2;
			WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
			Page page4 = webClient2.getPage(webRequest2);
			
			Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
			System.out.println(page4.getWebResponse().getContentAsString());
			HousingFundBengBuUserInfo h = new HousingFundBengBuUserInfo();
			h.setCompanyNum(doc.getElementById("dwzhLabel").text());
			h.setPersonalNum(doc.getElementById("grzhLabel").text());
			h.setCompany(doc.getElementById("dwmcLabel").text());
			h.setName(doc.getElementById("grxmLabel").text());
			h.setBankNum(doc.getElementById("dwyhzhLabel").text());
			h.setPersonalBankNum(doc.getElementById("SGrYhZh").text());
			h.setIDNum(doc.getElementById("sfzhmLabel").text());
			h.setFee(doc.getElementById("ljyeLabel").text());
			h.setStartDate(doc.getElementById("qjrqLabel").text());
			h.setEndDate(doc.getElementById("DtJzrqLabel").text());
			h.setMonthPay(doc.getElementById("dc_yjjeLabel").text());
			h.setStatus(doc.getElementById("IFcbjLabel").text());
			h.setCompanyRadio(doc.getElementById("DwblLabel").text());
			h.setPersonalRadio(doc.getElementById("GrblLabel").text());
			h.setBank(doc.getElementById("SYhTypeLabel").text());
			h.setTaskid("");
			System.out.println(h);
		}
	}
}
