package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

public class TestZhejiang {
	
	private static WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
	private static String integration_id = "";
	private static String serv_type_id = "";
	
	
	public static void main(String[] args) throws Exception {
		
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		login();
		
//		String baseUrl = "http://zj.189.cn/zjpr/servicenew/initMyhome/initMyhome.html";
//		getHtml(baseUrl,webClient2,null);
////		getHtml(baseUrl);
//		
//		//用户星级
////		String starUrl = "http://zj.189.cn/zjpr/servicenew/queryAccountInfo.htm";
////		getHtml(starUrl,webClient2,null);
//		
//		//查询页面
//		String queryUrl = "http://zj.189.cn/zjpr/service/query/query_order.html?menuFlag=1";
//		getHtml(queryUrl,webClient2,null);
//		
//		//套餐
////		String taocanUrl = "http://zj.189.cn/zjpr/servicenew/gettaocan.htm";
////		getHtml(taocanUrl,webClient2,null);
//		
//		String realUrl = "http://zj.189.cn/zjpr/user/Userinfodef/groupRealNameVerify.htm";
//		getHtml(realUrl,webClient2,null);
//		
//		//获取请求参数
//		String serviceUrl = "http://zj.189.cn/bfapp/buffalo/cdrService";
//		String servicepayload = "<buffalo-call><method>querycdrasset</method></buffalo-call>";
//		String serviceHtml = getHtmlPOST(serviceUrl,null,servicepayload);
//		System.out.println("serviceHtml ==============================================="+serviceHtml);
//		parserParam(serviceHtml);
		
		//发送验证码
		/*String sendUrl = "http://zj.189.cn/bfapp/buffalo/VCodeOperation";
		String formload = "<buffalo-call><method>SendVCodeByNbr</method><string>18057176046</string></buffalo-call>";
		getHtmlPOST(sendUrl,null,formload);*/
		
//		@SuppressWarnings("resource")
//		Scanner scanner = new Scanner(System.in);
//		String code = scanner.next();
//		
//		//获取通话记录
//		String callUrl = "http://zj.189.cn/zjpr/cdr/getCdrDetail.htm?flag=1&cdrCondition.pagenum=1&cdrCondition.pagesize=500&cdrCondition.productnbr=18057176046&cdrCondition.areaid=571&cdrCondition.cdrlevel=&cdrCondition.productid="+integration_id+"&cdrCondition.product_servtype="+serv_type_id+"&cdrCondition.recievenbr=%D2%C6%B6%AF%B5%E7%BB%B0&cdrCondition.cdrmonth=201709&cdrCondition.cdrtype=11&cdrCondition.usernameyanzheng=%C1%F5%D1%C7&cdrCondition.idyanzheng=341225199401020415&cdrCondition.randpsw="+code;
//		Page callPage = getHtml(callUrl,webClient2,null);
//		fileWriter("call",callPage.getWebResponse().getContentAsString());
//		
//	/*	//获取短信记录
//		String msgUrl = "http://zj.189.cn/zjpr/cdr/getCdrDetail.htm?flag=1&cdrCondition.pagenum=1&cdrCondition.pagesize=100&cdrCondition.productnbr=18057176046&cdrCondition.areaid=571&cdrCondition.cdrlevel=&cdrCondition.productid="+integration_id+"&cdrCondition.product_servtype="+serv_type_id+"&cdrCondition.recievenbr=%D2%C6%B6%AF%B5%E7%BB%B0&cdrCondition.cdrmonth=201709&cdrCondition.cdrtype=21&cdrCondition.usernameyanzheng=%C1%F5%D1%C7&cdrCondition.idyanzheng=341225199401020415&cdrCondition.randpsw="+code;
//		Page msgPage = getHtml(msgUrl,webClient2,null);
//		fileWriter("message",msgPage.getWebResponse().getContentAsString());
//		
//		//充值缴费
//		String payUrl = "http://zj.189.cn/zjpr/service/paym/payment_recharge.html?pgPHS.startDate=6";
//		Page payPage = getHtml(payUrl,webClient2,null);
//		fileWriter("pay",payPage.getWebResponse().getContentAsString());*/
	}
	

