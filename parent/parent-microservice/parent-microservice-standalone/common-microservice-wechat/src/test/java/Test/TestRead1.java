package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		String[] split = json.split("/head");
//		System.out.println(split[0]);
		String[] split2 = split[0].split("head");
//		System.out.println(split2[1]);
		String substring = split2[1].substring(23, 156);
//		System.out.println(substring);
		String replace = substring.replace("amp;", "");
		System.out.println(replace);
		String replaceAll = replace.replaceAll("tip=1", "tip=0");
		System.out.println(replaceAll);
		
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