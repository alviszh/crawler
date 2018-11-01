package test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		Element table=doc.select("table").first();
	
		Elements trs=table.select("tr");
		 int trs_size=trs.size();
		 if(trs_size>1){
			 for (int i = 1; i < trs_size; i++) {
			 Elements tds=trs.get(i).select("td");
			
			 String dealDate=tds.get(0).text();
			 System.out.println(dealDate);
			 String companyName=tds.get(1).text();
			 System.out.println(companyName);
			 String companyAccount=tds.get(2).text();
			 System.out.println(companyAccount);
			 String businessType=tds.get(3).text();
			 System.out.println(businessType);
			 String lendType=tds.get(4).text();
			 System.out.println(lendType);
			 String dealAmount=tds.get(5).text();
			 System.out.println(dealAmount);
			 String balance=tds.get(6).text();
			 System.out.println(balance);
			 String drawType=tds.get(7).text();
			 System.out.println(drawType);
			 String beginDate=tds.get(8).text();
			 System.out.println(beginDate);
			 String endDate=tds.get(9).text();
			 System.out.println(endDate);
			 String summary=tds.get(10).text();
			 System.out.println(summary);
			 System.out.println("--------------");
			 }
	}
		 Calendar c = Calendar.getInstance();
			String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
			c.add(Calendar.YEAR, -1);
			System.out.println(timeNow);
}
}
