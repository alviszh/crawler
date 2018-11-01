package test;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.controller.HousingBasicController;

public class hlbelogin extends HousingBasicController{

	public static void main(String[] args) {

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.zfgjj.com.cn/perlogin.jhtml";
			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput cardno = (HtmlTextInput) page.getElementById("cardno");//身份证
			HtmlPasswordInput perpwd = (HtmlPasswordInput) page.getElementById("perpwd");//密码
			HtmlTextInput verify = (HtmlTextInput)page.getFirstByXPath("//input[@name='verify']");//图片验证码
			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='guestbookCaptcha']");//图片
			String imageName = "111.jpg"; 
			File file = new File("F:\\img\\" + imageName);
			image.saveAs(file);
			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			
			HtmlButton sub = (HtmlButton) page.getElementById("sub");//登录
			String name = "152101199106150646";
			
			
			cardno.setText(name);//152101199106150646
			perpwd.setText("131922");//131922
			verify.setText(inputValue);
			Page page2 = sub.click();
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			String url7 = "http://www.zfgjj.com.cn/GJJQuery?"
					+ "tranCode=112805"
					+ "&task="
					+ "&certinum="+name
					+ "&flag=0";
			Page page3 = gethtmlPost(webClient, null, url7);
			String string = page3.getWebResponse().getContentAsString();
			System.out.println(string);
			if(html.indexOf("个人账户基本信息查询-呼伦贝尔市住房公积金管理中心")!=-1){
				System.out.println("登录成功");
				String url4 = "http://www.zfgjj.com.cn/siteViewCount.do";
				Page gethtmlPost = gethtmlPost(webClient, null, url4);
				String html5 = gethtmlPost.getWebResponse().getContentAsString();
				System.out.println(html5);
				Cookie cookie = webClient.getCookieManager().getCookie("gjjaccnum");
				String value = cookie.getValue();
				String html2 = getuserinfo(webClient);//个人信息
				System.out.println(html2);
				
				String html3 = getpay(webClient);
				System.out.println(html3);
				
				
			}
			else{
				System.out.println("登录失败");
				if(name.length()<10){
				String alertMsg = WebCrawler.getAlertMsg();
				System.out.println(alertMsg);
				}else{
					System.out.println("查询密码输入有误，必须为6位数字");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//个人信息
	public static String getuserinfo(WebClient webClient){
		//http://www.zfgjj.com.cn/GJJQuery?tranCode=112813&task=&accnum=801037262447
		//http://www.zfgjj.com.cn:80/perbase.jhtml
		String url2 = "http://www.zfgjj.com.cn/GJJQuery?tranCode=112813&task=&accnum=801037262447";
		Page page3 = gethtmlPost(webClient, null, url2);
		String html2 = page3.getWebResponse().getContentAsString();
		return html2;
	}
	//流水
	public static String getpay(WebClient webClient){
		String url = "http://www.zfgjj.com.cn/GJJQuery?tranCode=112814&task=ftp&accnum=801037262447&begdate=2016-02-02&enddate=2018-01-03";
		Page page3 = gethtmlPost(webClient, null, url);
		String html = page3.getWebResponse().getContentAsString();
		return html;
	}
	
	//152101199106150646
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url){
		try {

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
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
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
