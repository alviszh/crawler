package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouAccount;

import app.common.WebParam;
import net.sf.json.JSONObject;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zhuzhouliu.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		JSONObject fromObject = JSONObject.fromObject(json.substring(3));
//		System.out.println(fromObject);
		String string = fromObject.getString("html");
//		System.out.println(string);
		Document doc = Jsoup.parse(string);
		Elements elementsByTag = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//		System.out.println(elementsByTag);
		HousingFundZhuZhouAccount h = null;
		List<HousingFundZhuZhouAccount> list = new ArrayList<HousingFundZhuZhouAccount>();
		WebParam<HousingFundZhuZhouAccount> webParam = new WebParam<HousingFundZhuZhouAccount>();
		for (int i = 0; i < elementsByTag.size(); i++) {
			h = new HousingFundZhuZhouAccount();
			Elements elementsByTag2 = elementsByTag.get(i).getElementsByTag("td");
			System.out.println(elementsByTag2.text());
			h.setDatea(elementsByTag2.get(0).text());
			h.setPersonalNum(elementsByTag2.get(1).text());
			h.setName(elementsByTag2.get(2).text());
			h.setCompanyNum(elementsByTag2.get(3).text());
			h.setCompany(elementsByTag2.get(4).text());
			h.setMoney(elementsByTag2.get(5).text());
			h.setFee(elementsByTag2.get(6).text());
			h.setDesce(elementsByTag2.get(7).text());
			h.setStartDate(elementsByTag2.get(8).text());
			h.setEndDate(elementsByTag2.get(9).text());
			h.setPeople(elementsByTag2.get(10).text());
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