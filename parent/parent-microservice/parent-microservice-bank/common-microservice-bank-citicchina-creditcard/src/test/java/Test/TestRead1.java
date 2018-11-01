package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardBill;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\4.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Element element = doc.getElementsByTag("resinfo").get(0);
		String string = element.toString();
		//System.out.println(string);
		int address = string.indexOf("address");
		int city = string.indexOf("city");
		int companyphone = string.indexOf("companyphone");
		int email = string.indexOf("email");
		int emergentphone = string.indexOf("emergentphone");
		int province = string.indexOf("province");
		int zipcode = string.indexOf("zipcode");
		String address1 = string.substring(address, city).substring(8);
		String city1 = string.substring(city,companyphone).substring(5);
		String companyphone1 = string.substring(companyphone,email).substring(13);
		String email1 = string.substring(email,emergentphone).substring(6);
		String emergentphone1 = string.substring(emergentphone,province).substring(14);
		String province1 = string.substring(province,zipcode).substring(9);
		String zipcode1 = string.substring(zipcode).substring(8).replace("/>", "");
		
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