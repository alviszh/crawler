package Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import app.service.common.HousingBasicService;
import net.sf.json.JSONObject;

public class TestLogin2 {

//	@Autowired
//	private static ChaoJiYingOcrService chaoJiYingOcrService;
//	@Autowired
//	private static HousingBasicService housingBasicService;
	
	public static void main(String[] args) throws Exception {
		
		HousingBasicService h = new HousingBasicService();
		ChaoJiYingOcrService c= new ChaoJiYingOcrService();
		String url="http://www.zmdgjj.com:8090/wt-web/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String img="http://www.zmdgjj.com:8090/wt-web/captcha?"+Math.random();
//        http://www.zmdgjj.com:8090/wt-web/captcha?0.7446124405341923
//                                                  0.2340779123462574
		Page page6 = webClient.getPage(img);
		String imagePath = h.getImagePath(page6, "D:\\img\\");
		
		String code = c.callChaoJiYingService(imagePath, "1902"); 


		
		HtmlPage page = webClient.getPage(url);	
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		
		Element e1 = doc.getElementById("modulus");
		String val1 = e1.val();
		
		Element e2 = doc.getElementById("exponent");
		String val2 = e2.val();
		System.out.println(val1+"--"+val2);
		
		
	  	double random = Math.random();
	  	
	  	
		
		String params="111111";
		String str = encryptedPhone(params);
		System.out.println(str);
		
		
		String urllogin="http://www.zmdgjj.com:8090/wt-web/login";
		WebRequest webRequest = new WebRequest(new URL(urllogin), HttpMethod.POST);
		String a  ="username=412821198712040212&password="+str+"&captcha="+code+"&logintype=1";
		webRequest.setRequestBody(a);
	    Page page2 = webClient.getPage(webRequest);
	    System.out.println(page2.getWebResponse().getContentAsString());
	    
	   
		
		
//		String homeurl="http://www.zmdgjj.com/wt-web/home";
//		WebRequest  requestSettings2home = new WebRequest(new URL(homeurl), HttpMethod.GET);
//	  	requestSettings2home.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//	  	requestSettings2home.setAdditionalHeader("Connection", "keep-alive");
//	  	requestSettings2home.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
//	  	requestSettings2home.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//	  	requestSettings2home.setAdditionalHeader("Host", "www.zmdgjj.com");
//	  	requestSettings2home.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
//	  	requestSettings2home.setAdditionalHeader("Referer", "http://www.zmdgjj.com/wt-web/logout");
//	    Page pagehome = webClient.getPage(homeurl);
//	    System.out.println(pagehome.getWebResponse().getContentAsString());
//	    Set<Cookie> cookiesh = webClient.getCookieManager().getCookies();
//		for (Cookie cookie : cookiesh) {
//			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
//		}
		
		    String perurl="http://www.zmdgjj.com/wt-web/pages/personal/person_information_query.html";
		    Page page22 = webClient.getPage(perurl);
		    System.out.println(page22.getWebResponse().getContentAsString());
		    Set<Cookie> cookies2 = webClient.getCookieManager().getCookies();
			for (Cookie cookie : cookies2) {
				System.out.println("登录Page获取到的cookie是：" + cookie.toString());
			}
		
		
			String url3="http://www.zmdgjj.com/wt-web/pages/personal/person_information_query.js?_="+System.currentTimeMillis();
			WebRequest  requestSettings3 = new WebRequest(new URL(url3), HttpMethod.GET);
			requestSettings3.setCharset(Charset.forName("GBK"));
			requestSettings3.setAdditionalHeader("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
			requestSettings3.setAdditionalHeader("Connection", "keep-alive");
			requestSettings3.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=GBK");
			requestSettings3.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			requestSettings3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings3.setAdditionalHeader("Host", "www.zmdgjj.com");
			requestSettings3.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			requestSettings3.setAdditionalHeader("Referer", "http://www.zmdgjj.com/wt-web/home");
			requestSettings3.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			Page page3 = webClient.getPage(requestSettings3);
			System.out.println(page3.getWebResponse().getContentAsString());
			Set<Cookie> cookies3 = webClient.getCookieManager().getCookies();
			for (Cookie cookie : cookies3) {
				System.out.println("登录Page获取到的cookie是：" + cookie.toString());
			}
			
			String urlhx="http://www.zmdgjj.com/wt-web/person/jcqqxx?_=1516340782778";
			WebRequest  requestSettings33 = new WebRequest(new URL(url3), HttpMethod.GET);
			requestSettings33.setAdditionalHeader("Accept", "*/*");
			requestSettings33.setAdditionalHeader("Connection", "keep-alive");
			requestSettings33.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			requestSettings33.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings33.setAdditionalHeader("Host", "www.zmdgjj.com");
			requestSettings33.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			requestSettings33.setAdditionalHeader("Referer", "http://www.zmdgjj.com/wt-web/home");
			requestSettings33.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			Page page33 = webClient.getPage(requestSettings33);
			System.out.println(page33.getWebResponse().getContentAsString());
			
		String url4="http://www.zmdgjj.com/wt-web/personal/jcmxlist?UserId=1&beginDate=2017-11-01&endDate=2018-01-19&userId=1&pageNum=1&pageSize=10&totalcount=7&pages=1&random="+random;
		WebRequest  requestSettings4 = new WebRequest(new URL(url4), HttpMethod.POST);
	  	requestSettings4.setAdditionalHeader("Accept", "*/*");
	  	requestSettings4.setCharset(Charset.forName("UTF-8"));
	  	requestSettings4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	  	requestSettings4.setAdditionalHeader("Connection", "keep-alive");
	  	requestSettings4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	  	requestSettings4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	  	requestSettings4.setAdditionalHeader("Host", "www.zmdgjj.com");
	  	requestSettings4.setAdditionalHeader("Origin", "http://www.zmdgjj.com");
	  	requestSettings4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	  	requestSettings4.setAdditionalHeader("Referer", "http://www.zmdgjj.com/wt-web/home");
	  	Page page4 = webClient.getPage(requestSettings4);
		System.out.println(page4.getWebResponse().getContentAsString());
		Set<Cookie> cookies1 = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies1) {
			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
		}
		
		String url5="http://www.zmdgjj.com/wt-web/person/jbxx?random"+random;
		WebRequest  requestSettings2 = new WebRequest(new URL(url5), HttpMethod.GET);
	  	requestSettings2.setAdditionalHeader("Accept", "image/webp,image/*,*/*;q=0.8");
	  	requestSettings2.setAdditionalHeader("Connection", "keep-alive");
	  	requestSettings2.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
	  	requestSettings2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	  	requestSettings2.setAdditionalHeader("Host", "www.zmdgjj.com");
	  	requestSettings2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	  	requestSettings2.setAdditionalHeader("Referer", "http://www.zmdgjj.com/wt-web/home");
		Page page5 = webClient.getPage(requestSettings2);	
		System.out.println(page5.getWebResponse().getContentAsString());
		Set<Cookie> cookies31 = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies31) {
			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
		}
		
		
//		TestLogin t = new TestLogin();
//		String params="111111";
//		String str = t.encryptedPhone(params);
//		System.out.println(str);
	}

	public static String encryptedPhone(String phonenum) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("zhumadian.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",phonenum);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }

}
