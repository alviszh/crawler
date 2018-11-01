package app.test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.ws.rs.Encoded;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

public class CookieTest {

	private static WebClient webClient = null;
	public static String path;
	public static String code; 
	private static String phonenum = "18810705513"; 
//	public static String phonenum = "15911135456"; 
	public static String jsessionidEchdCptCmccJt;

	public static void main(String[] args) throws Exception {
		
//		busiqrydeal();
//		captchazh();
//		checkUidAvailable();
//		needVerifyCode();
//		chkNumberAction(); 
		sendSMS(phonenum);
		ajax(phonenum);
//		getUserMessage();
		Thread.sleep(50000L);
//		yoyo(phonenum);
//		billDetailQry();
//		temporaryAttestation();
//		secondSMSsend("15210072522");
//		download("http://shop.10086.cn/i/authImg?t=0.11407312090453536","D:\\img\\1.jpg");
//		preCheckCaptcha();
//		secondAttestation();
//		getResultData();
//		ocr();
		//Thread.sleep(10000L);
		//vovo(phonenum);
		//yoyo(phonenum);
	}
	
	public static void yoyo(String phonenum) throws Exception{
		System.out.println(phonenum+"---------jsessionidEchdCptCmccJt-----------"+jsessionidEchdCptCmccJt);
		String sms = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/"+phonenum;
		Document doc2 = Jsoup.connect(sms)
				.header("Host", "shop.10086.cn")
				.header("Referer", "http://shop.10086.cn/i/?f=home&welcome=1499080651399")
				//.header("Accept", "text/html,application/xhtml+xml,application/xml")
			    .cookie("jsessionid-echd-cpt-cmcc-jt", jsessionidEchdCptCmccJt)
			    .ignoreContentType(true)
			    .get();
		System.out.println(doc2.html());
		
	}
	
