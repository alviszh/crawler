package app.service;

import java.net.URL;
import java.util.Calendar;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class AsyncHangzhouGetAllDataService {
	@Autowired
	private TracerLog tracer;
//	@Autowired
//	private InsuranceHangzhouService insuranceHangzhouService;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
//	@Async
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebParam webParam = new WebParam();
		String username = insuranceRequestParameters.getUsername();
	    String password = insuranceRequestParameters.getPassword();
	    byte[] encodeBytes = Base64.encodeBase64(password.getBytes());  
//	    for(int i = 1;i<=3;i++){
		String url = "http://wsbs.zjhz.hrss.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getWebClient();		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		HtmlPage page = webClient.getPage(webRequest);
    	HtmlImage image = page.getFirstByXPath("//img[@id='f_svl1']");
    	String code = chaoJiYingOcrService.getVerifycode(image, "3004");
	    //System.out.println(new String(encodeBytes));
		HtmlPage loggedPage = null;
		String str = null;
		//for(int i = 0;i<3;i++){
		String persontype = null;
		if (insuranceRequestParameters.getLoginType().contains("CITIZEN_EMAIL")){
			persontype = "01";
		}else {
			persontype = "03";
		}
			try {
				webClient.getOptions().setTimeout(50000);
				String u = "http://wsbs.zjhz.hrss.gov.cn/loginvalidate.html?logintype=2&captcha="+code+"&"
						+"type=01&persontype="+persontype+"&account="+username+"&password="+new String(encodeBytes)+"&captcha1="+code+"";
				WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
				loggedPage = webClient.getPage(webRequest1);
				str = loggedPage.getWebResponse().getContentAsString();
				System.out.println(loggedPage.getWebResponse().getContentAsString());
				webParam.setCode(loggedPage.getWebResponse().getStatusCode());
				webParam.setPage(loggedPage);
			}catch(Exception e){
				System.out.println("链接超时");
				
			}
//		}
	    if(loggedPage.getWebResponse().getContentAsString().contains("校验码错误！")){
			tracer.addTag(code, "图片验证码错误，触发retry机制");
			throw new RuntimeException("请输入正确的附加码");
		}
	    return webParam;
	}
}
