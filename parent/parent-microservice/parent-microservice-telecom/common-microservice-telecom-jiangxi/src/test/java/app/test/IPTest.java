package app.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

/**
 * @description:测试爬取数据时候发送短信验证码   是不是受到了IP的影响，通过测试证明，会受到IP的影响
 * @author: sln 
 * @date: 2017年11月29日 下午3:48:28 
 */
public class IPTest {
	public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		String url = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";   //此请求必须用post
		WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("callCount", "1"));  
		params.add(new NameValuePair("page", "/2017/details.jsp"));
		params.add(new NameValuePair("httpSessionId", ""));
		params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338201"));
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
		webRequest.setAdditionalHeader("Cookie", "lvid=8df5b37ee7ed60bbf117544226e69922; nvid=1; svid=ED13D30298944A80; bdshare_firstime=1505543664093; cn_1260051947_dplus=%7B%22distinct_id%22%3A%20%2215e99048e866bf-0d03549370adfb-3a3e5f04-100200-15e99048e87bbf%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201505805770%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201505805770%7D%7D; UM_distinctid=15e99048e866bf-0d03549370adfb-3a3e5f04-100200-15e99048e87bbf; _pk_id.345.9df4=b864ac02a89790c3.1505557507.5.1507794623.1506320455.; aactgsh111220=13317954690; userId=201%7C20170100000009140729; isLogin=logined; .ybtj.189.cn=F6B34B0C20EFD1C63BA8C7CC21D82B08; flag=2; loginStatus=logined; s_cc=true; trkHmCitycode=0; trkHmCoords=0; trkHmPageName=0; trkHmClickCoords=0; s_fid=305BF42302E277AF-0876E0841E8D16BC; trkId=D2B48367-143E-4A2F-B230-45B9B81AB69E; s_sq=%5B%5BB%5D%5D; NETJSESSIONID1=ZvDJhpjKgWJlnyMYHpNQ0JL1RrppkcktRBrshT6Rrcnn5HWw9JhZ!1298888237; _pk_ref.345.1592=%5B%22%22%2C%22%22%2C1511941042%2C%22http%3A%2F%2Fwww.189.cn%2Fjx%2Fservice%2F%22%2C%220%22%5D; Hm_lvt_4ae12616aa0a873fc63cbdccf4a2e47a=1511774854,1511774872,1511774880,1511941042; Hm_lpvt_4ae12616aa0a873fc63cbdccf4a2e47a=1511941142; cityCode=jx; SHOPID_COOKIEID=10015; _pk_id.345.1592=d9e3cb7381e2e214.1510741741.3.1511941164.1511775363.; _pk_ses.345.1592=*");
		HtmlPage hPage2 = webClient.getPage(webRequest);
		String html = hPage2.getWebResponse().getContentAsString();
		System.out.println("获取的发送验证码的页面是："+html);
		int a=html.indexOf("{");
		int b=html.lastIndexOf("}");
		html=html.substring(a,b+1);
		JSONObject rr = JSONObject.fromObject(html);
		String codeDesc=rr.getString("CODE_DESC");
		System.out.println("返回的用于爬取数据的短信验证码的页面中获取到的错误描述信息是："+codeDesc);
	}
}
