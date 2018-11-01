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

import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingEndowment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUnemployment;

import app.common.WebParam;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zqlao.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("hccx_tableCon").get(0).getElementsByTag("tr");
		InsuranceZhaoQingEndowment in = null;
		List<InsuranceZhaoQingEndowment> list = new ArrayList<InsuranceZhaoQingEndowment>();
		WebParam<InsuranceZhaoQingEndowment> webParam = new WebParam<InsuranceZhaoQingEndowment>();
		for (int i = 0; i < elementsByClass.size(); i++) {
			in = new InsuranceZhaoQingEndowment();
//			System.out.println(elementsByClass.get(i));
			in.setDatea(elementsByClass.get(i).getElementsByTag("td").get(0).text());
			in.setCompany(elementsByClass.get(i).getElementsByTag("td").get(1).text());
			in.setBase(elementsByClass.get(i).getElementsByTag("td").get(2).text());
			in.setCompanyPay(elementsByClass.get(i).getElementsByTag("td").get(3).text());
			in.setPersonlPay(elementsByClass.get(i).getElementsByTag("td").get(4).text());
			in.setSum(elementsByClass.get(i).getElementsByTag("td").get(5).text());
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