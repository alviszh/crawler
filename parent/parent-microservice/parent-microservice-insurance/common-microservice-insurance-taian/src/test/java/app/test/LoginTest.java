package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
public class LoginTest {
	public static void main(String[] args) {
		try {
			String url="http://124.130.146.14:8002/hso/logon_370900.jsp";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				//成功获取登录页面，继续接下来获取图片验证码的操作：
				url="http://124.130.146.14:8002/hso/genAuthCode2?_=0.11931910041354588";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					String imageName = "captcha.jpg"; 
					File file = new File("D:\\img\\"+imageName); 
					String imgagePath = file.getAbsolutePath(); 
					InputStream inputStream = page.getWebResponse().getContentAsStream();
					FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
					if (inputStream != null && outputStream != null) {  
				        int temp = 0;  
				        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
				        	outputStream.write(temp);   // 边读边写  
				        }  
				        outputStream.close();  
				        inputStream.close();   // 关闭输入输出流  
				    }
				}
				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
				url="http://124.130.146.14:8002/hso/logon.do?";
				String requestBody="method=doLogonAllowRepeat"
						+ "&usertype=1"
						+ "&username=370902199112251848"
						+ "&password=f93a7b97e2f953816089ff0755679c85"
						+ "&validatecode="+inputValue+""
						+ "&appversion=1.1.89";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setRequestBody(requestBody);
				webClient.getOptions().setJavaScriptEnabled(false);
				page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					System.out.println("验证登录信息，响应的页面是："+html);
					
					
//					//请求个人信息
//					url="http://124.130.146.14:8002/hso/hsoPer.do?";
//					requestBody="method=QueryPersonBaseInfo&__logon_ticket=null";
//					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//					webRequest.setRequestBody(requestBody);
//					page = webClient.getPage(webRequest);
//					if(null!=page){
//						html=page.getWebResponse().getContentAsString();
//						System.out.println("获取的用户信息的页面是："+html);
//					}
					
					//获取缴费信息，此处测试改变每一页的个数可不可以一次性爬取完所有数据
					url="http://124.130.146.14:8002/hso/persi.do?";
//					requestBody="method=queryZgYanglaozh&__logon_ticket=null";
					requestBody="method=queryMediAccount&__logon_ticket=null";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						System.out.println("获取的养老保险缴费明细信息："+html);
						//尝试解析相关信息
						Document doc = Jsoup.parse(html);
						String text = doc.getElementsByClass("dataTable").get(1).getElementsByTag("tr").get(0).text();
						System.out.println("获取的元素内容是："+text);
						//用第一个dataTable来获取数据
						Elements trs = doc.getElementsByClass("dataTable").get(1).getElementsByTag("tr");
						//为了测试，写的10
						for (int i=1;i<10;i++) {
							//获取缴费年月
							System.out.println(trs.get(i).getElementsByTag("td").get(1).getElementsByTag("input").get(0).val());
							
						}
						
						
					}
					
				
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常"+e.toString());
		}
	}
}
