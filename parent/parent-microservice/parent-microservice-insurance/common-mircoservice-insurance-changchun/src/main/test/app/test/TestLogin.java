package app.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.common.WebParam;

public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) {
		String loginUrl = "http://www.ccshbx.org.cn/member/login.jhtml";
		try {
			login(loginUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void login(String loginUrl) throws Exception, IOException {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(30000);
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);		
		HtmlPage page = webClient.getPage(webRequest);
		
		int status = page.getWebResponse().getStatusCode();
		
		if(status == 200){
			HtmlImage image = page.getFirstByXPath("//img[@id='captcaImg']");
			
			String imageName = UUID.randomUUID() + ".jpg";
			File file = new File("D:\\img\\"+imageName);
			image.saveAs(file);
			
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, file.getAbsolutePath()); 
			System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
			System.out.println("code ====>>"+code);
			
			
			HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
			HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
			HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='checkCode']");
			
			username.setText("220102198908196169");
			password.setText("196169");
			verifyCode.setText(code);
			HtmlElement button = (HtmlElement)page.getFirstByXPath("//a[@id='login_btn']");
			
			
			HtmlPage loadPage = button.click();
			System.out.println("登陆后的页面   ==》》"+loadPage.getWebResponse().getContentAsString());
			
			webParam.setCode(loadPage.getWebResponse().getStatusCode());
			webParam.setPage(loadPage);
			
			String url2 = "http://www.ccshbx.org.cn/getData.jspx";
			WebRequest requestSettings = new WebRequest(new URL(url2), HttpMethod.POST);
			Page page2 = webClient.getPage(requestSettings);
			System.out.println(page2.getWebResponse().getContentAsString());
		}
	}

}
