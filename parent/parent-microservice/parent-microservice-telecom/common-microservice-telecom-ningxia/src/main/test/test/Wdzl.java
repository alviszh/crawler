package test;

import java.net.URL;
import java.util.Map;
import java.util.Set;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.module.htmlunit.WebCrawler;

public class Wdzl {

	public static void main(String[] args) {
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//		try {
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000142&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//					System.out.println("请求通过！");
//					String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//					WebRequest webRequest;
//					String requestPayload="<buffalo-call><method>queryProductForWebService</method><map><type>ct.qryonuse.Condition</type><string>_pageSize</string><string>100</string><string>_pageNum</string><string>1</string><string>_flag</string><string>true</string></map></buffalo-call>";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					
//					webRequest.setAdditionalHeader("Accept", "*/*");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "nx.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/cp/gl/?fastcode=20000142&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//					webRequest.setRequestBody(requestPayload);
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
		//////////////////////////星级客服经理组////////////////////////////
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//		try {
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000515&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//					System.out.println("请求通过！");
//					String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//					WebRequest webRequest;
//					String requestPayload="<buffalo-call><method>qryClubMemberStaff</method></buffalo-call>";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					
//					webRequest.setAdditionalHeader("Accept", "*/*");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "nx.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/stars/mng/?fastcode=20000515&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//					webRequest.setRequestBody(requestPayload);
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//					
//					String star = "";
//					String[] starbite = contentAsString.split("<string>_custLevelName</string>");
//					String substringpaperstype = starbite[1].substring(0, 6);
//					if("<null>".equals(substringpaperstype)){
//						star = "无信息";
//					}else{
//						String[] split2addr = starbite[1].split("</string>");
//						star = split2addr[0].substring(8, split2addr[0].length());
//						System.out.println(star);
//					}
//					
//				}
//		}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		//////////////////////////////////////汇总积分查询//////////////////////////////////////
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//		try {
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000515&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//					System.out.println("汇总积分查询请求通过！");
//					String url = "http://nx.189.cn/bfapp/buffalo/CtCustService";
//					WebRequest webRequest;
//					String requestPayload="<buffalo-call><method>qry_CustPoints_For_Webservice</method><string>jfcx.jsp</string></buffalo-call>";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					
//					webRequest.setAdditionalHeader("Accept", "*/*");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "nx.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/point/qry/?fastcode=10000515&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//					webRequest.setRequestBody(requestPayload);
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//				}
//		}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String all = "<buffalo-reply><map><type>crm2.unSoo.cp.qryGather.Point</type><string>_value</string><string>33</string><string>_monthValue</string><string>0</string><string>_dueValue</string><string>0</string><string>_exchangeFlag</string><string>YES</string></map></buffalo-reply>";
//		String integral = "";
//		String[] integralbite = all.split("<string>_value</string>");
//		String substringpaperstype = integralbite[1].substring(0, 6);
//		if("<null>".equals(substringpaperstype)){
//			integral = "无信息";
//		}else{
//			String[] split2addr = integralbite[1].split("</string>");
//			integral = split2addr[0].substring(8, split2addr[0].length());
//			System.out.println(integral);
//		}
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//		try {
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000515&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//					System.out.println("汇总积分查询请求通过！");
//					String url = "http://nx.189.cn/bfapp/buffalo/CtCustService";
//					WebRequest webRequest;
//					String requestPayload="<buffalo-call><method>qry_CustPoints_For_Webservice</method><string>jfcx.jsp</string></buffalo-call>";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					
//					webRequest.setAdditionalHeader("Accept", "*/*");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "nx.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/point/qry/?fastcode=10000515&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//					webRequest.setRequestBody(requestPayload);
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String all = "<buffalo-reply><map><type>crm2.unSoo.cp.qryGather.Point</type><string>_value</string><string>33</string><string>_monthValue</string><string>0</string><string>_dueValue</string><string>0</string><string>_exchangeFlag</string><string>YES</string></map></buffalo-reply>";
//		String integral = "";
//		String[] integralbite = all.split("<string>_value</string>");
//		String substringpaperstype = integralbite[1].substring(0, 6);
//		if("<null>".equals(substringpaperstype)){
//			integral = "无信息";
//		}else{
//			String[] split2addr = integralbite[1].split("</string>");
//			integral = split2addr[0].substring(8, split2addr[0].length());
//			System.out.println(integral);
//		}
		
	/////////////////////////////////////////我的余额/////////////////////////////////////////////////
//		try {
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//					String url = "http://www.189.cn/dqmh/homogeneity/queryBalance.do";
//					WebRequest webRequest;
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "www.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://www.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://www.189.cn/dqmh/homogeneity/initCost.do?menuType=callandbalance&fastcode=20000777&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//					JSONObject jsonObj = JSONObject.fromObject(contentAsString);
//					JSONObject jsonObj2= (JSONObject) jsonObj.get("obj");
//					System.out.println(jsonObj2);
//					String remainMoney = jsonObj2.getString("totalBalance");
//					System.out.println(remainMoney);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//////////////////////////////// 增值业务组/////////////////////////////
		
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//		try {
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000506&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//					String url = "http://nx.189.cn/bfapp/buffalo/CtProdService";
//					WebRequest webRequest;
//					String requestPayload="<buffalo-call><method>qryVsop</method><string>18995154123</string></buffalo-call>";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					
//					webRequest.setAdditionalHeader("Accept", "*/*");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "nx.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/zz/td/?fastcode=10000506&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//					webRequest.setRequestBody(requestPayload);
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//					
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		////////////////////////////////交易单查询/////////////////////////////
		
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequestlogin;
//		try {
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000137&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//					String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//					WebRequest webRequest;
//					String phone = "18995154123";
//					String requestPayload="<buffalo-call><method>qry_Order_jiaoyi_new</method><string>"+phone+"</string><string>20150101</string><string>20350101</string></buffalo-call>";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					
//					webRequest.setAdditionalHeader("Accept", "*/*");
//					webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					webRequest.setAdditionalHeader("Connection", "keep-alive");
//					webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					webRequest.setAdditionalHeader("Host", "nx.189.cn");
//					webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//					webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/dd/jyd/?fastcode=20000137&cityCode=nx");
//					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//					webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//					webRequest.setRequestBody(requestPayload);
//					Page page = webclientlogin.getPage(webRequest);
//					String contentAsString = page.getWebResponse().getContentAsString();
//					System.out.println(contentAsString);
//					
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//		String base = "<buffalo-reply><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu</type><string>IRESULT</string><string>0</string><string>SMSG</string><string>成功!</string><string>CREATED_DATE</string><string>20170829103318</string><string>lists</string><list><type>java.util.ArrayList</type><length>60</length><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170801050034</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170701035628</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170601024719</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170501025524</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170401025155</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170301030114</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170201032948</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20170101031055</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161201031104</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161129153430</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>22400</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>7</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161129153425</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>537600</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>7</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161102083143</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161101004646</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161006095937</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20161001004909</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160901013733</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160828215433</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160805072021</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>10000</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160801004800</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160703142314</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160701005420</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160601005421</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160530120721</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160530120704</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160502075116</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>10000</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160501010448</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160402015001</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>5000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>10000</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160401005938</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160328080303</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>5000</int><string>ORG_NAME</string><string>CRM积分兑换</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>24002</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160302075643</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160301010257</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160202190030</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>2000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160201010633</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160101102528</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>10000</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20160101010723</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20151201012347</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20151129152108</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20151105174510</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20151101014503</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20151009073250</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20151001022219</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150901130731</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150901021032</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150801221430</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150801020804</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150701021724</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150616201715</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>5000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>10000</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150602073833</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150601022354</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150501025242</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150421180523</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>2000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150410211734</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150401030436</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150312170059</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>5000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150301031113</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150227232313</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>8798</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150201030010</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>4000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150128095414</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>20000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>7</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150128095256</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>99000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>7</string><string>PRODUCT_NAME_CODE</string><string>2</string></map><map><type>cn.ffcs.ct10000.pojo.ocs.FuKuanJiLu_Lists</type><string>ACCT_NBR_97</string><string>10732369</string><string>START_TIME</string><string>20150101001623</string><string>PAYMENT_METHOD_NAME</string><string>现金</string><string>PAYMENT_CHARGE</string><int>10000</int><string>ORG_NAME</string><string>宁夏电信公司</string><string>ACCT_NAME</string><string>冯涛</string><string>ACCT_ADDR</string><string>银川市兴庆区园艺场92号</string><string>STAFF_ID</string><string>15</string><string>PRODUCT_NAME_CODE</string><string>2</string></map></list></map></buffalo-reply>";
//		Document doc = Jsoup.parse(base); 
//		Elements link1 = doc.getElementsByTag("list"); 
//		Elements elementsByTag = link1.get(0).getElementsByTag("map"); 
//		for (Element element : elementsByTag) { 
//			TelecomNingxiaPayMsg telecomNingxiaPayMsg = new TelecomNingxiaPayMsg();
//			Elements link2 = element.getElementsByTag("string"); 
//			//缴费时间
//			String paydate = link2.get(3).text(); 
//			System.out.println(paydate);
//			//缴费方式
//			String payway = link2.get(5).text(); 
//			System.out.println(payway);
//			//缴费金额
//			Elements link = element.getElementsByTag("int"); 
//			String paymoney = link.get(0).text(); 
//			paymoney = paymoney.substring(0,paymoney.length()-2)+"元";
//			System.out.println(paymoney);
//			//缴费地点
//			String payaddr = link2.get(8).text()+link2.get(12).text(); 
//			System.out.println(payaddr);
//			telecomNingxiaPayMsg.setPaydate(paydate);
//			telecomNingxiaPayMsg.setPayway(payway);
//			telecomNingxiaPayMsg.setPaymoney(paymoney);
//			telecomNingxiaPayMsg.setPayaddr(payaddr);
//		} 
		
		
		//////////////////////////////////////每月的账单明细//////////////////////////////////////
//		String loginurl = "http://login.189.cn/login";
//		WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		try{
//		WebRequest webRequestlogin;
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String url = "http://www.189.cn/dqmh/homogeneity/balanceOutDetailQuery.do?billingcycle=201707";
//				WebRequest webRequest;
//				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//				HtmlPage page = webclientlogin.getPage(webRequest);
//				System.out.println(page.asXml());
//				Document doc=Jsoup.parse(page.asXml());
//				//月份
//				String month = "";
//				//当月合计
//				String monthall = doc.select("span.fs24").get(0).text();
//				//月使用费
//				Elements select = doc.select("li");
//				boolean empty = select.isEmpty();
//				if(empty){
//					    System.out.println("dom节点li不存在！");
//				}else{
//					
//					String monthfee = select.get(1).child(0).text();
//					//综合信息服务费
//					String notemmsfee = select.get(3).child(0).text()+"  "+select.get(4).child(0).text();
//					System.out.println(notemmsfee);
//				}
//	        }
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		
		
		///////////////////////////////////语音////////////////////////////////////////
//		try{
//			String loginurl = "http://login.189.cn/login";
//			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
//		    WebRequest webRequestlogin;
//			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
//			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
//			//获取对应的输入框
//			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
//			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
//			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
//			username.setText("18995154123");
//			passwordInput.setText("795372");
//			HtmlPage htmlpage = button.click();
//			
//			String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
//			Set<Cookie> qzb = CommonUnit.transferJsonToSet(cookieString);
//			for(Cookie cookie:qzb){ 
//				webclientlogin.getCookieManager().addCookie(cookie); 
//			} 
//
//			String asXml = htmlpage.asXml();
//			System.out.println("------------"+asXml);
//			if (asXml.indexOf("登录失败") != -1) {
//				System.out.println("登录失败！");
//			}else{
//				System.out.println("登录成功！");
//				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000776&cityCode=nx";
//				WebRequest webRequestwdzl;
//				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
//				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
//				
//				String cookieString1 = CommonUnit.transcookieToJson(wdzl.getWebClient());
//				Set<Cookie> basecookies1 = CommonUnit.transferJsonToSet(cookieString1);
//				for(Cookie cookie:basecookies1){ 
//					webclientlogin.getCookieManager().addCookie(cookie); 
//				} 
//				
//				int statusCode = wdzl.getWebResponse().getStatusCode();
//				if(statusCode==200){
//						String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//						WebRequest webRequest;
//						String requestPayload="<buffalo-call><method>qry_sj_yuyinfeiqingdan</method><string>20170701</string><string>20170731</string></buffalo-call>";
//						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//						
//						webRequest.setAdditionalHeader("Accept", "*/*");
//						webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//						webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//						webRequest.setAdditionalHeader("Connection", "keep-alive");
//						webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//						webRequest.setAdditionalHeader("Host", "nx.189.cn");
////						webRequest.setAdditionalHeader("Cookie", "JSESSIONID=56K1ZmmCKNYjxzVhDkNfZJrpqM9JF0cDGy83C8HPhzsSthjmFytK!-1122894865");
//						webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
//						webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//						webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//						webRequest.setAdditionalHeader("X-Buffalo", "2.0");
//						webRequest.setRequestBody(requestPayload);
//						Page page = webclientlogin.getPage(webRequest);
//						String contentAsString = page.getWebResponse().getContentAsString();
//						System.out.println(contentAsString);
//						if("<buffalo-reply><null></null></buffalo-reply>".equals(contentAsString)){
//							System.out.println("没有查到对应的数据！");
//						}else{
//							System.out.println("数据获取成功！");
//							System.out.println(contentAsString);
//						}
//				}
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		
		
        ///////////////////////////////////短信////////////////////////////////////////
		
		try{
			String loginurl = "http://login.189.cn/login";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
		    WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
			//获取对应的输入框
			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			username.setText("18995154123");
			passwordInput.setText("795372");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("------------"+asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			}else{
				System.out.println("登录成功！");
				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000776";
				WebRequest webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				
				
				int statusCode = wdzl.getWebResponse().getStatusCode();
				if(statusCode==200){
				    //发送验证码接口
				    String sendurl = "http://nx.189.cn/bfapp/buffalo/CtSubmitService";
				    String requestPayloadSend="<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
				    WebRequest webRequestSend = new WebRequest(new URL(sendurl), HttpMethod.POST);
				    webRequestSend.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				    webRequestSend.setAdditionalHeader("Host", "nx.189.cn");
				    webRequestSend.setAdditionalHeader("Origin", "http://nx.189.cn");
				    webRequestSend.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				    webRequestSend.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				    webRequestSend.setRequestBody(requestPayloadSend);
				    Page pageSend = webclientlogin.getPage(webRequestSend);
					String send = pageSend.getWebResponse().getContentAsString();
					System.out.println("发送验证码接口"+send);
					
					Set<Cookie> logincookies = webclientlogin.getCookieManager().getCookies();
					for (Cookie cookie3 : logincookies) {
						System.out.println("发送请求的cookie ："+cookie3.toString());
					}
					
					
					
						//验证码第一个接口
//					    String url1 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//					    String requestPayload1="<buffalo-call><method>getCustOfBillByCustomerCode</method><string>2951121277270000__giveup</string></buffalo-call>";
//					    WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.POST);
//					    webRequest1.setAdditionalHeader("Accept", "*/*");
//					    webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					    webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					    webRequest1.setAdditionalHeader("Connection", "keep-alive");
//					    webRequest1.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					    webRequest1.setAdditionalHeader("Host", "nx.189.cn");
//					    webRequest1.setAdditionalHeader("Origin", "http://nx.189.cn");
//	//					webRequest1.setAdditionalHeader("Cookie", "JSESSIONID=56K1ZmmCKNYjxzVhDkNfZJrpqM9JF0cDGy83C8HPhzsSthjmFytK!-1122894865");
//					    webRequest1.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//					    webRequest1.setRequestBody(requestPayload1);
//					    Page page1 = webclientlogin.getPage(webRequest1);
//						String string1 = page1.getWebResponse().getContentAsString();
//						System.out.println("第一个接口"+string1);
						
						//////////////////////可以得到数据
						//验证码第二个接口
//						String url2 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//						String requestPayload2="<buffalo-call><method>getFeeNumByHT</method><string>10732369</string><string>201707</string></buffalo-call>";
//						WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
//						webRequest2.setAdditionalHeader("Accept", "*/*");
//						webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//						webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//						webRequest2.setAdditionalHeader("Connection", "keep-alive");
//						webRequest2.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//						webRequest2.setAdditionalHeader("Host", "nx.189.cn");
//						webRequest2.setAdditionalHeader("Origin", "http://nx.189.cn");
//						//webRequest1.setAdditionalHeader("Cookie", "JSESSIONID=56K1ZmmCKNYjxzVhDkNfZJrpqM9JF0cDGy83C8HPhzsSthjmFytK!-1122894865");
//						webRequest2.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//						webRequest2.setRequestBody(requestPayload2);
//					    Page page = webclientlogin.getPage(webRequest2);
//						String string2 = page.getWebResponse().getContentAsString();
//						System.out.println("第二个接口"+string2);
						
//						///////////////////////////网页上没有数据是正常的
//						//验证码第三个接口
//						String url3 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//						String requestPayload3="<buffalo-call><method>getSelectedFeeProdNum</method><string>undefined</string><string>18995154123</string><string>2</string></buffalo-call>";
//						WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.POST);
//						webRequest3.setAdditionalHeader("Accept", "*/*");
//						webRequest3.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//						webRequest3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//						webRequest3.setAdditionalHeader("Connection", "keep-alive");
//						webRequest3.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//						webRequest3.setAdditionalHeader("Host", "nx.189.cn");
//						webRequest3.setAdditionalHeader("Origin", "http://nx.189.cn");
//						//					webRequest1.setAdditionalHeader("Cookie", "JSESSIONID=56K1ZmmCKNYjxzVhDkNfZJrpqM9JF0cDGy83C8HPhzsSthjmFytK!-1122894865");
//						webRequest3.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//						webRequest3.setRequestBody(requestPayload3);
//						Page page3 = webclientlogin.getPage(webRequest3);
//						String string3 = page3.getWebResponse().getContentAsString();
//						System.out.println("第三个接口"+string3);
//						
//						
//						//验证码第四个接口
//						String url4 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//						String requestPayload4="<buffalo-call><method>getSelectedFeeProdNum</method><string>undefined</string><string>18995154123</string><string>2</string></buffalo-call>";
//						WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.POST);
//						webRequest4.setAdditionalHeader("Accept", "*/*");
//						webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//						webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//						webRequest4.setAdditionalHeader("Connection", "keep-alive");
//						webRequest4.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//						webRequest4.setAdditionalHeader("Host", "nx.189.cn");
//						webRequest4.setAdditionalHeader("Origin", "http://nx.189.cn");
//						//					webRequest1.setAdditionalHeader("Cookie", "JSESSIONID=56K1ZmmCKNYjxzVhDkNfZJrpqM9JF0cDGy83C8HPhzsSthjmFytK!-1122894865");
//						webRequest4.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//						webRequest4.setRequestBody(requestPayload4);
//						Page page4 = webclientlogin.getPage(webRequest4);
//						String string4 = page4.getWebResponse().getContentAsString();
//						System.out.println("第四个接口"+string4);
//					
//					
//						
				
						
//						//短信验证码验证接口
//						String checkurl = "http://nx.189.cn/bfapp/buffalo/CtQryService";
//					    String requestPayloadCheck="<buffalo-call><method>validBillSMS</method><string>18995154123</string><string>112332</string></buffalo-call>";
//					    WebRequest webRequestCheck = new WebRequest(new URL(checkurl), HttpMethod.POST);
//					    webRequestCheck.setAdditionalHeader("Accept", "*/*");
//					    webRequestCheck.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//					    webRequestCheck.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//					    webRequestCheck.setAdditionalHeader("Connection", "keep-alive");
//					    webRequestCheck.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					    webRequestCheck.setAdditionalHeader("Host", "nx.189.cn");
////					    webRequestCheck.setAdditionalHeader("Cookie", "JSESSIONID=56K1ZmmCKNYjxzVhDkNfZJrpqM9JF0cDGy83C8HPhzsSthjmFytK!-1122894865");
//						webRequestCheck.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//						webRequestCheck.setRequestBody(requestPayloadCheck);
//					    Page pageCheck = webclientlogin.getPage(webRequestCheck);
//						String Check = pageCheck.getWebResponse().getContentAsString();
//						System.out.println("验证验证码的接口"+Check);
//						
//						
//						
//						
					    //获得数据的接口
						String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
						String requestPayload="<buffalo-call><method>qry_sj_cxclxd</method><string>20170801</string><string>20170831</string></buffalo-call>";
						WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						webRequest.setAdditionalHeader("Accept", "*/*");
						webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
						webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
						webRequest.setAdditionalHeader("Connection", "keep-alive");
						webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
						webRequest.setAdditionalHeader("Host", "nx.189.cn");
						webRequest.setAdditionalHeader("Cookie", "Tzj0ZmkJvgssy2t3nLSl5LrQd01B88Mc1dLMhLVTG7xkfCL3gpJX!-1122894865");
						webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
						webRequest.setRequestBody(requestPayload);
						Page page = webclientlogin.getPage(webRequest);
						String contentAsString = page.getWebResponse().getContentAsString();
						System.out.println(contentAsString);
						if("<buffalo-reply><null></null></buffalo-reply>".equals(contentAsString)){
							System.out.println("没有查到对应的数据！");
						}else{
							System.out.println("数据获取成功！");
							System.out.println(contentAsString);
						}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
}
}