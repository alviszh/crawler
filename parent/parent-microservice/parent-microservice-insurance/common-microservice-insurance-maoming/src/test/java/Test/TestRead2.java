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

import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingEndowment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMedical;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\yl.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		String string = fromObject.getString("rows");
		
//		System.out.println(string);
		JSONArray fromObject2 = JSONArray.fromObject(string);
		InsuranceMaoMingEndowment in = null;
		List<InsuranceMaoMingEndowment> list = new ArrayList<InsuranceMaoMingEndowment>();
		for (int i = 0; i < fromObject2.size(); i++) {
			in = new InsuranceMaoMingEndowment();
//			System.out.println(fromObject2.get(i));
			JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
			in.setCompany(fromObject3.getString("aab069"));
			in.setStatus(fromObject3.getString("aae140"));
			in.setDatea(fromObject3.getString("aae079"));
			in.setBase(fromObject3.getString("aae180"));
			in.setPersonalpPay(fromObject3.getString("aae022"));
			in.setCompanyPay(fromObject3.getString("aae020"));
			in.setCompanyMoney(fromObject3.getString("aae056"));
			in.setSum(fromObject3.getString("aae058"));
			in.setFlag(fromObject3.getString("aae078"));
			in.setGetDate(fromObject3.getString("aae002"));
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