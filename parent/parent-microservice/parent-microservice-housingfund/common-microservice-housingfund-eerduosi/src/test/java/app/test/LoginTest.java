package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

/**
 * @description:      呼和浩特公积金登录测试类   requestBody的方式  ，总是请求后还在登录页面
 * @author: sln 
 * @date: 2018年1月17日 上午10:16:56 
 */
public class LoginTest {
	public static void main(String[] args) {
		try {
			String url="http://www.ordosgjj.com:8088/(S(iou4ov45shntcl45smbomwij))/login.aspx";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				int statusCode = hPage.getWebResponse().getStatusCode();
				if(500!=statusCode){
					System.out.println("网站可以正常使用");
					//请求验证码图片
					url="http://www.ordosgjj.com:8088/(S(xnuxzwihw4wn1czuax1urrnp))/ValidateCode.aspx";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page=webClient.getPage(webRequest);
					if(page!=null){
						getImagePath(page);
					}
					String code = JOptionPane.showInputDialog("请输入验证码……"); 
					//接下来验证相关信息的输入正确性
					url="http://www.ordosgjj.com:8088/(S(iou4ov45shntcl45smbomwij))/login.aspx";
					//请求参数中涉及到了数据总线
					String requestBody="&__VIEWSTATE=/wEPDwUJODE5NzgyNDkxZGR6Ljw4IyNTHANMy30Vg5+9BqJf4A=="
							+ "&__EVENTVALIDATION=/wEWBQLx8eIuAqXVsrgJAquUkZICApC5tIEFAoznisYG8ldWFd6JnH1SeaSQczB+D6vKwTo="
							+ "&txtUsername=mcx0242"
							+ "&txtUserpass=mcx0242"
							+ "&yzm="+code.trim()+""
							+ "&Button1=";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setRequestBody(requestBody);
					hPage= webClient.getPage(webRequest);							
					if(null!=hPage){
						statusCode = hPage.getWebResponse().getStatusCode();
						if(200==statusCode){
							String html=hPage.asXml();
							System.out.println("模拟点击登陆后获取的页面是："+html);
							//正式写登录代码的时候，此处需要加tracer日志，用于记录调研时候没出现登录错误的结果
							if(html.contains("alert")){
								if(html.contains("验证码输入错误")){
									System.out.println("验证码输入错误");
								}else if(html.contains("密码错误")){
									System.out.println("密码错误");
								}else if(html.contains("用户名不存在，请重新输入")){
									System.out.println("用户名不存在，请重新输入");
								}else if(html.contains("数据库连接失败")){
									System.out.println("数据库连接失败");
								}else{
									System.out.println("出现了其他登录错误，模拟点击后的页面是："+html);
								}
							}else{
								if(html.contains("欢迎登录系统")){
									System.out.println("登录成功");
									//请求用户信息：
									url="http://www.ordosgjj.com:8088/(S(ckko4o45ircvrj45rgdlmhzc))/PersonalAccumulationMoney/Personalinformation.aspx";
									webRequest = new WebRequest(new URL(url), HttpMethod.GET);
									page= webClient.getPage(webRequest);
									if(page!=null){
										html=page.getWebResponse().getContentAsString();
										System.out.println("获取的用户信息的页面是："+html);
										
										Document doc = Jsoup.parse(html);
										String text = doc.getElementById("GridView1").text();
										System.out.println("获取的table中的信息是："+text);
										
										text = doc.getElementById("GridView1").getElementsByTag("tr").text();
										System.out.println("获取的tr中的信息是："+text);
										
//										String attr = doc.getElementById("GridView1").getElementsByTag("tr").get(1).getElementsByTag("td").get(6).getElementsByTag("a").get(0).attr("href");
//										System.out.println("获取的链接参数是："+attr);
										
									}else{
										System.out.println("不能够获取的用户信息的页面");
									}
								}else{
									System.out.println("没有进入登录后的首页面");
								
								}
							}
						}else{
							System.out.println("响应的状态码不是200，请重新登录");
						}
				}else{
					System.out.println("网站升级维护中");
					String msg=page.getWebResponse().getStatusMessage();   //获取得到的网站信息Internal Server Error
					System.out.println("网站升级维护中，响应的信息是："+msg);
				}
				
			}
		}
	} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
	}
}
	
	
	
	////////////////////////////////////////////////////////////////////////
	
	
	//利用IO流保存验证码成功后，返回验证码图片保存路径
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
		//创建验证码图片保存路径
		public static File getImageCustomPath() {
			String path="D:\\img\\";
//			if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
//				path = System.getProperty("user.dir") + "/verifyCodeImage/";
//			} else {
//				path = System.getProperty("user.home") + "/verifyCodeImage/";
//			}
			File parentDirFile = new File(path);
			parentDirFile.setReadable(true); //
			parentDirFile.setWritable(true); //
			if (!parentDirFile.exists()) {
				System.out.println("==========创建文件夹==========");
				parentDirFile.mkdirs();
			}
//			String imageName = UUID.randomUUID().toString() + ".jpg";
			String imageName = "image.jpg";
			File codeImageFile = new File(path + "/" + imageName);
			codeImageFile.setReadable(true); //
			codeImageFile.setWritable(true, false); //
			return codeImageFile;
		}
}
