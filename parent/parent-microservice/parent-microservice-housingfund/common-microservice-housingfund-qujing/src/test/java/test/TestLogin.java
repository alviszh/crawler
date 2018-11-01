package test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.io.ByteStreams;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String imageUrl= "http://www.qjzfgjj.com/website/imageCreate.jsp?timestamp=1536808177425"; 
	    WebRequest webRequest = new WebRequest(new URL(imageUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Content-Type", "image/jpeg;charset=UTF-8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");	
		webRequest.setAdditionalHeader("Host", "www.qjzfgjj.com");	
		Page page = webClient.getPage(webRequest);
		WebResponse webResponse = page.getWebResponse();

		InputStream bodyStream = webResponse.getContentAsStream();
		byte[] responseContent = ByteStreams.toByteArray(bodyStream);
		bodyStream.close();
		File imageFile = getImageLocalPath();		
		// 创建输出流
		FileOutputStream outStream = new FileOutputStream(imageFile);
		// 写入数据
		outStream.write(responseContent);
		// 关闭输出流
		outStream.close();

//		String loginUrl= "http://www.qjzfgjj.com/website/grcxdl.html";
//		HtmlPage page2 =  getHtml(loginUrl,webClient);
//		webClient.getOptions().setJavaScriptEnabled(true);  
//		webClient.waitForBackgroundJavaScript(20000);
//		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@id='accnum']");
//		
//		HtmlTextInput certinum = (HtmlTextInput)page2.getFirstByXPath("//input[@id='certinum']");
//		//HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@id='pwd']");	
//		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@id='verifyCode']");		
//		HtmlElement button = (HtmlElement)page2.getFirstByXPath("//button[@id='submitBtn']");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
//		username.setText("113029023172");
//		
//		certinum.setText("53292519870621072X");
//		code.setText(input);
//		HtmlPage loggedPage = button.click();
//		System.out.println("===============");
//		   System.out.println(loggedPage.getWebResponse().getContentAsString());
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.asXml());
//	
//		
	    String loginUrl4 = "http://www.qjzfgjj.com/perLogin.json?accnum=113029023172&certinum=53292519870621072X&verifyCode="+input;		                     
		WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.GET);	
		//
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("accnum", "113029023172"));
		paramsList.add(new NameValuePair("certinum", "53292519870621072X"));
		paramsList.add(new NameValuePair("verify", input));
//		
		webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest4.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest4.setAdditionalHeader("Connection", "keep-alive");
		webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest4.setAdditionalHeader("Host", "www.qjzfgjj.com");
		webRequest4.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
		webRequest4.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/trans/queryPer_login.jsp?txt=1");
		webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		//webRequest4.setRequestParameters(paramsList);
		//webRequest4.setRequestBody("stxt=1&accnum=113029023172&certinum=53292519870621072X&verify="+input);
		Page page4 = webClient.getPage(webRequest4);
		System.out.println(page4.getWebResponse().getContentAsString());
		String loginUrl= "http://www.qjzfgjj.com/website/grcxdl.html";
		HtmlPage page2 =  getHtml(loginUrl,webClient);
		System.out.println(page2.getWebResponse().getContentAsString());
		//{"recode":"000000","msg":"成功","certinum":"53292519870621072X","accname":"范丽娟","code":"00"}
		 String url2 = "http://www.qjzfgjj.com/website/trans/query_list.jsp";		                     
//			WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);	
//			webRequest2.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			webRequest2.setAdditionalHeader("Connection", "keep-alive");
//			webRequest2.setAdditionalHeader("Host", "www.qjzfgjj.com");
//			webRequest2.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
//			webRequest2.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/website.do?className=TRA020001");
//			Page page22 = webClient.getPage(webRequest2);
		  HtmlPage page22 = getHtml(url2,webClient);
			System.out.println(page22.getWebResponse().getContentAsString());
			
			
//		 String url3 = "http://www.qjzfgjj.com/website/trans/gjjquery.do";		                     
//			WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.POST);	
//			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//			
//			paramsList.add(new NameValuePair("accnum", "113029023172"));
//			paramsList.add(new NameValuePair("certinum", "53292519870621072X"));
//			paramsList.add(new NameValuePair("className", "TRA020101"));
//			webRequest3.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest3.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//			webRequest3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		
//			webRequest3.setAdditionalHeader("Connection", "keep-alive");
//			webRequest3.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//			webRequest3.setAdditionalHeader("Host", "www.qjzfgjj.com");
//			webRequest3.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
//			webRequest3.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/trans/query_list.jsp");
//			webRequest3.setRequestBody("accnum=113029023172&ceridnum=53292519870621072X&className=TRA020101");
//			
//			Page page3 = webClient.getPage(webRequest3);
//			System.out.println(page3.getWebResponse().getContentAsString());
//		
//		
////		
//		 String url5 = "http://www.qjzfgjj.com/website/trans/gjjquery.do?className=TRA020102";		                     
//		WebRequest webRequest5 = new WebRequest(new URL(url5), HttpMethod.POST);	
//		List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
//		
//		paramsList2.add(new NameValuePair("accnum", "113029023172"));
//		paramsList2.add(new NameValuePair("certinum", "53292519870621072X"));
//		paramsList2.add(new NameValuePair("begdate", "2008-01-01"));
//		paramsList2.add(new NameValuePair("begdate", "2018-01-29"));
//		webRequest5.setAdditionalHeader("Connection", "keep-alive");
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest5.setAdditionalHeader("Host", "www.qjzfgjj.com");
//		webRequest5.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
//		webRequest5.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/trans/timeSelect.jsp?className=TRA020102");
//		
//		webRequest5.setRequestBody("accnum=113029023172&ceridnum=53292519870621072X&begdate=2008-01-01&enddate=2018-01-01");
//		Page page5 = webClient.getPage(webRequest5);
//		System.out.println(page5.getWebResponse().getContentAsString());
////		

	}
	public static String getPathBySystem() {

		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/snapshot/";
			return path;
		} else {
			String path = System.getProperty("user.home") + "/snapshot/";
			return path;
		}

	}
	public static File getImageLocalPath() {
		File parentDirFile = new File(getPathBySystem());
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(getPathBySystem() + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
