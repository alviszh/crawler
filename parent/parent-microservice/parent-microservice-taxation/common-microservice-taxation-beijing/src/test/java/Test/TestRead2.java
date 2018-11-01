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

import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingAccount;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingUserInfo;



public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\bjac.txt"); 
		String json = txt2String(file);
		
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("print").getElementsByTag("tr");
//		System.out.println(elementById);
		TaxationBeiJingAccount t = null;
		List<TaxationBeiJingAccount> list = new ArrayList<TaxationBeiJingAccount>();
		for (int i = 4; i < elementById.size()-6; i++) {
			t = new TaxationBeiJingAccount();
//			System.out.println(elementById.get(i).getElementsByTag("td").get(2).text());
			t.setGetProject(elementById.get(i).getElementsByTag("td").get(0).text());
			t.setTaxDate(elementById.get(i).getElementsByTag("td").get(1).text());
			t.setGetMoney(elementById.get(i).getElementsByTag("td").get(2).text());
			t.setTaxRatio(elementById.get(i).getElementsByTag("td").get(3).text());
			t.setInDate(elementById.get(i).getElementsByTag("td").get(4).text());
			t.setCompany(elementById.get(i).getElementsByTag("td").get(5).text());
			t.setGovement(elementById.get(i).getElementsByTag("td").get(6).text());
			list.add(t);
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