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
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUserInfo;


public class TestRead3{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\sheng.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		InsuranceZhangZhouMaternity in = null;
		List<InsuranceZhangZhouMaternity> list = new ArrayList<InsuranceZhangZhouMaternity>();
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("ylzps").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		for (int i = 0; i < elementById.size(); i++) {
			in = new InsuranceZhangZhouMaternity();
			in.setDatea(elementById.get(i).getElementsByTag("td").get(0).text());
			in.setCompanyPay(elementById.get(i).getElementsByTag("td").get(1).text());
			in.setBase(elementById.get(i).getElementsByTag("td").get(2).text());
			in.setCompanyRatio(elementById.get(i).getElementsByTag("td").get(3).text());
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