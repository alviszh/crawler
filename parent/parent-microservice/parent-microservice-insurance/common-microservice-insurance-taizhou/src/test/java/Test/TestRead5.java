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
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.taizhou.InsuranceTaiZhouRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.taizhou.InsuranceTaiZhouRepositoryUnemployment;


public class TestRead5{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\社保泰州\\shiye.txt"); 
		String json = txt2String(file);
		Document parse = Jsoup.parse(json);
		InsuranceTaiZhouUnemployment i = new InsuranceTaiZhouUnemployment(); 
		System.out.println(json);
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