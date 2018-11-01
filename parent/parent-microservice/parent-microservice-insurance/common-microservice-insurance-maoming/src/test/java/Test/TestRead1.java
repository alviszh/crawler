package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\user.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("form1").getElementsByTag("tbody");
		Element elementsByTag = elementById.get(0).getElementsByTag("tr").get(0);
		System.out.println(elementsByTag);
		InsuranceMaoMingUserInfo i = new InsuranceMaoMingUserInfo();
		i.setCardNum(elementById.get(0).getElementsByTag("tr").get(0).getElementsByTag("input").get(0).val());
		i.setName(elementById.get(0).getElementsByTag("tr").get(0).getElementsByTag("input").get(1).val());
		i.setIDNum(elementById.get(0).getElementsByTag("tr").get(1).getElementsByTag("input").get(0).val());
		i.setBirth(elementById.get(0).getElementsByTag("tr").get(1).getElementsByTag("input").get(1).val());
		i.setSex(elementById.get(0).getElementsByTag("tr").get(2).getElementsByTag("input").get(0).val());
		i.setNational(elementById.get(0).getElementsByTag("tr").get(2).getElementsByTag("input").get(1).val());
		i.setStatus(elementById.get(0).getElementsByTag("tr").get(3).getElementsByTag("input").get(0).val());
		i.setPersonalNum(elementById.get(0).getElementsByTag("tr").get(3).getElementsByTag("input").get(1).val());
		i.setCompanyNum(elementById.get(0).getElementsByTag("tr").get(4).getElementsByTag("input").get(0).val());
		i.setCompany(elementById.get(0).getElementsByTag("tr").get(4).getElementsByTag("input").get(1).val());
		i.setPhone(elementById.get(0).getElementsByTag("tr").get(5).getElementsByTag("input").get(0).val());
		i.setNum(elementById.get(0).getElementsByTag("tr").get(5).getElementsByTag("input").get(1).val());
		i.setHomeAddr(elementById.get(0).getElementsByTag("tr").get(6).getElementsByTag("input").get(0).val());
		i.setAddr(elementById.get(0).getElementsByTag("tr").get(7).getElementsByTag("input").get(0).val());
		i.setTaskid("");
		System.out.println(i);
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