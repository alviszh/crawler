package TestChi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;

import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zhu.txt"); 
		String json = txt2String(file);
		System.out.println(json.substring(3));
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		System.out.println(fromObject);
		String string = fromObject.getString("words_result");
		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		HousingFundZhuMaDianUserInfo h = new HousingFundZhuMaDianUserInfo();
		
		h.setCompanyNum(JSONObject.fromObject(fromObject2.get(0)).getString("words").substring(5));
		h.setCompany(JSONObject.fromObject(fromObject2.get(1)).getString("words").substring(5));
		h.setPersonalNum(JSONObject.fromObject(fromObject2.get(2)).getString("words").substring(5));
		h.setName(JSONObject.fromObject(fromObject2.get(3)).getString("words").substring(5));
		h.setStatus(JSONObject.fromObject(fromObject2.get(4)).getString("words").substring(5));
		h.setCardNum(JSONObject.fromObject(fromObject2.get(5)).getString("words").substring(5));
		h.setPhone(JSONObject.fromObject(fromObject2.get(6)).getString("words").substring(4));
		h.setAddr(JSONObject.fromObject(fromObject2.get(7)).getString("words").substring(5));
		h.setBank(JSONObject.fromObject(fromObject2.get(8)).getString("words").substring(4));
		h.setBankNum(JSONObject.fromObject(fromObject2.get(9)).getString("words").substring(5));
		h.setOpenDate(JSONObject.fromObject(fromObject2.get(10)).getString("words").substring(5));
		
		System.out.println(h);
		
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