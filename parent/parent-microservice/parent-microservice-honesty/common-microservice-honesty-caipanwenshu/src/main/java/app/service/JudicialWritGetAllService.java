package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeoutException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritList;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritTask;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritListRepository;

import app.bean.Parameter;
import app.bean.WebParam;
import app.client.aws.AwsApiClient;
import app.parser.JudicialWritParser;
import app.service.common.JudicialWritCommonService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritGetAllService {

	@Autowired
	private JudicialWritParser judicialWritParser;
	@Autowired
	private JudicialWritListRepository judicialWritListRepository;
	@Autowired
	private JudicialWritParameterService judicialWritParameterService;
	@Autowired
	private JudicialWritAmountService judicialWritAmountService;
	@Autowired
	private JudicialWritCommonService judicialWritCommonService;
	@Autowired
	private AwsApiClient awsApiClient;
	String Vl5x = null;
	String Number = null;
	String Guid = null;
	@Async
	public int getAlldata(WebParam webParam, HttpProxyBean httpProxyBean, JudicialWritTask judicialWritTask,int i, String keyword) throws Exception {
//		if(l<5){
			Parameter parameter = webParam.getParameter();
			Vl5x = parameter.getVl5x();
			Number = parameter.getNumber();
			Guid = parameter.getGuid();
//			webParam = getAmount(webParam, httpProxyBean);
//			Integer code = webParam.getCode();
//			int pagesize = 0;
//			if(code%20!=0){
//				pagesize = code/5+1;
//			}else{
//				pagesize=code/5;
//			}
			for (i = i; i < 6; i++) {
				String url = "http://wenshu.court.gov.cn/List/ListContent?"
						+ "Param="+keyword
						+ "&Index="+i
						+ "&Page=5"
						+ "&Order=%E6%B3%95%E9%99%A2%E5%B1%82%E7%BA%A7"//法院层级
						+ "&Direction=asc"
						+ "&vl5x="+Vl5x
						+ "&number="+Number
						+ "&guid="+Guid;
				System.out.println("第"+i+"页-----------------------------------》》》》");
				Page page = gethtmlPost(webParam.getWebClient(), null, url, httpProxyBean);
				String contentAsString = page.getWebResponse().getContentAsString();
				if(contentAsString.indexOf("\\")!=-1){
					String replace = contentAsString.replace("\\", "");
					System.out.println(replace);
					String substring = replace.substring(1, replace.length()-1);
					System.out.println(substring);
					JSONArray fromObject = JSONArray.fromObject(substring);
					for (int j = 1; j < fromObject.size(); j++) {
						String RunEval = fromObject.getJSONObject(0).getString("RunEval");
						JSONObject jsonObject = fromObject.getJSONObject(j);
						JudicialWritList judicialWritList = judicialWritParser.getCrawler(jsonObject,judicialWritTask,RunEval);
						if(judicialWritList!=null){
							judicialWritList = judicialWritCommonService.getcontent(judicialWritList, webParam.getWebClient(), httpProxyBean);
							judicialWritList.setKeyword(keyword);
							judicialWritListRepository.save(judicialWritList);
						}
					}
				}else{
					return i;
				}/*else if(contentAsString.indexOf("remind")!=-1){
					System.out.println("remind重试第"+l+"次");
					l= l+1;
					getAlldata(webParam, httpProxyBean, judicialWritTask, l, keyword);
				}
				else{
					//重试
					l=l+1;
					System.out.println("重试第"+l+"次");
					httpProxyBean = awsApiClient.getProxy();
					
					//webParam.getWebClient().close();
				 	webParam = judicialWritParameterService.getParameter(httpProxyBean, judicialWritTask,0);
					getAlldata(webParam, httpProxyBean, judicialWritTask, l,keyword);
				}
*/
			}
//		}
//		webParam = judicialWritAmountService.getAll(webParam,httpProxyBean,judicialWritTask,keyword,1);

		return 6;
	}

	public WebParam getAmount(WebParam webParam, HttpProxyBean httpProxyBean) throws Exception {
		Parameter parameter = webParam.getParameter();

		String url = "http://wenshu.court.gov.cn/List/ListContent?"
				+ "Param="
				+ "&Index=1"
				+ "&Page=20"
				+ "&Order=%E8%A3%81%E5%88%A4%E6%97%A5%E6%9C%9F"
				+ "&Direction=desc"
				+ "&vl5x="+parameter.getVl5x()
				+ "&number="+parameter.getNumber()
				+ "&guid="+parameter.getGuid();

		Page page = gethtmlPost(webParam.getWebClient(), null, url, httpProxyBean);
		String contentAsString = page.getWebResponse().getContentAsString();
		if(contentAsString.indexOf("\\")!=-1){
			String replace = contentAsString.replace("\\", "");
			System.out.println(replace);
			String substring = replace.substring(1, replace.length()-1);
			System.out.println(substring);
			JSONArray fromObject = JSONArray.fromObject(substring);
			Integer Count = Integer.parseInt(fromObject.getJSONObject(0).getString("Count"));
			System.out.println(Count);
			webParam.setCode(Count);
			return webParam;
		}else if(contentAsString.indexOf("remind key")!=-1){

			throw new  TimeoutException("请求超时");

		}else if(contentAsString.indexOf("<!DOCTYPE html>")!=-1){

			throw new  TimeoutException("网页超时");
		}
		webParam.setCode(0);
		return webParam;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url,HttpProxyBean httpProxyBean) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		if (httpProxyBean!=null) {
			webRequest.setProxyHost(httpProxyBean.getIp());
			webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}

	public static HtmlPage getHtml(String url, WebClient webClient,HttpProxyBean httpProxyBean) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		if (httpProxyBean!=null) {
			webRequest.setProxyHost(httpProxyBean.getIp());
			webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
		}
		//		webClient.setJavaScriptTimeout(50000); 
		//		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}



}
