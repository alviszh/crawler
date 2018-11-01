import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangUserInfo;
import com.module.htmlunit.WebCrawler;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("all")
public class ShenYYY {

    public static void main(String[] args) throws Exception {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();

        //		webClient.getOptions().setJavaScriptEnabled(false);
		String loginUrl = "https://puser.zjzwfw.gov.cn/sso/usp.do";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);

        webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        webRequest.setAdditionalHeader("Host", "puser.zjzwfw.gov.cn");
        webRequest.setAdditionalHeader("Referer", "https://puser.zjzwfw.gov.cn/sso/usp.do");
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
		HtmlPage htmlPage = webClient.getPage(webRequest);
        webClient.waitForBackgroundJavaScript(10000);
//		System.out.println("登录页   =====》》"+htmlPage.getWebResponse().getContentAsString());


        HtmlTextInput username = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='loginname']");
        HtmlPasswordInput password = (HtmlPasswordInput)htmlPage.getFirstByXPath("//input[@id='loginpwd']");
        HtmlElement loginButton = (HtmlElement)htmlPage.getFirstByXPath("//input[@id='submit']");

        System.out.println("username   :"+username.asXml());
		System.out.println("password   :"+password.asXml());
		System.out.println("button   :"+loginButton.asXml());
		

		username.setText("412721198804173842");
		password.setText("g123456");
		
//		webClient.getOptions().setJavaScriptEnabled(true);
		HtmlPage loggedPage = loginButton.click();

		String html = loggedPage.getWebResponse().getContentAsString();
		String alertMsg = WebCrawler.getAlertMsg();
//        System.out.println("alertMsg------------"+alertMsg);
		System.out.println("\n\n\n\n\n登陆成功后的页面  ====》》"+html);
		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
		}


//        getNext(webClient);
		
	}

	private static void getNext(WebClient webClient) throws Exception{
//        WebClient webClient = WebCrawler.getInstance().getNewWebClient();

        URL url  = new URL("http://gs.sysb.gov.cn/sysb/xx");

        WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);

        requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
        requestSettings.getRequestParameters().add(new NameValuePair("year", "2017"));
        requestSettings.getRequestParameters().add(new NameValuePair("zgbh", "40451074"));

        requestSettings.setAdditionalHeader("Host", "gs.sysb.gov.cn");
        requestSettings.setAdditionalHeader("Origin", "http://gs.sysb.gov.cn");
        requestSettings.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/logingo");
        requestSettings.setAdditionalHeader("Accept", "*/*");
        requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        Page page = webClient.getPage(requestSettings);

        String s = page.getWebResponse().getContentAsString();
        System.out.println("\n=========="+s+"\n===================");

        for(Cookie coco:webClient.getCookieManager().getCookies())
            System.out.println(coco);

        JSONObject json = JSON.parseObject(s);
        System.out.println(json.getJSONArray("data"));
        System.out.println(json.getIntValue("size"));
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject js:jsonArray.toJavaList(JSONObject.class)){
            System.out.println("jsonArray分解========="+js.getString("jfbz"));
        }

    }

private static void getUserInfo(WebClient webClient) throws Exception {
		
		//String str = "{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{contentStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"contentStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.content\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"130181881216392\", \"12\"]}},xzStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"xzStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.xzxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"130181881216392\", \"12\"]}},sbkxxStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"sbkxxStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.sbkxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"130181881216392\", \"12\"]}},grqyjlStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"grqyjlStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.grqyjlyj\",attributes:{\"AAE135\": [\"130181198812163921\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"UCI314\", \"BUSINESS_REQUEST_ID\": \"REQ-IC-Q-098-60\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
	
		String url = "http://gs.sysb.gov.cn/sysb/logingo";
        WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		//String aaa = "{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{contentStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"contentStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.content\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"\", \"12\"]}},xzStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"xzStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.xzxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"\", \"12\"]}},sbkxxStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"sbkxxStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.sbkxx\",attributes:{\"AAC002\": [\"130181198812163921\", \"12\"], \"AAE135\": [\"\", \"12\"]}},grqyjlStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"grqyjlStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.grqyjlyj\",attributes:{\"AAE135\": [\"130181198812163921\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"\", \"BUSINESS_REQUEST_ID\": \"\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
        webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
        webRequest.setAdditionalHeader("Connection", "keep-alive");
        webRequest.setAdditionalHeader("Host", "gs.sysb.gov.cn");
        webRequest.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/logingo");
        webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js
        webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常
        HtmlPage page = webClient.getPage(webRequest);

//	    HtmlPage  page=webClient.getPage(webRequest);
	    HtmlSelect year = (HtmlSelect)page.getFirstByXPath("//select[@name='year']");
	    year.setSelectedAttribute("2015", true);
	    
	    HtmlElement queryButton=(HtmlElement)page.getFirstByXPath("//button[@id='queryBtn']");
	    HtmlPage  queryPage=queryButton.click();
		int status = page.getWebResponse().getStatusCode();
		System.out.println("========================");
		System.out.println(queryPage.asXml());
		System.out.println(status);
		
	}
}
