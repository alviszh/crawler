package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\user.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		String replaceAll = json.replace("<!--", "").replaceAll("-->", "");
		Document doc = Jsoup.parse(replaceAll);
		InsuranceZhangZhouUserInfo i = new InsuranceZhangZhouUserInfo();
		Elements elementById = doc.getElementById("result").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		String name = getNextLabelByKeywordTwo(elementById, "姓名", "td");
		i.setName(name);
		i.setIDNum(getNextLabelByKeywordTwo(elementById, "身份证号", "td"));
		i.setBirth(getNextLabelByKeywordTwo(elementById, "出生日期", "td"));
		i.setHomeLand(getNextLabelByKeywordTwo(elementById, "户口所在地址", "td"));
		i.setPhone(getNextLabelByKeywordTwo(elementById, "移动电话", "td"));
		i.setTelephone(getNextLabelByKeywordTwo(elementById, "联系电话", "td"));
		i.setAddr(getNextLabelByKeywordTwo(elementById, "通讯地址", "td"));
		i.setCode(getNextLabelByKeywordTwo(elementById, "邮政编码", "td"));
		i.setEmail(getNextLabelByKeywordTwo(elementById, "电子邮箱", "td"));
		i.setCardNum(getNextLabelByKeywordTwo(elementById, "社保卡号", "td"));
		i.setStatus(getNextLabelByKeywordTwo(elementById, "当前状态", "td"));
		i.setTaskid("");
		System.out.println(i);	
	}
	
	public static  String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
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