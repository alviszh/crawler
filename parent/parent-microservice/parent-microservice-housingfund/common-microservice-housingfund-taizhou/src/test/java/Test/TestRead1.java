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

import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouAccount;
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\taizhou.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("con_body").get(0).getElementsByTag("tr").get(3).getElementsByTag("td");
		Elements elementsByClass1 = doc.getElementsByClass("con_body").get(0).getElementsByTag("tr").get(4).getElementsByTag("td");
		

//		System.out.println(elementsByClass2);

		HousingFundTaiZhouUserInfo h = null;
				
				
		for (int i = 0; i < elementsByClass.size(); i=i+8) {
			h = new HousingFundTaiZhouUserInfo();
			h.setNum(elementsByClass.get(i).text());
			h.setName(elementsByClass.get(i+1).text());
			h.setIdCard(elementsByClass.get(i+2).text());
			h.setFee(elementsByClass.get(i+3).text());
			h.setMonthPay(elementsByClass.get(i+4).text());
			h.setStatus(elementsByClass.get(i+5).text());
			h.setPayDate(elementsByClass.get(i+6).text());
			h.setBank(elementsByClass.get(i+7).text());
			h.setCompany(elementsByClass1.text().substring(5));
			h.setTaskid("");
		}
//		System.out.println(h);
		
		
		Elements elementsByClass2 = doc.getElementsByClass("custom_list").get(1).getElementsByTag("td");
		System.out.println(elementsByClass2);
		HousingFundTaiZhouAccount h1= null;
		List<HousingFundTaiZhouAccount> list = new ArrayList<HousingFundTaiZhouAccount>();
		for (int i = 0; i < elementsByClass2.size(); i=i+5) {
			h1 = new HousingFundTaiZhouAccount();
			h1.setType(elementsByClass2.get(i).text());
			h1.setNum(elementsByClass2.get(i+1).text());
			h1.setFee(elementsByClass2.get(i+2).text());
			h1.setDatea(elementsByClass2.get(i+3).text());
			h1.setMoney(elementsByClass2.get(i+4).text());
			h1.setTaskid("");
			list.add(h1);
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