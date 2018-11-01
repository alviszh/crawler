package app.test;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class LoginTest {
	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String url="http://gjj.lishui.gov.cn/hdcy.aspx";
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest); 
			
			HtmlImage image = page.getFirstByXPath("//img[@id='CodeImage']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	
			
			HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtName']"); 
			HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='txtPass']"); 
			HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtCode']"); 
			HtmlImageInput submitbt = (HtmlImageInput)page.getFirstByXPath("//input[@name='ctl00']"); 
//			String requestBody=""
//					+ "__EVENTTARGET="
//					+ "&__EVENTARGUMENT="
//					+ "&__VIEWSTATE=%2FwEPDwUJMzQyNTIwOTgwZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUFY3RsMDA0pUW9XvEwGP%2BWjAUyrPD9moUP6I6l%2BZPVPpY6gxhusg%3D%3D"
//					+ "&__EVENTVALIDATION=%2FwEWBgLTsuLvCALEhISFCwKrm7qxCwLKw6LdBQLChPzDDQKhwImNC8G1Tnm1uo97yd1Htl1L5TPdU9mW5l81I9cTYkx5s7A%2F"
//					+ "&txtName=332501198902260222"
//					+ "&txtPass=438326"
//					+ "&txtCode=1111"
//					+ "&ctl00.x=15"
//					+ "&ctl00.y=19";
//			webRequest.setRequestBody(requestBody);
			loginName.setText("332501198902260222"); 
			loginPassword.setText("438326"); 	
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			validateCode.setText(inputValue); 	
			Page page1= submitbt.click(); 
			String msg=WebCrawler.getAlertMsg();
			System.out.println("获取的弹框信息是------------------------------------------"+msg);
			if(null!=page1){
				String html = page1.getWebResponse().getContentAsString();
				if(html.contains("欢迎您登录丽水市住房公积金管理中心")){
					System.out.println("登录成功");
					//该网站一个链接就可以响应所有的数据
					url="http://gjj.lishui.gov.cn/hdcy_1.aspx";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					page1= webClient.getPage(webRequest); 
					if(null!=page1){
						html=page1.getWebResponse().getContentAsString();
						System.out.println("获取到的用户和缴存信息是："+html);
					}
				}else{
					if(msg.contains("错误的身份证号码或密码")){
						System.out.println("错误的身份证号码或密码！");
					}else if(msg.contains("错误的验证码")){
						System.out.println("错误的验证码");
					}else if(msg.contains("错误的身份证号码")){
						System.out.println("错误的身份证号码");
					}else{
						System.out.println("出现了其他类型的登录错误："+msg);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}
	
}
