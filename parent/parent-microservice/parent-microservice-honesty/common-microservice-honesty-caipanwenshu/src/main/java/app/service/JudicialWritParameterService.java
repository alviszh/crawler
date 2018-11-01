package app.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritTask;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritTaskRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.Parameter;
import app.bean.WebParam;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritParameterService {

	@Autowired
	private JudicialWritTaskRepository judicialWritTaskRepository;
	public WebParam getParameter(HttpProxyBean httpProxyBean, JudicialWritTask judicialWritTask,int i) throws Exception{
		if(i<6){
			WebParam webParam = new WebParam();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Parameter parameter = new Parameter();
			//		String jsurl = "http://wenshu.court.gov.cn/Assets/js/20180807/Lawyee.CPWSW.List.js";
			//		getHtml(jsurl, webClient, httpProxyBean);
			String url = "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+3+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E8%A1%8C%E6%94%BF%E6%A1%88%E4%BB%B6";
			HtmlPage html = getHtml(url, webClient, httpProxyBean);
			String contentAsString = html.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			if(contentAsString!=null){
				//参数1：guid
				String guid = encryptedPhone("guid.js","ref","");
				System.out.println(guid);
				if(guid!=null){
					parameter.setGuid(guid);
					judicialWritTaskRepository.getguid(guid,"guid获取成功",judicialWritTask.getTaskid());
				}else{
					judicialWritTaskRepository.getguid(null, "guid获取失败", judicialWritTask.getTaskid());
					return null;
				}

				Set<Cookie> cookies = webClient.getCookieManager().getCookies();
				if(cookies.size()>0){
					//参数2：vl5x
					String vjkl5 = null;
					for (Cookie cookie : cookies) {
						if(cookie.getName().equals("vjkl5")){
							vjkl5 = cookie.getValue();
							break;
						}
					}

					String vl5x = encryptedPhone("vl5x.js","getKey",vjkl5);
					System.out.println(vl5x);
					if(vl5x!=null){
						parameter.setVl5x(vl5x);
						judicialWritTaskRepository.getVl5x(vl5x,"vl5x获取成功！",judicialWritTask.getTaskid());
					}else{
						judicialWritTaskRepository.getVl5x(null,"vl5x获取失败！",judicialWritTask.getTaskid());
						return null;
					}
				}else{
					judicialWritTaskRepository.getVl5x(null,"vl5x获取失败！",judicialWritTask.getTaskid());
					return null;
				}
				//参数3：number
				String url2 = "http://wenshu.court.gov.cn/ValiCode/GetCode?"
						+ "guid="+guid;
				Page page2 = gethtmlPost(webClient, null, url2, httpProxyBean);
				String number = page2.getWebResponse().getContentAsString();

				if(number!=null){
					parameter.setNumber(number);
					judicialWritTaskRepository.getNumber(number,"参数获取完成！",judicialWritTask.getTaskid());
				}else{
					judicialWritTaskRepository.getNumber(null,"number获取失败！",judicialWritTask.getTaskid());
					return null;
				}
			}else{
				System.out.println("获取参数重试第"+i+"次");
				Thread.sleep(1000);
				getParameter(httpProxyBean, judicialWritTask,i+1);
			}

			webParam.setWebClient(webClient);
			webParam.setParameter(parameter);
			return webParam;
		}
		return null;
	}
	public WebParam getParameter2(HttpProxyBean httpProxyBean, JudicialWritTask judicialWritTask,WebParam webParam,WebClient webClient) throws Exception{

		//参数1：guid
		String guid = encryptedPhone("guid.js","ref","");
		System.out.println(guid);
		if(guid!=null){
			webParam.getParameter().setGuid(guid);
			judicialWritTaskRepository.getguid(guid,"guid获取成功",judicialWritTask.getTaskid());
		}else{
			judicialWritTaskRepository.getguid(null, "guid获取失败", judicialWritTask.getTaskid());
			return null;
		}
		//参数3：number
		String url2 = "http://wenshu.court.gov.cn/ValiCode/GetCode?"
				+ "guid="+guid;
		Page page2 = gethtmlPost(webClient, null, url2, httpProxyBean);
		String number = page2.getWebResponse().getContentAsString();

		if(number!=null){
			webParam.getParameter().setNumber(number);
			judicialWritTaskRepository.getNumber(number,"参数获取完成！",judicialWritTask.getTaskid());
		}else{
			judicialWritTaskRepository.getNumber(null,"number获取失败！",judicialWritTask.getTaskid());
			return null;
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
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static String encryptedPhone(String jsname,String wayname,String vjkl5) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource(jsname, Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction(wayname,vjkl5);
		return data.toString(); 
	}

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

}
