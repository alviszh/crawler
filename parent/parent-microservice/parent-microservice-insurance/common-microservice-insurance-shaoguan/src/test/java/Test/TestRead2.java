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

import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanMedical;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\yl.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		InsuranceShaoGuanMedical in = null;
		List<InsuranceShaoGuanMedical> list = new ArrayList<InsuranceShaoGuanMedical>();
		Elements elementsByTag = doc.getElementsByClass("tableblue").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
		for (int i = 1; i < elementsByTag.size(); i++) {
			in = new InsuranceShaoGuanMedical();
			System.out.println(elementsByTag.get(i).getElementsByTag("td").text());
			in.setCompanyNum(elementsByTag.get(i).getElementsByTag("td").get(0).text());
			in.setCompany(elementsByTag.get(i).getElementsByTag("td").get(1).text());
			in.setDatea(elementsByTag.get(i).getElementsByTag("td").get(2).text());
			in.setSf(elementsByTag.get(i).getElementsByTag("td").get(3).text());
			in.setBase(elementsByTag.get(i).getElementsByTag("td").get(4).text());
			in.setCompanyRatio(elementsByTag.get(i).getElementsByTag("td").get(5).text());
			in.setCompanyPay(elementsByTag.get(i).getElementsByTag("td").get(6).text());
			in.setPersonalRatio(elementsByTag.get(i).getElementsByTag("td").get(7).text());
			in.setPersoanlPay(elementsByTag.get(i).getElementsByTag("td").get(8).text());
			in.setCompanySend(elementsByTag.get(i).getElementsByTag("td").get(9).text());
			in.setPaySum(elementsByTag.get(i).getElementsByTag("td").get(10).text());
			in.setSendDate(elementsByTag.get(i).getElementsByTag("td").get(11).text());
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