	//({"data":null,"retCode":"555002","retMsg":"尊敬的用户,单位时间内下发短信次数过多，请稍后再使用！","sOperTime":null})
	public static void vovo(String phonenum) throws Exception{
		System.out.println("-------------------vovo()-----------------");
		
		System.out.println("-----jsessionidEchdCptCmccJt ==>"+jsessionidEchdCptCmccJt);
		/*if (webClient == null) {
			webClient = getWebClient();
		} */ 
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		
		String domain = "shop.10086.cn";
		String sms = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/"+phonenum;
		WebRequest requestSettings = new WebRequest(new URL(sms), HttpMethod.GET);  
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome=1499080651399");
		requestSettings.setAdditionalHeader("Accept", "*/*");
		
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"CaptchaCode", "pEUNOI"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"CmLocation", "100|100"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"CmProvid", "bj"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"Hm_lpvt_bd8a03831fee34ebc98b4f4b9bbabba7", "1499080694"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"Hm_lvt_bd8a03831fee34ebc98b4f4b9bbabba7", "1,498,806,394,149,880,000,000,000,000,000,000,000,000"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"PHPSESSID", "rp6qtj54mmo3oknlj2i39v0982"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"SSOTime", "2017/7/3 11:16"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"WT_FPC", "id=2e25f60040bb43fe4fb1496904628549:lv=1499080699297:ss=1499080677429"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"c", "cb5bf6ad695348839f3c3c0265ecc312"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"cart_code_key", "3mjldsjqnnjcnvjm3v52e6nk35"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"cmccssotoken", "cb5bf6ad695348839f3c3c0265ecc312@.10086.cn"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"continue", "http://www.bj.10086.cn/service/"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"continuelogout", "http://www.bj.10086.cn/service/"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"freelogin_userlogout", ""));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"is_login", "TRUE"));
		//webClient.getCookieManager().addCookie(new Cookie(domain,"jsessionid-echd-cpt-cmcc-jt", jsessionidEchdCptCmccJt));
		webClient.getCookieManager().addCookie(new Cookie(domain,"jsessionid-echd-cpt-cmcc-jt", jsessionidEchdCptCmccJt));
		
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"loginName", "13520800817"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"mobileNo1", "36ebd8542f7bc3d060217eea8243112c89d11611@@1f328698aef02abb8da9e6872103ed2e0dfead7b@@1496904813717"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"mobileNo2", "dca60ff52ea11e143923898b11d58d2584b00325@@0f3446a45b33271b22f130a2ad775145ea725d01@@1496904923614"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"rdmdmd5", "65C745265BE4B9A16E47AF5835C2F6DB"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"ssologinprovince", "100"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"td_cookie", "18446744070135900000"));
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"userinfokey", "%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22100%22%2c%22pwdType%22%3a%2202%22%2c%22userName%22%3a%2213520800817%22%7d"));//{"loginType":"01","provinceName":"100","pwdType":"02","userName":"13520800817"}
		//--1--//webClient.getCookieManager().addCookie(new Cookie(domain,"verifyCode", "3ebd115851dbd9d2d20e069c78d0bf74867172a9")); 
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("vovo  cookie==>  "+cookie.getName()+":  "+cookie.getValue());	
		}
		
		Page page = webClient.getPage(requestSettings); 
		
		
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("第二次发送随机短信  ====》》"+html);		
		
	}
	
	
	public static void billDetailQry() throws Exception{
		
		String url = "http://shop.10086.cn/i/?f=billdetailqry&welcome="+System.currentTimeMillis();
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Page page = webClient.getPage(requestSettings); 
			
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("billDetailQry的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}
		
		String html = page.getWebResponse().getContentAsString();
		
		System.out.println(html);
		
		
	}
	
	public static void busiqrydeal() throws Exception{
		
		String url = "http://shop.10086.cn/i/v1/auth/loginfo?_="+System.currentTimeMillis();
		System.out.println(url);
		webClient = WebCrawler.getInstance().getWebClient();
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//		requestSettings.setAdditionalHeader("Cookie", "ssologinprovince=100; td_cookie=18446744070397051796; cart_code_key=rtn332rdk9b4pae6rddb0kbvc0; PHPSESSID=33jvd6q6p50masfdo2v2raobp4; td_cookie=18446744070390420834; Hm_lvt_bd8a03831fee34ebc98b4f4b9bbabba7=1498813817,1499047535,1499054002,1499062052; Hm_lpvt_bd8a03831fee34ebc98b4f4b9bbabba7=1499066636; userinfokey=%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22100%22%2c%22pwdType%22%3a%2202%22%2c%22userName%22%3a%2215210072522%22%7d; c=8de1e7988b204d42892c08b7b92a48aa; verifyCode=88899ac77e1bdfa802e9a67ed71f246c197309c1; jsessionid-echd-cpt-cmcc-jt=1059A782C4553734CF9FB721CFEDBAD5; freelogin_userlogout=; CaptchaCode=ZyCKMw; WT_FPC=id=275ee6bb7dbb9a3bde41498716643871:lv=1499072158891:ss=1499068771099; CmLocation=100|100; CmProvid=bj");
		
        Page page = webClient.getPage(requestSettings); 
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("busiqrydeal的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}		
		
		String json = page.getWebResponse().getContentAsString();   
//		System.out.println("busiqrydeal ------------------------"+json);
	}
	
	public static void captchazh() throws Exception{
		
		String url = "https://login.10086.cn/captchazh.htm?type=12";
		
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		
		requestSettings.setAdditionalHeader("Accept", "image/webp,image/*,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "login.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
		Page page = webClient.getPage(requestSettings); 
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("captchazh的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}
//		String json = page.getWebResponse().getContentAsString();   
//		System.out.println("captchazh ------------------------"+json);
		
	}
	
	
	public static void checkUidAvailable() throws Exception{
		String url = "https://login.10086.cn/checkUidAvailable.parser";
		
//		WebClient webClient = WebCrawler.getInstance().getWebClient();

		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST); 
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "login.10086.cn");
		requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=http%3A%2F%2Fshop.10086.cn%2Fmall_100_100.html%3Fforcelogin%3D1");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		Page page = webClient.getPage(requestSettings); 
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("checkUidAvailable的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}
		
		String json = page.getWebResponse().getContentAsString();   
		System.out.println("checkUidAvailable ------------------------"+json);
		
		
	}
	
	
	public static void needVerifyCode() throws Exception{
		
		String url = "https://login.10086.cn/needVerifyCode.htm?accountType=01&account=15210072522&timestamp="+System.currentTimeMillis();
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "login.10086.cn");
//		requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=http%3A%2F%2Fshop.10086.cn%2Fmall_100_100.html%3Fforcelogin%3D1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Page page = webClient.getPage(requestSettings); 
		String json = page.getWebResponse().getContentAsString();   
		System.out.println("needVerifyCode ------------------------"+json);
	}
	
	
	public static void chkNumberAction() throws Exception{
		
		String url = "https://login.10086.cn/chkNumberAction.parser";
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST); 
		
		
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "login.10086.cn");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=http%3A%2F%2Fshop.10086.cn%2Fmall_100_100.html%3Fforcelogin%3D1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("userName", "15210072522"));
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Page page = webClient.getPage(requestSettings); 
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("chkNumberAction的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}
		
		String json = page.getWebResponse().getContentAsString();   
		System.out.println("chkNumberAction ------------------------"+json);
	}
	
	
	
	public static void ajax(String phonenum) throws Exception{
		HtmlPage page1 = webClient.getPage("https://login.10086.cn/login.html");
		ScriptResult scriptResult = page1.executeJavaScript("encrypt('100094wsl')");
		
		String sss = scriptResult.getJavaScriptResult().toString();
		
		String t=URLEncoder.encode(sss, "GBK");
		
		System.out.println("加密的密码： "+t);
		String url = "https://login.10086.cn/login.htm";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine(); 
		System.out.println("code: " + code);
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		/*if (webClient == null) {
			webClient = getWebClient();
		} */
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		
		
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,fr;q=0.6");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
	    requestSettings.setAdditionalHeader("Host", "login.10086.cn");
	    requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/");
	    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
		
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("accountType", "01"));
		requestSettings.getRequestParameters().add(new NameValuePair("account", phonenum));
		requestSettings.getRequestParameters().add(new NameValuePair("password", t));
		requestSettings.getRequestParameters().add(new NameValuePair("pwdType", "01"));
		requestSettings.getRequestParameters().add(new NameValuePair("smsPwd", code));
		requestSettings.getRequestParameters().add(new NameValuePair("inputCode", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("backUrl", "http://shop.10086.cn/i/"));
		requestSettings.getRequestParameters().add(new NameValuePair("rememberMe", "0"));
		requestSettings.getRequestParameters().add(new NameValuePair("channelID", "12003"));
		requestSettings.getRequestParameters().add(new NameValuePair("protocol", "https:"));
//		requestSettings.getRequestParameters().add(new NameValuePair("isNew", "1"));
		requestSettings.getRequestParameters().add(new NameValuePair("timestamp",""+System.currentTimeMillis())); 
		
		String domain = "login.10086.cn";
		
//		webClient.getCookieManager().addCookie(new Cookie(domain,"FSSBBIl1UgzbN7N443T", "1srEP54u17LCJXJbKba9yqcRry2_QW5axGKQGKrAjvEjmuC799RfKW3tWzh0KaxRkqFyeKo0ZjGXWtkUaIroaKjxXdENuE75svMBDNHNWgEoaq4Cuwrwz8SnU_visdl4.t16Ko_rmdRzqo7vSWIHWkSwp5vrkN0W.ijlRyC5WfZXZ7q"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"FSSBBIl1UgzbN7N443S", "dOn30Hf0zJStFeAd0PnXuvEUDM3zGg.kSWGa3h39T8Npci0ceUIKsCxT9rtMAbR1"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"mobileNo1", "36ebd8542f7bc3d060217eea8243112c89d11611@@1f328698aef02abb8da9e6872103ed2e0dfead7b@@1499222612846"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"SSOTime", "2017-07-05 10:45:47"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"continue", "http%3A%2F%2Fservice.bj.10086.cn%2Fpoffice%2Fpackage%2Fshowpackage.parser%3FPACKAGECODE%3DXD%26PRODUCTSHOWCODE%3D%26isAutonomous%3D1"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"continuelogout", "http%3A%2F%2Fservice.bj.10086.cn%2Fpoffice%2Fpackage%2Fshowpackage.parser%3FPACKAGECODE%3DXD"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"freelogin_userlogout", ""));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"cmccssotoken", "54811767e7ad49ceae3b5c3061309370@.10086.cn"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"is_login", "true"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"userinfokey", "%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22100%22%2c%22pwdType%22%3a%2201%22%2c%22userName%22%3a%2215210072522%22%7d"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"loginName", "15210072522"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"c", "54811767e7ad49ceae3b5c3061309370"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"verifyCode", "88899ac77e1bdfa802e9a67ed71f246c197309c1"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"CmLocation", "100|100"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"CmProvid", "bj"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"jsessionid-echd-cpt-cmcc-jt", "B7430D01378D4D8DDFA74A834EC60C19"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"WT_FPC", "id=26ccbee8b7c64e0fbf11499220169907:lv=1499232785964:ss=1499232785645"));
//		webClient.getCookieManager().addCookie(new Cookie(domain,"CaptchaCode", "XMqrGV"));		
//		webClient.getCookieManager().addCookie(new Cookie(domain,"rdmdmd5", "B21D251B079022736A71FD6B2788BECA"));
			
		
		Page page = webClient.getPage(requestSettings); 
		Set<Cookie> set = webClient.getCookieManager().getCookies();
		for(Cookie cookie:set){
			System.out.println("第一次认证  cookie==>  "+cookie.getName()+":  "+cookie.getValue());	
		}
		String json = page.getWebResponse().getContentAsString();   
		System.out.println("第一次认证  ====》》"+json);
		Gson gson = new Gson();
		
		LoginAuthJson laj = gson.fromJson(json, LoginAuthJson.class);
		String artifac = laj.getArtifact();
		System.out.println("artifacId-------------------------------------------------"+artifac);
		
		String welcome = "http://shop.10086.cn/i/v1/auth/getArtifact";
		WebRequest welcomeRequestSettings = new WebRequest(new URL(welcome), HttpMethod.GET); 
		welcomeRequestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1"); 
		welcomeRequestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		welcomeRequestSettings.getRequestParameters().add(new NameValuePair("backUrl", "http://shop.10086.cn/i/"));
		welcomeRequestSettings.getRequestParameters().add(new NameValuePair("artifact", artifac));
	 
		HtmlPage welcomepage = webClient.getPage(welcomeRequestSettings); 
		WebResponse wr = welcomepage.getWebResponse();
//		System.out.println("登录成功后页面：==>>"+wr.getContentAsString());
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("ajax  cookie==>  "+cookie.getName()+":  "+cookie.getValue());	
			if(cookie.getName().equals("jsessionid-echd-cpt-cmcc-jt")){
				System.out.println("jsessionid-echd-cpt-cmcc-jt--------"+cookie.toString());
				jsessionidEchdCptCmccJt = cookie.getValue();
			}	
		}
		 
		path = welcomepage.getUrl().toString();
		System.out.println("登陆成功后的url：  "+path);
		System.out.println("--------------------------login end-----------------------");

		
	}
	
	
	 
