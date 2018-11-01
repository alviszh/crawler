package app.service;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
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
	
    @Retryable(value = Exception.class, maxAttempts = 3)
	public Page getByHtmlUnit(String url,HttpProxyBean httpProxyBean) throws Exception {

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
//		if (searchTask.getIpaddress() != null && searchTask.getIpport() != null) {
//			webRequest.setProxyHost(searchTask.getIpaddress());
//			webRequest.setProxyPort(Integer.parseInt(searchTask.getIpport()));
//		}

//		webClient.setJavaScriptTimeout(50000);
//		webClient.getOptions().setTimeout(10000); // 15->60
		Page page = webClient.getPage(webRequest);
		
		return page;
	}
    
    @Retryable(value = Exception.class, maxAttempts = 3)
   	public Page getByHtmlUnitPost(String url,HttpProxyBean httpProxyBean, List<NameValuePair> paramsList) throws Exception {

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

   		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
   		if (httpProxyBean!=null) {
   			webRequest.setProxyHost(httpProxyBean.getIp());
   			webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
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
   
	
}

