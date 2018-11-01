package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		String[] split = json.split("<AREA_CODE>");
		String[] split2 = split[1].split("<");
		System.out.println(split2[0]);
		
//		StringBuffer buf=new StringBuffer();
//		buf.append("<!DOCTYPE html><html><head></head><body>");
//		buf.append(json);
//		buf.append("</body></html>");
		Document doc = Jsoup.parse(json);
//		Elements elementsByTag = doc.getElementsByTag("AREA_CODE");
//		System.out.println(elementsByTag.text());
		
		
		int indexOf = json.indexOf("PRODTYPE");
		String substring2 = json.substring(indexOf+10, indexOf+17);
		System.out.println(substring2);
		Element elementById = doc.getElementById("qry_num");
		System.out.println(elementById);
		String string = elementById.toString();
		int area_codel = string.indexOf("area_code");
		int prod_typel = string.indexOf("prod_type");
		int prod_namel = string.indexOf("prod_name");
		String substring = string.substring(area_codel, prod_typel);
		String area_code = substring.substring(11,substring.length()-2);
		
		String substring1 = string.substring(prod_typel, prod_namel);
		String prod_type = substring1.substring(11,substring1.length()-2);
		System.out.println(prod_type);
		
		
	}
	public static String txt2String(File file) { 
		StringBuilder result = new StringBuilder(); 
		try { 
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); 
		String s = null; 
		while ((s = br.readLine()) != null) { 
		result.append(System.lineSeparator() + s); 
		} 
		br.close(); 
		} catch (Exception e) { 
		e.printStackTrace(); 
		} 
		return result.toString(); 
		}

		
}