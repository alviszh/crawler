package TestChi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;

import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zhushui.txt"); 
		String json = txt2String(file);
		System.out.println(json.substring(3));
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		System.out.println(fromObject);
		String string = fromObject.getString("words_result");
		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		
		
		
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