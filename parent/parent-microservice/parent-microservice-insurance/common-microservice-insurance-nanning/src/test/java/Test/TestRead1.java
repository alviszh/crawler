package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMedical;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\nanning.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		String string = fromObject.getString("body");
		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("dataStores");
		JSONObject fromObject3 = JSONObject.fromObject(string2);
		String string3 = fromObject3.getString("");
		JSONObject fromObject4 = JSONObject.fromObject(string3);
		String string4 = fromObject4.getString("rowSet");
		JSONObject fromObject5 = JSONObject.fromObject(string4);
		String string5 = fromObject5.getString("primary");
		JSONArray fromObject6 = JSONArray.fromObject(string5);
		InsuranceNanNingMedical j =null;
		List<InsuranceNanNingMedical> list = new ArrayList<InsuranceNanNingMedical>();
		for (int i = 0; i < fromObject6.size(); i++) {
			j = new InsuranceNanNingMedical();
			JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
			System.out.println(fromObject7.getString("WEB_V_AC20_AAE216"));
			j.setWEB_V_AC20_AAC001(fromObject7.getString("WEB_V_AC20_AAC001"));
			j.setWEB_V_AC20_AAB004(fromObject7.getString("WEB_V_AC20_AAB004"));
			j.setWEB_V_AC20_AAE003(fromObject7.getString("WEB_V_AC20_AAE003"));
			j.setWEB_V_AC20_AAE140(fromObject7.getString("WEB_V_AC20_AAE140"));
			j.setWEB_V_AC20_AAE210(fromObject7.getString("WEB_V_AC20_AAE210"));
			j.setWEB_V_AC20_AAC130(fromObject7.getString("WEB_V_AC20_AAC130"));
			j.setWEB_V_AC20_AAB001(fromObject7.getString("WEB_V_AC20_AAB001"));
			list.add(j);
		}
		System.out.println(list);
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