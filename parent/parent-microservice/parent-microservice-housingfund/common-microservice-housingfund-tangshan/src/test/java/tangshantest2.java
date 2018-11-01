

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.service.LoginAndGetUnit;
import app.service.common.LoginAndGetCommon;

public class tangshantest2 {

	public static void main(String[] args) {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url2 = "http://60.2.168.122:81/per.login";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("cardno", "6217000180014445468"));
		paramsList.add(new NameValuePair("hi_value", "0"));
		paramsList.add(new NameValuePair("passwd", "901202"));
		paramsList.add(new NameValuePair("vericode", "8496"));
		paramsList.add(new NameValuePair("seqno", "3"));
		paramsList.add(new NameValuePair("telphone", ""));
		paramsList.add(new NameValuePair("yzword", "639456"));
		try {
			Page page = gethtmlPost(webClient, paramsList, url2);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Host", "60.2.168.122:81");
		webRequest.setAdditionalHeader("Origin", "http://60.2.168.122:81");
		webRequest.setAdditionalHeader("Referer", "http://60.2.168.122:81/index.jsp?flg=1");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "/");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("X-Powered-By", "Servlet/3.0");
		webRequest.setAdditionalHeader("Keep-Alive", "timeout=10, max=100");
		webRequest.setAdditionalHeader("Cookie","JSESSIONID=0001h66R6k_XvW_DJkrorCVzwp6:-K07B4G; cookiesession1=LLIBMBAU0GB2ODG2NFB2H1KL9XLYAPYL; td_cookie=18446744072775669813");
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
