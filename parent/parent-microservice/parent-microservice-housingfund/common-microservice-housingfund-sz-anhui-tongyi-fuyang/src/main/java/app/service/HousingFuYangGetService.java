package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import app.commontracerlog.TracerLog;


/**   
*    
* 项目名称：common-microservice-housingfund-sz-anhui-tongyi-fuyang   
* 类名称：HousingFuYangGetService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年5月3日 下午5:13:16   
* @version        
*/

@Component
@Service
@EnableAsync
public class HousingFuYangGetService {

	@Autowired
	public TracerLog tracerLog;

	
	public  String getPersionId(WebClient webClient) throws Exception {
		String url = "http://wx.gjj.fy.cn/hfmis_wt/personal/jbxx";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);

		Pattern p = Pattern.compile("[\\d]{9}"); // 得到字符串中的数字
		Matcher m = p.matcher(searchPage.getWebResponse().getContentAsString());
		if (m.find()) {			
			return m.group();
		}
		return null;
	}
	
	public  Page getPesionInfo(WebClient webClient,String persionid) throws Exception{
		String url = "http://wx.gjj.fy.cn/hfmis_wt/common/zhfw/invoke/020101";
		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("grzh", persionid.trim()));
		paramsList.add(new NameValuePair("random", ""));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		
		
		return searchPage;
	}
	
	public  Page getPay(WebClient webClient,String persionid) throws Exception{
		String url = "http://wx.gjj.fy.cn/hfmis_wt/common/zhfw/invoke/020102";
		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("filterscount", "0"));
		paramsList.add(new NameValuePair("groupscount", "0"));
		paramsList.add(new NameValuePair("pagenum", "0"));
		paramsList.add(new NameValuePair("pagesize", "1000"));
		paramsList.add(new NameValuePair("recordstartindex", "0"));
		paramsList.add(new NameValuePair("recordendindex", "20"));
		paramsList.add(new NameValuePair("grzh", persionid.trim()));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);		
		
		return searchPage;
	}
}
