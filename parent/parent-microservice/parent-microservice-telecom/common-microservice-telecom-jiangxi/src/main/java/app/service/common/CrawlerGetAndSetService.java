package app.service.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.domain.TempBean;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.jiangxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.jiangxi")
public class CrawlerGetAndSetService {
	@Autowired
	private TracerLog tracer;
	public static String encryptedPhone;
	@Autowired 
	private LoginAndGetCommonService loginAndGetCommonService;
	//===========================爬取数据时需要的短信验证码发送和接收接口  start======================================
	public  TempBean getphonecode(TaskMobile taskMobile) {
		TempBean tempBean  = new TempBean();
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";   //此请求必须用post
			taskMobile.setTrianNum(0);
			WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.POST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new NameValuePair("callCount", "1"));  
			params.add(new NameValuePair("page", "/2017/details.jsp"));
			params.add(new NameValuePair("httpSessionId", ""));
			params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
			params.add(new NameValuePair("c0-scriptName", "Service"));
			params.add(new NameValuePair("c0-methodName", "excute"));
			params.add(new NameValuePair("c0-id", "0"));   //参数1
			params.add(new NameValuePair("c0-param0", "string:DETAILS_SERVICE"));   //参数2
			params.add(new NameValuePair("c0-param1", "boolean:false"));
			params.add(new NameValuePair("c0-e1", "string:SEND_SMS_CODE"));
			params.add(new NameValuePair("c0-param2", "Object_Object:{method:reference:c0-e1}"));
			params.add(new NameValuePair("batchId", "1"));
			   
