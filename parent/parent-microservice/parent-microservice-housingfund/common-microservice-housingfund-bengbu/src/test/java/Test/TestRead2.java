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

import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuAccount;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuUserInfo;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\bb.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("GridViewZm").getElementsByTag("tr");
		HousingFundBengBuAccount h = null;
		List<HousingFundBengBuAccount> list = new ArrayList<HousingFundBengBuAccount>();
		for (int i = 1; i < elementById.size(); i++) {
			h = new HousingFundBengBuAccount();
			Element element = elementById.get(i);
//			System.out.println(element);
			Element element2 = element.getElementsByTag("td").get(0);
//			System.out.println(element2);
			h.setDatea(element2.text());
			h.setDescr(element.getElementsByTag("td").get(0).text());
			h.setJf(element.getElementsByTag("td").get(1).text());
			h.setDf(element.getElementsByTag("td").get(2).text());
			h.setFee(element.getElementsByTag("td").get(3).text());
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