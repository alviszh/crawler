package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.codec.digest.DigestUtils;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class QiZhongBinTest {
	public static void main(String[] args) {
		try {
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// 图片请求
			String loginurl3 = "https://creditcard.ecitic.com/citiccard/ucweb/newvalicode.do";
			WebRequest webRequest = new WebRequest(new URL(loginurl3), HttpMethod.GET);
			Page html = webClient.getPage(webRequest);
			getImagePath(html, "D:\\img");
			// 验证登录信息的链接：
			String code = JOptionPane.showInputDialog("请输入验证码……");
			String loginUrl0 = "https://creditcard.ecitic.com/citiccard/ucweb/login.do";
			
			String str=DigestUtils.md5Hex("kbaoxiao5746"); 
			
			String body = "{\"loginType\":\"01\"," + "\"memCode\":\""+str+"\","
					+ "\"isBord\":\"false\",\"phone\":\"15010935861\","
					+ "\"source\":\"PC\",\"page\":\"new\",\"valiCode\":\"" + code.trim() + "\"}";
			webRequest = new WebRequest(new URL(loginUrl0), HttpMethod.POST);
			webRequest.setAdditionalHeader("Content-Type", "application/json");
			webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Origin", "https://creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Referer", "https://creditcard.ecitic.com/citiccard/ucweb/entry.do");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36");
			webRequest.setRequestBody(body);
			Page page0 = webClient.getPage(webRequest);
			String contentAsString0 = page0.getWebResponse().getContentAsString();
			System.out.println("验证登陆是否成功的响应：" + contentAsString0);
			if (contentAsString0.contains("验证成功")) {
				String url = "https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do";
				webClient.getOptions().setJavaScriptEnabled(false);
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				html = webClient.getPage(webRequest);
				String contentAsString = html.getWebResponse().getContentAsString();
				System.out.println("发送验证码前提页面：" + contentAsString);

				if (contentAsString.contains("免费获取")) {
					url = "https://creditcard.ecitic.com/citiccard/ucweb/sendSms.do";
					webClient.getOptions().setJavaScriptEnabled(false);
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Origin", "https://creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Referer",
							"https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do");
					webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
					webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
					html = webClient.getPage(webRequest);
					contentAsString = html.getWebResponse().getContentAsString();
					System.out.println("发送验证码：" + contentAsString);

					String code1 = JOptionPane.showInputDialog("请输入短信验证码……");
					url = "https://creditcard.ecitic.com/citiccard/ucweb/checkSms.do?date=1527502348409";
					webClient.getOptions().setJavaScriptEnabled(false);
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Origin", "https://creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Referer",
							"https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do");
					webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
					webRequest.setAdditionalHeader("Content-Type", "application/json");
					String requestBody = "{\"smsCode\":\"" + code1 + "\"}";
					webRequest.setRequestBody(requestBody);
					html = webClient.getPage(webRequest);
					contentAsString = html.getWebResponse().getContentAsString();
					System.out.println("验证验证码：" + contentAsString);
					
					url="https://creditcard.ecitic.com/citiccard/newonline/myaccount.do?func=mainpage";
					webClient.getOptions().setJavaScriptEnabled(false);
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Referer",
							"https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do");
					
					Page page = webClient.getPage(webRequest);
					System.out.println(page.getWebResponse().getContentAsString());
					if(page.getWebResponse().getContentAsString().contains("首页-我的账户"))
					{
						url="https://creditcard.ecitic.com/citiccard/newonline/settingManage.do?func=queryUserInfo&";
						Page page2 = webClient.getPage(url);
						System.out.println(page2.getWebResponse().getContentAsString());
						if(page2.getWebResponse().getContentAsString().contains("用户个人信息查询 成功")){
							System.out.println("44444444444444444444444444");
						}
					}
					
				}
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getImagePath(Page page, String imagePath) throws Exception {
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = "11.jpg";
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
