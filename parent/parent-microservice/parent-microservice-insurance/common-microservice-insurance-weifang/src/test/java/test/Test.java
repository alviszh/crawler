package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\222.html"),"UTF-8");
		
		Document doc = Jsoup.parse(html);
		Elements inputs = doc.getElementsByAttributeValue("class", "dataTableCell");
		//Elements inputs =doc.getElementsByAttributeValue("type", "text");
		System.out.println(inputs);
		for (int i = 0; i < inputs.size(); i += 5) {
			// 缴费年月
			String yearMonth = inputs.get(i).val();
			System.out.println(yearMonth);
			// 单位名称
			String company_name = inputs.get(i + 1).val();
			System.out.println(company_name);
			// 缴费基数
			String pay_cardinal = inputs.get(i + 2).val();
			System.out.println(pay_cardinal);
			// 单位交费额
			String company_pay = inputs.get(i + 3).val();
			System.out.println(company_pay);
			System.out.println("-----------------");
		}
	}
	

}
