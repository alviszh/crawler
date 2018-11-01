package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;
/**
 *   该测试类不好用
 * @author sln
 *
 */
public class LoginTest {
	public static void main(String[] args) {
		try {
			String url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				String html=hPage.asXml();
				System.out.println("登录页面html为："+html);
				
				//=========================================================================
//				List<HtmlDivision> list = hPage.getByXPath("//div[@onclick]");
//				for (HtmlDivision object : byXPath) {
//					System.out.println("获取的object是："+object);
//				}
//				Page page =list.get(3).dblClick();
//				String contentAsString = page.getWebResponse().getContentAsString();
//				System.out.println("获取个人信息查询模块后，点击得到的页面 是："+contentAsString);
				Document doc = Jsoup.parse(html);
				//选中个人信息查询
				String text =doc.getElementsByClass("gn3").get(0).text();
				
				System.out.println("获取的要查询的内容种类是："+text);
				//=========================================================================
				
				
				
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
			System.out.println("出现了异常"+e.toString());
		}
	}
}
