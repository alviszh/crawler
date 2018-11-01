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
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"), "UTF-8");
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		HousingBaojiPay housingBaojiPay = null;
		Elements tr = doc.getElementsByTag("tr");
		for (Element element : tr) {
			Elements td = element.getElementsByTag("td");
			if (td.size() == 6) {
				String date = td.get(0).text();
				if (!"日期".equals(date)) {
					String debtAmount = td.get(1).text();
					String creditAmount = td.get(2).text();
					String balance = td.get(3).text();
					String lendingdirection = td.get(4).text();
					String summary = td.get(5).text();
					housingBaojiPay = new HousingBaojiPay("111", date, debtAmount, creditAmount, balance, lendingdirection,
							summary);
                  System.out.println(housingBaojiPay.toString());
				}

			}
		}
	}

}
