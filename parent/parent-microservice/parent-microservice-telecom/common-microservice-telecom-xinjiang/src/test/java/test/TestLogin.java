package test;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";	
		HtmlPage  searchPage=getHtml(url,webClient);
		HtmlTextInput username = (HtmlTextInput) searchPage.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) searchPage.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) searchPage.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("18999891510");
    	passwordInput.setText("415505");
		HtmlPage loginpage = button.click();
		webClient=loginpage.getWebClient();
		HtmlPage htmlPage=getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000795", webClient);	
		if (200==htmlPage.getWebResponse().getStatusCode()) {
			getVoice(webClient);			
			//getNumber(webClient);
		}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}



	public static Page getVoice(WebClient webClient) throws Exception {
		//String url="http://shop.xj.189.cn:8081/xjwt_webapp/billQueryController/getmonthQuery.do";	
		
		WebRequest requestSettings = new WebRequest(new URL("http://shop.xj.189.cn:8081/xjwt_webapp/billQueryController/getmonthQuery.do"), HttpMethod.POST);

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("number","18999891510"));
		paramsList.add(new NameValuePair("AreaCode", "0991"));	
		paramsList.add(new NameValuePair("ListType", "0"));
		paramsList.add(new NameValuePair("queryDate", "201810"));
		paramsList.add(new NameValuePair("deviceType", "25"));			
		paramsList.add(new NameValuePair("PageIndex", "0"));		
		paramsList.add(new NameValuePair("PageSize", "20"));	
		paramsList.add(new NameValuePair("telephoneCode", ""));	
		paramsList.add(new NameValuePair("photoCode", ""));			

		
		requestSettings.setAdditionalHeader("Accept", "text/plain, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8"); 
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		requestSettings.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
		requestSettings.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
		requestSettings.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billing/historybill/HistoryBill.jsp?fastcode=10000331&cityCode=xj"); 
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Content-Type", "application/json"); 
		requestSettings.setAdditionalHeader("Connection", "keep-alive"); 
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");		
		requestSettings.setRequestParameters(paramsList);
		Page page = webClient.getPage(requestSettings);
		Thread.sleep(1000);
		System.out.println("返回结果为");
		System.out.println(page.getWebResponse().getContentAsString());
		return page;
	}
	
	
	public static HtmlPage getNumber(WebClient webClient) throws Exception {
		WebRequest requestSettings = new WebRequest(new URL("http://shop.xj.189.cn:8081/xjwt_webapp/PayCostsRecordQueryControllerNew/payCostsRecordQuery.do"), HttpMethod.POST);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("date", "201709"));
		paramsList.add(new NameValuePair("phone", "105017992115"));
		paramsList.add(new NameValuePair("deviceType", "25"));
		paramsList.add(new NameValuePair("areaCode", "0991"));	
		
		requestSettings.setAdditionalHeader("Host", "shop.xj.189.cn:8081");
		requestSettings.setAdditionalHeader("Origin", "http://shop.xj.189.cn:8081");
		requestSettings.setAdditionalHeader("Referer", "http://shop.xj.189.cn:8081/xjwt_webapp/pcapp/service/billingNew/czjl.jsp?fastcode=20000797&cityCode=xj");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setRequestParameters(paramsList);
	    HtmlPage searchPage = webClient.getPage(requestSettings);
		System.out.println(searchPage.getWebResponse().getContentAsString(Charset.forName("UTF-8")));
		return searchPage;
	}
	
	public static Page gettmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;

	}
}