			webRequest.setRequestParameters(params);
			webRequest.setAdditionalHeader("Accept", "*/*");    
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");      
			webRequest.setAdditionalHeader("Connection", "keep-alive");      
			webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
			webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
			webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/2017/details.jsp"); 
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36"); 
			HtmlPage hPage2 = webClient.getPage(webRequest);
			String html = hPage2.getWebResponse().getContentAsString();
			System.out.println("获取的发送验证码的页面是："+html);
			tracer.addTag("获取的用于爬取数据的短信验证码的页面是：", taskMobile.getTaskid()+html);
			int a=html.indexOf("{");
			int b=html.lastIndexOf("}");
			html=html.substring(a,b+1);
			JSONObject rr = JSONObject.fromObject(html);
			String codeDesc=rr.getString("CODE_DESC");
			System.out.println("返回的用于爬取数据的短信验证码的页面中获取到的错误描述信息是："+codeDesc);
			if(!codeDesc.contains("\u6210\u529F")){   //\u6210\u529F  成功
				//您发送的短信验证码超过了当日最大发送次数，请明天再试！\u60A8\u53D1\u9001\u7684\u77ED\u4FE1\u9A8C\u8BC1\u7801\u8D85\u8FC7\u4E86\u5F53\u65E5\u6700\u5927\u53D1\u9001\u6B21\u6570\uFF0C\u8BF7\u660E\u5929\u518D\u8BD5\uFF01
//				if(html.contains("\u60a8\u53d1\u9001\u7684\u77ed\u4fe1\u9a8c\u8bc1\u7801\u8d85\u8fc7\u4e86\u5f53\u65e5\u6700\u5927\u53d1\u9001\u6b21\u6570")){
				if(codeDesc.contains("\u60a8\u53d1\u9001\u7684\u77ed\u4fe1\u9a8c\u8bc1\u7801\u8d85\u8fc7\u4e86\u5f53\u65e5")){
					tempBean.setErrormessage("您发送的短信验证码超过了当日最大发送次数，请明天再试");
					System.out.println("==========您发送的短信验证码超过了当日最大发送次数，请明天再试");
					tracer.addTag("action.crawler.getphonecode.-2", "您发送的短信验证码超过了当日最大发送次数，请明天再试");
				}else if(codeDesc.contains("\u77ed\u4fe1\u9a8c\u8bc1\u7801\u53d1\u9001\u5931\u8d25")){
					tempBean.setErrormessage("短信验证码发送失败");
					System.out.println("短信验证码发送失败");
					tracer.addTag("action.crawler.getphonecode.-1", "短信验证码发送失败");
				}else if(codeDesc.contains("\u672A\u67E5\u8BE2\u5230\u60A8\u7684\u9A8C\u8BC1\u53F7\u7801\u4FE1\u606F")){
					tempBean.setErrormessage("未查询到您的验证号码信息");
					tracer.addTag("action.crawler.getphonecode.-9", "未查询到您的验证号码信息");
					System.out.println("未查询到您的验证号码信息");
				}else if(codeDesc.contains("\u60A8\u53D1\u9001\u7684\u592A\u5FEB\u4E86\uFF0C\u8BF7\u7A0D\u540E\u518D\u8BD5\uFF01")){
					tempBean.setErrormessage("您发送的太快了，请稍后再试！");
					System.out.println("==========您发送的太快了，请稍后再试！");
					tracer.addTag("action.crawler.getphonecode.-5", "您发送的太快了，请稍后再试！");
				}else{
					//自己加的
					tempBean.setErrormessage("获取短信验证码过程中网站出现其他错误");
					tracer.addTag("action.crawler.getphonecode.-6", "获取短信验证码过程中网站出现其他错误");
				}
			}
			webClient=hPage2.getWebClient();
			tempBean.setHtml(html);  //存储发送验证码后返回的页面
			tempBean.setWebClient(webClient);  //将发送验证码的webClient存储
			return tempBean;
		} catch (Exception e) {
			if (taskMobile.getTrianNum() > 2) {
				return null;
			}
			tempBean = getphonecode(taskMobile);
			return tempBean;
		}
	}

	public  TempBean setphonecode(MessageLogin messageLogin, TaskMobile taskMobile){
		TempBean tempBean = new TempBean();
		String url = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
		WebClient webClient = loginAndGetCommonService.addcookie(taskMobile);
		try {
			WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.POST);
			String nowYearMonth = CalendarParamService.getNowYearMonth();
			String smsCode = messageLogin.getSms_code().trim();
			if(null!=smsCode){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new NameValuePair("callCount", "1"));  
				params.add(new NameValuePair("page", "/2017/details.jsp"));
				params.add(new NameValuePair("httpSessionId", ""));
				params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
				params.add(new NameValuePair("c0-scriptName", "Service"));
				params.add(new NameValuePair("c0-methodName", "excute"));
				params.add(new NameValuePair("c0-id", "0"));   //参数1
				params.add(new NameValuePair("c0-param0", "string:DETAILS_SERVICE"));   //参数2
				params.add(new NameValuePair("c0-param1", "boolean:false"));
				params.add(new NameValuePair("c0-e1", "string:"+nowYearMonth+""));
				params.add(new NameValuePair("c0-e2", "string:7"));
				params.add(new NameValuePair("c0-e3", "string:"+smsCode+""));
				params.add(new NameValuePair("c0-e4", "string:QRY_DETAILS_BY_LOGIN_NBR"));
				
				params.add(new NameValuePair("c0-param2", "Object_Object:{month:reference:c0-e1, query_type:reference:c0-e2, valid_code:reference:c0-e3, method:reference:c0-e4}"));
				params.add(new NameValuePair("batchId", "3"));
				   
				webRequest.setRequestParameters(params);
				webRequest.setAdditionalHeader("Accept", "*/*");    
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
				webRequest.setAdditionalHeader("Connection", "keep-alive");      
				webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
				webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
				webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
				webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/2017/details.jsp"); 
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
				HtmlPage hPage = webClient.getPage(webRequest);
				String html=hPage.getWebResponse().getContentAsString();
				System.out.println("获取的验证验证码的页面是："+html);
				tracer.addTag("用于爬取数据：获取的验证验证码的页面是", taskMobile.getTaskid()+"===>"+html);
				//根据验证码校验的返回状态值来判断是否校验成功
				int i=html.indexOf("METHOD");
				int j=html.lastIndexOf("}");
				html=html.substring(i,j+1);
				html="{"+html;
				JSONObject json=JSONObject.fromObject(html);
				String code = json.getString("CODE");
				System.out.println(json.getString("CODE"));
				if(!code.equals("0")){
					tempBean.setErrormessage("短信验证码校验失败！");
				}
				webClient=hPage.getWebClient();    
				tempBean.setHtml(html);  //存储验证验证码后返回的页面
				tempBean.setWebClient(webClient);  //将验证验证码的webClient存储
				return tempBean;
			}
		} catch (Exception e) {
			tempBean.setHtml(e.getMessage());
			tempBean.setErrormessage(e.getMessage());
			return tempBean;
		}
		return tempBean;
	}
}
