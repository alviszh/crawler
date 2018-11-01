package text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class dandong{

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String url = "https://223.100.185.98/wt-web/logout";
		
		HtmlPage page = getHtml(url, webClient);
		
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("in_password");//密码
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码
		
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		username.setText("210604196304021092");
		pass.setText("111111");
		captcha.setText(inputValue);
		
		HtmlButton login = (HtmlButton) page.getElementById("gr_login");
		Page page2 = login.click();
		
		String html = page2.getWebResponse().getContentAsString();
		
		System.out.println(html);
		if(html.indexOf("加载中 ...")!=-1){
		String url2 = "https://223.100.185.98/wt-web/person/bgcx";// https://223.100.185.98/wt-web/person/bgcx
		webClient.addRequestHeader("Host", "223.100.185.98");
		webClient.addRequestHeader("Origin", "http://223.100.185.98");
		webClient.addRequestHeader("Referer", "https://223.100.185.98/wt-web/home?logintype=1");
		
		
		Page page3 = gethtmlPost(webClient, null, url2);
		String html2 = page3.getWebResponse().getContentAsString();//个人信息(OK)
		System.out.println(html2);
		
		
		
		String url5  = "https://223.100.185.98/wt-web/person/jcqqxx";
		Page page5 = getHtml1(url5, webClient);
		InputStream contentAsStream = page5.getWebResponse().getContentAsStream();
		//String c= "C:\\Users\\Administrator\\git\\strong-auth-crawler\\parent\\parent-microservice\\parent-microservice-housingfund\\common-microservice-housingfund-cangzhou\\abc.txt";
		save1(contentAsStream, "f:\\abc.pdf");
		text.login2.readFdf("f:\\abc.pdf");
		File file1 = new File("f:\\abc.pdf");
		String readTxtFile = text.login2.readTxtFile(file1);
		System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile); 
		/**
		 * 借款信息
//		String url4 = "https://223.100.185.98/wt-web/person/dkhkcx";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("htbh", "20140100567"));
//		paramsList.add(new NameValuePair("beginDate", "2000-01-01"));
//		paramsList.add(new NameValuePair("endDate", "2017-12-11"));
//		paramsList.add(new NameValuePair("userId", "1"));
//		paramsList.add(new NameValuePair("pageNum", "1"));
//		paramsList.add(new NameValuePair("pageSize", "100"));
//		
//		Page page4 = gethtmlPost(webClient, paramsList, url4);
//		String html4 = page4.getWebResponse().getContentAsString();//个人信息(OK)
//		System.out.println(html4);
		
		*/
		
		
		
//		String url3 = "https://223.100.185.98/wt-web/person/jcmx?yhkh=2017-07-01";
//		text.login2.saveUrlAs(url3, "f:\\a.pdf", "GET");
//		text.login2.readFdf("f:\\b.pdf");
//		File file1 = new File("f:\\b.txt");
//		String readTxtFile = text.login2.readTxtFile(file1);
//		System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile);  
		
		
		}
		else{
			System.out.println("登录失败");
		}
	}
	
	public static void save1(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int bytesWritten = 0; 
		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
		outputStream.write(bytes, 0, byteCount); 

		} 
		inputStream.close(); 
		outputStream.close(); 
		} 

	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page getHtml1(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "223.100.185.98");
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
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
}
