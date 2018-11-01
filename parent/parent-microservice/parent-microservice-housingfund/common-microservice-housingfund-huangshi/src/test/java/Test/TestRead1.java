package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\aspay.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
//		System.out.println(fromObject.getString("results"));
		String string = fromObject.getString("results");
		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		for (int i = 0; i < fromObject2.size(); i++) {
			System.out.println(fromObject2.get(i));
		}
		
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