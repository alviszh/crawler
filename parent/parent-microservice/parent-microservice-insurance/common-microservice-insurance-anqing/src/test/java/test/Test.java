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
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\555.html"), "UTF-8");
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		Element table =  doc.select("table").get(2);
		System.out.println(table);
//		Elements trs = table.select("tr");
//		int trs_size = trs.size();
//		if (trs_size > 0) {
//			for (int i = 1; i < trs_size; i++) {
//				Elements tds = trs.get(i).select("td");
//				
//				
//				
//				
//				 String type=tds.get(0).text();//险种类型
//				 String companynum=tds.get(1).text();//单位
//				 String companyname=tds.get(2).text();//单位名称
//				 String insuredtype=tds.get(3).text();//比例类型
//				 String insuredway=tds.get(4).text();//人员参保关系
//				 String paybase=tds.get(5).text();//缴费基数
//				 String insuredstate=tds.get(6).text();//个人参保状态
//				 String insureddate=tds.get(7).text();//个人参保日期
//				
//				    System.out.println(type);
//				
//				
//			}
//		}
//	
//	    String num=getNextLabelByKeyword(doc, "个人编号");
//	    System.out.println(num);
	}

	
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.last();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}
