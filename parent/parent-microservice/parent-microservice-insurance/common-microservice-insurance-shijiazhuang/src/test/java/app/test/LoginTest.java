/**
 * 
 */
package app.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;


/**
 * @author sln
 * @date 2018年8月3日下午3:33:25
 * @Description: 测试发现登陆方式需要换成post请求的方式，不能用之前的模拟点击的方式了
 */
public class LoginTest {
	public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {
		String url = "http://grsbcx.sjz12333.gov.cn/login.do?method=begin";
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage searchPage = webClient.getPage(webRequest);		
		if(null!= searchPage){
			HtmlImage image = searchPage.getFirstByXPath("//img[@id='jcaptcha']");
			String imageName = "shijiazhuang.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			//验证登陆信息
			url="http://grsbcx.sjz12333.gov.cn/j_unieap_security_check.do";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody=""
					+ "Method=P"
					+ "&pid=1373174326875"
					+ "&j_username=130103198805052124"
					+ "&j_password=234721"
					+ "&jcaptcha_response="+code+"";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					System.out.println("获取的登陆成功的信息是："+html);
					if(html.contains("mianFrame")){
						System.out.println("登陆成功");
					}else if(html.contains("si_LoginErrMsg")){ //登陆失败
						System.out.println("登陆失败");
					}
			}
		}
	}
}
