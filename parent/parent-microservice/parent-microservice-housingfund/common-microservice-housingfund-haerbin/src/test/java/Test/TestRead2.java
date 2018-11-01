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

import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinAccount;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.microservice.dao.repository.crawler.housing.haerbin.HousingHaErBinUserInfoRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\haliu.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
		System.out.println(fromObject);
		String string = fromObject.getString("data");
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("data");
		System.out.println(string2);
		JSONArray fromObject3 = JSONArray.fromObject(string2);
		HousingFundHaErBinAccount h = null;
		List<HousingFundHaErBinAccount> list= new ArrayList<HousingFundHaErBinAccount>();
		for (int i = 0; i < fromObject3.size(); i++) {
			h = new HousingFundHaErBinAccount();
//			System.out.println(fromObject3.get(i));
			JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(i));
			h.setDatea(fromObject4.getString("transdate"));
			h.setDescr(fromObject4.getString("freeuse4"));
			h.setMoney(fromObject4.getString("amt4"));
			h.setFee(fromObject4.getString("basenumber"));
			if(fromObject4.getString("freeuse1").contains("0"))
			{
				h.setStatus("正常");
			}
			else
			{
				h.setStatus("非正常");
			}
			h.setStartDate(fromObject4.getString("begindatec"));
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

	
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}
		
}