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

import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangAccount;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\liu.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document parse = Jsoup.parse(json);
		HousingFundZhanJiangAccount h = null;
		List<HousingFundZhanJiangAccount> list = new ArrayList<HousingFundZhanJiangAccount>();
		Elements elementsByTag = parse.getElementsByTag("table").get(1).getElementsByTag("tr");
		for (int i = 1; i < elementsByTag.size(); i++) {
			h = new HousingFundZhanJiangAccount();
//			System.out.println(elementsByTag.get(i));	
			h.setDatea(elementsByTag.get(i).getElementsByTag("td").get(0).text());
			h.setDesrc(elementsByTag.get(i).getElementsByTag("td").get(1).text());
			h.setGetMoney(elementsByTag.get(i).getElementsByTag("td").get(2).text());
			h.setOutMoney(elementsByTag.get(i).getElementsByTag("td").get(3).text());
			h.setFee(elementsByTag.get(i).getElementsByTag("td").get(4).text());
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