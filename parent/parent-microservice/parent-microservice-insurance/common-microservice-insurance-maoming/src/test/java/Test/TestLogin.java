package Test;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
		String url="http://rsj.maoming.gov.cn:8999/PersonFlat/pages/login.jsp";
//		String url="http://www.sqgjj.com/Home";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			
		WebRequest requestSettings1 = new WebRequest(new URL(url), HttpMethod.GET);
//		requestSettings1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		requestSettings1.setAdditionalHeader("Cache-Control", "max-age=0");
//		requestSettings1.setAdditionalHeader("Origin", "http://ah.189.cn");
//		requestSettings1.setAdditionalHeader("Host", "www.sqgjj.com");
//		requestSettings1.setAdditionalHeader("Referer","http://www.sqgjj.com/Convenient/Index/1?security_verify_data=313336362c373638");
//		requestSettings1.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		HtmlPage page = webClient.getPage(requestSettings1);	
		System.out.println(page.getWebResponse().getContentAsString());
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='UserName']");
		id_card.reset();
		id_card.setText("440902199308221630");
		
		HtmlTextInput pwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='LoginUserName']");
		pwd.reset();
		pwd.setText("郑嘉伟");
		
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='UserPassword']");
		id_account.reset();
		id_account.setText("221630");
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='btnLogin']");
		Page page2 = firstByXPath.click();
		System.out.println(page2.getWebResponse().getContentAsString());
		String url2="http://218.22.88.62:8000/wsbsbb/person/main.html";
		
		String url3="http://rsj.maoming.gov.cn:8999/PersonFlat/person/cardManage!cardInfo.action";
//		String url3="http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/cardManage!cardInfo.action";
		
//		String url4="http://ggfw.mmrs.gov.cn:20001/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		String url4="http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action";
		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		String a ="aae140=110&aae041=2016%E5%B9%B401%E6%9C%88&aae042=2018%E5%B9%B401%E6%9C%88&page=1&pagesize=20&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D";
//		           aae140=110&aae041=2018%E5%B9%B403%E6%9C%88&aae042=2018%E5%B9%B404%E6%9C%88&page=1&pagesize=20&codetable=%5B%7B%22type%22%3A%22aae140%22%2C%22data%22%3A%22aae140%22%2C%22display%22%3A%22aae140%22%7D%2C%7B%22type%22%3A%22aae078%22%2C%22data%22%3A%22aae078%22%2C%22display%22%3A%22aae078%22%7D%5D
		WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
		requestSettings.setRequestBody(a);
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Host", "rsj.maoming.gov.cn:8999");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("Origin", "http://rsj.maoming.gov.cn:8999");
		requestSettings.setAdditionalHeader("Referer", "http://rsj.maoming.gov.cn:8999/PersonFlat/person/personPaymentInfo!paymentInfo.action");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		
		Page page4 = webClient.getPage(requestSettings);
		System.out.println(page4.getWebResponse().getContentAsString());
		
		
		if(page3.getWebResponse().getContentAsString().contains("欢迎页"))
		{
			System.out.println("-----------");
		}
	}
}
