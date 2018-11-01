package test.housingfund.jiuquan;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {
	

	public static void main(String[] args) throws Exception {
		
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",    "org.apache.commons.logging.impl.NoOpLog");  
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")  
            .setLevel(Level.OFF);  
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient")  
            .setLevel(Level.OFF);  
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		
		String url="http://www.zmdgjj.com/wt-web/login";
		
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		 webClient.waitForBackgroundJavaScript(10000); 
		 
//		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		id_card.reset();
		id_card.setText("412821198712040212");
		
//		HtmlSelect select = (HtmlSelect)page.getFirstByXPath("//select[@id='a001']");
//		select.setTextContent("<option value=\"00340067\">00340067</option>");
		
//		WebElement adrOption = page.getFirstByXPath("//select[@id='a001'] option[value='00340067']");
//		adrOption.click(); 
		
	
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		searchpwd.click();
		searchpwd.reset();
		searchpwd.setText("111111");
		//00eaf6edc3fe47ec74787e661845c21d8b
		
		//7ba9fec9ed8e24e6ac6fb1e4da39b76e
		//adaf84909f1213b3e3237e99a7b5fb56
//		Thread.sleep(10000);
		
//		String url3="http://www.zmdgjj.com/wt-web/zgzhLoad?username=412821198712040212";
//		Page searchPage = getPage(webClient, url3, HttpMethod.POST, null, null, null, null);
//		System.out.println(searchPage.getWebResponse().getContentAsString());
		
//		System.out.println(page.asXml());
		
		HtmlButton button = (HtmlButton)page.getFirstByXPath("//button[@id='gr_login']");
		HtmlPage page2 = button.click();
//		Thread.sleep(5000);
		System.out.println(page2.asXml());
		
		
//		String url1="http://www.zmdgjj.com/wt-web/home";
//		HtmlPage page1 = webClient.getPage(url1);	
//		System.out.println(page1.getWebResponse().getContentAsString());
		
		
//		String url1="http://www.thgjj.com/nethousing/topPage.action";
//		WebClient webClient2 = page2.getWebClient();
//		HtmlPage page3 = webClient2.getPage(url1);
//		//System.out.println(page3.getWebResponse().getContentAsString());
//		String url2="http://www.thgjj.com/nethousing/personalInformation_list.action";
//		Page page4 = webClient2.getPage(url2);
//		System.out.println(page4.getWebResponse().getContentAsString());
	}
	
	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		// webRequest.setAdditionalHeader("Accept", "*/*");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		// webRequest.setAdditionalHeader("ajaxRequest", "true");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Type",
		// "multipart/form-data");
		// webRequest.setAdditionalHeader("Host", "wsbs.dggjj.cn");
		// webRequest.setAdditionalHeader("Origin", "http://wsbs.dggjj.cn");
		// webRequest.setAdditionalHeader("Referer",
		// "http://wsbs.dggjj.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnAccInfo.jsp?psnAcc=0909149484&psnName=%25E4%25BD%2595%25E4%25BD%25A9%25E7%258E%25B2&certNo=441900198907160626&orgName=%25E4%25B8%259C%25E8%258E%259E%25E5%25B8%2582%25E6%2598%2593%25E6%2589%258D%25E4%25BA%25BA%25E5%258A%259B%25E8%25B5%2584%25E6%25BA%2590%25E9%25A1%25BE%25E9%2597%25AE%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8&psnAccSt=%25E6%25AD%25A3%25E5%25B8%25B8&bal=8580.48&orgEndPayTime=2017-09&pay=152&originalBase=1520");
		// webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT
		// 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
		// Chrome/61.0.3163.100 Safari/537.36");
		// webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}

		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();

		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}
	
}
