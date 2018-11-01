package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.microservice.dao.entity.crawler.housing.liaocheng.HousingLiaoChengPaydetails;

import app.service.InsuranceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\5555.html"),"UTF-8");
		Document doc = Jsoup.parse(html,"utf-8");	
		Element table = doc.select("table").last();
		if (null != table) {
			Elements trs = table.select("tbody").select("tr");
			int trs_size = trs.size()-2;
			if (trs_size > 0) {
				for (int i = 1; i <= trs_size; i++) {
					Elements tds = trs.get(i).select("td");
					String paymonth = tds.get(0).text();
					System.out.println(paymonth);
			
					
				}
			}
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
