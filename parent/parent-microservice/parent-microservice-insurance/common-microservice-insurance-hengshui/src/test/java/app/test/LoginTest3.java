package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;
/**
 *该测试类不好用
 *
 */
public class LoginTest3 {
	public static void main(String[] args) {
		try {
			String url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			Page hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				String html=hPage.getWebResponse().getContentAsString();
				System.out.println("登录页面html为："+html);
				getImg();
				String code = JOptionPane.showInputDialog("请输入验证码……"); 
				//在此处用请求链接的方式来登录，因为此链接会将错误信息响应回来
				url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/login?j_username=131102199111230220&j_password=911018&j_captcha="+code+"";
				webRequest=new WebRequest(new URL(url), HttpMethod.POST);
				Page page = webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					System.out.println("用链接进行登录信息的校验，返回的信息是；"+html);
					if(html.contains("0000")){
						//登录成功
						//获取相关信息的请求必须带参数——个人编号，该参数如下链接请求：
				 		 url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/curuser?_=1513570750019";
				 		 webRequest=new WebRequest(new URL(url), HttpMethod.GET);
						 page = webClient.getPage(webRequest);
						 if(page!=null){
							 html=page.getWebResponse().getContentAsString();
							 System.out.println("获取的个人编号参数信息的html是:"+html);
							 String pernum = JSONObject.fromObject(html).getJSONArray("userbussList").getJSONObject(0).getString("grbh");
							//请求必须带参数
							url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/app/getYalRyjbxx?grbh="+pernum+"";
							webRequest=new WebRequest(new URL(url), HttpMethod.GET);
							page = webClient.getPage(webRequest);
							if(page!=null){
								html=page.getWebResponse().getContentAsString();
								System.out.println("获取的用户信息的html是:"+html);
							}
						 }
					}
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常"+e.toString());
		}
		
	}
	private static String getImg() { 
		String url = "http://ggfw.hsrsw.gov.cn:8001/ggfwweb/captchaimg?tm=1513578831622"; 
		

		Connection con = Jsoup.connect(url).header("Accept","image/webp,image/*,*/*;q=0.8") 
		.header("Accept-Encoding", "gzip, deflate, sdch") 
		.header("Accept-Language", "zh-CN,zh;q=0.8") 
		.header("Connection", "keep-alive") 
//		.header("Host", "221.207.175.178:7989") 
//		.header("Content-Type","image/jpeg") 
//		.header("Referer", "http://221.207.175.178:7989/uaa/personlogin") 
		.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); 
	
		String imgagePath="";
		
		try { 
		Response response = con.ignoreContentType(true).execute(); 
		String imageName = "11.jpg"; 
		File file = new File("D:\\img\\"+imageName); 
		imgagePath = file.getAbsolutePath(); 
		FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath))); 

		out.write(response.bodyAsBytes()); 
		out.close(); 
		return imgagePath; 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return imgagePath;
	}
}
