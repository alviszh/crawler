package app.test;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiPayfee;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiUserInfo;
import com.module.htmlunit.WebCrawler;
import app.bean.DataBean;

public class TestCrawler {
	
	private static WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
	public static String chargeFzxh = "";
	
	public static void main(String[] args) throws Exception {
		
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		login();
		
		String baseUrl = "http://service.sh.189.cn/service/account";
		Page page = getHtml(baseUrl,webClient2,null);
		getParam(page);
		
		//发送短信
		String smsUrl = "http://service.sh.189.cn/service/service/authority/query/billdetail/sendCode.do?flag=1&devNo=17316359776&dateType=&moPingType=LOCAL&startDate=&endDate=";
		getHtml(smsUrl,webClient2,null);
		
		//验证短信
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String code = scanner.next();
		String validateUrl = "http://service.sh.189.cn/service/service/authority/query/billdetail/validate.do?input_code="+code+"&selDevid=17316359776&flag=nocw&checkCode=%E9%AA%8C%E8%AF%81%E7%A0%81";
		getHtml(validateUrl,webClient2,null);
		
		//用户信息
//		String userUrl = "http://service.sh.189.cn/service/my/basicinfo.do";		
//		HtmlPage userPage = (HtmlPage) getHtml(userUrl,webClient2,null);
//		parserUserinfo(userPage);
//		getHtml(userUrl);
		
		//缴费信息
//		String payUrl = "http://service.sh.189.cn/service/service/authority/query/rechargePage.do?begin=0&end=10&time=0.2716233631759468&channel_wt=1&total=on&payment_no=17316359776&exchange_date=&beginDate=2017-06-01&endDate=2017-08-31&exchangeType=&channelf=1&prodType=4&chargeFzxh="+chargeFzxh;
//		Page payPage = getHtml(payUrl,webClient2,null);
//		parserPay(payPage);
		
		//账户信息
//		String accountUrl = "http://service.sh.189.cn/service/service/authority/queryInfo/getMsgByDeviceId.do";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("DeviceId", "17316359776");
//		getHtml(accountUrl,map);

		
		//通话记录
		String callUrl = "http://service.sh.189.cn/service/service/authority/query/billdetailQuery.do?begin=0&end=100&flag=1&devNo=17316359776&dateType=his&bill_type=SCP&moPingType=LOCAL&queryDate=2017%2F09&startDate=&endDate=";
		getHtml(callUrl,webClient2,null);
		
		//短信信息
//		String msgUrl = "http://service.sh.189.cn/service/service/authority/query/billdetailQuery.do?begin=0&end=10&flag=1&devNo=17316359776&dateType=his&bill_type=SMSC&moPingType=LOCAL&queryDate=2017%2F09&startDate=&endDate=";
//		getHtml(msgUrl,webClient2,null);
	}
	
	
	public static void parserPay(Page payPage) {
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<TelecomShanghaiPayfee>>(){}.getType();
		DataBean<TelecomShanghaiPayfee> data = gson.fromJson(payPage.getWebResponse().getContentAsString(),userListType);
		List<TelecomShanghaiPayfee> list = data.getList();
		for(TelecomShanghaiPayfee payfee:list){
			System.out.println("****************************************缴费信息");
			System.out.println(payfee.toString());
		}
	}

	public static void parserUserinfo(HtmlPage userPage) {
		Gson gson = new Gson();
		Type userListType = new TypeToken<DataBean<TelecomShanghaiUserInfo>>(){}.getType();
		DataBean<TelecomShanghaiUserInfo> data = gson.fromJson(userPage.getWebResponse().getContentAsString(), userListType);
		TelecomShanghaiUserInfo telecomShanghaiUserInfo = data.getRESULT();
		System.out.println("****************************************用户信息");
		System.out.println(telecomShanghaiUserInfo.toString());
	}


	public static void getParam(Page page) {
		Document doc =Jsoup.parse(page.getWebResponse().getContentAsString());
		chargeFzxh = doc.getElementById("defaultFzxh").attr("value");
	}


	public static WebClient login() throws Exception {

		String url2 = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url2,webClient2,null);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("17316359776");
		passwordInput.setText("139764");

		HtmlPage htmlpage2 = button.click();
		String url = htmlpage2.getUrl().toString();
		
