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
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\333.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8");
		Element table = doc.select("table").last();
		
		if (null != table) {
			Elements trs = table.select("tr");
			//System.out.println(trs);
			int trs_size = trs.size()-1;
			if (trs_size > 1) {
				for (int i = 1; i < trs_size; i++) {
					Elements tds = trs.get(i).select("td");
					String rnum = tds.get(0).text();
					System.out.println(rnum);
					String beginMonth = tds.get(1).text();
					System.out.println(beginMonth);
					String endMonth = tds.get(2).text();
					System.out.println(endMonth);
					String payPersonBase = tds.get(3).text();
					System.out.println(payPersonBase);
					String payCompanyBase = tds.get(4).text();
					System.out.println(payCompanyBase);
					String payPersonAmount = tds.get(5).text();
					System.out.println(payPersonAmount);
					String payCompanyAmount = tds.get(6).text();
					System.out.println(payCompanyAmount);
					System.out.println("========"+i);
					
				}
			}
		}
	}

}
