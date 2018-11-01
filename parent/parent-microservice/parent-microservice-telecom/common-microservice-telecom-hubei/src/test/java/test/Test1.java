package test;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class Test1 {
	
	public static void main(String[] args) throws Exception {
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
//		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
//		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
//		login();
	}

	public static WebClient login() throws Exception {

		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		String url2 = "http://login.189.cn/web/login";
		HtmlPage html = (HtmlPage) getHtml(url2, webClient2);
		System.out.println(html.asXml());
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlTextInput valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("//*[@id='txtCaptcha']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("17707287540");
		passwordInput.setText("081202");

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
			System.out.println(htmlpage2.asXml());

			HtmlImage valiCodeImg = html.getFirstByXPath("//*[@id='imgCaptcha']");
			ImageReader imageReader = valiCodeImg.getImageReader();
			if ((valiCodeImg.isDisplayed())) {
				System.out.println("Element is not displayed!");
				 imageReader = valiCodeImg.getImageReader();
				BufferedImage bufferedImage = imageReader.read(0);

				JFrame f2 = new JFrame();
				JLabel l = new JLabel();
				l.setIcon(new ImageIcon(bufferedImage));
				f2.getContentPane().add(l);
				f2.setSize(500, 100);
				f2.setTitle("验证码");
				f2.setVisible(true);

				String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
				valicodeStrinput.setText(valicodeStr.trim());
				username.setText("17707287540");
				passwordInput.setText("081202");
				htmlpage2 = button.click();
				System.out.println("登录成功页面"+htmlpage2.asXml());
			}else{
				return null;
			}
			
		} else {
			System.out.println("=======成功==============");
		}
		url2 = "http://hb.189.cn:80/";
		HtmlPage	html3 = (HtmlPage) getHtml(url2, webClient2);
		System.out.println(html3.asXml());
//		
		getCityCode(webClient2);
		/*
		 * url2 =
		 * "http://qh.189.cn/service/bill/resto.action?rnd=0.6692071573214755";
		 * 
		 * html3 = (HtmlPage) getHtml(url2, webClient2);
		 */
		return webClient2;

//		Set<Cookie> cookies4 = html3.getWebClient().getCookieManager().getCookies();
//		for (Cookie cookie4 : cookies4) {
//			System.out.println("发送请求的cookie：" + cookie4.toString());
//		}
//		return html3.getWebClient();
		// return null;
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(20000);

		webClient.getOptions().setTimeout(20000); // 15->60
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/service/integral/qryIndex.parser");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
	//获取城市信息
	public static void getCityCode(WebClient webClient) throws Exception {
	
		String url2="http://hb.189.cn/pages/selfservice/custinfo/userinfo/getStrategy.parser";
		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page2 = webClient.getPage(webRequest2);
		System.out.println(page2.getWebResponse().getContentAsString());
		String url = "http://hb.189.cn/ajaxServlet/getCityCodeAndIsLogin?method=getCityCodeAndIsLogin";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "hb.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hb.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://hb.189.cn/hbuserCenter.parser");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		getCityCode(webClient);
	   System.out.println(html);
	}
}
