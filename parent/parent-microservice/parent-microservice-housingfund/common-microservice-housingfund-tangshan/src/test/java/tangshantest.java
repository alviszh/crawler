

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
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

public class tangshantest {

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			webClient.getCookieManager().getCookies();

			String url = "http://60.2.168.122:9080/olbh/index";
			HtmlPage page = (HtmlPage)getHtml(url, webClient);
			
			String contentAsString = page.getWebResponse().getContentAsString();
			Elements elementsByTag = Jsoup.parse(contentAsString).getElementsByTag("head").get(0).getElementsByTag("script");
			String string = elementsByTag.get(0).toString();
		//	System.out.println(text);
			
			
			String splitData = splitData(string,"var csrfVal = \"","\";");
			System.out.println(splitData);
			//idNumber
			HtmlTextInput idNumber = (HtmlTextInput)page.getFirstByXPath("//input[@id='idNumber']");//身份证号
			HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='cardno']");//联名卡号
			HtmlPasswordInput password =(HtmlPasswordInput) page.getFirstByXPath("//input[@id='password']");//密码
			HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='verifyCode']");//验证码

			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='person_pic']");//图片验证码
			String imageName = "111.jpg"; 
			File file = new File("F:\\img\\" + imageName);

			image.saveAs(file);

			HtmlTextInput imgver = (HtmlTextInput)page.getFirstByXPath("//input[@name='dxyzm']");//手机验证码
			
			idNumber.setText("13022919901202184X");//13022919901202184X
			cardno.setText("");//6217000180014445468
			password.setText("901202");
			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			verifyCode.setText(inputValue);

			HtmlButton sendBtnText = (HtmlButton) page.getElementById("sendBtnText");//发送短信
			HtmlPage page4 = sendBtnText.click();
			
			String error = "";
			if(page4.asText().contains("请输入合法的证件号码")){
				error = "请输入合法的证件号码";
			}else if(page4.asText().contains("请输入验证码")){
				error = "请输入验证码";
			}else if(page4.asText().contains("验证码输入错误")){
				error = "验证码输入错误";
			}else if(page4.asText().contains("[卡号]不能为空！")){
				error = "[卡号]不能为空！";
			}else if(page4.asText().contains("[密码]不能为空！")){
				error = "[密码]不能为空！";
			}else if(page4.asText().contains("证件号码、卡号、密码不匹配！")){
				error = "证件号码、卡号、密码不匹配！";
			}else if(page4.asText().contains("[密码]不能为空！")){
				error = "[密码]不能为空！";
			}
			//Session invalid
			System.out.println(error);
			

			String msg = WebCrawler.getAlertMsg();
			System.out.println(msg);
			String duanxin = JOptionPane.showInputDialog("请输入短信验证码……");
			imgver.setText(duanxin);

//			HtmlButton form = (HtmlButton) page.getFirstByXPath("//button[@type='submit']");
//			Page page3 = form.click();
//			String html = page3.getWebResponse().getContentAsString();
//			System.out.println(html);
//
//
//			String url3 = "http://60.2.168.122:81/per.login";
//			//cardno=6217000180014445468&hi_value=0&passwd=901202&vericode=7407&seqno=3&telphone=&yzword=835353
//			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
//			paramsList2.add(new NameValuePair("cardno", "6217000180014445468"));
//			paramsList2.add(new NameValuePair("hi_value", "0"));
//			paramsList2.add(new NameValuePair("passwd","901202"));
//			paramsList2.add(new NameValuePair("vericode", inputValue));
//			paramsList2.add(new NameValuePair("seqno", "2"));
//			paramsList2.add(new NameValuePair("telphone", ""));
//			paramsList2.add(new NameValuePair("yzword", duanxin));
//			Page page2 = gethtmlPost(webClient, paramsList2, url3);
//			String html2 = page2.getWebResponse().getContentAsString();
//			System.out.println(html2);

			//			};
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url){
		try {

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			//		webRequest.setAdditionalHeader("Host", "60.2.168.122:81");
			//		webRequest.setAdditionalHeader("Referer", "http://60.2.168.122:81/index.jsp?flg=1");
			//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			//		webRequest.setAdditionalHeader("Origin", " http://60.2.168.122:81");
			//		webRequest.setAdditionalHeader("Cache-Control", " max-age=0");
			//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			//		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public static String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}	
