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

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardAccount;

public class TestRead2 {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\3.txt");
		String json = txt2String(file);
//		System.out.println(json);
		List<CiticChinaDebitCardAccount> list = new ArrayList<CiticChinaDebitCardAccount>();
		Document doc = Jsoup.parse(json);
		CiticChinaDebitCardAccount c = new CiticChinaDebitCardAccount();
		Element elementById = doc.getElementById("resultTable1");
		Elements elementsByTag = elementById.getElementsByTag("tbody");
		Element element = elementsByTag.get(0);
		Elements elementsByTag2 = element.getElementsByTag("tr");
		for (int i = 0; i < elementsByTag2.size(); i++) {
//			System.out.println(elementsByTag2.get(i));
			Elements elementsByTag3 = elementsByTag2.get(i).getElementsByTag("td");
			for (int j = 1; j < elementsByTag3.size(); j = j + 10) {
				System.out.println(elementsByTag3);
				c.setDatea(elementsByTag3.get(j + 1).text());
				c.setSetMoney(elementsByTag3.get(j + 2).text());
				c.setGetMoney(elementsByTag3.get(j + 3).text());
				c.setFee(elementsByTag3.get(j + 4).text());
				c.setAnotherOne(elementsByTag3.get(j + 5).text());
				c.setCompany(elementsByTag3.get(j + 6).text());
				c.setRemark(elementsByTag3.get(j + 7).text());
				c.setStatus(elementsByTag3.get(j + 8).text());
			}
			list.add(c);
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