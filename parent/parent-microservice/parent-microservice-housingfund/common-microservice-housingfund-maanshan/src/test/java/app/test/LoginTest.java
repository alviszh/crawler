package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;

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
 * @date: 2018年3月14日 下午2:27:17 
 */
public class LoginTest {
	public static void main(String[] args) throws Exception { 
		String loginUrl="http://211.141.223.135/maswt/?sc=ff80808160c5f6f20160cb3c0e511061&ac=340500000000";
		WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		if(null!=hPage){
			//此处请求图片验证码链接
			String vericodeUrl="http://211.141.223.135/maswt/vericode.jsp";
			webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			//利用io流保存图片验证码
			String path = "D:\\img"; 
			getImagePath(page, path); 
			HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='certinum']"); 
			HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@id='pwd']"); 
			HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='vericode']"); 
			HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']"); 
			loginName.setText("34050319890511002X"); 
			loginPassword.setText("140530"); 
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			validateCode.setText(code); 	
			HtmlPage logonPage= submitbt.click(); 
			if(null!=logonPage){
				String html=logonPage.asXml();
				System.out.println("模拟点击登陆后获取的页面是："+html);
				if(html.contains("欢迎您")){
					System.out.println("登录成功");
				}
			}
		}
	}
	
	public static String getImagePath(Page page,String imagePath) throws Exception{ 
		File parentDirFile = new File(imagePath); 
		parentDirFile.setReadable(true); 
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) { 
		System.out.println("==========创建文件夹=========="); 
		parentDirFile.mkdirs(); 
		} 
		String imageName ="11.jpg"; 
		File codeImageFile = new File(imagePath + "/" + imageName); 
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false); 
		//////////////////////////////////////// 

		String imgagePath = codeImageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream(); 
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) { 
		int temp = 0; 
		while ((temp = inputStream.read()) != -1) { // 开始拷贝 
		outputStream.write(temp); // 边读边写 
		} 
		outputStream.close(); 
		inputStream.close(); // 关闭输入输出流 
		} 
		return imgagePath; 
		}
}