	private static void fileWriter(String string, String contentAsString) {
		 File file = new File("D:app\\" +string+".txt");  
		 OutputStream out = null;  
	        try {  
	            // 根据文件创建文件的输出流  
	            out = new FileOutputStream(file);  
	            // 把内容转换成字节数组  
	            byte[] data = contentAsString.getBytes();  
	            // 向文件写入内容  
	            out.write(data);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                // 关闭输出流  
	                out.close();  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	}


	public static void parserParam(String serviceHtml) {
		Document doc = Jsoup.parse(serviceHtml);
		Element e = doc.select("string:contains(integration_id)").first();
		integration_id = e.nextElementSibling().text();
		Element a = doc.select("string:contains(serv_type_id)").first();
		serv_type_id = a.nextElementSibling().text();
		System.out.println("integration_id ： "+integration_id);
		System.out.println("serv_type_id ： "+serv_type_id);
	}


	public static WebClient login() throws Exception {

		String url2 = "http://login.189.cn/login";
		HtmlPage html = (HtmlPage) getHtml(url2,webClient2,null);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("17342000534");
		passwordInput.setText("001314");

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
		
//		HtmlElement button1 = htmlpage2.getFirstByXPath("//a[@href='http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10012&toStUrl=http://zj.189.cn/zjpr/balancep/getBalancep.htm']");
//		System.out.println("*****************按钮 ==》"+button1.asXml());
//		
//		HtmlPage adminPage = button1.click();
//		String adminurl = adminPage.getUrl().toString();
//		System.out.println(" adminurl  =======>>"+adminurl);
//		
//		System.out.println("***********************************************跳转查询页");
//		System.out.println(adminPage.getWebResponse().getContentAsString());
//		
		
		return webClient2;
	}
	
	
	public static Page getHtml(String url, WebClient webClient2, String fromData) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		webRequest.setAdditionalHeader("Host", "zj.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://zj.189.cn/service");
			
		if(null != fromData){
			webRequest.setRequestBody(fromData);
		}
		
		webClient2.setJavaScriptTimeout(20000);

		webClient2.getOptions().setTimeout(20000); // 15->60

		Page searchPage = webClient2.getPage(webRequest);
		System.out.println(url+"   =========================================>>源码   ");
		System.out.println(searchPage.getWebResponse().getContentAsString());
//		System.out.println("当前链接============》》"+searchPage.getUrl().toString());
		Set<Cookie> cookies = webClient2.getCookieManager().getCookies();
		for(Cookie cookie : cookies){
			System.out.println("cookie  ====== "+cookie.getName()+" : "+cookie.getValue());
		}
		return searchPage;

	}
	
	public static String getHtmlPOST(String url,Map<String,String> map,String payload) throws Exception{
		String html = "";
		URL gsurl = new URL(url);
		WebRequest request = new WebRequest(gsurl, HttpMethod.POST);
		
		request.setAdditionalHeader("Host", "zj.189.cn");
		request.setAdditionalHeader("Referer", "http://zj.189.cn/zjpr/service/query/query_order.html?menuFlag=1");
		request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		request.setAdditionalHeader("Pragma", "no-cache");
		request.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
		request.setAdditionalHeader("X-Buffalo-Version", "2.0");
				
		if(null != map){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : map.entrySet()) {  
				list.add(new NameValuePair(entry.getKey(), entry.getValue()));  
			}  
			request.setRequestParameters(list);			
		}
		
		if(null != payload){
			request.setRequestBody(payload);
		}
			
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
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "zj.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/zj/");
		
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn",".ybtj.189.cn", "9B399B8F9572A49DC60A2DAE65975FAD"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","Hm_lpvt_cac7f443cf800580e934d6d809b26a22", "1505963239"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","Hm_lvt_cac7f443cf800580e934d6d809b26a22", "1505962966"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","JSESSIONID", "C1D4A3D688CAC1BE381DFD1B3413A4CA.node1"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","SHOPID_COOKIEID", "10012"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","_pk_id.3.08a5", "0603a3169ab0f035.1505962966.1.1505962966.1505962966."));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","_pk_ses.3.08a5", "*"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","accessTokenId", "f009d861ef1c06c557e489e046bac00df0c998a2b756f43e73138d1bc26d22034ae9a1a384358df461d45f72db10b6b22084d926e7c760d14dc5b96afc4ec6fb"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","access_ip", "123.126.87.169"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","area_id_choose", "571"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","cityCode", "zj"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","city_id_choose", "571003"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","city_id_ip", "null"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","loginStatus", "logined"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","lvid", "c47285dc38179ea85adaf56d73548b28"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","name", "new_uc_dialog"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","nvid", "1"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","s_cc", "true"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","s_fid", "037CC4B4C264BA00-331286F5479BEBE1"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","s_sq", "eshipeship-189-all%3D%2526pid%253D%25252Fzj%25252F%2526pidt%253D1%2526oid%253Dhttp%25253A%25252F%25252Fwww.189.cn%25252Fdqmh%25252FssoLink.do%25253Fmethod%25253DlinkTo%252526platNo%25253D10012%252526toStUrl%25253Dhttp%25253A%25252F%25252Fzj.189.cn%25252Fzjpr%25252Fbalancep%25252F%2526ot%253DA"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","sto-id-36895", "HNADBOKMJABP"));
//		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","svid", "64C28D49928D46BE"));
/*		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","trkHmCity", "zj"));
		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","trkHmClickCoords", "309%2C285%2C4454"));
		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","trkHmCoords", "290%2C271%2C361%2C301%2C4454"));
		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","trkHmLinks", "http%3A%2F%2Fwww.189.cn%2Fdqmh%2FssoLink.do%3Fmethod%3DlinkTo%26platNo%3D10012%26toStUrl%3Dhttp%3A%2F%2Fzj.189.cn%2Fzjpr%2Fbalancep%2FgetBalancep.htm"));
		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","trkHmPageName", "%2Fzj%2F"));
		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","trkId", "C12C0E7B-858D-42F9-A92E-3D7FFC0C31C8"));
		webClient2.getCookieManager().addCookie(new Cookie("zj.189.cn","userId", "201%7C20140000000008087832"));*/
	
		
		Page page = webClient2.getPage(webRequest);
		System.out.println("*****************************************************************个人信息  ");
		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("**********当前网站的url            ==>>>>>"+page.getUrl().toString());
		
		/*if("http://zj.189.cn/service".equals(page.getUrl().toString())){
			login();
		}*/
		return page;
		
	}

}
