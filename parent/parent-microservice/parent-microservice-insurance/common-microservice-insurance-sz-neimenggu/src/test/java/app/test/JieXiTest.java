package app.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月28日 下午4:23:29 
 */
public class JieXiTest {
	public static void main(String[] args) throws Exception, FileNotFoundException {
		String html="";
		String encoding = "GBK";
		File file = new File("E:\\Noname5.html"); 
		if (file.isFile() && file.exists()) { //判断文件是否存在
	        InputStreamReader read = new InputStreamReader(
	                new FileInputStream(file), encoding);//考虑到编码格式
	        BufferedReader bufferedReader = new BufferedReader(read);
	        String lineTxt = null;
	        while ((lineTxt = bufferedReader.readLine()) != null) {
//	               System.out.println(lineTxt);  
	        	html+=lineTxt;
	         }       
	        read.close();
	        
	        
	        System.out.println("最终："+html);
	        
	        Document doc = Jsoup.parse(html);
	        Elements elementsByClass = doc.getElementById("tab_cont_box").getElementsByClass("pas_table taocan_table");
	        for (Element element : elementsByClass) {
				System.out.println("获取的table中的内容是："+element);
			}
	    } else {
	        System.out.println("找不到指定的文件");
	    }
	}
}
