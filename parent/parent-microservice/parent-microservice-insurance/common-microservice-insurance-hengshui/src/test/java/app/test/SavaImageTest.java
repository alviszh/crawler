package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.swing.JOptionPane;


import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

/**
 * @description:爬取泰安社保的时候将page所代表的图片用流的 方式保存到本地，故决定用这个方式再来尝试衡水的登录
 * @author: sln 
 * @date: 2017年12月26日 下午4:32:26 
 */
public class SavaImageTest {
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
				//此处请求图片验证码链接
				url="http://ggfw.hsrsw.gov.cn:8001/ggfwweb/captchaimg?tm=1514277408596";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				hPage = webClient.getPage(webRequest);
				getImagePath(hPage);
				
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
	
	public static File getImageCustomPath() {
		String path="";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}
	
	
	public static String getImagePath(Page page) throws Exception{
		File imageFile = getImageCustomPath();
		String imgagePath = imageFile.getAbsolutePath(); 
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
		return imgagePath;
	}
}
