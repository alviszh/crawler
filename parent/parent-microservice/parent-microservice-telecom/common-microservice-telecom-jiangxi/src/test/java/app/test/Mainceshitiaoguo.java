package app.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class Mainceshitiaoguo {

	public static void main(String[] args) throws MalformedURLException {
		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
		String url = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("callCount", "1"));  
		params.add(new NameValuePair("page", "page=/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url="));
		params.add(new NameValuePair("httpSessionId", ""));
		params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338897"));
		params.add(new NameValuePair("c0-scriptName", "Service"));
		params.add(new NameValuePair("c0-methodName", "excute"));
		params.add(new NameValuePair("c0-id", "0"));   //参数1
		params.add(new NameValuePair("c0-param0", "string:SEND_LOGIN_RANDOM_PWD"));   //参数2
		params.add(new NameValuePair("c0-param1", "boolean:false"));
		params.add(new NameValuePair("c0-e1", "string:18970922391"));
		params.add(new NameValuePair("c0-e2", "string:CR0"));
		params.add(new NameValuePair("c0-e3", "string:001"));
		params.add(new NameValuePair("c0-e4", "string:no"));
		params.add(new NameValuePair("c0-param2", "Object_Object:{RECV_NUM:reference:c0-e1, SMS_OPERTYPE:reference:c0-e2, RAND_TYPE:reference:c0-e3, need_val:reference:c0-e4}"));
		params.add(new NameValuePair("batchId", "6"));
		
		webRequest.setRequestParameters(params);
		webRequest.setAdditionalHeader("Accept", "*/*");    
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
		webRequest.setAdditionalHeader("Connection", "keep-alive");      
		webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
		webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
		webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
		webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url="); 
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
		HtmlPage hPage=null;
		try {
			hPage = webclientlogin.getPage(webRequest);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = hPage.getWebResponse().getContentAsString();
		System.out.println("获取的发送二次登录所需要的验证码的页面是："+html);
	}

}
