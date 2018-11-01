package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
/***
 * 无关键字查询（只包含升序降序）
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritAmountService {
	@Autowired
	private JudicialWritParser judicialWritParser;
	@Autowired
	private JudicialWritListRepository judicialWritListRepository;
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
						+ "&Page=20"
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
	@Async
	public int getAll(WebParam webParam, HttpProxyBean httpProxyBean, JudicialWritTask judicialWritTask, String keyword,int i) throws Exception {
//		if(l<4){
			Parameter parameter = webParam.getParameter();
			Vl5x = parameter.getVl5x();
			Number = parameter.getNumber().substring(0,4);
			Guid = parameter.getGuid();
			for (i = i; i < 6; i++) {
				String url = "http://wenshu.court.gov.cn/List/ListContent?"
						+ "Param="+keyword
						+ "&Index="+i
						+ "&Page=20"
						+ "&Order=%E6%B3%95%E9%99%A2%E5%B1%82%E7%BA%A7"//法院层级
						+ "&Direction=desc"
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
				}/*else if(contentAsString.indexOf("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！")!=-1){
					System.out.println("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！");
				}else if(contentAsString.indexOf("remind")!=-1){
					System.out.println("getAll----remind重试第"+l+"次");
					getAll(webParam, httpProxyBean, judicialWritTask, keyword,l+1);
				}else{
					httpProxyBean = awsApiClient.getProxy();
					getAll(webParam, httpProxyBean, judicialWritTask, keyword,l+1);
				}*/
			}
//		}

//		webParam = gettimeall(webParam, httpProxyBean, judicialWritTask,keyword,0);
		return 6;
	}
	@Async
	public int gettimeall(WebParam webParam, HttpProxyBean httpProxyBean, JudicialWritTask judicialWritTask, String keyword,int i) throws Exception{
//		if(l<4){
			Parameter parameter = webParam.getParameter();
			Vl5x = parameter.getVl5x();
			Number = parameter.getNumber();
			Guid = parameter.getGuid();
			List<String> list = new ArrayList<String>();
			list.add("asc");
			list.add("desc");
			for (String time : list) {
				for (i = i; i < 6; i++) {
					String url = "http://wenshu.court.gov.cn/List/ListContent?"
							+ "Param="+keyword
							+ "&Index="+i
							+ "&Page=20"
							+ "&Order=%E8%A3%81%E5%88%A4%E6%97%A5%E6%9C%9F"//裁判日期
							+ "&Direction="+time
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
								judicialWritListRepository.save(judicialWritList);
							}
						}
					}else{
						return i;
					}/*else if(contentAsString.indexOf("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！")!=-1){
						System.out.println("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！");
					}else if(contentAsString.indexOf("remind")!=-1){
						System.out.println("gettimeall---remind重试第"+l+"次");
						httpProxyBean = awsApiClient.getProxy();
						gettimeall(webParam, httpProxyBean, judicialWritTask, keyword,l+1);
					}else{
						httpProxyBean = awsApiClient.getProxy();
						gettimeall(webParam, httpProxyBean, judicialWritTask, keyword,l+1);
					}*/
				}
			}
//		}
//		webParam = getprocedureall(webParam, httpProxyBean, judicialWritTask,keyword,1);
		return 6;
	}
	@Async
	public int getprocedureall(WebParam webParam, HttpProxyBean httpProxyBean, JudicialWritTask judicialWritTask, String keyword, int i) throws Exception{
//		if(l<4){
			Parameter parameter = webParam.getParameter();
			Vl5x = parameter.getVl5x();
			Number = parameter.getNumber();
			Guid = parameter.getGuid();
			List<String> list = new ArrayList<String>();
			list.add("asc");
			list.add("desc");
			for (String time : list) {
				for (i = i; i < 6; i++) {
					String url = "http://wenshu.court.gov.cn/List/ListContent?"
							+ "Param="+keyword
							+ "&Index="+i
							+ "&Page=20"
							+ "&Order=%E5%AE%A1%E5%88%A4%E7%A8%8B%E5%BA%8F%E4%BB%A3%E7%A0%81"//审判程序代码
							+ "&Direction="+time
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
								judicialWritListRepository.save(judicialWritList);
							}
						}
					}else{
						return i;
					}/*else if(contentAsString.indexOf("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！")!=-1){
						System.out.println("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！");
					}else if(contentAsString.indexOf("remind")!=-1){
						httpProxyBean = awsApiClient.getProxy();
						System.out.println("getprocedureall--remind重试第"+l+"次");
						getprocedureall(webParam, httpProxyBean, judicialWritTask, keyword,l+1);
					}else{
						httpProxyBean = awsApiClient.getProxy();
						getprocedureall(webParam, httpProxyBean, judicialWritTask, keyword,l+1);
					}*/
				}
//			}
		}
		return 6;
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
