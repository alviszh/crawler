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
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\666.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8");
		Element table=doc.select("table").last();
		//System.out.println(table);
		Elements tds=table.select("td");
		String userAccount = tds.get(1).text();
	
		String username = tds.get(3).text();
	
		String companyAccount = tds.get(5).text();
		String companyName = tds.get(7).text();
		String idnum = tds.get(9).text();
		String state = tds.get(11).text();
		String lastPaymonth = tds.get(13).text();
		String basemny = tds.get(15).text();
		String payAmount = tds.get(17).text();
		String balance=tds.get(19).text();
		String isFreeze=tds.get(21).text();
		System.out.println(companyAccount);
		System.out.println(companyName);
		System.out.println(idnum);
		System.out.println(state);
		System.out.println(lastPaymonth);
		System.out.println(basemny);
		System.out.println(payAmount);
		System.out.println(balance);
		System.out.println(isFreeze);
		
	}

}
