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

import com.microservice.dao.entity.crawler.housing.taizhou2.HousingFundTaiZhou2Account;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\liu.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		HousingFundTaiZhou2Account h = null;
		List<HousingFundTaiZhou2Account> list = new ArrayList<HousingFundTaiZhou2Account>();
		Elements elementById = doc.getElementById("contentTable").getElementsByTag("tbody").get(0).getElementsByTag("tr");
//		System.out.println(elementById);
		for (int i = 1; i < elementById.size(); i++) {
			h = new HousingFundTaiZhou2Account();
			System.out.println(elementById.get(i));
			h.setDatea(elementById.get(i).getElementsByTag("td").get(1).text());
			h.setType(elementById.get(i).getElementsByTag("td").get(2).text());
			h.setPayDate(elementById.get(i).getElementsByTag("td").get(3).text());
			h.setGetFund(elementById.get(i).getElementsByTag("td").get(4).text());
			h.setOutFund(elementById.get(i).getElementsByTag("td").get(5).text());
			h.setFundInterest(elementById.get(i).getElementsByTag("td").get(6).text());
			h.setFundFee(elementById.get(i).getElementsByTag("td").get(7).text());
			h.setGetSubsidy(elementById.get(i).getElementsByTag("td").get(8).text());
			h.setOutSubsidy(elementById.get(i).getElementsByTag("td").get(9).text());
			h.setSubsidyInterest(elementById.get(i).getElementsByTag("td").get(10).text());
			h.setSubsidyFee(elementById.get(i).getElementsByTag("td").get(11).text());
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