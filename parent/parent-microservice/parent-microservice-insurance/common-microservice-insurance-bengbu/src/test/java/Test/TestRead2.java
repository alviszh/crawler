package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMedical;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\account.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
//		System.out.println(fromObject);
		String string = fromObject.getString("data");
//		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		InsuranceBengBuMedical in =new InsuranceBengBuMedical();
		List<InsuranceBengBuMedical> list =new ArrayList<InsuranceBengBuMedical>();
		for (int i = 0; i < fromObject2.size(); i++) {
//			System.out.println(fromObject2.get(i));
			in =new InsuranceBengBuMedical();
			JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
			in.setDatea(fromObject3.getString("aae037"));
			in.setInDate(fromObject3.getString("aae037"));
			in.setPersonalMoney(fromObject3.getString("aae082"));
			in.setCompanyMoney(fromObject3.getString("aae081"));
			in.setBase(fromObject3.getString("aac150"));
			in.setFlag(fromObject3.getString("aae925"));
			in.setSumMoney(fromObject3.getString("aae080"));
			in.setType(fromObject3.getString("aae140mc"));
			in.setTaskid("");
			list.add(in);
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