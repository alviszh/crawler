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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
public class LoginTest {
	public static void main(String[] args) {
		try {
			String url="http://222.175.225.42:8222/rz_query/logAction.do?method=logmain";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				String html=hPage.asXml();
//				System.out.println("登录页面html为："+html);
				HtmlImage image = hPage.getFirstByXPath("//img[@id='jcaptcha']");
				String imageName = "111.jpg"; 
				File file = new File("D:\\img\\"+imageName); 
				image.saveAs(file); 	
//				HtmlTextInput username = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='j_username']"); 
//				HtmlPasswordInput password = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@id='j_password']");
//				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='jcaptcha_response']");
//				HtmlAnchor button = (HtmlAnchor)hPage.getFirstByXPath("//a[@id='loginButton']");			
//				username.setText("371121199109191211");
//				password.setText("wq12345678");
				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
//				validateCode.setText(inputValue); 	
//				HtmlPage logonPage = button.click();
				
				
//				用如下链接验证相关信息输入是否正确
				url="http://222.175.225.42:8222/rz_query/j_unieap_security_check.do?logtype=1";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="j_username=371121199109191211"
						+ "&j_password=wq12345678"
						+ "&jcaptcha_response="+inputValue+"";
//						+ "&city2="
//						+ "&j_username2="
//						+ "&j_password2="
//						+ "&jcaptcha_response2=";
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
//					int status=page.getWebResponse().getStatusCode();
//					System.out.println("登录成功之后的页面响应的状态吗是："+status);
					html=page.getWebResponse().getContentAsString();
//					System.out.println("获取的验证登录信息的html是："+html);
					if(html.contains("setEsgStyle")){
						System.out.println("出现了登录错误");
						if(html.contains("错误的验证码")){
							System.out.println("错误的验证码");
						}else if(html.contains("用户名不存在或密码错误")){
							System.out.println("用户名不存在或密码错误");
						}else{
							System.out.println("出现了其他错误");  //此处应该保留登录信息验证响应的html
						}
					}else{
						System.out.println("登录成功");
						
						//用户信息
//						url="http://222.175.225.42:8222/rz_query/personQueryAction.do?method=gg_basic";   //不加也可以
//						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//						page = webClient.getPage(webRequest);
//						if(null!=page){
//							html=page.getWebResponse().getContentAsString();
//							System.out.println("获取的用户信息页面之前需要先请求用户信息显示的位置框架html页面"+html);
//							url="http://222.175.225.42:8222/rz_query/rpc.do?method=doQuery";
//							requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"queryStore\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"queryStore\",pageNumber:1,pageSize:1,recordCount:0,rowSetName:\"pq.vpq_gg_ac01\"}},parameters:{\"synCount\":\"true\"}}}";
//							webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//							//如下请求头信息必须加上，不然无法响应出数据
//							webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
//							webRequest.setAdditionalHeader("Host", "222.175.225.42:8222");
//							webRequest.setAdditionalHeader("Origin", "http://222.175.225.42:8222");
//							webRequest.setAdditionalHeader("Referer", "http://222.175.225.42:8222/rz_query/personQueryAction.do?method=gg_basic");
//							webRequest.setRequestBody(requestBody);
//							page = webClient.getPage(webRequest);
//							if(null!=page){
//								html=page.getWebResponse().getContentAsString();
//								System.out.println("获取的用户信息页面是"+html);
//								
//							}
//						}
							
						//测试请求养老信息
						url="http://222.175.225.42:8222/rz_query/rpc.do?method=doQuery";
						requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"queryStore\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"queryStore\",pageNumber:1,"
								+ "pageSize:100,recordCount:0,"
								+ "rowSetName:\"pq.vpq_yl_sj\""
								+ ",condition:\"[VPQ_YL_SJ_AAE003] >= '201212' and [VPQ_YL_SJ_AAE003] <= '201712'\"}},parameters:{\"synCount\":\"true\"}}}";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
						webRequest.setAdditionalHeader("Host", "222.175.225.42:8222");
						webRequest.setAdditionalHeader("Origin", "http://222.175.225.42:8222");
						webRequest.setAdditionalHeader("Referer", "http://222.175.225.42:8222/rz_query/personQueryAction.do?method=change&forwardjsp=gg_grjf_ylmx");
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的养老信息页面是"+html);
							
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常"+e.toString());
		}
	}
}
