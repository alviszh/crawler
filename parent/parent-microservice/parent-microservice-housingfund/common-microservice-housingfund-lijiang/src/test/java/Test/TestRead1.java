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

import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangAccount;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\lj.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByTag = doc.getElementsByTag("tbody").get(1).getElementsByTag("tr");//("/html/body/table[2]/tbody");
		HousingFundLiJiangAccount h = null;
		List<HousingFundLiJiangAccount>  list = new ArrayList<HousingFundLiJiangAccount>();
		for (int i = 1; i < elementsByTag.size(); i++) {
			h = new HousingFundLiJiangAccount();
//			System.out.println(elementsByTag.get(i).getElementsByTag("td").text());
			h.setDatea(elementsByTag.get(i).getElementsByTag("td").get(0).text());
			h.setDescr(elementsByTag.get(i).getElementsByTag("td").get(1).text());
			h.setJf(elementsByTag.get(i).getElementsByTag("td").get(2).text());
			h.setDf(elementsByTag.get(i).getElementsByTag("td").get(3).text());
			h.setFee(elementsByTag.get(i).getElementsByTag("td").get(4).text());
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