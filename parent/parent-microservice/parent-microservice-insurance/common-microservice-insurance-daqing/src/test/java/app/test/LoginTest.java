/**
 * 
 */
package app.test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

/**
 * @author sln
 * @date 2018年8月16日下午4:59:33
 * @Description: 
 */
public class LoginTest {
	public static void main(String[] args) {
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://dq12333.gov.cn/";   //用于验证登陆信息的连接
		try {
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					System.out.println("可以正常响应页面");
				}else{
					System.out.println("页面无响应");
				}
			}else{
				System.out.println("页面无响应");
			}
		} catch (Exception e) {
			System.out.println("出现异常："+e.toString());
		}
    
	}
}
