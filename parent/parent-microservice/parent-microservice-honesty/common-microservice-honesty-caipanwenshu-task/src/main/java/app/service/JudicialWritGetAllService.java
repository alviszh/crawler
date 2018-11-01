package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeoutException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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

import app.bean.HonestyJsonBean;
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
//	@Autowired
//	private JudicialWritAmountService judicialWritAmountService;
	@Autowired
	private JudicialWritCommonService judicialWritCommonService;
	@Autowired
	private AwsApiClient awsApiClient;
	String Vl5x = null;
	String Number = null;
	String Guid = null;
	public WebParam getAlldata(WebParam webParam, HttpProxyBean httpProxyBean, HonestyJsonBean honestyJsonBean, String keyword, int l) throws Exception {
		if(l<5){
			Parameter parameter = webParam.getParameter();
			Vl5x = parameter.getVl5x();
			Number = parameter.getNumber();
			Guid = parameter.getGuid();
			
			for (int i = 1; i < 6; i++) {
				String url = "http://wenshu.court.gov.cn/List/ListContent?"
						+ "Param="+keyword
						+ "&Index="+i
						+ "&Page=5"
						+ "&Order=%E6%B3%95%E9%99%A2%E5%B1%82%E7%BA%A7"//法院层级
						+ "&Direction=asc"
						+ "&vl5x="+Vl5x
						+ "&number="+Number.substring(0,4)
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
						JudicialWritList judicialWritList = judicialWritParser.getCrawler(jsonObject, honestyJsonBean, RunEval);
						if(judicialWritList!=null){
							judicialWritList = judicialWritCommonService.getcontent(judicialWritList, webParam.getWebClient(), httpProxyBean);
							judicialWritList.setKeyword(keyword);
							judicialWritListRepository.save(judicialWritList);
						}
					}
				}else if(contentAsString.indexOf("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！")!=-1){
					System.out.println("您的访问频次超出正常访问范围，为保障网站稳定运行，请输入验证码后继续查看！");
				}else if(contentAsString.indexOf("remind")!=-1){
					System.out.println("remind重试第"+i+"次");
					getAlldata(webParam, httpProxyBean, honestyJsonBean, keyword, l+1);
				}
				else{
					//重试
					i=i+1;
					System.out.println("重试第"+i+"次");
					httpProxyBean = awsApiClient.getProxy();
					getAlldata(webParam, httpProxyBean, honestyJsonBean, keyword, l+1);
				}

			}
			//judicialWritAmountService.getAll(webParam,honestyJsonBean,judicialWritTask,keyword,0);
		}


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
