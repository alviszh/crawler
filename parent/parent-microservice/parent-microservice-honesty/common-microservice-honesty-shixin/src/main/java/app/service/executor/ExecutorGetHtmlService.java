package app.service.executor;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.module.htmlunit.WebCrawler;
/**   
*    
* 项目名称：common-microservice-executor   
* 类名称：ExecutorGetHtmlService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年7月12日 上午11:08:38   
* @version        
*/

@Component
public class ExecutorGetHtmlService {

	
	
	public final int DEFAULT_PAGE_TIME_OUT = 20000; // ms
	public final int DEFAULT_JS_TIME_OUT = 20000;
	
	public Page getByHtmlUnit(String url,HonestyTask honestyTask) throws Exception {

		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setTimeout(DEFAULT_PAGE_TIME_OUT); // 15->60
		webClient.waitForBackgroundJavaScript(DEFAULT_JS_TIME_OUT); // 5s
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		if (honestyTask.getIpaddress() != null && honestyTask.getIpport() != null) {
			webRequest.setProxyHost(honestyTask.getIpaddress());
			webRequest.setProxyPort(Integer.parseInt(honestyTask.getIpport()));
		}

//		webClient.setJavaScriptTimeout(50000);
//		webClient.getOptions().setTimeout(10000); // 15->60
		Page page = webClient.getPage(webRequest);
		
		return page;
	}
	
	public Page getByHtmlUnit2(String url,HonestyTask honestyTask) throws Exception {

		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setTimeout(DEFAULT_PAGE_TIME_OUT); // 15->60
		webClient.waitForBackgroundJavaScript(DEFAULT_JS_TIME_OUT); // 5s
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		if (honestyTask.getIpaddress() != null && honestyTask.getIpport() != null) {
			webRequest.setProxyHost(honestyTask.getIpaddress());
			webRequest.setProxyPort(Integer.parseInt(honestyTask.getIpport()));
		}

		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(10000); // 15->60
		Page page = webClient.getPage(webRequest);
		
		return page;
	}
	
	
	public Page getByHtmlUnitByWebClient(String url,HonestyTask honestyTask,WebClient webClient) throws Exception {

		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setTimeout(DEFAULT_PAGE_TIME_OUT); // 15->60
		webClient.waitForBackgroundJavaScript(DEFAULT_JS_TIME_OUT); // 5s
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		if (honestyTask.getIpaddress() != null && honestyTask.getIpport() != null) {
			webRequest.setProxyHost(honestyTask.getIpaddress());
			webRequest.setProxyPort(Integer.parseInt(honestyTask.getIpport()));
		}

//		webClient.setJavaScriptTimeout(50000);
//		webClient.getOptions().setTimeout(10000); // 15->60
		Page page = webClient.getPage(webRequest);
		
		return page;
	}
	
   	public Page getByHtmlUnitPost(String url,HonestyTask honestyTask, List<NameValuePair> paramsList) throws Exception {

    
    	
   		WebClient webClient = new WebClient(BrowserVersion.CHROME);
   		webClient.setRefreshHandler(new ThreadedRefreshHandler());
   		webClient.getOptions().setCssEnabled(false);
   		webClient.getOptions().setJavaScriptEnabled(true);
   		webClient.getOptions().setThrowExceptionOnScriptError(false);
   		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
   		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
   		webClient.getOptions().setRedirectEnabled(false);
   		webClient.getOptions().setTimeout(DEFAULT_PAGE_TIME_OUT); // 15->60
   		webClient.waitForBackgroundJavaScript(DEFAULT_JS_TIME_OUT); // 5s
   		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
   		
	
   		webClient.addRequestHeader("Host", "zxgk.court.gov.cn");
		webClient.addRequestHeader("Origin", "http://zxgk.court.gov.cn");
		webClient.addRequestHeader("Referer", "http://zxgk.court.gov.cn/shixin/index_form.do");

		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");

   		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
   		if (honestyTask.getIpaddress() != null && honestyTask.getIpport() != null) {
			webRequest.setProxyHost(honestyTask.getIpaddress());
			webRequest.setProxyPort(Integer.parseInt(honestyTask.getIpport()));
		}
   		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
   		webClient.setJavaScriptTimeout(50000);
   		webClient.getOptions().setTimeout(50000); // 15->60
   		Page page = webClient.getPage(webRequest);
   		return page;
   	}
    
    
    public static String getHtml(String url,Map<String,String> map) throws Exception{
		String html = "";
		URL gsurl = new URL(url);
		WebRequest request = new WebRequest(gsurl, HttpMethod.POST);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : map.entrySet()) {  
			list.add(new NameValuePair(entry.getKey(), entry.getValue()));  
		}  
		request.setRequestParameters(list);
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		Page page = webClient.getPage(request);
		int code = page.getWebResponse().getStatusCode();
		if(code == 200){			
			html = page.getWebResponse().getContentAsString();
		}
		return html;	
	}
   
    public  Page getHtmlByUrlForGET2(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// if (searchTask.getIpaddress() != null && searchTask.getIpport() !=
		// null) {
		// webRequest.setProxyHost(searchTask.getIpaddress());
		// webRequest.setProxyPort(Integer.parseInt(searchTask.getIpport()));
		// }

		Page page = webClient.getPage(webRequest);

		return page;
	}
	
}

