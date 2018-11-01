package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinMedical;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\333.html"), "UTF-8");
		// System.out.println(html);
		Document doc = Jsoup.parse(html,"UTF-8");
		Element  table=doc.select("table").last();
     	if (null !=table) {
     		Elements trs = table.select("tr");
     		int trs_size = trs.size()-1;
			if (trs_size >0) {
				for (int i = 1; i < trs_size; i++) {					
					Elements tds = trs.get(i).select("td");
					String useraccount=tds.get(0).text();
					String useraccount2=tds.get(1).text();
					System.out.println(useraccount);
					System.out.println(useraccount2);
					System.out.println("======="+i);
				}
			}
		}	
	}
	private  static String getNextLabelByKeyword(Document document, String keyword) {
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
