package text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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

public class siping {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "https://xn--fiq70ag3c6xju1jmtau7ekthks2cjmmomeop8f.xn--55qw42g.cn:8443/wt-web/login";

		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput a001 = (HtmlTextInput) page.getElementById("a001");//职工号
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("in_password");//密码
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码
		
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");

		a001.setText("0182622000119");
		username.setText("222403198910250421");
		pass.setText("123456");
		captcha.setText(inputValue);
		
		HtmlButton login = (HtmlButton) page.getFirstByXPath("//button[@onclick='individualSubmitForm();']");
		
		Page page2 = login.click();

		String html = page2.getWebResponse().getContentAsString();

		System.out.println(html);
		if(html.indexOf("加载中 ...")!=-1){
			System.out.println("登录成功");
			String date = new Date().toLocaleString().substring(0, 10);
			String url2= "https://xn--fiq70ag3c6xju1jmtau7ekthks2cjmmomeop8f.xn--55qw42g.cn:8443/wt-web/personal/jcmxlist?"
					+ "UserId=1&beginDate=2000-01-01&endDate="+date+"&userId=1&pageNum=1&pageSize=500";
			
			//String url2 = "https://xn--fiq70ag3c6xju1jmtau7ekthks2cjmmomeop8f.xn--55qw42g.cn:8443/wt-web/person/bgcx";
			Page page3 = gethtmlPost(webClient, null, url2);
			String string = page3.getWebResponse().getContentAsString();
			
			System.out.println(string);
		}else{
			System.out.println("登录失败");
		}
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
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	public static Page getHtml1(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
