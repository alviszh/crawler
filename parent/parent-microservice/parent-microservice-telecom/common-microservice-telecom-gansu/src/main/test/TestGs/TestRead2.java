package TestGs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json.substring(3));
		String substring = json.substring(3);
		JSONObject fromObject = JSONObject.fromObject(substring);
		String string = fromObject.getString("user");
		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String cardNumber = fromObject2.getString("cardNumber");
		String cardType = fromObject2.getString("cardType");
		String contactphone = fromObject2.getString("contactphone");
		String custname = fromObject2.getString("custname");
		String orgname = fromObject2.getString("orgname");
		String orgcode = fromObject2.getString("orgcode");
		
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