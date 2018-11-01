package app.htmlunit.wap;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WapParam;
import net.sf.json.JSONObject;

@Component
public class TelecomHubeiWapSmsUnit {

	public  final Logger log = LoggerFactory.getLogger(TelecomHubeiWapSmsUnit.class);
	@Autowired
	private TelecomHubeiWapCommon telecomHubeiWapCommon;
	@Autowired
	private TracerLog tracer;
	//发送验证码
	public  WapParam getphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		WapParam webParam=new WapParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
    	String preUrl = "http://waphb.189.cn/login/login.jsp";
		Page prePage = webClient.getPage(preUrl);
		tracer.addTag("getphonecode.detail.page", prePage.getWebResponse().getContentAsString());
		String smsUrl="http://waphb.189.cn/login/getSmsCode.htm?phoneNumber="+mssageLogin.getName()+"&randomType=loginRan";
		tracer.addTag("getphonecode 参数为 smsUrl=",smsUrl);	
		WebRequest webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);				
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json; charset=utf-8");
		webRequest.setAdditionalHeader("Host", "waphb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn/login/login.jsp"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);				
		Thread.sleep(1000);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("getphonecode",html);	
		webParam.setUrl(smsUrl);
		webParam.setHtml(html);		
		if (html.contains("flag") && html.contains("result")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String flag=jsonInsurObjs.getString("flag");
			String result=jsonInsurObjs.getString("result");	
			if ("1".equals(flag) && "0".equals(result)) {
				webParam.setWebClient(webClient);
				webParam.setSmsResult(html);	
				webParam.setState(true);
				Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 
				for (Cookie cookie:cookies) {
					System.out.println(cookie.getDomain());
					System.out.println(cookie.getName());
					System.out.println(cookie.getValue());
				}
				tracer.addTag("getphonecodeTwo","第二次获取验证码发送成功");		
			}
		}
		return webParam;
	}
	//手机验证码验证
	public  WapParam setphonecode(MessageLogin mssageLogin,TaskMobile taskMobile) throws Exception {
		WapParam webParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		String url = "http://waphb.189.cn/login/doLogin.htm";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accountID", encrypt(mssageLogin.getName())));
//		paramsList.add(new NameValuePair("random",encrypt(mssageLogin.getSms_code())));
//		paramsList.add(new NameValuePair("loginType", "2"));	
		String requestBody="accountID="+telecomHubeiWapCommon.encrypt(mssageLogin.getName())+"%3D&random="+telecomHubeiWapCommon.encrypt(mssageLogin.getSms_code())+"&loginType=2";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "waphb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://waphb.189.cn");	
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn/login/login.jsp"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);	
		
		String html=page.getWebResponse().getContentAsString();
		System.out.println(html);
		tracer.addTag("setphonecode",html);
		webParam.setUrl(url);
		webParam.setHtml(html);
		if (html.contains("flag") && html.contains("result")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String flag=jsonInsurObjs.getString("flag");
			String result=jsonInsurObjs.getString("result");	
			if ("1".equals(flag) && "0".equals(result)) {
				webParam.setWebClient(webClient);
				webParam.setSmsResult(html);	
				webParam.setState(true);
				Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 
				System.out.println("setphonecode cookies==");
				for (Cookie cookie:cookies) {
					System.out.println(cookie.getDomain());
					System.out.println(cookie.getName());
					System.out.println(cookie.getValue());
				}
				tracer.addTag("getphonecodeTwo","第二次获取验证码发送成功");		
			}
		}
		return webParam;
	}
	
	//发送验证码
	public  WapParam getphonecodeTwo(MessageLogin mssageLogin,TaskMobile taskMobile,int count) throws Exception {
		WapParam webParam=new WapParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	//	webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);
		String smsUrl="http://waphb.189.cn/login/getSmsCode.htm?phoneNumber="+mssageLogin.getName()+"&randomType=billQuery";
		tracer.addTag("getphonecodeTwo 参数为 smsUrl=",smsUrl);	
		WebRequest webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);				
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json; charset=utf-8");
		webRequest.setAdditionalHeader("Host", "waphb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn/service/custBillDetail.jsp"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);				
		Thread.sleep(1000);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("getphonecodeTwo",html);	
		webParam.setUrl(smsUrl);
		webParam.setHtml(html);				
		if (html.contains("flag") && html.contains("result")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String flag=jsonInsurObjs.getString("flag");
			String result=jsonInsurObjs.getString("result");	
			if ("1".equals(flag) && "0".equals(result)) {
				webParam.setWebClient(webClient);
				webParam.setSmsResult(html);	
				webParam.setState(true);
				tracer.addTag("getphonecodeTwo","第二次获取验证码发送成功");		
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.setphonecodeTwo.count" + count, "这是第" + count + "次校验验证码");
					Thread.sleep(1000);
					getphonecodeTwo(mssageLogin, taskMobile,count);
				}
			}
		}
			
		return webParam;
	}
	//手机验证码验证
	public  WapParam setphonecodeTwo(MessageLogin mssageLogin,TaskMobile taskMobile,int count) throws Exception {
		WapParam webParam= new WapParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient = telecomHubeiWapCommon.addcookie(webClient, taskMobile);		
		
		String url = "http://waphb.189.cn/billQuery/checkRan.htm";
		String requestBody="accountID="+telecomHubeiWapCommon.encrypt(mssageLogin.getName())+"%3D&random="+telecomHubeiWapCommon.encrypt(mssageLogin.getSms_code());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "waphb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://waphb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://waphb.189.cn/service/custBillDetail.jsp"); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");			
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);	
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("setphonecodeTwo",html);
		webParam.setUrl(url);
		webParam.setHtml(html);
		if (html.contains("flag") && html.contains("result")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String flag=jsonInsurObjs.getString("flag");
			String result=jsonInsurObjs.getString("result");	
			if ("1".equals(flag) && "0".equals(result)) {
				webParam.setWebClient(webClient);
				webParam.setSmsResult(html);		
				webParam.setState(true);
				tracer.addTag("setphonecodeTwo","第二次获取验证码校验成功");		
			}else{
				if (count < 3) {
					count++;
					tracer.addTag("parser.setphonecodeTwo.count" + count, "这是第" + count + "次校验验证码");
					Thread.sleep(1000);
					setphonecodeTwo(mssageLogin, taskMobile,count);
				}
			}
		}
		return webParam;
	}
	
	public Page getPage(WebClient webClient, List<NameValuePair> paramsList, String url) {
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			tracer.addTag("telecomhubei ", e.getMessage());
			return null;
		}
	}
	
	public  HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			tracer.addTag("telecomhubei ", e.getMessage());
			return null;
		}

	}

}
