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

import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangEndowment;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangUserInfo;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\xxlao.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("cx_table").get(0).getElementsByTag("tr");
		System.out.println(elementsByClass);
		List<InsuranceXinXiangEndowment> list = new ArrayList<InsuranceXinXiangEndowment>();
		InsuranceXinXiangEndowment in = null;
		for (int i = 1; i < elementsByClass.size()-3; i++) {
			in = new InsuranceXinXiangEndowment();
			Elements elementsByTag = elementsByClass.get(i).getElementsByTag("td");
			in.setEndDate(elementsByTag.get(0).text());
			in.setBase(elementsByTag.get(1).text());
			in.setPersonalPay(elementsByTag.get(2).text());
			in.setCompanyPay(elementsByTag.get(3).text());
			in.setCompanyAccount(elementsByTag.get(4).text());
			in.setAverage(elementsByTag.get(5).text());
			in.setPayMoney(elementsByTag.get(6).text());
			in.setStatus(elementsByTag.get(7).text());
			in.setPayFlag(elementsByTag.get(8).text());
			in.setGetDate(elementsByTag.get(9).text());
			in.setAccountFlag(elementsByTag.get(10).text());
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
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
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

		
}