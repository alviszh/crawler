package test;

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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class Qzb {

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//			String url2 = "http://118.122.8.57:81/ispobs/Forms/SysFiles/Sys_Yzm.aspx";
//			WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
//			Page page = webClient.getPage(webRequest2);
//			if (null != page) {
//				getImagePath(page, "");
//			}
			String url = "http://login.189.cn/login";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage html = webClient.getPage(webRequest);
			HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
			HtmlTextInput txtCaptcha2 = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtCaptcha']");
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
			HtmlImage image = html.getFirstByXPath("//img[@id='imgCaptcha']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 
			String txtCaptcha = JOptionPane.showInputDialog("请输入验证码……"); 

			username.setText("18995154123");
			passwordInput.setText("795372");
			txtCaptcha2.setText(txtCaptcha);

			
			HtmlPage htmlpage = button.click();
			String asXml = htmlpage.asXml();
			System.out.println(asXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 指定图片验证码保存的路径和随机生成的名称，拼接在一起
	// 利用IO流保存验证码成功后，将完整路径信息一并返，
	public static String getImagePath(Page page, String imagePath) throws Exception {
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File("D://" + imageName);
		codeImageFile.setReadable(true);
		codeImageFile.setWritable(true, false);

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
