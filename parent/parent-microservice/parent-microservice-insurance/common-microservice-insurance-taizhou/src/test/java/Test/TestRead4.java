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

import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnEndowment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouMedical;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.taizhou.InsuranceTaiZhouRepositoryMedical;


public class TestRead4{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\社保泰州\\shengyu.txt"); 
		String json = txt2String(file);
		Document parse = Jsoup.parse(json);
//		System.out.println(json);
		InsuranceTaiZhouMedical in = null;
        Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
		
		Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
		
		Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
		
		Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
		
		Elements elementsByClass4 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(4).getElementsByTag("dd");
		
		Elements elementsByClass5 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(5).getElementsByTag("dd");
		
		Elements elementsByClass6 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(6).getElementsByTag("dd");
		
		Elements elementsByClass7 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(7).getElementsByTag("dd");
		
		
		System.out.println(elementsByClass.get(0).text());
		if(elementsByClass.get(0).text() == "")
		{
			System.out.println("4444444");
		}
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