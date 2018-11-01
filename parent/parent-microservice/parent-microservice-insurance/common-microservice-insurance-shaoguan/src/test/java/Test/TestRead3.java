package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanMedical;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;

import app.common.WebParam;


public class TestRead3{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\sg.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document doc = Jsoup.parse(json);
		String elementsByClass = doc.getElementsByTag("table").get(8).getElementsByTag("td").text();
		int indexOf3 = elementsByClass.indexOf("/");
		int indexOf4 = elementsByClass.indexOf("第");
		String substring3 = elementsByClass.substring(indexOf3, indexOf4);
		String[] split = substring3.split("共");
		String[] substring4 = split[1].split("页");
		System.out.println(substring4[0]);
		
		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(substring4[0].trim());
        if (!isNum.matches()) {
            System.out.println("false");
        }
        else
        {
            System.out.println(substring4[0]);
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