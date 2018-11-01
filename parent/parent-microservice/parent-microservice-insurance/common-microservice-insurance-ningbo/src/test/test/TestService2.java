package test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class TestService2{
	
	public static void main(String[] args) {
		
		try {
			login("330222197703136914", "63207764");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static String selector_username = "#loginid";
	private static String selector_password = "#pwd";
	private static String selector_userjym = "#yzm";
    public static void login( String name, String password ) throws Exception{
    	
    	String url = "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/index.jsp";
    	
    	String urlData = "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-ylbx.jsp";
    	
        WebClient webClient = new WebClient();
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//		  webClient.getOptions().setJavaScriptEnabled(true);  
		// 2 禁用Css，可避免自动二次请求CSS进行渲染
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		// 3 启动客户端重定向
		webClient.getOptions().setRedirectEnabled(true);
		// 4 js运行错误时，是否抛出异常
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		// 5 设置超时
		webClient.getOptions().setTimeout(80000);
		HtmlPage searchPage = webClient.getPage(url);
		// 等待JS驱动dom完成获得还原后的网页
		webClient.waitForBackgroundJavaScript(30000); 
//        WebRequest webRequest111 = new WebRequest(new URL(url), HttpMethod.GET);
//        HtmlPage searchPage = webClient.getPage(webRequest111);
//        webClient.getOptions().setJavaScriptEnabled(false);
//		HtmlPage searchPage = webClient.getPage(url);
//		System.out.println(searchPage.asXml());
		HtmlImage image = searchPage.getFirstByXPath("//img[@id='yzmJsp']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		image.saveAs(file);
		
		HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector(selector_username);
		inputUserName.reset();
		inputUserName.setText(name);

		HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector(selector_password);
		inputPassword.reset();
		inputPassword.setText(password);
		
		HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector(selector_userjym);
		inputuserjym.reset();
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		inputuserjym.setText(inputValue);

//		HtmlElement loginButton =(HtmlElement) searchPage.querySelector("#btnLogin");
		HtmlButtonInput loginButton = searchPage.querySelector("#btnLogin");
		if (loginButton == null) {
			throw new Exception("login button can not found : null");
		} else {
			// Click Submit Button
			// loginButton.click();
			searchPage = loginButton.click();
//			HtmlListItem li = searchPage.querySelector("#ylbxNew");
//			searchPage = li.click();
//			String xmlStr = searchPage.asXml();
//			System.out.println(xmlStr);
//			org.jsoup.nodes.Document document = Jsoup.parse(xmlStr);
//			String sjh = document.getElementsByTag("td").text();
//			System.out.println("--------"+sjh+"11111111");
		}
//		WebRequest webRequest = new WebRequest(new URL(urlData), HttpMethod.POST);
//		HtmlPage searchPage1 = webClient.getPage(webRequest);
		HtmlPage searchPage1 = webClient.getPage(urlData);
		webClient.waitForBackgroundJavaScript(30000); 
		System.out.println("------------------------------------------");
		System.out.println(searchPage1.asXml());
		
    }
    
    
}
