package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\2.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		
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