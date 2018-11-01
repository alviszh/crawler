package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.microservice.dao.repository.crawler.housing.haerbin.HousingHaErBinUserInfoRepository;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\hauser.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document parse = Jsoup.parse(json);
		Elements elementsByTag = parse.getElementsByTag("textarea");
		System.out.println(elementsByTag.get(0).text());
		
		
		Element elementById = parse.getElementById("container-5db4fbfa5a2c4d8b8e4e261c6b0ed1d2").getElementsByTag("tbody").get(0);
//		System.out.println(elementById);
		String val = elementById.getElementsByTag("tr").get(1).getElementsByTag("input").val();//姓名
		
		String val2 = parse.getElementById("accname").val();
		System.out.println(val2);
		HousingFundHaErBinUserInfo h = new HousingFundHaErBinUserInfo();
		h.setName(parse.getElementById("accname").val());
		h.setStatus(parse.getElementById("proptype").val());
		h.setCardType(parse.getElementById("certitype").val());
		h.setIdCard(parse.getElementById("certinum").val());
		h.setOpenDate(parse.getElementById("opnaccdate").val());
		h.setPhone(parse.getElementById("handset").val());
		h.setPersonalNum(parse.getElementById("accnum").val());
		h.setPersonalStatus(parse.getElementById("indiaccstate").val());
		h.setComapnyNum(parse.getElementById("unitaccnum").val());
		h.setCompany(parse.getElementById("unitaccname").val());
		h.setCompanyRatio(parse.getElementById("unitprop").val());
		h.setPersonalRatio(parse.getElementById("indiprop").val());
		h.setPersonalBase(parse.getElementById("basenum").val());
		h.setLastDate(parse.getElementById("lpaym").val());
		h.setPersonalMonth(parse.getElementById("indipayamt").val());
		h.setCompanyMonth(parse.getElementById("unitpayamt").val());
		h.setMonthSave(parse.getElementById("indipaysum").val());
		h.setFee(parse.getElementById("bal").val());
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

	
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}
		
}