package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.baoji.HousingBaojiPay;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\444.html"), "UTF-8");
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		Element option = doc.getElementsByTag("option").last();
		String text=option.attr("value");
		System.out.println(text);
//		for (Element element : option) {
//			String text = element.attr("value");
//		
//			System.out.println(text);
//		}
	}

}
