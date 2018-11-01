package Test.md;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		String substring = json.substring(3);
		System.out.println(substring);
		JSONObject fromObject = JSONObject.fromObject(substring);
		String string = fromObject.getString("flowUseList");
		System.out.println(string);
		if(string.contains("pay"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string2 = fromObject2.getString("totalLines");
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