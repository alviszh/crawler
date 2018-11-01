package app.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestParser {
	
	public static void main(String[] args) {
		String text = readTxtFile("D:\\img\\新建文本文档.html");
		parser(text);
	}

	
	private static void parser(String text) {
		Document doc = Jsoup.parse(text);
		Element radio = doc.select("input[value=unband]").first();
		System.out.println(radio);
	}


	/**
	 * @param filePath
	 * @return
	 */
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
