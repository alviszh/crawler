package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\html.txt"); 
		String json = txt2String(file);
//		System.out.println(json.substring(3));
		String substring = json.substring(3);
		String substring2 = substring.substring(1, substring.length()-1);
//		System.out.println(substring2);
		substring2 = substring2.replace("\\", "");
//		System.out.println(substring2);
		JSONObject fromObject = JSONObject.fromObject(substring2);
		String string = fromObject.getString("list");
//		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		for (int i = 0; i < fromObject2.size(); i++) {
//			System.out.println(fromObject2.toString());
			JSONArray fromObject3 = JSONArray.fromObject(fromObject2.toString());
			Object object = fromObject3.get(i);
//			System.out.println(object);
			JSONObject fromObject4 = JSONObject.fromObject(object);
			String string2 = fromObject4.getString("JZBH");
			System.out.println(string2);
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