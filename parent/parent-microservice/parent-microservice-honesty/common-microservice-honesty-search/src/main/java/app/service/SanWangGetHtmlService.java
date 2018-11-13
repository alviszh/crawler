package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.search.SearchTask;

import app.service.unit.SanWangUnitService;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：GetHtmlService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月17日 下午6:06:23   
* @version        
*/
@Component
public class SanWangGetHtmlService {

	public final int DEFAULT_PAGE_TIME_OUT = 50000; // ms
	public final int DEFAULT_JS_TIME_OUT = 50000;
	
	@Autowired
	private SanWangUnitService sanWangUnitService;
	
	
	public String getHtmlByUrlForGET(SearchTask searchTask) throws Exception{

		String url = sanWangUnitService.toUtf8String(searchTask.getLinkurl());
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
		if (searchTask.getIpaddress() != null && searchTask.getIpport() != null) {
			webRequest.setProxyHost(searchTask.getIpaddress());
			webRequest.setProxyPort(Integer.parseInt(searchTask.getIpport()));
		}

//		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(10000); // 15->60
		Page page = webClient.getPage(webRequest);
		webClient.close();// 关闭webclient 防止资源占用
		return page.getWebResponse().getContentAsString();
	}
	
	public  String getDocByHtmlunitFalse(SearchTask searchTask,String linkurl) throws Exception{
		
		String url = sanWangUnitService.toUtf8String(searchTask.getLinkurl());

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
		if (searchTask.getIpaddress() != null && searchTask.getIpport() != null) {
			webRequest.setProxyHost(searchTask.getIpaddress());
			webRequest.setProxyPort(Integer.parseInt(searchTask.getIpport()));
		}

//		webClient.setJavaScriptTimeout(10000);
		webClient.getOptions().setTimeout(10000); // 15->60
		Page page = webClient.getPage(webRequest);
		webClient.close();// 关闭webclient 防止资源占用

		return page.getWebResponse().getContentAsString();
	}

}
