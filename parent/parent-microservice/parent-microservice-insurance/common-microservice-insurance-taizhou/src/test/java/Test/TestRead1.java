package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\tz.txt"); 
		String json = txt2String(file);
		Document parse = Jsoup.parse(json);
		Element elementById = parse.getElementById("queryform").getElementsByClass("condition_box").get(0).getElementsByTag("ul").get(1);
		Element elementById1 = parse.getElementById("queryform").getElementsByClass("condition_box").get(0).getElementsByTag("ul").get(3);
		InsuranceTaiZhouUserInfo i = new InsuranceTaiZhouUserInfo();
		i.setPersonalNum(elementById.getElementsByTag("li").get(0).getElementsByTag("input").val());
		i.setName(elementById.getElementsByTag("li").get(1).getElementsByTag("input").val());
		i.setBirth(elementById.getElementsByTag("li").get(2).getElementsByTag("input").val());
		i.setSex(elementById.getElementsByTag("li").get(3).getElementsByTag("input").val());
		i.setPersonalStatus(elementById.getElementsByTag("li").get(4).getElementsByTag("input").val());
		i.setType(elementById.getElementsByTag("li").get(5).getElementsByTag("input").val());
		
		i.setIdNum(elementById1.getElementsByTag("li").get(0).getElementsByTag("input").val());
		i.setNational(elementById1.getElementsByTag("li").get(1).getElementsByTag("input").val());
		i.setPhone(elementById1.getElementsByTag("li").get(2).getElementsByTag("input").val());
		i.setMedical(elementById1.getElementsByTag("li").get(3).getElementsByTag("input").val());
		i.setHome(elementById1.getElementsByTag("li").get(4).getElementsByTag("input").val());
		i.setAddr(elementById1.getElementsByTag("li").get(5).getElementsByTag("input").val());
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