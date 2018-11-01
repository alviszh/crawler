package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * @description:该测试类只是能将图片保存到本地，但是并不能和htmlunit融合使用
 * @author: sln 
 * @date: 2017年12月22日 上午10:19:03 
 */
public class SaveImageTest {
	public static void main(String[] args) {
		getImg();
	}
	private static String getImg() { 
		String url="http://124.130.146.14:8002/hso/genAuthCode2?_=0.11931910041354588";

		Connection con = Jsoup.connect(url).header("Accept","image/webp,image/*,*/*;q=0.8") 
		.header("Accept-Encoding", "gzip, deflate, sdch") 
		.header("Accept-Language", "zh-CN,zh;q=0.8") 
		.header("Connection", "keep-alive") 
//		.header("Host", "221.207.175.178:7989") 
		.header("Content-Type","image/jpeg") 
//		.header("Referer", "http://221.207.175.178:7989/uaa/personlogin") 
		.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); 

		try { 
			Response response = con.ignoreContentType(true).execute(); 
			String imageName = "11.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			String imgagePath = file.getAbsolutePath(); 
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath))); 
	
			out.write(response.bodyAsBytes()); 
			out.close(); 
			return imgagePath; 
			} catch (IOException e) { 
			e.printStackTrace(); 
			} 

		return null; 
		} 

}
