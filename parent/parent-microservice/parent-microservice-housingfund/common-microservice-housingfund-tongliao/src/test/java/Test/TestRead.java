package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json.substring(3));
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		String string = fromObject.getString("results");
		JSONArray fromObject2 = JSONArray.fromObject(string);
		HousingFundTongLiaoUserInfo h = new HousingFundTongLiaoUserInfo();
		List<HousingFundTongLiaoUserInfo> list = new ArrayList<HousingFundTongLiaoUserInfo>();
		for (int i = 0; i < fromObject2.size(); i++) {
			h.setName(JSONObject.fromObject(fromObject2.get(i)).getString("xingming"));
			h.setBirthday(JSONObject.fromObject(fromObject2.get(i)).getString("csny"));
			h.setSex(JSONObject.fromObject(fromObject2.get(i)).getString("xingbie"));
			h.setCardType(JSONObject.fromObject(fromObject2.get(i)).getString("zjlx"));
			h.setIdcardNum(JSONObject.fromObject(fromObject2.get(i)).getString("zjhm"));
			h.setPhone(JSONObject.fromObject(fromObject2.get(i)).getString("sjhm"));
			h.setNum(JSONObject.fromObject(fromObject2.get(i)).getString("gddhhm"));
			h.setCode(JSONObject.fromObject(fromObject2.get(i)).getString("yzbm"));
			h.setGetMoney(JSONObject.fromObject(fromObject2.get(i)).getString("jtysr"));
			h.setAddr(JSONObject.fromObject(fromObject2.get(i)).getString("jtzz"));
			h.setMarry(JSONObject.fromObject(fromObject2.get(i)).getString("hyzk"));
			h.setLoan(JSONObject.fromObject(fromObject2.get(i)).getString("dkqk"));
			h.setCardNum(JSONObject.fromObject(fromObject2.get(i)).getString("grzh"));
			h.setCardStatus(JSONObject.fromObject(fromObject2.get(i)).getString("grzhzt"));
			h.setFee(JSONObject.fromObject(fromObject2.get(i)).getString("grzhye"));
			h.setOpenDate(JSONObject.fromObject(fromObject2.get(i)).getString("djrq"));
			h.setCompany(JSONObject.fromObject(fromObject2.get(i)).getString("dwmc"));
			h.setPayRatio(JSONObject.fromObject(fromObject2.get(i)).getString("jcbl"));
			h.setPayBase(JSONObject.fromObject(fromObject2.get(i)).getString("grjcjs"));
			h.setMonthPay(JSONObject.fromObject(fromObject2.get(i)).getString("yjce"));
			h.setBank(JSONObject.fromObject(fromObject2.get(i)).getString("grckzhkhyhmc"));
			h.setSetNum(JSONObject.fromObject(fromObject2.get(i)).getString("grckzhhm"));
			list.add(h);
			System.out.println(h);
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