/*
	public static WebClient getWebClient() {
		if (webClient == null) {

			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.setRefreshHandler(new ThreadedRefreshHandler());
			webClient.getCookieManager().setCookiesEnabled(true);
			webClient.getOptions().setCssEnabled(true);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(10000);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		}
		return webClient;
	}
	*/
	public static String sendSMS(String phonenum) throws Exception{
		
		webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			URL smsAction = new URL("https://login.10086.cn/sendRandomCodeAction.action");
			WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("userName", phonenum));
			requestSettings.getRequestParameters().add(new NameValuePair("type", "01"));
			requestSettings.getRequestParameters().add(new NameValuePair("channelID", "12034"));
			
			requestSettings.setAdditionalHeader("Host", "login.10086.cn");
			requestSettings.setAdditionalHeader("Origin", "https://login.10086.cn");
			requestSettings.setAdditionalHeader("Referer", "https://login.10086.cn/login.html");
			
			Page page = webClient.getPage(requestSettings); 
			String html = page.getWebResponse().getContentAsString();
//			tracer.addTag("sendSMS 登录短信发送", html);
			System.out.println("sendSMS 登录短信发送:"+html);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static void getUserMessage() throws Exception{
		System.out.println("---------------------获取个人信息---------------------");
		String url = "http://shop.10086.cn/i/v1/cust/mergecust/15210072522?_="+System.currentTimeMillis();
		WebRequest request = new WebRequest(new URL(url), HttpMethod.GET); 
		
		request.setAdditionalHeader("Host", "shop.10086.cn");
		request.setAdditionalHeader("pragma", "no-cache");
		request.setAdditionalHeader("expires", "0");
		request.setAdditionalHeader("If-Modified-Since", "0");
		request.setAdditionalHeader("Referer", path);
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		UnexpectedPage page = webClient.getPage(request);
		System.out.println("个人信息  ====》》"+page.getWebResponse().getContentAsString());
	}
	
	public static void getShowVEC() throws Exception{
		String showwev = "http://shop.10086.cn/i/apps/serviceapps/billdetail/showvec.html"; 
		WebRequest request = new WebRequest(new URL(showwev), HttpMethod.GET); 
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		HtmlPage page = webClient.getPage(request);
		String html = page.getWebResponse().getContentAsString();
		System.out.println("ShowVEC----"+html);
		Set<Cookie> set = webClient.getCookieManager().getCookies();
		for(Cookie cookie:set){
				System.out.println(cookie);
		}
		
	}
	
	
	public static void secondSMSsend(String phonenum) throws Exception{
		
		System.out.println("-------------secondSMSsend-------------"+path);
		
		//String url = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/"+phonenum+"?callback=jQuery183008660428427514177_"+System.currentTimeMillis()+"&_="+System.currentTimeMillis();
		//WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST); 
		String url = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/"+phonenum;
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");

		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+System.currentTimeMillis());
		requestSettings.setAdditionalHeader("Accept", "*/*");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
	    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
	    requestSettings.setRequestParameters(new ArrayList<NameValuePair>());

		requestSettings.getRequestParameters().add(new NameValuePair("callback", "jQuery183008660428427514177_"+String.valueOf(System.currentTimeMillis())));
		requestSettings.getRequestParameters().add(new NameValuePair("_", String.valueOf(System.currentTimeMillis())));		
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Page page = webClient.getPage(requestSettings); 
		
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		
		for(Cookie cookie:cookies){
			System.out.println("二次发送短信随机的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("第二次发送随机短信  ====》》"+html);		
		
	}
	
	
	public static void download(String urlString, String filename) throws Exception {
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
	    Set<Cookie> set = webClient.getCookieManager().getCookies();
	    
	    System.out.println("---------------download 图片验证码下载-------------");
	    
	    Map<String,String> cookieMap = new HashMap<String,String>();
	    for(Cookie cookie:set){
			cookieMap.put(cookie.getName(), cookie.getValue());
		}
	    
	    Connection con = Jsoup.connect(urlString);
//	    con.header("Accept", "image/webp,image/*,*/*;q=0.8"); 
 //       con.header("Host", "shop.10086.cn");
 //       con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); 
 //       con.header("Referer", path);
	    
        Response response = con.cookies(cookieMap).execute();
        FileOutputStream out = (new FileOutputStream(new java.io.File(filename)));
        out.write(response.bodyAsBytes()); 
        
        out.close();
	}   
	
	public static void preCheckCaptcha() throws Exception{
		
		System.out.println("-------------------preCheckCaptcha 图片验证码认证-------------------");
		

        Scanner input = new Scanner(System.in);
        code = input.next();
        
		String url = "http://shop.10086.cn/i/v1/res/precheck/15210072522?captchaVal="+code+"&_="+System.currentTimeMillis();
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+System.currentTimeMillis());
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Cache-Control", "no-store, must-revalidate");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("expires", "0");
		requestSettings.setAdditionalHeader("If-Modified-Since", "0");
		requestSettings.setAdditionalHeader("pragma", "no-cache");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Page page = webClient.getPage(requestSettings);
		String html = page.getWebResponse().getContentAsString();
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		
		for(Cookie cookie:cookies){
			System.out.println("图片验证码认证的COOKIE ==>>  "+cookie.getName()+" :  "+cookie.getValue());
		}
		System.out.println("图片验证码认证  ====》》"+html);
		
		
	}
	
	
	public static void secondAttestation() throws Exception{
		
		System.out.println("---------------------secondAttestation  二次认证--------------------");
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Scanner input = new Scanner(System.in);
		String url = "https://shop.10086.cn/i/v1/fee/detailbilltempidentjsonp/15210072522?callback=jQuery183020907438300533676_1498788146403&pwdTempSerCode="+dealWithPassword("001314")+"&pwdTempRandCode="+dealWithPassword(input.next())+"&captchaVal="+code+"&_="+System.currentTimeMillis();
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Referer", path);
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		Page page = webClient.getPage(requestSettings);
		
		String html = page.getWebResponse().getContentAsString();
		
		System.out.println("二次认证  =========>>"+html);
		
	}
	
