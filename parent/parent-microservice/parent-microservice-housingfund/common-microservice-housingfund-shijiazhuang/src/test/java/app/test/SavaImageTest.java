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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;


/**
 * @description:爬取泰安社保的时候将page所代表的图片用流的 方式保存到本地，故决定用这个方式再来尝试以石家庄为模板的登录
 * @author: sln 
 * @date: 2017年12月27日 下午5:32:26 
 */
public class SavaImageTest {
	public static void main(String[] args) {
		try {
			String url="http://www.sjzgjj.cn/wsyyt/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				String html=hPage.getWebResponse().getContentAsString();
				System.out.println("登录页面html为："+html);
				//此处请求图片验证码链接
				url="http://www.sjzgjj.cn/wsyyt/vericode.jsp";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				getImagePath(page);
				HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']"); 
				HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']"); 
				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']"); 
				HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']"); 
				loginName.setText("130181199004034827"); 
				loginPassword.setText("123123"); 	
				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
				validateCode.setText(inputValue); 	
				HtmlPage logonPage = submitbt.click(); 
				System.out.println("点击登陆后获取的页面是："+logonPage.asXml());
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
