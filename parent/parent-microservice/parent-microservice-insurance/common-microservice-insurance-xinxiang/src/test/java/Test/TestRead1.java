package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangEndowment;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\xx.txt"); 
		String json = txt2String(file);
		
		File file1 = new File("C:\\Users\\Administrator\\Desktop\\xxu.txt"); 
		String json1 = txt2String(file1);
		System.out.println(json1);
		Document doc = Jsoup.parse(json);
		Document doc1 = Jsoup.parse(json1);
		if(json.contains("个人基本信息")&&json1.contains("个人基本信息"))
		{
			Elements elementsByClass = doc.getElementsByClass("cx_table");
			Elements elementsByClass1 = doc1.getElementsByClass("cx_table");
			
			InsuranceXinXiangUserInfo i = new InsuranceXinXiangUserInfo();
			String nextLabelByKeywordTwo = getNextLabelByKeywordTwo(elementsByClass,"姓名", "td");
			i.setTaskid("");
			i.setName(nextLabelByKeywordTwo);
			i.setPersonalPhone(getNextLabelByKeywordTwo(elementsByClass,"手机", "td"));
			i.setSex(getNextLabelByKeywordTwo(elementsByClass,"性别", "td"));
			i.setCardNum(getNextLabelByKeywordTwo(elementsByClass,"社保卡号", "td"));
			i.setKeeper(getNextLabelByKeywordTwo(elementsByClass,"监护人姓名", "td"));
			i.setIDNum(getNextLabelByKeywordTwo(elementsByClass,"身份证号", "td"));
			i.setCompany(getNextLabelByKeywordTwo(elementsByClass,"单位名称", "td"));
			i.setAddr(getNextLabelByKeywordTwo(elementsByClass,"通讯地址", "td"));
			i.setCommunity(getNextLabelByKeywordTwo(elementsByClass,"所在社区（村）", "td"));
			
			i.setPersonalNum(getNextLabelByKeywordTwo(elementsByClass1,"个人编号", "td"));
			i.setNational(getNextLabelByKeywordTwo(elementsByClass1,"民族", "td"));
			i.setBirth(getNextLabelByKeywordTwo(elementsByClass1,"出生日期", "td"));
			i.setStatus(getNextLabelByKeywordTwo(elementsByClass1,"单位状态", "td"));
			i.setJobStatus(getNextLabelByKeywordTwo(elementsByClass1,"行业风险类型", "td"));
			i.setCompanyNum(getNextLabelByKeywordTwo(elementsByClass1,"单位编号", "td"));
			i.setCompanyType(getNextLabelByKeywordTwo(elementsByClass1,"单位类型", "td"));
			i.setPayName(getNextLabelByKeywordTwo(elementsByClass1,"缴费单位专管员姓名", "td"));
			i.setPayPhone(getNextLabelByKeywordTwo(elementsByClass1,"缴费单位专管员电话", "td"));
			i.setJoinDate(getNextLabelByKeywordTwo(elementsByClass1,"参加工作时间", "td"));
			System.out.println(i);
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
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
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

		
}