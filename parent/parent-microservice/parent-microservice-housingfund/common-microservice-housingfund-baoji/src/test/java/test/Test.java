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
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		//System.out.println(html);
		Document doc = Jsoup.parse(html);
		Element table = doc.select("table").get(1);
		Elements trs=table.select("tr");
		System.out.println(trs);
		Elements tds=table.select("td");
		System.out.println(tds.get(1).text());
	}

}
