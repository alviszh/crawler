package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url="https://creditcard.ecitic.com/citiccard/ucweb/entry.do?func=entryebank&ebankPage=mainpage";
		Page page = webClient.getPage(url);
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element elementById = doc.getElementById("img_code");
//		System.out.println(elementById.toString());
		String code =null;
		if(elementById.toString().contains("style=\"display:none\""))
		{
			String imgUrl="https://creditcard.ecitic.com/citiccard/ucweb/newvalicode.do?time=1543218849691";
			WebRequest webRequest = new WebRequest(new URL(imgUrl), HttpMethod.GET);
			Page html = webClient.getPage(webRequest);
			String imgagePath=getImagePath(html,"/opt/image");
			ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
			code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1004");
			System.out.println(code);
		}
//		{"resultCode":"0000000","resultDesc":"短信发送成功"}   {"resultCode":"0000001","resultDesc":"请输入4位图形验证码"}
		String loginurl3 = "https://creditcard.ecitic.com/citiccard/ucweb/getsms.do?&timestamp1543217717367";
		WebRequest webRequest = new WebRequest(new URL(loginurl3), HttpMethod.POST);
		webRequest.setRequestBody("phone=13520800817&imgValidCode="+code);
		Page html = webClient.getPage(webRequest);
//		System.out.println(html.getWebResponse().getContentAsString());
//		https://creditcard.ecitic.com/citiccard/ucweb/registrycheck.do?&timestamp1543218056296
//		phone=13261252572&smsCode=237132  {"resultCode":"0000032","resultDesc":"短信验证码已失效，请重新获取"}
		if(html.getWebResponse().getContentAsString().contains("短信发送成功"))
		{
			String urlLogin="https://creditcard.ecitic.com/citiccard/ucweb/registrycheck.do?&timestamp1543221006458";
			webRequest = new WebRequest(new URL(urlLogin), HttpMethod.POST);
			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			webRequest.setRequestBody("phone=13520800817&smsCode="+inputValue);
			Page html2 = webClient.getPage(webRequest);
			System.out.println(html2.getWebResponse().getContentAsString());
			
			String url2="https://creditcard.ecitic.com/citiccard/ucweb/tologin.do";
			webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
			Page page2 = webClient.getPage(webRequest);
			if(page2.getWebResponse().getContentAsString().contains("请输入登录密码"))
			{
				//密码加密
				String str=DigestUtils.md5Hex("12qwaszx");
				String url3="https://creditcard.ecitic.com/citiccard/ucweb/newlogin.do?&timestamp1543220152915";
				webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
				webRequest.setRequestBody("mm="+str);
				Page html3 = webClient.getPage(webRequest);
				System.out.println(html3.getWebResponse().getContentAsString());
				
				if(html3.getWebResponse().getContentAsString().contains("验证成功"))
				{
					String url4="https://creditcard.ecitic.com/citiccard/newonline/settingManage.do?func=queryUserInfo&";
					webRequest = new WebRequest(new URL(url4), HttpMethod.POST);
					Page html4 = webClient.getPage(webRequest);
					System.out.println(html4.getWebResponse().getContentAsString());
				}
			}
		}
	}
	
	public static  String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
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
