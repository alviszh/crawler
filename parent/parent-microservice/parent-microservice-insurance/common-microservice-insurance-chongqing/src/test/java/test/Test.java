package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\1111.html"),"UTF-8");
		Document doc = Jsoup.parse(html,"utf-8");	
		Element table = doc.getElementById("basicInfoTable");
		System.out.println("5555555555");
		if (null != table) {
			
			String name = getNextLabelByKeyword(doc, "姓名");
			System.out.println(name);
			String sex = getNextLabelByKeyword(doc, "性别");
			String idNum = getNextLabelByKeyword(doc, "身份证号");
			String birthdate = getNextLabelByKeyword(doc, "出生日期");
			String personNumber = getNextLabelByKeyword(doc, "个人编号");
			String nation = getNextLabelByKeyword(doc, "民族");
			String companyNum = getNextLabelByKeyword(doc, "所在单位编号");
			String category = getNextLabelByKeyword(doc, "户口性质");
			
			System.out.println(sex);
			System.out.println(idNum);
			System.out.println(birthdate);
			System.out.println(personNumber);
			System.out.println(nation);
			System.out.println(companyNum);
			System.out.println(category);
			System.out.println("ttttttttt");
//			Elements trs = table.select("tbody").select("tr");
//			int trs_size = trs.size()-2;
//			if (trs_size > 0) {
//				for (int i = 1; i <= trs_size; i++) {
//					Elements tds = trs.get(i).select("td");
//					String paymonth = tds.get(0).text();
//					System.out.println(paymonth);
//			
//					
//				}
//			}
		}
	}

	
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}
