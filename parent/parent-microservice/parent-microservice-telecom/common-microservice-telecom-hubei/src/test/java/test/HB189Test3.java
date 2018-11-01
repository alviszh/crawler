package test;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;

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

public class HB189Test3 {

	public static void main(String[] args) throws Exception {

//		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		long startWC = System.currentTimeMillis(); 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(true);  
	    // 2 禁用Css，可避免自动二次请求CSS进行渲染  
	    webClient.getOptions().setCssEnabled(false);  
	    // 3 启动客户端重定向  
	    webClient.getOptions().setRedirectEnabled(false);  	  
	    // 4 js运行错误时，是否抛出异常  
	    webClient.getOptions().setThrowExceptionOnScriptError(false);  
		
		long endWC = System.currentTimeMillis(); 
		System.out.println("初始化WC用时:" + (endWC-startWC)); 
		
		String url = "http://hb.189.cn/pages/selfservice/custinfo/userinfo/userInfo.action?trackPath=SYDH";
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);	
		
		webRequest.setAdditionalHeader("Host", "hb.189.cn"); 
		HtmlPage htmlpage = webClient.getPage(webRequest);  
		String currentPage = htmlpage.getUrl().toString();
		System.out.println(currentPage+"----------loginpage----------"+htmlpage.asXml());
		http://hb.189.cn/toLogin?toPageID=URL_USERINFOJUMP
		if(currentPage.contains("http://hb.189.cn/toLogin")){
			
			String loginUrl="http://login.189.cn/web/login";
			webRequest = new WebRequest(new URL(loginUrl),HttpMethod.GET);	
			htmlpage = webClient.getPage(webRequest);  
			System.out.println("------------------登录失效，重新登录--------------------");			
			HtmlTextInput username = (HtmlTextInput) htmlpage.getFirstByXPath("//input[@id='txtAccount']");
			HtmlElement htmlElement = (HtmlTextInput)htmlpage.getFirstByXPath("//input[@id='txtShowPwd']");
			htmlElement.click();
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) htmlpage.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) htmlpage.getFirstByXPath("//a[@id='loginbtn']");
			
			username.setText("17786472180");
			passwordInput.setText("73307533"); 
			htmlpage = button.click();
			String currentUrl = htmlpage.getUrl().toString();
			System.out.println(currentUrl);
			webClient=htmlpage.getWebClient();
			System.out.println(htmlpage+"---------------------"+htmlpage.asXml());
			////////////////////////////////////////////////////////
			/*String feesyue11 = "http://hb.189.cn/pages/selfservice/feesquery/feesyue.jsp";
			WebRequest webRequest11 = new WebRequest(new URL(feesyue11),HttpMethod.GET);
			webRequest11.setAdditionalHeader("Host", "login.189.cn"); 
			webRequest11.setAdditionalHeader("Referer", "http://login.189.cn/web/login");
			HtmlPage feesyuePage11 = webClient.getPage(webRequest11);  
			String currentPage11 = feesyuePage11.getUrl().toString();
			System.out.println(currentPage11+"----------feesyue11----------"+feesyuePage11.asXml());*/
			
		}else{
			System.out.println("222222");
		}
		
//		String userinfo = "http://hb.189.cn/pages/selfservice/business/coo/functiondisplay/oldBussiness.jsp";
//		WebRequest webRequest4 = new WebRequest(new URL(userinfo),HttpMethod.GET);	
//		HtmlPage userinfoPage = webClient.getPage(webRequest4);  
//		Thread.sleep(1000);
//		String currentPage2 = userinfoPage.getUrl().toString();
//		System.out.println(currentPage2);
//		System.out.println("----------userinfoPage----------"+userinfoPage.asXml());
		
//		String month="201707";
//		String url5 = "http://hb.189.cn/pages/selfservice/feesquery/newBOSSQueryCustBill.action?billbeanos.citycode=027&billbeanos.btime="
//				+ month + "&billbeanos.accnbr=15392835199" 
//				+ "&skipmethod.cityname=%CE%E4%BA%BA&billbeanos.paymode=2";
//		WebRequest webRequest5 = new WebRequest(new URL(url5),HttpMethod.GET);
//		HtmlPage page5 = webClient.getPage(webRequest5);  
//		System.out.println(page5.asXml());
		
//		String payUrl="http://hb.189.cn/pages/selfservice/qrybill/rechargeHistoryAjax/queryRH.action";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accNbr","50:15392835199"));
//		paramsList.add(new NameValuePair("billing_Cycle", "201709"));			
//		paramsList.add(new NameValuePair("queryType", "1"));
//		WebRequest webRequest5 = new WebRequest(new URL(payUrl), HttpMethod.POST);		
//		webRequest5.setAdditionalHeader("Host", "hb.189.cn");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/qrybill/rechargeHistory/queryRechargeHistory.action");
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
//		webRequest5.setCharset(Charset.forName("UTF-8")); 		
//		webRequest5.setRequestParameters(paramsList);
//		Page page5 = webClient.getPage(webRequest5);	
//		String html2 = page5.getWebResponse().getContentAsString();
//		System.out.println(html2);
		
