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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.io.ByteStreams;
import com.module.htmlunit.WebCrawler;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String imageUrl= "http://www.bzgjj.cn/code.php";         
		WebRequest webRequest = new WebRequest(new URL(imageUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Content-Type", "image/png");
		webRequest.setAdditionalHeader("Connection", "keep-alive");	
		webRequest.setAdditionalHeader("Host", "www.bzgjj.cn");	
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
	
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
	    String loginUrl4 = "http://www.bzgjj.cn/userlogin.php";		                     
		WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();			
		paramsList.add(new NameValuePair("username", "zhaotongtong"));
		paramsList.add(new NameValuePair("password", "ztt123456"));
		paramsList.add(new NameValuePair("checkcode", input));
		paramsList.add(new NameValuePair("url", "index.php"));
		paramsList.add(new NameValuePair("action", "check"));
		paramsList.add(new NameValuePair("Submit.x", "36"));
		paramsList.add(new NameValuePair("Submit.y", "18"));
		//String requestBody="username=zhaotongtong&password=ztt123456&checkcode="+input+"&url=index.php&action=check&Submit.x=36&Submit.y=18";
		webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest4.setAdditionalHeader("Connection", "keep-alive");
		webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest4.setAdditionalHeader("Host", "www.bzgjj.cn");
		webRequest4.setAdditionalHeader("Origin", "http://www.bzgjj.cn");
		webRequest4.setAdditionalHeader("Referer", "http://www.bzgjj.cn/index.php");
		webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest4.setRequestParameters(paramsList);
		HtmlPage page4 = webClient.getPage(webRequest4);
		System.out.println(page4.asXml());
		
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
