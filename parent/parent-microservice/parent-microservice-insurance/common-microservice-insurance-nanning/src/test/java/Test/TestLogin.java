package Test;

import java.net.URL;
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
		
		
		String url="http://222.216.5.212:8060/Cas/login?service=http://222.216.5.212:8081/siweb/userlogin.do?method=begin_dl";
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		id_card.reset();
		id_card.setText("450881198602013565");
		
		HtmlPasswordInput pwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		pwd.reset();
		pwd.setText("Lgl013565");
		
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@id='loginButton']");
		Page click = button.click();
//		System.out.println(click.getWebResponse().getContentAsString());
		String url1="http://222.216.5.212:8081/siweb/userlogin.do?method=begin_dl";
		HtmlPage page2 = webClient.getPage(url1);
//		System.out.println(page2.getWebResponse().getContentAsString());
		    
		
		HtmlTextInput text1 = (HtmlTextInput)page2.getFirstByXPath("//input[@id='text1']");
		text1.reset();
		text1.setText("13955854");
		HtmlElement button1 = (HtmlElement)page2.getFirstByXPath("//input[@id='button1']");
		Page click1 = button1.click();
		
		System.out.println(click1.getWebResponse().getContentAsString());
		
		String url2="http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin";
		Page page3 = webClient.getPage(url2);
		System.out.println(page3.getWebResponse().getContentAsString());
		
//		String cookie="td_cookie=18446744071594580152; Secure; SANGFOR=20111148; JSESSIONID=WM9PF-A3iNlxaffyPl_k-wejeRIeilzTwk73StDvfjB5CPfcBcUZ!1814317098";
		
		String url3="http://222.216.5.212:8081/siweb/rpc.do?method=doQuery";
		String body="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"nn_apply.web_v_ac20\",parameters:{\"gridid\":\"grid1\"},condition:\"WEB_V_AC20.AAE003 >='201702' and WEB_V_AC20.AAE003 <='201802' and [WEB_V_AC20_AAC001]='13955854' and WEB_V_AC20.AAE114 ='1'\"}},parameters:{\"synCount\":\"true\"}}}";
		WebRequest webRequest2 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest2.setRequestBody(body);
		webRequest2.setAdditionalHeader("Accept", "*/*");
//		webRequest2.setAdditionalHeader("Cookie", "td_cookie=18446744071594580152; Secure; SANGFOR=20111148; JSESSIONID=WM9PF-A3iNlxaffyPl_k-wejeRIeilzTwk73StDvfjB5CPfcBcUZ!1814317098");
		
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("ajaxRequest", "true");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Origin", "http://222.216.5.212:8081");
		webRequest2.setAdditionalHeader("Referer", "http://222.216.5.212:8081/siweb/emp_payinof_query.do?method=begin");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page4 = webClient.getPage(webRequest2);
		System.out.println(page4.getWebResponse().getContentAsString());
	}
}
