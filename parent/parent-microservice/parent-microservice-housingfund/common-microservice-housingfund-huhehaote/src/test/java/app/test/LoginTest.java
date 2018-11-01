package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2018年1月3日 下午5:29:23 
 */
public class LoginTest {
	public static void main(String[] args) {
		try {
			String loginUrl="http://222.74.26.19/hhhtwsyyt/";
			WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				//此处请求图片验证码链接
				String vericodeUrl="http://222.74.26.19/hhhtwsyyt/vericode.jsp";
				webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				//利用io流保存图片验证码
				getImagePath(page);
				HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']"); 
				HtmlTextInput name = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='accName']");
				HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']"); 
				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']"); 
				HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']"); 
				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
				validateCode.setText(inputValue); 	
				loginName.setText("150929199304182129"); 
				name.setText("银晓娟");
				loginPassword.setText("239673"); 	
				validateCode.setText(inputValue); 	
				HtmlPage logonPage= submitbt.click(); 
				if(null!=logonPage){
					String html=logonPage.asXml();
//					System.out.println("模拟点击登陆后获取的页面是："+html);
					if(html.contains("欢迎您")){
				    	System.out.println("登录成功，欢迎来到首页面");
				 		String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
				 		System.out.println("存储起来的cookie是："+cookieString);
					}else{
						Document doc = Jsoup.parse(html);
						String errorMsg= doc.getElementsByClass("WTLoginError").get(0).text();  //获取页面中可能出现的错误信息
						//页面信息提示中可能存在返回这个字段，截取掉
//						String str="操作失败:进行身份校验时出错:密码错误,请检查! 返回";
						String[] split = errorMsg.split(" ");
						errorMsg=split[0];
						System.out.println("获取的错误信息是："+errorMsg);
						//通过调研，发现图片验证码输入错误，这几个公积金网站的提示都是一样的
						if(errorMsg.contains("您输入的验证码与图片不符")){
							System.out.println("操作失败:进行身份校验时出错:您输入的验证码与图片不符");
						}else if(errorMsg.contains("录入姓名与根据身份证查出的姓名不符")){
							System.out.println("录入姓名与根据身份证查出的姓名不符");
						}else if(errorMsg.contains("身份证转换错误")){
							System.out.println("身份证转换错误");
						}else if(errorMsg.contains("录入密码不正确")){
							System.out.println("录入密码不正确");
						}else if(errorMsg.contains("系统日终尚未结束")){
							System.out.println("进行身份校验时出错:系统日终尚未结束，请稍后再试");
						}else{
							System.out.println("出现了其他的登录错误"+errorMsg);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}
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
		String path="D:/img";


		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
//		String imageName = UUID.randomUUID().toString() + ".jpg";
		String imageName = "yanzhengma.jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}
}
