package app;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final String OCR_FILE_PATH = "/home/img";
	private static String uuid = UUID.randomUUID().toString();
	static Gson gson = new GsonBuilder().create();
	static WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	
	public static void main(String[] args) {
		String url = "http://119.6.84.89:7001/scwssb/login.jsp";
		
		getHtml(url);
	}

	private static void getHtml(String url) {
		webClient.getOptions().setJavaScriptEnabled(false);
		try {
			HtmlPage searchPage = (HtmlPage) getHtmlPage(url);
			System.out.println("登录页面   :================="+searchPage.asXml());
			
			HtmlTextInput username = searchPage.getFirstByXPath("//input[@style='width:190px;height:24px;line-height: 24px;font-size: 14px;border: 1px solid #203246;']");
			System.out.println("用户名输入框："+username.asXml());
			
			String usernameKey = username.getAttribute("id");
			
			HtmlPasswordInput password = searchPage.getFirstByXPath("//input[@type='password']");
			System.out.println("密码输入框： "+password.asXml());
			
			String passwordKey = password.getAttribute("id");
			
//			HtmlTextInput checkCode = searchPage.getFirstByXPath("//input[@id='checkCode']");
//			System.out.println("验证码输入框： "+checkCode.asXml());
			
			HtmlImage image = searchPage.getFirstByXPath("//img[@id='codeimg']");
			File codeImageFile = getImageLocalPath();
			
			image.saveAs(codeImageFile);
			
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG, codeImageFile.getAbsolutePath());
			System.out.println("超级鹰识别之后的结果： "+chaoJiYingResult);
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code : "+code);
			
//			HtmlAnchor button = searchPage.getFirstByXPath("//a[@class='STYLE1']");
//			System.out.println("登录按钮 ： "+button.asXml());
//			
//			username.setText("Qinxu");
//			password.setText("q12345678");
//			checkCode.setText(code);
//			HtmlPage loginPage = button.click();
//			System.out.println("点击登录后的 页面 ： "+loginPage.asXml());
			
			Set<Cookie> cookies = webClient.getCookieManager().getCookies();
			for(Cookie cookie : cookies){
				System.out.println("Cookie   key = "+cookie.getName()+"    value = "+cookie.getValue());
			}
			
			getHtmlByParam(usernameKey,passwordKey,code);
			
			String welcomeUrl = "http://119.6.84.89:7001/scwssb/welcome2.jsp";
			Page welcomePage = getHtmlPage(welcomeUrl);
			System.out.println("欢迎页面："+welcomePage.getWebResponse().getContentAsString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void getHtmlByParam(String usernameKey, String passwordKey, String code) {
		try {
			URL smsAction = new URL("http://119.6.84.89:7001/scwssb/userYzAction!check.do");
			WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair(usernameKey, "Qinxu"));
			requestSettings.getRequestParameters().add(new NameValuePair(passwordKey, "q12345678"));
			requestSettings.getRequestParameters().add(new NameValuePair("orgId", "undefined"));
			requestSettings.getRequestParameters().add(new NameValuePair("checkCode", code));
			requestSettings.getRequestParameters().add(new NameValuePair("bz", "0"));
			requestSettings.getRequestParameters().add(new NameValuePair("tm", String.valueOf(System.currentTimeMillis())));
			
			requestSettings.setAdditionalHeader("Host", "119.6.84.89:7001");
			requestSettings.setAdditionalHeader("Origin", "http://119.6.84.89:7001");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			
			Page page = webClient.getPage(requestSettings); 
			
			Set<Cookie> cookies = webClient.getCookieManager().getCookies();
			for(Cookie cookie : cookies){
				System.out.println("Cookie   key = "+cookie.getName()+"    value = "+cookie.getValue());
			}
//			String html = page.getWebResponse().getContentAsString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Page getHtmlPage(String url) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "119.6.84.89:7001");
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	
	public static File getImageLocalPath(){
		
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		
		String imageName = uuid + ".jpg";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
				
		return codeImageFile;
		
	}

}