		System.out.println(" url  =======>>"+url);
		
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		} else {
			System.out.println("=======成功==============");
		}

		System.out.println("************************************************点击后源码");
		System.out.println(htmlpage2.getWebResponse().getContentAsString());
		
		Set<Cookie> cookies = webClient2.getCookieManager().getCookies();
		for(Cookie cookie : cookies){
			System.out.println("cookie  ====== "+cookie.getName()+" : "+cookie.getValue());
		}
		return webClient2;
	}
	
	
	public static Page getHtml(String url, WebClient webClient2, String fromData) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			
		if(null != fromData){
			webRequest.setRequestBody(fromData);
		}
		
		webClient2.setJavaScriptTimeout(20000);

		webClient2.getOptions().setTimeout(20000); // 15->60

		Page searchPage = webClient2.getPage(webRequest);
		System.out.println(url+"   =========================================>>源码   ");
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		Set<Cookie> cookies = webClient2.getCookieManager().getCookies();
		for(Cookie cookie : cookies){
			System.out.println("cookie  ====== "+cookie.getName()+" : "+cookie.getValue());
		}
		return searchPage;

	}
	
	public static String getHtml(String url,Map<String,String> map) throws Exception{
		String html = "";
		URL gsurl = new URL(url);
		WebRequest request = new WebRequest(gsurl, HttpMethod.POST);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : map.entrySet()) {  
			list.add(new NameValuePair(entry.getKey(), entry.getValue()));  
		}  
		request.setRequestParameters(list);
			
		Page page = webClient2.getPage(request);
		int code = page.getWebResponse().getStatusCode();
//		System.out.println(code);
		if(code == 200){
			html = page.getWebResponse().getContentAsString();		
			System.out.println("***************************************************  post请求");
			System.out.println(html);
		}

		return html;	
	}
	
	
	
	
	public static Page getHtml(String url) throws Exception{
		
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "service.sh.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://service.sh.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://service.sh.189.cn/service/mytelecom/cusInfo");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn",".ybtj.189.cn", "54016232387D31EC06D53D93C1E31657"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","SESSION", "0694ffc1-eb1b-4d95-8fc1-0b31f1707cf3"));
		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","JSESSIONID", "64D498F99347AA0DBF34D52E9B41BA52"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","SHOPID_COOKIEID", "10003"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","_gscbrs_1708861450", "1"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","_gscs_1708861450", "05185060v6c5be14|pv:9"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","_gscu_1708861450", "05185060vtmpj814"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","_pk_id.5.3ec1", "57ab0809500d7e95.1505185297.1.1505186614.1505185297."));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","_pk_ses.5.3ec1", "*"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","cityCode", "sh"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","isLogin", "logined"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","loginStatus", "logined"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","nvid", "1"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","lvid", "fd6b1963121f88e812f59877e77555bf"));
		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","route", "c54c4a4b84d749f0f2e9ca5537249dab"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","s_cc", "true"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","s_fid", "65A6B80D87A1BB3D-07ECEBACFA810E71"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","s_sq", "eshipeship-189-all%3D%2526pid%253D%25252Fsh%25252F%2526pidt%253D1%2526oid%253Dhttp%25253A%25252F%25252Fwww.189.cn%25252Fdqmh%25252FssoLink.do%25253Fmethod%25253Dskip%252526platNo%25253D93507%252526toStUrl%25253Dhttp%25253A%25252F%25252Forder.sh.189.cn%25252Ftrade%25252Fbroa%2526ot%253DA"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","trkHmCity", "sh"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","trkHmClickCoords", "783%2C40%2C4454"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","trkHmCoords", "742%2C37%2C790%2C53%2C4454"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","trkHmLinks", "http%3A%2F%2Fwww.189.cn%2Fdqmh%2FssoLink.do%3Fmethod%3Dskip%26platNo%3D93507%26toStUrl%3Dhttp%3A%2F%2Forder.sh.189.cn%2Ftrade%2Fbroadband%2FqueryLogin.jhtml"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","trkHmPageName", "%2Fsh%2F"));
//		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","trkId", "7A30CC8A-07FA-4823-A776-01EC72EF0B87"));
		/*webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","uname1", "de300b083aa9dcd"));
		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","uname2", "6d8e5862b1f5c1d78"));
		webClient2.getCookieManager().addCookie(new Cookie("service.sh.189.cn","userId", "201%7C20170000000019782708"));*/
		
		Page page = webClient2.getPage(webRequest);
		System.out.println("*****************************************************************个人信息  ");
		System.out.println(page.getWebResponse().getContentAsString());
		
		return page;
		
	}


}
