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

import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUserInfo;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\养老.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		InsuranceZhangZhouEndowment in = null;
		List<InsuranceZhangZhouEndowment> list = new ArrayList<InsuranceZhangZhouEndowment>();
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		for (int i = 0; i < elementById.size(); i++) {
			in = new InsuranceZhangZhouEndowment();
			System.out.println(elementById.get(i));
			in.setDatea(elementById.get(i).getElementsByTag("td").get(0).text());
			in.setCompany(elementById.get(i).getElementsByTag("td").get(1).text());
			in.setType(elementById.get(i).getElementsByTag("td").get(2).text());
			in.setPersonalPay(elementById.get(i).getElementsByTag("td").get(3).text());
			in.setCompanyPay(elementById.get(i).getElementsByTag("td").get(4).text());
			in.setMoney(elementById.get(i).getElementsByTag("td").get(5).text());
			in.setBase(elementById.get(i).getElementsByTag("td").get(6).text());
			in.setTaskid("");
			list.add(in);
		}
		System.out.println(list);
		
	}
	
	public static  String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
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