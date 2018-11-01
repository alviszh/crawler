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


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		String string = fromObject.getString("results");
		JSONArray fromObject3 = JSONArray.fromObject(string);
		HousingFundTongLiaoAccount  h = null;
		List<HousingFundTongLiaoAccount> list = new ArrayList<HousingFundTongLiaoAccount>();
		for (int i = 0; i < fromObject3.size(); i++) {
			JSONObject fromObject2 = JSONObject.fromObject(fromObject3.get(i));
//			System.out.println(fromObject2);
			h = new HousingFundTongLiaoAccount();
			h.setDatea(fromObject2.getString("jzrq"));
			h.setType(fromObject2.getString("gjhtqywlx"));
			h.setMoney(fromObject2.getString("fse"));
			h.setInterest(fromObject2.getString("fslxe"));
			h.setFee(fromObject2.getString("grzhye"));
			h.setReason(fromObject2.getString("tqyy"));
			h.setGetType(fromObject2.getString("tqfs"));
			h.setTaskid("");
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