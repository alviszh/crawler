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
		 Element table2= doc.select("table").get(2);
		 Elements tds=table2.select("tr").select("td");
		 String username=tds.get(1).text();
		 String accountNum =tds.get(3).text();
		 String companyAccount=tds.get(5).text();		
		 String companyName=tds.get(7).text();
		 String idnum=tds.get(9).text();
		 String openTime=tds.get(11).text();
		  String accountState=tds.get(13).text();
			
		 String crrountBalance =tds.get(15).text();
		 System.out.println(crrountBalance);
		 String lastPaytime =tds.get(17).text();
		 System.out.println(lastPaytime);
		 String monthpay =tds.get(19).text();
		 System.out.println(monthpay);
		 String companyPay =tds.get(21).text();
		 System.out.println(companyPay);
		 String personPay =tds.get(23).text();
		 System.out.println(personPay);
		 String hasCard=tds.get(25).text();
		 System.out.println(hasCard);
		 String drawAmount=tds.get(27).text();	
		// System.out.println(drawAmount);
//		 Element table= doc.getElementById("prtable");
//		 Elements trs=table.select("tbody").select("tr");
//		 for (int i = 0; i < trs.size(); i++) {
//			 Elements tds = trs.get(i).select("td");
//			 String accountDate=tds.get(0).text();
//			 String accountAmount= tds.get(1).text();
//			 System.out.println(accountDate);
//			 System.out.println(accountAmount);
//		}
//		 
//		 Elements td= table.select("td");
//		 System.out.println(td);
//		String  a_text=td.last().getElementsByTag("a").last().attr("href");
//	    String[] ss=a_text.split("=");
//		System.out.println(ss[1]);

	}

}
