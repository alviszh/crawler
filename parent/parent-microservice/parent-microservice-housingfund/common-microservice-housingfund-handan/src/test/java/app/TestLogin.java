package app;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	private static String _csrf="";
	private static String code="";
	static WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	
	public static void main(String[] args) throws Exception {
		String url = "http://www.hdgjj.cn/olbh/index";
		
		login(url);
		
		loginByPost();
		HtmlPage mingxiPage = (HtmlPage) getHtml("http://www.hdgjj.cn/olbh/pub/home",webClient);
		System.out.println("home===================================================================");
		System.out.println(mingxiPage.asXml());
		
		try {
//			getUserInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static void getUserInfo() throws Exception {
		URL smsAction = new URL("http://www.hdgjj.cn/olbh/qryPersonInfo.do?_csrf"+_csrf);
		WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("_csrf", _csrf));
		
		requestSettings.setAdditionalHeader("Host", "www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Origin", "http://www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Referer", "http://www.hdgjj.cn/olbh/pub/home");
		
		Page page = webClient.getPage(requestSettings); 
		System.out.println("用户信息=====================================================================");
		System.out.println(page.getWebResponse().getContentAsString());
		
		System.out.println(_csrf);
		System.out.println(code);
	}

	private static void loginByPost() {
		try {
			for(int i = 0;i<4;i++){
				URL smsAction = new URL("http://www.hdgjj.cn/olbh/auth");
				WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
				requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
				requestSettings.getRequestParameters().add(new NameValuePair("_csrf", _csrf));
				requestSettings.getRequestParameters().add(new NameValuePair("bizno", ""));
				requestSettings.getRequestParameters().add(new NameValuePair("loginType", "person"));
				requestSettings.getRequestParameters().add(new NameValuePair("pername", ""));
				requestSettings.getRequestParameters().add(new NameValuePair("idNumber_face", ""));
				requestSettings.getRequestParameters().add(new NameValuePair("idNumber", "130434199106167526"));
				requestSettings.getRequestParameters().add(new NameValuePair("password	", "123456"));
				requestSettings.getRequestParameters().add(new NameValuePair("verifyCode", code));
				
				requestSettings.setAdditionalHeader("Host", "www.hdgjj.cn");
				requestSettings.setAdditionalHeader("Origin", "http://www.hdgjj.cn");
				requestSettings.setAdditionalHeader("Referer", "http://www.hdgjj.cn/olbh/index");
				
				Page page = webClient.getPage(requestSettings); 
				if(200 == page.getWebResponse().getStatusCode()){
					String html = page.getWebResponse().getContentAsString();
//			tracer.addTag("sendSMS 登录短信发送", html);
					System.out.println("post登录:"+html);
					break;					
				}else{
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void login(String url) {
		//*[@id="tabs-1"]/form/div[4]/div/button
		try {
			outer:for(int i=0;i<5;i++){
				HtmlPage searchPage = (HtmlPage) getHtml(url,webClient);
				if(200 == searchPage.getWebResponse().getStatusCode()){
					System.out.println("登录页----------------------------------------------");
					System.out.println(searchPage.asXml());
					System.out.println("--------------------------------------------------");
					
					HtmlHiddenInput input = (HtmlHiddenInput)searchPage.getFirstByXPath("//input[@name='_csrf']");
					_csrf = input.getAttribute("value");
					System.out.println(_csrf);
					//radio选择
//					HtmlRadioButtonInput radio = searchPage.getFirstByXPath("//input[@id='perRadio']");
//					System.out.println("radio选择框  ： "+radio.asXml());
//					radio.click();		
//					//身份证号输入框
//					HtmlTextInput username = (HtmlTextInput)searchPage.getFirstByXPath("//input[@id='idNumber']");
//					//密码输入框
//					HtmlPasswordInput password = (HtmlPasswordInput)searchPage.getFirstByXPath("//input[@id='password']");
//					//图片验证码输入框
//					HtmlTextInput vericode = (HtmlTextInput)searchPage.getFirstByXPath("//input[@id='verifyCode']");
//					//登录按钮点击
//					HtmlButton button = (HtmlButton)searchPage.getFirstByXPath("//button[@class='btn btn-default btn_login']");
//					
//					System.out.println("登录按钮  ： "+button.asXml());
//					System.out.println("身份证号输入框  ： "+username.asXml());
//					System.out.println("密码输入框  ： "+password.asXml());
//					System.out.println("图片验证码输入框  ： "+vericode.asXml());
					
					for(int y = 0;y<4;y++){
						//图片验证码
						HtmlImage img = searchPage.getFirstByXPath("//img[@id='person_pic']");
						System.out.println("图片验证码  ： "+img.asXml());
						String imageName = UUID.randomUUID() + ".jpg";
						File file = new File("D:\\img\\"+imageName);
						img.saveAs(file);
						
						System.out.println("path---------------"+file.getAbsolutePath()); 
						String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, file.getAbsolutePath()); 
						System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
						Gson gson = new GsonBuilder().create();
						code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
						System.out.println("code ====>>"+code); 
						if(StringUtils.isBlank(code)){
							continue;
						}
//						username.setText("130434199106167526");
//						password.setText("123456");
////			username.setText("111111111111111111");
////			password.setText("000000");
//						vericode.setText(code);
//						
//						HtmlPage loggedPage = button.click();
//						webClient = loggedPage.getWebClient();
//						System.out.println("点击登录后返回的页面 ----------------"+loggedPage.asXml());
						break outer;
						
					}
					
					
				}else{
					
					continue;
					
				}
				
			}
			
			
//			String mingxiUrl = "http://49.4.159.140:7001/wsyyt/init.summer?_PROCID=60020007";
//			HtmlPage mingxiPage = (HtmlPage) getHtml(mingxiUrl,loggedPage.getWebClient());
//			System.out.println("用户信息   ------------------------"+mingxiPage.asXml());
//			
//			HtmlButton mingxiButton = (HtmlButton) mingxiPage.getElementById("b_query3");
//			
//			HtmlPage liushuiPage = mingxiButton.click();
//			System.out.println("流水明细-------------------------"+liushuiPage.asXml());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public static Page getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		Thread.sleep(10000);
		return searchPage;
		
	}

}
