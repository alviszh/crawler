package app.test;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class DWRTest {

	public static void main(String[] args) {
		try{
			String loginurl = "http://login.189.cn/login";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
	
			// 获取对应的输入框
			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin
					.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			username.setText("18970922391");
			passwordInput.setText("830818");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("点击登录后的页面是：------------" + asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				String wdzlurl0 = "http://www.189.cn/dqmh/my189/initMy189.do?&ACCOUNT_TYPE=80000045&cityCode=jx";
				WebRequest webRequestwdzl0;
				webRequestwdzl0 = new WebRequest(new URL(wdzlurl0), HttpMethod.GET);
				HtmlPage wdzl0 = webclientlogin.getPage(webRequestwdzl0);
				webclientlogin = wdzl0.getWebClient();
				System.out.println("中间请求的页面是："+wdzl0.asXml());
				
				//用户
//				String wdzlurl = "http://tj.189.cn/tj/service/manage/modifyUserInfo.action?fastcode=02241349&amp;cityCode=tj";
				//通话
				//String wdzlurl = "http://jx.189.cn/service/bill/customerbill/dinner_ui.jsp?ACC_NBR=18970922391&LAN_CODE=0791&PRODUCT_ID=80000045";
				//短信
//				String wdzlurl = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=2&sRandomCode=&randInputValue=%E8%AF%B7%E7%82%B9%E5%87%BB&startTime=2017-06-01&endTime=2017-06-30&exFormat=1";
				
				String wdzlurl = "http://jx.189.cn/service/bill/customerbill/index.jsp?bill=balance";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl);
				Page wdzl = webclientlogin.getPage(webRequestwdzl);
				String asXml2 = wdzl.getWebResponse().getContentAsString();
				System.out.println("获取到的页面是："+asXml2);
				
				
				
				
				WebRequest  requestSettings = new WebRequest(new URL("http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr"), HttpMethod.POST); 
  
				requestSettings.setAdditionalHeader("Host", "jx.189.cn");
				requestSettings.setAdditionalHeader("Origin", "http://jx.189.cn");
				requestSettings.setAdditionalHeader("Referer", "http://jx.189.cn/service/bill/customerbill/index.jsp?bill=balance");
				//requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
				
				requestSettings.setCharset(Charset.forName("UTF-8"));
				
				String requestBody = "callCount=1"+"/r/n";
				requestBody+= "page=/service/bill/customerbill/index.jsp?bill=balance"+"/r/n";
				requestBody+= "httpSessionId="+"/r/n";
				requestBody+= "scriptSessionId=AA0CBE9FB90164F9E0E55CF74FCC9338791"+"/r/n";
				requestBody+= "c0-scriptName=Service"+"/r/n";
				requestBody+= "c0-methodName=excute"+"/r/n";
				requestBody+= "c0-id=0"+"/r/n";
				requestBody+= "c0-param0=string:TWB_GET_MONTH_DETAIL_BILL_NEW"+"/r/n";
				requestBody+= "c0-param1=boolean:false"+"/r/n";
				requestBody+= "c0-e1=string:myPage"+"/r/n";
				requestBody+= "c0-e2=string:myPage_table"+"/r/n";
				requestBody+= "c0-e3=string:TWB_GET_MONTH_DETAIL_BILL_NEW"+"/r/n";
				requestBody+= "c0-e4=boolean:false"+"/r/n";
				requestBody+= "c0-e5=string:15"+"/r/n";
				requestBody+= "c0-e6=string:1"+"/r/n";
				requestBody+= "c0-e7=null:null"+"/r/n";
				requestBody+= "c0-e8=boolean:false"+"/r/n";
				requestBody+= "c0-e9=null:null"+"/r/n";
				requestBody+= "c0-e10=string:-1"+"/r/n";
				requestBody+= "c0-e11=string:18970922391"+"/r/n";
				requestBody+= "c0-e12=string:0"+"/r/n";
				requestBody+= "c0-e13=string:201708"+"/r/n";
				requestBody+= "c0-e14=string:7"+"/r/n";
				requestBody+= "c0-e15=string:10"+"/r/n";
				requestBody+= "c0-e16=string:1"+"/r/n";
				requestBody+= "c0-e17=string:1"+"/r/n";
				requestBody+= "c0-e18=string:"+"/r/n";
				requestBody+= "c0-e19=string:"+"/r/n";
				requestBody+= "c0-e20=string:yes"+"/r/n";
				requestBody+= "c0-param2=Object_Object:{div_id:reference:c0-e1, table_id:reference:c0-e2, func_id:reference:c0-e3, is_sql:reference:c0-e4, page_size:reference:c0-e5, page_index:reference:c0-e6, exp_excel:reference:c0-e7, hide_pager:reference:c0-e8, class_name:reference:c0-e9, area_code:reference:c0-e10, acc_nbr:reference:c0-e11, service_type:reference:c0-e12, inYearMonth:reference:c0-e13, queryContent:reference:c0-e14, deviceType:reference:c0-e15, sortingOrder:reference:c0-e16, write_order:reference:c0-e17, call_type:reference:c0-e18, search_date:reference:c0-e19, need_check_session:reference:c0-e20}"+"/r/n";
		 
				
				System.out.println(requestBody);
				requestSettings.setRequestBody(requestBody);
				
				
				Page page = webclientlogin.getPage(requestSettings); 
				
				String html = page.getWebResponse().getContentAsString();
				
				
				System.out.println("html-----"+html);
				
				
				
				
			}
		}catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}

}
