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

import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\泸州社保\\yanglao.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("row-bottom");
		List list = new ArrayList();
		InsuranceLuZhouEndowment in = null;
		for (int i = 1; i < elementsByClass.size(); i++) {
			Elements element = elementsByClass.get(i).getElementsByTag("span");
//			System.out.println(element.get(1));
				in = new InsuranceLuZhouEndowment();
				in.setTime(element.get(1).text());
				in.setMonth(element.get(2).text());
				in.setMoney(element.get(3).text());
				in.setFee(element.get(4).text());
				in.setTaskid("");
				list.add(in);
			
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