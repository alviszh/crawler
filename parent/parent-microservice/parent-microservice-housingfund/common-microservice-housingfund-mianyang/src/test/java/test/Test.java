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
		Document doc = Jsoup.parse(html, "utf-8"); 
		Element div= doc.getElementById("UpdatePanel4");
		//System.out.println(div);
		Element table1=div.select("table").first();
		String username=table1.select("font").get(0).text();
		System.out.println(username);
		String company=table1.select("font").get(2).text();
		System.out.println(company);
		String newaccount=table1.select("span").get(1).text();
		System.out.println(newaccount);
		String oldaccount=table1.select("font").get(3).text();
		System.out.println(oldaccount);
		String balance=table1.select("span").get(2).text();
		System.out.println(balance);
		
		Element table2=div.select("table").last();
		Elements trs=table2.select("tr");
		 int trs_size=trs.size();
		 if(trs_size>1){
			 for (int i = 1; i < trs_size; i++) {
			 Elements tds=trs.get(i).select("td");
			
			 String date=tds.get(0).text();
			 System.out.println(date);
			 String summy=tds.get(1).text();
			 System.out.println(summy);
			 String outmoney=tds.get(2).text();
			 System.out.println(outmoney);
			 String inMoney=tds.get(3).text();
			 System.out.println(inMoney);
			 String yumoney=tds.get(4).text();
			 System.out.println(yumoney);
			 System.out.println("--------------");
			 }
	}
}
}
