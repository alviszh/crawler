package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;
/**
 * 该测试类不好用，由于网站登录页面，错误后的提示只是一闪，
 * 所以如下登录方式并不能准确捕捉错误登录信息，
 * 且不好定位到个人用户登录方式
 * @author sln
 *
 */
public class LoginTest {
	public static void main(String[] args) {
		try {
			String url="http://60.213.43.44/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				String loginHtml=hPage.asXml();
				System.out.println("登录页面html为："+loginHtml);
				
				//先选择用户登录的方式：
				hPage.getFirstByXPath("//a[@onclick='clickTab(this, 1)']");
				
//				HtmlAnchor anchor = (HtmlAnchor) hPage.getByXPath("//*[@id=\"ctl00_LKGContext_CardUserFill1_ctl00_Pager1_Next\"]").get(0);  
				HtmlAnchor anchor = (HtmlAnchor) hPage.getFirstByXPath("//a[@onclick='clickTab(this, 1)']");
				hPage=anchor.click(); 
				String alertMsg0 = WebCrawler.getAlertMsg();
				
				//选择了个人用户页面登录的返回结果是：
				System.out.println("个人用户登录后的页面弹框提示是："+alertMsg0);
				
			    Thread.sleep(1000);
				
				HtmlImage image = hPage.getFirstByXPath("//img[@id='f_svl']");
				String imageName = "111.jpg"; 
				File file = new File("D:\\img\\"+imageName); 
				image.saveAs(file); 	
				HtmlTextInput username = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='account']"); 
				HtmlTextInput password = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='psddly']");
				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='captcha']");
				HtmlInput button = (HtmlInput)hPage.getFirstByXPath("//input[@name='input2']");			
				username.setText("530326198811294920");
				password.setText("zhq19881129");
				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
				validateCode.setText(inputValue); 	
				HtmlPage logonPage = button.click();
				String alertMsg = WebCrawler.getAlertMsg();
				System.out.println("弹出的错误信息是："+alertMsg);
				if(null!=logonPage){
					String logonHtml=logonPage.getWebResponse().getContentAsString();
					if(logonHtml.contains("success")){
						System.out.println("登录成功！");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常");
		}
	}
}
