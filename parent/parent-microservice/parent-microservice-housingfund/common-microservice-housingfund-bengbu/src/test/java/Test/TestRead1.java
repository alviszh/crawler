package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\bb.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		Elements elementsByTag = doc.getElementsByTag("table");
		System.out.println(elementsByTag);
		HousingFundBengBuUserInfo h = new HousingFundBengBuUserInfo();
		Element elementById = doc.getElementById("dwzhLabel");
		String text = elementById.text();
		h.setCompanyNum(text);
		h.setPersonalNum(doc.getElementById("grzhLabel").text());
		h.setCompany(doc.getElementById("dwmcLabel").text());
		h.setName(doc.getElementById("grxmLabel").text());
		h.setBankNum(doc.getElementById("dwyhzhLabel").text());
		h.setPersonalBankNum(doc.getElementById("SGrYhZh").text());
		h.setIDNum(doc.getElementById("sfzhmLabel").text());
		h.setFee(doc.getElementById("ljyeLabel").text());
		h.setStartDate(doc.getElementById("qjrqLabel").text());
		h.setEndDate(doc.getElementById("DtJzrqLabel").text());
		h.setMonthPay(doc.getElementById("dc_yjjeLabel").text());
		h.setStatus(doc.getElementById("IFcbjLabel").text());
		h.setCompanyRadio(doc.getElementById("DwblLabel").text());
		h.setPersonalRadio(doc.getElementById("GrblLabel").text());
		h.setBank(doc.getElementById("SYhTypeLabel").text());
		h.setTaskid("");
		System.out.println(h);
		
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