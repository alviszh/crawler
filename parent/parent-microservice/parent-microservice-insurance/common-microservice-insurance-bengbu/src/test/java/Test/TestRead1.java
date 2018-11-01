package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\user.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementById = doc.getElementById("form1").getElementsByTag("tr");
		String val = elementById.get(0).getElementsByTag("td").get(1).getElementsByTag("input").val();
		System.out.println(val);
		InsuranceBengBuUserInfo i = new InsuranceBengBuUserInfo();
		i.setPersonalNum(elementById.get(0).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setCardNum(elementById.get(0).getElementsByTag("td").get(3).getElementsByTag("input").val());
		i.setCompanyNum(elementById.get(1).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setIDNum(elementById.get(1).getElementsByTag("td").get(3).getElementsByTag("input").val());
		i.setName(elementById.get(2).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setSex(elementById.get(2).getElementsByTag("td").get(3).getElementsByTag("input").val());
		i.setNational(elementById.get(3).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setBirth(elementById.get(3).getElementsByTag("td").get(3).getElementsByTag("input").val());
		i.setDatea(elementById.get(4).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setStatus(elementById.get(4).getElementsByTag("td").get(3).getElementsByTag("input").val());
		i.setConnectPerson(elementById.get(5).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setCode(elementById.get(5).getElementsByTag("td").get(3).getElementsByTag("input").val());
		i.setPhone(elementById.get(6).getElementsByTag("td").get(1).getElementsByTag("input").val());
		i.setAddr(elementById.get(6).getElementsByTag("td").get(3).getElementsByTag("input").val());
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