//		String payUrl="http://hb.189.cn/service/integral/qryResult.action";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("qryMonth","201707"));
//		WebRequest webRequest5 = new WebRequest(new URL(payUrl), HttpMethod.POST);		
//		webRequest5.setAdditionalHeader("Host", "hb.189.cn");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Referer", "http://hb.189.cn/service/integral/qryIndex.action");
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
//		webRequest5.setCharset(Charset.forName("UTF-8")); 		
//		webRequest5.setRequestParameters(paramsList);
//		Page page5 = webClient.getPage(webRequest5);	
//		String html2 = page5.getWebResponse().getContentAsString();
//		System.out.println(html2);
		
		//String smsUrl="http://hb.189.cn/feesquery_PhoneIsDX.action?productNumber=15392835199&cityCode=0127&sentType=C&ip=0";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("productNumber","15392835199"));
//		paramsList.add(new NameValuePair("cityCode", "0127"));
//		paramsList.add(new NameValuePair("sentType", "C"));		
//		paramsList.add(new NameValuePair("ip", "0"));		
//		WebRequest webRequest5 = new WebRequest(new URL(smsUrl), HttpMethod.POST);				
//		webRequest5.setAdditionalHeader("Accept", "text/plain, */*");
//		webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest5.setAdditionalHeader("Connection", "keep-alive");
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest5.setAdditionalHeader("Host", "hb.189.cn");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Referer", "hhttp://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
//		webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		//webRequest5.setRequestParameters(paramsList);
//		Page page5 = webClient.getPage(webRequest5);		
//		System.out.println(page5.getWebResponse().getContentAsString());
		
		
//		String url5 = "http://hb.189.cn/feesquery_checkCDMAFindWeb.action";			
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("random", "858729"));
//		paramsList.add(new NameValuePair("sentType", "C"));	
//		WebRequest webRequest5 = new WebRequest(new URL(url5), HttpMethod.POST);
//		webRequest5.setAdditionalHeader("Host", "hb.189.cn");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Connection", "keep-alive");
//		webRequest5.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
//		webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*"); 
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		webRequest5.setRequestParameters(paramsList);
//		Page page5 = webClient.getPage(webRequest5);	
//		System.out.println(page5.getWebResponse().getContentAsString());
		
		
//		String url5="http://hb.189.cn/feesquery_querylist.action";									
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("startMonth", "2017090000"));
//		paramsList.add(new NameValuePair("type", "1"));
//		paramsList.add(new NameValuePair("random", "951978"));					
//		WebRequest webRequest5 = new WebRequest(new URL(url5), HttpMethod.POST);
//		webRequest5.setAdditionalHeader("Host", "hb.189.cn");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
//		webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
//		webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate"); 		
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
//		webRequest5.setAdditionalHeader("Connection", "keep-alive"); 
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
//		webRequest5.setRequestParameters(paramsList);
//		HtmlPage page5 = webClient.getPage(webRequest5);
//		System.out.println(page5.asXml());
		
//		String url5="http://hb.189.cn/feesquery_querylist.action";	
//		WebRequest webRequest5 = new WebRequest(new URL(url5), HttpMethod.POST);	
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("startMonth","2017090000"));
//		paramsList.add(new NameValuePair("type", "7"));
//		paramsList.add(new NameValuePair("random","951978"));							
//		webRequest5.setAdditionalHeader("Accept", "*/*");
//		webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest5.setAdditionalHeader("Connection", "keep-alive");
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest5.setAdditionalHeader("Host", "hb.189.cn");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
//		webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		webRequest5.setRequestParameters(paramsList);
//		HtmlPage page5 = webClient.getPage(webRequest5);
//		System.out.println(page5.asXml());
		
//		String url5="http://hb.189.cn/feesquery_pageQuery.parser?page=2&showCount=20";
////		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
////		paramsList = new ArrayList<NameValuePair>();
////		paramsList.add(new NameValuePair("page", "2"));
////		paramsList.add(new NameValuePair("showCount", "20"));			
//		WebRequest webRequest5 = new WebRequest(new URL(url5), HttpMethod.GET);		
//		webRequest5.setAdditionalHeader("Accept", "*/*");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest5.setAdditionalHeader("Origin", "http://hb.189.cn");
//		webRequest5.setAdditionalHeader("Referer", "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp"); 
//		webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded"); 
//		webRequest5.setAdditionalHeader("Connection", "keep-alive"); 
//		webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	
//		//webRequest5.setRequestParameters(paramsList);
//		HtmlPage page5 = webClient.getPage(webRequest5);
//		System.out.println(page5.asXml());
		
	}
	
	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			return searchPage;
		} 
		return null;
	}
	
	public Page gettmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			
			return null;
		}

	}

}
