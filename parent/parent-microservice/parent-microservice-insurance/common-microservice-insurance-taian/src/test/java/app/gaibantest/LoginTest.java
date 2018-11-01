package app.gaibantest;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import org.apache.commons.io.Charsets;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;
public class LoginTest {
	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String validateUrl="http://124.130.146.14:8082/hsp/logon.do";
			WebRequest webRequest = new WebRequest(new URL(validateUrl),HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("method", "writeMM2Temp"));
			webRequest.getRequestParameters().add(new NameValuePair("_xmlString",
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s tempmm=\"713616l\"/></p>"));
			webRequest.getRequestParameters().add(new NameValuePair("_random", "0.20038958982881083"));
			Page page = webClient.getPage(webRequest);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);

			// 获取图片验证码
			String imageUrl = "http://124.130.146.14:8082/hsp/authcode";
			webRequest = new WebRequest(new URL(imageUrl), HttpMethod.GET);
			page = webClient.getPage(webRequest);
			String path = "D:\\img";
			getImagePath(page, path);
			String code = JOptionPane.showInputDialog("请输入验证码……");
			System.out.println("识别出来的图片验证码是---------" + code);
			//如下获取的版本号不对
			String appversion = encrypted("98533987291166723","1.1.11");
			System.out.println(appversion);
			
			webRequest = new WebRequest(new URL("http://124.130.146.14:8082/hsp/logon.do"),
					HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("method", "doLogon"));
			webRequest.getRequestParameters().add(new NameValuePair("_xmlString",
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s userid=\"370902199112251848\"/><s usermm=\"4b75ac6df55198690e4669d753841049\"/><s authcode=\""
							+ code
//							+ "\"/><s yxzjlx=\"A\"/><s appversion=\""+appversion+"\"/><s dlfs=\"1\"/></p>"));
			+ "\"/><s yxzjlx=\"A\"/><s appversion=\"81102198533709667232251339822518481729\"/><s dlfs=\"1\"/></p>"));
			webRequest.getRequestParameters().add(new NameValuePair("_random", "0.20038958982881083"));
			page = webClient.getPage(webRequest);
			html = page.getWebResponse().getContentAsString();
			System.out.println(html);
			if(html.contains("true")){
				System.out.println("登录成功");
			    String usersession_uuid = JSONObject.fromObject(html).getString("__usersession_uuid");
				//获取个人信息
				String url="http://124.130.146.14:8082/hsp/hspUser.do";
				webRequest = new WebRequest(new URL(url),HttpMethod.POST);
				//后边拼接的_laneID写死貌似也不影响
				String requestBody="method=fwdQueryPerInfo&_random=0.9962615729836675&__usersession_uuid="+usersession_uuid+"&_laneID=de1a3292-c62d-4845-9f4c-63239dbfc3b6";
				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest);
				html = page.getWebResponse().getContentAsString();
				System.out.println(html);
			}else{
				System.out.println("登录失败");
			}
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
	public static String encrypted(String appverson,String yhm) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("login.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("onLogin","",appverson,yhm);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
}