//	public static void getResultData() throws Exception{
//		
//		System.out.println("-----------------getResultData  获取通话记录------------------");
//	
//		String url = "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/15210072522?callback=jQuery183008660428427514177_"+System.currentTimeMillis()+"&curCuror=1&step=100&qryMonth=201706&billType=02&_="+System.currentTimeMillis();
//
//		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
//		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome=1499052356977");
//		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
//		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		requestSettings.setAdditionalHeader("Connection", "keep-alive");
//		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
//		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
////		WebClient webClient = WebCrawler.getInstance().getWebClient();
//		Page page = webClient.getPage(requestSettings);
//		String html = page.getWebResponse().getContentAsString();
//		
//		String json = html.substring(html.indexOf("(")+1, html.length()-1);
//		Gson gson = new Gson();
//		CallRecordBean callRecordBean = gson.fromJson(json, CallRecordBean.class);
//		
//		List<CmccUserCallResult> list = callRecordBean.getData();
//		for(CmccUserCallResult cmccUserCallResult:list){
//			System.out.println(cmccUserCallResult);
//			System.out.println("---------------------------------------------------");
//		}
//		
//	}
	
	public static void temporaryAttestation() throws Exception{
		
		String url = "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/15210072522?callback=jQuery183010523130956915194_"+System.currentTimeMillis()+"&curCuror=1&step=100&qryMonth=201706&billType=01&_="+System.currentTimeMillis();
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=billdetailqry&welcome=1498813721466");
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		Page page = webClient.getPage(requestSettings);
		String html = page.getWebResponse().getContentAsString();
		
		System.out.println("临时认证  ====>>"+html);
		
		
	}
	
	public static String dealWithPassword(String password){
		byte[] encodeBase64 = null;
		try {
			encodeBase64 = Base64.encodeBase64(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(encodeBase64); 
	}
	
	
	

}
