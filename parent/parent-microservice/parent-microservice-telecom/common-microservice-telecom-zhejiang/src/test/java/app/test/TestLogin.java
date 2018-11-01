package app.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.unit.TeleComCommonUnit;

public class TestLogin extends AbstractChaoJiYingHandler {
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final String OCR_FILE_PATH = "/home/img";
	private static String uuid = UUID.randomUUID().toString();
	static Gson gson = new GsonBuilder().create();
	
	public static void main(String[] args) throws Exception {
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/web/login";
		HtmlPage htmlpage = (HtmlPage) getHtml(url, webClient);
		
		HtmlTextInput username = (HtmlTextInput) htmlpage.getFirstByXPath("//input[@id='txtAccount']");
		HtmlElement htmlElement = (HtmlTextInput)htmlpage.getFirstByXPath("//input[@id='txtShowPwd']");
		htmlElement.click();
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) htmlpage.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) htmlpage.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("17342000534");
		passwordInput.setText("001314");
		htmlpage = button.click();
		
		if (htmlpage.asXml().indexOf("登录失败") != -1) {
			HtmlImage valiCodeImg = htmlpage.getFirstByXPath("//img[@id='imgCaptcha']");
			File codeImageFile = getImageLocalPath();
			
			valiCodeImg.saveAs(codeImageFile);
			
			String chaoJiYingResult = getVerifycodeByChaoJiYing("5000", LEN_MIN, TIME_ADD, STR_DEBUG, codeImageFile.getAbsolutePath());
			System.out.println(chaoJiYingResult);
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code : "+code);
			
			HtmlTextInput valicodeStrinput = (HtmlTextInput) htmlpage.getFirstByXPath("//*[@id='txtCaptcha']");
			
			username.setText("17342000534");
			passwordInput.setText("001314");
			valicodeStrinput.setText(code);
			htmlpage = button.click();			
		}
		
		String adminUrl = "http://www.189.cn/zj/";
		HtmlPage adminPage = (HtmlPage) TeleComCommonUnit.getHtml(adminUrl, webClient);
//		tracer.addTag("crawler.telecom.zhejiang.intermediate.page", "<xmp>"+adminPage.getWebResponse().getContentAsString()+"</xmp>");
		
		HtmlElement button1 = adminPage.getFirstByXPath("//a[@href='https://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10012&toStUrl=https://zj.189.cn/service/queryorder/']");

		HtmlPage clickPage = button1.click();	
		
		url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		htmlpage = (HtmlPage) getHtml(url,webClient);
		writer(htmlpage.asXml(),"C:/home/logined.txt");

		String url2 = "http://zj.189.cn/zjpr/service/query/query_order.html?menuFlag=1";
		Page url2Page = getHtml(url2, webClient);
		writer(url2Page.getWebResponse().getContentAsString(),"C:/home/url2Page.txt");
		
		String realUrl = "http://zj.189.cn/zjpr/user/Userinfodef/groupRealNameVerify.htm";
		Page realPage = TeleComCommonUnit.getHtml(realUrl, webClient);
		writer(realPage.getWebResponse().getContentAsString(),"C:/home/realUrl.txt");
		
		String serviceUrl = "http://zj.189.cn/bfapp/buffalo/cdrService";
		String servicepayload = "<buffalo-call><method>querycdrasset</method></buffalo-call>";
		Page serviceHtml = getHtmlPOST(serviceUrl,null,servicepayload,webClient);
		System.out.println("++++++++++++++++++++++++++++++++++++++所需参数需要访问的页面");
		System.out.println(serviceHtml.getWebResponse().getContentAsString());
		
		//发短信
//		String sendUrl = "http://zj.189.cn/bfapp/buffalo/VCodeOperation";
//		String formload = "<buffalo-call><method>SendVCodeByNbr</method><string>"+"17342000534"+"</string></buffalo-call>";
//		Page smsHtml =  getHtmlPOST(sendUrl,null,formload,webClient);
//		System.out.println("==============================================");
//		System.out.println(smsHtml.getWebResponse().getContentAsString());
//		
//		@SuppressWarnings("resource")
//		Scanner input = new Scanner(System.in);
//		String code = input.next();
//		
//		String callUrl = "http://zj.189.cn/zjpr/cdr/getCdrDetail.htm?flag=1&cdrCondition.pagenum=1&cdrCondition.pagesize=500&cdrCondition.productnbr="+"17342000534"+""
//				+ "&cdrCondition.areaid=571&cdrCondition.cdrlevel=&cdrCondition.productid="+"1-I8NHL026487"+"&cdrCondition.product_servtype="+"18"+"&cdrCondition.recievenbr=%D2%C6%B6%AF%B5%E7%BB%B0&cdrCondition.cdrmonth="+"201808"+
//				"&cdrCondition.cdrtype=11&cdrCondition.usernameyanzheng="+URLEncoder.encode("张振", "gb2312")
//				+"&cdrCondition.idyanzheng="+"130406199110233017"+"&cdrCondition.randpsw="+code;
//		
//		Page callPage = TeleComCommonUnit.getHtml(callUrl, webClient);
//		
//		System.out.println("===================================================详单");
//		System.out.println(callPage.getWebResponse().getContentAsString());
		
	
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
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
	
	
	public static Page getHtmlPOST(String url,Map<String,String> map,String payload, WebClient webClient) throws Exception{
		
		URL gsurl = new URL(url);
		WebRequest request = new WebRequest(gsurl, HttpMethod.POST);
		
		request.setAdditionalHeader("Host", "zj.189.cn");
		request.setAdditionalHeader("Referer", "http://zj.189.cn/zjpr/service/query/query_order.html?menuFlag=1");
//		request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
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
			
		Page page = webClient.getPage(request);
		int code = page.getWebResponse().getStatusCode();
		if(code == 200){
			String html = page.getWebResponse().getContentAsString();		
		}
		return page;
	}
	
	
	
	public static void writer(String page, String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 byte bt[] = new byte[1024];  
	        bt = page.getBytes();  
	        try {  
	            FileOutputStream in = new FileOutputStream(file);  
	            try {  
	                in.write(bt, 0, bt.length);  
	                in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	}
	

}
