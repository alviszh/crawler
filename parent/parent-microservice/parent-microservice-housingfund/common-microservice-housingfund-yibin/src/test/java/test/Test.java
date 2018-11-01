package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\888.html"), "UTF-8");
		Document doc = Jsoup.parse(html, "utf-8");
		Elements tr = doc.getElementsByClass("tablejieguo");
		String name = tr.select("td").get(1).text();
		System.out.println(name);
	}

}
