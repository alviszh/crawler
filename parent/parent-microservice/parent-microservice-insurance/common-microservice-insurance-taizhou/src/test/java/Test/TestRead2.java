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

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouEndowment;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\社保泰州\\yanglao.txt"); 
		String json = txt2String(file);
		Document parse = Jsoup.parse(json);
//		System.out.println(json);
		InsuranceTaiZhouEndowment in = null;
		Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
		
		Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
		
		Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
		
		Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
		
		Elements elementsByClass4 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(4).getElementsByTag("dd");
		
		Elements elementsByClass5 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(5).getElementsByTag("dd");
		
		Elements elementsByClass6 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(6).getElementsByTag("dd");
		
		Elements elementsByClass7 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(7).getElementsByTag("dd");
		
		Elements elementsByClass8 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(8).getElementsByTag("dd");
		
		Elements elementsByClass9 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(9).getElementsByTag("dd");

		List<InsuranceTaiZhouEndowment> list = new ArrayList<InsuranceTaiZhouEndowment>();
		for (int i = 0; i < elementsByClass.size(); i++) {
			in = new InsuranceTaiZhouEndowment();
			in.setDatea(elementsByClass.get(i).text());
			in.setInDatea(elementsByClass1.get(i).text());
			in.setType(elementsByClass2.get(i).text());
			in.setBase(elementsByClass3.get(i).text());
			in.setPersonalMoney(elementsByClass4.get(i).text());
			in.setCompany(elementsByClass5.get(i).text());
			in.setMonth(elementsByClass6.get(i).text());
			
			in.setName(elementsByClass7.get(i).text());
			in.setIDNum(elementsByClass8.get(i).text());
			in.setPersonalNum(elementsByClass9.get(i).text());
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