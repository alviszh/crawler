package qq;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class qq {
	static String driverPath = "F:\\IEDriverServer_Win32\\chromedriver.exe";
	static Boolean headless = false;
	static String skey = "";
	public static void main(String[] args) throws Exception {
		WebDriver driver = intiChrome();
		String url = "https://xui.ptlogin2.qq.com/cgi-bin/xlogin?"
				+ "proxy_url=https%3A//qzs.qq.com/qzone/v6/portal/proxy.html"
				+ "&daid=5"
				+ "&&hide_title_bar=1"
				+ "&low_login=0"
				+ "&qlogin_auto_login=1"
				+ "&no_verifyimg=1"
				+ "&link_target=blank"
				+ "&appid=549000912"
				+ "&style=22"
				+ "&target=self"
				+ "&s_url=https%3A%2F%2Fqzs.qzone.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara%3Dizone"
				+ "&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4"
				+ "&pt_qr_link=http%3A//z.qzone.com/download.html"
				+ "&self_regurl=https%3A//qzs.qq.com/qzone/v6/reg/index.html"
				+ "&pt_qr_help_link=http%3A//z.qzone.com/download.html&pt_no_auth=0";
		driver.get(url);
		Thread.sleep(5000);
		driver.findElement(By.id("switcher_plogin")).click();
		Thread.sleep(5000);
		String qqnum = "382684542";
		String pass = "QH3344521";
		driver.findElement(By.id("u")).sendKeys(qqnum);
		driver.findElement(By.id("p")).sendKeys(pass);
		WebElement element = driver.findElement(By.id("login_button"));
		element.click();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Thread.sleep(2000);
		String ur = "https://user.qzone.qq.com/"+qqnum;
		driver.get(ur);
		
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		String transcookieToJson2 = transcookieToJson2(cookies);
		Set<Cookie> set = transferJsonToSet(transcookieToJson2);
		for (Cookie cookie : set) {
			if(cookie.getName().equals("p_skey")){
				skey = cookie.getValue();
			}
		}
		System.out.println(skey);		
		Iterator<Cookie> j = set.iterator();
		while(j.hasNext()){
			webClient.getCookieManager().addCookie(j.next());
		}
		String g_tk = encryptedPhone("skey.js", "getskey", skey, "");
		
		
		String qunurl = "https://user.qzone.qq.com/proxy/domain/r.qzone.qq.com/cgi-bin/tfriend/qqgroupfriend_extend.cgi?"
				+ "uin="+qqnum
				+ "&g_tk="+g_tk;
		driver.get(qunurl);
		String htmls = driver.getPageSource();
		String[] split = htmls.split("_Callback");
		String[] split2 = split[1].split("</body></html>");
		split2[0] = split2[0].trim();
		String html = split[1].substring(1, split2[0].length()-2);
		JSONObject fromObject = JSONObject.fromObject(html.trim());
		JSONArray jsonArray = fromObject.getJSONObject("data").getJSONArray("group");
		for(int i=0;i<jsonArray.size();i++){
			String groupcode = jsonArray.getJSONObject(i).getString("groupcode");//群号
			String groupname = jsonArray.getJSONObject(i).getString("groupname");//群名
			String total_member = jsonArray.getJSONObject(i).getString("total_member");//群人数
			String notfriends = jsonArray.getJSONObject(i).getString("notfriends");//不是好友的数量
			System.out.println("\b\r群号："+groupcode+"\b\r群名："+groupname+"\b\r群人数："+total_member);
		}
		
		
		String findurl = "https://user.qzone.qq.com/proxy/domain/r.qzone.qq.com/cgi-bin/tfriend/friend_mngfrd_get.cgi?"
				+ "uin="+qqnum
				+ "&g_tk="+g_tk;
		driver.get(findurl);
		String html2 = driver.getPageSource();
		System.out.println(html2);
		String[] split3 = html2.split("_Callback");
		String[] split4 = split3[1].split("</pre></body></html>");
		split4[0] = split4[0].trim();
		String html3 = split3[1].substring(1, split4[0].length()-2);
		JSONObject fromObject2 = JSONObject.fromObject(html3.trim());
		JSONArray jsonArray2 = fromObject2.getJSONObject("data").getJSONArray("items_list");
		for(int k=0;k<jsonArray2.size();k++){
			String uin=jsonArray2.getJSONObject(k).getString("uin");//qq号
			String name=jsonArray2.getJSONObject(k).getString("name");//备注名称
			String score=jsonArray2.getJSONObject(k).getString("score");//空间好感度
			String img=jsonArray2.getJSONObject(k).getString("img");//qq头像
			System.out.println("\b\rqq号码"+uin+"\b\r备注名称："+name+"\b\r空间好感度："+score);
		}
//		String url01 = "https://qzone.qq.com/";
//		getHtml(url01, webClient);
//		
//		String url02 = "https://xui.ptlogin2.qq.com/cgi-bin/xlogin?"
//				+ "proxy_url=https%3A//qzs.qq.com/qzone/v6/portal/proxy.html&daid=5"
//				+ "&&hide_title_bar=1"
//				+ "&low_login=0"
//				+ "&qlogin_auto_login=1"
//				+ "&no_verifyimg=1"
//				+ "&link_target=blank"
//				+ "&appid=549000912"
//				+ "&style=22"
//				+ "&target=self"
//				+ "&s_url=https%3A%2F%2Fqzs.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara%3Dizone&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4"
//				+ "&pt_qr_link=https%3A//z.qzone.com/download.html"
//				+ "&self_regurl=https%3A//qzs.qq.com/qzone/v6/reg/index.html"
//				+ "&pt_qr_help_link=https%3A//z.qzone.com/download.html&pt_no_auth=0";
//		getHtml(url02, webClient);
//		
//		String url2 = "https://qzs.qq.com/qzone/v5/loginsucc.html?para=izone";
//		getHtml(url2, webClient);
		
		
		
		
		
		
	/*	driver.findElement(By.id("aMyFriends")).click();
		double random = Math.random();
		System.out.println(random);
		
		String findurl = "https://user.qzone.qq.com/proxy/domain/r.qzone.qq.com/cgi-bin/tfriend/qqgroupfriend_extend.cgi?uin=382684542&g_tk=180552989";
		driver.get(findurl);*/
		
//		if(page2.asText().contains("你输入的帐号或密码不正确，请重新输入。")){
//			System.out.println("你输入的帐号或密码不正确，请重新输入。");
//		}
//		String urlq = "https://user.qzone.qq.com/382684542";
//		HtmlPage html3 = getHtml(urlq, webClient);
//		String contentAsString3 = html3.getWebResponse().getContentAsString();
//		System.out.println(contentAsString3);
//		
//		System.out.println(html);
//		
//		String url2 = "https://user.qzone.qq.com/proxy/domain/r.qzone.qq.com/cgi-bin/tfriend/qqgroupfriend_extend.cgi?"
//				+ "uin=382684542"
//				+ "&rd=0.38907710608508306"
//				+ "&cntperpage=0"
//				+ "&fupdate=1"
//				+ "&g_tk=26559016"
//				+ "&qzonetoken=c0931c503244517359a649120f063f3ad9ac061653ebbb82f0c7cf47ee05bcc911fe12c10a6e80c8467d"
//				+ "&g_tk=26559016";
//		HtmlPage html2 = getHtml(url2, webClient);
//		String contentAsString = html2.getWebResponse().getContentAsString();
//		System.out.println(contentAsString);
	}
	

	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static String transcookieToJson2(Set<org.openqa.selenium.Cookie> cookies){
		//Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 

		Set<CookieJson> cookiesSet= new HashSet<>();
		
		for(org.openqa.selenium.Cookie cookie:cookies){ 
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson); 
		} 

		String cookieJson = new Gson().toJson(cookiesSet);
		return cookieJson;

	}
	public static Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

	}
	public static String encryptedPhone(String jsname,String Navi,String writid, String runEval) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource(jsname, Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction(Navi,writid,runEval);
		return data.toString(); 
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
}
