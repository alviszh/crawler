package app.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.sz.anhui.InsuranceSZAnHuiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.InsuranceService;
import net.sf.json.JSONObject;

@Component
public class InsuranceSZAnHuiHtmlunit {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws MalformedURLException 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		String url1 = "https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=d4a4dfffe8974f309b7f1f6b25b35ecd&service=http%3A%2F%2F61.190.31.166%3A9091%2Fahrsgrwt%2Findex.do";
		// 调用下面的getHtml方法
		WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest1);
		String html = page.asXml();
		Document doc = Jsoup.parse(html);
		Element elementById = doc.getElementById("legpsdFm");
		String lt = elementById.getElementsByAttributeValue("name", "lt").val();
		
		String execution = elementById.getElementsByAttributeValue("name", "execution").val();
		
		String _eventId = elementById.getElementsByAttributeValue("name", "_eventId").val();
		
		String platform = elementById.getElementsByAttributeValue("name", "platform").val();
	
		String loginType = elementById.getElementsByAttributeValue("name", "loginType").val();
		
		// 调用下面的getHtml方法
		String url="https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=4fc73e5bcd794b08889f39ad2b89acde&service=https://uc.ewoho.com/provinceCas/login?whService=aHR0cDovL3d3dy5ld29oby5jb20vcGVyc29uYWxjZW50ZXIvbG9hZGhvdXNlZnVuZC5kbw";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("lt", lt));
		webRequest.getRequestParameters().add(new NameValuePair("execution", execution));
		webRequest.getRequestParameters().add(new NameValuePair("_eventId", _eventId));
		webRequest.getRequestParameters().add(new NameValuePair("platform", platform));
		webRequest.getRequestParameters().add(new NameValuePair("loginType", loginType));
		webRequest.getRequestParameters().add(new NameValuePair("credentialType", "PASSWORD"));
		webRequest.getRequestParameters().add(new NameValuePair("userType", "0"));
		webRequest.getRequestParameters().add(new NameValuePair("username", "mengshanshan007"));
		webRequest.getRequestParameters().add(new NameValuePair("password", "mss007"));
		webRequest.getRequestParameters().add(new NameValuePair("random", ""));
		Page loginPage = webClient.getPage(webRequest);
		String loginHtml=loginPage.getWebResponse().getContentAsString();
		webParam.setWebClient(webClient);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHtml(loginHtml);	
		tracer.addTag("login page","<xmp>"+loginHtml+"</xmp>");		
		return webParam;			
	}
	
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfo(InsuranceRequestParameters insuranceRequestParameters, Set<Cookie> cookies){	
		WebParam webParam= new WebParam();
		WebClient webClient = insuranceService.getWebClient(cookies);		
		HtmlPage indexPage;
		try {
			indexPage = getHtml("http://61.190.31.166:9091/ahrsgrwt/index.do",webClient);
			String indexHtml=indexPage.getWebResponse().getContentAsString();
		   	tracer.addTag("getUserInfo indexPage","<xmp>"+indexHtml+"</xmp>");
			String url = "http://61.190.31.166:9091/ahrsgrwt/grbs/sbkgl/cd030101.do";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
			webRequest.setAdditionalHeader("Accept", "*/*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			webRequest.setAdditionalHeader("Host", "61.190.31.166:9091");
			Page page = webClient.getPage(webRequest);		
			int statusCode = page.getWebResponse().getStatusCode();
	    	String html = page.getWebResponse().getContentAsString();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
		    if(200 == statusCode){
		    	if (html.contains("aac003")) {
		    		InsuranceSZAnHuiUserInfo userInfo = htmlParserForUserInfo(html,insuranceRequestParameters.getTaskId());	
		    		webParam.setInsuranceSZAnHuiUserInfo(userInfo);
				}else if(html.contains("authentication-list.html")){
					webParam.setMsgAlert("请至少完成2个中级认证");
				}
		    	webParam.setCode(statusCode);    
		    	webParam.setUrl(url);
		    	webParam.setHtml(html); 	
		    }
		} catch (Exception e) {			
			e.printStackTrace();
	     	tracer.addTag("InsuranceSZAnHuiHtmlunit.getUserInfo.Exception",e.getMessage());
		}	
		return webParam;
	}
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceSZAnHuiUserInfo htmlParserForUserInfo(String html, String taskid) {
		InsuranceSZAnHuiUserInfo userInfo=null;
		if (html.contains("resultBody")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);		
			if (null !=list1ArrayObjs) {
				String dataStr=list1ArrayObjs.getString("resultBody");
				JSONObject dataObjs=JSONObject.fromObject(dataStr);
				String citynum=dataObjs.getString("aab301");
				String cardnum=dataObjs.getString("aaz500");
				String idnum=dataObjs.getString("aac002");
				String type=dataObjs.getString("zjlx");
				String validdate=dataObjs.getString("aae042");
				String username=dataObjs.getString("aac003");
				String gender=dataObjs.getString("aac004");
				String nation=dataObjs.getString("aac005");
				String birthdate=dataObjs.getString("aac006");
				String firstdate=dataObjs.getString("aac008");
				
				String householdType=dataObjs.getString("aac009");
				String householdAddress=dataObjs.getString("aac010");
				String mobile=dataObjs.getString("aae005");
				String telphone=dataObjs.getString("ace005");
				String address=dataObjs.getString("aae006");
				String postalcode=dataObjs.getString("aae007");
				String mailaddress=dataObjs.getString("aae159");
				String usernum=dataObjs.getString("aac001");
				String applycardcompanynum=dataObjs.getString("aab001");
				String applycardcompanyname=dataObjs.getString("aab004");
			    userInfo = new InsuranceSZAnHuiUserInfo(citynum, cardnum, idnum, type, validdate,
						username, gender, nation, birthdate, firstdate, householdType,
						householdAddress, mobile, telphone, address, postalcode,
						mailaddress, usernum, applycardcompanynum, applycardcompanyname,
						taskid);	
			}	
		}			
		return userInfo;
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
