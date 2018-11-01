package Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler; 

public class LoginTest extends AbstractChaoJiYingHandler{

	public static void main(String[] args) throws Exception {
		//org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// 正常页面登陆 17718194181    119110
		String url = "http://ah.189.cn/sso/login";
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(requestSettings);
		//System.out.println(loginPage.asXml());
		Set<Cookie> cookies0 = webClient.getCookieManager().getCookies();
		for(Cookie cookie : cookies0){
			System.out.println("登录Page获取到的cookie是："+cookie.toString());
		} 
		
		HtmlImage image = (HtmlImage) loginPage.querySelector("#vImg");
		String imageName ="D:\\img\\"+ UUID.randomUUID() + ".jpg";
		System.out.println("imageName-------"+imageName);
		File file = new File(imageName);
		image.saveAs(file);  
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", imageName);  
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code---------"+code); 
		
		
		WebRequest  requestSettings2 = new WebRequest(new URL("http://ah.189.cn/sso/LoginServlet"), HttpMethod.POST);  
		requestSettings2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings2.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings2.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings2.setAdditionalHeader("Referer", "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.parser");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("latnId", "551"));  
		params.add(new NameValuePair("loginType", "4"));  
		params.add(new NameValuePair("passWord", "17b3497f726221657ae2414cb02fa9ffd8d8902b0be120c2824a168b3d4f319974012589dbed1766ac0bf5cd707238f5ad6159533f874e1e8c3c691dca143198"));  
		params.add(new NameValuePair("result", "true"));  
		params.add(new NameValuePair("accountType", "9"));  
		params.add(new NameValuePair("serviceNbr", "17718194181"));  
		params.add(new NameValuePair("validCode", code));  
		
		params.add(new NameValuePair("loginName", "17718194181"));  
		params.add(new NameValuePair("passType", "0"));  
		params.add(new NameValuePair("csrftoken", ""));  
		params.add(new NameValuePair("ssoAuth", "0"));  
		params.add(new NameValuePair("returnUrl", "%2Fservice%2Faccount%2Finit.parser"));
		params.add(new NameValuePair("sysId", "1003"));   
		
		requestSettings2.setRequestParameters(params);
		
		requestSettings2.setCharset(Charset.forName("UTF-8"));
		
		Page hPage2 = webClient.getPage(requestSettings2);
		
		System.out.println("hPage2---"+hPage2.getWebResponse().getContentAsString());
		
		
		Set<Cookie> cookies1 = webClient.getCookieManager().getCookies();
		for(Cookie cookie : cookies1){
			System.out.println("登录后获取到的cookie是："+cookie.toString());
		}
		
		WebRequest  requestSettings3 = new WebRequest(new URL("http://ah.189.cn/service/account/usedBalance.parser"), HttpMethod.POST);
		
		requestSettings3.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings3.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings3.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings3.setAdditionalHeader("Referer", "http://ah.189.cn/service/account/init.parser");
		requestSettings3.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		
		List<NameValuePair> params2 = new ArrayList<NameValuePair>();
		params2.add(new NameValuePair("serviceNum", "17718194181"));  
		
		
		requestSettings3.setCharset(Charset.forName("UTF-8"));
		
		Page hPage3 = webClient.getPage(requestSettings3);
		
		String userurl="http://ah.189.cn/service/manage/showCustInfo.parser";
		Page page = webClient.getPage(userurl);
		System.out.println("hPage3---"+hPage3.getWebResponse().getContentAsString());
		System.out.println(page.getWebResponse().getContentAsString());
		
	}
	
	
	
	public String encryptedPassword(String phonenum) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecom.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",phonenum);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
	        return Resources.toString(Resources.getResource(fileName), charset);
	}

}
