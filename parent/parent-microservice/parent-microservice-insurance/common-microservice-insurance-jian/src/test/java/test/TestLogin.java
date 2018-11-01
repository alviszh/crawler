package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.io.ByteStreams;
import com.module.htmlunit.WebCrawler;

public class TestLogin {
	
	private static final String OCR_FILE_PATH = "D:/img";	
	public static void main(String[] args) throws Exception {
	
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	
		String imageUrl= "http://www.gdzs.lss.gov.cn:8030/validateCodeServlet3?timpstamp=1523173585296";         
		WebRequest webRequest = new WebRequest(new URL(imageUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Content-Type", "image/png");
		webRequest.setAdditionalHeader("Connection", "keep-alive");	
		webRequest.setAdditionalHeader("Host", "www.gdzs.lss.gov.cn:8030");	
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

//		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@name='sfzh']");
//		HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@name='password']");	
//		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@name='yzm']");		
//		HtmlImage button = (HtmlImage)page2.getFirstByXPath("//img[@name='yes']");
		
//		System.out.println("username   :"+username.asXml());
//		System.out.println("password   :"+password.asXml());	
//		System.out.println("button   :"+button.asXml());
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
//		code.setText(input);
//		username.setText("371121199109191211");
//		password.setText("053228");
//				
//		HtmlPage loggedPage = (HtmlPage) button.click();
//		Thread.sleep(1500);
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
		 String loginUrl4 = "http://www.gdzs.lss.gov.cn:8030/ajax/login.action";
		         
		WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
		String requestBody="cardid=44200019920517208X&taxid=&sbkid=249317230&password=317230&code="+input+"&type=1";
		webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest4.setAdditionalHeader("Connection", "keep-alive");
		webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest4.setAdditionalHeader("Host", "www.gdzs.lss.gov.cn:8030");
		webRequest4.setAdditionalHeader("Origin", "ttp://www.gdzs.lss.gov.cn:8030");
		webRequest4.setAdditionalHeader("Referer", "http://www.gdzs.lss.gov.cn:8030/main/");
		webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest4.setRequestBody(requestBody);
		Page page4 = webClient.getPage(webRequest4);
		System.out.println(page4.getWebResponse().getContentAsString());
		//{"ret":1,"logintime":"16:02:45","name":"凌小燕","prompt":"登录成功！","type":1}
	
		HtmlPage page5=getHtml("http://www.gdzs.lss.gov.cn:8030/myprofile/index.action?appcode=A1_01_01",webClient);
		System.out.println(page5.getWebResponse().getContentAsString());
		
		HtmlPage page6=getHtml("http://www.gdzs.lss.gov.cn:8030/myprofile/index.action?appcode=A1_05_01",webClient);
		System.out.println(page6.getWebResponse().getContentAsString());
		
		HtmlPage page7=getHtml("http://www.gdzs.lss.gov.cn:8030/myprofile/index.action?appcode=C1_02_01",webClient);
		System.out.println(page7.getWebResponse().getContentAsString());
//
//		
//	   String loginUrl4 = "http://58.59.38.82:8080/rzwscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp";
//	                     
//		WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
//		String requestBody="sfzh=371121199109191211&zgxm=%D5%D4%CE%C4%C7%E0&zgzh=200000053228&dwbm=0103040362&cxyd=0&zgzt=%D5%FD%B3%A3";
//		webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest4.setAdditionalHeader("Connection", "keep-alive");
//		webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest4.setAdditionalHeader("Host", "58.59.38.82:8080");
//		webRequest4.setAdditionalHeader("Origin", "http://58.59.38.82:8080");
//		webRequest4.setAdditionalHeader("Referer", "http://58.59.38.82:8080/rzwscx/zfbzgl/zgzhxx/zgzhxx.jsp");
//		webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//		webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		webRequest4.setRequestBody(requestBody);
//		HtmlPage page4 = webClient.getPage(webRequest4);
//		System.out.println(page4.asXml());
//	
//	    String loginUrl5 = "http://58.59.38.82:8080/rzwscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";			
//		WebRequest webRequest5 = new WebRequest(new URL(loginUrl5), HttpMethod.POST);
//		String requestBody5="sfzh=371121199109191211&zgxm=%D5%D4%CE%C4%C7%E0&zgzh=200000053228&dwbm=0103040362&cxyd=0&zgzt=%D5%FD%B3%A3";
//		webRequest5.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest5.setAdditionalHeader("Connection", "keep-alive");
//		webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest5.setAdditionalHeader("Host", "58.59.38.82:8080");
//		webRequest5.setAdditionalHeader("Origin", "http://58.59.38.82:8080");
//		webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//		webRequest5.setRequestBody(requestBody5);
//		HtmlPage page5 = webClient.getPage(webRequest5);
//		System.out.println(page5.asXml());
			
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
