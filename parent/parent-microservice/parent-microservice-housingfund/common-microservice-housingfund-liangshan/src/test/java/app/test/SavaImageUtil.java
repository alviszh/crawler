package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.gargoylesoftware.htmlunit.Page;

/**
 * @description:
 * @author: sln 
 * @date: 2018年2月7日 下午2:11:04 
 */
public class SavaImageUtil {
	//指定图片验证码保存的路径和随机生成的名称，拼接在一起
	//利用IO流保存验证码成功后，将完整路径信息一并返，
public static  String getImagePath(Page page) throws Exception{
		String imagePath="D://img";
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
	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
	        	outputStream.write(temp);   // 边读边写  
	        }  
	        outputStream.close();  
	        inputStream.close();   // 关闭输入输出流  
	    }
		return imgagePath;
	}
}
