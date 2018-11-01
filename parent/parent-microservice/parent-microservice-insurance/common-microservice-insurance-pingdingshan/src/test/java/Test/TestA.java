package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanEndowment;

public class TestA {

	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\2.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		String substring = json.substring(4, json.length()-1);
		StringBuilder sb = new StringBuilder(); 
		sb.append("<table>"); 
		sb.append(substring); 
		sb.append("</table>"); 
//		System.out.println(sb);
		Document doc = Jsoup.parse(sb.toString());
		System.out.println(sb.toString());
		
		//基数部分
		Elements elementsByClass1 = doc.getElementsByClass("table-number");
		System.out.println(elementsByClass1);
		
		InsurancePingDingShanEndowment j = null;
		List list = new ArrayList();
		
		Elements elementsByTag = doc.getElementsByTag("td");
		int size = elementsByTag.toString().length()/2;
	    System.out.println(elementsByTag.text());
		
		for (int i = 0; i < elementsByTag.size()/2; i++) {
			System.out.println(elementsByTag.get(i).text());
			j = new InsurancePingDingShanEndowment();
			if(elementsByTag.get(i).text().length()>6)
			{
				if(elementsByTag.get(i).text().contains("年"))
				{
					j.setYear(elementsByTag.get(i).text().substring(0, 4));
					
				}
				if(elementsByClass1.get(i).text().length()>6)
				{
					j.setBase(elementsByClass1.get(i).text().substring(0, 4));
				}
				
			}
			
			list.add(j);
		}
		
		String substring3 = elementsByTag.toString().substring(0,elementsByTag.toString().length()-size);
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
