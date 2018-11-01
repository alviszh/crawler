package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年11月27日 下午5:39:27 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JiexiTest {
	public static void main(String[] args) throws Exception {
		String encoding = "GBK";
	    File file = new File("E:\\jiexi.html");
	    if (file.isFile() && file.exists()) { //判断文件是否存在
	        InputStreamReader read = new InputStreamReader(
	                new FileInputStream(file), encoding);//考虑到编码格式
	        BufferedReader bufferedReader = new BufferedReader(read);
	        String lineTxt = null;
	        while ((lineTxt = bufferedReader.readLine()) != null) {
	        	System.out.println(lineTxt);
	        }  
	    	//解析数据：
            Document doc = Jsoup.parse(lineTxt);
            String text =doc.select("table[align='center']").text();
         	   System.out.println("获取到的数据是:"+text);
	        read.close();
	    } else {
	        System.out.println("找不到指定的文件");
	    }
	}
}
