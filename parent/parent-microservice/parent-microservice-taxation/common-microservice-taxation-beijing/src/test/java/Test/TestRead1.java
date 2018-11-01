package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\bj.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByTag = doc.getElementsByTag("table").get(1).getElementsByTag("tr").get(0).getElementsByTag("td");
		System.out.println(elementsByTag.get(1).getElementsByTag("input").val());
		TaxationBeiJingUserInfo taxationBeiJingUserInfo = new TaxationBeiJingUserInfo();
		taxationBeiJingUserInfo.setName(elementsByTag.get(1).getElementsByTag("input").val());
		taxationBeiJingUserInfo.setNation(elementsByTag.get(3).getElementsByTag("input").val());
		taxationBeiJingUserInfo.setCardType(elementsByTag.get(3).getElementsByTag("input").val());
		taxationBeiJingUserInfo.setCardNum(elementsByTag.get(5).getElementsByTag("input").val());
		taxationBeiJingUserInfo.setTaskid("");
		System.out.println(taxationBeiJingUserInfo);
		
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