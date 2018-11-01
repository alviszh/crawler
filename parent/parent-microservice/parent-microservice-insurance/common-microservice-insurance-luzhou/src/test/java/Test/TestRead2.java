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


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\泸州社保\\user.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("detail").get(0).getElementsByClass("item");
		InsuranceLuZhouUserInfo in = new InsuranceLuZhouUserInfo();
		in.setName(elementsByClass.get(0).text());
		in.setIDNum(elementsByClass.get(1).text());
		in.setPhone(elementsByClass.get(2).text());
		
		Elements elementsByClass1 = doc.getElementsByClass("detail").get(0).getElementsByClass("item-info");
		in.setCompany(elementsByClass1.get(0).text());
		in.setCanbaoCompany(elementsByClass1.get(1).text());
		in.setEndowment(elementsByClass1.get(2).text());
		in.setUnemployment(elementsByClass1.get(3).text());
		in.setMedical(elementsByClass1.get(4).text());
		in.setAddMedical(elementsByClass1.get(5).text());
		in.setInjury(elementsByClass1.get(6).text());
		in.setMaternity(elementsByClass1.get(7).text());
		in.setTaskid("");
		System.out.println(in);
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