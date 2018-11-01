package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Test2 {

	private final static String ENCODE = "UTF-8"; 
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		/*String smsUrl = "http://wap.zhgjj.org.cn/wt-web/retrieve/fsyzm?dwkey=&ywbm=109998&jgbm=01&blqd=wt_02&sftj=1&grkey=18688156180";
		WebRequest webRequest = new WebRequest(new URL(smsUrl), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		System.out.println("page----"+page.getWebResponse().getContentAsString());*/
		
		String loginUrl = "http://wap.zhgjj.org.cn/wt-web/grlogin";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		System.out.println("loginPage--->"+loginPage.asXml());
		
		HtmlTextInput username = loginPage.getFirstByXPath("//input[@id='username']");
		HtmlTextInput zjhm = loginPage.getFirstByXPath("//input[@id='zjhm']");
		HtmlTextInput captcha = loginPage.getFirstByXPath("//input[@id='captcha']");
		HtmlTextInput ssyzm = loginPage.getFirstByXPath("//input[@id='ssyzm']");
		HtmlImage img = loginPage.getFirstByXPath("//img[@id='captcha_img']");
		HtmlButtonInput login = loginPage.getFirstByXPath("//input[@id='gr_login']");
		
		File file = new File("E:\\Codeimg\\zhuhai.jpg");
		img.saveAs(file);
		
		username.setText("18688156180");
		zjhm.setText("370786199202151222");
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String imgcode = scanner.next();
		captcha.setText(imgcode);
		
		String smscode = scanner.next();
		ssyzm.setText(smscode);
		HtmlPage loginedPage = login.click();
		System.out.println("loginedPage===="+loginedPage.asXml());
		if(!loginedPage.asXml().contains("今日登陆次数已达上限")){
			String url = "http://wap.zhgjj.org.cn/wt-web/grlogintwo?force_and_dxyz=1&grloginDxyz=0&username=18688156180&zjhm=370786199202151222&password=111111&force=&captcha="+imgcode+"&sliderCaptcha=&ssyzm="+smscode;
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			Page loginedPage2 = webClient.getPage(webRequest);
			System.out.println("loginedPage2===="+loginedPage2.getWebResponse().getContentAsString());
			
			String url2 = "http://wap.zhgjj.org.cn/wt-web/home?logintype=1";
			webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
			Page loginedPage3 = webClient.getPage(webRequest);
			System.out.println("loginedPage3===="+loginedPage3.getWebResponse().getContentAsString());
			if(loginedPage3.getWebResponse().getContentAsString().contains("var grzh='")){
				String content = loginedPage3.getWebResponse().getContentAsString();
				int i = content.indexOf("var grzh='");
				int j = content.indexOf("';", i);
				String grzh = content.substring(i+10, j);
				if(null != grzh && !grzh.equals("")){
					String userUrl = "http://wap.zhgjj.org.cn/wt-web/jcr/jcrkhxxcx_mh.service";
					webRequest = new WebRequest(new URL(userUrl), HttpMethod.POST);
					webRequest.setAdditionalHeader("Host", "wap.zhgjj.org.cn");
					webRequest.setAdditionalHeader("Origin", "http://wap.zhgjj.org.cn");
					webRequest.setAdditionalHeader("Referer", "http://wap.zhgjj.org.cn/wt-web/home?logintype=1");
					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
					webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
					webRequest.setRequestBody("grxx=grbh&ffbm=01&ywfl=01&ywlb=99&cxlx=01");
					Page userPage = webClient.getPage(webRequest);
					System.out.println("userPage===="+userPage.getWebResponse().getContentAsString());
					
					String payUrl = "http://wap.zhgjj.org.cn/wt-web/jcr/jcrxxcxzhmxcx.service?dwzh=&ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2010-01-01&jsrq=2018-04-18&grxx="+grzh+"&fontSize=13px&pageNum=1&pageSize=100&totalcount=&pages=&random=0.7840922261826775";
					webRequest = new WebRequest(new URL(payUrl), HttpMethod.GET);
					webRequest.setAdditionalHeader("Host", "wap.zhgjj.org.cn");
					webRequest.setAdditionalHeader("Origin", "http://wap.zhgjj.org.cn");
					webRequest.setAdditionalHeader("Referer", "http://wap.zhgjj.org.cn/wt-web/home?logintype=1");
					webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
					webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//					webRequest.setRequestBody("dwzh=&ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2010-01-01&jsrq=2018-04-11&grxx=00350164&fontSize=13px&pageNum=1&pageSize=100&totalcount=7&pages=1&random=0.7840922261826775");
					Page payPage = webClient.getPage(webRequest);
//					save(payPage.getWebResponse().getContentAsStream(), "E:\\Codeimg\\zhuhaiPayInfo2.png");
					
					System.out.println("payPage===="+payPage.getWebResponse().getContentAsString());
				}
			}
		}
		
		/*HtmlTextInput cardno = loginPage.getFirstByXPath("//input[@id='cardno']");
		HtmlTextInput xm = loginPage.getFirstByXPath("//input[@id='xm']");
		HtmlTextInput authcode = loginPage.getFirstByXPath("//input[@id='authcode']");
		HtmlButton docx = loginPage.getFirstByXPath("//button[@id='docx']");
		
		cardno.setText("420323198309053414");
		xm.setText("张绍朋");
		authcode.setText(input);
		Page page = docx.click();*/
		
//		String loginUrl2 = "https://grcx.sygjj.gov.cn/servlet/query.do";
//		System.out.println("loginUrl2---->"+loginUrl2);
//		webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.POST);
//		webRequest.setAdditionalHeader(":authority", "grcx.sygjj.gov.cn");
//		webRequest.setAdditionalHeader(":method", "POST");
//		webRequest.setAdditionalHeader(":path", "/servlet/query.do");
//		webRequest.setAdditionalHeader(":scheme", "https");
//		webRequest.setAdditionalHeader("accept", "application/json, text/javascript, */*; q=0.01");
//		webRequest.setAdditionalHeader("accept-encoding", "gzip, deflate, br");
//		webRequest.setAdditionalHeader("accept-language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
//		webRequest.setAdditionalHeader("origin", "https://grcx.sygjj.gov.cn");
//		webRequest.setAdditionalHeader("referer", "https://grcx.sygjj.gov.cn/");
//		webRequest.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("x-requested-with", "XMLHttpRequest");
//		webRequest.setRequestBody("cardno=420323198309053414&xm="+getURLEncoderString("张绍朋")+"&authcode="+input);
//		Page page = webClient.getPage(webRequest);
//		System.out.println("page----->"+page.getWebResponse().getContentAsString());
	}
	
	public static String getURLEncoderString(String str) {
		String result = "";
		if (null == str) {
		    return "";
		}
		try {
		    result = java.net.URLEncoder.encode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		return result;
    }
	
	public static void save(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);

		int bytesWritten = 0;
		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);

		}
		inputStream.close();
		outputStream.close();
	}
	
	 public static String encodeImgageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
		    // 其进行Base64编码处理  
		    byte[] data = null;  
		    // 读取图片字节数组  
		    try {  
		        InputStream in = new FileInputStream(imageFile);  
		        data = new byte[in.available()];  
		        in.read(data);  
		        in.close();  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
		  
		    // 对字节数组Base64编码  
//		    BASE64Encoder encoder = new BASE64Encoder();  
		    return new String(Base64.encodeBase64(data));// 返回Base64编码过的字节数组字符串  
		}
}
