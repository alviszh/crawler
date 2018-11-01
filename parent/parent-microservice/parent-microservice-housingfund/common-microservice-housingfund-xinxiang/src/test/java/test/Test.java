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
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\777.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		 Element table= doc.select("table").last();
		 Elements trs=table.select("tbody").select("tr");
		 if (trs.size()>2) {
			 for (int i = 2; i < trs.size(); i++) {
				 Elements tds = trs.get(i).select("td");
				 String companyNumber=tds.get(0).text();
				 String accountNumber= tds.get(1).text();
				 String incomeAmount= tds.get(2).text();
				 System.out.println(companyNumber);
				 System.out.println(accountNumber);
				 System.out.println(incomeAmount);
				 System.out.println("-------------");
			 }
		}
		
	}

}
