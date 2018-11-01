package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class Test3 {

	public static void main(String[] args) throws Exception{
		File file = new File("E:\\Codeimg\\zhuhaiPayInfo.png");  
        String imageBase = encodeImgageToBase64(file);  
        imageBase = imageBase.replaceAll("\r\n","");  
        imageBase = imageBase.replaceAll("\\+","%2B");  
        String httpUrl = "http://apis.baidu.com/apistore/idlocr/ocr";  
        String httpArg = "fromdevice=pc&clientip=10.10.10.0&detecttype=LocateRecognize&languagetype=CHN_ENG&imagetype=1&image="+imageBase;  
        String jsonResult = request(httpUrl, httpArg);  
        System.out.println("返回的结果--------->"+jsonResult);
		
	}
	
	public static String encodeImgageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
	    // 其进行Base64编码处理  
	    byte[] data = null;  
	    // 读取图片字节数组  
	    try {  
	        InputStream in = new FileInputStream(imageFile);  
	        data = new byte[in.available()];  
	        in.read(data);  
	        in.close();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	  
	    // 对字节数组Base64编码  
//	    BASE64Encoder encoder = new BASE64Encoder();  
	    return new String(Base64.encodeBase64(data));// 返回Base64编码过的字节数组字符串  
	}
	
	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();

		try {
			URL url = new URL(httpUrl);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(httpUrl), HttpMethod.POST);
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("apikey", "ObSIrcvdP94ShKYBeXtkYQa0");
			webRequest.setRequestBody(httpArg);
			Page page = webClient.getPage(webRequest);
			InputStream is = page.getWebResponse().getContentAsStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
