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
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class zaozhuanglogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://www.zzzfgjj.com/wt-web/login";

		HtmlPage page = (HtmlPage) getHtml(url, webClient);

		HtmlTextInput username = (HtmlTextInput) page.getElementById("username_sfz");//身份证
		username.setText("37040519870324134X");
		HtmlTextInput username2 = (HtmlTextInput) page.getElementById("username");//身份证
		username2.setText("37040519870324134X");
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("password");//密码
		pass.setText("675725");
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码
		String imageName = "111.jpg";
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		captcha.setText(inputValue);
		
		HtmlButton login = (HtmlButton) page.getFirstByXPath("//button[@onclick='submitForm();']");

		login.click();
		
		

		//5D9016E4937  95E0
		String url2 = "http://www.zzzfgjj.com/wt-web/login?"
				+ "username=5e59192b057d5dcd564845f676ed5213%2C57d4247888a5957043dc3a23f708b129"
			    + "&a001=10043269"
				+ "&password=248c0e828a25b7e8c73cdcf994570d8b"
				+ "&captcha="+inputValue
				+ "&modulus=008ea03130d5c355ace49077e75b941a41"
				+ "&exponent=010001";
		
		
		getHtml(url2, webClient);
		
		String url3 = "http://www.zzzfgjj.com/wt-web/home";
		
		Page page2 = getHtml(url3, webClient);
		String html2 = page2.getWebResponse().getContentAsString();

		System.out.println(html2);
		
		if(html2.indexOf("山东枣庄公积金网厅登录页")!=-1){
			System.out.println("登录失败");
		}else{
			System.out.println("登录成功");
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
}
