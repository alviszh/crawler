package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestStr {

	
	public static void main(String[] args) {
		
//		String str = "doDetail('23662460')";
//		
//		String code = str.substring(str.indexOf("\'")+1, str.length()-2).trim();
//		System.out.println(code);
//		
//		int i = 77;
//		System.out.println(i/10+1);
		
		String html = readTxtFile("C:\\home\\sanming.txt");
		
		Document doc = Jsoup.parse(html);
		Element nobr = doc.select("nobr:contains(业务流水号)").first();
		System.out.println(nobr.toString());
		System.out.println("======================================");
		
		String td = nobr.parent().parent().nextElementSibling().text();
		System.out.println(td.toString());
		
	}
	
	
	
	public static String readTxtFile(String filePath) {
		 String text = "";
	     try {
	         File file = new File(filePath);
	         if (file.isFile() && file.exists()) {
	             InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
	             BufferedReader br = new BufferedReader(isr);
	             String lineTxt;
				while ((lineTxt = br.readLine()) != null) {
					text = text+lineTxt;
	             }
	             br.close();
	         } else {
	             System.out.println("文件不存在!");
	         }
	     } catch (Exception e) {
	         System.out.println("文件读取错误!");
	     }
		return text;

	    }
}
