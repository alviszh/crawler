package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoAccount;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\2.txt"); 
		String json = txt2String(file);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		String string = fromObject.getString("data");
		JSONObject fromObject3 = JSONObject.fromObject(string);
		String string2 = fromObject3.getString("ret");
		JSONArray fromObject2 = JSONArray.fromObject(string2);
		HousingFundTongLiaoAccount  h = null;
		List<HousingFundTongLiaoAccount> list = new ArrayList<HousingFundTongLiaoAccount>();
		for (int i = 0; i < fromObject2.size(); i=i+7) {
				h = new HousingFundTongLiaoAccount();
				h.setDatea(JSONObject.fromObject(fromObject2.get(i)).getString("word"));
				h.setType(JSONObject.fromObject(fromObject2.get(i+1)).getString("word"));
				h.setMoney(JSONObject.fromObject(fromObject2.get(i+2)).getString("word"));
				h.setInterest(JSONObject.fromObject(fromObject2.get(i+3)).getString("word"));
				h.setFee(JSONObject.fromObject(fromObject2.get(i+4)).getString("word"));
				h.setReason(JSONObject.fromObject(fromObject2.get(i+5)).getString("word"));
				h.setGetType(JSONObject.fromObject(fromObject2.get(i+6)).getString("word"));
				System.out.println(h);
			list.add(h);
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