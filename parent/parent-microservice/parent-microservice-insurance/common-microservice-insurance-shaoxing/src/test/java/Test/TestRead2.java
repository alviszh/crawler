package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\yl.txt"); 
		String json = txt2String(file);
//		System.out.println(json.substring(4,json.length()-1));
		JSONObject fromObject = JSONObject.fromObject(json.substring(4,json.length()-1));
		String string = fromObject.getString("s0012Resdatas");
//		System.out.println(string);
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("rowList");
//		System.out.println(string2);
		JSONArray fromObject3 = JSONArray.fromObject(string2);
		List<InsuranceShaoXingEndowment> list = new ArrayList<InsuranceShaoXingEndowment>();
		InsuranceShaoXingEndowment in =null;
		for (int i = 0; i < fromObject3.size(); i++) {
//			System.out.println(fromObject3.get(i));
			in = new InsuranceShaoXingEndowment();
			JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(i));
			in.setStatus(fromObject4.getString("xzdm"));
			in.setBase(fromObject4.getString("jfdc"));
			in.setGetMoney(fromObject4.getString("dzqk"));
			in.setPayCompany(fromObject4.getString("dwyj"));
			in.setPayPersonal(fromObject4.getString("gryj"));
			in.setTime(fromObject4.getString("yjny"));
			in.setTaskid("");
			list.add(in);
		}
		System.out.println(list);
//		String substring = string2.substring(1, string2.length()-1);
//		System.out.println(substring);
//		JSONObject fromObject3 = JSONObject.fromObject(substring);
//		String csrq = fromObject3.getString("csrq");
//		InsuranceShaoXingUserInfo i = new InsuranceShaoXingUserInfo();
//		i.setBirth(csrq);
//		i.setCompanyNum(fromObject3.getString("dwdm"));
//		i.setCompany(fromObject3.getString("dwmc"));
//		i.setName(fromObject3.getString("xm"));
//		i.setSex(fromObject3.getString("xb"));
//		i.setIDNum(fromObject3.getString("zjhm"));
//		i.setStatus(fromObject3.getString("zjlx"));
//		i.setGrbh(fromObject3.getString("grbh"));
//		i.setTaskid("");
//		System.out.println(i);
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